var list;
var editSsid;
var templateTypeId;
var templateId;

function getTmplateTypes() {
    var url=getActionURL(getactionid_manage().moreTemplate_getTemplateTypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callTmplateTypes);
}
function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var list=data.pagelist;
                if (isNotEmpty(list)) {
                    for (var i = 0; i < list.length; i++) {
                        var templateType = list[i];
                        $("#templatetypessid").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

function getTmplates_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().moreTemplate_getTemplates);
    var keyword =$("#templatename").val();
    var templateType = $("#templatetypessid").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            keyword: keyword,
            templatetypeid: parseInt(templateType),
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callTmplates2);
}
function getTmplates(keyword, templateType, currPage, pageSize) {
    var url=getActionURL(getactionid_manage().moreTemplate_getTemplates);
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            keyword: keyword,
            templateType: parseInt(templateType),
            currPage: currPage,
            pageSize: pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTmplates);
}
function callTmplates(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}
function callTmplates2(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            getTemplateById(0);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


/**
 * 局部刷新
 */
function getTmplateTypesParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 3;//测试
        getTmplates_init(currPage, pageSize);
    }  else if (len == 2) {
        getTmplates('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getTmplates(arguments[0], arguments[1], arguments[2], arguments[3]);
    }
}

function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var keyword =$("#templatename").val();
        var templateType = $("#templatetypessid").val();

        var arrparam=new Array();
        arrparam[0]=keyword;
        arrparam[1]=templateType;
        showpage("paging",arrparam,'getTmplateTypesParam',currPage,pageCount,pageSize);
    }
}

layui.use(['laypage', 'form', 'layer', 'layedit', 'laydate'], function(){
    var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
        ,form = layui.form
        ,layer = layui.layer
        ,layedit = layui.layedit
        ,laydate = layui.laydate
        ,laypage = layui.laypage;

    //不显示首页尾页
    laypage.render({
        elem: 'demo1'
        ,count: 100
        ,first: false
        ,last: false
    });
    form.render();
});

function getTemplateById(id) {
    if (pagelist[id]) {
        editSsid = pagelist[id].ssid;
        templateTypeId = pagelist[id].templatetypessid;
        templateId = pagelist[id].id;

        $('#templatetitle').html(pagelist[id].title);
        var templateToProblems = pagelist[id].templateToProblems;
        var templatedetail = '';
        $('#templatedetail').html(templatedetail);

        if (templateToProblems.length > 1) {
            for (var i = 0; i < templateToProblems.length; i++) {
                var Problem = templateToProblems[i];

                templatedetail += '<tr><td class="font_red_color">问：' + Problem.problem + '</td></tr>';
                templatedetail += '<tr><td class="font_blue_color">答：' + Problem.referanswer + '</td></tr>';
            }
            $('#templatedetail').html(templatedetail);
        }

    }
}