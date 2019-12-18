var recorduser=[];//会议用户集合
var mtssid=null;//当前会议的ssid
var videourl=null;//视频地址

var recordnameshow="";

var subtractime_q=0;//问的时间差
var subtractime_w=0;//答的时间差


var pdfdownurl=null;//pdf下载地址
var worddownurl=null;//word下载地址

var phdatabackList=null;//身心回放数据

var first_playstarttime=0;//第一个视频的开始时间
var dq_play=null;//当前视频数据
var recordPlayParams=[];//全部视频数据集合

var phSubtracSeconds=0;

var iid=null;//打包iid

var getRecordById_data=null;
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

var positiontime=0;




/**
 * 局部刷新
 */
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
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


//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getRecordById_getRecordById);
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


function setqw(problems) {
    $("#recorddetail").empty();
    if (isNotEmpty(problems)){
        var problemhtml="";
        $("#datanull_2").hide();

        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];

            var problemstarttime=problem.starttime;
            var q_starttime=problemstarttime
            if (isNotEmpty(problemstarttime)&&problemstarttime!=-1) {
                q_starttime= problemstarttime+parseFloat(subtractime_q);
            }

            var problemtext=problem.problem==null?"未知":problem.problem;
             problemhtml+= '<tr>\
                        <td style="padding: 0;width: 95%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>问：</span><label name="q" contenteditable="false" times="'+q_starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];

                    var answerstarttime=answer.starttime;
                    var w_starttime=answerstarttime;
                    if (isNotEmpty(answerstarttime)&&answerstarttime!=-1) {
                        w_starttime=answerstarttime+parseFloat(subtractime_w);
                    }

                    var answertext=answer.answer==null?"未知":answer.answer;
                    problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" times="'+w_starttime+'" >'+answertext+'</label></div>';
                }
            }else{
                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" ></label></div>';

            }
            problemhtml+=' <div  id="btnadd" style="display: none;"></div></td>\
                        <td style="display: none" id="record_util">\
                            <div class="layui-btn-group">\
                            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                        <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                        <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                         </div>\
                        </td>\
                        </tr>';
        }

        return problemhtml;
    }
    return "";
}
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            getRecordById_data=data;
            var record=data.record;
            pdfdownurl=record.pdfdownurl;//pdf下载地址
            worddownurl=record.worddownurl;//word下载地址
            iid=data.iid;

            recordnameshow=record.recordname;//当前笔录名称

            var modeltds=data.modeltds;
            if (isNotEmpty(modeltds)&&isNotEmpty(gnlist)){
                var asrbool=0;//使用语音识别的个数
                var phbool=0;//使用身心检测的个数

                for (let i = 0; i < modeltds.length; i++) {
                    const modeltd = modeltds[i];
                    var usepolygraph=modeltd.usepolygraph;
                    var useasr=modeltd.useasr;
                    if (isNotEmpty(usepolygraph)&&usepolygraph==1) {phbool++;}
                    if (isNotEmpty(useasr)&&useasr==1) {asrbool++;}
                }
                console.log("asrbool__"+asrbool+"__phbool__"+phbool)
                //存在语音识别授权并且模板中使用语音识别的个数大于0
                if (isNotEmpty(gnlist)&&gnlist.indexOf(ASR_F)>0&&asrbool>0){
                    $("#asr").show();
                }
                if (isNotEmpty(gnlist)&&gnlist.indexOf(PH_F)>0&&phbool>0){
                    $("#mood").show();
                    $("#generatePhreport").show();
                }
                if (isNotEmpty(gnlist)&&gnlist.indexOf(FY_T)==-1) {
                    //法院版暂未提供案件人员编辑页
                    $("#open_casetouser").css("display","inline-block");
                }
            }

            if (isNotEmpty(record)){
                positiontime=record.positiontime==null?0:record.positiontime;
                $("#positiontime").val(positiontime);

                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }

                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname).attr("title",record.recordname==null?"笔录标题":record.recordname);
                $("#recorddetail_strong").html('【笔录问答】<i class="layui-icon layui-icon-edit" style="font-size: 20px;color: red;visibility: hidden" title="编辑" id="open_recordqw" onclick="open_recordqw()"></i>');

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
                    recorduser=[];
                    recorduser.push(user1);
                    recorduser.push(user2);
                }

                //案件信息
                var case_=record.case_;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(case_)){
                    //显示编辑按钮
                    var casebool=case_.casebool;
                    if (isNotEmpty(casebool)&&(casebool==0||casebool==1)){
                        $("#open_recordqw").css("visibility","visible");
                        $("#open_casetouser").css("visibility","visible");
                    }

                    var casename=case_.casename==null?"":case_.casename;
                    var username=recordUserInfosdata.username==null?"":recordUserInfosdata.username;
                    var cause=case_.cause==null?"":case_.cause;
                    var occurrencetime=case_.occurrencetime==null?"":case_.occurrencetime;
                    var starttime=case_.starttime==null?"":case_.starttime;
                    var casenum=case_.casenum==null?"":case_.casenum;
                    var adminname=recordUserInfosdata.adminname==null?"":recordUserInfosdata.adminname;
                    var otheradminname=recordUserInfosdata.otheradminname==null?"":recordUserInfosdata.otheradminname;
                    var recordadminname=recordUserInfosdata.recordadminname==null?"":recordUserInfosdata.recordadminname;
                    var department=case_.department==null?"":case_.department;
                    var recordtypename=record.recordtypename==null?"":record.recordtypename;
                    var userInfos=case_.userInfos;
                    var USERHTNL="";
                    if(null!=userInfos) {for (let i = 0; i < userInfos.length; i++) {const u = userInfos[i];USERHTNL += u.username + "、";} USERHTNL = (USERHTNL .substring(USERHTNL .length - 1) == '、') ? USERHTNL .substring(0, USERHTNL .length - 1) : USERHTNL ;}
                    var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>被询(讯)问人</td><td>"+username+"</td> </tr>\
                                  <tr><td>案件嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+starttime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>\
                                  <tr><td>笔录类型</td><td>"+recordtypename+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
            }

            getRecordrealByRecordssid();//右侧数据
            setInterval( function() {
                setRecordreal();//3秒实时保存
            },3000);

            var phDataBackVoParams=data.phDataBackVoParams;
            if (isNotEmpty(phDataBackVoParams)){
                phdatabackList=phDataBackVoParams;
                phSubtracSeconds=phdatabackList[0].phSubtracSeconds==null?0:phdatabackList[0].phSubtracSeconds;
                $("#fd_ph_HTML").attr("class","layui-col-md5").show();
                $("#record_qw_HTML").attr("class","layui-col-md7").show();
                $(".phitem2").html("");//将tabdata下的身心检测去掉
                $("#ph_HTML,#allmonitor,#allranking").show();
                main1();//身心统计回放
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                $("#fd_ph_HTML").attr("class","layui-col-md5").show();
                $("#record_qw_HTML").attr("class","layui-col-md7").show();
                $("#fd_HTML").show();
                set_getPlayUrl(getPlayUrlVO);
            }else {
                if (isNotEmpty(iid)){
                    getplayurl_setinterval= setInterval(function () {
                        getPlayUrl();
                    },5000)
                }
                $("#videos").html('<div id="datanull_1" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无视频...可能正在生成中请稍后访问</div>');
            }

            //左侧asr识别数据
            var getMCVO=data.getMCVO;
            if (isNotEmpty(getMCVO)&&isNotEmpty(getMCVO.list)){
                set_getRecord(getMCVO);
            }else  {
                $("#recordreals").html('<div id="datanull_3" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无语音对话...可能正在生成中请稍后访问</div>');
            }

            //提讯数据
            var police_arraignment=record.police_arraignment;
            if (isNotEmpty(police_arraignment)){
                mtssid=police_arraignment.mtssid;//获取会议mtssid
                if (!isNotEmpty(mtssid)) {
                    //不存在会议
                }
            }

            //情绪报告列表
            getPhreportsByParam();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}



