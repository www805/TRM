var recordtypes_son=null;//子集个数
var oldpidm=null;//原有的pidm

function opneModal_1(id) {
    recordtypes_son=null;
    oldpidm=null;
    getPidRecordtypes();
    var html=  '<form class="layui-form layui-form-pane site-inline"  style="margin: 30px;">\
               <div class="layui-form-item">\
                   <label class="layui-form-label">所属类型</label>\
                    <div class="layui-input-block">\
                           <select name="pidm" lay-verify="required" id="pidm" >\
                           <option value="0" >最高类目</option>\
                            </select>\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">类型名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="typenamem" id="typenamem" lay-verify="required" autocomplete="off" placeholder="请输入类型名称" class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">排序</label>\
                    <div class="layui-input-block">\
                    <input type="num" name="ordernumm" id="ordernumm" lay-verify="required" autocomplete="off" placeholder="请输入排序" class="layui-input" value="1">\
                    </div>\
                </div>\
            </form>';

    var index = layer.open({
        type:1,
        title:'笔录类型编辑',
        content: html,
        area: ['500px', '300px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            var pidm=$("#pidm option:selected").val();
            var typenamem=$("#typenamem").val();
            var typenamem=$("#typenamem").val();
            var ordernum=$("#ordernumm").val();
            if(!isNotEmpty(typenamem)){
                layer.msg("请输入类型名称");
                $("#typenamem").focus();
                return;
            }
            if (isNotEmpty(id)) {
                var url=getActionURL(getactionid_manage().recordTypeList_updateRecordtype);
                var data={
                    token:INIT_CLIENTKEY,
                    param:{
                        id:id,
                        pid:pidm,
                        ordernum:ordernum,
                        typename:typenamem
                    }
                };
                if (oldpidm==0&&pidm!=0&&recordtypes_son>0){
                    layer.confirm('该类目下的子类目将会变成同级，是否保存？', {
                        btn: ['保存','取消'] //按钮
                    }, function(){
                        ajaxSubmitByJson(url,data,callbackaddOrUpdateRecordtype);
                        layer.close(index);
                    }, function(){

                    });
                }else {
                    ajaxSubmitByJson(url,data,callbackaddOrUpdateRecordtype);
                    layer.close(index);
                }
            } else {
                var url=getActionURL(getactionid_manage().recordTypeList_addRecordtype);
                var data={
                    token:INIT_CLIENTKEY,
                    param: {
                        pid: pidm,
                        ordernum:ordernum,
                        typename: typenamem
                    }
                };
                ajaxSubmitByJson(url,data,callbackaddOrUpdateRecordtype);
                layer.close(index);
            }
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });

    if (null!=id){
        var url=getActionURL(getactionid_manage().recordTypeList_getRecordtypeById);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                id:id
            }
        };
        ajaxSubmitByJson(url,data,callbackgetRecordtypeById);
    }


}
function callbackaddOrUpdateRecordtype(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data = data.data;
        if (isNotEmpty(data)) {
            layer.msg("保存成功",{icon:6});
            getRecordtypes();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
function callbackgetRecordtypeById(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data = data.data;
        if (isNotEmpty(data)) {
            $("#pidm").find("option[value='"+data.pid+"']").attr("selected",true);
            $("#pidm option").each(function(){ //遍历全部option
                var v = $(this).val();
                if (v==data.id) {
                    $("#pidm").find("option[value='"+v+"']").remove();
                }
            });
            $("#typenamem").val(data.typename);
            $("#ordernumm").val(data.ordernum);

            oldpidm=data.pid;
            recordtypes_son=0;
            var recordtypes=data.recordtypes;
            if (isNotEmpty(recordtypes)){
                recordtypes_son=recordtypes.length;
            }
        }
        layui.use('form', function(){
            var form =  layui.form;
            form.render();
        });
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//获取类型列表
function getRecordtypes() {
    var url=getActionURL(getactionid_manage().recordTypeList_getRecordtypes);
    var pid=$("#pid option:selected").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            pid:pid
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordtypes);
}
function callbackgetRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var getRecordtypesVOParamList=data.getRecordtypesVOParamList;
            var recordtypehtml=$("#recordtypehtml").html("");
            num=$("#recordtypehtml tr").length;
            gets(getRecordtypesVOParamList);
        }
    }
}

//随机颜色
function getRandomColor() {
    return '#' +
        (function(color) {
            return (color += '0123456789abcdef' [Math.floor(Math.random() * 16)]) && (color.length == 6) ? color : arguments.callee(color);
        })('');
}

var len="----";
var color='red';
var num=$("#recordtypehtml tr").length;
function gets(data) {
    var color2='blue';
    if (isNotEmpty(data)){
        for (var i = 0; i < data.length; i++) {
            var l = data[i];
            num++;
            var html='<tr>\
                                     <td>'+num+'</td>\
                                     <td style="text-align: left;color: '+color+';font-weight: bold;">|'+len+' '+l.typename+'</td>\
                                     <td>'+l.createtime+'</td>\
                                     <td>'+l.ordernum+'</td>\
                                    <td>\
                                        <a  class="layui-btn layui-btn-normal layui-btn-sm" onclick="opneModal_1('+l.id+');">修改</a>\
                                    </td>\
                                </tr>';
            $("#recordtypehtml").append(html);
            if (l.police_recordtypes.length>0){
                len+=len;
                color=color2;
                gets(l.police_recordtypes);
                len="----";
                color='red';
            }
        }
    }
}

//获取pid=0的
function getPidRecordtypes() {
    var url=getActionURL(getactionid_manage().recordTypeList_getPidRecordtypes);
    var pid=$("#pid option:selected").val();

    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetPidRecordtypes);
}
function callbackgetPidRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data = data.data;
        $('#pid option').not(":lt(1)").remove();
        $("#pidm").not(":lt(1)").remove();
        if (isNotEmpty(data)) {
            if (isNotEmpty(data)){
                for (var i = 0; i < data.length; i++) {
                    var recordtypes = data[i];
                    $("#pid").append("<option value='"+recordtypes.id+"'>|---- "+recordtypes.typename+"</option>");
                    $("#pidm").append("<option value='"+recordtypes.id+"'>|---- "+recordtypes.typename+"</option>");
                }
            }
        }
        layui.use('form', function(){
            var form =  layui.form;
            form.render();
        });
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

$(function () {
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;
        form.on('select(change_pid)', function(data){
            getRecordtypes();
        });
    });

})