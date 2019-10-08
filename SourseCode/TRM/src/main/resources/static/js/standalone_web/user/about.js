var runbookdownurl=null;//下载地址

function getAbout() {
    var url=getActionURL(getactionid_manage().about_getAbout);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetAbout);
}

function callbackgetAbout(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            runbookdownurl=data.runbookdownurl;
           $("#runbookdownurl_html").attr("src",data.runbookdownurl_html);
            $("#sysmsg").html(data.sysmsg==null?"无":data.sysmsg);
            $("#companymsg").html(data.companymsg==null?"无":data.companymsg);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}



$(function () {
    $("#abouthtml div").click(function () {
        $(this).css({"background-color":"#1E9FFF"}).siblings().css({"background-color":"#000000"})
    });
    
    $("#downrunbook").click(function () {
        if (isNotEmpty(runbookdownurl)){
            layer.msg("下载中，请稍后...",{icon: 6});
            window.location.href = runbookdownurl;
        } else {
            layer.msg("未找到下载地址",{icon: 5});
        }
    });
});


