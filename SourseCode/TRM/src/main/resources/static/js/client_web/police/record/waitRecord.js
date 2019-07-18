var templatessid=null;//模板ssid
var recordUserInfos;//询问人和被询问人数据
var td_lastindex={};//td的上一个光标位置  为0需要处理一下
var recorduser=[];//会议用户集合


var mcbool=null;//会议状态
var recordbool=null;//笔录状态


var fdrecordstarttime=0;//直播开始时间戳（用于计算笔录时间锚点）

var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上

var dqselec_left="";//当前左侧鼠标选择的文本
var dqselec_right="";//当前左侧鼠标选择的文本


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



    //字典定位问答
    /* if (classc=="btalk") {
         var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]');
         var old= $html.attr("w_starttime");
         var h=$html.html();
         $html.append(copy_text_html);
         if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
             $html.attr("w_starttime",starttime);//直接使用最后追加的时间点
         }
     }else if(classc=="atalk"){
        var $html= $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]');
         var old= $html.attr("q_starttime");
         var h=$html.html();
         $html.append(copy_text_html);
         if (!isNotEmpty(old)||!isNotEmpty(h)) {
             $html.attr("q_starttime",starttime);//直接使用最后追加的时间点
         }
     }*/


    //当前下标问答
    /* $("#recorddetail label").each(function(){
         var lastindex=$(this).closest("tr").index();
         var value=$(this).attr("name");
         //当前下标问答
         if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
             $(this).append(copy_text_html);
         }
    });*/
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
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
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
                        layer.closeAll("tips");
                        }else {
                        layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
                    }

                }

                //询问人和被询问人信息
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recordUserInfos=recordUserInfosdata;
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

                getTdAndUserAndOtherCacheParamByMTssid(recordUserInfosdata.userssid);
            }
            //案件信息
            var caseAndUserInfo=data.caseAndUserInfo;
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
                var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>被询(讯)问人</td><td>"+username+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>";
                $("#caseAndUserInfo_html").html(init_casehtml);
            }
        }
    }else{
        layer.msg(data.message);
    }
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
    if (isNotEmpty(recordUserInfos)){
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
        $("#LiveState").text("加载中");
        $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
        $("#PolygraphState").text("加载中");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});


        var tdList=[];
        var user1={
            username:recordUserInfos.username
            ,userssid:recordUserInfos.userssid
            ,grade:2 //1主麦，2副麦，有时需要一些特殊的处理(主麦只有一个)
        };
        var user2={
            username:recordUserInfos.adminname
            ,userssid:recordUserInfos.adminssid
            ,grade:1
        };

        tdList.push(user1);
        tdList.push(user2);

        var url=getUrl_manage().startRercord;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                meetingtype: 2       //会议类型，1视频/2音频
                ,tdList:tdList
                ,recordssid:recordssid
            }
        };
        ajaxSubmitByJson(url, data, callbackstartMC);
    }else {
        layer.close(startMC_index);
        layer.msg("请稍等",{time:1000},function () {
            getRecordById();
            $("#record_img img").css("display","none");
            $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
            layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
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
            var polygraphnum=data.polygraphnum==null?0:data.polygraphnum;
            var recordnum=data.recordnum==null?0:data.recordnum;
            var asrnum=data.asrnum==null?0:data.asrnum;

            var mtssiddata=data.mtssid;
             useretlist=data.useretlist;

             var mcuserssid2=null;
            if (isNotEmpty(useretlist)){
                for (var i = 0; i < useretlist.length; i++) {
                    var useret = useretlist[i];
                    var userssid1=useret.userssid;
                    for (var j = 0; j < recorduser.length; j++) {
                        var u = recorduser[j];
                        var userssid2=u.userssid;
                        if (userssid1==userssid2) {
                            var grade=u.grade;
                            if (1==grade){
                                liveurl=useret.livingurl;
                                console.log("liveurl_____"+liveurl+"_______"+grade);
                                break;
                            }else if (grade==2){
                                mcuserssid2=userssid1;
                            }
                        }
                    }
                }
            }
            mtssid=mtssiddata;
            mcbool=1;//正常开启
            getTdAndUserAndOtherCacheParamByMTssid(mcuserssid2);
          /*  updateArraignment();*/
            var con="笔录已开启：<br>语音识别开启数："+asrnum+"<br>测谎仪开启数："+polygraphnum+"<br>设备录音数："+recordnum;
            layer.msg(con, {time: 2000});
        }
    }else{
        if (null!=data.data&&data.data==-1){
            $("#record_img img").css("display","none");
            $("#endrecord").css("display","block");
            $("#pauserecord").attr("onclick","");
        }else {
            $("#record_img img").css("display","none");
            $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
            layer.tips('点击将开启场景模板对应的设备，进行笔录制作' ,'#pauserecord',{time:0, tips: 2});
        }

        $("#MtState").text("未启动");
        $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
        $("#AsrState").text("未启动");
        $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
        $("#LiveState").text("未启动");
        $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
        $("#PolygraphState").text("未启动");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});

        layer.msg("笔录开启失败");
    }
}

