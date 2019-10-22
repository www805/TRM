var pdfdownurl=null;
var worddownurl=null;

var videourl=null;//视频地址
var dq_play=null;//当前播放视频
var first_playstarttime=0;//第一个视频的开始时间
var recordPlayParams=[];//全部视频数据集合

var getRecordById_data=null;
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

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
            getRecordById_data=data;
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
                $("#recorddetail_strong").html('【谈话笔录】<i class="layui-icon layui-icon-edit" style="font-size: 20px;color: red" title="编辑" onclick="open_recordqw()"></i>');
                //会议人员
                var recordUserInfosdata=record.recordUserInfos;

                //案件信息
                var case_=record.case_;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(case_)){
                    var casename=case_.casename==null?"":case_.casename;
                    var username=recordUserInfosdata.username==null?"":recordUserInfosdata.username;
                    var cause=case_.cause==null?"":case_.cause;
                    var occurrencetime=case_.occurrencetime==null?"":case_.occurrencetime;
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
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                progrssBar_width="89%";
                set_getPlayUrl(getPlayUrlVO);
            }

        }
    }else{
        layer.msg(data.message,{icon: 5});
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
                    const file = recordFileParams[j];
                    var filename= file.filename;
                    if (filename==playname&&oldname.indexOf(filename)<0){
                        var VIDEO_HTML='<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="'+play.filenum+'"  state="'+file.state+'">视频'+play.filenum+'</span>';
                        $("#videos").append(VIDEO_HTML);
                        play["start_range"]=parseFloat(play.recordstarttime)-parseFloat(first_playstarttime);
                        play["end_range"]=parseFloat(play.recordendtime)-parseFloat(first_playstarttime);
                        recordPlayParams[i]=play;
                        oldname.push(filename)
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
    $("#recorddetail #record_qw").css({"width":"75%"});
    $("#recorddetail #record_util,#btnadd").css({"display":"block"});
    $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","true");
    $("#wqutil").show();


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
                times=parseInt(times);
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
    var url=getActionURL(getactionid_manage().getconversationById_setRecordreal);

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
    var url=getActionURL(getactionid_manage().getconversationById_getRecordrealByRecordssid);
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

//整合问答笔录html

function setqw(problems) {
    if (isNotEmpty(problems)){
        var problemhtml="";
        $("#recorddetail").empty();
        $("#datanull_2").hide();

        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];

            var problemstarttime=problem.starttime;
            var q_starttime=problemstarttime;

            var problemtext=problem.problem==null?"未知":problem.problem;
            problemhtml+= '<tr>\
                        <td style="padding: 0;width: 95%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>问：</span><label name="q"  contenteditable="false" times="'+q_starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];

                    var answerstarttime=answer.starttime;
                    var w_starttime=answerstarttime;

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

//视频进度
function showrecord(times,oldtime) {
    $("#recorddetail label").removeClass("highlight_right");
    times=parseFloat(times);
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
                    locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                    changeProgrss(parseFloat(locationtime));
                } else {
                    //赋值新视频,计算新的时间
                    dq_play=recordPlayParam;
                    videourl=dq_play.playUrl;
                    var locationtime=(first_playstarttime+times)-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);//重新计算时间
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


        var recorddetailtrlen= $("#recorddetail label").length;
        $("#recorddetail label").each(function (i,e) {
            var t1=$(this).attr("times");
            if (t1==times) {
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
        });
    }
}


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
var overRecord_index=null;
function addRecord() {
    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    overRecord_loadindex = layer.msg("保存中，请稍等...", {typy:1, icon: 16,shade: [0.1, 'transparent'], time:10000 });
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getconversationById_addRecord);
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
    $('#recorddetail label').bind('mousedown', function(e) {
        if (3 == e.which) {
            document.execCommand('removeFormat');
        }  else if (1 == e.which) {
            document.execCommand('backColor',false,'yellow');
        }
    });
}
//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 75%;" class="onetd" id="record_qw">\
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

    setInterval( function() {
        setRecordreal();//5秒实时保存
    },3000)

    $("#baocun").click(function () {
        addRecord();
    });


    })



