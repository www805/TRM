var list;
var editSsid;
var templateTypeId;
var templateId;
var templatetypeidSSID;
var xiazai = false;
var xiazai2 = false;
var updateTemplateType;
var repeatStatus = null;

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
            pagelist = data.data.pagelist;
            getTemplateById(0);
            var listcountsize = data.data.pageparam.recordCount;
            if (listcountsize == 0) {
                $("#wushuju").show();
            } else {
                $("#wushuju").hide();
            }
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
                    $("#templateTypes").append("<option value='-1' >全部</option>");
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
        var pageSize = 13;//测试
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

layui.use(['laypage', 'form', 'layer', 'layedit', 'laydate','element', 'upload'], function(){
    var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
        ,form = layui.form
        ,layer = layui.layer
        ,layedit = layui.layedit
        ,laydate = layui.laydate
        ,laypage = layui.laypage
        ,element = layui.element;

    upload = layui.upload;

    form.on('select(pid)', function (data) {
        // var text = $("#keyword").val();
        // if(text.replace(/\s*/g,"") == ""){
        // }
        getTmplates_init(1,13);
    });



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

function modelbanUpdateTemplate() {
    // var problem = "";
    // var referanswer = "";
    // var id = "";
    //
    // if (isNotEmpty(problemV)){
    //     problem = problemV.problem;
    //     referanswer = problemV.referanswer;
    //     id = problemV.id;
    // }

    //  problemV

    var templateTypes = "";
    var templateType = {};
    for (var i = 0; i < list.length; i++) {
        templateType = list[i];
        if (i == 0) {
            updateTemplateType = templateType.id; //把第一个类型给他
        }
        templateTypes += "<option value='" + templateType.id + "' >" + templateType.typename + "</option>";
    }

    var content = "<div class=\"layui-form-item layui-form-text\" style='margin-top: 20px;padding-right: 20px;'>\n" +
        "            <label class=\"layui-form-label\">模板类型</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <select name=\"problemtypessid\" onchange='updateTemplateType = this[selectedIndex].value;' lay-verify=\"\" style=\"margin-top: 5px;width: 120px;height: 30px;\">\n"
        + templateTypes +
        "                </select>\n" +
        "            </div>\n" +
        "            <div class=\"layui-upload\" style='margin-left: 40px;' id='xzfile'>\n" +
        "                <button type=\"button\" class=\"layui-btn layui-btn-normal\" id=\"uploadFile\">选择文件</button>\n" +
        "            </div>\n" +
        "            <div class=\"layui-upload\" style='margin-left: 40px;margin-top: 10px;'>\n" +
        "                <button type=\"button\" class=\"layui-btn\" id=\"testListAction\" onclick='updateTemplateFile();'>开始导入</button>\n" +
        "            </div>\n" +
        "        </div>";

    //弹窗层
    layer.open({
        type: 1,
        title: "导入模板",
        area: ['420px', '220px'], //宽高
        shadeClose: true,
        content: content
    });

    var url = getActionURL(getactionid_manage().templateIndex_uploadFile);

    //执行实例
    var uploadInst = upload.render({
        elem: '#uploadFile' //绑定元素
        ,url: url //上传接口
        ,acceptMime: '.xls' //只允许上传图片文件
        ,exts: 'xls' //只允许上传压缩文件
        ,auto: false //选择文件后不自动上传
        ,bindAction: '#testListAction' //指向一个按钮触发上传
        ,data: {
            tmplateTypeId:updateTemplateType.toString()
            ,repeatStatus:repeatStatus
        } //可选项。额外的参数，如：{id: 123, abc: 'xxx'}
        , before: function (obj) {
            this.data = {
                tmplateTypeId: updateTemplateType.toString() //末班类型
                ,repeatStatus: repeatStatus //覆盖
            };

            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                var filevalue = file.name;
                var fileType = getFileType(filevalue)
                if (fileType !== 'xls') {
                    layer.msg("请选择xls类型的文件导入！", {icon: 2});
                }
            });
            return false;
        }
        , done: function (res) {
            //上传完毕回调doc|docx|
            console.log(res);
            repeatStatus = null;
            if ("SUCCESS" == res.actioncode) {
                layer.msg(res.message, {icon: 1});
                setTimeout("window.location.reload()", 1500);
            }else if(res.data == 504){

                //询问框
                layer.confirm(res.message, {
                    btn: ['覆盖','不覆盖','取消'] //按钮
                }, function(){
                    repeatStatus = "1";
                    uploadInst.upload();
                }, function(){
                    repeatStatus = "2";
                    uploadInst.upload();
                }, function(){
                    layer.closeAll();
                });
            }
        }
        , error: function (res) {
            //请求异常回调
            console.log("请求异常回调");
            // layer.msg(res.message,{icon: 2});
        }
    });
    // layer.closeAll();
}