//结束会议
function overMC() {
    if (isNotEmpty(recordssid)){
        var url=getUrl_manage().overRercord;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
            }
        };
        /*$.ajax({
            url : url,
            type : "POST",
            async : false,
            dataType : "json",
            contentType: "application/json",
            data : JSON.stringify(data),
            timeout : 60000,
            success : callbackoverMC,
            error : function () {
                parent.layer.msg("网络异常,请稍后重试---!", {
                    icon : 1
                },1);
            }
        });*/
        ajaxSubmitByJson(url, data, callbackoverMC);
    }
}
function callbackoverMC(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("overMC(返回结果_"+data);
            if (data){
                mtssid=null;//会议ssid
            }
        }
    }else{
       /* layer.msg(data.message);*/
    }
}

//保存按钮
//recordbool 1进行中 2已结束
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
                recordToProblems:recordToProblems
            }
        };
        ajaxSubmitByJson(url, data, calladdRecord);
      /*  $.ajax({
            url : url,
            type : "POST",
            async : false,
            dataType : "json",
            contentType: "application/json",
            data : JSON.stringify(data),
            timeout : 60000,
            success : calladdRecord,
            error : function () {
                parent.layer.msg("网络异常,请稍后重试---!", {
                    icon : 1
                },1);
            }
        });*/
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
                                type: 2,
                                title: '导出PDF笔录',
                                shadeClose: true,
                                maxmin: true, //开启最大化最小化按钮
                                area: ['893px', '600px'],
                                content: data
                            });
                            layer.msg("导出成功,等待下载中...");
                        }
                    }else{
                        layer.msg(data.message);
                    }
                });
            } else {
                layer.msg('保存成功', {
                    btn: ['去查看', '继续编辑'],
                    yes:function(index){
                        parent.document.getElementById("record_select").click();
                        layer.close(index);
                    }, btn2:function(index){
                        layer.close(index);
                        return false;
                    }});
            }
        }
    }else{
        layer.msg(data.message);
    }
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
        if (recordbool==2&&mtssid!=null) {
            overMC();//结束会议
        }
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
 * 获取会议实时数据
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
                liveurl=fdCacheParam.livingUrl;
            }
        }
        if (isNotEmpty(list)) {
            layer.close(loadindex);
            $("#recordreals").html("");
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

//获取
var  fdrecord=null;//是否需要录像，1使用，-1 不使用
var  usepolygraph=null;//是否使用测谎仪，1使用，-1 不使用
var  useasr=null;//是否使用语言识别，1使用，-1 不使用
var  asrRun=null;//语音识别服务是否启动 1开启 -1不开起
//暂时没有区分副麦主麦，后期需要修改
function getTdAndUserAndOtherCacheParamByMTssid(userssid) {
    if (isNotEmpty(mtssid)&&(isNotEmpty(mcbool)&&mcbool==1)) {//会议正常的时候
        var url=getUrl_manage().getTdAndUserAndOtherCacheParamByMTssid;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid,
                userssid:userssid,
            }
        };

        ajaxSubmitByJson(url, data, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                      fdrecordstarttime=data.fdrecordstarttime==null?0:data.fdrecordstarttime;
                      fdrecord=data.fdrecord==null?-1:data.fdrecord;//是否需要录像，1使用，-1 不使用
                      usepolygraph=data.usepolygraph==null?-1:data.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
                      useasr=data.useasr==null?-1:data.useasr;//是否使用语言识别，1使用，-1 不使用

                    //第一行上时间
                    var lable=  $('#first_originaltr label[name="q"]');
                    setFocus(lable);


                    if (null!=data.asrRun&&data.asrRun) {
                        asrRun=1;//语音识别服务是否启动 1开启 -1不开起
                    }else {
                        asrRun=-1;//语音识别服务是否启动 1开启 -1不开起
                    }
                }
            }
        });
    }
}




/**
 * 视频地址切换 type 1主麦 type 2副麦
 */

