var templatessid=null;//当前选择的模板ssid
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

var recorduser=[];//会议用户集合：副麦主麦
var dq_recorduser=null;//当前被询问人ssid

var mcbool=null;//会议状态



var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上


var getRecordById_data=null;//单份笔录返回的全部数据

var dq_livingurl=null;//当前直播地址
var dq_previewurl=null;//当前预览地址

var record_pausebool=-1;//笔录是否允许暂停1允许 -1 不允许 默认不允许-1
var record_adjournbool=-1;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1

var occurrencetime_format;//案发时间

var multifunctionbool;
var FDAudPowerMapTimer; //音频定时器


var asrbool=0;//使用语音识别的个数
var phbool=0;//使用身心检测的个数

//跳转变更模板页面//变更模板题目
function opneModal_1() {
    var url=getActionURL(getactionid_manage().waitRecord_tomoreTemplate);

    var index = layer.open({
        type: 2,
        title:'变更模板',
        content:url,
        area: ['1000px', '680px'],
        btn: ['应用模板','导入该模板全部题目'],
        yes:function(index, layero){
           var editSsid = $(window.frames["layui-layer-iframe"+index])[0].editSsid;
            templatessid=editSsid;
            getTemplateById();
            layer.close(index);
        },
        btn2:function(index, layero){
            var editSsid = $(window.frames["layui-layer-iframe"+index])[0].editSsid;
            templatessid=editSsid;
              setAllproblem();
            layer.close(index);
        }
    });
}
function getTemplateById() {
    if (isNotEmpty(templatessid)){
        var url=getActionURL(getactionid_manage().waitRecord_getTemplateById);

        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: templatessid
            }
        };
        ajaxSubmitByJson(url, data, callTemplateById);
    }
}
function callTemplateById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var template=data.template;
            if (isNotEmpty(template)){
                var templateToProblems=template.templateToProblems;
               $("#templatetoproblem_html").html("");
                if (isNotEmpty(templateToProblems)) {
                    sessionStorage.setItem(recordssid,JSON.stringify(templateToProblems));//存储最后当前打开模板
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'referanswer='"+templateToProblem.referanswer+"'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);
                    }
                }
              }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//导入全部题目
function setAllproblem() {
    if (isNotEmpty(templatessid)){
        var url=getActionURL(getactionid_manage().waitRecord_getTemplateById);

        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: templatessid
            }
        };
        ajaxSubmitByJson(url, data, callsetAllproblem);
    }
}
function callsetAllproblem(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var template=data.template;
            if (isNotEmpty(template)){
                var templateToProblems=template.templateToProblems;
                $("#templatetoproblem_html").html("");
                //第一行去掉-----------------------------start
                var textlen=null;
                $("#first_originaltr label").each(function(){
                    textlen+=$(this).text();
                });
                if (!isNotEmpty(textlen)){
                    $("#first_originaltr").remove();
                    td_lastindex["key"]=null;
                    td_lastindex["value"]=null;
                }
                //第一行去掉-----------------------------end
                if (isNotEmpty(templateToProblems)) {
                    sessionStorage.setItem(recordssid,JSON.stringify(templateToProblems));//存储最后当前打开模板
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'referanswer='"+templateToProblem.referanswer+"'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);

                        var html='<tr >\
                                <td style="padding: 0;width: 80%;" class="onetd" >\
                                    <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"  onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.problem+'" starttime="" isn_fdtime="-1">'+templateToProblem.problem+'</label></div>\
                                    <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.referanswer+'" starttime=""  isn_fdtime="-1"></label></div>\
                                    <div  id="btnadd"></div>\
                                </td>\
                                <td>\
                                                                <div class="layui-btn-group">\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                                                                <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                                                                </div>\
                                                            </td>\
                                                            </tr>';
                        focuslable(html,1,'w');
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }

}

//导入单个题目
function copy_problems(obj) {
    var text=$(obj).find("span").text();
    var w=$(obj).attr("referanswer");
    var html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"   class=""  placeholder="'+text+'"  starttime="" isn_fdtime="-1">'+text+'</label></div>\
            <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w"   class="" placeholder="'+w+'" starttime="" isn_fdtime="-1"></label></div>\
            <div  id="btnadd"></div>\
        </td>\
        <td>\
            <div class="layui-btn-group">\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
            <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete" ></i>删除</a>\
            </div>\
        </td>\
        </tr>';
    //第一行去掉-----------------------------start
    var textlen=null;
    $("#first_originaltr label").each(function(){
        textlen+=$(this).text();
    });
    if (!isNotEmpty(textlen)){
        $("#first_originaltr").remove();
        td_lastindex["key"]=null;
        td_lastindex["value"]=null;
    }
    //第一行去掉-----------------------------end
    focuslable(html,1,'w');
}


//录音按钮显示隐藏 type:1开始录音
var startMC_index;
function img_bool(obj,type){
    $("#record_img img").css("display","none");
    if (type==1){
        if (mtssid==null){
            //开始会议
            $("#pauserecord").attr("onclick","");
            startMC_index = layer.msg("开启中，请稍等...", {
                icon: 16,
                time:-1,
                shade: [0.1,'#fff'], //不显示遮罩
            });
            console.log("开始会议")
            startMC();
        } else if (record_pausebool==1) {
            //继续会议

            startMC_index = layer.msg("再次启动中，请稍等...", {
                icon: 16,
                time:-1,
                shade: [0.1,'#fff'], //不显示遮罩
            });
            console.log("继续会议");
            pauseOrContinueRercord(2);
        }
    }else if (type==2) {
        if (record_pausebool==1){
            //暂停录音

            startMC_index = layer.msg("暂停中，请稍等...", {
                icon: 16,
                time:-1,
                shade: [0.1,'#fff'], //不显示遮罩
            });
            console.log("暂停会议")
            pauseOrContinueRercord(1);
        } else {
            $("#startrecord").css("display","inline-block");
            if (gnlist.indexOf(HK_O)== -1){
                //非海康
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips("笔录中~" ,'#startrecord',{time:0, tips: 1});
                });
            }
            layer.msg("笔录中~");
        }
    }else if(type==-1) {
        $("#endrecord").css("display","inline-block");
        if (gnlist.indexOf(HK_O)== -1){
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips:1});
            });
        }
        console.log("会议已结束")
        layer.msg("该笔录已经制作过啦~");
    }
}


