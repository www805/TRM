var pdfdownurl=null;
var worddownurl=null;

var videourl=null;//视频地址
var dq_play=null;//当前播放视频
var recordPlayParams=null;//所有视频

//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getconversationById_getRecordById);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid:recordssid,
            }
        };
        ajaxSubmitByJson(url,data,callbackgetRecordById);
    }else{
        console.log("笔录信息未找到__"+recordssid);
    }
}


function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var record=data.record;
            pdfdownurl=record.pdfdownurl;//pdf下载地址
            worddownurl=record.worddownurl;//word下载地址

            //提讯数据
            var police_arraignment=record.police_arraignment;
            if (isNotEmpty(police_arraignment)){
                mtssid=police_arraignment.mtssid;//获取会议mtssid
                if (!isNotEmpty(mtssid)) {

                }
            }

            recordnameshow=record.recordname;//当前笔录名称

            if (isNotEmpty(record)){
                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);

                //问题答案
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
                            problemhtml+='<tr> <td class="font_blue_color">答：</td></tr>';
                        }
                        $("#recorddetail").append(problemhtml);
                    }
                    $("#recorddetail_strong").html("【审讯笔录】");

                }

                //会议人员
                var recordUserInfosdata=record.recordUserInfos;


                //案件信息
                var caseAndUserInfo=record.caseAndUserInfo;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(caseAndUserInfo)){
                    var casename=caseAndUserInfo.casename==null?"":caseAndUserInfo.casename;
                    var username=caseAndUserInfo.username==null?"":caseAndUserInfo.username;
                    var cause=caseAndUserInfo.cause==null?"":caseAndUserInfo.cause;
                    var occurrencetime=caseAndUserInfo.occurrencetime==null?"":caseAndUserInfo.occurrencetime;
                    var casenum=caseAndUserInfo.casenum==null?"":caseAndUserInfo.casenum;
                    var adminname=recordUserInfosdata.adminname==null?"":recordUserInfosdata.adminname;
                    var otheradminname=recordUserInfosdata.otheradminname==null?"":recordUserInfosdata.otheradminname;
                    var recordadminname=recordUserInfosdata.recordadminname==null?"":recordUserInfosdata.recordadminname;
                    var department=caseAndUserInfo.department==null?"":caseAndUserInfo.department;
                    var recordtypename=record.recordtypename==null?"":record.recordtypename;
                    var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>被询(讯)问人</td><td>"+username+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                set_getPlayUrl(getPlayUrlVO);
            }

        }
    }else{
        layer.msg(data.message);
    }
}

function sortPlayUrl(a, b) {
    return a.filenum - b.filenum;//由低到高
}
function  set_getPlayUrl(data) {
    if (isNotEmpty(data)){
        var iiddata=data.iid;
        var recordFileParams=data.recordFileParams;
         recordPlayParams=data.recordPlayParams;
        var state;
        $("#videos").html("");
        if (isNotEmpty(recordFileParams)&&isNotEmpty(recordPlayParams)){
            recordPlayParams.sort(sortPlayUrl);//重新排序一边
            dq_play=recordPlayParams[0];


            for (let i = 0; i < recordPlayParams.length; i++) {
                var play=recordPlayParams[i];
                var playname=play.filename;
                for (let j = 0; j < recordFileParams.length; j++) {
                    const file = recordFileParams[j];
                    var filename= file.filename;
                    if (filename==playname){
                        var VIDEO_HTML='<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="'+play.filenum+'"  state="'+file.state+'">视频'+play.filenum+'</span>';
                        $("#videos").append(VIDEO_HTML);
                    }
                }
            }

            var firststate= $("#videos span:eq(0)").attr("state");
            //文件状态,0文件未获取，等待中；1文件正常，生成请求地址中；2文件可以正常使用；-1文件未正常获取，需强制获取；-2文件请求地址有误，需重新生成
            if (firststate==2) {
                videourl=dq_play.playUrl;
                initplayer();
            }else {
                layer.msg("文件获取中...")
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
                    layer.msg("文件获取中...")
                }
            });
        }
    }
}

$(function () {
    SewisePlayer.onPlayTime(function(time, id) {
        var totaltime = SewisePlayer.duration() == null ? 0 : SewisePlayer.duration();
        if (parseFloat(time) == parseFloat(totaltime) && isNotEmpty(dq_play) && isNotEmpty(recordPlayParams)) {
            var dqfilenum = dq_play.filenum; //1
            if (dqfilenum < recordPlayParams.length) {  //3
                dq_play = recordPlayParams[dqfilenum];
                videourl = dq_play.playUrl;
                initplayer();
                //样式跟着改变
                $("#videos span").each(function () {
                    var filenum = $(this).attr("filenum");
                    if (filenum == dq_play.filenum) {
                        $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        return false;
                    }
                });
            }
        }
    });
    })