function select_liveurl(obj,type){
    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    if (isNotEmpty(useretlist)){
        for (var i = 0; i < useretlist.length; i++) {
            var useret = useretlist[i];
            var userssid1=useret.userssid;
            for (var j = 0; j < recorduser.length; j++) {
                var u = recorduser[j];
                var userssid2=u.userssid;
                if (userssid1==userssid2) {
                    var grade=u.grade;
                    if (type==grade){
                        liveurl=useret.livingurl;
                        console.log("liveurl_____"+liveurl+"______"+grade)
                        break;
                    }
                }
            }
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
        console.log("直播的开始时间："+fdrecordstarttime+";是否开启语音识别："+useasr)
        if (isNotEmpty(useasr)&&useasr==-1&&isNotEmpty(mtssid)){
            console.log("语音识别未开启~")
            var dqtime=new Date().getTime();
            var qw_type=el.getAttribute("name");
            if (isNotEmpty(qw_type)){
                if (qw_type=="w"){
                    var w_starttime=el.getAttribute("w_starttime");
                    if ((!isNotEmpty(w_starttime)||w_starttime<0)&&(isNotEmpty(fdrecordstarttime)&&fdrecordstarttime>0)){
                        //计算时间戳
                        w_starttime=Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime))==null?0:Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime));
                        el.setAttribute("w_starttime",w_starttime);
                    }
                }else  if (qw_type=="q"){
                    var q_starttime=el.getAttribute("q_starttime");
                    if ((!isNotEmpty(q_starttime)||q_starttime<0)&&(isNotEmpty(fdrecordstarttime)&&fdrecordstarttime>0)){
                        //计算时间戳
                        q_starttime=Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime))==null?0:Math.abs(parseInt(dqtime)-parseInt(fdrecordstarttime));
                        el.setAttribute("q_starttime",q_starttime);
                    }
                }
            }
        }else {
            console.log("语音识别非未开启状态~")
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
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"   q_starttime=""  >'+datadata["q"]+'</label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="" w_starttime="" >'+datadata["w"]+'</label></div>\
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



//伸缩按钮
function shrink(obj) {
    var shrink_bool=$(obj).attr("shrink_bool");
    if (shrink_bool==1){
        $("#shrink_html").hide();
        $(obj).attr("shrink_bool","-1");
        $("i",obj).attr("class","layui-icon layui-icon-spread-left");
        $("#notshrink_html").attr("class","layui-col-md12");

        $("#layui-layer"+recordstate_index).hide();

    }else{
        $("#shrink_html").show();
        $(obj).attr("shrink_bool","1");
        $("#notshrink_html").attr("class","layui-col-md9");
        $("i",obj).attr("class","layui-icon layui-icon-shrink-right");
        $("#layui-layer"+recordstate_index).show();
    }
}


//*******************************************************************图表区域start****************************************************************//
/**
 * 监测数据
 */
function getPolygraphdata() {
    if (isNotEmpty(mtssid)) {
        var url=getUrl_manage().getPolygraphdata;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetPolygraphdata);
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
                myChart.hideLoading();
                var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                var br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                var  status=obj.status;
                var status_text="未知";
                if (status==0){
                    status_text="正常";
                }else if (status==1){
                    status_text="紧张";
                }else if (status==2){
                    status_text="生理疲劳";
                }else if (status==3){
                    status_text="昏昏欲睡";
                }
                var relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                var bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                var spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                var hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);

                $("#xthtml #xt1").html(' '+status_text+'   ');
                $("#xthtml #xt2").html(' '+relax+'  ');
                $("#xthtml #xt3").html(' '+stress+'  ');
                $("#xthtml #xt4").html(' '+bp+'  ');
                $("#xthtml #xt5").html(' '+spo2+'  ');
                $("#xthtml #xt6").html(' '+hr+'  ');
                $("#xthtml #xt7").html(' '+hrv+'  ');
                $("#xthtml #xt8").html(' '+br+'  ');



                if (isNotEmpty(select_monitorall_iframe_body)) {
                    select_monitorall_iframe_body.find("#monitorall #xt1").html(' '+status_text+'   ');
                    select_monitorall_iframe_body.find("#monitorall #xt2").html(' '+relax+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt3").html(' '+stress+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt4").html(' '+bp+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt5").html(' '+spo2+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt6").html(' '+hr+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt7").html(' '+hrv+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt8").html(' '+br+'  ');
                }

                var hr_snr=obj.hr_snr.toFixed(1)==null?0:obj.hr_snr.toFixed(1);
                var fps=obj.fps.toFixed(1)==null?0:obj.fps.toFixed(1);
                var stress_snr=obj.stress_snr.toFixed(1)==null?0:obj.stress_snr.toFixed(1);
                var snrtext="fps：0&nbsp;hr_snr：0&nbsp;stress_snr：0";
                snrtext="fps："+fps+"&nbsp;hr_snr："+hr_snr+"&nbsp;stress_snr："+stress_snr+"";


                $("#snrtext").html(snrtext);
                $("#snrtext3").html(snrtext);

                var tsmsg="身心监测加载中...";
                var tscss={"color":" #31708f","background-color": "#d9edf7"," border-color": "#bce8f1"};
                if (isNotEmpty(hr_snr)&&hr_snr>=0.1){
                    tsmsg="身心准确监测中";
                    tscss={"color": "#3c763d","background-color":"#dff0d8","border-color":"#d6e9c6"};
                }else{
                    tsmsg="监测准确度较低，请调整光线，减少身体晃动";
                    tscss={"color": "#a94442","background-color":"#f2dede","border-color":"#ebccd1"};
                }

                $("#showmsg,#open_showmsg").css(tscss);
                $("#showmsg strong,#open_showmsg strong").text(tsmsg);
                if (isNotEmpty(select_monitorall_iframe_body)) {
                    select_monitorall_iframe_body.find("#open_showmsg").css(tscss);
                    select_monitorall_iframe_body.find("#open_showmsg strong").text(tsmsg);
                    select_monitorall_iframe_body.find("#snrtext2").html(snrtext);
                }

                addData_hr(true,hr);
                addData_hrv(true,hrv);
                addData_br(true,br);
                addData_relax(true,relax);
                addData_stress(true,stress);
                addData_bp(true,bp);
                addData_spo2(true,spo2);


                $("#monitor_btn span").each(function (e) {
                    var type=$(this).attr("type");
                    var name=$(this).text();
                    var isn=$(this).attr("isn");
                    if (isn==1){
                        if (type=="hr") {
                            date1=date_hr;
                            data1=data_hr;
                        }else if (type=="hrv") {
                            date1=date_hrv;
                            data1=data_hrv;
                        }else if (type=="br") {
                            date1=date_br;
                            data1=data_br;
                        }else if (type=="relax") {
                            date1=date_relax;
                            data1=data_relax;
                        }else if (type=="stress") {
                            date1=date_stress;
                            data1=data_stress;
                        }else if (type=="bp") {
                            date1=date_bp;
                            data1=data_bp;
                        }else if (type=="spo2") {
                            date1=date_spo2;
                            data1=data_spo2;
                        }
                    }
                });
                myChart.setOption({
                    xAxis: {
                        data: date1
                    },
                    series: [{
                        data: data1
                    }]
                });

                if (null!=select_monitorall_iframe){
                    select_monitorall_iframe.myMonitorall.setOption({
                        xAxis: {
                            data: date_hr
                        },
                        series: [{
                            data: data_hr
                        }]
                    });
                    select_monitorall_iframe.myMonitorall2.setOption({
                        xAxis: {
                            data: date_hrv
                        },
                        series: [{
                            data: data_hrv
                        }]
                    });
                    select_monitorall_iframe.myMonitorall3.setOption({
                        xAxis: {
                            data: date_br
                        },
                        series: [{
                            data: data_br
                        }]
                    });
                    select_monitorall_iframe.myMonitorall4.setOption({
                        xAxis: {
                            data: date_relax
                        },
                        series: [{
                            data: data_relax
                        }]
                    });
                    select_monitorall_iframe.myMonitorall5.setOption({
                        xAxis: {
                            data: date_stress
                        },
                        series: [{
                            data: data_stress
                        }]
                    });
                    select_monitorall_iframe.myMonitorall6.setOption({
                        xAxis: {
                            data: date_bp
                        },
                        series: [{
                            data: data_bp
                        }]
                    });
                    select_monitorall_iframe.myMonitorall7.setOption({
                        xAxis: {
                            data: date_spo2
                        },
                        series: [{
                            data: data_spo2
                        }]
                    });
                }
            }
        }
    }else{
       /* layer.msg(data.message);*///不需要弹出错误信息
    }
}