function updateTemplateFile() {
    var text = $("#xzfile").find("span").text();
    if(!text){
        layer.msg("请选择文件，再开始导入", {icon: 5});
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
                var url = data;
                var a = document.createElement('a');          // 创建一个a节点插入的document
                var event = new MouseEvent('click')           // 模拟鼠标click点击事件
                a.download = 'beautifulGirl'                  // 设置a节点的download属性值
                a.href = url;                                 // 将图片的src赋值给a节点的href
                a.dispatchEvent(event);
                layer.msg("导出成功,等待下载中...");
                // DownLoadReportIMG(url);
            }
        }else{
            layer.msg("导出失败",{icon: 2});
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
                var url = data;
                var a = document.createElement('a');          // 创建一个a节点插入的document
                var event = new MouseEvent('click')           // 模拟鼠标click点击事件
                a.download = 'beautifulGirl'                  // 设置a节点的download属性值
                a.href = url;                                 // 将图片的src赋值给a节点的href
                a.dispatchEvent(event);
                layer.msg("导出成功,等待下载中...");

                // DownLoadReportIMG(url);
            }
        }else{
            layer.msg("导出失败",{icon: 2});
        }
        btn(obj);
    });
}

function getTemplateById(id) {

    if (null != pagelist && pagelist.length > 0) {
        $("#zhuangtai").show();
        $("#leixing").show();
        editSsid = pagelist[id].ssid;
        // templateTypeId = $("#templateTitle").val();
        templateTypeId = pagelist[id].templatetypessid;
        templateId = pagelist[id].id;

        $('#templateTitle').html(pagelist[id].title);
        if(pagelist[id].typename == null){
            pagelist[id].typename = "未知";
        }
        $('#leixing').html("类型：" + pagelist[id].typename);
        var templateToProblems = pagelist[id].templateToProblems;
        var tableProblems = '';
        $('#tableProblems').html(tableProblems);

        if (templateToProblems.length >= 1) {
            for (var i = 0; i < templateToProblems.length; i++) {
                var Problem = templateToProblems[i];

                if(Problem) {
                    tableProblems += '<tr><td class="font_red_color">问：' + Problem.problem + '</td></tr>';
                    tableProblems += '<tr><td class="font_blue_color">答：' + Problem.referanswer + '</td></tr>';
                }
            }
            $('#tableProblems').html(tableProblems);
        }

    }else{
        $("#zhuangtai").hide();
        $("#leixing").hide();
        $("#tableProblems").html("");
        $("#templateTitle").html("暂无模板");
    }

}


function DownLoadReportIMG(imgPathURL) {
    //如果隐藏IFRAME不存在，则添加
    if (!document.getElementById("IframeReportImg"))
        $('<iframe style="display:none;" id="IframeReportImg" name="IframeReportImg" onload="DoSaveAsIMG();" width="0" height="0" src="about:blank"></iframe>').appendTo("body");
    if (document.all.IframeReportImg.src != imgPathURL) {
        //加载图片
        document.all.IframeReportImg.src = imgPathURL;
    }
    else {
        //图片直接另存为
        DoSaveAsIMG();
    }
}
function DoSaveAsIMG() {
    if (document.all.IframeReportImg.src != "about:blank")
        window.frames["IframeReportImg"].document.execCommand("SaveAs");
}