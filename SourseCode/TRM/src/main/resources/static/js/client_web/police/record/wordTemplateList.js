
var explaindownurl_html=null;//word模板制作说明html地址
var explaindownurl=null;//word模板说明制作下载地址
var explaindownssid=null;

var wordtemplatelist=null;//模板word数据

function getWordTemplateList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().wordTemplateList_getWordTemplateList);
    var wordtemplatename=$("#wordtemplatename").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            wordtemplatename:wordtemplatename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetWordTemplateList);
}

function getWordTemplateList(wordtemplatename,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().wordTemplateList_getWordTemplateList);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            wordtemplatename:wordtemplatename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetWordTemplateList);
}

function callbackgetWordTemplateList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            var data=data.data;
            if (isNotEmpty(data)){
                wordtemplatelist=data.pagelist;
                explaindownurl=data.wordtemplate_explaindownurl;
                explaindownurl_html=data.wordtemplate_explaindownurl_html;
                explaindownssid=data.wordtemplate_explaindownssid;
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function getWordTemplateListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;
        getWordTemplateList_init(currPage,pageSize);
    }else if (len==2){
        getWordTemplateList('',arguments[0],arguments[1]);
    }else if(len>2){
        getWordTemplateList(arguments[0],arguments[1],arguments[2],arguments[3]);
    }
}

function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var wordtemplatename=pageparam.wordtemplatename;
        var arrparam=new Array();
        arrparam[0] = wordtemplatename;
        showpage("paging",arrparam,'getWordTemplateListByParam',currPage,pageCount,pageSize);
    }
}

//预览
function preview(url) {

    if (!isNotEmpty(url)){
        layer.msg("请先上传笔录模板",{icon: 5});
        return;
    }

    if (isNotEmpty(url)) {
        previewgetNotifications_index = layer.open({
            type:2,
            title:'预览笔录模板',
            content:url,
            shadeClose:true,
            area: ['50%', '700px'],
        });
    }

}