/**
 * 身心监测按钮组
 */
function select_monitor(obj) {
    $(obj).attr("isn","1");
    $(obj).siblings().attr("isn","-1");

    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    myChart.showLoading();
    var name=$(obj).text();
    myChart.setOption({
        title: {
            text: name,
        },
        series: [{
            name:name,
        }]
    });
}


//显示全部图表

var option = {
    tooltip: {
        trigger: 'axis',
        formatter: '{a}: {c}'
    },
    xAxis: {
        type: 'category',
        splitLine: {
            show: false
        },
        show: false,
    },
    yAxis: {
        type: 'value',
        boundaryGap: [0, '100%'],
        splitLine: {
            show: false
        },
        show: true
    },
    grid: {
        x:30,
        y:45,
        x2:30,
        y2:10,
    },
    series: [{
        type: 'line',
        showSymbol: false,
        hoverAnimation: false,
        itemStyle : {
            normal : {
                color:'#00FF00',
                lineStyle:{
                    color:'#00FF00'
                }
            }
        },
    }]
};


var select_monitorall_iframe=null;
var select_monitorall_iframe_body=null;

function select_monitorall(obj) {
    layer.open({
         type: 2
        , skin: 'layui-layer-lan' //样式类名
        ,title: "身心检测"
        ,area: ['40%','98%']
        ,shade: 0
        ,id: 'layer_monitorall' //设定一个id，防止重复弹出
        ,offset: 'l'
        ,resize: true
        ,content: togetPolygraphurl
        ,success:function (layero,index) {
            select_monitorall_iframe = window['layui-layer-iframe' + index];
            select_monitorall_iframe_body=layer.getChildFrame('body', index);
            select_monitorall_iframe.monitorall1(option);
            select_monitorall_iframe.myMonitorall.setOption({
                title: {
                    text: "心率",
                },
                xAxis: {
                    data: date_br
                },
                series: [{
                    name:"心率",
                    data: data_br
                }]
            });
            select_monitorall_iframe.monitorall2(option);
            select_monitorall_iframe.myMonitorall2.setOption({
                title: {
                    text: "心率变异",
                },
                xAxis: {
                    data: date_hrv
                },
                series: [{
                    name:"心率变异",
                    data: data_hrv
                }]
            });
            select_monitorall_iframe.monitorall3(option);
            select_monitorall_iframe.myMonitorall3.setOption({
                title: {
                    text: "呼吸次数",
                },
                xAxis: {
                    data: date_br
                },
                series: [{
                    name:"呼吸次数",
                    data: data_br
                }]
            });
            select_monitorall_iframe.monitorall4(option);
            select_monitorall_iframe.myMonitorall4.setOption({
                title: {
                    text: "放松值",
                },
                xAxis: {
                    data: date_relax
                },
                series: [{
                    name:"放松值",
                    data: data_relax
                }]
            });
            select_monitorall_iframe.monitorall5(option);
            select_monitorall_iframe.myMonitorall5.setOption({
                title: {
                    text: "紧张值",
                },
                xAxis: {
                    data: date_stress
                },
                series: [{
                    name:"紧张值",
                    data: data_stress
                }]
            });
            select_monitorall_iframe.monitorall6(option);
            select_monitorall_iframe.myMonitorall6.setOption({
                title: {
                    text: "血压变化",
                },
                xAxis: {
                    data: date_bp
                },
                series: [{
                    name:"血压变化",
                    data: data_bp
                }]
            });
            select_monitorall_iframe.monitorall7(option);
            select_monitorall_iframe.myMonitorall7.setOption({
                title: {
                    text: "血氧",
                },
                xAxis: {
                    data: date_spo2
                },
                series: [{
                    name:"血氧",
                    data: data_spo2
                }]
            });
        },
        cancel: function(index, layero){
             select_monitorall_iframe=null;
             select_monitorall_iframe_body=null;
                layer.close(index)
        }
    });
}

