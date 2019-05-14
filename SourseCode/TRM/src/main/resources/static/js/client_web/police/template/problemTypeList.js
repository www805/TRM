var ssid;

function getProblemTypeList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().problemIndex_getProblemTypes);
    var keyword=$("input[name='keyword']").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            typename: keyword,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callProblemTypeList);
}

function getProblemTypeList(keyword, currPage, pageSize) {
    var url=getActionURL(getactionid_manage().problemIndex_getProblemTypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            typename: keyword,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url, data, callProblemTypeList);
}

function getProblemTypeById(ssidd) {
    setpageAction(INIT_CLIENT, "client_web/police/template/addOrupdateProblemType");
    var url=getActionURL(getactionid_manage().addOrupdateProblemType_getProblemTypeById);
    setpageAction(INIT_CLIENT, "client_web/police/template/problemIndex");
    ssid = ssidd;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url,data,callProblemTypeById);
}

function AddOrUpdateProblemType(version) {
    setpageAction(INIT_CLIENT, "client_web/police/template/addOrupdateProblemType");
    var url=getActionURL(getactionid_manage().addOrupdateProblemType_addProblemType);
    var typename=$("input[name='typename']").val();
    var ordernum=$("input[name='ordernum']").val();
    if (!isNotEmpty(version)) {
        //添加
        url=getActionURL(getactionid_manage().addOrupdateProblemType_updateProblemType);
    }
    setpageAction(INIT_CLIENT, "client_web/police/template/problemIndex");

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid,
            typename: typename,
            ordernum: parseInt(ordernum)
        }
    };
    ajaxSubmitByJson(url, data, callAddOrUpdate);
}

function callAddOrUpdate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (data.data.bool) {
                layer.msg("操作成功",{icon: 1});
            }else{
                layer.msg("操作失败",{icon: 2});
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callProblemTypeList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callProblemTypeById(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            opneModal_1(data.data.typename, data.data.ordernum);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getProblemTypeListParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 3;//测试
        getProblemTypeList_init(currPage, pageSize);
    }  else if (len == 2) {
        getProblemTypeList('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getProblemTypeList(arguments[0], arguments[1], arguments[2]);
    }
}

function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var keyword=$("input[name='keyword']").val();
        var arrparam=new Array();
        arrparam[0]=keyword;
        showpage("paging",arrparam,'getProblemTypeListParam',currPage,pageCount,pageSize);
    }
}

function opneModal_1(typename, ordernum) {

    if (!isNotEmpty(typename)){
        typename = "";
    }
    if (!isNotEmpty(ordernum)){
        ordernum = "0";
    }

    var html = '  <form class="layui-form site-inline" style="margin-top: 20px">\
               <div class="layui-form-item">\
                   <label class="layui-form-label">类型名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="typename" lay-verify="title" autocomplete="off" placeholder="请输入类型名称" value="' + typename + '" class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">排序</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="ordernum" lay-verify="title" autocomplete="off" placeholder="请输入排序" value="' + ordernum + '" class="layui-input">\
                    </div>\
                </div>\
            </form>';


    var index = layer.open({
        title: '问题类型编辑',
        content: html,
        area: ['500px', '300px'],
        btn: ['确定', '取消'],
        yes: function (index, layero) {
            layer.close(index);

            if (isNotEmpty(typename)){
                AddOrUpdateProblemType();//修改
            }else{
                AddOrUpdateProblemType(1);//新增
            }

        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}