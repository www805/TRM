var tableProblems = '';
var version = "";
var list, all, plist;
var arrayProblem = [];
var problemtypessidV;
var trObj;
var NoDelele = false;
var modelban_index;
var typeSsId, tlist;
var TtemplateinfoData = "";

//初始化获取模板列表
function getProblems_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getProblems);
    // var url = "/cweb/police/template/getProblems";
    // console.log(url);

    var keyword =$("#keyword").val();
    var problemtypeid = $("#problemType").val();

    if(isNotEmpty(typeSsId)){
        problemtypeid = typeSsId;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            keyword: keyword,
            problemtypeid: problemtypeid,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callTmplates2);
}

//分页查询模板列表
function getProblems(keyword, problemtypeid, currPage, pageSize) {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getProblems);
    // var url = "/cweb/police/template/getProblems";
    // console.log(url);

    var data={
        token:INIT_CLIENTKEY,
        param:{
            keyword: keyword,
            problemtypeid: problemtypeid,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTmplates);
}

//获取问题类型
function getProblemTypes() {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getProblemTypes);
    // var url = "/cweb/police/template/getProblemTypes";
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callProblemTypes);
}

//获取模板类型
function getTmplateTypes() {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getTemplateTypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{

        }
    };
    ajaxSubmitByJson(url,data,callTmplateTypes);
}

/**
 * 笔录类型
 */
function getTmplateTypes() {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getTemplateTypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callTmplateTypes);
}

//获取授权功能列表
function getSQGnlist() {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_getSQGnlist);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callSQGnlist);
}

function callSQGnlist(data){
    if(null!=data&&data.actioncode=='SUCCESS'){

        var gnlist = data.data.data.gnlist;
        if (gnlist.indexOf(HK_O) != -1) {
            if(isNotEmpty(list)){

                var type = null;
                for (var i = 0; i < list.length; i++) {
                    type = list[i];
                    if (type.typename == "谈话") {
                        // typeSsId = type.ssid;
                        break;
                    }
                }
                $("#templateType").html("<option selected value='" + type.id + "' >" + type.typename + "</option>");
            }

            // plist
            if(isNotEmpty(plist)){

                var type = null;
                for (var i = 0; i < plist.length; i++) {
                    type = plist[i];
                    if (type.typename == "谈话") {
                        typeSsId = type.ssid;
                        break;
                    }
                }
            }
            $("#problemTypes").remove();
            layui.use('form', function(){
                var form = layui.form;
                form.render();
            });
        }
        getProblemTypesParam(); //查询模板

    }else{
        layer.msg(data.message, {icon: 5});
    }
}

function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var list=data.pagelist;
                tlist = list;
                if (isNotEmpty(list)) {
                    for (var i = 0; i < list.length; i++) {
                        var templateType = list[i];
                        $("#templatetypessid").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }
                }
            }
            // typeId
            console.log(typeSsId);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

function callTmplates(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            all = data.data.pagelist;
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callTmplates2(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            all = data.data.pagelist;
            pageshow(data);
            var listcountsize = data.data.pageparam.recordCount;
            if (listcountsize == 0) {
                $("#wushuju").show();
            } else {
                $("#wushuju").hide();
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//修改问题答案返回
function callAddOrUpdate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            // window.location.reload();
            // console.log(data);
            layer.msg("操作成功",{icon: 6});
            setTimeout("getProblems_init(1,10);layer.close(modelban_index);",1500);//getProblems_init(1,10);window.location.reload();
        }
    }else{
        // if (data.message.search("存在")) {
        //     //询问框
        //     layer.confirm(data.message, {
        //         btn: ['继续','取消'] //按钮
        //     }, function(){
        //         var title = $("#problem").val();
        //         $("#problem").val(title + "副本");
        //         if (data.message.search("修改")) {
        //             AddOrUpdateProblem("");
        //         }else{
        //             AddOrUpdateProblem("1");
        //         }
        //
        //     });
        //
        // }else{
        // }
        layer.msg(data.message,{icon: 5});
    }
}

function callProblemTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var plistAll = data.data.pagelist;

            plist = plistAll;
            if (isNotEmpty(plistAll)) {
                for (var i = 0; i < plistAll.length; i++) {
                    var problemType = plistAll[i];
                    $("#problemType").append("<option value='" + problemType.ssid + "' >" + problemType.typename + "</option>");
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callUpdateProblem(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var problem = data.data.problem;
            modelban(problem);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callTemplateById(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var template = data.data.template;
            var templateToProblems = data.data.template.templateToProblems;

            $("#templateTitle").val(template.title);

            var tableProblems = '';
            $('#dataTable').html(tableProblems);

            if (templateToProblems.length >= 1) {
                for (var i = 0; i < templateToProblems.length; i++) {
                    var Problem = templateToProblems[i];

                    tableProblems += '<tr onclick=\'trObj = this;\'>\n' +
                        '                        <td style="padding: 0;" class="onetd font_red_color" name="' + Problem.id + '">\n' +
                        '                            <div name="1" class="table_td_tt2" value="' + Problem.ssid + '" ><p class="table_td_tt_p">问：</p><p contenteditable="true" class="table_td_tt " >' + Problem.problem + '</p></div>\n' +
                        '                            <div name="2" class="table_td_tt2 font_blue_color" value="' + Problem.ssid + '" ><p class="table_td_tt_p">答：</p><p contenteditable="true" class="table_td_tt content text" placeholder="' + Problem.referanswer + '"></p></div>\n' +
                        '                        </td>\n' +
                        '                        <td>\n' +
                        '                            <div class="layui-btn-group">\n' +
                        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="upp(this);"><i class="layui-icon layui-icon-up"></i></button>\n' +
                        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="downn(this);"><i class="layui-icon layui-icon-down"></i></button>\n' +
                        '                                <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="DeleteProblem(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\n' +
                        '                            </div>\n' +
                        '                        </td>\n' +
                        '                    </tr>';
                }
                $('#dataTable').html(tableProblems);

                // console.log($("p[contenteditable=true]").html());

                $(trObj).on("blur",function(){
                    trObj = null;
                });
            }

        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var listAll = data.data.pagelist;

            list = listAll;
            var type = getQueryString("type");

            if (isNotEmpty(listAll)) {
                for (var i = 0; i < listAll.length; i++) {
                    var templateType = listAll[i];
                    if(type == templateType.id){
                        $("#templateType").append("<option selected value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }else{
                        $("#templateType").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }
                }
            }

            getSQGnlist();//获取授权判断是否单机版
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callAddTmplate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功！",{icon: 6});

            setTimeout(function () {
                var url=getActionURL(getactionid_manage().addOrupdateTemplate_toTemplateIndex);
                window.location.href = url;

            },1500);
        }
    }else{
        if (data.message.search("存在")) {

            //询问框
            layer.confirm(data.message, {
                btn: ['新增','取消'] //按钮
            }, function(){
                var title = $("#templateTitle").val();
                $("#templateTitle").val(title + "副本");
                getDataAll();
            });

        }else{
            layer.msg(data.message,{icon: 5});
        }
    }
}

function callUpdateTmplate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功！",{icon: 6});

            setTimeout("var templateTypeId = $(\"#templateType\").val();var editSsid = getQueryString(\"ssid\");var templateId = getQueryString(\"templateId\");window.location.href = 'toaddOrupdateTemplate?ssid=' + editSsid + '&type='+templateTypeId +'&templateId='+templateId;",1500);
            // window.location.reload();
            // console.log(data);
            // alert("chengg")
        }
    }else{
        if (data.message.search("存在")) {
            //询问框
            layer.confirm(data.message, {
                btn: ['修改','取消'] //按钮
            }, function(){
                var title = $("#templateTitle").val();
                $("#templateTitle").val(title + "副本");
                getDataAll();
            });

        }else{
            layer.msg(data.message,{icon: 5});
        }
    }
}

/**
 * 局部刷新
 */
function getProblemTypesParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 10;//测试
        getProblems_init(currPage, pageSize);
    }  else if (len == 2) {
        getProblems('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getProblems(arguments[0], arguments[1], arguments[2], arguments[3]);
    }
}

function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var keyword=$("input[name='keyword']").val();
        var problemType = $("#problemType").val();

        if(isNotEmpty(typeSsId)){
            problemType = typeSsId;
        }

        var arrparam=new Array();
        arrparam[0]=keyword;
        arrparam[1]=problemType;
        showpage("paging",arrparam,'getProblemTypesParam',currPage,pageCount,pageSize);
    }
}

layui.use(['laypage', 'form', 'layer', 'layedit', 'laydate', 'table'], function () {
    var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
        , form = layui.form
        , layer = layui.layer
        , layedit = layui.layedit
        , laydate = layui.laydate
        , laypage = layui.laypage;
    var table = layui.table;

    //不显示首页尾页
    laypage.render({
        elem: 'demo1'
        , count: 100
        , first: false
        , last: false
    });

    //监听单元格编辑
    huoqu();

    form.render();
});

