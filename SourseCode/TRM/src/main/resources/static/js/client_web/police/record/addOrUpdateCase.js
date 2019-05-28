/**
 * 获取案件信息
 * @param ssid
 */
function getCaseBySsid(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }

    var url=getActionURL(getactionid_manage().addOrUpdateCase_getCaseBySsid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casessid:ssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCaseBySsid);
}
function callbackgetCaseBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var caseAndUserInfo=data.caseAndUserInfo;
                if (isNotEmpty(caseAndUserInfo)){
                    $("#casename").val(caseAndUserInfo.casename);
                    $("#cause").val(caseAndUserInfo.cause);
                    $("#casenum").val(caseAndUserInfo.casenum);
                    $("#occurrencetime").val(caseAndUserInfo.occurrencetime);
                    $("#caseway").val(caseAndUserInfo.caseway);
                    $("#userssid").val(caseAndUserInfo.userssid);
                    $("#userssid").find("option[value='"+caseAndUserInfo.userssid+"']").attr("selected",true);
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var form =  layui.form;

        form.render();
    });
}
function addOrUpdateCase() {
    var url=null;
    if (isNotEmpty(ssid)){
        url=getActionURL(getactionid_manage().addOrUpdateCase_updateCase);
    }else{
        url=getActionURL(getactionid_manage().addOrUpdateCase_addCase);
    }
    var casename=$("#casename").val();
    var cause=$("#cause").val();
    var casenum=$("#casenum").val();
    var occurrencetime=$("#occurrencetime").val();
    var caseway=$("#caseway").val();
    var userssid=$("#userssid").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid:ssid,
            casename:casename,
            cause:cause,
            casenum:casenum,
            occurrencetime:occurrencetime,
            caseway:caseway,
            userssid:userssid
        }
    };
    ajaxSubmitByJson(url,data,callbackaddOrUpdateCase);
}
function callbackaddOrUpdateCase(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("保存成功",{icon: 1,time:500},function () {
                    var nextparam=getAction(getactionid_manage().addOrUpdateCase_updateCase);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_CLIENT,nextparam.nextPageId);
                        var url=getActionURL(getactionid_manage().main_tocaseIndex);
                        window.location.href=url;
                    }
                });
            }
        }
    }else{
        layer.msg(data.message);
    }
}


function getUserinfoList() {
    var url=getActionURL(getactionid_manage().addOrUpdateCase_getUserinfoList);
    var data={
        token:INIT_CLIENTKEY,
        param:{
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserinfoList);
}

function callbackgetUserinfoList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var userinfos=data.userinfos;
                $('#userssid option').not(":lt(1)").remove();
                if (isNotEmpty(userinfos)){
                    for (var i = 0; i < userinfos.length; i++) {
                        var l = userinfos[i];
                        $("#userssid").append("<option value='"+l.ssid+"'> "+l.username+"</option>");
                    }
                    if (isNotEmpty(ssid)) {
                        getCaseBySsid(ssid);
                    }
                }

            }
        }
    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var form =  layui.form;

        form.render();
    });
}