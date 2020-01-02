/*
录像回放
*/

//回填视频文件
function sortPlayUrl(a, b) {
    return a.filenum - b.filenum;//由低到高
}
function set_getPlayUrl(data) {
    if (isNotEmpty(data)){
        iid=data.iid;
        var recordFileParams=data.recordFileParams;
        recordPlayParams=data.recordPlayParams;
        var state;
        $("#videos").empty();
        if (isNotEmpty(recordFileParams)&&isNotEmpty(recordPlayParams)){
            recordPlayParams.sort(sortPlayUrl);//重新排序一边
            dq_play=recordPlayParams[0];
            first_playstarttime=parseFloat(dq_play.recordstarttime);
            var oldname=[];
            for (let i = 0; i < recordPlayParams.length; i++) {
                var play=recordPlayParams[i];
                var playname=play.filename;
                for (let j = 0; j < recordFileParams.length; j++) {
                    const file = recordFileParams[j]
                    var filename= file.filename;
                    if (filename==playname&&oldname.indexOf(filename)<0) {
                        var VIDEO_HTML = '<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="' + play.filenum + '"  state="' + file.state + '">视频' + play.filenum + '</span>';
                        $("#videos").append(VIDEO_HTML);
                        play["start_range"] = parseFloat(play.recordstarttime) - parseFloat(first_playstarttime);
                        play["end_range"] = parseFloat(play.recordendtime) - parseFloat(first_playstarttime);
                        recordPlayParams[i] = play;
                        //时间毫秒区域计算结束------
                        oldname.push(filename);
                    }
                }
            }

            var firststate= $("#videos span:eq(0)").attr("state");
            //文件状态,0文件未获取，等待中；1文件正常，生成请求地址中；2文件可以正常使用；-1文件未正常获取，需强制获取；-2文件请求地址有误，需重新生成
            if (firststate==2) {
                videourl=dq_play.playUrl;
                initplayer();
            }else {
                layer.msg("文件获取中...",{icon: 5})
            }

            $("#videos span:eq(0)").removeClass("layui-bg-gray").addClass("layui-bg-black");
            $("#videos span").click(function () {
                $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                var filenum= $(this).attr("filenum");
                var state= $(this).attr("state");
                if (state==2) {
                    for (let i = 0; i < recordPlayParams.length; i++) {
                        const dqdata = recordPlayParams[i];
                        if (dqdata.filenum==filenum){
                            dq_play=dqdata;
                        }
                    }
                    videourl=dq_play.playUrl;
                    initplayer();
                }else {
                    layer.msg("文件获取中...",{icon: 5})
                }
            });
        }
    }else {
        $("#videos").html('<div id="datanull_1" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无视频...可能正在生成中请稍后访问</div>');
    }
}

//*******************************************************************定时请求视频地址start****************************************************************//
var getplayurl_setinterval=null;
function getPlayUrl() {
    if (isNotEmpty(iid)) {
        var url=getUrl_manage().getPlayUrl;
        var data={
            iid: iid
        };
        ajaxSubmitByJson(url, data, callbackgetPlayUrl);
    }else{
        console.log("直播信息未找到__"+iid);
    }
}
function callbackgetPlayUrl(data) {
    $("#gZIPVod_html").css("display","block");
    if(null!=data&&data.actioncode=='SUCCESS') {
        $("#gZIPVod_html").css("display","none");
        var data=data.data;
        if (isNotEmpty(data)){
            clearInterval(getplayurl_setinterval);
            set_getPlayUrl(data);
        }
    }else{
        $("#gZIPVod_html .layui-col-md9").html(data.message);
    }
}
//*******************************************************************定时请求视频地址start****************************************************************//