var myChart;
var date1 = [];
var data1 = [];

var init_br = 1;
var date_br = [];
var data_br = [];
function addData_br(shift,data) {
    init_br++;
    date_br.push(init_br);
    data_br.push(data);
    if (shift) {
        date_br.shift();
        data_br.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_br(false,0);
}

var init_bp = 1;
var date_bp = [];
var data_bp = [];
function addData_bp(shift,data) {
    init_bp++;
    date_bp.push(init_bp);
    data_bp.push(data);
    if (shift) {
        date_bp.shift();
        data_bp.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_bp(false,0);
}

var init_hr = 1;
var date_hr = [];
var data_hr = [];
function addData_hr(shift,data) {
    init_hr++;
    date_hr.push(init_hr);
    data_hr.push(data);
    if (shift) {
        date_hr.shift();
        data_hr.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hr(false,0);
}

date1=date_hr;
data1=data_hr;
var init_hrv = 1;
var date_hrv = [];
var data_hrv = [];
function addData_hrv(shift,data) {
    init_hrv++;
    date_hrv.push(init_hrv);
    data_hrv.push(data);
    if (shift) {
        date_hrv.shift();
        data_hrv.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hrv(false,0);
}

var init_relax = 1;
var date_relax = [];
var data_relax = [];
function addData_relax(shift,data) {
    init_relax++;
    date_relax.push(init_relax);
    data_relax.push(data);
    if (shift) {
        date_relax.shift();
        data_relax.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_relax(false,0);
}

var init_spo2 = 1;
var date_spo2 = [];
var data_spo2 = [];
function addData_spo2(shift,data) {
    init_spo2++;
    date_spo2.push(init_spo2);
    data_spo2.push(data);
    if (shift) {
        date_spo2.shift();
        data_spo2.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_spo2(false,0);
}

var init_stress = 1;
var date_stress = [];
var data_stress = [];
function addData_stress(shift,data) {
    init_stress++;
    date_stress.push(init_stress);
    data_stress.push(data);
    if (shift) {
        date_stress.shift();
        data_stress.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_stress(false,0);
}

function main1() {

    $("#main1").css( 'width',$("#leftdiv").width());
    $(window).resize(function() {
        myChart.resize();
    });

    myChart = echarts.init(document.getElementById('main1'),'dark');
    var option = {
        title: {
            text: '心率',
        },
        tooltip: {
            trigger: 'axis',
            formatter: '{a}: {c}'
        },
        xAxis: {
            type: 'category',
            splitLine: {
                show: false
            },
            show: false,
            data: date1
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            },
            show: true
        },
        grid: {
            x:30,
            y:45,
            x2:30,
            y2:10,
        },
        series: [{
            name: '心率',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            itemStyle : {
                normal : {
                    color:'#00FF00',
                    lineStyle:{
                        color:'#00FF00'
                    }
                }
            },
            data: data1,
            markLine: {//警戒线标识
                symbol:"none",               //去掉警戒线最后面的箭头
                silent: true,
                lineStyle: {
                    normal: {
                        color: 'red'                   // 这儿设置安全基线颜色
                    }
                },
                data: [{
                    yAxis: 0.75//安全值
                }],
                label: {
                    normal: {
                        formatter: '心率安全值'           // 这儿设置安全基线
                    }
                },
            }
        }]
    };
    myChart.setOption(option);
}
//*******************************************************************图表区域end****************************************************************//

//*******************************************************************点击start****************************************************************//
//身心检测
function initheart() {
    $("#initheart_click").removeClass("layui-show");
    $(".layui-tab-content").css("height","650px");

    if (isNotEmpty($("#living3_2").html())) {
        $("#living3_1").html($("#living3_2").html());
        $("#living3_2").html("");
        if (isNotEmpty(useretlist)){
            for (var i = 0; i < useretlist.length; i++) {
                var useret = useretlist[i];
                var userssid1=useret.userssid;
                for (var j = 0; j < recorduser.length; j++) {
                    var u = recorduser[j];
                    var userssid2=u.userssid;
                    if (userssid1==userssid2) {
                        var grade=u.grade;
                        if (2==grade){//只播放被询问人的
                            liveurl=useret.livingurl;
                            console.log("liveurled_____"+liveurl+"_______"+grade)
                            break;
                        }
                    }
                }
            }
        }
        // initplayer();
    }

    layui.use(['element'], function(){
        var element = layui.element;
        //使用模块
        element.render();
    });
    // initplayer();
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
    //initplayer();
}
//*******************************************************************点击start****************************************************************//

//重置模板
function clearRecord() {
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
}



//*******************************************************************告知书start****************************************************************//
var notificationListdata=null;
function getNotifications() {
    var url=getActionURL(getactionid_manage().waitRecord_getNotifications);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            currPage:1,
            pageSize:100
        }
    };
    ajaxSubmitByJson(url, data, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var pagelist=data.pagelist;
                $("#notificationList").html("");
                if (isNotEmpty(pagelist)){
                    notificationListdata=pagelist;
                    for (var i = 0; i < pagelist.length; i++) {
                        var l = pagelist[i];
                        var l_html="<tr>\
                                      <td>"+l.notificationname+"</td>\
                                      <td style='padding-bottom: 0;'>\
                                          <div class='layui-btn-container'>\
                                          <button  class='layui-btn layui-btn-danger' onclick='previewgetNotifications(\""+l.ssid+"\");'>打开</button>\
                                          <button  class='layui-btn layui-btn-normal' onclick='downloadNotification(\""+l.ssid+"\")'>直接下载</button>\
                                          </div>\
                                          </td>\
                                 </tr>";
                        $("#notificationList").append(l_html);
                    }


                    layui.use(['layer','element','upload'], function(){
                        var layer = layui.layer; //获得layer模块
                        var element = layui.element;
                        var upload = layui.upload;
                        //使用模块

                        var url=getActionURL(getactionid_manage().waitRecord_uploadNotification);

                        //执行实例
                        var uploadInst = upload.render({
                            elem: "#uploadFile" //绑定元素
                            ,url: url //上传接口
                            , acceptMime: '.doc, .docx' //只允许上传图片文件
                            ,exts: 'doc|docx' //只允许上传压缩文件
                            ,before: function(obj){
                            }
                            ,done: function(res){
                                if("SUCCESS" == res.actioncode){
                                    layer.msg(res.message,{time:500},function () {
                                        getNotifications();
                                    });
                                }
                            }
                            ,error: function(res){
                                console.log("请求异常回调");
                            }
                        });
                    });
                }
            }
        }else{
            layer.msg(data.message);
        }
    })
}

//获取告知书列表
function open_getNotifications() {
    var html= '<table class="layui-table"  lay-skin="nob">\
        <colgroup>\
        <col>\
        <col  width="200">\
        </colgroup>\
        <tbody id="notificationList">\
        </tbody>\
        <thead>\
            <tr>\
            <td></td><td style="float: right"><button  class="layui-btn layui-btn-warm" id="uploadFile">上传告知书</button></td>\
            </tr>\
         </thead>\
        </table>';
    var index = layer.open({
        type:1,
        title:'选择告知书',
        content:html,
        shadeClose:false,
        shade:0,
        area: ['893px', '600px']
    });
    getNotifications();
}

//下载告知书
function downloadNotification(ssid) {
    var url=getActionURL(getactionid_manage().waitRecord_downloadNotification);
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url, data, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var base_filesave=data.base_filesave;
                if (isNotEmpty(base_filesave)) {
                    var recorddownurl=base_filesave.recorddownurl;
                    layer.msg("下载中，请稍后...");
                    window.location.href=recorddownurl;
                }
            }
        }else{
            layer.msg(data.message);
        }
    });
}