//数据渲染
function set_getRecord(data){
    if (isNotEmpty(data.list)){
        $("#recordreals").empty();
        $("#recordreals_selecthtml").show();
        var list=data.list;
        for (var i = 0; i < list.length; i++) {
            var data=list[i];
            if (isNotEmpty(recorduser)){
                for (var j = 0; j < recorduser.length; j++) {
                    var user = recorduser[j];
                    var userssid=user.userssid;
                    if (data.userssid==userssid){
                        var username=user.username==null?"未知":user.username;//用户名称
                        var usertype=user.grade;//1、询问人2被询问人
                        var txt=data.txt==null?"...":data.txt;//翻译文本
                        var asrtime=data.asrtime;//时间
                        var starttime=data.starttime;
                        var asrstartime=data.asrstartime;
                        var subtractime=data.subtractime;//时间差
                        //实时会议数据
                        var recordrealshtml="";
                        var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本


                        //情绪报告找到紧张值最高点=======================================================================start:只需要正对答：摄像头对着被问人的
                        var ph_stress=-1;//最高点 -1为不正常数据
                        var dqphdate="";//找出来最大值的全部data
                       var phbadge="background-color: #00CD68 !important";
                       var phtitle="最高紧张值";
                       if (ph_stress>=0&&ph_stress<=30){
                          phbadge="background-color: #00CD68 !important";
                        }else if (ph_stress>30&&ph_stress<=50){
                          phbadge="background-color: #e4e920 !important";
                        }else if (ph_stress>50&&ph_stress<=70){
                          phbadge="background-color: #ff840f !important";
                        }else if (ph_stress>70&&ph_stress<=100){
                          phbadge="background-color: #e90717 !important";
                        }else {
                          phtitle="未找到最高值";
                        }
                        //情绪报告找到紧张值最高点=======================================================================start

                        //实时会议数据
                        if (usertype==1){
                            //询问人没有情绪报告
                            subtractime_q=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_q);
                            recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)" times='+starttime+' usertype='+usertype+' dqphdate='+dqphdate+'>\
                                                            <a style="display: none;color: #ccc" id="dqphdate">'+dqphdate+'</a>\
                                                            <p><a id="username_time">【'+username+'】 '+asrstartime+' </a><a class="layui-badge" style="display:none; '+phbadge+' " title='+phtitle+'>'+ph_stress+'</a></p>\
                                                            <span id="translatext">'+translatext+'</span> \
                                                      </div >';
                            /*recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)" times='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+'</p>\
                                                            <span>'+translatext+'</span> \
                                                      </div >';*/
                        }else if (usertype==2){
                            subtractime_w=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_w);
                            recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)" times='+starttime+' usertype='+usertype+' dqphdate='+dqphdate+'>\
                                                            <a style="display: none;color: #ccc" id="dqphdate">'+dqphdate+'</a>\
                                                            <p><a class="layui-badge " style="visibility:hidden;  '+phbadge+'" title='+phtitle+'>'+ph_stress+'</a>  <a  id="username_time">'+asrstartime+' 【'+username+'】</a> </p>\
                                                            <span id="translatext">'+translatext+'</span> \
                                                      </div >';
                        }
                        var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                        if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                            $("#recordreals div[userssid="+userssid+"]:last").remove();
                        }
                        $("#recordreals").append(recordrealshtml);
                        var div = document.getElementById('recordreals');
                        div.scrollTop = div.scrollHeight;
                    }
                }
            }
        }

        var recordreals_selecthtml=document.getElementById("recordreals_selecthtml");
        var IHTML='<span class="layui-table-sort layui-inline" title="语音识别可滚动"><i class="layui-edge layui-table-sort-asc"></i><i class="layui-edge layui-table-sort-desc" "></i></span>';
        if(recordreals_selecthtml.scrollHeight>recordreals_selecthtml.clientHeight||recordreals_selecthtml.offsetHeight>recordreals_selecthtml.clientHeight){
            $("#webkit2").html(IHTML)
        }else {
            $("#webkit2").empty();
        }
    }else {
        $("#recordreals").html('<div id="datanull_3" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无语音对话...可能正在生成中请稍后访问</div>');
    }
    //存在问答需要获取时间差
    getRecordrealByRecordssid();

    layui.use(['layer','form'], function(){
        var form = layui.form;
        form.render();
    });
}