function templateInit() {
    var ssid = getQueryString("ssid");

    if (ssid) {
        //查询单个模板 addOrupdateTemplate_getTemplateById
        var url=getActionURL(getactionid_manage().addOrupdateTemplate_getTemplateById);
        // var url = "/cweb/police/template/getTemplateById";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: ssid
            }
        };
        ajaxSubmitByJson(url, data, callTemplateById);
    }else{
        $("#editState").html("新增模板中....");
        addTr("","","");//新增一条空白的问答
    }
}

function AddOrUpdateProblem(version) {
    var url=getActionURL(getactionid_manage().addOrupdateTemplate_updateProblem);
    // var url = "/cweb/police/template/updateProblem";
    var problem = $("#problem").val();
    var referanswer = $("#referanswer").val();
    var problemtypessid = $("#problemtypessid").val();
    var ByIdDDD = "";
    var ByssIdDDD = "";

    // console.log(pageActionByPage);
    // console.log(url);

    if("" == problem){
        layer.msg("问题不能为空",{icon: 5});
        return;
    }

    if (version == "修改") {
        ByIdDDD = $("#ByIdDDD").val();
        ByssIdDDD = $("#ByssIdDDD").val();
    } else {
        //添加
        url=getActionURL(getactionid_manage().addOrupdateTemplate_addProblem);
        // url = "/cweb/police/template/addProblem";
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            id: ByIdDDD,
            ssid: ByssIdDDD,
            problem: problem,
            referanswer: referanswer,
            problemtypessid: problemtypessid,
            problemtypessidV: problemtypessidV
        }
    };
    ajaxSubmitByJson(url, data, callAddOrUpdate);

}

function UpdateProblemBy(va, ByIdDDD, text) {
    var url=getActionURL(getactionid_manage().addOrupdateProblem_updateProblem);
    // var url = "/cweb/police/template/updateProblem";
    // var problem = $("#problem").val();
    // var referanswer = $("#referanswer").val();
    // var problemtypessid = $("#problemtypessid").val();

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            id: ByIdDDD,
        }
    };

    if (va == '1') {
        data.param.problem = text;
    } else {
        data.param.referanswer = text;
    }

    ajaxSubmitByJson(url, data, callAddOrUpdate);
}

function addUpdateinfo(ssid, problemtypessid, type) {
    version = "修改";
    if (isNotEmpty(type)) {
        version = "添加";
    }

    problemtypessidV = problemtypessid;

    tableProblems = ""; //清空以前的
    for (var i = 0; i < plist.length; i++) {
        var problemType = plist[i];

        if(isNotEmpty(typeSsId)){
            if (problemType.ssid == typeSsId) {
                tableProblems += "<option selected value='" + problemType.ssid + "' >" + problemType.typename + "</option>";
                break;
            }
        }else{
            if (problemtypessid == problemType.ssid) {
                tableProblems += "<option selected value='" + problemType.ssid + "' >" + problemType.typename + "</option>";
            }else{
                tableProblems += "<option value='" + problemType.ssid + "' >" + problemType.typename + "</option>";
            }
        }
    }

    if (isNotEmpty(ssid) && isNotEmpty(problemtypessid)) {
        // var url = "/cweb/police/template/getProblemById";
        var url = getActionURL(getactionid_manage().addOrupdateTemplate_getProblemById);
        var data = {
            token: INIT_CLIENTKEY,
            param: {
                ssid: ssid,
                problemtypessid: problemtypessid
            }
        };
        ajaxSubmitByJson(url, data, callUpdateProblem);

    } else {
        modelban();
    }
}

function addTemplateProblem(obj, id) {
    var text = $(obj).find("td").eq(0).find(".wentide").text();
    if (text == "") {
        text = $(obj).parents('tr').find("td").eq(0).find(".wentide").text();
    }
    text = text.replace('问：', '');

    var onetd = $("#dataTable").find("td.onetd");
    if (onetd.length == 1) {
        var str = $(onetd).text();
        str = str.replace(/\s/g,'');
        str = str.replace('问：','');
        var arr = str.split('答：');

        if (arr[0] == "") {
            trObj = null;
            $("#dataTable").html("");
        }
    }

    $("#pagelisttemplates_tbody").find("tr").each(function (i, val) {
        $(this).css("background-color", "#fff");
        if (i == id) {
            $(this).css("background-color", "#f2f2f2");
        }
    });
    addTr(text, all[id].referanswer, all[id].id);
    huoqu();
}

