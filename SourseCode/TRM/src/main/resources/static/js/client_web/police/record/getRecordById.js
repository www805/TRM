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


var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上


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
                $("#positiontime").val(parseFloat(positiontime)/1000);

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

            var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().getRecordById_getRecordrealByRecordssid);
            getRecordrealByRecordssid(getRecordrealByRecordssidUrl);//右侧数据
            setInterval( function() {
                var setRecordrealUrl=getActionURL(getactionid_manage().getRecordById_setRecordreal);
                setRecordreal(setRecordrealUrl);
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

//未合并
function setqw(problems) {
    $("#recorddetail").empty();
    var problemhtml="";
    if (isNotEmpty(problems)){
        $("#datanull_2").hide();
        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];
            var q_starttime=problem.starttime;
            if (isNotEmpty(q_starttime)&&q_starttime!=-1) {
                q_starttime+= parseFloat(subtractime_q);
            }
            var problemtext=problem.problem;
            if (isNotEmpty(problemtext)){
                problemhtml+= '<tr>\
                        <td style="padding: 0;width: 95%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>问：</span><label name="q" contenteditable="false" starttime="'+q_starttime+'">'+problemtext+'</label></div>';
                var answers=problem.answers;
                var answerhtml='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" ></label></div>';
                if (isNotEmpty(answers)){
                    for (var j = 0; j < answers.length; j++) {
                        var answer = answers[j];

                        var w_starttime=answer.starttime;
                        if (isNotEmpty(w_starttime)&&w_starttime!=-1) {
                            w_starttime+=parseFloat(subtractime_w);
                        }
                        var answertext=answer.answer==null?"":answer.answer;
                        answerhtml='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" starttime="'+w_starttime+'" >'+answertext+'</label></div>';
                    }
                }
                problemhtml+=answerhtml;
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
        }
    }
    return problemhtml;
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


                        //实时会议数据
                        if (usertype==1){
                            //询问人没有情绪报告
                            subtractime_q=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_q);
                            recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+')" times='+starttime+' usertype='+usertype+' dqphdate="">\
                                                            <a style="display: none;color: #ccc" id="dqphdate"></a>\
                                                            <p><a id="username_time">【'+username+'】 '+asrstartime+' </a><a class="layui-badge" style="display:none;" title="未找到最高值">-1</a></p>\
                                                            <span id="translatext">'+translatext+'</span> \
                                                      </div >';
                        }else if (usertype==2){
                            subtractime_w=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_w);
                            recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+')" times='+starttime+' usertype='+usertype+' dqphdate="">\
                                                            <a style="display: none;color: #ccc" id="dqphdate"></a>\
                                                            <p><a class="layui-badge " style="visibility:hidden; background-color: #00CD68  " title="未找到最高值">-1</a>  <a  id="username_time">'+asrstartime+' 【'+username+'】</a> </p>\
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
    var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().getRecordById_getRecordrealByRecordssid);
    getRecordrealByRecordssid(getRecordrealByRecordssidUrl);

    layui.use(['layer','form'], function(){
        var form = layui.form;
        form.render();
    });
}






var phreportfirst=-1;//是否为第一次打开情绪报告
$(function () {
    //情绪报告
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
            phreportshowhide(bool);
        });
    });

    $("#baocun").click(function () {
      addRecord();
    });

    //定位差值
    $("#positiontime").click(function () {
        var url=getActionURL(getactionid_manage().getRecordById_updateRecord);
        open_positiontime(url);
    });

    //导出
    $("#dc_li dd").click(function () {
        layer.msg("导出中，请稍等...", { icon: 16, shade: [0.1, 'transparent'],time:6000});
        var type=$(this).attr("type");
        if (type==1){
            var url=getActionURL(getactionid_manage().getRecordById_exportWord);
            exportWord(url);
        }else  if(type==2){
            var url=getActionURL(getactionid_manage().getRecordById_exportPdf);
            exportPdf(url);
        }
    });

});

//显示审讯检测数据到语音识别上
function phreportshowhide(bool) {
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


                    var phbadge="#00CD68";
                    var phtitle="最高紧张值";
                    if (ph_stress>=0&&ph_stress<=30){
                        phbadge="#00CD68";
                    }else if (ph_stress>30&&ph_stress<=50){
                        phbadge="#e4e920";
                    }else if (ph_stress>50&&ph_stress<=70){
                        phbadge="#ff840f";
                    }else if (ph_stress>70&&ph_stress<=100){
                        phbadge="#e90717";
                    }else {
                        phtitle="未找到最高值";
                    }// !important
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
}



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


//*******************************************************************笔录问答编辑start****************************************************************//

//问答编辑
function open_recordqw() {
    //切换界面
    $("#recorddetail #record_qw").css({"width":"80%"});
    $("#recorddetail #record_util,#btnadd").css({"display":"block"});
    $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","true");
    $("#wqutil").show();

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    });
}






//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
var overRecord_index=null;
function addRecord() {
    var setRecordrealUrl=getActionURL(getactionid_manage().getRecordById_setRecordreal);
    setRecordreal(setRecordrealUrl);

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