function sortPlayUrl(a, b) {
    return a.filenum - b.filenum;//由低到高
}


function  set_getPlayUrl(data) {
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

function btn(obj) {
    var selected=$(obj).closest("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $(obj).closest("div[name='btn_div']").attr("showorhide","true");
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $(obj).closest("div[name='btn_div']").attr("showorhide","false");
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}
function exportWord(obj){
        //调用导出方法
        layer.msg("导出中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });
        var url=getActionURL(getactionid_manage().getRecordById_exportWord);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
            }
        };
        ajaxSubmitByJson(url, paramdata, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    var word_htmlpath=data.word_htmlpath;//预览html地址
                    var word_path=data.word_path;//下载地址
                    window.location.href = word_path;
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
    btn(obj);
}
function exportPdf(obj) {
        //调用导出方法
        layer.msg("导出中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });
        var url=getActionURL(getactionid_manage().getRecordById_exportPdf);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
            }
        };
        ajaxSubmitByJson(url, paramdata, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    //window.location.href = data;
                    layer.open({
                        id:"pdfid",
                        type: 1,
                        title: '导出PDF笔录',
                        shadeClose: true,
                        shade: false,
                        maxmin: true, //开启最大化最小化按钮
                        area: ['893px', '600px'],
                    });
                    showPDF("pdfid",pdfdownurl);
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
    btn(obj);
}


//视频进度
function showrecord(times,oldtime) {
    $("#recorddetail label").removeClass("highlight_right");
    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
    times=parseFloat(times);

    console.log("双击:"+positiontime)

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

        /* 视频播放会自动定位变色 此处可以不处理
                var recorddetailtrlen= $("#recorddetail label").length;
                $("#recorddetail label").each(function (i,e) {
                    var t1=$(this).attr("times");
                    var name=$(this).attr("name");
                    if (t1==times) {//需要减去差值
                        $(this).addClass("highlight_right");
                        var top=$(this).position().top;
                        var div = document.getElementById('recorddetail');
                        div.scrollTop = top;
                        return false;
                    }
                });

                $("#recordreals div").each(function (i,e) {
                    var t2=$(this).attr("times");
                    if (t2==times) {
                        $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                        var top=$(this).position().top;
                        var div = document.getElementById('recordreals_scrollhtml');
                        div.scrollTop = top;
                        return false;
                    }
                });*/
    }
}
var dqindex_realtxt=0;//当前显示的下标
var likerealtxtarr=[];//搜索txt
//搜索上
function last_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt--;
        if (dqindex_realtxt<0){
            dqindex_realtxt=0;
            layer.msg("这是第一个~");
        }
        set_dqrealtxt();
    }
}
//搜索下
function next_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt++;
        if (dqindex_realtxt>=likerealtxtarr.length-1){
            dqindex_realtxt=likerealtxtarr.length-1;
            layer.msg("这是最后一个~");
        }
        set_dqrealtxt();
    }
}
//搜索赋值
function set_dqrealtxt(){
    mouseoverbool=1;//不滚动
    if (isNotEmpty(likerealtxtarr)) {
        for (let i = 0; i < likerealtxtarr.length; i++) {
            const all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        var div = document.getElementById('recordreals_scrollhtml');
        div.scrollTop = top;
    }
}

function recordreals_select() {
    mouseoverbool=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];

    var recordrealshtml= $("#recordreals").html();
    recordrealshtml=recordrealshtml.replace(/(<\/?a.*?>)/g, '');
    $("#recordreals").html(recordrealshtml);
    $("#recordreals").children('div').each(function (i,e) {
        var spantxt=$(this).find("span").text();
        if (isNotEmpty(likerealtxt)){
            if (spantxt.indexOf(likerealtxt) >= 0) {
                var html=$(this).find("span").html();
                html = html.split(likerealtxt).join('<a class="highlight_all">'+ likerealtxt +'</a>');
                $(this).find("span").html(html);
                likerealtxtarr.push($(this).find("span"));
            }
        }
    });

    if (isNotEmpty(likerealtxtarr)){
        set_dqrealtxt();
    }else {
        /*layer.msg("没有找到内容~");*/
    }
}

