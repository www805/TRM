

/**
 * 带参数的
 * @param text
 * @param currPage
 * @param pageSize
 */
function getKeyWordPage(text,currPage,pageSize){

    var url=getActionURL(getactionid_manage().getKeyword_getKeywordList);
    var data={
        text:text,
        currPage:currPage,
        pageSize:pageSize
    };

    //加载层-风格4
    var index = layer.msg('加载中', {
        icon: 16
        ,shade: 0.01
        ,time: 6000, //20s后自动关闭
    });

    ajaxSubmit(url,data,callbackgetKeyWordPage);

    layer.close(index);
}


function callbackgetKeyWordPage(data){
    //
    // console.log("=====================");
    // console.log(data);
    $("#listqk").html("");

    if(null!=data&&data.actioncode=='SUCCESS'){
        pageshow(data);
    }else{
        //parent.layer.msg(data.message,{icon: 2},1);
        alert(data.message);
    }

}

/**
 * 局部刷新
 */
function getKeyWordByParam() {

    var len = arguments.length;

    var currPage = 1;
    var pageSize = 3;//测试
    var text = $(" input[ name='text' ] ").val();


    if (len == 0) {
        getKeyWordPage(text, currPage, pageSize);
    }  else if (len == 2) {
        getKeyWordPage('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getKeyWordPage(arguments[0], arguments[1], arguments[2]);
    }

}


function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;
        var text=$("input[name='text']").val();
        var arrparam=new Array();
        arrparam[0]=text;
        showpage("paging",arrparam,'getKeyWordByParam',currPage,pageCount,pageSize);
    }


}