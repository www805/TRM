var templatessid=null;//模板ssid
//跳转变更模板页面
function opneModal_1() {
    var url=getActionURL(getactionid_manage().waitRecord_tomoreTemplate);

    var index = layer.open({
        type: 2,
        title:'变更模板',
        content:url,
        area: ['1000px', '680px'],
        btn: ['应用模板'],
        yes:function(index, layero){
           var editSsid = $(window.frames["layui-layer-iframe"+index])[0].editSsid;
            templatessid=editSsid;
            getTemplateById();
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

//变更模板题目
function getTemplateById() {
    if (isNotEmpty(templatessid)){
        var url=getActionURL(getactionid_manage().waitRecord_getTemplateById);

        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: templatessid
            }
        };
        ajaxSubmitByJson(url, data, callTemplateById);
    }
}
function callTemplateById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log(data);
            var template=data.template;
            if (isNotEmpty(template)){
                var templateToProblems=template.templateToProblems;
               $("#templatetoproblem_html").html("");
                if (isNotEmpty(templateToProblems)) {
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);
                    }
                }
              }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


var td_lastindex;//td的上一个光标位置
function copy_problems(obj) {
    var text=$(obj).find("span").text();
    var html=' <tr><td class="font_red_color" contenteditable="true">问：'+text+'</td></tr>\
                <tr><td class="font_blue_color"contenteditable="true">答：</td></tr>';
    $("#recorddetail").append(html);
   $("#recorddetail .font_blue_color").focus(function(){
       /* $(this).append(copy_text_html);
        copy_text_html="";*/
       td_lastindex=$(this).parent().index();
    });
}

function random(lower, upper) {
    return Math.floor(Math.random() * (upper - lower)) + lower;
}
function openxthtml(obj) {
    var xtbool=$(obj).attr("xtbool");
    if (xtbool==1){
        layer.msg("情绪分析已开启");
        return;
    }else{
        layer.confirm('确定要启动情绪分析吗', {
            btn: ['确认','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            $("#xthtml").css("display","block");
            $(obj).attr("xtbool","1");
            var strings=["高度紧张","中度紧张","不很紧张","平平淡淡"];
            var ran=random(1,100);
            var t = setInterval(function (args) {
                $("#xthtml #xt1").html('<i class="layui-icon">&#xe67a;</i>'+strings[Math.floor(Math.random()*strings.length)]+'  ');
                $("#xthtml #xt2").html('<i class="layui-icon">&#xe6af;</i> '+random(1,100)+' ');
                $("#xthtml #xt3").html('<i class="layui-icon">&#xe69c;</i> '+random(1,100)+' ');
                $("#xthtml #xt4").html('<i class="layui-icon">&#xe6fc;</i> '+random(1,100)+'/分  ');
                $("#xthtml #xt5").html('<i class="layui-icon">&#xe62c;</i>'+random(1,100)+'mmHg  ');
            },2000);

            layer.close(index);
        }, function(index){
            layer.close(index);
        });
    }


    
}

//录音按钮显示隐藏
var t;
function img_bool(obj,type){
    var img_bool=$(obj).attr("img_bool");
    if (img_bool=="true"){
        $(obj).css("display","none").attr("img_bool","false");
        $(obj).siblings().css("display","block");
    }else{
        $(obj).css("display","none").attr("img_bool","true");
        $(obj).siblings().css("display","block");
    }

    if (type==1){
        //实时会议数据
        var recordtype=1;
        var username="未知";
        var translatext="未知";
       t = setInterval(function (args) {
          /* $("#recordreals").html("");*/
            if (recordtype==1){
                recordrealclass="atalk";
                username="检察官";
                translatext="我是检察官，现在开始考察你";
                recordtype=2;
            }else if (recordtype==2){
                recordrealclass="btalk";
                username="被询问人";
                translatext="我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察"
                recordtype=1;
            }
            var recordrealshtml='<div class="'+recordrealclass+'" >\
                                                        <p>【'+username+'】 2019-4-14 02:24:55</p>\
                                                        <span ondblclick="copy_text(this)">'+translatext+'</span> \
                                                  </div >';

            $("#recordreals").append(recordrealshtml);
        },1000);
    }else{
        clearInterval(t);
    }
}
var copy_text_html="";
function copy_text(obj) {
    var text=$(obj).text();
    copy_text_html=text;

    $("#recorddetail .font_blue_color").each(function(){
        if (isNotEmpty(td_lastindex)){
            var lastindex=$(this).parent().index();
            if (lastindex==td_lastindex) {
                $(this).append(copy_text_html);
                copy_text_html="";
                td_lastindex=null;
            }
        }
    });
}


