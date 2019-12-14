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
    }else{
        layer.msg(data.message,{icon: 5});
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
                    html+=' <tr><td recordtype='+ls.ssid+' recordtypebool="false">'+ls.typename+'</td></tr>';
                }
            }
            html+='</table> </div></div>';
            $("#recotdtypes").append(html);
        }
    }
    $('#recotdtypes td').eq(0).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
    $('#recotdtypes td').eq(0).attr("recordtypebool","true");

    $('#recotdtypes td').click(function() {
        var obj=this;
        var cardnum=$("#ifranmehtml").contents().find("#cardnum").val();
        layer.confirm('是否需要重置人员案件数据？', {
            btn: ['确认','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            $('#recotdtypes td').not(obj).css({"background-color":"#ffffff","color":"#000000"});
            $('#recotdtypes td').not(obj).attr("recordtypebool","false");

            $(obj).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
            $(obj).attr("recordtypebool","true");

            var url=getActionURL(getactionid_manage().addCaseToUser_toaddCaseToUserDetail);

            $("iframe").prop("src",url);
            layer.close(index);
        }, function(index){
            $('#recotdtypes td').not(obj).css({"background-color":"#ffffff","color":"#000000"});
            $('#recotdtypes td').not(obj).attr("recordtypebool","false");

            $(obj).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
            $(obj).attr("recordtypebool","true");

            layer.close(index);
        });
    });

    layui.use(['element','form'], function(){
        var element = layui.element;
        var form=layui.form;
        element.render();
        form.render();
    });
}