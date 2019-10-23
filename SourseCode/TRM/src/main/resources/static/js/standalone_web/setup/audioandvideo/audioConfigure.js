var fdType = "FD_AVST";
var volumelist = [];
var sliderAll = [];
var volume0,volume1,volume2,volume3,volume4,volume5;

//获得设备音频配置
function getFDAudioConf() {
    var url=getActionURL(getactionid_manage().audioConfigure_getSQInfo);
    // var url="/cweb/setUp/basicConfigure/getFDAudioConf";

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
    var url=getActionURL(getactionid_manage().audioConfigure_setFDAudioVolume);
    // var url = "/cweb/setUp/basicConfigure/setFDAudioVolume";

    //save

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType: fdType,
            flushbonadingetinfossid: "sxsba2",
            ch: ch,
            volume: volume,
            save:0 //不保存
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

                        var sliderV = sliderAll[i];

                        if(isNotEmpty(sliderV)){
                            sliderV.setValue(audio.volume);
                        }

                        $("#audio" + i).html(audio.volume);

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

            // layer.msg(data.message,{icon: 6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}



function muteVoice(ch) {

    $("#slideP" + ch).css("color", "red");
    setFDAudioVolume(ch, 0);

}