//获取笔录信息
function getRecordById() {
    var url=getActionURL(getactionid_manage().waitRecord_getRecordById);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordById);
}
function callbackgetRecordById(data) {
    getRecordById_data=null;
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            getRecordById_data=data;

            record_pausebool=data.record_pausebool;//暂停是否
            record_adjournbool=data.record_adjournbool;//休庭是否（案件暂停）
            if ((record_adjournbool==1||record_adjournbool=="1")&&record_pausebool==1){ $("#adjourn_btn").show(); } else {$("#adjourn_btn").hide();}

            var modeltds=data.modeltds;
            if (isNotEmpty(modeltds)&&isNotEmpty(gnlist)){
                asrbool=0;
                phbool=0;
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
                    $("#record_switch_HTML").css("visibility","visible");
                    $("#AsrState_tr").show();
                }
                if (isNotEmpty(gnlist)&&gnlist.indexOf(PH_F)>0&&phbool>0){
                    $("#ph").show();
                    $("#ph_HTML").show();
                    $("#xthtml").css("visibility","visible");
                    $("#PolygraphState_tr").show();
                }
                if (isNotEmpty(gnlist)&&gnlist.indexOf(FY_T)==-1) {
                    //法院版暂未提供案件人员编辑页
                    $("#open_casetouser").css("display","inline-block");
                }
            }


            var default_fhurl=data.default_fhurl;
            if (isNotEmpty(default_fhurl)){
                liveurl=default_fhurl;
                initplayer();
            }


            var record=data.record;
            if (isNotEmpty(record)){
                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }

                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname).attr("title",record.recordname==null?"笔录标题":record.recordname);
                mcbool=record.mcbool;

                //获取提讯会议ssid
                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    //按钮显示控制
                     multifunctionbool=police_arraignment.multifunctionbool;//按钮显示控制
                    if (multifunctionbool==2){
                        console.log("我是谈话笔录隐藏")

                        $("#overRecord_btn").attr("src","/uimaker/images/record_stop_2.png");
                        $("#adjourn_btn").attr("src","/uimaker/images/record_zt.png");
                        if (record_pausebool==1){
                            $("#startrecord").attr("src","/uimaker/images/record6.png");
                        }else {
                            $("#startrecord").attr("src","/uimaker/images/record10.png");
                        }
                        $("#pauserecord").attr("src","/uimaker/images/record5.png");
                        $("#endrecord").attr("src","/uimaker/images/record8.png");
                    } else if (multifunctionbool==3) {
                        console.log("我不是谈话笔录")

                        $("#overRecord_btn").attr("src","/uimaker/images/record_stop_1.png");
                        $("#adjourn_btn").attr("src","/uimaker/images/record_zt.png");
                        if (record_pausebool==1){
                            $("#startrecord").attr("src","/uimaker/images/record2.png");
                        }else {
                            $("#startrecord").attr("src","/uimaker/images/record9.png");
                        }
                        $("#pauserecord").attr("src","/uimaker/images/record.png");
                        $("#endrecord").attr("src","/uimaker/images/record4.png");
                    }


                    //获取会议ssid
                    var mtssiddata=police_arraignment.mtssid;
                    if (isNotEmpty(mtssiddata)){
                        mtssid=mtssiddata;
                        getRecordrealing();
                    }

                    $("#record_img img").css("display","none");
                    if ((!isNotEmpty(mcbool)||!(mcbool==1||mcbool==3))&&isNotEmpty(mtssiddata)){
                        //存在会议但是状态为空或者不等于1
                        $("#endrecord").css("display","inline-block");
                        if (gnlist.indexOf(HK_O)== -1){
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips('该笔录已经制作过啦~' ,'#endrecord',{time:0, tips: 1});
                            });
                        }
                        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
                    }else if (null!=mcbool&&(mcbool==1||mcbool==3)){
                        if (multifunctionbool==2){
                            $("#pauserecord").attr({"src":"/uimaker/images/record7.png","onclick":"img_bool(this,1);"});
                        }else if (multifunctionbool==3) {
                            $("#pauserecord").attr({"src":"/uimaker/images/record3.png","onclick":"img_bool(this,1);"});
                        }
                        //存在会议状态正常
                        if (mcbool==1){
                            $("#startrecord").css("display","inline-block");
                            var tips_msg="笔录中~";
                            if (record_pausebool==1) {
                                tips_msg="点击我可以暂停~";
                            }
                            if (gnlist.indexOf(HK_O)== -1){
                                layui.use(['layer','element','form'], function(){
                                    var layer=layui.layer;
                                    layer.tips(tips_msg ,'#startrecord',{time:0, tips: 1});
                                });
                            }
                        } else if (mcbool==3&&record_pausebool==1) {
                            $("#pauserecord").css("display","inline-block");
                            if (gnlist.indexOf(HK_O)== -1){
                                layui.use(['layer','element','form'], function(){
                                    var layer=layui.layer;
                                    layer.tips('点击我可以再次启动制作~' ,'#pauserecord',{time:0, tips: 1});
                                });
                            }
                        }

                        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
                    }else {
                        $("#pauserecord").css("display","inline-block");
                        if (gnlist.indexOf(HK_O)== -1){
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 1});
                            });
                        }
                        $("#start_over_btn").text("开始谈话").attr("onclick","startMC()");
                    }
                }



                //询问人和被询问人信息
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
                    dq_recorduser=recordUserInfosdata.userssid;
                }



                //案件信息
                var case_=record.case_;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(case_)){
                    var occurrencetime_format_=case_.occurrencetime_format;
                    if (isNotEmpty(occurrencetime_format_)){
                        occurrencetime_format=occurrencetime_format_;
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
                    casebool=case_.casebool==null?"":case_.casebool;

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
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }


                getMCCacheParamByMTssid();//获取缓存
                getTDCacheParamByMTssid();
                var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().waitRecord_getRecordrealByRecordssid);
                getRecordrealByRecordssid(getRecordrealByRecordssidUrl);
                setInterval( function() {
                    var setRecordrealUrl=getActionURL(getactionid_manage().waitRecord_setRecordreal);
                    setRecordreal(setRecordrealUrl);
                },3000);
                setInterval( function() {
                    var setRecordProtectUrl=getActionURL(getactionid_manage().waitRecord_setRecordProtect);
                    setRecordProtect(setRecordProtectUrl);//5秒缓存一次
                },5000);
            }

            //获取默认的片头信息
            getptdjconst(1);

            //开始获取设备状态
            getFDState();

            setInterval(function () {
                getFDState();
            }, 1000);

            FDAudPowerMapTimer = setInterval(function () {
                getFDAudPowerMap();
            }, 500);

            setInterval(function () {
                putRecessStatus();
            }, 10000);

            //获取刻录选时
            getBurnTime();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
    $("#start_over_btn").text("谈话开启中").attr("onclick","");
    if (isNotEmpty(getRecordById_data)){
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});
        $("#LiveState").text("加载中");
        $("#LiveState").attr({"LiveState": "", "class": "layui-badge layui-bg-gray"});
        $("#PolygraphState").text("加载中");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "layui-badge layui-bg-gray"});


        var casenum=null;
        var casename=null;
        var cause=null;
        var department=null;
        var askedname=null;
        var askname=null;
        var recordername=null;
        var askplace=null;
        var casetypename=null;
        var recordtypename=null;
        var recordname=null;

        var record=getRecordById_data.record;
        if (isNotEmpty(record)){
            var case_=record.case_;
            var recordUserInfos=record.recordUserInfos;
            var police_arraignment=record.police_arraignment;
            casenum=case_.casenum;
            casename=case_.casename;
            cause=case_.cause;
            department=case_.department;
            askedname=recordUserInfos.userssid;
            askname=recordUserInfos.adminname;
            recordername=recordUserInfos.recordadminname;
            askplace=police_arraignment.recordplace;
            casetypename=record.recordtypename;
            recordtypename=record.recordtypename;
            recordname=record.recordname;



            var startRecordAndCaseParam={
                casenum:casenum,
                casename:casename,
                cause:cause,
                department:department,
                askedname:askedname,
                askname:askname,
                recordername:recordername,
                askplace:askplace,
                casetypename:casetypename,
                recordtypename:recordtypename,
                recordname:recordname
            }


            var ptdjParam_out=getptdjinfo();

            var url=getUrl_manage().startRercord;
            var data={
                token:INIT_CLIENTKEY,
                param:{
                     startRecordAndCaseParam:startRecordAndCaseParam
                    ,recordssid:recordssid
                    ,ptdjParam_out:ptdjParam_out,
                }
            };
            ajaxSubmitByJson(url, data, callbackstartMC);
        }
    }else {
        layer.close(startMC_index);
        layer.msg("请稍等",{time:1000},function () {
            getRecordById();
            $("#record_img img").css("display","none");
            if (multifunctionbool==2){
                $("#pauserecord").css("display","inline-block").attr({"src":"/uimaker/images/record5.png","onclick":"img_bool(this,1);"});
            }else  if (multifunctionbool==3){
                $("#pauserecord").css("display","inline-block").attr({"src":"/uimaker/images/record.png","onclick":"img_bool(this,1);"});
            }
            if (gnlist.indexOf(HK_O)== -1){
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 1});
                });
            }
            $("#start_over_btn").text("开始谈话").attr("onclick","startMC()");
        });
    }
}
function callbackstartMC(data) {
    layer.close(startMC_index);
    if(null!=data&&data.actioncode=='SUCCESS'){
        $("#record_img img").css("display","none");
        $("#pauserecord").attr("onclick","img_bool(this,1);");
        var tips_msg="笔录作中~";
        if (record_pausebool==1){
            tips_msg="点击我可以暂停~";
            //制作暂停 ，判断是笔录还是审讯
            if (multifunctionbool==2){
                $("#startrecord").attr("src","/uimaker/images/record6.png");
                $("#pauserecord").attr("src","/uimaker/images/record7.png");
            }else if (multifunctionbool==3) {
                $("#startrecord").attr("src","/uimaker/images/record2.png");
                $("#pauserecord").attr("src","/uimaker/images/record3.png");
            }
        }else {
            //制作中 ，判断是笔录还是审讯
            if (multifunctionbool==2){
                $("#startrecord").attr("src","/uimaker/images/record10.png");
            }else  if (multifunctionbool==3) {
                $("#startrecord").attr("src","/uimaker/images/record9.png");
            }
        }
        $("#startrecord").css("display","inline-block");
        if (gnlist.indexOf(HK_O)== -1){
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips(tips_msg ,'#startrecord',{time:0, tips: 1});
            });
        }


        var data=data.data;
        if (isNotEmpty(data)){
            var startMCVO=data.startMCVO;
            var polygraphnum=startMCVO.polygraphnum==null?0:startMCVO.polygraphnum;
            var recordnum=startMCVO.recordnum==null?0:startMCVO.recordnum;
            var asrnum=startMCVO.asrnum==null?0:startMCVO.asrnum;

            var mtssiddata=startMCVO.mtssid;
             useretlist=startMCVO.useretlist;
            if (isNotEmpty(useretlist)) {
                for (var i = 0; i < useretlist.length; i++) {
                    var useret = useretlist[i];
                    var userssid1 = useret.userssid;
                    if (userssid1 == dq_recorduser) {
                        dq_livingurl=useret.livingurl;//当前直播地址
                        dq_previewurl=useret.previewurl;//当前预览地址
                        liveurl = dq_livingurl;//开始会议后默认使用副麦预览地址
                        console.log("当前liveurl————"+liveurl)
                    }
                }
                initplayer();//初始化地址
            }
            mtssid=mtssiddata;
            mcbool=1;//正常开启

            getMCCacheParamByMTssid();//获取缓存
            getTDCacheParamByMTssid();

            var con="已开启：<br>设备录像数："+recordnum;
            if (isNotEmpty(gnlist)&&gnlist.indexOf(ASR_F)>0&&asrbool>0){
                con+="<br>语音识别开启数："+asrnum;
            }
            if (isNotEmpty(gnlist)&&gnlist.indexOf(PH_F)>0&&phbool>0){
               con+="<br>身心监测开启数："+polygraphnum;
            }
            layer.msg(con, {time: 2000});
        }
        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
    }else{
        $("#MtState").text("未启动");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("未启动");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});
        $("#LiveState").text("未启动");
        $("#LiveState").attr({"LiveState": "", "class": "layui-badge layui-bg-gray"});
        $("#PolygraphState").text("未启动");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "layui-badge layui-bg-gray"});


        var data2=data.data;
        if (isNotEmpty(data2)){
            var checkStartRecordVO=data2.checkStartRecordVO;
            var recordbool=data2.recordbool; //笔录开始状态
            $("#record_img img").css("display","none");
            if (null!=recordbool&&recordbool==true){
                $("#endrecord").css("display","inline-block");
                if (gnlist.indexOf(HK_O)== -1){
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips: 1});
                    });
                }
            }else {
                $("#pauserecord").css("display","inline-block").attr("onclick","img_bool(this,1);");
                if (gnlist.indexOf(HK_O)== -1){
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips:1});
                    });
                }
            }

            if (null!=checkStartRecordVO){
                var msg=checkStartRecordVO.msg;
                parent.layer.confirm("开启失败(<span style='color:red'>"+msg+"</span>)，请先结束正在进行中的笔录", {
                    btn: ['好的'], //按钮
                    shade: [0.1,'#fff'], //不显示遮罩
                    closeBtn:0,
                    shade: [0.1,'#fff'], //不显示遮罩
                }, function(index){
                    parent.layer.close(index);
                });
                return;
            }
        }
        layer.msg("开启失败");
        $("#start_over_btn").text("开始谈话").attr("onclick","startMC()");
    }
}


