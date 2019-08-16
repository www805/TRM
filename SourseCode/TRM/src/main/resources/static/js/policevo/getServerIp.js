var modeltds = [];
var serverIpData;

function updateServerIp(){

    var url = getActionURL(getactionid_manage().serverip_updateServerIp);
    // var url = "/sweb/base/ip/updateServerIp";

    var trmip = $("#trmip").val();
    var flushbonadingip = $("#flushbonadingip").val();
    var asrip = $("#asrip").val();
    var polygraphip = $("#polygraphip").val();
    var storageip = $("#storageip").val();
    var ttsetinfoip = $("#ttsetinfoip").val();

    var data={
        trmip: trmip,
        flushbonadingip: {
            etip: flushbonadingip,
            ssid: serverIpData['flushbonadingip'].equipmentssid
        },
        asrip: {
            etip: asrip,
            ssid: serverIpData['asrip'].equipmentssid
        },
        polygraphip: {
            etip: polygraphip,
            ssid: serverIpData['polygraphip'].equipmentssid
        },
        storageip: {
            etip: storageip,
            ssid: serverIpData['storageip'].ssid
        },
        ttsetinfoip: {
            etip: ttsetinfoip,
            ssid: serverIpData['ttsetinfoip'].ssid
        }
    }

    ajaxSubmitByJson(url,data,callUpdateServerIp);
}

/**
 * 获取笔录客户端ip和会议模板
 */
function getServerIpList(){
    var url = getActionURL(getactionid_manage().serverip_getServerIpList);
    // var url = "/sweb/base/ip/getServerIpList";

    ajaxSubmit(url,null,callgetServerIpList);
}

/**
 * 通过会议模板查到相关设备ip
 * @param num
 */
function getServerIpALL(num){
    var url = getActionURL(getactionid_manage().serverip_getServerIpALL);
    // var url = "/sweb/base/ip/getServerIpALL";

    if(modeltds.length == 0){
        layer.msg("会议模板数组为空", {icon:5});
        return;
    }
    var modeltd = modeltds[num];

    data = {
        asrssid: modeltd.asrssid,
        polygraphssid: modeltd.polygraphssid,
        fdssid: modeltd.fdssid
    }

    ajaxSubmit(url,data,callgetServerIpALL);
}

function callgetServerIpList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var serverIplist = data.data;
        modeltds = serverIplist.modeltds;
        $("#trmip").val(serverIplist.trmip);

        for (var i = 0; i < modeltds.length; i++) {
            var modeltd = modeltds[i];
            $("#mctemplate").append("<option value='" + i + "' >" + modeltd.explain + (modeltd.grade == 1 ? " - 主麦" : " - 副麦") + "</option>");
        }

        layui.use('form', function () {
            var form = layui.form;
            form.render();
        });
        getServerIpALL(0);
    }else{
        layer.msg(data.message, {icon:5});
    }
}

function callgetServerIpALL(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        // console.log(data);

        serverIpData = data.data;
        $("#polygraphip").val(serverIpData['polygraphip'].etip);
        $("#flushbonadingip").val(serverIpData['flushbonadingip'].etip);
        $("#asrip").val(serverIpData['asrip'].etip);
        $("#storageip").val(serverIpData['storageip'].etip);
        $("#ttsetinfoip").val(serverIpData['ttsetinfoip'].etip);

    }else{
        layer.msg(data.message, {icon:5});
    }
}

function callUpdateServerIp(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("操作成功",{icon: 1});
        setTimeout("window.location.reload()",1500);
    }else{
        layer.msg(data.message, {icon:5});
    }
}



