function opneModal_1(id) {
    getPidRecordtypes();
    var html='  <form class="layui-form site-inline" style="margin-top: 20px">\
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
                    <input type="text" name="typenamem" id="typenamem" lay-verify="title" autocomplete="off" placeholder="请输入类型名称" class="layui-input">\
                    </div>\
                </div>\
            </form>';

    var index = layer.open({
        title:'笔录类型编辑',
        content: html,
        area: ['500px', '300px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            if (isNotEmpty(id)) {
                var url=getActionURL(getactionid_manage().recordTypeList_updateRecordtype);
                var pidm=$("#pidm option:selected").val();
                var typenamem=$("#typenamem").val();
                var data={
                   token:INIT_CLIENTKEY,
                   param:{
                       id:id,
                       pid:pidm,
                       typename:typenamem
                   }
                };
                ajaxSubmitByJson(url,data,callbackaddOrUpdateRecordtype);
            } else {
                var url=getActionURL(getactionid_manage().recordTypeList_addRecordtype);
                var pidm=$("#pidm option:selected").val();
                var typenamem=$("#typenamem").val();
                var data={
                    token:INIT_CLIENTKEY,
                    param: {
                        pid: pidm,
                        typename: typenamem
                    }
                };
                ajaxSubmitByJson(url,data,callbackaddOrUpdateRecordtype);
            }
            layer.close(index);
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
            layer.msg("保存成功");
            getRecordtypes();
        }
    }else{
        layer.msg(data.message);
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
        }
        layui.use('form', function(){
            var form =  layui.form;
            form.render();
        });
    }else{
        layer.msg(data.message);
    }
}


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
            console.log(data);
            var getRecordtypesVOParamList=data.getRecordtypesVOParamList;
            var recordtypehtml=$("#recordtypehtml").html("");
            gets(getRecordtypesVOParamList);
        }
    }
}


var len="----";
var num=$("#recordtypehtml tr").length;
function gets(data) {
    if (isNotEmpty(data)){
        for (var i = 0; i < data.length; i++) {
            var l = data[i];
            num++;
            var html='<tr>\
                                    <td>'+num+'</td>\
                                    <td style="text-align: left;color: #000000;font-weight: bold;">|'+len+' '+l.typename+'</td>\
                                    <td>'+l.createtime+'</td>\
                                    <td>\
                                        <a  class="layui-btn layui-btn-normal layui-btn-sm" onclick="opneModal_1('+l.id+');">修改</a>\
                                    </td>\
                                </tr>';
            $("#recordtypehtml").append(html);
            if (l.police_recordtypes.length>0){
                len+=len;
                gets(l.police_recordtypes);
                 len="----";
            }
        }
    }
}


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
        layer.msg(data.message);
    }
}