
function getKeywordById(ssid) {

    // var url = getActionURL(getactionid_manage().getKeyword_updateShieldbool);
    var url = "/sweb/keyword/getKeywordById";

    var data = {
        ssid: ssid
    };

    console.log(ssid);
    // ajaxSubmit(url,data,callgetKeywordById);
}

function callgetKeywordById(data) {

    if(null!=data&&data.actioncode=='SUCCESS'){

        console.log(data);

        layer.msg(data.message,{icon: 6});
    }else{
        // alert(data.message);
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

/**
 * 局部刷新
 */
function getKeyWordByParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 10;//测试
        getKeyword_init(currPage, pageSize);
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