var fdType = "FD_AVST";


//获得设备音频配置
function getFDAudioConf() {
    // var url=getActionURL(getactionid_manage().softAndHardInfo_getSQInfo);
    var url="/cweb/setUp/basicConfigure/getFDAudioConf";

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType:fdType,
            flushbonadingetinfossid: "sxsba2"
        }
    };

    ajaxSubmitByJson(url, data, callgetFDAudioConf);
}

//设置设备某一个通道的通道音量
function setFDAudioVolume(ch, volume) {
    // var url=getActionURL(getactionid_manage().softAndHardInfo_getSQInfo);
    var url = "/cweb/setUp/basicConfigure/setFDAudioVolume";

    //save

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType: fdType,
            flushbonadingetinfossid: "sxsba2",
            ch: ch,
            volume: volume
        }
    };

    ajaxSubmitByJson(url, data, callsetFDAudioVolume);
}

function callgetFDAudioConf(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            console.log(data);

            if(isNotEmpty(data.data.audiolist)){
                var audiolist = data.data.audiolist;
                for(var i=0; i<audiolist.length; i++){
                    var audio = audiolist[i];

                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callsetFDAudioVolume(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            console.log(data);

            layer.msg(data.message,{icon: 6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function muteVoice(ch) {





}