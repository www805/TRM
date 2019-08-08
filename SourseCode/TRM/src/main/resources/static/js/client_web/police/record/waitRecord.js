var templatessid=null;//当前选择的模板ssid
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

var recorduser=[];//会议用户集合：副麦主麦
var dq_recorduser=null;//当前被询问人ssid

var mcbool=null;//会议状态
var recordbool=null;//笔录状态 3 4暂时用于导出判断


var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上
var dqselec_left="";//当前左侧鼠标选择的文本
var dqselec_right="";//当前左侧鼠标选择的文本

var MCCache=null;//会议缓存数据
var TDCache=null;//会议通道缓存：不可借用会议缓存json转换识别（转换失败原因：疑似存在线程对象）
var  fdrecordstarttime=0;//直播开始时间戳（用于计算回车笔录时间锚点）

var getRecordById_data=null;//单份笔录返回的全部数据

var dq_livingurl=null;//当前直播地址
var dq_previewurl=null;//当前预览地址

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
        layer.msg(data.message);
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
                                <td style="padding: 0;width: 90%;" class="onetd" >\
                                    <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"  onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.problem+'" q_starttime="" >'+templateToProblem.problem+'</label></div>\
                                    <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.referanswer+'" w_starttime=""></label></div>\
                                    <div  id="btnadd"></div>\
                                </td>\
                                <td style="float: right;">\
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
        layer.msg(data.message);
    }

}

//导入单个题目
function copy_problems(obj) {
    var text=$(obj).find("span").text();
    var w=$(obj).attr("referanswer");
    var html='<tr>\
        <td style="padding: 0;width: 90%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"  class=""  placeholder="'+text+'"  q_starttime="">'+text+'</label></div>\
            <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  class="" placeholder="'+w+'" w_starttime=""></label></div>\
            <div  id="btnadd"></div>\
        </td>\
        <td style="float: right;">\
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
    setRecordreal();
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
    addbtn();
    setRecordreal();
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
    addbtn();
    setRecordreal();
}

//录音按钮显示隐藏 type:1开始录音
var startMC_index;
function img_bool(obj,type){

    if (type==1){
        //开始会议
        console.log("开始会议")
         startMC_index = layer.msg("笔录开启中，请稍等...", {
            icon: 16,
            time:-1
        });
        $("#record_img img").css("display","none");
        $("#startrecord").css("display","block");
        layer.closeAll('tips');
        $("#pauserecord").attr("onclick","");
        startMC();
    }else if (type==2) {
       //暂停录音
        console.log("暂停会议")
    }else if(type==-1) {
        console.log("会议已结束")
        layer.msg("该案件已开启过笔录");
    }
}

//粘贴语音翻译文本
var copy_text_html="";
var touchtime = new Date().getTime();
function copy_text(obj,event) {
    var text=$(obj).text();
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
   if( new Date().getTime() - touchtime < 300 ){
       var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+qw+'"]');
       var old= $html.attr(qw+"_starttime");
       var h=$html.html();
       $html.append(copy_text_html);
       if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
           $html.attr(qw+"_starttime",starttime);//直接使用最后追加的时间点
       }
    }else{
        touchtime = new Date().getTime();
       if (3 == event.which&&isNotEmpty(dqselec_left)){
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
   }
    copy_text_html="";
    setRecordreal();
    return false;
}



