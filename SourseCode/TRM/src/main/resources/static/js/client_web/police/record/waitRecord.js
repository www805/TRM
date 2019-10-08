var templatessid=null;//当前选择的模板ssid
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

var recorduser=[];//会议用户集合：副麦主麦
var dq_recorduser=null;//当前被询问人ssid

var mcbool=null;//会议状态
var recordbool=null;//笔录状态 -1 -2暂时用于导出判断不存在数据库

var casebool=null;//案件状态


var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上

var MCCache=null;//会议缓存数据
var TDCache=null;//会议通道缓存：不可借用会议缓存json转换识别（转换失败原因：疑似存在线程对象）
var  fdrecordstarttime=0;//直播开始时间戳（用于计算回车笔录时间锚点）

var getRecordById_data=null;//单份笔录返回的全部数据

var dq_livingurl=null;//当前直播地址
var dq_previewurl=null;//当前预览地址

var record_pausebool=-1;//笔录是否允许暂停1允许 -1 不允许 默认不允许-1
var record_adjournbool=-1;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1

var occurrencetime_format;//案发时间

var multifunctionbool;

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
                                <td style="padding: 0;width: 85%;" class="onetd" >\
                                    <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"  onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.problem+'" q_starttime="" isn_fdtime="-1">'+templateToProblem.problem+'</label></div>\
                                    <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.referanswer+'" w_starttime=""  isn_fdtime="-1"></label></div>\
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
        <td style="padding: 0;width: 85%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"  class=""  placeholder="'+text+'"  q_starttime="" isn_fdtime="-1">'+text+'</label></div>\
            <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  class="" placeholder="'+w+'" w_starttime="" isn_fdtime="-1"></label></div>\
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

//tr移动删除事件
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
    /*setRecordreal();*/
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
    addbtn();
   /* setRecordreal();*/
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
    addbtn();
    /*setRecordreal();*/
}

//录音按钮显示隐藏 type:1开始录音
var startMC_index;
function img_bool(obj,type){
    layer.closeAll('tips');
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
            $("#startrecord").css("display","block");
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips("笔录中~" ,'#startrecord',{time:0, tips: 1});
            });
            layer.msg("笔录中~");
        }
    }else if(type==-1) {
        $("#endrecord").css("display","block");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips:1});
        });
        console.log("会议已结束")
        layer.msg("该笔录已经制作过啦~");
    }
}

