var token = "4843484445444848454842464648464A424549474A4A45";
var tableProblems = '';
var version = "";
var list, all;

function getProblems_init(currPage,pageSize) {
    // var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var url = "/cweb/police/template/getProblems";
    var keyword =$("#keyword").val();
    var problemtypeid = $("#problemType").val();
    var data={
        token:token,
        param:{
            keyword: keyword,
            problemtypeid: parseInt(problemtypeid),
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callTmplates2);
}

function getProblems(keyword, problemtypeid, currPage, pageSize) {
    // var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var url = "/cweb/police/template/getProblems";

    var data={
        token:token,
        param:{
            keyword: keyword,
            problemtypeid: parseInt(problemtypeid),
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url, data, callTmplates);
}

//获取问题类型
function getProblemTypes() {
    // var url=getActionURL(getactionid_manage().getUserList_getRoles);
    var url = "/cweb/police/template/getProblemTypes";
    var data={
        token:token,
        param:{}
    };
    ajaxSubmitByJson(url,data,callTmplateTypes);
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
            all = data.data.pagelist;
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callTmplates2(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            all = data.data.pagelist;
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callAddOrUpdate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            window.location.reload();
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            list = data.data.problemtypes;

            if (isNotEmpty(list)) {
                for (var i = 0; i < list.length; i++) {
                    var problemType = list[i];
                    $("#problemType").append("<option value='" + problemType.id + "' >" + problemType.typename + "</option>");
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callUpdateProblem(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var problem = data.data.problem;
            modelban(problem.problem, problem.referanswer, problem.id);
        }
    }else{
        layer.msg(data.message,{icon: 2});
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


            if (templateToProblems.length > 1) {
                for (var i = 0; i < templateToProblems.length; i++) {
                    var Problem = templateToProblems[i];


                    tableProblems += '<tr>\n' +
                        '                        <td style="padding: 0;" class="onetd font_red_color" name="' + Problem.id + '">\n' +
                        '                            <p class="table_td_tt table_td_tt2">问：' + Problem.problem + '</p>\n' +
                        '                            <p class="table_td_tt table_td_tt2 font_blue_color" >答：' + Problem.referanswer + '</p>\n' +
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
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callTmplateTypes(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            list = data.data.templatetypes;

            var type = getQueryString("type");

            if (isNotEmpty(list)) {
                for (var i = 0; i < list.length; i++) {
                    var templateType = list[i];
                    if(type == templateType.id){
                        $("#templateType").append("<option selected value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }else{
                        $("#templateType").append("<option value='" + templateType.id + "' >" + templateType.typename + "</option>");
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callAddOrUpdateTmplate(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            // window.location.reload();
            console.log(data);
            alert("chengg")
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getProblemTypesParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 3;//测试
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
        var arrparam=new Array();
        arrparam[0]=keyword;
        arrparam[1]=problemType;
        showpage("paging",arrparam,'getProblemTypesParam',currPage,pageCount,pageSize);
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


/**
 * 删除
 * @param ssid
 */
function deleteUser(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }

    layer.confirm('确定要删除该用户吗', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().getUserList_deleteUser);
        var data={
            ssid:ssid,
            adminbool:-1
        };
        ajaxSubmit(url,data,callbackdeleteUser);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });
}

function callbackdeleteUser(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("删除成功",{icon: 1,time:500},function () {
                  /* var nextparam=getAction(getactionid_manage().getUserList_deleteUser);
                     if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                         setpageAction(INIT_WEB,nextparam.nextPageId);
                         var url=getActionURL(getactionid_manage().main_getUser);
                         window.location.href=url;
                     }*/
                    getUserList_init(1,3);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


function callbackchangeUser(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("操作成功",{icon: 1,time:500},function () {
                    getUserList_init(1,3);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function templateInit() {
    var ssid = getQueryString("ssid");

    if (ssid) {
        //查询单个模板
        var url = "/cweb/police/template/getTemplateById";
        var data={
            token:token,
            param:{
                ssid: ssid
            }
        };
        ajaxSubmitByJson(url, data, callTemplateById);
    }
}

function AddOrUpdateProblem(version) {

    var url = "/cweb/police/template/updateProblem";
    var problem = $("#problem").val();
    var referanswer = $("#referanswer").val();
    var problemtypessid = $("#problemtypessid").val();
    var ByIdDDD = "";

    if (version == "修改") {
        ByIdDDD = $("#ByIdDDD").val();
    } else {
        //添加
        url = "/cweb/police/template/addProblem";
    }

    var data = {
        token: token,
        param: {
            id: ByIdDDD,
            problem: problem,
            referanswer: referanswer,
            problemtypessid: parseInt(problemtypessid),
        }
    };
    ajaxSubmitByJson(url, data, callAddOrUpdate);

}

function addUpdateinfo(ssid, type) {
    version = "修改";
    if (type) {
        version = "添加";
    }

    if (!tableProblems) {
        for (var i = 0; i < list.length; i++) {
            var templateType = list[i];
            tableProblems += "<option value='" + templateType.id + "' >" + templateType.typename + "</option>";
        }
    }

    if(ssid){
        var url = "/cweb/police/template/getProblemById";
        var data={
            token:token,
            param:{
                id: ssid
            }
        };
        ajaxSubmitByJson(url, data, callUpdateProblem);

    }else {
        modelban("", "", "");
    }
}

function modelban(problem, referanswer, id) {
    var content = "<div class=\"layui-form-item layui-form-text\" style='margin-top: 20px;padding-right: 20px;'>\n" +
        "            <input type='hidden' id='ByIdDDD' value='" + id + "'>\n" +
        "            <label class=\"layui-form-label\">问题类型</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <select name=\"problemtypessid\" id='problemtypessid' lay-verify=\"\" style=\"margin-top: 5px;width: 120px;height: 30px;\">\n"
        + tableProblems +
        "                </select>\n" +
        "            </div>\n" +
        "            <label class=\"layui-form-label\">问题</label>\n" +
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
    layer.open({
        type: 1,
        title: version,
        area: ['620px', '390px'], //宽高
        shadeClose: true,
        content: content
    });
}

function addTemplateProblem(obj, id) {
    var text = $(obj).parents('tr').find("td").eq(0).text();

    var updown = '<div class="layui-btn-group">\n' +
        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="upp(this);"><i class="layui-icon layui-icon-up"></i></button>\n' +
        '                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="downn(this);"><i class="layui-icon layui-icon-down"></i></button>\n' +
        '                                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" onclick="DeleteProblem(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>' +
        '</div>';

    var textHtml = '<p class="table_td_tt table_td_tt2">' + text + '</p><p class="table_td_tt table_td_tt2 font_blue_color">答：' + all[id].referanswer + '</p>';

    $("#testTable tbody").append("<tr class=\"onetd font_red_color\"><td style=\"padding: 0;\" class=\"onetd\" value='" + all[id].id + "''>" + textHtml + "</td><td>" + updown + "</td></tr>");
}

function DeleteProblem(obj) {
    $(obj).parents('tr').remove();
}

//拿到所有数据
function getDataAll() {

    var templatetoproblemids = [];
    var problem = {};
    var templateType = $("#templateType").val();
    var tableLength = $("#testTable tr").length;
    var title = $("#templateTitle").val();
    var ssid = getQueryString("ssid");
    var type = getQueryString("type");
    title = title.replace("//s/g", "");
    // alert(t01);
    if (tableLength == 0) {
        layer.msg("模板问题不能为空", {icon: 2});
        return;
    }
    if(!title){
        layer.msg("模板标题不能为空", {icon: 2});
        return;
    }

    $("#testTable").find("td.onetd").each(function(i) {

        var str = $(this).text();
        str = str.replace(/\s/g,'');
        str = str.replace('问：','');
        arr = str.split('答：');
        // console.log(arr);
        //上传更新模板
        // var title =$("input[name='title']").val();

        problem = {
            id:parseInt($(this).attr("name")),
            ordernum:i
        }

        templatetoproblemids[i] = problem;
    })

    console.log(templatetoproblemids);

    var url = "/cweb/police/template/addTemplate";

    if (ssid) {
        url = "/cweb/police/template/updateTemplateType";
    }
    var data={
        token:token,
        param:{
            templatetoproblemids:templatetoproblemids,
            templatetypeid: templateType,
            title:title
        }
    };

    console.log(data);

    // ajaxSubmitByJson(url, data, callAddOrUpdateTmplate);

}


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}