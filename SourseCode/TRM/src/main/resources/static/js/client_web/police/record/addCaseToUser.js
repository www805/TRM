function addCaseToArraignment() {
    //添加笔录案件关联，跳转页面

    var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
        setpageAction(INIT_CLIENT,nextparam.nextPageId);
        var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
        window.location.href=url;
    }

}

/**
 * 获取笔录类型列表
 */
function getRecordtypes() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getRecordtypes);
    var pid=$("#pid option:selected").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            pid:pid
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordtypes);
}
function callbackgetRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var list=data.getRecordtypesVOParamList;
           gettree(list);
        }
    }
}
function gettree(data){
    if (isNotEmpty(data)){
        for (var i = 0; i < data.length; i++) {
            var l = data[i];
            var html='<div class="layui-colla-item" style="border: 1px solid #FFFFFF">\
                                                       <h2 class="layui-colla-title layui-elem-quote" style="background-color: #FFFFFF;margin-bottom:0;border-left: 5px solid #1E9FFF;">'+l.typename+'</h2>\
                                                       <div class="layui-colla-content layui-show" style="padding: 0;border: 0">\
                                                           <table class="layui-table"  lay-skin="nob" >';
            if (l.police_recordtypes.length>0){
                for (var j = 0; j < l.police_recordtypes.length; j++) {
                    var ls = l.police_recordtypes[j];
                    html+=' <tr><td>'+ls.typename+'</td></tr>';
                }
            }
            html+='</table> </div></div>';
            $("#recotdtypes").append(html);
        }
    }
    $('#recotdtypes td').eq(0).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
    $('#recotdtypes td').dblclick(function() {
        $('#recotdtypes td').not(this).css({"background-color":"#ffffff","color":"#000000"});
        $(this).css({"background-color":"#1E9FFF","color":"#FFFFFF"});//还原所有td的颜色
    });
    layui.use('element', function(){
        var element = layui.element;
        element.render();
    });
}


function getNationalitys(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getNationalitys);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetNationalitys);
}

function callbackgetNationalitys(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#nationality option').not(":lt(1)").remove();
        if (isNotEmpty(data)){
            if (isNotEmpty(data)) {
                    for (var i = 0; i < data.length; i++) {
                        var l = data[i];
                        $("#nationality").append("<option value='"+l.id+"' title='"+l.enname+"'> "+l.zhname+"</option>");
                    }
            }
        }
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

function getNationals(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getNationals);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetNationals);
}


function callbackgetNationals(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#national option').not(":lt(1)").remove();
        if (isNotEmpty(data)) {
            for (var i = 0; i < data.length; i++) {
                var l = data[i];
                $("#national").append("<option value='"+l.id+"' title='"+l.nationname+"'>"+l.nationname+"</option>");
            }
        }
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}


$(function () {
    $('#recotdtypes td').eq(0).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
    $('#recotdtypes td').dblclick(function() {
        $('#recotdtypes td').not(this).css({"background-color":"#ffffff","color":"#000000"});
        $(this).css({"background-color":"#1E9FFF","color":"#FFFFFF"});//还原所有td的颜色
    });
});
