var recorduser=[];//会议用户集合
var mtssid=null;//当前会议的ssid



function opneModal_1() {
    var url=getActionURL(getactionid_manage().getRecordById_tomoreRecord);

    var index = layer.open({
        type: 2,
        title:'笔录选择',
        content:url,
        area: ['1000px', '680px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            var recordssid_go = $(window.frames["layui-layer-iframe"+index])[0].recordssid_go;
            recordssid=recordssid_go;
            getRecordById();
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

/**
 * 局部刷新
 */
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getRecords_init(currPage,pageSize);
    }else if (len==2){
        getRecords('',arguments[0],arguments[1]);
    }else if(len>2){
        getRecords(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
    }

}
function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var recordtypessid=pageparam.recordtypessid;
        var recordname=pageparam.recordname;


        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
    }

}

/*弹出框数据*/




function getRecordById() {
    var url=getActionURL(getactionid_manage().getRecordById_getRecordById);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordById);
}
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var record=data.record;
            var caseAndUserInfo=data.caseAndUserInfo;
            if (isNotEmpty(record)){
                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);
                if (isNotEmpty(record.recorddownurl)){
                    wavesurfer.load(record.recorddownurl);
                }
                    var problems=record.problems;
                    $("#recorddetail").html("");
                    if (isNotEmpty(problems)) {
                        for (var z = 0; z< problems.length;z++) {
                            var problem = problems[z];
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
                            $("#recorddetail").append(problemhtml);
                        }
                    }

                    //会议人员
                    var recordUserInfosdata=record.recordUserInfos;
                    if (isNotEmpty(recordUserInfosdata)){
                        var user1={
                            username:recordUserInfosdata.username
                            ,userssid:recordUserInfosdata.userssid
                            ,grade:2
                        };
                        var user2={
                            username:recordUserInfosdata.adminname
                            ,userssid:recordUserInfosdata.adminssid
                            ,grade:1
                        };
                        recorduser.push(user1);
                        recorduser.push(user2);
                    }

                    //提讯数据
                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    mtssid=police_arraignment.mtssid;
                }
                getRecord();//实时数据获取
            }

            $("#caseAndUserInfo_html").html("");
            if (isNotEmpty(caseAndUserInfo)){
                var  init_casehtml="<tr><td>案件名称</td><td>"+caseAndUserInfo.casename+"</td></tr>\
                                  <tr><td>案件人</td><td>"+caseAndUserInfo.username+"</td> </tr>\
                                  <tr><td>当前案由</td><td>"+caseAndUserInfo.cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+caseAndUserInfo.occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+caseAndUserInfo.casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+recordUserInfosdata.adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+recordUserInfosdata.otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordUserInfosdata.recordadminname+"</td> </tr>";
                $("#caseAndUserInfo_html").html(init_casehtml);
            }

        }
    }else{
        layer.msg(data.message);
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


/**
 * 获取会议实时数据
 */
function getRecord() {
    $("#recordreals").html("");
    if (isNotEmpty(mtssid)) {
        var url="/v1/police/out/getRecord";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetRecord);
    }
}
function callbackgetRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var datas = data.data;
        var loadindex = layer.msg("加载中，请稍等...", {
            icon: 16,
            time:1000
        });
        if (isNotEmpty(datas)) {
            layer.close(loadindex);
            for (var i = 0; i < datas.length; i++) {
                var data=datas[i];
                if (isNotEmpty(recorduser)){
                    for (var j = 0; j < recorduser.length; j++) {
                        var user = recorduser[j];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var asrtime=data.asrtime;//时间
                            var starttime=data.starttime;
                            //实时会议数据
                            if (usertype==1){
                                recordrealclass="atalk";

                            }else if (usertype==2){
                                recordrealclass="btalk";

                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").remove();
                            }
                            var recordrealshtml='<div class="'+recordrealclass+'" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrtime+'</p>\
                                                            <span ondblclick="copy_text(this)" >'+translatext+'</span> \
                                                      </div >';

                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals');
                            div.scrollTop = div.scrollHeight;
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
}