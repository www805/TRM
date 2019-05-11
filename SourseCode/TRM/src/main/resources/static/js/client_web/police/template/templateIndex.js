var token = "4843484445444848454842464648464A424549474A4A45";
var list;
var editSsid;
var templateTypeId;

function getTmplates_init(currPage,pageSize) {
    // var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var url = "/cweb/police/template/getTemplates";
    var keyword =$("#keyword").val();
    var templatetypeid = $("#templateType").val();
    var data={
        token:token,
        param:{
            keyword: keyword,
            templatetypeid: parseInt(templatetypeid),
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callTmplates2);
}

function getTmplates(keyword, templateType, currPage, pageSize) {
    // var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var url = "/cweb/police/template/getTemplates";

    var data = {
        token: token,
        param: {
            keyword: keyword,
            templateType: templateType,
            currPage: currPage,
            pageSize: pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTmplates);
}

//获取模板类型
function getTmplateTypes() {
    // var url=getActionURL(getactionid_manage().getUserList_getRoles);
    var url = "/cweb/police/template/getTemplateTypes";
    var data={
        token:token,
        param:{}
    };
    ajaxSubmitByJson(url,data,callTmplateTypes);
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

function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            list = data.data.templatetypes;

            if (isNotEmpty(list)) {
                for (var i = 0; i < list.length; i++) {
                    var templateType = list[i];
                    $("#templateType").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
                }
            }
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
        var pageSize = 10;//测试
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

        var keyword=$("input[name='keyword']").val();
        var templateType = $("#templateType").val();
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
        // templateTypeId = $("#templateTitle").val();
        templateTypeId = pagelist[id].templatetypessid;

        $('#templateTitle').html(pagelist[id].title);
        var templateToProblems = pagelist[id].templateToProblems;
        var tableProblems = '';
        $('#tableProblems').html(tableProblems);

        if (templateToProblems.length > 1) {
            for (var i = 0; i < templateToProblems.length; i++) {
                var Problem = templateToProblems[i];

                tableProblems += '<tr><td class="font_red_color">问：' + Problem.problem + '</td></tr>';
                tableProblems += '<tr><td class="font_blue_color">答：' + Problem.referanswer + '</td></tr>';
            }
            $('#tableProblems').html(tableProblems);
        }

    }
}