//模板开始上传---------------------------------------------------------start-------------------------------------------------------
var open_uploadword_index=null;
var upload_data={};
function open_uploadword(ssid) {
    var html='<form class="layui-form layui-form-pane site-inline"  style="margin: 30px;">\
            <div class="layui-form-item">\
                <label class="layui-form-label" style=" width: 200px;"><span style="color: red;font-weight: initial">*</span>笔录模板名称</label>\
                <div class="layui-input-block"  style="margin-left: 200px;">\
                    <input type="text" name="wordtemplatenamem" id="wordtemplatenamem"   lay-verify="wordtemplatenamem" placeholder="请输入笔录模板名称" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-form-item">\
                <label class="layui-form-label" style=" width: 200px;">word</label>\
                <div class="layui-input-block" style="margin-left: 200px;">\
                    <button type="button" class="layui-btn layui-btn-normal layui-input" id="test1" style="background-color: #1e9fff" onclick="wordfileclick();"><i class="layui-icon"></i>上传笔录模板</button>\
                    <input id="wordfile" type="file" style="display: none;" onchange="showfilename(this);">\
                </div>\
                <div class="layui-form-mid layui-word-aux" style="width: 100%"><div style="display: inline;text-align:left" id="showfilename"></div></div>\
            </div>\
            <div class="layui-form-item">\
                <label class="layui-form-label" style=" width: 200px;" >是否为默认</label>\
                <div class="layui-input-inline" style="width:auto;">\
                    <input  type="checkbox" name="defaultboolm" lay-skin="switch" id="defaultboolm" lay-text="默认|非默认">\
                 </div>\
                 <div class="layui-form-mid layui-word-aux"></div>\
            </div>\
             <div class="layui-progress " lay-showpercent="true" lay-filter="progress_demo" style="visibility:hidden;margin-top: 10px" >\
                <div class="layui-progress-bar "  ><span class="layui-progress-text">10%</span></div>\
            </div>\
            </form>';

    layui.use('form', function() {
        var form = layui.form;


    open_uploadword_index= layer.open({
        type: 1,
        title: '笔录模板编辑',
        shadeClose: true,
        maxmin: true, //开启最大化最小化按钮
        area: ['893px', '450px'],
        content: html,
        btn: ['确定', '取消'],
        success: function (layero, index) {
            layero.addClass('layui-form');//添加form标识
            layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
            form.render();
        },
        yes:function(index, layero){
            form.verify({
                wordtemplatenamem:[ /\S/,"请输入笔录模板名称"],
            });

            form.on("submit(fromContent)", function (data) {
                var file = document.getElementById("wordfile").files[0];
                if (!isNotEmpty(ssid)&&!isNotEmpty(file)){
                    layer.msg("请选择文件进行上传",{icon: 5});
                    return;
                }

                var wordtemplatename=$("#wordtemplatenamem").val();
                var defaultboolm=$("#defaultboolm").prop("checked");
                var defaultbool;
                if (defaultboolm){
                    defaultbool=1;
                } else{
                    defaultbool=-1;
                }



                if (!isNotEmpty(wordtemplatename)) {
                    layer.msg("请输入笔录模板名称",{icon: 5});
                    $("#wordtemplatenamem").focus();
                    return;
                }

                var data={
                    wordtemplatename:wordtemplatename,
                    defaultbool:defaultbool,
                    word:1,
                    ssid:ssid,
                };
                uploadWordTemplate(data,file);
            });

        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
    });

    if (isNotEmpty(ssid)){
        var url=getActionURL(getactionid_manage().wordTemplateList_getWordTemplateByssid);
        var data=
            {
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid
                }
            };
        ajaxSubmitByJson(url,data,callbackgetWordTemplateByssid);
    }

    layui.use('form', function(){
        var form = layui.form;
        form.render();
        if (!isNotEmpty(ssid)){
            if (isNotEmpty(wordtemplatelist)) {
                var defaultnum=0;//默认模板数量
                for (let i = 0; i < wordtemplatelist.length; i++) {
                    const wordtemplate = wordtemplatelist[i];
                    if (null!=wordtemplate.defaultbool&&wordtemplate.defaultbool==1){
                        defaultnum++;
                    }
                }
                if (defaultnum==0){
                    $("#defaultboolm").prop("checked", true);
                    form.render("checkbox");
                }
            }else {
                $("#defaultboolm").prop("checked", true);
                form.render("checkbox");
            }
        }
    });
}

function callbackgetWordTemplateByssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            $("#wordtemplatenamem").val(data.wordtemplatename);
            if (data.defaultbool==1) {
                $("#defaultboolm").prop("checked", true);
            }else {
                $("#defaultboolm").prop("checked", false);
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

function wordfileclick() {
    $("#wordfile").click();
}
function showfilename(obj) {
    var file = document.getElementById("wordfile").files[0];
    if (isNotEmpty(file)){
        $("#showfilename").text(file.name);
    }
}

//word 模板类型1 word笔录 word笔录说明
function uploadWordTemplate(data,file) {
    $("[lay-filter='progress_demo']").css("visibility","visible");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    var url=getActionURL(getactionid_manage().wordTemplateList_uploadWordTemplate);

    var formData = new FormData();
    formData.append("param", JSON.stringify(data));
    formData.append("token", INIT_CLIENTKEY);
    formData.append("wordfile", file);


    // XMLHttpRequest 对象
    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        $("[lay-filter='progress_demo']").css("visibility","hidden");
        callbackuploadWordTemplate(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(formData);
}

function progressFunction(evt) {
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("[lay-filter='progress_demo']").css("visibility","visible");
        $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
        $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
    }
};

function callbackuploadWordTemplate(str) {
    var data = eval('(' + str + ')');
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("保存成功",{time:500,icon:6},function () {
                layer.close(open_uploadword_index);
                getWordTemplateListByParam();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//模板开始上传---------------------------------------------------------end---------------------------------------------------------
$(function () {
    layui.use(['layer','form'], function(){
        var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
            ,form = layui.form;


        form.on('switch(defaultbool_filter)', function(switchdata){
            var obj=switchdata.elem.checked;
            var ssid=switchdata.value;
            if (!isNotEmpty(ssid)){
                layer.msg("系统异常",{icon: 5});
                return;
            }

            var con;
            var defaultbool;
            if (obj) {
                con="你确定要设置该笔录模板为默认模板吗";
                defaultbool=1;
            }else{
                con="你确定要取消该笔录模板的默认模板吗";
                defaultbool=-1;
            }
            layer.open({
                content:con
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    var url=getActionURL(getactionid_manage().wordTemplateList_changeboolWordTemplate);
                    var data=
                        {
                            token:INIT_CLIENTKEY,
                            param:{ ssid:ssid,
                                defaultbool:defaultbool
                            }
                        };
                    ajaxSubmitByJson(url,data,function (data) {
                        if(null!=data&&data.actioncode=='SUCCESS'){
                            if (isNotEmpty(data)){
                                if (isNotEmpty(data.data)){
                                    layer.msg("操作成功",{time:500},function () {
                                        switchdata.elem.checked=obj;
                                        getWordTemplateListByParam();
                                    });
                                }
                            }
                        }else{
                            switchdata.elem.checked=!obj;
                            layer.msg(data.message,{icon: 5});
                        }
                    });
                    form.render();
                    layer.close(index);
                }
                ,btn2: function(index, layero){
                    switchdata.elem.checked=!obj;
                    form.render();
                    layer.close(index);
                }
                ,cancel: function(){
                    switchdata.elem.checked=!obj;
                    form.render();
                }
            });
        });
    });
});


function exportWord(worddownurl) {
    if (isNotEmpty(worddownurl)){
        window.location.href = worddownurl;
        layer.msg("导出成功,等待下载中...",{icon: 6});
    }else {
        layer.msg("导出失败",{icon: 5});
    }
}


//word模板制作说明--------------------------start---------------------------------------------//
function open_getWordexplain() {
    if (!isNotEmpty(explaindownurl_html)){
        layer.msg("请先上传笔录模板制作说明",{icon: 5});
        return;
    }

    if (isNotEmpty(explaindownurl_html)) {
        layer.open({
            type:2,
            title:'查看模板制作说明',
            content:explaindownurl_html,
            shadeClose:true,
            area: ['50%', '700px'],
        });
    }
}

function open_uploadWordexplain() {
    $("#wordexplainfile").click();
}

function uploadWordexplain() {
    var file = document.getElementById("wordexplainfile").files[0];
    if (!isNotEmpty(file)){
        layer.msg("请选择文件进行上传",{icon: 5});
        return;
    }

    var data={
        wordtype:2,
        ssid:explaindownssid,
    };
    uploadWordTemplate(data,file);
}

/*************************删除**********************/
function changeboolWordTemplate(ssid) {
    layer.confirm('确定要删除该笔录模板吗', function(index){
        var url=getActionURL(getactionid_manage().wordTemplateList_changeboolWordTemplate);
        var data=
            {
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid,
                    wordtemplatebool:-1,//删除状态
                }
            };
        ajaxSubmitByJson(url,data,callbackchangeboolWordTemplate);
        layer.close(index);
    });
}

function callbackchangeboolWordTemplate(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("删除成功",{time:500,icon:6},function () {
                getWordTemplateListByParam();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}