//视频地址切换 type 1主麦 type 2副麦
function select_liveurl(obj,type){
    for (let i = 0; i < recorduser.length; i++) {
        const user = recorduser[i];
        if (user.userssid==dq_recorduser){
            if (type==2){
                liveurl=dq_livingurl;//开始会议后默认使用副麦预览地址
            } else {
                liveurl=dq_previewurl;//开始会议后默认使用副麦预览地址
            }
            console.log("当前liveurl————"+liveurl)
        }
    }
    initplayer();
}

//大视频or笔录切换
function switchbtn(type,obj) {
    if (type==1){
        $(".phitem1").css("display","block");
        $("#shrink_html").css("display","none");
        $("#notshrink_html1").css("display","none");

        var html=$("#living3_2").html();
        if (!isNotEmpty(html)){
            $("#living3_2").html($("#living3_1").html());
            $("#living3_1").empty();
        }

        $(".phitem2").attr("id","");
        $(".phitem1").attr("id","phitem");
    } else {
        //判断笔录制作界面的显示
        var shrink_bool=$("#shrink_bool").attr("shrink_bool");
        if (shrink_bool==1){
            $("#shrink_html").show();
            $("#notshrink_html1").attr("class","layui-col-md6");
            $("#layui-layer"+recordstate_index).show();
        }else{
            $("#shrink_html").hide();
            $("#notshrink_html1").attr("class","layui-col-md9");
            $("#layui-layer"+recordstate_index).hide();
        }


        $(".phitem1").css("display","none");
        /*$("#shrink_html").css("display","block");*/
        $("#notshrink_html1").css("display","block");

        var html=$("#living3_1").html();
        if (!isNotEmpty(html)){
            $("#living3_1").html($("#living3_2").html());
            $("#living3_2").empty();
        }
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
        $(".phitem1").attr("id","");
        $(".phitem2").attr("id","phitem");
        main1($("#leftdiv").width());
    }

    initplayer();//启动设备画面预览
    $(obj).removeClass("layui-btn-primary").addClass("layui-btn-normal").siblings().removeClass("layui-btn-normal").addClass("layui-btn-primary");
}

