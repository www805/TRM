var fdType = "FD_AVST";
var sliderAll = [];
var volume0;
var flushbonadingetinfossid;

//获取默认设备的ssid
function getFDssid() {
    var url=getActionURL(getactionid_manage().audioConfigure_getFDssid);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var data = {
        token: INIT_CLIENTKEY,
        param: {}
    };

    ajaxSubmitByJson(url, data, callgetFDssid);
}

//获得设备音频配置
function getFDAudioConf() {
    var url=getActionURL(getactionid_manage().audioConfigure_getSQInfo);
    // var url="/cweb/setUp/basicConfigure/getFDAudioConf";

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType:fdType,
            flushbonadingetinfossid: flushbonadingetinfossid
        }
    };

    ajaxSubmitByJson(url, data, callgetFDAudioConf);
}

//设置设备某一个通道的通道音量
function setFDAudioVolume(ch, volume, save) {
    var url = getActionURL(getactionid_manage().audioConfigure_setFDAudioVolume);
    // var url = "/cweb/setUp/basicConfigure/setFDAudioVolume";

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType: fdType,
            flushbonadingetinfossid: flushbonadingetinfossid,
            ch: ch,
            volume: volume,
            save: save //不保存
        }
    };

    ajaxSubmitByJson(url, data, callsetFDAudioVolume);
}


//保存声音设备
function saveVoice() {
    slideTestKey = -1;

    var url = getActionURL(getactionid_manage().audioConfigure_saveFDAudioVolume);
    // var url = "/cweb/setUp/basicConfigure/saveFDAudioVolume";

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var fdAudioVolumeList = [];

    for (var i = 0; i <= 5; i++) {
        var volume = $("#audio" + i).html();
        var FDAudioVolume={
            ch:i,
            volume:volume,
            save:1,
            fdType: fdType,
            flushbonadingetinfossid: flushbonadingetinfossid,
        }
        fdAudioVolumeList.push(FDAudioVolume);
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdAudioVolumeList: fdAudioVolumeList
        }
    };

    ajaxSubmitByJson(url, data, callsaveVoice);
}

function callgetFDAudioConf(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            // console.log(data);

            if(isNotEmpty(data.data.audiolist)){
                    var audiolist = data.data.audiolist;
                    for(var i=0; i<audiolist.length; i++){
                        var audio = audiolist[i];

                        var sliderV = sliderAll[i];

                        if(isNotEmpty(sliderV)){
                            sliderV.setValue(audio.volume);
                        }
                        $("#audio" + i).html(audio.volume);
                    }

                layui.form.render();
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callsetFDAudioVolume(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            // console.log(data);

            // layer.msg(data.message,{icon: 6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callgetFDssid(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            flushbonadingetinfossid = data.data;
            getFDAudioConf();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callsaveVoice(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg(data.message,{icon: 6});
            setTimeout("location.replace(location.href);",2000);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//设置静音
function muteVoice(ch) {

    volume0 = 0;
    slideTestKey = ch;

    $("#audio" + ch).html(0);
    //setFDAudioVolume(ch, 0, 0);

    var sliderV = sliderAll[ch];

    if(isNotEmpty(sliderV)){
        sliderV.setValue(0);
    }
}



