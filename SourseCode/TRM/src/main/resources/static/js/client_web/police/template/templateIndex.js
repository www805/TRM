var list;
var editSsid;
var templateTypeId;
var templateId;
var templatetypeidSSID;

function getTmplates_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().templateIndex_getTemplates);
    var keyword =$("#keyword").val();
    var templatetypeid = $("#templateTypes").val();
    if(!templatetypeid){
        templatetypeid = -1;
    }

    var data={
        token:INIT_CLIENTKEY,
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
    var url=getActionURL(getactionid_manage().templateIndex_getTemplates);

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            keyword: keyword,
            templatetypeid: parseInt(templateType),
            currPage: currPage,
            pageSize: pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTmplates);
}

function eitdTemplateYe() {

    if(editSsid){
        window.location.href = 'toaddOrupdateTemplate?ssid=' + editSsid + '&type='+templateTypeId +'&templateId='+templateId
    }else{
        layer.msg("当前没有模板，不能编辑",{icon: 2});
    }

}

//获取模板类型
function getTmplateTypess() {
    var url=getActionURL(getactionid_manage().templateIndex_getTemplateTypes);
    var data={
        token:INIT_CLIENTKEY,
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
            var listAll = data.data.pagelist;

            list = listAll;
            for (var i = 0; i < listAll.length; i++) {
                var templateType = listAll[i];

                if(i == 0){
                    templatetypeidSSID = templateType.id;
                }
                $("#templateTypes").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


/**
 * 局部刷新
 */
function getTmplateTypesParams() {

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
        var templateType = $("#templateTypes").val();
        var arrparam=new Array();
        arrparam[0]=keyword;
        arrparam[1]=templateType;
        showpage("paging",arrparam,'getTmplateTypesParams',currPage,pageCount,pageSize);
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

function btn(obj) {
    var selected = $(obj).closest("div[name='btn_div']").attr("class");
    if (selected.indexOf("layui-form-selected") == -1) {
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else{
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}


//导出word
function exportWord(obj){
    var url=getActionURL(getactionid_manage().templateIndex_exportWord);
    // var url = "/cweb/police/template/exportWord";
    var data={
        token:INIT_CLIENTKEY,
        param:{
            templatessid: editSsid,
        }
    };

    // console.log(url);
    ajaxSubmitByJson(url, data, function (data) {
        // console.log(data);
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                // var host = "http://localhost";
                var host = "http://" + window.location.host;
                host = host.replace(":8080","");
                window.location.href = host + data;
                // window.open(host + data);
                layer.msg("导出成功,等待下载中...");
            }
        }else{
            layer.msg("导出失败");
        }
        btn(obj);
    });
}

//导出Excel
function exportEcxcel(obj){
    var url=getActionURL(getactionid_manage().templateIndex_exportExcel);
    // var url = "/cweb/police/template/exportWord";
    var data={
        token:INIT_CLIENTKEY,
        param:{
            templatessid: editSsid,
        }
    };

    // console.log(url);
    ajaxSubmitByJson(url, data, function (data) {
        // console.log(data);
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                // var host = "http://localhost";
                var host = "http://" + window.location.host;
                host = host.replace(":8080","");
                window.location.href = host + data;
                // window.open(host + data);
                layer.msg("导出成功,等待下载中...");
            }
        }else{
            layer.msg("导出失败");
        }
        btn(obj);
    });
}

function getTemplateById(id) {

    if (pagelist[id]) {
        editSsid = pagelist[id].ssid;
        // templateTypeId = $("#templateTitle").val();
        templateTypeId = pagelist[id].templatetypessid;
        templateId = pagelist[id].id;

        $('#templateTitle').html(pagelist[id].title);
        $('#leixing').html("类型：" + pagelist[id].typename);
        var templateToProblems = pagelist[id].templateToProblems;
        var tableProblems = '';
        $('#tableProblems').html(tableProblems);

        if (templateToProblems.length >= 1) {
            for (var i = 0; i < templateToProblems.length; i++) {
                var Problem = templateToProblems[i];

                tableProblems += '<tr><td class="font_red_color">问：' + Problem.problem + '</td></tr>';
                tableProblems += '<tr><td class="font_blue_color">答：' + Problem.referanswer + '</td></tr>';
            }
            $('#tableProblems').html(tableProblems);
        }

    }
}