//打开告知书
var previewgetNotifications_index=null;
var dqrecorddownurl_htmlread=null;//读取阅读txt

var t1=null;
var len=0;
function previewgetNotifications(ssid) {

    if (isNotEmpty(previewgetNotifications_index)) {
        layer.close(previewgetNotifications_index);
        clearInterval(t1);
        if (isNotEmpty(audioplay)){
            audioplay.pause();
        }
        audioplay=null;
        len=0;
    }

    var url=getActionURL(getactionid_manage().waitRecord_downloadNotification);
    var setdata = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };


    ajaxSubmitByJson(url, setdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var recorddownurl_html=data.recorddownurl_html;

                var base_filesave=data.base_filesave;


                if (isNotEmpty(recorddownurl_html)) {
                    previewgetNotifications_index = layer.open({
                        type:2,
                        title:'阅读告知书',
                        content:recorddownurl_html,
                        shadeClose:false,
                        maxmin: true,
                        shade:0,
                        area: ['50%', '700px']
                        ,btn: ['开始朗读', '取消'],
                        id:"notification_read"
                        ,yes: function(index, layero){
                            var dis=$("#layui-layer"+previewgetNotifications_index).find(".layui-layer-btn0").attr('disabled');
                            if (isNotEmpty(dis)){
                                layer.msg("朗读中")
                                return;
                            }

                          if (!isNotEmpty(gnlist)||!gnlist.includes("tts")){
                                layer.msg("请先获取语音播报授权")
                                return;
                            }
                            layer.msg("加载中，请稍等...", {
                                icon: 16,
                                time:1000
                            });

                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;


                            //点击了
                            dqrecorddownurl_htmlread=data.recorddownurl_htmlread;
                            if (isNotEmpty(dqrecorddownurl_htmlread)){




                                var arr=new Array();
                                arr=dqrecorddownurl_htmlread.split(/[，|,]/);//注split可以用字符或字符串分割
                                for (var i = 0; i < arr.length; i++) {
                                    if(!isNotEmpty(arr[i])){
                                        arr.splice(i,1);
                                        i = i-1;
                                    }
                                }
                                t1 = window.setInterval(function (args) {
                                    var text=arr[len];
                                    if (audioplay==null){
                                        str2Tts(text);
                                        len++;
                                    } else if (audioplay.ended) {
                                        str2Tts(text);
                                        len++;
                                    }
                                    if (len>arr.length-1){
                                        clearInterval(t1);
                                    }
                                },500);
                            }else {
                                layer.msg("未找到需要朗读的文本");
                            }
                        }
                        ,btn2: function(index, layero){
                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;
                            layer.close(index)
                        }
                        ,cancel: function(index, layero){
                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;
                            layer.close(index)
                        }
                    });
                }else {
                    layer.msg("文件未找到，可尝试下载预览");
                }
            }
        }else{
            layer.msg(data.message);
        }
    });

}
//*******************************************************************告知书start****************************************************************//