//粘贴语音翻译文本
var copy_text_html="";
var touchtime = new Date().getTime();
function copy_text(obj,event) {
    var text=$(obj).html();
    copy_text_html=text;
    var classc=$(obj).closest("div").attr("class");
    var starttime=$(obj).closest("div").attr("starttime");

    var qw=null;
    if ((classc=="atalk"&&1 == event.which)||(classc=="btalk"&&3 == event.which)) {//左键并且为问||右键并且为答
        qw="q";
    }else  if ((classc=="btalk"&&1 == event.which)||(classc=="atalk"&&3 == event.which)){//左键并且为答 || 右键并且为问
        qw="w";
    }


    //鼠标双击事件
   if( new Date().getTime() - touchtime < 350 ){
       console.log("现在是双击事件")
       var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+qw+'"]');
       var old= $html.attr(qw+"_starttime");
       var h=$html.html();
       $html.append(copy_text_html);
       if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
           $html.attr(qw+"_starttime",starttime);//直接使用最后追加的时间点
       }
    }else{
       console.log("现在是单击事件")
       var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
       var dqselec_left= txt.toString();
       if (3 == event.which&&isNotEmpty(dqselec_left)&&copy_text_html.indexOf(dqselec_left)>-1&&new Date().getTime() - touchtime >700){
           if (classc=="btalk") {
               qw="w";
           }else if(classc=="atalk"){
               qw="q";
           }
           var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+qw+'"]');
           var old= $html.attr(qw+"_starttime");
           var h=$html.html();
           $html.append(dqselec_left);
           dqselec_left="";
           window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
           if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
               $html.attr(qw+"_starttime",starttime);//直接使用最后追加的时间点
           }
       }
       touchtime = new Date().getTime();
   }
    copy_text_html="";
   /* setRecordreal();*/
    return false;
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

            var record=data.record;
            if (isNotEmpty(record)){
                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }


                mcbool=record.mcbool;

                //获取提讯会议ssid
                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    //按钮显示控制
                     multifunctionbool=police_arraignment.multifunctionbool;//按钮显示控制
                    if (multifunctionbool==2){
                        console.log("我是谈话笔录隐藏")
                        $("#fd").show();
                        $("#initec ul li").removeClass("layui-this");
                        $("#initec .layui-tab-item").removeClass("layui-show");
                        $("#case").addClass("layui-this");
                        $("#caseitem").addClass("layui-show");

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
                        $("#asr").show();
                        $("#fd").show();
                        $("#ph").show();
                        $("#xthtml").css("visibility","visible");

                        $("#initec ul li").removeClass("layui-this");
                        $("#initec .layui-tab-item").removeClass("layui-show");
                        $("#case").addClass("layui-this");
                        $("#caseitem").addClass("layui-show");

                        $("#record_switch_HTML").css("visibility","visible");
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
                        $("#endrecord").css("display","block");
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                            layer.tips('该笔录已经制作过啦~' ,'#endrecord',{time:0, tips: 1});
                        });
                        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
                    }else if (null!=mcbool&&(mcbool==1||mcbool==3)){
                        if (multifunctionbool==2){
                            $("#pauserecord").attr({"src":"/uimaker/images/record7.png","onclick":"img_bool(this,1);"});
                        }else if (multifunctionbool==3) {
                            $("#pauserecord").attr({"src":"/uimaker/images/record3.png","onclick":"img_bool(this,1);"});
                        }
                        //存在会议状态正常
                        if (mcbool==1){
                            $("#startrecord").css("display","block");
                            var tips_msg="笔录中~";
                            if (record_pausebool==1) {
                                tips_msg="点击我可以暂停~";
                            }
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips(tips_msg ,'#startrecord',{time:0, tips: 1});
                            });
                        } else if (mcbool==3&&record_pausebool==1) {
                            $("#pauserecord").css("display","block");
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips('点击我可以再次启动制作~' ,'#pauserecord',{time:0, tips: 1});
                            });
                        }

                        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
                    }else {
                        $("#pauserecord").css("display","block");
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                            layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 1});
                        });
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
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
                getMCCacheParamByMTssid();//获取缓存
                getTDCacheParamByMTssid();
            }

            //获取默认的片头信息
            getptdjconst(1);

            //开始获取设备状态
            getFDState();

            setInterval(function () {
              /*  console.log("刷新，获取设备状态");*/
                getFDState();
            }, 1000);

            setInterval(function () {
                putRecessStatus();
            }, 10000);

            //获取刻录选时
            getBurnTime();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layer.closeAll('tips');//###
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
    $("#start_over_btn").text("谈话开启中").attr("onclick","");
    if (isNotEmpty(getRecordById_data)){
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
        $("#LiveState").text("加载中");
        $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
        $("#PolygraphState").text("加载中");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});


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
                $("#pauserecord").css("display","block").attr({"src":"/uimaker/images/record5.png","onclick":"img_bool(this,1);"});
            }else  if (multifunctionbool==3){
                $("#pauserecord").css("display","block").attr({"src":"/uimaker/images/record.png","onclick":"img_bool(this,1);"});
            }
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 1});
            });
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
        $("#startrecord").css("display","block");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips(tips_msg ,'#startrecord',{time:0, tips: 1});
        });

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

            var con="已开启：<br>语音识别开启数："+asrnum+"<br>测谎仪开启数："+polygraphnum+"<br>设备录像数："+recordnum;
            layer.msg(con, {time: 2000});
        }
        $("#start_over_btn").text("结束谈话").attr("onclick","overRecord(0)");
    }else{
        $("#MtState").text("未启动");
        $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
        $("#AsrState").text("未启动");
        $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
        $("#LiveState").text("未启动");
        $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
        $("#PolygraphState").text("未启动");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});


        var data2=data.data;
        if (isNotEmpty(data2)){
            var checkStartRecordVO=data2.checkStartRecordVO;
            var recordbool=data2.recordbool; //笔录开始状态
            $("#record_img img").css("display","none");
            if (null!=recordbool&&recordbool==true){
                $("#endrecord").css("display","block");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips: 1});
                });
            }else {
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips:1});
                });
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

    layer.closeAll('tips');//###
}


