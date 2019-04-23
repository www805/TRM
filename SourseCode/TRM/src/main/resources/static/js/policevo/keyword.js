var KeywordAction = null;

function getAddOrUpdateKeyword(){

    var url = "";

    var id =$('input[name="id"]').val();
    var text =$('input[name="text"]').val();
    var replacetext =$('input[name="replacetext"]').val();
    var color =$('input[name="color"]').val();
    var backgroundcolor =$('input[name="backgroundcolor"]').val();
    var shieldbool =$('input[name="shieldbool"]').val();

    console.log(shieldbool);
    console.log($("div[class='.layui-form-onswitch']"));


    if (shieldbool == "") {
        shieldbool = -1;
    }else{
        shieldbool = 1;
    }

    var data={
        id:id,
        text:text,
        replacetext:replacetext,
        color:color,
        backgroundcolor:backgroundcolor,
        shieldbool:shieldbool
    };

    if(id){
        url = getActionURL(getactionid_manage().addOrUpdateKeyword_getAddOrUpdateKeyword) + "/" + id;
    }else{
        url = getActionURL(getactionid_manage().addOrUpdateKeyword_getAddOrUpdateKeyword);
    }

    //ajaxSubmit(url,data,calladdOrUpdataKeyWordPage);
}

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


function deleteKeyword(id){

    var aa = layer.confirm('你确定要删除关键字？', {
        btn: ['确定','取消'] //按钮
    }, function(){

        layer.close(aa);
        KeywordAction = getAction(getactionid_manage().getKeyword_deleteKeyword);
        var data={
            id:id
        };
        ajaxSubmit(KeywordAction.reqURL,data,callDeleteKeyword);

    }, function(){
        return;
    });

}

function callbackgetKeyWordPage(data){

    $("#listqk").html("");

    if(null!=data&&data.actioncode=='SUCCESS'){
        pageshow(data);
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:5});
    }
}

function calladdOrUpdataKeyWordPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){

        setpageAction(init_web,"police/keyword/getKeyword");

        var url=getActionURL(getactionid_manage().addOrUpdateKeyword_getKeyword);
        window.location.href=url;
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:5});
    }
}

function callDeleteKeyword(data){

    if(null!=data&&data.actioncode=='SUCCESS'){

        if (KeywordAction.gotopageOrRefresh == 1) {

            setpageAction(init_web,KeywordAction.nextPageId);

            var url=getActionURL(getactionid_manage().main_getKeyword);
            window.location.href=url;
        }

    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:5});
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