var phreportfirst=-1;//是否为第一次打开情绪报告
$(function () {
    layui.use(['layer','element','slider','form','laydate'], function(){
        var form = layui.form;
        form.on('switch(phreportshowhide)', function(data){
            if (!isNotEmpty(dq_play)){
                layer.msg("暂未找到视频，未能出现情绪报告");
                return;
            }
            if (!isNotEmpty(phdatabackList)){
                layer.msg("暂未找到身心监测数据，未能出现情绪报告");
                return;
            }

            var bool=data.elem.checked;
            if (bool) {
                //显示checkbox
                $("#phreportshowhidemsg").text("（提示:勾选需要生成的情绪数据)");
                $("#uploadPhreport").css("visibility","visible");
                $("#recordreals #dqphdate").css("display", "block");
                $("#recordreals").children('div').each(function () {
                    var talkclass=$(this).attr("class");
                    if (talkclass=="atalk"){
                        $("p",this).prepend('<input type="checkbox" name="" lay-skin="primary" >');
                    }else if (talkclass=="btalk"){
                        $("#recordreals .layui-badge").css("visibility", "visible");
                        $("p",this).append('<input type="checkbox" name="" lay-skin="primary" >');
                    }
                });
                console.log("phreportfirst__"+phreportfirst)
                if (isNotEmpty(phdatabackList)&&isNotEmpty(dq_play)&&phreportfirst==-1){
                    //身心监测数据时间点已在后台计算好差值，首先先获取asr差值后的数据
                    layer.msg("情绪加载中，请稍等",{icon:16})
                    var ph_stress=-1;//最高点 -1为不正常数据
                    var dqphdate="";//找出来最大值的全部data
                    $("#recordreals").children('div').each(function (i,e) {
                        var starttime=$(this).attr("times");
                        var usertype=$(this).attr("usertype");
                        if (usertype==2) {
                            var  locationtime=(parseFloat(first_playstarttime)+parseFloat(starttime))-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);
                            locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                            //获取该时间的前五秒和后五秒
                            console.log(locationtime)
                            var arrph=[];//找出来的集合
                            for (var j = 0; j < phdatabackList.length; j++) {
                                var phdataback = phdatabackList[j];
                                var num=phdataback.num;
                                var phBataBackJson=phdataback.phBataBackJson;
                                var obj=eval("(" + phBataBackJson + ")");
                                var startph=num;
                                var endph=0;
                                if (j>= phdatabackList.length-1) {
                                    endph= num;//下一个区间
                                }else {
                                    endph=phdatabackList[j+1].num;
                                }
                                if (locationtime>=parseFloat(startph)&&(parseFloat(startph)==parseFloat(endph)||locationtime<=parseFloat(endph))) {
                                    var start_i=(j-4)<0?0:(j-4);
                                    var end_i=(j+6)>=phdatabackList.length?phdatabackList.length:(j+6);
                                    arrph= phdatabackList.slice(start_i,end_i);
                                }
                            }

                            if (isNotEmpty(arrph)){
                                var max_stress = 0;
                                var max_stress_i = 0;
                                for (var z = 0; z < arrph.length; z++) {
                                    var data = arrph[z];
                                    if(isNotEmpty(data)){
                                        var phBataBackJson=data.phBataBackJson;
                                        var obj=eval("(" + phBataBackJson + ")");
                                        if (isNotEmpty(obj)) {//&&obj.hr_snr>=0.1
                                            var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                                            var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);
                                            if (stress > max_stress) {
                                                max_stress =stress;
                                                max_stress_i = z;
                                            }
                                        }
                                    }
                                }
                                ph_stress=max_stress==null?0:max_stress;
                                dqphdate=arrph[max_stress_i]==null?"":arrph[max_stress_i];
                                if (isNotEmpty(dqphdate)){
                                    var phBataBackJson=dqphdate.phBataBackJson;
                                    var obj=eval("(" + phBataBackJson + ")");
                                    var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                                    var br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                                    var relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                                    var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                                    var bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                                    var spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                                    var hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);
                                    dqphdate="(紧张值:"+stress+";呼吸次数:"+br+";心率:"+hr+";血氧:"+spo2+";血压变化:"+bp+")";
                                }
                            }


                            var phbadge="#00CD68 !important";
                            var phtitle="最高紧张值";
                            if (ph_stress>=0&&ph_stress<=30){
                                phbadge="#00CD68 !important";
                            }else if (ph_stress>30&&ph_stress<=50){
                                phbadge="#e4e920 !important";
                            }else if (ph_stress>50&&ph_stress<=70){
                                phbadge="#ff840f !important";
                            }else if (ph_stress>70&&ph_stress<=100){
                                phbadge="#e90717 !important";
                            }else {
                                phtitle="未找到最高值";
                            }
                            $(this).attr("dqphdate",dqphdate);
                            $(this).find("#dqphdate").html(dqphdate);
                            $(this).find(".layui-badge").css("background-color",phbadge);
                            $(this).find(".layui-badge").attr("title",phtitle).html(ph_stress);
                        }
                    });
                    phreportfirst=1;
                }
                form.render();
            }else {
                //隐藏并且checkbox
                $("#phreportshowhidemsg").text("（提示:开启可勾选生成情绪报告)");
                $("#uploadPhreport").css("visibility","hidden");
                $("#recordreals .layui-badge").css("visibility", "hidden");
                $("#recordreals #dqphdate").css("display", "none");
                $("#recordreals input[type=checkbox]").remove();
                $("#recordreals .layui-form-checkbox").remove();
            }

        });
    });

    $("#baocun").click(function () {
      addRecord();
    });

    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    newDate.setDate(newDate.getDate());
    var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()];
    setinterval1= setInterval( function() {
        var seconds = new Date().getSeconds();
        var sec=( seconds < 10 ? "0" : "" ) + seconds;
        var minutes = new Date().getMinutes();
        var min=( minutes < 10 ? "0" : "" ) + minutes;
        var hours = new Date().getHours();
        var hour=( hours < 10 ? "0" : "" ) + hours;

        if (isNotEmpty(select_monitorall_iframe_body)) {
            /*  select_monitorall_iframe_body.find("#dqtime").html(date+ hour + "：" + min + "：" + sec);*/

            var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ';
            var week=dayNames[newDate.getDay()];
            var time=hour + "：" + min + "：" + sec;
            select_monitorall_iframe_body.find("#dqtime1").html(date);
            select_monitorall_iframe_body.find("#dqtime2").html(week+time);
        }

    },1000);





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


        /!*此处开始定位*!/
        if (isNotEmpty(time)&&time>0){
            var locationtime=time*1000<0?0:time*1000; //秒转时间戳
            locationtime=locationtime+dq_play.recordstarttime+(parseFloat(dq_play.repeattime)*1000)-first_playstarttime;
            locationtime+=positiontime;//时间戳加上毫秒差值


            //左侧
            var recordrealsdivlen=$("#recordreals").children('div').length;;//识别长度
            console.log(recordrealsdivlen)
            $("#recordreals").children('div').each(function (i,e) {
                var t=$(this).attr("times");
                var start=parseFloat(t);
                var end=0;
                if (i>=recordrealsdivlen-1) {
                    end= t;//下一个区间
                }else {
                    var end_=$("#recordreals").children("div:eq("+(i+1)+")").attr("times");
                    end=parseFloat(end_);//下一个区间
                }
                if (locationtime>=parseFloat(start)&&(parseFloat(start)==parseFloat(end)||locationtime<=parseFloat(end))) {
                    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
                    $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                    $("#asritem").hover(
                        function(){
                            mouseoverbool=1
                        } ,
                        function(){
                            mouseoverbool=-1;
                        });

                    if (parseInt(mouseoverbool)==-1&&parseInt(mouseoverbool)!=1){
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


});


//导出下载
var gZIPVod_index;
var gZIPVod_Url;
var gZIPVodtime=0;//打包请求秒数间隔
function gZIPVod(){
    if (gZIPVodtime>0){
        layer.msg("打包太频繁，请"+gZIPVodtime+"秒后再试",{icon:5});
        return;
    }



    var gZIPVodsetInterval=setInterval(function () {
        gZIPVodtime--;
        if (gZIPVodtime<1){
            clearInterval(gZIPVodsetInterval);
        }
    },1000)

    if (gZIPVodtime<1){
        if (isNotEmpty(iid)){
            $("#gZIPVod_html").css("display","none");
            gZIPVod_index=layer.msg("打包中，请稍等...", {
                icon: 16,
                shade: [0.1, 'transparent'],
                time: 100000
            });
            var url=getActionURL(getactionid_manage().getRecordById_gZIPVod);
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid:recordssid,
                    iid:iid,
                    zipfilename:recordnameshow
                }
            };
            gZIPVodtime=15;//15秒
            ajaxSubmitByJson(url,data,callbackgZIPVod);
        } else {
            layer.msg("请先确认视频文件是否生成...",{icon: 5});
        }
    }


}
var timer_zIPVodProgress;
function callbackgZIPVod(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        layer.close(gZIPVod_index);
        gZIPVod_Url=data;

        //开始请求获取进度
         timer_zIPVodProgress=setInterval(function () {
            zIPVodProgress();
        },1000)

    }else {
        layer.msg(data.message,{icon: 5});
    }
}

