var recordstarttime=null;//录音开始时间戳
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
                /*var recordtemplates=data.recordtemplates;
                $("#recordtemplates").html("");
                if (isNotEmpty(recordtemplates)){
                    for (var i = 0; i < recordtemplates.length; i++) {
                        var recordtemplate = recordtemplates[i];
                        var recordtemplatetext=recordtemplate.title==null?"未知":recordtemplate.title;
                        $("#recordtemplates").append("<option value='"+recordtemplate.ssid+"' disabled='disabled' >"+recordtemplatetext+"</option>");
                    }
                }*/

                //全部关键字
                var keywords=data.keywords;
                $("#keywords").html("");
                if (isNotEmpty(keywords)){
                    for (var i = 0; i < keywords.length; i++) {
                        var keyword = keywords[i];
                        var text=keyword.text==null?"未知":keyword.text;
                        $("#keywords").append("  <span class='layui-badge' style='background-color:"+keyword.backgroundcolor+";color:"+keyword.color+"'>"+text+"</span>  ");
                    }
                }
                //笔录模板全部问题答案
                var problems=data.problems;
                $("#problems").html("");
                if (isNotEmpty(problems)){
                    for (var i = 0; i < problems.length; i++) {
                        var problem = problems[i];
                        var problemtext=problem.problem==null?"未知":problem.problem;
                        var problemhtml=' <tr><td class="font_red_color">问：'+problemtext+' </td></tr>';
                        var answers=problem.answers;
                        if (isNotEmpty(answers)){
                            for (var j = 0; j < answers.length; j++) {
                                var answer = answers[j];
                                var answertext=answer.answer==null?"未知":answer.answer;
                                problemhtml+='<tr> <td class="font_blue_color">答：'+answertext+' </td></tr>';
                            }
                        }else{
                            problemhtml+='<tr> <td class="font_blue_color">答：... </td></tr>';
                        }
                        $("#problems").append(problemhtml);
                    }
                }

                //笔录数据
                var record=data.record;
                $("#record").html("");
                if (isNotEmpty(record)){
                   //笔录实时文件

                    if (isNotEmpty(record.recordname)){
                        $("#recordtext").text("当前笔录："+record.recordname);
                    }

                    if(isNotEmpty(record.recordtemplatessid)){
                        $("#recordtemplates").find("option[value='"+record.recordtemplatessid+"']").attr("selected",true);
                    }

                    if (isNotEmpty(record.recordtime)){
                        $("#recordtime").text(record.recordtime);
                    }

                    if (isNotEmpty(record.recorddownurl)){
                       // wavesurfer.load('http://m10.music.126.net/20190425173525/721bc03017e3e00632223bc8ff0948b8/ymusic/363b/72ef/7661/0b373b6cdfc54e3022ef436c3ad58ec3.mp3');
                        wavesurfer.load(record.recorddownurl);
                    }

                    if (isNotEmpty(record.recordstarttime)){
                        recordstarttime=record.recordstarttime;
                    }


                   /*后期改成会议记录
                   var recordreals=record.recordreals;
                    $("#recordreals").html("");
                    if (isNotEmpty(recordreals)){

                        for (var i = 0; i < recordreals.length; i++) {
                            var recordreal = recordreals[i];
                            var recordtype=recordreal.recordtype;
                            var recordrealclass;
                            var username;
                            if (recordtype==1){
                                recordrealclass="atalk";
                                username=recordreal.askusername==null?"未知":recordreal.askusername;
                            }else if (recordtype==2){
                                recordrealclass="btalk";
                                username=recordreal.askedusername==null?"未知":recordreal.askedusername;
                            }
                            var recordrealshtml='<div class="'+recordrealclass+'" ondblclick="showrecord('+recordreal.starttime+');" >\
                                                        <p>【'+username+'】 '+recordreal.createtime+'</p>\
                                                        <span>'+recordreal.translatext+'</span> \
                                                  </div>';

                            $("#recordreals").append(recordrealshtml);
                        }

                    }*/
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

function showrecord(times) {
    if (isNotEmpty(recordstarttime)&&isNotEmpty(times)){
        var locationtime=times-recordstarttime;
        locationtime=locationtime/1000<0?0:locationtime/1000;
        wavesurfer.play(locationtime);//时间戳设置，需要处理设置**
    }
}

var wavesurfer;
$(function () {
     wavesurfer = WaveSurfer.create({
        container: '#waveform',
        scrollParent: true,
        waveColor: "#368666",
        progressColor: "#6d9e8b",
        cursorColor: "#fff",
        height: 160,
      hideScrollbar: false
    });

    wavesurfer.on("ready",function () {
        wavesurfer.play();
        $("#recordtime").text(parseInt(wavesurfer.getDuration()));
        $("#currenttime").text(wavesurfer.getCurrentTime());
    });
    wavesurfer.on("audioprocess",function () {
        $("#currenttime").text(wavesurfer.getCurrentTime());
    });




    //播放按钮
    $("#recordplay").click(function(){
        wavesurfer.play();
    });

    //停止按钮
    $("#recordpause").click(function(){
        wavesurfer.pause();
    });


    layui.use(['form', 'slider'], function(){
        var $ = layui.$
            ,slider = layui.slider;

        //定义初始值
        slider.render({
            elem: '#slideTest'
            ,max:'1'
            ,max:'100'
            ,theme: '#1E9FFF' //主题色
            ,change: function(value){
                wavesurfer.setVolume(value/100);//设置音频音量
            }
        });
    });

});



