
function getabout(){
  /*  var url=null;
    var data={
    };
    ajaxSubmitByJson(url,data,callbackgetabout);*/
}
function callbackgetabout(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

$(function () {
    $(".div_about div").click(function () {
        $(this).css({"background-color":" #1E9FFF"}).siblings().css({"background-color":" #ffffff"});
        $(".div_about a").css({"color":" #000000"});
        $("a",this).css({"color":" #ffffff"});
    });
})