function zIPVodProgress() {
    var url=getActionURL(getactionid_manage().getRecordById_zIPVodProgress);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            iid:iid,
            zipfilename:recordnameshow
        }
    };
    ajaxSubmitByJson(url,data,callbackzIPVodProgress);
}
function callbackzIPVodProgress(data) {
    $("#gZIPVod_html").css("display","block");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        //开始显示进度
        if (isNotEmpty(data)){
            $("#gZIPVod_html .layui-col-md9").empty();
            var totalzipnum=data.totalzipnum==null?0:data.totalzipnum;//总共有多少个需要打包的文件
            var overzipnum=data.overzipnum==null?0:data.overzipnum;//已经完成了多少个文件
            var shu=(overzipnum/totalzipnum)*100;
            shu=parseInt(shu);
            var HTML='<div class="layui-progress " lay-showPercent="true" style="margin:8px" lay-filter="progress">\
                <div class="layui-progress-bar" lay-percent="'+shu+'%"></div>\
                </div>';
            $("#gZIPVod_html .layui-col-md9").html(HTML);

            layui.use(['layer','element','slider','form'], function(){
                var element = layui.element;
                element.render('progress');
                //使用模块
            });


        }
    }else if(null!=data&&data.actioncode=='SUCCESS_NOTHINGTODO'){
        $("#gZIPVod_html .layui-col-md9").html(data.message);
       setTimeout(function () {
            $("#gZIPVod_html").css("display","none");
        },5000)

        clearInterval(timer_zIPVodProgress);
        if (isNotEmpty(gZIPVod_Url)){
            var $a = $("<a></a>").attr("href", gZIPVod_Url).attr("download", "打包文件");
            $a[0].click();
        }
    }else {
        console.log(data.message);
        //进度请求失败
        $("#gZIPVod_html .layui-col-md9").html(data.message);
    }
}