//编辑框下面按钮事件-------------------------------start-------------------------------
var currenttime;//当前时间
var yesterdaytime;//昨日时间
var occurrencetime_format;//案发时间
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
function get_case_time(obj) {

    if (isNotEmpty(occurrencetime_format)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(occurrencetime_format);
                setRecordreal();
            }
        });
        btn(obj);
    }
}
function get_current_time(obj) {
    getTime();
    if (isNotEmpty(currenttime)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(currenttime);
                setRecordreal();
            }
        });
        btn(obj);
    }
}
function get_yesterday_time(obj) {
    getTime();
    if (isNotEmpty(yesterdaytime)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(yesterdaytime);
                setRecordreal();
            }
        });
        btn(obj);
    }
}
//获取当期时间
function getTime() {
    var url=getActionURL(getactionid_manage().waitRecord_getTime);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetTime);
}
function callbackgetTime(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            currenttime=data.currenttime;
            yesterdaytime=data.yesterdaytime;
        }
    }else{
        layer.msg(data.message);
    }
}
//编辑框下面按钮事件-------------------------------end-------------------------------


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
            var record=data.record;
            if (isNotEmpty(record)){
                //获取提讯会议ssid
                mcbool=record.mcbool;
                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    var mtssiddata=police_arraignment.mtssid;
                    if (isNotEmpty(mtssiddata)){
                        mtssid=mtssiddata;
                        getRecordrealing();
                    }

                    if ((!isNotEmpty(mcbool)||mcbool!=1)&&isNotEmpty(mtssiddata)){
                        //存在会议但是状态为空或者1
                        $("#record_img img").css("display","none");
                        $("#endrecord").css("display","block");
                        $("#pauserecord").attr("onclick","");
                    }else if (null!=mcbool&&mcbool==1){
                        //存在会议状态正常
                        $("#record_img img").css("display","none");
                        $("#startrecord").css("display","block");
                        $("#pauserecord").attr("onclick","");
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                            layer.closeAll("tips");
                        });

                        }else {
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                                var layer=layui.layer;
                                layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
                        });

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
                    recorduser.push(user1);
                    recorduser.push(user2);
                    dq_recorduser=recordUserInfosdata.userssid;
                }


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
                                  <tr><td>询问人二</td> <td>"+otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>\
                                  <tr><td>笔录类型</td><td>"+recordtypename+"</td> </tr>";
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
            }, 10000);

        }
    }else{
        layer.msg(data.message);
    }
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
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
            var caseAndUserInfo=record.caseAndUserInfo;
            var recordUserInfos=record.recordUserInfos;
            var police_arraignment=record.police_arraignment;
            casenum=caseAndUserInfo.casenum;
            casename=caseAndUserInfo.casename;
            cause=caseAndUserInfo.cause;
            department=caseAndUserInfo.department;
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
            $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
            });
        });
    }
}

function callbackstartMC(data) {
    layer.close(startMC_index);
    if(null!=data&&data.actioncode=='SUCCESS'){
        $("#record_img img").css("display","none");
        $("#startrecord").css("display","block");
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
                        liveurl = useret.previewurl;//开始会议后默认使用副麦预览地址
                        dq_livingurl=useret.livingurl;//当前直播地址
                        dq_previewurl=useret.previewurl;//当前预览地址
                        console.log("当前liveurl————"+liveurl)
                    }
                }
                initplayer();//初始化地址
            }
            mtssid=mtssiddata;
            mcbool=1;//正常开启

            getMCCacheParamByMTssid();//获取缓存
            getTDCacheParamByMTssid();

            var con="笔录已开启：<br>语音识别开启数："+asrnum+"<br>测谎仪开启数："+polygraphnum+"<br>设备录音数："+recordnum;
            layer.msg(con, {time: 2000});
        }
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
            if (null!=recordbool&&recordbool==true){
                $("#record_img img").css("display","none");
                $("#endrecord").css("display","block");
                $("#pauserecord").attr("onclick","");
            }else {
                $("#record_img img").css("display","none");
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
                });
            }

            if (null!=checkStartRecordVO){
                var msg=checkStartRecordVO.msg;
                parent.layer.confirm("笔录开启失败(<span style='color:red'>"+msg+"</span>)，请先结束正在进行中的笔录", {
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


        layer.msg("笔录开启失败");

    }
}

//获取会议缓存
function getMCCacheParamByMTssid() {
    if (mcbool==1){
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
    if (mcbool==1&&isNotEmpty(dq_recorduser)) {//会议正常的时候
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
//recordbool 1进行中 2已结束 0初始化
function addRecord() {
    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().waitRecord_addRecord);
        //需要收拾数据
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
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                recordbool:recordbool,
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
                layer.msg("笔录结束",{time:500},function () {
                    window.history.go(-1);
                })
            }else if (recordbool==3){//导出word
                var url=getActionURL(getactionid_manage().waitRecord_exportWord);
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
                            layer.msg("导出成功,等待下载中...");
                        }
                    }else{
                        layer.msg(data.message);
                    }
                });
            } else  if (recordbool==4){//导出pdf
                var url=getActionURL(getactionid_manage().waitRecord_exportPdf);
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
                                maxmin: true, //开启最大化最小化按钮
                                area: ['893px', '600px'],
                            });

                            showPDF("pdfid",data);
                            layer.msg("导出成功,等待下载中...");
                        }
                    }else{
                        layer.msg(data.message);
                    }
                });
            } else {
                layer.msg('保存成功');
            }
        }
    }else{
        layer.msg(data.message);
    }
    $("#overRecord_btn").attr("click","overRecord();");
}