/*
会议暂停或者继续 pauseOrContinue 1请求暂停，2请求继续
*/
function pauseOrContinueRercord(pauseOrContinue) {
    if (isNotEmpty(mtssid)){
        var url=getUrl_manage().pauseOrContinueRercord;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
                ,pauseOrContinue:pauseOrContinue
            }
        };
        ajaxSubmitByJson(url, data, callbackpauseOrContinueRercord);
    }
}

function callbackpauseOrContinueRercord(data) {
    layer.close(startMC_index);
    getMCCacheParamByMTssid();//获取缓存
    getTDCacheParamByMTssid();
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            //1请求暂停，2请求继续
            var pauseOrContinue=data.pauseOrContinue;
            var msg=pauseOrContinue==null?"":(pauseOrContinue==1?"暂停":"继续");
            var recordnum=data.recordnum;//录音设备暂停/停止个数
            var asrnum=data.asrnum;//语音识别服务暂停/停止个数
            var polygraphnum=data.polygraphnum;//测谎仪服务暂停/停止个数
            var con=msg+"：<br>语音识别"+msg+"数："+asrnum+"<br>测谎仪"+msg+"数："+polygraphnum+"<br>设备"+msg+"数："+recordnum;
            layer.msg(con, {time: 2000});
            if (pauseOrContinue==1){
                $("#pauserecord").css("display","block");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 1});
                });
            } else {
                $("#startrecord").css("display","block");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips: 1});
                });
            }

        }
    }else {
        var data2=data.data;
        if (isNotEmpty(data2)){
            var pauseOrContinue=data2.pauseOrContinue;
            $("#record_img img").css("display","none");
            if (pauseOrContinue==1){//请求暂停
                $("#startrecord").css("display","block");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips: 1});
                });
            } else if (pauseOrContinue==2){//请求继续
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 1});
                });
            }
        }
        console.log(data)
        layer.msg(data.message,{icon: 5});
    }
}




//获取会议缓存
function getMCCacheParamByMTssid() {
    if (mcbool==1||mcbool==3){
        var url=getUrl_manage().getMCCacheParamByMTssid;
        var d={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
            }
        };
        ajaxSubmitByJson(url, d, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    MCCache=data;
                }
            }
        });
    }
}