//获取各个状态
function  getEquipmentsState() {
    $("#MtState").text("加载中");
    $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
    $("#AsrState").text("加载中");
    $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
    $("#LiveState").text("加载中");
    $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
    $("#PolygraphState").text("加载中");
    $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});

    if (isNotEmpty(mtssid)){
        var url=getUrl_manage().getEquipmentsState;
        var data = {
            token: INIT_CLIENTKEY,
            param: {
                mtssid: mtssid,
                fdrecord:fdrecord,
                usepolygraph:usepolygraph,
                useasr:useasr,
                asrRun:asrRun
            }
        };

        ajaxSubmitByJson(url, data, callbackgetEquipmentsState);
    }else{
        console.log("设备状态信息位置未找到__"+mtssid);
    }
}

function callbackgetEquipmentsState(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        var data = data.data;
        if (isNotEmpty(data)) {
            //状态： -1异常  0未启动 1正常
            var MtText = "加载中";
            var AsrText = "加载中";
            var LiveText = "加载中";
            var PolygraphText = "加载中";
            var MtClass = "ayui-badge layui-bg-gray";
            var AsrClass = "ayui-badge layui-bg-gray";
            var LiveClass = "ayui-badge layui-bg-gray";
            var PolygraphClass = "ayui-badge layui-bg-gray";

            var MtState = data.mtState;
            if (MtState == 0) {
                MtText = "未启动";
                MtClass = "ayui-badge layui-bg-gray";
            } else if (MtState == 1) {
                MtText = "正常";
                MtClass = "layui-badge layui-bg-green";
            } else if (MtState == -1) {
                MtText = "异常";
                MtClass = "layui-badge";
            }
            var AsrState = data.asrState;
            if (AsrState == 0) {
                AsrText = "未启动";
                AsrClass = "ayui-badge layui-bg-gray";
            } else if (AsrState == 1) {
                AsrText = "正常";
                AsrClass = "layui-badge layui-bg-green";
            } else if (AsrState == -1) {
                AsrText = "异常";
                AsrClass = "layui-badge";
            }
            var LiveState = data.liveState;
            if (LiveState == 0) {
                LiveText = "未启动";
                LiveClass = "ayui-badge layui-bg-gray";
            } else if (LiveState == 1) {
                LiveText = "正常";
                LiveClass = "layui-badge layui-bg-green";
            } else if (LiveState == -1) {
                LiveText = "异常";
                LiveClass = "layui-badge";
            }
            var PolygraphState = data.polygraphState;
            if (PolygraphState == 0) {
                PolygraphText = "未启动";
                PolygraphClass = "ayui-badge layui-bg-gray";
            } else if (PolygraphState == 1) {
                PolygraphText = "正常";
                PolygraphClass = "layui-badge layui-bg-green";
            } else if (PolygraphState == -1) {
                PolygraphText = "异常";
                PolygraphClass = "layui-badge";
            }


            $("#MtState").text(MtText);
            $("#MtState").attr({"MtState": MtState, "class": MtClass});
            $("#AsrState").text(AsrText);
            $("#AsrState").attr({"AsrState": AsrState, "class": AsrClass});
            $("#LiveState").text(LiveText);
            $("#LiveState").attr({"LiveState": LiveState, "class": LiveClass});
            $("#PolygraphState").text(PolygraphText);
            $("#PolygraphState").attr({"PolygraphState": PolygraphState, "class": PolygraphClass});

        }
    }
}


/**
 * 获取各个客户端的状态
 */
function getClient() {
    var url=getUrl_manage().getClient;
    var data={
        token:INIT_CLIENTKEY,
         param:{
          
        }
    };
    /*ajaxSubmitByJson(url, data, callbackgetClient);*/
}
function callbackgetClient(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        var data=data.data;
        if (isNotEmpty(data)){
            console.log(data)
        }
    }
}

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

//lable focus 1当前光标加一行 2尾部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    var qwfocus=null;

    if (null!=td_lastindex["key"]&&type==1){
        $('#recorddetail tr:eq("'+td_lastindex["key"]+'")').after(html);
        if (isNotEmpty(qw)){
            qwfocus= $('#recorddetail tr:eq("'+(td_lastindex["key"]+1)+'") label[name="'+qw+'"]');
            td_lastindex["key"]=td_lastindex["key"]+1;
        }
    } else{
            $("#recorddetail").append(html);

        $("#recorddetail").hover(
            function(){
                mouseoverbool_right=1
                console.log("鼠标移入")
            } ,
            function(){
                mouseoverbool_right=-1;
                console.log("鼠标移出")
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
            console.log(data);
        }
    }else{
        layer.msg(data.message);
    }
}

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
                for (var z = 0; z< problems.length;z++) {
                    var problem = problems[z];
                    var problemtext=problem.problem==null?"未知":problem.problem;
                    var problemhtml= '<tr>\
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
                    focuslable(problemhtml,1,'w');
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
}


function str2Tts(text) {
    if (isNotEmpty(text)){
        var url=getUrl_manage().str2Tts;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                text:text
            }
        };
        ajaxSubmitByJson(url, data, callbackstr2Tts);
    }
}