//*******************************************************************案件人员信息编辑start****************************************************************//
var casetouser_iframe=null;
var casetouser_body=null;
function  open_casetouser() {
    layer.open({
        type: 2,
        title:'人员案件基本信息',
        content:tocaseToUserURL,
        area: ['80%', '90%'],
        btn: ['确定','取消'],
        success:function(layero, index){
            casetouser_iframe = window['layui-layer-iframe' + index];
            casetouser_body=layer.getChildFrame('body', index);
            casetouser_iframe.recordssid=recordssid;
            casetouser_iframe.setcaseToUser(getRecordById_data);
        },
        yes:function(index, layero){
            var formSubmit=layer.getChildFrame('body', index);
            var submited = formSubmit.find('#permissionSubmit')[0];
            submited.click();
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}
//*******************************************************************案件人员信息编辑end****************************************************************//

//*******************************************************************笔录问答编辑start****************************************************************//
function open_recordqw() {
    //切换界面
    $("#recorddetail #record_qw").css({"width":"80%"});
    $("#recorddetail #record_util,#btnadd").css({"display":"block"});
    $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","true");
    $("#wqutil").show();

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
}

//tr工具按钮==start
function tr_remove(obj) {
    var bool=$(obj).parents("tr").attr("automaticbool");
    if (isNotEmpty(bool)&&bool==1){
        laststarttime_qq=-1;
        laststarttime_ww=-1;
        last_type=-1;//1问题 2是答案
        qq="";
        qqq="";
        ww="";
        www="";
    }
    td_lastindex={};
    $(obj).parents("tr").remove();
    addbtn();
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
    addbtn();
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
    addbtn();
}
//tr工具按钮==end


//lable type 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    var qwfocus=null;

    if (null!=td_lastindex["key"]&&type==1){
        $('#recorddetail tr:eq("'+td_lastindex["key"]+'")').after(html);
        if (isNotEmpty(qw)){
            qwfocus= $('#recorddetail tr:eq("'+(td_lastindex["key"]+1)+'") label[name="'+qw+'"]');
            td_lastindex["key"]=td_lastindex["key"]+1;
        }
    }  else if (type==0) {
        $("#recorddetail").prepend(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:eq(0) label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:eq(0)').index();
        }
    }else {
        $("#recorddetail").append(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:last label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:last').index();
        }
    }

    if (isNotEmpty(qw)){
        setFocus(qwfocus);
    }
    addbtn();
    contextMenu();

    $("#recorddetail .table_td_tt").dblclick(function () {
        var contenteditable=$("label",this).attr("contenteditable");
        if (isNotEmpty(contenteditable)&&contenteditable=="false") {
            //开始定位视频位置
            var times=$("label",this).attr("times");
            if (times!="-1"&&isNotEmpty(times)){
                /*var name=$("label",this).attr("name");
                if (name == "q"){
                    times=parseInt(times)+subtractime_q;
                }else if( name=="w") {
                    times=parseInt(times)+subtractime_w;
                }*/
                showrecord(times,null);
            }
        }
    })

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
}

//聚焦
function setFocus(el) {
    if (isNotEmpty(el)){
        el = el[0];
        if (window.getSelection) {//ie11 10 9 ff safari
            el.focus(); //解决ff不获取焦点无法定位问题
            var range = window.getSelection();//创建range
            range.selectAllChildren(el);//range 选择obj下所有子内容
            range.collapseToEnd();//光标移至最后
        }
        else if (document.selection) {//ie10 9 8 7 6 5
            var range = document.createRange();
            range.selectNodeContents(el);
            range.collapse(false);
            var sel = window.getSelection();
            if(sel.anchorOffset!=0){
                return;
            };
            sel.removeAllRanges();
            sel.addRange(range);
        }
        event.preventDefault();
    }
};

//最后一行添加按钮初始化
function addbtn() {
    var btnhtml='<button type="button"  class="layui-btn layui-btn-warm" style="border-radius: 50%;width: 45px;height: 45px;padding:0px"  title="添加一行自定义问答" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon" style="font-size: 45px" >&#xe608;</i></button>';
    $("#recorddetail tr").each(function () {
        $("#btnadd",this).html("");
    });
    $('#recorddetail tr:last #btnadd').html(btnhtml);
}


//回车+上下键
function qw_keydown(obj,event) {
    var e = event || window.event;
    var keyCode = e.keyCode;

    var dqname=$(obj).attr("name");
    var trindex= $(obj).closest("tr").index();
    var trlength=$("#recorddetail tr").length;
    var lable=null;
    switch(keyCode){
        case 13:
            if (dqname=="q") {
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="w"]');//定位本行的答
                setFocus(lable);
            }else {
                if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                    focuslable(trtd_html,1,'q');
                } else {
                    lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                    setFocus(lable);
                }
            }
            event.preventDefault();
            break;
        case 38:
            var name=dqname=="q"?"w":"q";
            var index=(trindex-1)<=0?0:(trindex-1);
            if (dqname=="w"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                if(trindex!=0){
                    lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                    setFocus(lable);
                }
                event.preventDefault();
            }
            break;
        case 40:
            var index=(trindex+1)>=trlength?trlength:(trindex+1);
            var name=dqname=="q"?"w":"q";
            if (dqname=="q"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                setFocus(lable);
            }
            break;
        default: break;
    }
}