function getTDCacheParamByMTssid() {
    if ((mcbool==1||mcbool==3)&&isNotEmpty(dq_recorduser)) {//会议正常的时候
        var url=getUrl_manage().getTDCacheParamByMTssid;
        var d={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid,
                userssid:dq_recorduser,
            }
        };

        ajaxSubmitByJson(url, d, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    TDCache=data;
                    fdrecordstarttime=data.fdrecordstarttime==null?0:data.fdrecordstarttime;

                    //第一行上时间
                    var lable=  $('#first_originaltr label[name="q"]');
                    setFocus(lable);
                }
            }
        });
    }
}


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
function addRecord() {
    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().waitRecord_addRecord);
        //需要收拾数据
        var recordToProblems=[];//题目集合
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                recordbool:recordbool,
                casebool:casebool,
                recordToProblems:recordToProblems,
                mtssid:mtssid //会议ssid用于笔录结束时关闭会议
            }
        };
        $("#overRecord_btn").attr("click","");
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

            if (recordbool==2) {
                layer.msg("已结束",{time:500,icon:6},function () {
                    window.history.go(-1);
                })
            }else if (recordbool==-1){//导出word
                var url=getActionURL(getactionid_manage().waitRecord_exportWord);
                var paramdata={
                    token:INIT_CLIENTKEY,
                    param:{
                        recordssid: recordssid,
                    }
                };
                ajaxSubmitByJson(url, paramdata, function (data) {
                    if (isNotEmpty(exportWord_index)) {
                            layer.close(exportWord_index);
                    }
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
            } else  if (recordbool==-2){//导出pdf
                var url=getActionURL(getactionid_manage().waitRecord_exportPdf);
                var paramdata={
                    token:INIT_CLIENTKEY,
                    param:{
                        recordssid: recordssid,
                    }
                };
                ajaxSubmitByJson(url, paramdata, function (data) {
                    if (isNotEmpty(exportPdf_index)) {
                        layer.close(exportPdf_index);
                    }
                    if(null!=data&&data.actioncode=='SUCCESS'){
                        var data=data.data;
                        if (isNotEmpty(data)){
                            //window.location.href = data;
                            layer.open({
                                id:"pdfid",
                                type: 1,
                                title: '导出PDF',
                                shadeClose: true,
                                maxmin: true, //开启最大化最小化按钮
                                area: ['893px', '600px'],
                            });

                            showPDF("pdfid",data);
                            layer.msg("导出成功,等待下载中...",{icon: 6});
                        }
                    }else{
                        layer.msg(data.message,{icon: 5});
                    }
                });
            } else {
                layer.msg('保存成功',{icon:6});
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    $("#overRecord_btn").attr("click","overRecord();");
}

//结束笔录按钮
var overRecord_index=null;
var overRecord_loadindex =null;
 function overRecord(state) {
     var msgtxt2="是否结束？";
     if (state==1){
         msgtxt2="是否暂停？";
     }
     var msgtxt="";
     if (isNotEmpty(fdStateInfo)) {
         var atxt=fdStateInfo.roma_status==null?"":fdStateInfo.roma_status;//1是刻录中
         var btxt=fdStateInfo.romb_status==null?"":fdStateInfo.romb_status;
         if (isNotEmpty(atxt)&&isNotEmpty(btxt)&&atxt=="1"||btxt=="1") {
             msgtxt="<span style='color: red'>*存在光驱正在刻录中，审讯关闭将会停止刻录</span>"
         }
     }


    layer.confirm(msgtxt2+'<br/><span style="color: red">*确保存在对应模板否则导出功能失效</span><br>'+msgtxt, {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        if (null!=setinterval1){
            clearInterval(setinterval1);
        }

        $("#record_switch_bool").attr("isn",-1);
        $("#record_switch_bool").removeClass("layui-form-onswitch");
        $("#record_switch_bool").find("em").html("关闭");


        overRecord_index=index;
        recordbool=2;
        if (state==1){
            casebool=3;//需要暂停
        }


        addRecord();
        overRecord_loadindex = layer.msg("保存中，请稍等...", {
            typy:1,
            icon: 16,
            shade: [0.1, 'transparent'],
            time:10000
        });
    }, function(index){
        layer.close(index);
    });
}


/**
 * 获取会议asr实时数据
 */
function getRecordrealing() {
    if (isNotEmpty(mtssid)) {
        var url=getUrl_manage().getRecordrealing;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetgetRecordrealing);
    }
}
function callbackgetgetRecordrealing(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var datas = data.data;
        var loadindex = layer.msg("加载中，请稍等...", {
            icon: 16,
            time:1000
        });

        var list= datas.list;
        var fdCacheParams= datas.fdCacheParams;
        if (isNotEmpty(fdCacheParams)){
            for (var i = 0; i < fdCacheParams.length; i++) {
                var fdCacheParam = fdCacheParams[i];
                    dq_livingurl= fdCacheParam.livingUrl;
                    dq_previewurl= fdCacheParam.previewurl;
                    liveurl = dq_livingurl;//开始会议后默认使用副麦预览地址
                    console.log("当前liveurl————"+liveurl)
            }
            initplayer();
        }
        if (isNotEmpty(list)) {
            layer.close(loadindex);
            $("#recordreals").html("");
            $("#recordreals_selecthtml").show();
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
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";
                            var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本


                            /*translatext= checkKeyword(translatext);
                            console.log(translatext)*/

                            //实时会议数据
                            if (usertype==1){
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+' </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }else if (usertype==2){
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"][starttime="+starttime+"]").remove();
                            }

                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals_scrollhtml');
                            div.scrollTop = div.scrollHeight;
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}




/**
 * 视频地址切换 type 1主麦 type 2副麦
 */

function select_liveurl(obj,type){
    /*$(obj).removeClass("layui-bg-gray").attr("isn","1");
    $(obj).siblings().addClass("layui-bg-gray").attr("isn","-1");*/
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


//回车
function qw_keydown(obj,event) {
    var e = event || window.event;
  var keyCode = e.keyCode;

    var dqname=$(obj).attr("name");
    var trindex= $(obj).closest("tr").index();
    var trlength=$("#recorddetail tr").length;
    var lable=null;
    switch(keyCode){
        case 13:
            console.log("回车")
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
            console.log("上一句")
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
            console.log("下一句")
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
function setFocus(el) {
    if (isNotEmpty(el)){
        el = el[0];

        var isn_fdtime=el.getAttribute("isn_fdtime");//是否为模板里面的问答 -1不是 1 是的 用户回车追加时间点判别为模板里面的问题不加时间点
        if (!isNotEmpty(isn_fdtime)&&isn_fdtime!="-1") {
            //回车加锚点：先判断语音识别是否开启
            if (isNotEmpty(TDCache)&&isNotEmpty(MCCache)) {
                var useasr=TDCache.useasr==null?-1:TDCache.useasr;//是否使用语言识别，1使用，-1 不使用
                var asrnum=MCCache.asrnum==null?0:MCCache.asrnum;
                console.log("直播的开始时间："+fdrecordstarttime+";是否开启语音识别："+useasr)
                if ((useasr==-1&&isNotEmpty(mtssid))||(asrnum<1&&isNotEmpty(mtssid))&&(isNotEmpty(fdrecordstarttime)&&fdrecordstarttime>0)){
                    var dqtime=new Date().getTime();
                    var qw_type=el.getAttribute("name");
                    if (isNotEmpty(qw_type)&&(isNotEmpty(fdrecordstarttime)&&fdrecordstarttime>0)){
                        console.log("开始使用直播时间~")
                        if (qw_type=="w"){
                            var w_starttime=el.getAttribute("w_starttime");
                            if ((!isNotEmpty(w_starttime)||w_starttime<0)){
                                //计算时间戳
                                w_starttime=Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime))==null?0:Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime));
                                el.setAttribute("w_starttime",w_starttime);
                            }
                        }else  if (qw_type=="q"){
                            var q_starttime=el.getAttribute("q_starttime");
                            if ((!isNotEmpty(q_starttime)||q_starttime<0)){
                                //计算时间戳
                                q_starttime=Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime))==null?0:Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime));
                                el.setAttribute("q_starttime",q_starttime);
                            }
                        }
                    }
                }else {
                    console.log("使用语音识别时间~")
                }
            }
        }else {
                console.log("这是模板里面的题目~")
        }






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