//结束笔录按钮
var overRecord_index=null;
var overRecord_loadindex =null;
 function overRecord() {
    layer.confirm('是否结束笔录?<br/><span style="color: red">*确保笔录类型存在对应模板否则导出功能失效</span>', {
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
        addRecord();
        overRecord_loadindex = layer.msg("保存中，请稍等...", {
            typy:1,
            icon: 16,
            shade: [0.1, 'transparent']
        });
    }, function(index){
        layer.close(index);
    });
}


//导出word
function exportWord(obj){
    recordbool=3; //不存在数据库
    addRecord();
    btn(obj);
    layer.msg("导出中，请稍等...", {
        icon: 16,
        shade: [0.1, 'transparent']
    });
}
function exportPdf(obj) {
    recordbool=4; //不存在数据库
    addRecord();
    btn(obj);
    layer.msg("导出中，请稍等...", {
        icon: 16,
        shade: [0.1, 'transparent']
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
                    liveurl=fdCacheParam.previewurl;//开始会议后默认使用副麦预览地址
                    dq_livingurl= fdCacheParam.livingUrl;
                    dq_previewurl= fdCacheParam.previewurl;
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
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";

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
                            $('#recordreals span').mouseup(function(){
                                var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
                                dqselec_left= txt.toString();
                            })
                            var div = document.getElementById('recordreals_scrollhtml');
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




/**
 * 视频地址切换 type 1主麦 type 2副麦
 */

function select_liveurl(obj,type){
    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
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
    if (e && e.keyCode == 13) { //回车键的键值为13
        var dqname=$(obj).attr("name");
        var trindex= $(obj).closest("tr").index();
        if (dqname=="q") {
            var lable=$('#recorddetail tr:eq("'+trindex+'") label[name="w"]');//定位本行的答
            setFocus(lable);
        }else {
            var trlength=$("#recorddetail tr").length;
            if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                focuslable(trtd_html,1,'q');
            } else {
                var lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                setFocus(lable);
            }
        }
        event.preventDefault();
    }
}
function setFocus(el) {
    if (isNotEmpty(el)){
        el = el[0];
        el.focus();

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
};


//自动上墙
function setrecord_html() {
    $("#recorddetail tr").attr("automaticbool","");
    var trtd_html='<tr automaticbool="1">\
        <td style="padding: 0;width: 90%;" class="onetd" >\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"   q_starttime=""  ></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="" w_starttime="" ></label></div>\
               <div  id="btnadd"></div>\
                </td>\
                <td style="float: right;">\
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
    $("#initheart_click").removeClass("layui-show");
    $(".layui-tab-content").css("height","650px");

    if (isNotEmpty($("#living3_2").html())) {
        $("#living3_1").html($("#living3_2").html());
        $("#living3_2").html("");
        if (isNotEmpty(recorduser)){
            for (var i = 0; i < recorduser.length; i++) {
                var useret = recorduser[i];
                var userssid1=useret.userssid;
                if (userssid1==dq_recorduser){
                    liveurl=dq_livingurl;
                    console.log("当前liveurl————"+liveurl)
                }
            }
        }
        initplayer();//启动设备画面预览
    }

    layui.use(['element'], function(){
        var element = layui.element;
        //使用模块
        element.render();
    });
    initplayer();
    main1();

}
//语音识别
function initasr() {
    $("#initheart_click").addClass("layui-show");
    $(".layui-tab-content").css("height","450px");
}
//案件
function initcase() {
    $("#initheart_click").addClass("layui-show");
    $(".layui-tab-content").css("height","450px");
}
function initcase_header() {
    $("#initec ul li").removeClass("layui-this");
    $("#initec .layui-tab-item").removeClass("layui-show");
    $("#case").addClass("layui-this");
    $("#caseitem").addClass("layui-show");

    $("#initheart_click").addClass("layui-show");
    $(".layui-tab-content").css("height","450px");
}
//直播
function initliving() {
    $("#initheart_click").removeClass("layui-show");
    $(".layui-tab-content").css("height","650px");


    var html=$("#living3_2").html();
   if (!isNotEmpty(html)){
        $("#living3_2").html($("#living3_1").html());
        $("#living3_1").html("");
    }
    initplayer();//启动设备画面预览
}
//*******************************************************************点击end****************************************************************//





//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 90%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" q_starttime=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  w_starttime=""placeholder=""></label></div>\
               <div  id="btnadd"></div>\
                </td>\
                <td style="float: right;">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   </div>\
                </td>\
                </tr>';

//lable focus 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
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

    setRecordreal();
    $('#recorddetail label').bind('input', function() {
        setRecordreal();
    });
}

/***************************************笔录实时问答start*************************************************/
/*笔录实时保存*/
function setRecordreal() {
    var url=getActionURL(getactionid_manage().waitRecord_setRecordreal);

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
        layer.msg(data.message);
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
        layer.msg(data.message);
    }
}

//重置模板
function clearRecord() {
    //1、询问是否重置
    //1、保存重置内容，并且给出返回上一份笔录的按钮
    //返回按钮点击的时候询问要插入的位置当前光标孩纸追加后面或者最前面，没有任何光标将追加
    layer.open({
        content:"重置后问答笔录将清空，确定要重置吗？"
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
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid: recordssid,
                    recordToProblems:recordToProblems
                }
            };
            ajaxSubmitByJson(url, data, function (data) {
                if(null!=data&&data.actioncode=='SUCCESS'){
                    var data=data.data;
                    if (isNotEmpty(data)){
                        console.log("重置成功");
                        //撤销重置按钮
                        $("#getRecordreal_LastByRecordssid").show();
                    }
                }else{
                    layer.msg(data.message);
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
            setRecordreal();
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
                    content:"已获取重置问答，请选择要插入的笔录位置"
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
        layer.msg(data.message);
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
                        <td style="padding: 0;width: 90%;" class="onetd">\
                            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" placeholder="'+problemtext+'" q_starttime="'+problem.starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];
                    var answertext=answer.answer==null?"未知":answer.answer;
                    problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="'+answertext+'"   w_starttime="'+answer.starttime+'">'+answertext+'</label></div>';
                }
            }else{
                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder=""  w_starttime=""></label></div>';
            }
            problemhtml+=' <div  id="btnadd"></div></td>\
                        <td style="float: right;">\
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
    $('#recorddetail label').mousedown(function (e) {
        var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
        dqselec_right= txt.toString();
        if (isNotEmpty(dqselec_right)) {
            if (3 == e.which) {
                console.log("右键")
                if (null!=td_lastindex["key"]&&null!=td_lastindex["value"]) {
                    var $label=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+td_lastindex["value"]+'"]');
                    var $txt = $label.text();
                    $label.html($txt);

                    var newtxt=[];
                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if ((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value)){
                            var txt=tag.txt;
                            if(isNotEmpty(txt)){
                                for (let j = 0; j < txt.length; j++) {
                                    const shujuElement = txt[j];
                                    if(dqselec_right.indexOf(shujuElement)<0){
                                        var $html=$label.html();
                                        $html = $html.split(shujuElement).join('<a class="highlight_all" >'+ shujuElement +'</a>');
                                        $label.html($html);
                                    }
                                }
                            }
                        }
                    }
                    var newtxt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            newtxt.push(t);
                        }
                    })
                    newtxt=Array.from(new Set(newtxt));
                    var shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:newtxt
                    }
                    dqtag_right=[];
                    dqtag_right.push(shuju);
                }
            } else if (1 == e.which) {
                console.log("左键")
                if (null!=td_lastindex["key"]&&null!=td_lastindex["value"]){
                    var $label=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+td_lastindex["value"]+'"]');
                    var h=$label.html();
                    var txt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            txt.push(t);
                        }
                    })
                    txt=Array.from(new Set(txt));
                    var shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:txt
                    }
                    dqtag_right=[];
                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if (!((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value))){
                            dqtag_right.push(tag);
                        }
                    }

                    dqtag_right.push(shuju);

                    var $txt = $label.text();
                    $label.html($txt);
                    var $html = $label.html();
                    $html = $html.split(dqselec_right).join('<a class="highlight_all" >'+ dqselec_right +'</a>');
                    $label.html($html);

                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if ((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value)){
                            var txt=tag.txt;
                            if(isNotEmpty(txt)){
                                for (let j = 0; j < txt.length; j++) {
                                    const shujuElement = txt[j];
                                    if(dqselec_right.indexOf(shujuElement)<0){
                                        var $html=$label.html();
                                        $html = $html.split(shujuElement).join('<a class="highlight_all" >'+ shujuElement +'</a>');
                                        $label.html($html);
                                    }
                                }
                            }
                        }
                    }
                    var newtxt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            newtxt.push(t);
                        }
                    })
                    newtxt=Array.from(new Set(newtxt));
                    shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:newtxt
                    }
                    dqtag_right=[];
                    dqtag_right.push(shuju);
                }
            }
        }
    });
}