//重置模板
function clearRecord() {
    //1、询问是否重置
    //1、保存重置内容，并且给出返回上一份笔录的按钮
    //返回按钮点击的时候询问要插入的位置当前光标孩纸追加后面或者最前面，没有任何光标将追加
    layer.open({
        content:"重置后问答将清空，确定要重置吗？"
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
            var url=getActionURL(getactionid_manage().waitRecord_setRecordreal_Last);

            var recordToProblems=[];//题目集合
            $("#recorddetail td.onetd").each(function (i) {
                var arr={};
                var answers=[];//答案集合
                var q=$(this).find("label[name='q']").text();
                var q_starttime=$(this).find("label[name='q']").attr("starttime");
                q=q.replace(/\s/g,'');
                //经过筛选的q
                var ws=$(this).find("label[name='w']");
                var w_starttime=$(this).find("label[name='w']").attr("starttime");
                if (isNotEmpty(q)){
                    if (null!=ws&&ws.length>0){
                        for (var j = 0; j < ws.length; j++) {
                            var w =ws.eq(j).text();
                            w=w.replace(/\s/g,'');
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
            if (isNotEmpty(recordToProblems)){

            }
            var data2={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid: recordssid,
                    recordToProblems:recordToProblems
                }
            };
            ajaxSubmitByJson(url, data2, function (data) {
                if(null!=data&&data.actioncode=='SUCCESS'){
                    var data=data.data;
                    if (isNotEmpty(data)){
                        console.log("重置成功");
                        //撤销重置按钮
                        $("#getRecordreal_LastByRecordssid").show();
                    }
                }else{
                    layer.msg(data.message,{icon: 5});
                }
            });


            $("#recorddetail").html("");
            td_lastindex={};
            layer.close(index);
        }
        ,btn2: function(index, layero){
            layer.close(index);
        }
        ,cancel: function(){
        }
    });
}

//返回重置
function getRecordreal_LastByRecordssid() {
    var url=getActionURL(getactionid_manage().waitRecord_getRecordreal_LastByRecordssid);

    var recordToProblems=[];//题目集合
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
        }
    };
    ajaxSubmitByJson(url, data,callbackgetgetRecordreal_LastByRecordssid);
}
function callbackgetgetRecordreal_LastByRecordssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var problems=data;
            if (isNotEmpty(problems)) {
                var problemhtml= setqw(problems);
                var qw_location=2; //1当前光标加一行 2尾部追加 0首部
                layer.open({
                    content:"已获取重置问答，请选择要插入的位置"
                    ,btn: ['当前光标', '首部追加','尾部追加']
                    ,yes: function(index, layero){
                        qw_location=1;
                        focuslable(problemhtml,qw_location,'w');
                        layer.close(index);
                    }
                    ,btn2: function(index, layero){
                        qw_location=0;
                        focuslable(problemhtml,qw_location,'w');
                        layer.close(index);
                    }
                    ,btn3: function(index, layero){
                        qw_location=2;
                        focuslable(problemhtml,qw_location,'w');
                        layer.close(index);
                    }
                });

            }
        }else {
            focuslable(null,2,'q');//重置保存的内容为空时添加一行自定义问答
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//整合问答笔录html
function setqw(problems){
    $("#recorddetail").empty();
    var problemhtml="";
    if (isNotEmpty(problems)) {
        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];
            var problemtext=problem.problem;
            var q_starttime=problem.starttime;
            if (isNotEmpty(problemtext)){
                problemhtml+= '<tr>\
                        <td style="padding: 0;width: 80%;" class="onetd">\
                            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" starttime="'+q_starttime+'">'+problemtext+'</label></div>';
                var answers=problem.answers;
                var answerhtml='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w"  starttime=""></label></div>'
                if (isNotEmpty(answers)){
                    for (var j = 0; j < answers.length; j++) {
                        var answer = answers[j];
                        var w_starttime=answer.starttime;
                        var answertext=answer.answer==null?"未知":answer.answer;
                        answerhtml='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w"     starttime="'+w_starttime+'">'+answertext+'</label></div>';
                    }
                }
                problemhtml+=answerhtml;
                problemhtml+=' <div  id="btnadd"></div></td>\
                        <td>\
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

var currenttime;
var yesterdaytime;
$(function () {
    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });

   //初始化第一行的焦点
    contextMenu();

    $(document).keypress(function (e) {
        if (e.which == 13) {
            focuslable(trtd_html,2,'q');
            event.preventDefault();
        }
    });

    //导出
    $("#dc_li li").click(function () {
        var type=$(this).attr("type");
        if (type==1){
            var exportWordUrl=getActionURL(getactionid_manage().waitRecord_exportWord);
            exportWord(exportWordUrl)
        }else  if(type==2){
            var exportPdUrl=getActionURL(getactionid_manage().waitRecord_exportPdf);
            exportPdf(exportPdUrl);
        }
    });
    //常用问答点击
    $("#cywd_li li").click(function () {
        var text=$(this).text();
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value=="w") {
                $(this).append(text);
            }
        });
    });
    //常用时间点击
    $("#cysj_li li").click(function () {
        var type=$(this).attr("type");
        var text=$(this).text();
        if (type==1){
           //当前时间
            if (isNotEmpty(currenttime)){
                $("#recorddetail label").each(function(){
                    var lastindex=$(this).closest("tr").index();
                    var value=$(this).attr("name");
                    if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                        $(this).append(currenttime);
                    }
                });
            }
        }else  if(type==2){
            //昨天时间
            if (isNotEmpty(yesterdaytime)){
                $("#recorddetail label").each(function(){
                    var lastindex=$(this).closest("tr").index();
                    var value=$(this).attr("name");
                    if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                        $(this).append(yesterdaytime);
                    }
                });
            }
        }else  if(type==3){
        //案发时间
            if (isNotEmpty(occurrencetime_format)){
                $("#recorddetail label").each(function(){
                    var lastindex=$(this).closest("tr").index();
                    var value=$(this).attr("name");
                    if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                        $(this).append(occurrencetime_format);
                    }
                });
            }else {
                layer.msg("案发时间未设置",{icon:6})
            }
        }
    })

    //告知书点击
    $("#open_getNotifications").click(function () {
        var getNotificationsUrl=getActionURL(getactionid_manage().waitRecord_getNotifications);//获取告知书列表地址
        var uploadNotificationUrl=getActionURL(getactionid_manage().waitRecord_uploadNotification);//上传告知书地址
        var downloadNotificationUrl=getActionURL(getactionid_manage().waitRecord_downloadNotification);//下载告知书地址
        open_getNotifications(getNotificationsUrl,uploadNotificationUrl,downloadNotificationUrl);
    });


    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    var preDate = new Date(newDate.getTime()-24*60*60*1000);
    newDate.setDate(newDate.getDate());
    preDate.setDate(preDate.getDate())
    $('#Date').html(newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()]);
    setInterval( function() {
        var seconds = new Date().getSeconds();
        $("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
        var minutes = new Date().getMinutes();
        $("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
        var hours = new Date().getHours();
        $("#hours").html(( hours < 10 ? "0" : "" ) + hours);
        currenttime=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日' + dayNames[newDate.getDay()]+( hours < 10 ? "0" : "" ) + hours+"时"+( minutes < 10 ? "0" : "" ) + minutes+"分"+( seconds < 10 ? "0" : "" ) + seconds+"秒";
        yesterdaytime=preDate.getFullYear() + "年" + monthNames[preDate.getMonth()] + '月' + preDate.getDate() + '日' + dayNames[preDate.getDay()]+( hours < 10 ? "0" : "" ) + hours+"时"+( minutes < 10 ? "0" : "" ) + minutes+"分"+( seconds < 10 ? "0" : "" ) + seconds+"秒";


        if (isNotEmpty(select_monitorall_iframe_body)) {
            var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ';
            var week=dayNames[newDate.getDay()];
            var time=$("#hours").html() + "：" + $("#min").html() + "：" + $("#sec").html();
            select_monitorall_iframe_body.find("#dqtime1").html(date);
            select_monitorall_iframe_body.find("#dqtime2").html(week+time);
        }



        if (isNotEmpty(mtssid)&&isNotEmpty(TDCache)) {
            var usepolygraph=TDCache.usepolygraph==null?-1:TDCache.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
            if (usepolygraph==1){//使用测谎仪开启获取
                getPolygraphdata();
            }
            if (isNotEmpty(mcbool)&&(mcbool==1||mcbool==3)){
                getEquipmentsState();
            }
        }

        var recordreals_selecthtml=document.getElementById("recordreals_selecthtml");
        var IHTML='<span class="layui-table-sort layui-inline" title="语音识别可滚动"><i class="layui-edge layui-table-sort-asc"></i><i class="layui-edge layui-table-sort-desc" "></i></span>';
        if(recordreals_selecthtml.scrollHeight>recordreals_selecthtml.clientHeight||recordreals_selecthtml.offsetHeight>recordreals_selecthtml.clientHeight){
            $("#webkit2").html(IHTML)
        }else {
            $("#webkit2").empty();
        }


    },1000);




    // 建立连接
    if (isNotEmpty(SOCKETIO_HOST)&&isNotEmpty(SOCKETIO_PORT)) {

        socket = io.connect('http://'+SOCKETIO_HOST+':'+SOCKETIO_PORT+'');
        socket.on('connect', function (data) {
            console.log("socket连接成功__SOCKETIO_HOST："+SOCKETIO_HOST+"__SOCKETIO_PORT："+SOCKETIO_PORT);
        });
        socket.on('disconnect', function (data) {
            console.log("socket断开连接__");
        });



        socket.on('getback', function (data) {
            var mtssiddata=data.mtssid;
            if (isNotEmpty(mtssiddata)&&isNotEmpty(mtssid)&&mtssiddata==mtssid) {
                if (isNotEmpty(recorduser)){
                    for (var i = 0; i < recorduser.length; i++) {
                        var user = recorduser[i];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"":data.txt;//翻译文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";
                            var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本


                            var p_span_HTML="";
                            //实时会议数据
                            if (usertype==1){
                                p_span_HTML='  <a style="display: none;color: #ccc" id="dqphdate"></a>\
                                            <p><a id="username_time">【'+username+'】 '+asrstartime+' </a><a class="layui-badge" style="display:none;" title="未找到最高值">-1</a></p>\
                                            <span id="translatext" >'+translatext+'</span>';
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' usertype='+usertype+' dqphdate="" id="asrdiv" onmousedown="copy_text(this,event)">'+p_span_HTML+'</div >';

                            }else if (usertype==2){
                                p_span_HTML= '<a style="display: none;color: #ccc" id="dqphdate"></a>\
                                            <p><a class="layui-badge " style="visibility:hidden; background-color: #00CD68  " title="未找到最高值">-1</a>  <a  id="username_time">'+asrstartime+' 【'+username+'】</a> </p>\
                                             <span id="translatext"  >'+translatext+'</span> ';
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' usertype='+usertype+' dqphdate="" id="asrdiv" onmousedown="copy_text(this,event)">'+p_span_HTML+'</div >';
                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").html(p_span_HTML);
                            }else {
                                $("#recordreals").append(recordrealshtml);
                            }


                            $("#asritem").hover(
                                function(){
                                    mouseoverbool_left=1
                                } ,
                                function(){
                                    mouseoverbool_left=-1;
                              });

                            if (mouseoverbool_left==-1){
                                var div = document.getElementById('recordreals_scrollhtml');
                                div.scrollTop = div.scrollHeight;
                            }
                            $("#recordreals_selecthtml").show();


                            //检测自动上墙是否开启
                            var record_switch_bool=$("#record_switch_bool").attr("isn");
                            if (record_switch_bool==1){
                                identify(usertype,starttime,null,translatext);
                            }
                        }
                    }
                }
            }
        });
    }else{
        console.log("socket连接失败")
    }


    $("#record_switch_bool").click(function () {
        var isn=$(this).attr("isn");
        var obj=this;
        var con;
        if (isn==-1) {
            con="确定要开启自动甄别吗";
        }else{
            con="确定要关闭自动甄别吗";
        }
        layer.open({
            content:con
            ,btn: ['确定', '取消']
            ,yes: function(index, layero){
                if (isn==-1){
                    $(obj).attr("isn",1);
                    $(obj).addClass("layui-form-onswitch");
                    $(obj).find("em").html("开启");
                    layer.msg("自动甄别已开启");
                } else {
                    $(obj).attr("isn",-1);
                    $(obj).removeClass("layui-form-onswitch");
                    $(obj).find("em").html("关闭");
                    layer.msg("自动甄别已关闭");
                }
                layer.close(index);
            }
            ,btn2: function(index, layero){
                layer.close(index);
            }
            ,cancel: function(){
            }
        });
    });

    $("#baocun").click(function () {
        recordbool=1;
        overbtn();
    });
});