//自动上墙
function setrecord_html() {
    $("#recorddetail tr").attr("automaticbool","");
    var trtd_html='<tr automaticbool="1">\
        <td style="padding: 0;width: 85%;" class="onetd" >\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"   q_starttime=""  ></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="" w_starttime="" ></label></div>\
               <div  id="btnadd"></div>\
                </td>\
                <td\>\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
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
    focuslable(trtd_html,2,null);//自动追加不设置光标
}


//*******************************************************************点击start****************************************************************//
//身心检测
function initheart() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//语音识别
function initasr() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//案件
function initcase() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
function initcase_header() {
    $(".layui-tab-content").css("height","0%");
    $("#templatetoproblem").css("height","62%");
}

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
        $(".phitem1").css("display","none");
        $("#shrink_html").css("display","block");
        $("#notshrink_html1").css("display","block");

        var html=$("#living3_1").html();
        if (!isNotEmpty(html)){
            $("#living3_1").html($("#living3_2").html());
            $("#living3_2").empty();
        }
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
        $(".phitem1").attr("id","");
        $(".phitem2").attr("id","phitem");
        main1();
    }

    initplayer();//启动设备画面预览
    $(obj).removeClass("layui-btn-primary").addClass("layui-btn-normal").siblings().removeClass("layui-btn-normal").addClass("layui-btn-primary");
}

//直播
function initliving() {
    $("#initheart_click").removeClass("layui-show");
    $(".layui-tab-content").css("height","300px");


    var html=$("#living3_2").html();
   if (!isNotEmpty(html)){
        $("#living3_2").html($("#living3_1").html());
        $("#living3_1").html("");
    }

    var type=$("#livetypes span[isn='1']").attr("type");
    type=parseInt(type);
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
    initplayer();//启动设备画面预览
}
//*******************************************************************点击end****************************************************************//