/*笔录实时保存*/
function setRecordreal() {

    var url=getActionURL(getactionid_manage().getRecordById_setRecordreal);

    var recordToProblems=[];//题目集合
    $("#recorddetail td.onetd").each(function (i) {
        var arr={};
        var answers=[];//答案集合
        var q=$(this).find("label[name='q']").html();
        var q_starttime=$(this).find("label[name='q']").attr("times");
        //经过筛选的q
        var ws=$(this).find("label[name='w']");
        var w_starttime=$(this).find("label[name='w']").attr("times");
        if (isNotEmpty(q)){
            if (null!=ws&&ws.length>0){
                for (var j = 0; j < ws.length; j++) {
                    var w =ws.eq(j).html();
                    //经过筛选的w
                    if (isNotEmpty(w)) {
                        answers.push({
                            answer:w,
                            starttime:w_starttime,
                        });
                    }
                }
            }
            recordToProblems.push({
                problem:q,
                starttime:q_starttime,
                answers:answers
            });
        }
    });
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
            recordToProblems:recordToProblems
        }
    };
    ajaxSubmitByJson(url, data, callbacksetRecordreal);
}
function callbacksetRecordreal(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            /* console.log("笔录实时保存成功__"+data);*/
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//获取缓存实时问答
function getRecordrealByRecordssid() {
    var url=getActionURL(getactionid_manage().getRecordById_getRecordrealByRecordssid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid
        }
    };
    ajaxSubmitByJson(url, data, callbackgetRecordrealByRecordssid);
}
function callbackgetRecordrealByRecordssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var problems=data;
            $("#recorddetail").html("");
            if (isNotEmpty(problems)) {
                var problemhtml= setqw(problems);
                focuslable(problemhtml,2,'w');

            }else {
                $("#recorddetail").html('<div id="datanull_2" style="font-size: 18px;text-align: center; margin:10px;color: rgb(144, 162, 188)">暂无笔录问答</div>');
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
var overRecord_index=null;
function addRecord() {
    setRecordreal();//3秒实时保存
    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    overRecord_loadindex = layer.msg("保存中，请稍等...", {typy:1, icon: 16,shade: [0.1, 'transparent'], time:10000 });
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getRecordById_addRecord);
        //需要收拾数据
        var recordToProblems=[];//题目集合
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                justqwbool:true,
            }
        };
        ajaxSubmitByJson(url, data, calladdRecord);
    }else{
        layer.msg("系统异常");
    }
}
function calladdRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            if (isNotEmpty(overRecord_loadindex)) {
                layer.close(overRecord_loadindex);
            }
            $("#recorddetail #record_qw").css({"width":"95%"});
            $("#recorddetail #record_util,#btnadd").css({"display":"none"});
            $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","false");
            $("#wqutil").hide();
            layer.msg('保存成功',{icon:6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function contextMenu() {
    $('#recorddetail label').bind('mouseup', function(e) {
        if (3 == e.which) {
            document.execCommand('removeFormat');
        }  else if (1 == e.which) {
            document.execCommand('backColor',false,'yellow');
        }
    });
}
//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd" id="record_qw">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" times=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w"   times=""></label></div>\
               <div  id="btnadd" ></div>\
                </td>\
                <td id="record_util">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   </div>\
                </td>\
                </tr>';
//*******************************************************************笔录问答编辑end****************************************************************//


//*******************************************************************情绪报告的生成start****************************************************************//
function uploadPhreport() {

    layer.prompt({title: '请输入情绪报告名称', formType:0}, function(phreportname, index){
        //开始收集生成的数据
        var phreportdataList;
        var dataList=[];
        $("#recordreals input[type='checkbox']:checked").each(function(index, element){
            var html=$(this).closest("div");
            var username_time=$(html).find("#username_time").html();
            var translatext=$(html).find("#translatext").html();
            var ph_stress=$(html).find(".layui-badge").html();
            var usertype=$(html).attr("usertype");
            var dqphdate=$(html).attr("dqphdate");
            if (isNotEmpty(username_time)&&isNotEmpty(translatext)) {
                var content=null;
                if (usertype==1){
                    content = "<div style='text-align: left'><p style='color: #000000;font-size: 14px'>"+dqphdate+"</p><p style='color: #999;'>"+username_time+"  </p><span style='color: #fff; background: #0181cc;'>"+translatext+"</span></div>";
                }else  if (usertype==2) {
                    content = "<div style='text-align: right'><p style='color: #000000;font-size: 14px'>"+dqphdate+"</p><p style='color: #999'>"+ph_stress+"  "+username_time+" </p> <span  style='color: #fff; background: #ef8201;'>"+translatext+"</span></div>";
                }
                if (isNotEmpty(content)){
                    dataList.push(content);
                }
            }
        });

        if (!isNotEmpty(dataList)) {
            layer.msg("请先勾选需要生成的情绪数据");
            return;
        }

        var url=getActionURL(getactionid_manage().getRecordById_uploadPhreport);
        //需要收拾数据
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                phreportname:phreportname,
                phreportdataList:dataList
            }
        };
        ajaxSubmitByJson(url, data, callbackuploadPhreport);
        layer.close(index);
    });

}