//视频进度
function showrecord(times) {
    $("#recorddetail label").removeClass("highlight_right");
    if (gnlist.indexOf(NX_O)!= -1){
        $("#recordreals span").css("color","#000").removeClass("highlight_left");
    }else {
        $("#recordreals span").css("color","#fff").removeClass("highlight_left");
    }
    times=parseFloat(times);
    console.log("定位差值:"+positiontime)
    if (isNotEmpty(times)&&times!=-1&&first_playstarttime!=0&&isNotEmpty(dq_play)&&isNotEmpty(recordPlayParams)){
        var isnvideo=0;//是否有视频定位点
        //检测点击的时间戳是否在当前视频中，不在切换视频并且定位
        for (let i = 0; i < recordPlayParams.length; i++) {
            const recordPlayParam = recordPlayParams[i];
            var start_range=recordPlayParam.start_range;
            var end_range=recordPlayParam.end_range;
            if (parseFloat(times)>=parseFloat(start_range)&&parseFloat(times)<=parseFloat(end_range)) {
                if (dq_play.filenum==recordPlayParam.filenum){
                    var  locationtime=(first_playstarttime+times)-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);
                    locationtime+=positiontime;
                    locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                    changeProgrss(parseFloat(locationtime));
                } else {
                    //赋值新视频,计算新的时间
                    dq_play=recordPlayParam;
                    videourl=dq_play.playUrl;
                    var locationtime=(first_playstarttime+times)-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);//重新计算时间
                    locationtime+=positiontime;
                    locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                    initplayer(parseFloat(locationtime));

                    //样式跟着改变
                    $("#videos span").each(function () {
                        var filenum=$(this).attr("filenum");
                        if (filenum==dq_play.filenum){
                            $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        }
                    });
                }
                isnvideo++;
            }
        }
        if (isnvideo==0){
            layer.msg("没有找到视频定位点",{time:500})
        }
    }
}


$(function () {
    //检测视频是否播完，播完自动进入下一个视频===================================
    SewisePlayer.onPlayTime(function(time, id){
        var totaltime=SewisePlayer.duration()==null?0:SewisePlayer.duration();
        if (parseFloat(time)==parseFloat(totaltime)&&isNotEmpty(dq_play)&&isNotEmpty(recordPlayParams)) {
            var dqfilenum=dq_play.filenum; //1
            if (dqfilenum<recordPlayParams.length){  //3
                dq_play=recordPlayParams[dqfilenum];
                videourl=dq_play.playUrl;
                initplayer();
                //样式跟着改变
                $("#videos span").each(function () {
                    var filenum=$(this).attr("filenum");
                    if (filenum==dq_play.filenum){
                        $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        return false;
                    }
                });
            }
        }

        if (isNotEmpty(time)&&time>0){
            var locationtime=time*1000<0?0:time*1000; //秒转时间戳
            locationtime=locationtime+dq_play.recordstarttime+(parseFloat(dq_play.repeattime)*1000)-first_playstarttime;
            locationtime+=positiontime;//时间戳加上毫秒差值


            //左侧
            var recordrealsdivlen=$("#recordreals").children('div').length;;//识别长度
            $("#recordreals").children('div').each(function (i,e) {
                var t=$(this).attr("starttime");
                var start=parseFloat(t);
                var end=0;
                if (i>=recordrealsdivlen-1) {
                    end= t;//下一个区间
                }else {
                    var end_=$("#recordreals").children("div:eq("+(i+1)+")").attr("starttime");
                    end=parseFloat(end_);//下一个区间
                }
                if (locationtime>=parseFloat(start)&&(parseFloat(start)==parseFloat(end)||locationtime<=parseFloat(end))) {
                    if (gnlist.indexOf(NX_O)!= -1){
                        $("#recordreals span").css("color","#000").removeClass("highlight_left");
                    }else {
                        $("#recordreals span").css("color","#fff").removeClass("highlight_left");
                    }
                    $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                    $("#asritem").hover(
                        function(){
                            mouseoverbool_left=1
                        } ,
                        function(){
                            mouseoverbool_left=-1;
                        });

                    if (parseInt(mouseoverbool_left)==-1&&parseInt(mouseoverbool_left)!=1){
                        var top=$(this).position().top;
                        var div = document.getElementById('recordreals_scrollhtml');
                        div.scrollTop = top;
                    }
                    return false;
                }
            });

            //中间
            var arrph=[];
            var dq_phdataback=null;
            if (isNotEmpty(phdatabackList)){
                locationtime=locationtime/1000<0?0:locationtime/1000; //秒转时间戳//时间戳转秒
                for (var i = 0; i < phdatabackList.length; i++) {
                    var phdataback = phdatabackList[i];
                    var num=phdataback.num;
                    var startph=num;
                    var endph=0;
                    if (i>= phdatabackList.length-1) {
                        endph= num;//下一个区间
                    }else {
                        endph=phdatabackList[i+1].num;
                    }
                    if (locationtime>=parseFloat(startph)&&(parseFloat(startph)==parseFloat(endph)||locationtime<=parseFloat(endph))) {
                        dq_phdataback = phdatabackList[i];
                        var start_i=(i-26)<0?0:(i-26);
                        var end_i=(i+25)>=phdatabackList.length?phdatabackList.length:(i+25);
                        arrph= phdatabackList.slice(start_i,end_i);
                    }
                }
                phdata(arrph,dq_phdataback);
            }
        }
    });
})