var audioplay=null;
function callbackstr2Tts(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var uploadpath=data.uploadpath;
            if (isNotEmpty(uploadpath)){
                $("#layui-layer"+previewgetNotifications_index).find(".layui-layer-btn0").text("朗读中").attr('disabled',true);
                audioplay =new Audio();
                audioplay.src = uploadpath;
                audioplay.play();
            }
        }
    }else {
        layer.msg(data.message);
    }
}


function getgnlist() {
    var url=getActionURL(getactionid_manage().waitRecord_gnlist);
    var data={
        token:INIT_CLIENTKEY,
        param:{
         
        }
    };
    ajaxSubmitByJson(url, data, callbackgnlist);
}

var gnlist=null;
function callbackgnlist(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var lists=data.lists;
            if (isNotEmpty(lists)){
                gnlist=lists;
                if (!isNotEmpty(gnlist)||!gnlist.includes("record")){
                    layer.msg("请先获取笔录授权",{time:2000,icon:16,shade: 0.3},function () {
                        window.history.go(-1);
                        return false;
                    })
                }


                for (let i = 0; i < gnlist.length; i++) {
                    var list = gnlist[i];
                    if (list=="asr") {
                      /*  $("#asr").show();
                        $("#initec ul li").removeClass("layui-this");
                        $("#initec .layui-tab-item").removeClass("layui-show");
                        $("#asr").addClass("layui-this");
                        $("#asritem").addClass("layui-show");*/
                    }else if (list=="fd") {
                       /*$("#fd").show();*/
                    }else if (list=="ph") {
                      /* $("#ph").show();*/
                        $("#xthtml").css("visibility","visible");
                    }
                }
            }
        }
    }else {
        layer.msg(data.message);
    }
}




/*左侧搜索块-----------------start--------------------*/
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
    mouseoverbool_left=1;//不滚动
    if (isNotEmpty(likerealtxtarr)) {
        for (let i = 0; i < likerealtxtarr.length; i++) {
            const all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        $("#recordreals_scrollhtml").animate({scrollTop:top},500);
    }
}
function recordreals_select() {
    mouseoverbool_left=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];
    $("#recordreals div").each(function (i,e) {
        var spantxt=$(this).find("span").text();
        $(this).find("span").html(spantxt);
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
/*左侧搜索块-----------------end--------------------*/

//右边问答笔录右键标记
function rightclick_right() {

}
















var datadata={};

var laststarttime_qq=-1;
var laststarttime_ww=-1;
var last_type=-1;//1问题 2是答案
var qq="";
var qqq="";
var ww="";
var www="";


//定时器关闭
var setinterval1=null;
$(function () {



    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
    $('#recorddetail label').bind('input', function() {
        setRecordreal();
    });
   //初始化第一行的焦点

    //回车搜索
   /* $("#recordreals_select").keypress(function (e) {
        if (e.which == 13) {
            event.preventDefault();
        }document
    });*/
    $(document).keypress(function (e) {
        if (e.which == 13) {
            focuslable(trtd_html,2,'q');
        }
        event.preventDefault();
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
            select_monitorall_iframe_body.find("#dqtime").html($('#Date').html() + $("#hours").html() + "：" + $("#min").html() + "：" + $("#sec").html());
        }


        if (isNotEmpty(mtssid)) {
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
            console.log("连接成功__");
        });
        socket.on('disconnect', function (data) {
            console.log("断开连接__");
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
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";


                            //实时会议数据
                            if (usertype==1){
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+' </p>\
                                                            <span onmousedown="copy_text(this,event)"  >'+translatext+'</span> \
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

                            $("#recordreals").hover(
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


                            //检测自动上墙是否开启
                            var record_switch_bool=$("#record_switch_bool").attr("isn");
                            if (record_switch_bool==1){
                                if (last_type==-1){
                                    if (usertype==1){
                                        qq+=translatext;
                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        datadata["q"]=qq;
                                        datadata["w"]=ww;
                                        setrecord_html();
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
                                    }
                                }else  if (last_type==1){//最后是问
                                    last_type=usertype;
                                    if (usertype==1){//最后是问，本次是问，判断本次问和最后一次问的时间是否一致，一致问刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_qq==starttime||laststarttime_qq==-1){
                                            qq="";//清空q
                                            qq+=translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qqq+qq);
                                        }else{
                                            var labletext= $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text();
                                            qqq=labletext;
                                            qq=qqq+translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                        }
                                    }else if (usertype==2){//最后是问，本次是答，开始拼接答案
                                        ww+=translatext;
                                        laststarttime_ww=starttime;
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(ww);
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").attr("w_starttime",starttime);
                                    }
                                }else  if (last_type==2){//最后是答
                                    last_type=usertype;
                                    if (usertype==2){//最后是答，本次确实答，判断本次答和最后一次答的时间是否一致，一致刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_ww==starttime||laststarttime_ww==-1){
                                            ww="";
                                            ww+=translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(www+ww);
                                        }else{
                                            var labletext= $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text();
                                            www=labletext;
                                            ww=www+translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(ww);
                                        }

                                    }else if (usertype==1){//最后是答，本次为问

                                        //2.初始化问答
                                        laststarttime_qq=-1;
                                        laststarttime_ww=-1;
                                        last_type=-1;//1问题 2是答案
                                        qq="";
                                        qqq="";
                                        ww="";
                                        www="";
                                        qq+=translatext;
                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        datadata["q"]=qq;
                                        datadata["w"]=ww;
                                        setrecord_html();
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").attr("q_starttime",starttime);
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





