var KeywordAction = null;

function updateShieldbool(ssid, shieldbool) {

    var url = getActionURL(getactionid_manage().getKeyword_updateShieldbool);
    var data = {
        ssid: ssid,
        shieldbool: shieldbool
    };

    ajaxSubmit(url,data,callupdateShieldbool);
}

function getKeyword_init(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().getKeyword_getKeywordList);
    var data={
        currPage:currPage,
        pageSize:pageSize
    };

    ajaxSubmit(url,data,callbackgetKeyWordPage);
}

function getKeyword_search(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().getKeyword_getKeywordList);
    var text=$("#kText").val();
    var data={
        text:text,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetKeyWordPage);
}

function getKeywordList(){

    var url = "";

    var ssid =$('input[name="ssid"]').val();
    var text =$('input[name="text"]').val();
    var replacetext =$('input[name="replacetext"]').val();
    var color =$('input[name="color"]').val();
    var backgroundcolor =$('input[name="backgroundcolor"]').val();
    var shieldbool =$('#shieldbool').prop("checked");

    if (shieldbool) {
        shieldbool = 1;
    }else{
        shieldbool = -1;
    }

    var data={
        ssid:ssid,
        text:text,
        replacetext:replacetext,
        color:color,
        backgroundcolor:backgroundcolor,
        shieldbool:shieldbool
    };

    if(ssid){
        url = getActionURL(getactionid_manage().addOrUpdateKeyword_getAddOrUpdateKeyword) + "/" + ssid;
    }else{
        url = getActionURL(getactionid_manage().addOrUpdateKeyword_getAddOrUpdateKeyword);
    }

    ajaxSubmit(url,data,calladdOrUpdataKeyWordPage);
}

/**
 * 带参数的
 * @cmparam text
 * @cmparam currPage
 * @cmparam pageSize
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


function deleteKeyword(ssid){

    var index_Keyword = layer.confirm('你确定要删除关键字？', {
        btn: ['确定','取消'] //按钮
    }, function(){

        layer.close(index_Keyword);
        KeywordAction = getAction(getactionid_manage().getKeyword_deleteKeyword);
        var data={
            ssid:ssid
        };
        ajaxSubmit(KeywordAction.reqURL,data,callDeleteKeyword);

    }, function(){
        return;
    });

}

function callupdateShieldbool(data) {

    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg(data.message,{icon:6});
    }else{
        // alert(data.message);
        layer.msg(data.message,{icon: 5});
    }
}

function callbackgetKeyWordPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        // alert(data.message);
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();

        //监听指定开关
        form.on('switch(switchTest)', function(data){
            var shieldbool = -1;
            if(this.checked){
                shieldbool = 1;
            }
            updateShieldbool(data.value, shieldbool);
        });

    });
}

function calladdOrUpdataKeyWordPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){

        // setpageAction(init_web,"police/keyword/getKeyword");
        //
        // var url=getActionURL(getactionid_manage().addOrUpdateKeyword_getKeyword);
        // window.location.href=url;
        window.history.go(-1);return false;
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