//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 85%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" q_starttime=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  w_starttime=""placeholder=""></label></div>\
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
        $("#recorddetail").hover(
            function(){
                mouseoverbool_right=1
            } ,
            function(){
                mouseoverbool_right=-1;
        });
        if (mouseoverbool_right==-1){
            var div = document.getElementById('recorddetail_scrollhtml');
            div.scrollTop = div.scrollHeight;
        }
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:last label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:last').index();
        }
     }

     if (isNotEmpty(qw)){
         setFocus(qwfocus);
         td_lastindex["value"]=qw;
     }
    addbtn();
    contextMenu();
}
//最后一行添加按钮初始化
function addbtn() {
    var btnhtml='<button type="button"  class="layui-btn layui-btn-warm" style="border-radius: 50%;width: 45px;height: 45px;padding:0px"  title="添加一行自定义问答" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon" style="font-size: 45px" >&#xe608;</i></button>';
    $("#recorddetail tr").each(function () {
        $("#btnadd",this).html("");
    });
  $('#recorddetail tr:last #btnadd').html(btnhtml);

    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });

   /* setRecordreal();
    $('#recorddetail label').bind('mouseout', function() {
        setRecordreal();
    });*/
}

/***************************************笔录实时问答start*************************************************/
/*笔录实时保存*/
function setRecordreal() {

    var url=getActionURL(getactionid_manage().waitRecord_setRecordreal);

    var recordToProblems=[];//题目集合
    $("#recorddetail td.onetd").each(function (i) {
        var arr={};
        var answers=[];//答案集合
        var q=$(this).find("label[name='q']").html();
        var q_starttime=$(this).find("label[name='q']").attr("q_starttime");
        //经过筛选的q
        var ws=$(this).find("label[name='w']");
        var w_starttime=$(this).find("label[name='w']").attr("w_starttime");
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
    var url=getActionURL(getactionid_manage().waitRecord_getRecordrealByRecordssid);
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
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
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
                var q_starttime=$(this).find("label[name='q']").attr("q_starttime");
                q=q.replace(/\s/g,'');
                //经过筛选的q
                var ws=$(this).find("label[name='w']");
                var w_starttime=$(this).find("label[name='w']").attr("w_starttime");
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
            laststarttime_qq=-1;
            laststarttime_ww=-1;
            last_type=-1;//1问题 2是答案
            qq="";
            qqq="";
            ww="";
            www="";
            td_lastindex={};
            /*setRecordreal();*/
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
/***************************************笔录实时问答end*************************************************/

//整合问答笔录html
function setqw(problems){
    if (isNotEmpty(problems)) {
        var problemhtml=null;
        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];
            var problemtext=problem.problem==null?"未知":problem.problem;
             problemhtml+= '<tr>\
                        <td style="padding: 0;width: 85%;" class="onetd">\
                            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" placeholder="\'+problemtext+\' " q_starttime="'+problem.starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];
                    var answertext=answer.answer==null?"未知":answer.answer;
                    problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="\'+answertext+\'"   w_starttime="'+answer.starttime+'">'+answertext+'</label></div>';
                }
            }else{
                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder=""  w_starttime=""></label></div>';
            }
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
        return problemhtml;
    }
    return null;
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


//自动甄别初始化
var laststarttime_qq=-1;//上一个问时间
var laststarttime_ww=-1;//上一个答时间
var last_type=-1;//上一个是 1问题 2是答案
var qq="";
var qqq="";
var ww="";
var www="";

/*var q_prevstarttime=-1;
var qq_prev="";*/
var w_prevstarttime=-1;
var ww_prev="";


