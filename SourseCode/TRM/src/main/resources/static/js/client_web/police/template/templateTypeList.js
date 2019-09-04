var ssid;

function getTemplateTypeList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().templateTypeList_getTemplateTypes);
    var keyword=$("input[name='keyword']").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            typename: keyword,
            currPage:currPage,
            pageSize:pageSize
        }
    };

    ajaxSubmitByJson(url,data,callTemplateTypeList);
}

function getTemplateTypeList(keyword, currPage, pageSize) {
    var url=getActionURL(getactionid_manage().templateTypeList_getTemplateTypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            typename: keyword,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTemplateTypeList);
}

function getTemplateTypeById(ssidd) {
    var url=getActionURL(getactionid_manage().templateTypeList_getTemplateTypeById);
    ssid = ssidd;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url,data,callTemplateTypeById);
}

function AddOrUpdateTemplateType(version) {
    var url=getActionURL(getactionid_manage().templateTypeList_updateTemplateType);
    var typename=$("input[name='typename']").val();
    var ordernum=$("input[name='ordernum']").val();
    if (isNotEmpty(version)) {
        //添加
        url=getActionURL(getactionid_manage().templateTypeList_addTemplateType);
    }

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
                layer.msg("操作成功",{icon: 6});
            }else{
                layer.msg("操作失败",{icon: 5});
            }
            setTimeout("window.location.reload()",1500);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callTemplateTypeList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            var listcountsize = data.data.pageparam.recordCount;
            if (listcountsize == 0) {
                $("#wushuju").show();
                $("#pagelistview").hide();
            } else {
                $("#wushuju").hide();
                $("#pagelistview").show();
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callTemplateTypeById(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            opneModal_1(data.data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

/**
 * 局部刷新
 */
function getTemplateTypeListParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 10;//测试
        getTemplateTypeList_init(currPage, pageSize);
    }  else if (len == 2) {
        getTemplateTypeList('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getTemplateTypeList(arguments[0], arguments[1], arguments[2]);
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
        showpage("paging",arrparam,'getTemplateTypeListParam',currPage,pageCount,pageSize);
    }
}

function opneModal_1(problem) {

    var typename = "";
    var ordernum = "0";

    if(isNotEmpty(problem)) {
        typename = problem.typename;
        ordernum = problem.ordernum;
    }

    var html='  <form class="layui-form site-inline" style="margin-top: 20px;padding-right: 35px;">\
               <div class="layui-form-item">\
                   <label class="layui-form-label"><span style="color: red;">*</span>类型名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="typename" lay-verify="typename" autocomplete="off" placeholder="请输入类型名称" value="' + typename + '"  class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label"><span style="color: red;">*</span>排序</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="ordernum" lay-verify="ordernum" autocomplete="off" placeholder="请输入排序" value="' + ordernum + '"  class="layui-input">\
                    </div>\
                </div>\
            </form>';

    layui.use('form', function(){
        var form = layui.form;
        var index = layer.open({
            type: 1,
            title:'模板类型编辑',
            content: html,
            area: ['500px', '300px'],
            btn: ['确定', '取消'],
            success: function (layero, index) {
                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();
            },
            yes:function(index, layero){
                //自定义验证规则
                form.verify({
                    typename:[/\S/,'请输入模板类型名称'], ordernum: [/\S/,'请输入模板排序号']
                });
                //监听提交
                form.on('submit(fromContent)', function(data){
                    if (isNotEmpty(typename)){
                        AddOrUpdateTemplateType();//修改
                    }else{
                        AddOrUpdateTemplateType(1);//新增
                    }
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
    });
}