function modelban(problemV) {
    var problem = "";
    var referanswer = "";
    var id = "";
    var ssid = "";
    var problemtypessid = "";

    if (isNotEmpty(problemV)){
        problem = problemV.problem;
        referanswer = problemV.referanswer;
        id = problemV.id;
        ssid = problemV.ssid;
        problemtypessid = problemV.problemtypessid;
    }

    var content = "<div class=\"layui-form-item layui-form-text\" style='margin-top: 20px;padding-right: 20px;'>\n" +
        "            <input type='hidden' id='ByIdDDD' value='" + id + "'>\n" +
        "            <input type='hidden' id='ByssIdDDD' value='" + ssid + "'>\n" +
        "            <label class=\"layui-form-label\"><span style=\"color: red;\">*</span>问题类型</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <select name=\"problemtypessid\" id='problemtypessid' lay-verify=\"\" style=\"margin-top: 5px;width: 120px;height: 30px;\">\n"
        + tableProblems +
        "                </select>\n" +
        "            </div>\n" +
        "            <label class=\"layui-form-label\"><span style=\"color: red;\">*</span>问题</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <textarea name='problem' id='problem' placeholder=\"请输入问题\" class=\"layui-textarea\">" + problem + "</textarea>\n" +
        "            </div>\n" +
        "            <label class=\"layui-form-label\">参考答案</label>\n" +
        "            <div class=\"layui-input-block\">\n" +
        "                <textarea name='referanswer' id='referanswer' placeholder=\"请输入答案\" class=\"layui-textarea\">" + referanswer + "</textarea>\n" +
        "            </div>\n" +
        "            <button class=\"layui-btn layui-btn-normal\" onclick='AddOrUpdateProblem(version)' style='float: right;margin-top: 10px;'>" + version + "</button>\n" +
        "        </div>";

    //弹窗层
    modelban_index = layer.open({
        type: 1,
        title: version,
        area: ['620px', '390px'], //宽高
        shadeClose: true,
        content: content
    });
}

function DeleteProblem(obj) {
    NoDelele = true;
    $(obj).parents('tr').remove();
}

//拿到所有数据
function getDataAll() {
    // addArrProblem();

    var templatetoproblemids = [];
    var arr = [];
    var problem = {};
    var templateType = $("#templateType").val();
    var tableLength = $("#testTable tr").length;
    var title = $("#templateTitle").val();
    var ssid = getQueryString("ssid");
    var type = getQueryString("type");
    var templateId = getQueryString("templateId");
    title = title.replace("//s/g", "");
    // alert(t01);
    if (tableLength == 0) {
        layer.msg("模板问题不能为空", {icon: 5});
        return;
    }
    if(!title){
        layer.msg("模板标题不能为空", {icon: 5});
        // $("#templateTitle").css("border","1px solid red");
        $("#templateTitle").focus();
        return;
    }

    var tiaochu = true;

    $("#testTable").find("td.onetd").each(function(i) {

        var str = $(this).text();
        str = str.replace(/\s/g,'');
        str = str.replace('问：','');
        arr = str.split('答：');
        // console.log(arr);
        //上传更新模板
        // var title =$("input[name='title']").val();

        if(arr[0] == ""){
            tiaochu = false;
            layer.msg("新增的问题不能为空", {icon: 5});
            return;
        }

        if (!isNotEmpty(arr[1])) {
            arr[1] = $(this).find("p.table_td_tt.content.text").attr('placeholder');
        }

        problem = {
            id:parseInt($(this).attr("name")),
            problem:arr[0],
            referanswer:arr[1],
            ordernum:i
        }

        templatetoproblemids[i] = problem;
    })

    if(arr[0] == "" || tiaochu == false){
        return;
    }

    // console.log(templatetoproblemids);

    // var url = "/cweb/police/template/addTemplate";
    var url = getActionURL(getactionid_manage().addOrupdateTemplate_addTemplate);

    if (ssid) {
        var url = getActionURL(getactionid_manage().addOrupdateTemplate_updateTemplate);
        // url = "/cweb/police/template/updateTemplate";
    }
    var data={
        token:INIT_CLIENTKEY,
        param:{
            templatetoproblemids:templatetoproblemids,
            templatetypeid: templateType,
            title:title,
            ssid:ssid,
            id:templateId
        }
    };

    //判断如果是重复数据就不提交
    var Ttemplateinfo = JSON.stringify(data.param);
    if(TtemplateinfoData != Ttemplateinfo){
        TtemplateinfoData = Ttemplateinfo;

        if(ssid){
            ajaxSubmitByJson(url, data, callUpdateTmplate);
        }else{
            ajaxSubmitByJson(url, data, callAddTmplate);
        }

    }
}