//制作中结束
function overbtn() {
    var setRecordrealUrl=getActionURL(getactionid_manage().waitRecord_setRecordreal);
    setRecordreal(setRecordrealUrl);
    var addRecordUrl=getActionURL(getactionid_manage().waitRecord_addRecord);
    var backurl=null;
    if (gnlist.indexOf(HK_O)<0){
        //hk跳转
        backurl=getActionURL(getactionid_manage().waitRecord_torecordIndex);
    }else {
        backurl=getActionURL(getactionid_manage().waitRecord_tocaseIndex);
    }
    addRecord(addRecordUrl,backurl);//回放不需要跳转地址
}

//获取身心检测数据
function getPolygraphdata() {
    if (isNotEmpty(mtssid)&&isNotEmpty(MCCache)){
        var polygraphnum=MCCache.polygraphnum;//本次会议开启的测谎仪个数
        if (null!=polygraphnum&&polygraphnum>0){
            var url=getUrl_manage().getPolygraphdata;
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    mtssid: mtssid
                }
            };
            ajaxSubmitByJson(url, data, callbackgetPolygraphdata);
        }
    }else{
        console.log("身心监测信息未找到__"+mtssid);
    }
}
function callbackgetPolygraphdata(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data=data.data;
        if (isNotEmpty(data)){
            var obj=data.t;
            if (isNotEmpty(obj)) {
                dqphdata(obj,true,null);
            }
        }
    }else{
        /* layer.msg(data.message);*///不需要弹出错误信息
        console.log(data.message)
    }
}


layui.use('form', function(){
    var form=layui.form;
    form.on('switch(voicebool)', function (data) {
        if (data.elem.checked) {
            FDAudPowerMapTimer = setInterval(function () {
                getFDAudPowerMap();
            }, 500);
            $("#voice").show();
            $("#voice2").show();
        }else{
            $("#voice").hide();
            $("#voice2").hide();
            clearInterval(FDAudPowerMapTimer);
        }

    });
    form.on('switch(sceneVoice)', function (data) {
        if (data.elem.checked) {
            setVolume(50);
        }else{
            setVolume(0);
        }
    });
    form.render();
});

