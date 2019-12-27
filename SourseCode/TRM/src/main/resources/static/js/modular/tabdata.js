
//身心检测
function initph() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//语音识别
function initasr() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//案件
function initcase() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//情绪报告
function initmood() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}




//*******************************************************************左侧搜索块start****************************************************************//
var dqindex_realtxt=0;//当前显示的下标
var likerealtxtarr=[];//搜索txt
//搜索上
function last_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt--;
        if (dqindex_realtxt<0){
            dqindex_realtxt=0;
            layer.msg("这是第一个~");
        }
        set_dqrealtxt();
    }
}
//搜索下
function next_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt++;
        if (dqindex_realtxt>=likerealtxtarr.length-1){
            dqindex_realtxt=likerealtxtarr.length-1;
            layer.msg("这是最后一个~");
        }
        set_dqrealtxt();
    }
}
//搜索赋值
function set_dqrealtxt(){
    mouseoverbool_left=1;//不滚动
    if (isNotEmpty(likerealtxtarr)) {
        for (var i = 0; i < likerealtxtarr.length; i++) {
            var all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        var div = document.getElementById('recordreals_scrollhtml');
        div.scrollTop = top;
    }
}
function recordreals_select(obj) {
    mouseoverbool_left=1;//不滚动
    dqindex_realtxt=0;
    likerealtxtarr=[];


    var recordrealshtml= $("#recordreals").html();
    if (isNotEmpty(recordrealshtml)){
        recordrealshtml=recordrealshtml.replace(/(<\/?a.*?>)/g, '');//过滤之前的highlight_all标签
        $("#recordreals").html(recordrealshtml);
    }

    var likerealtxt = $(obj).val();
    if (isNotEmpty(likerealtxt)){
        $("#recordreals div").each(function (i,e) {
            var spantxt=$(this).find("span").text();
                if (spantxt.indexOf(likerealtxt) >= 0) {
                    var html=$(this).find("span").html();
                    html = html.split(likerealtxt).join('<a class="highlight_all">'+ likerealtxt +'</a>');
                    $(this).find("span").html(html);
                    likerealtxtarr.push($(this).find("span"));
                }
        });
    }


    if (isNotEmpty(likerealtxtarr)){
        set_dqrealtxt();
    }else {
        /*layer.msg("没有找到内容~");*/
    }
}
//*******************************************************************左侧搜索块end****************************************************************//

//*******************************************************************案件人员信息编辑start****************************************************************//
var casetouser_iframe=null;
var casetouser_body=null;
function  open_casetouser() {
    layer.open({
        type: 2,
        title:'人员案件基本信息',
        content:tocaseToUserURL,
        area: ['80%', '90%'],
        btn: ['确定','取消'],
        success:function(layero, index){
            casetouser_iframe = window['layui-layer-iframe' + index];
            casetouser_body=layer.getChildFrame('body', index);
            if (isNotEmpty(recordssid)&&isNotEmpty(getRecordById_data)) {
                casetouser_iframe.recordssid=recordssid;
                casetouser_iframe.setcaseToUser(getRecordById_data);
            }else {
                console.log("案件人员信息编辑参数recordssid："+recordssid+"__getRecordById_data："+getRecordById_data);
            }
        },
        yes:function(index, layero){
            var formSubmit=layer.getChildFrame('body', index);
            var submited = formSubmit.find('#permissionSubmit')[0];
            submited.click();
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}
//*******************************************************************案件人员信息编辑end****************************************************************//

//*******************************************************************修改定位时间start****************************************************************//
var open_positiontime_index=null;
var positiontime=0;
function open_positiontime(url) {
    var html='  <form class="layui-form site-inline" style="margin-top: 20px;padding-right: 35px;">\
               <div class="layui-form-item">\
                   <label class="layui-form-label"><span style="color: red;">*</span>定位差值</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="positiontimem" id="positiontimem" lay-verify="positiontimem" autocomplete="off" placeholder="请输入定位差值(秒)" value="' + parseFloat(positiontime)/1000 + '"  class="layui-input">\
                    </div>\
                     <div class="layui-form-mid layui-word-aux" style="float: right;margin-right: 0px">请输入差值在-10到-1或者1到10区间的值以及0(秒)</div>\
                </div>\
            </form>';
    layui.use(['layer','element','form','laydate'], function() {
        var form = layui.form;
        open_positiontime_index=layer.open({
            type:1,
            title:'编辑定位差值',
            content:html,
            area: ['25%', '30%'],
            btn: ['确定','取消'],
            success:function(layero, index){
                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();
            },
            yes:function(index, layero){
                //自定义验证规则
                form.verify({
                    positiontimem:function (value) {
                        if (!(/\S/).test(value)) {
                            return "请输入定位差值";
                        }
                        if (!((value<=-1&&value>=-10)||(value<=10&&value>=1)||value==0)) {
                            return "请输入差值在-10到-1或者1到10区间的值以及0(秒)";
                        }
                    }
                });
                //监听提交
                form.on('submit(fromContent)', function(data){
                    updateRecord(url);
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
    });
}
function updateRecord(url){
    var positiontime=$("#positiontimem").val();
    if (!isNotEmpty(positiontime)){
        layer.msg("请输入定位差值",{icon:5});
        return;
    }

    positiontime=parseFloat(positiontime)*1000;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: recordssid,
            positiontime:positiontime
        }
    };
    ajaxSubmitByJson(url, data, callbackupdateRecord);
}
function callbackupdateRecord(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var param=data.param;
            if (isNotEmpty(param)){
                layer.close(open_positiontime_index);
                positiontime=param.positiontime;//更新值
                layer.msg("保存成功",{icon:6,time:500},function () {
                    $("#positiontime").val(parseFloat(positiontime)/1000);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//*******************************************************************修改定位时间start****************************************************************//