function callbackuploadPhreport(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var  downurl=data.downurl;
            if (isNotEmpty(downurl)){
                var $a = $("<a></a>").attr("href", downurl).attr("download", "down");
                $a[0].click();
                layer.msg('情绪报告生成成功,等待下载中...',{icon:6});
                getPhreportsByParam();
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//*******************************************************************情绪报告的生成end****************************************************************//


//*******************************************************************修改定位时间start****************************************************************//
var open_positiontime_index=null;
function open_positiontime() {
    var html='  <form class="layui-form site-inline" style="margin-top: 20px;padding-right: 35px;">\
               <div class="layui-form-item">\
                   <label class="layui-form-label"><span style="color: red;">*</span>定位差值</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="positiontimem" id="positiontimem" lay-verify="positiontimem" autocomplete="off" placeholder="请输入定位差值" value="' + positiontime + '"  class="layui-input">\
                    </div>\
                     <div class="layui-form-mid layui-word-aux" style="float: right;margin-right: 0px">请输入差值在-100000到-100或者100到10000区间的值以及0</div>\
                </div>\
            </form>';
    layui.use(['layer','element','form','laydate'], function() {
        var form = layui.form;
        open_positiontime_index=layer.open({
            type:1,
            title:'编辑定位差值',
            content:html,
            area: ['25%', '30%'],
            btn: ['确定','取消'],
            success:function(layero, index){
                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();
            },
            yes:function(index, layero){
                //自定义验证规则
                form.verify({
                    positiontimem:function (value) {
                        if (!(/\S/).test(value)) {
                            return "请输入定位差值";
                        }
                        if (!((value<=-100&&value>=-100000)||(value<=100000&&value>=100)||value==0)) {
                            return "请输入差值在-100000到-100或者100到10000区间的值以及0";
                        }
                    }
                });
                //监听提交
                form.on('submit(fromContent)', function(data){
                    updateRecord();
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
    });
}
function updateRecord(){
    var positiontime=$("#positiontimem").val();
    if (!isNotEmpty(positiontime)){
        layer.msg("请输入定位差值",{icon:5});
        return;
    }
    var url=getActionURL(getactionid_manage().getRecordById_updateRecord);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: recordssid,
            positiontime:positiontime
        }
    };
    ajaxSubmitByJson(url, data, callbackupdateRecord);
}
function callbackupdateRecord(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var param=data.param;
            if (isNotEmpty(param)){
                layer.close(open_positiontime_index);
                 positiontime=param.positiontime;//更新值
                layer.msg("保存成功",{icon:6,time:500},function () {
                    $("#positiontime").val(positiontime);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//*******************************************************************修改定位时间start****************************************************************//


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
