function getArraignmentBySsid(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }

    var url=getActionURL(getactionid_manage().getArraignmentShow_getArraignmentBySsid);
    var data={
        ssid:ssid
    };
    ajaxSubmit(url,data,callbackgetArraignmentBySsid);
}

function callbackgetArraignmentBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                //全部模板
                var recordtemplates=data.recordtemplates;
                $("#recordtemplates").html("");
                if (isNotEmpty(recordtemplates)){
                    for (var i = 0; i < recordtemplates.length; i++) {
                        var recordtemplate = recordtemplates[i];
                        $("#recordtemplates").append("<option value='"+recordtemplate.ssid+"' disabled >"+recordtemplate.title+"</option>");
                    }
                }
            }
            layui.use('form', function(){
                var form = layui.form;
                //监听提交
                form.render();
            });
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}