//右侧标记的文本：当前
var dqtag_right=[];//{key:0,value:"q",txt:["哈哈","哈哈"]}



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
$(function () {

    /*对于trm.exe不适应会卡死
     $(window).bind('beforeunload',function(){
          return '确定要离开当前页面吗';
      });*/

    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
    $('#recorddetail label').bind('input', function() {
        setRecordreal();
    });
   //初始化第一行的焦点


    $('#recorddetail label').mousedown(function (e) {
        var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
        dqselec_right= txt.toString();
        if (isNotEmpty(dqselec_right)) {
            if (3 == e.which) {
                console.log("右键")
                if (null!=td_lastindex["key"]&&null!=td_lastindex["value"]) {
                    var $label=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+td_lastindex["value"]+'"]');
                    var $txt = $label.text();
                    $label.html($txt);

                    var newtxt=[];
                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if ((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value)){
                            var txt=tag.txt;
                            if(isNotEmpty(txt)){
                                for (let j = 0; j < txt.length; j++) {
                                    const shujuElement = txt[j];
                                    if(dqselec_right.indexOf(shujuElement)<0){
                                        var $html=$label.html();
                                        $html = $html.split(shujuElement).join('<a class="highlight_all" >'+ shujuElement +'</a>');
                                        $label.html($html);
                                    }
                                }
                            }
                        }
                    }
                    var newtxt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            newtxt.push(t);
                        }
                    })
                    newtxt=Array.from(new Set(newtxt));
                    var shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:newtxt
                    }
                    dqtag_right=[];
                    dqtag_right.push(shuju);
                }
            } else if (1 == e.which) {
                console.log("左键")
                if (null!=td_lastindex["key"]&&null!=td_lastindex["value"]){
                    var $label=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+td_lastindex["value"]+'"]');
                    var h=$label.html();
                    var txt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            txt.push(t);
                        }
                    })
                    txt=Array.from(new Set(txt));
                    var shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:txt
                    }
                    dqtag_right=[];
                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if (!((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value))){
                            dqtag_right.push(tag);
                        }
                    }

                    dqtag_right.push(shuju);

                    var $txt = $label.text();
                    $label.html($txt);
                    var $html = $label.html();
                    $html = $html.split(dqselec_right).join('<a class="highlight_all" >'+ dqselec_right +'</a>');
                    $label.html($html);

                    for (let i = 0; i < dqtag_right.length; i++) {
                        const tag = dqtag_right[i];
                        if ((td_lastindex["key"]==tag.key)&&(td_lastindex["value"]==tag.value)){
                            var txt=tag.txt;
                            if(isNotEmpty(txt)){
                                for (let j = 0; j < txt.length; j++) {
                                    const shujuElement = txt[j];
                                    if(dqselec_right.indexOf(shujuElement)<0){
                                        var $html=$label.html();
                                        $html = $html.split(shujuElement).join('<a class="highlight_all" >'+ shujuElement +'</a>');
                                        $label.html($html);
                                    }
                                }
                            }
                        }
                    }
                    var newtxt=[];
                    $("a",$label).each(function () {
                        var t=$(this).text();
                        if(isNotEmpty(t)){
                            newtxt.push(t);
                        }
                    })
                    newtxt=Array.from(new Set(newtxt));
                    shuju={
                        key:td_lastindex["key"],
                        value:td_lastindex["value"],
                        txt:newtxt
                    }
                    dqtag_right=[];
                    dqtag_right.push(shuju);
                }
            }
        }
    });


    $(document).keypress(function (e) {
        if (e.which == 13) {
            focuslable(trtd_html,2,'q');
            event.preventDefault();
        }
    });



    $("#dl_dd dd").click(function () {
        var text=$(this).attr('lay-value');
        //文本
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
          /*  if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {*/
           if (lastindex==td_lastindex["key"]&&value=="w") {
                $(this).append(text);
               setRecordreal();
            }
        });
        btn(this);
    })

    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    newDate.setDate(newDate.getDate());
    $('#Date').html(newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()]);
    setinterval1= setInterval( function() {
        var seconds = new Date().getSeconds();
        $("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
        var minutes = new Date().getMinutes();
        $("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
        var hours = new Date().getHours();
        $("#hours").html(( hours < 10 ? "0" : "" ) + hours);

        if (isNotEmpty(select_monitorall_iframe_body)) {
           /* select_monitorall_iframe_body.find("#dqtime").html($('#Date').html() + $("#hours").html() + "：" + $("#min").html() + "：" + $("#sec").html());*/
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
            if ((isNotEmpty(mcbool)&&mcbool==1)){
                getEquipmentsState();
            }
        }

    },1000);


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
                            $('#recordreals span').mouseup(function(){
                                var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
                                dqselec_left= txt.toString();
                            })

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
                                    if (usertype==1){
                                        qq=translatext;
                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        setrecord_html();
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                    }
                                }else  if (last_type==1){
                                    //最后是问
                                    var last_type2=last_type;
                                    last_type=usertype;
                                    if (usertype==1){
                                        //最后是问，本次是问，判断本次问和最后一次问的时间是否一致，一致问刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_qq==starttime||laststarttime_qq==-1){
                                            qq=translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qqq+qq);
                                        }else{
                                            qqq+=qq;
                                            qq=translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qqq+qq);
                                        }
                                    }else if (usertype==2){
                                        //最后是问，本次是答，开始拼接答案
                                        if (w_prevstarttime==starttime){
                                            last_type=last_type2;//还原
                                            console.log("修改即可————————————————————————————w")
                                            console.log("ww_prev_______________________________"+ww_prev)
                                            $("#recorddetail tr[automaticbool='1']").prev().find("td:first label[name='w']").text(ww_prev+translatext);
                                        }else {
                                            ww=translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(ww);
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").attr("w_starttime",starttime);
                                            ww_prev=ww;
                                            console.log("ww_prev3_____________________________"+ww_prev)
                                        }
                                    }
                                }else  if (last_type==2){
                                    //最后是答
                                    last_type=usertype;
                                    if (usertype==2){//最后是答，本次确实答，判断本次答和最后一次答的时间是否一致，一致刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_ww==starttime||laststarttime_ww==-1){
                                            ww=translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(www+ww);
                                        }else{
                                            www+=ww;
                                            ww=translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(www+ww);
                                        }
                                        ww_prev=www;
                                        console.log("ww_prev2_________________________"+ww_prev)
                                    }else if (usertype==1){//最后是答，本次为问
                                        //判断本次问是否和上一次问相同时间，相同改变否则追加一行
                                        if (laststarttime_qq==starttime||laststarttime_qq==-1){
                                            console.log("修改即可————————————————————————————q")
                                            qq=translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q'] ").text(qqq+qq);
                                        }else {
                                            console.log("从这里开始追加新的一行_________")
                                            w_prevstarttime=laststarttime_ww;

                                           /* if (isNotEmpty(www)){
                                                ww_prev=www;
                                                console.log("ww_prev2____"+ww_prev)
                                            }else {
                                              var txt=  $("#recorddetail tr[automaticbool='1']").find("td:first label[name='w']").text();
                                                ww_prev=txt;
                                                console.log("ww_prev3____"+ww_prev)
                                            }*/


                                            //2.初始化问答
                                            laststarttime_qq=-1;
                                            laststarttime_ww=-1;
                                            last_type=-1;//1问题 2是答案
                                            qq="";
                                            qqq="";
                                            ww="";
                                            www="";

                                            qq=translatext;
                                            last_type=usertype;
                                            laststarttime_qq=starttime;
                                            setrecord_html();
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
                                        }
                                    }
                                }
                                setRecordreal();//实时保存一下
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
        addRecord();
    });
});