//定时器关闭
var setinterval1=null;
//导出word
var exportWord_index=null;
var exportPdf_index=null;

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
                recordbool=-1; //不存在数据库
                addRecord();
                exportWord_index=layer.msg("导出中，请稍等...", {
                    icon: 16,
                    shade: [0.1, 'transparent'],
                    time:10000
                });
        }else  if(type==2){
                recordbool=-2; //不存在数据库
                addRecord();
                exportPdf_index=layer.msg("导出中，请稍等...", {
                    icon: 16,
                    shade: [0.1, 'transparent'],
                    time:10000
                });
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


    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    var preDate = new Date(newDate.getTime()-24*60*60*1000);
    newDate.setDate(newDate.getDate());
    preDate.setDate(preDate.getDate())
    $('#Date').html(newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()]);
    setinterval1= setInterval( function() {
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
            if (usepolygraph==1&&isNotEmpty(myChart)){//使用测谎仪开启获取
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

    setInterval( function() {
         setRecordreal();//5秒实时保存
    },3000)


//自动甄别初始化

    var laststarttime_qq=-1;//上一个问时间
    var laststarttime_ww=-1;//上一个答时间
    var last_type=-1;//上一个是 1问题 2是答案
    var qq="";//本次的问的内容
    var qqq="";//没有跳问答的已经写入行中问的内容
    var ww="";//本次的答的内容
    var www="";//没有跳问答的已经写入行中答的内容


    // 建立连接
    if (isNotEmpty(SOCKETIO_HOST)&&isNotEmpty(SOCKETIO_PORT)) {

        socket = io.connect('http://'+SOCKETIO_HOST+':'+SOCKETIO_PORT+'');
        socket.on('connect', function (data) {
            console.log("socket连接成功__");
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

                           /* translatext=checkKeyword(translatext);
                            console.log(translatext)*/


                            var p_span_HTML="";
                            //实时会议数据
                            if (usertype==1){
                                p_span_HTML='<p>【'+username+'】 '+asrstartime+' </p>\
                                            <span onmousedown="copy_text(this,event)"  >'+translatext+'</span>';
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';

                            }else if (usertype==2){
                                p_span_HTML= '<p>'+asrstartime+' 【'+username+'】 </p>\
                                             <span onmousedown="copy_text(this,event)" >'+translatext+'</span> ';
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';
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
                                if (last_type==-1){
                                    //初始化，开始有问就开始追加一行
                                    if (usertype==1){//初始化上墙，

                                        qq=translatext;

                                        setrecord_html();
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").html(qq);

                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        www="";
                                        qqq="";
                                    }
                                }else  if (last_type==1){
                                    //最后是问

                                    if (usertype==1){
                                        //最后是问，本次是问，判断本次问和最后一次问的时间是否一致，一致问刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_qq==starttime){//修改最后一条数据
                                            qq=translatext;
                                        }else{//追加这条数据
                                            qqq+=qq;//把上一条数据加到qqq，说明上一条数据已经完成，qqq记录的都是已经完成的问
                                            qq=translatext;
                                        }
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").html(qqq+qq);
                                        laststarttime_qq=starttime;
                                    }else if (usertype==2){
                                        //最后是问，本次是答，开始拼接答案
                                        if (laststarttime_ww==starttime){//判断这次答的时间跟上一句答的时间是否一样，一样的话
                                            console.log("修改即可————————————————————————————w")
                                            console.log("www_______________________________"+www);
                                            $("#recorddetail tr[automaticbool='1']").prev().find("td:first label[name='w']").html(www+translatext);
                                        }else {//建立新答
                                            ww=translatext;
                                            www="";
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").html(ww);
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").attr("w_starttime",starttime);
                                            console.log("建立新答_____________________________"+ww);
                                        }
                                        laststarttime_ww=starttime;
                                    }
                                    last_type=usertype;

                                }else  if (last_type==2){
                                    //最后是答

                                    if (usertype==2){//最后是答，本次确实答，判断本次答和最后一次答的时间是否一致，一致刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_ww==starttime){//答的修改最后一句
                                            ww=translatext;
                                        }else{//答的追加最后一句
                                            www+=ww;
                                            ww=translatext;
                                        }
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").html(www+ww);
                                        laststarttime_ww=starttime;
                                        console.log("www_________________________"+www)
                                    }else if (usertype==1){//最后是答，本次为问
                                        //判断本次问是否和上一次问相同时间，相同改变否则追加一行
                                        if (laststarttime_qq==starttime){//只需要修改最后一个问
                                            console.log("修改即可————————————————————————————q")
                                            qq=translatext;

                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q'] ").text(qqq+qq);
                                        }else {//追加一个新问
                                            console.log("从这里开始追加新的一行_________")
                                            qqq="";
                                            qq=translatext;
                                            setrecord_html();
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").html(qq);
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
                                        }
                                        laststarttime_qq=starttime;
                                    }
                                    last_type=usertype;
                                }
                              /*  setRecordreal();//实时保存一下*/
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
        addRecord();
    });
});



