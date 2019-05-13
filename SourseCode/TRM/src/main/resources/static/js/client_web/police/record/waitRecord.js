var templatessid=null;//模板ssid
function opneModal_1() {
    var url=getActionURL(getactionid_manage().waitRecord_tomoreTemplate);

    var index = layer.open({
        type: 2,
        title:'变更模板',
        content:url,
        area: ['1000px', '680px'],
        btn: ['应用模板', '设置为默认模板'],
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
                        var html="<tr> <td>问：<span ondblclick='copy_problems(this);'>"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);
                    }
                }
              }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function copy_problems(obj) {
    var text=$(obj).text();
    $("td:focus").html(text);
}

function random(lower, upper) {
    return Math.floor(Math.random() * (upper - lower)) + lower;
}
function openxthtml() {
    layer.confirm('确定要启动情绪分析吗', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
       $("#xthtml").css("display","block");

       var strings=["高度紧张","中度紧张","不很紧张","平平淡淡"];
       var ran=random(1,100);
       var t = setInterval(function (args) {
           $("#xthtml #xt1").html('<i class="layui-icon">&#xe67a;</i>'+strings[Math.floor(Math.random()*strings.length)]+'');
           $("#xthtml #xt2").html('<i class="layui-icon">&#xe6af;</i> '+random(1,100)+'');
           $("#xthtml #xt3").html('<i class="layui-icon">&#xe69c;</i> '+random(1,100)+'');
           $("#xthtml #xt4").html('<i class="layui-icon">&#xe6fc;</i> '+random(1,100)+'/分');
           $("#xthtml #xt5").html('<i class="layui-icon">&#xe62c;</i>'+random(1,100)+' mmHg');
       }, 1000);

        layer.close(index);
    }, function(index){
        layer.close(index);
    });

    
}

//录音按钮显示隐藏
function img_bool(obj){
    var recordbool=$(obj).attr("recordbool");
    if (recordbool=="true"){
        $(obj).css("display","none").attr("recordbool","false");
        $(obj).siblings().css("display","block");
    }else{
        $(obj).css("display","none").attr("recordbool","true");
        $(obj).siblings().css("display","block");
    }
}