function huoqu() {
    $("p").blur(function(){
        //修改问答
        var id = $(this).attr("name");
        var va = $(this).attr("value");
        var text = $(this).text();

        text = text.replace(/问：/g, "");
        text = text.replace(/答：/g, "");

        problem = {
            id: id,
            va: va,
            text: text
        };

        arrayProblem.push(problem);

        // console.log(arrayProblem);

        // UpdateProblemBy(id, va, text);
    });
}

function addTr(text, referanswer, id) {
    var updown = '<div class="layui-btn-group">\n' +
        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="upp(this);"><i class="layui-icon layui-icon-up"></i></button>\n' +
        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="downn(this);"><i class="layui-icon layui-icon-down"></i></button>\n' +
        '                                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" onclick="DeleteProblem(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>' +
        '</div>';
    // var textHtml = '<p contenteditable="true" class="table_td_tt table_td_tt2">' + text + '</p><p contenteditable="true" class="table_td_tt table_td_tt2 font_blue_color">答：' + all[id].referanswer + '</p>';

    var textHtml = '<div name="1" class="table_td_tt2" ><p class="table_td_tt_p">问：</p><p contenteditable="true" class="table_td_tt " >' + text + '</p></div><div name="2" class="table_td_tt2 font_blue_color" ><p class="table_td_tt_p">答：</p><p contenteditable="true" class="table_td_tt content text" placeholder="' + referanswer + '"></p></div>'

    var trHtml = "<tr class=\"onetd font_red_color\" onclick='trObj = this;'><td style=\"padding: 0;\" class=\"onetd\" name='" + id + "'>" + textHtml + "</td><td>" + updown + "</td></tr>";
    // $("#testTable tbody").after(trHtml);

    var htmlOBJ;
    // console.log(isNotEmpty(trObj));
    // console.log(trObj);
    // console.log(NoDelele);

    if(!isNotEmpty(trObj) && null == trObj || NoDelele == true){
        $("#dataTable").append(trHtml);
        htmlOBJ = $("#dataTable tr").last();

        // $("#dataTable tr").last().find("p").eq(1).focus().select();//下一行获取焦点
        // $("#dataTable tr").last().find("p").eq(1).html("<div><br/></div>");
        // $("#dataTable tr").last().find("p").eq(1).focus(function () {
        //     $(this).html("<div><br/></div>");
        // });
    }else if(isNotEmpty(trObj) && null != trObj ){
        //光标如果在问题，就跳到答案，如果是在答案就跳到下一行
        if($(trObj).find("p").eq(1).is(":focus")){
            $(trObj).find("p").eq(3).focus().select();
            return;
        }

        $(trObj).after(trHtml);
        htmlOBJ = $(trObj).next();
        // trObj = null;
        // $(trObj).next().find("p").eq(1).html("<div><br/></div>");
    }
    NoDelele = false;
    trObj = htmlOBJ;

    htmlOBJ.find("p").eq(1).focus().select();//下一行获取焦点
    // htmlOBJ.find("p").eq(1).focus(function () {
    //     $(this).html("<div><br/></div>");
    // });

    // htmlOBJ.blur(function(){
    //     trObj = null;
    // });
    //
    // $("p[contenteditable='true']").focus(function(){
    //     alert(1111);
    // });

    // htmlOBJ.find("p").blur(function(){
    //
    //     alert(1111);
    // });



}

/**
 * 保存后修改问题和答案
 */
function addArrProblem() {
    for (var i = 0; i < arrayProblem.length; i++) {
        var problem = arrayProblem[i];
        UpdateProblemBy(problem.id, problem.va, problem.text);
    }
}

function btn(obj) {
    // var selected = $(obj).closest("div[name='btn_div']").attr("class");
    // if (selected.indexOf("layui-form-selected") == -1) {
    //     $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    // }else{
    //     $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    // }

    var selected=$(obj).closest("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $(obj).closest("div[name='btn_div']").attr("showorhide","true");
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $(obj).closest("div[name='btn_div']").attr("showorhide","false");
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}