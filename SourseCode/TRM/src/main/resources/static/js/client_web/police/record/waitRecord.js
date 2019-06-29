var templatessid=null;//模板ssid
var recordUserInfos;//询问人和被询问人数据
var td_lastindex={};//td的上一个光标位置  为0需要处理一下
var recorduser=[];//会议用户集合


var mcbool=null;//会议状态
var recordbool=null;//笔录状态

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
                if (isNotEmpty(templateToProblems)) {
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'referanswer='"+templateToProblem.referanswer+"'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);

                        var html='<tr >\
                                <td style="padding: 0;width: 90%;" class="onetd" >\
                                    <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"  onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.problem+'" q_starttime="" >'+templateToProblem.problem+'</label></div>\
                                    <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  placeholder="'+templateToProblem.referanswer+'" w_starttime=""></label></div>\
                                </td>\
                                <td style="float: right;">\
                                                                <div class="layui-btn-group">\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                                                                <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                                                                <div style="display: inline" id="btnadd"></div>\
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
        </td>\
        <td style="float: right;">\
            <div class="layui-btn-group">\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
            <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete" ></i>删除</a>\
             <div style="display: inline" id="btnadd"></div>\
            </div>\
        </td>\
        </tr>';
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
/*var touchtime = new Date().getTime();*/
function copy_text(obj,event) {
    var text=$(obj).text();
    copy_text_html=text;
    var classc=$(obj).closest("div").attr("class");
    var starttime=$(obj).closest("div").attr("starttime");


    //鼠标左击右击
/*   if( new Date().getTime() - touchtime < 250 ){
        if(3 == event.which){
            $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').append(copy_text_html);
            var old= $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').attr("w_starttime");
            if (!isNotEmpty(old)) {
                $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').attr("w_starttime",starttime);//直接使用最后追加的时间点
            }
        }else if(1 == event.which){
            $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').append(copy_text_html);
            var old= $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').attr("q_starttime");
            if (!isNotEmpty(old)) {
                $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').attr("q_starttime",starttime);//直接使用最后追加的时间点
            }
        }
    }else{
        touchtime = new Date().getTime();
    }*/



    //字典定位问答
     if (classc=="btalk") {
         $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').append(copy_text_html);
         var old= $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').attr("w_starttime");
         if (!isNotEmpty(old)) {
             $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').attr("w_starttime",starttime);//直接使用最后追加的时间点
         }
     }else if(classc=="atalk"){
         $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').append(copy_text_html);
         var old= $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').attr("q_starttime");
         if (!isNotEmpty(old)) {
             $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').attr("q_starttime",starttime);//直接使用最后追加的时间点
         }
     }


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
                    }else if (null!=mcbool&&mcbool==1){
                        //存在会议状态正常
                        $("#record_img img").css("display","none");
                        $("#startrecord").css("display","block");
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
            }
            //案件信息
            var caseAndUserInfo=data.caseAndUserInfo;
            $("#caseAndUserInfo_html").html("");
            if (isNotEmpty(caseAndUserInfo)){
                var occurrencetime_formatdata=caseAndUserInfo.occurrencetime_format;
                if (isNotEmpty(occurrencetime_formatdata)){
                    occurrencetime_format=occurrencetime_formatdata;
                }
                var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+caseAndUserInfo.casename+"</td></tr>\
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

        var url="/v1/police/out/startRercord";
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
        layer.msg("请刷新重试...");
    }
}
function callbackstartMC(data) {
    layer.close(startMC_index);
    if(null!=data&&data.actioncode=='SUCCESS'){
        $("#record_img img").css("display","none");
        $("#startrecord").css("display","block");
        var data=data.data;
        if (isNotEmpty(data)){
            var asrnum=data.asrnum;
            var mtssiddata=data.mtssid;
            var polygraphnum=data.polygraphnum;
            var recordnum=data.recordnum;
            useretlist=data.useretlist;
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
                            }
                        }
                    }
                }
            }
            mtssid=mtssiddata;
          /*  updateArraignment();*/
            layer.msg("笔录已开启");
        }
    }else{
        if (null!=data.data&&data.data==-1){
            $("#record_img img").css("display","none");
            $("#endrecord").css("display","block");
        }else {
            $("#record_img img").css("display","none");
            $("#pauserecord").css("display","block");
        }

        $("#MtState").text("未启动");
        $("#MtState").attr({"MtState": "", "class": "ayui-badge layui-bg-gray"});
        $("#AsrState").text("未启动");
        $("#AsrState").attr({"AsrState": "", "class": "ayui-badge layui-bg-gray"});
        $("#LiveState").text("未启动");
        $("#LiveState").attr({"LiveState": "", "class": "ayui-badge layui-bg-gray"});
        $("#PolygraphState").text("未启动");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "ayui-badge layui-bg-gray"});
        layer.msg(data.message);
    }
}

//结束会议
function overMC() {
    if (isNotEmpty(recordssid)){
        var url="/v1/police/out/overRercord";
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
        if (recordbool==2&&mtssid!=null) {
            overMC();//结束会议
        }
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
                layer.msg("笔录保存成功",{time:500},function () {
                    window.history.go(-1);
                })
            }else {
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
    layer.confirm('是否结束笔录?<br/><span style="font-size: 4px;color: red">(提示：请先确保笔录对应类型存在word模板<br/>否则将无法导出模板)</span>', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        if (null!=setinterval1){
            clearInterval(setinterval1);
        }
        overRecord_index=index;
        recordbool=2;
        addRecord();
        overRecord_loadindex = layer.msg("保存中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });

    }, function(index){
        layer.close(index);
    });
}


//导出word
function exportWord(obj){
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
        btn(obj);
    });
}

function exportPdf(obj) {
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
            layer.msg("导出失败");
        }
        btn(obj);
    });
}


/**
 * 获取会议实时数据
 */
function getRecordrealing() {
    if (isNotEmpty(mtssid)) {
        var url="/v1/police/out/getRecordrealing";
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


//阻止回车
function qw_keydown(obj,event) {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        var dqname=$(obj).attr("name");
        if (dqname=="q") {
           var trindex= $(obj).closest("tr").index();
            var lable=$('#recorddetail tr:eq("'+trindex+'") label[name="w"]');
            setFocus(lable);
        }else {
            focuslable(trtd_html,1,'q');
        }
        event.preventDefault();
    }
}
function setFocus(el) {
    el = el[0];
    el.focus();
    var range = document.createRange();
    range.selectNodeContents(el);
    range.collapse(false);
    var sel = window.getSelection();
    if(sel.anchorOffset!=0){
        return;
    };
    sel.removeAllRanges();
    sel.addRange(range);
};


//自动上墙
function setrecord_html() {
    $("#recorddetail tr").attr("automaticbool","");
    var trtd_html='<tr automaticbool="1">\
        <td style="padding: 0;width: 90%;" class="onetd" >\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);"   q_starttime="">'+datadata["q"]+'</label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);" placeholder="" w_starttime="">'+datadata["w"]+'</label></div>\
                </td>\
                <td style="float: right;">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                    <div style="display: inline" id="btnadd"></div></div>\
                </td>\
                </tr>';
    focuslable(trtd_html,1,'w');
}



//*******************************************************************图表区域start****************************************************************//
/**
 * 监测数据
 */
function getPolygraphdata() {
    if (isNotEmpty(mtssid)) {
        var url="/v1/police/out/getPolygraphdata";
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
/*
var myMonitorall;
var myMonitorall2;
var myMonitorall3;
var myMonitorall4;
var myMonitorall5;
var myMonitorall6;
var myMonitorall7;
*/

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
    $("#main1").css( 'width',$(".layui-tab-title").width() );
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
            data: data1
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
        initplayer();
    }

    layui.use(['element'], function(){
        var element = layui.element;
        //使用模块
        element.render();
    });
    initplayer();
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
//直播
function initliving() {
    $("#initheart_click").addClass("layui-show");
    $(".layui-tab-content").css("height","450px");
    var html=$("#living3_2").html();
   if (!isNotEmpty(html)){
        $("#living3_2").html($("#living3_1").html());
        $("#living3_1").html("");
    }
    initplayer();
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
//获取告知书列表
function open_getNotifications() {
    var html= '<table class="layui-table"  lay-skin="nob">\
        <colgroup>\
        <col>\
        <col  width="200">\
        </colgroup>\
        <tbody id="notificationList">\
        </tbody>\
        </table>';
    var index = layer.open({
        type:1,
        title:'选择告知书',
        content:html,
        shadeClose:false,
        shade:0,
        area: ['893px', '600px'],
    });
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
                  for (var i = 0; i < pagelist.length; i++) {
                      var l = pagelist[i];
                      var l_html="<tr>\
                                      <td>"+l.notificationname+"</td>\
                                      <td style='padding-bottom: 0;'>\
                                          <div class='layui-btn-container'>\
                                          <button  class='layui-btn layui-btn-danger' onclick='previewgetNotifications(\""+l.ssid+"\");'>打开</button>\
                                          <button  class='layui-btn layui-btn-disabled'>朗读</button>\
                                          <button  class='layui-btn layui-btn-normal' onclick='downloadNotification(\""+l.ssid+"\")'>直接下载</button>\
                                          </div>\
                                          </td>\
                                 </tr>";
                      $("#notificationList").append(l_html);
                  }
              }
            }
        }else{
            layer.msg(data.message);
        }
    });
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
function previewgetNotifications(ssid) {
    if (isNotEmpty(previewgetNotifications_index)) {
        layer.close(previewgetNotifications_index);
    }

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
                var recorddownurl_html=data.recorddownurl_html;
                if (isNotEmpty(recorddownurl_html)) {
                    previewgetNotifications_index = layer.open({
                        type:2,
                        title:'阅读告知书',
                        content:recorddownurl_html,
                        shadeClose:false,
                        area: ['50%', '700px'],
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
        var url="/v1/police/out/getEquipmentsState";
        var data = {
            token: INIT_CLIENTKEY,
            param: {
                mtssid: mtssid
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
    var url="/v1/police/out/getClient";
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
                </td>\
                <td style="float: right;">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   <div style="display: inline" id="btnadd"></div>\
                   </div>\
                </td>\
                </tr>';

//lable focus 1当前光标加一行 2尾部追加 qw光标文还是答
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    var qwfocus=null;
    if (null!=td_lastindex["key"]&&type==1){
            $('#recorddetail tr:eq("'+td_lastindex["key"]+'")').after(html);
             qwfocus= $('#recorddetail tr:eq("'+(td_lastindex["key"]+1)+'") label[name="'+qw+'"]');
            td_lastindex["key"]=td_lastindex["key"]+1;
        } else{
            $("#recorddetail").append(html);
            qwfocus =  $('#recorddetail tr:last label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:last').index();
     }
    setFocus(qwfocus);
    td_lastindex["value"]=qw;
    addbtn();
}
//最后一行添加按钮初始化
function addbtn() {
    var btnhtml='<button class="layui-btn layui-btn-warm  layui-btn-xs" style="margin-right: 10px;width: 99%" title="添加一行自定义问答" lay-event="del" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon">&#xe608;</i>添加</button>';
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
                    problemhtml+=' </td>\
                        <td style="float: right;">\
                            <div class="layui-btn-group">\
                            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                        <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                        <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                         <div style="display: inline" id="btnadd"></div>\
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






    $("#dl_dd dd").click(function () {
        var text=$(this).attr('lay-value');
        //文本
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(text);
            }
        });
        btn(this);
    })

    /*    var defaults = {}
            , one_second = 1000
            , one_minute = one_second * 60
            , one_hour = one_minute * 60
            , one_day = one_hour * 24
            , startDate = new Date()
            , face = document.getElementById('jishi');

        var requestAnimationFrame = (function() {
            return window.requestAnimationFrame       ||
                window.webkitRequestAnimationFrame ||
                window.mozRequestAnimationFrame    ||
                window.oRequestAnimationFrame      ||
                window.msRequestAnimationFrame     ||
                function( callback ){
                    window.setTimeout(callback, 1000 / 60);
                };
        }());

        tick();
        function tick() {

            var now = new Date()
                , elapsed = now - startDate
                , parts = [];

            parts[0] = '' + Math.floor( elapsed / one_hour );
            parts[1] = '' + Math.floor( (elapsed % one_hour) / one_minute );
            parts[2] = '' + Math.floor( ( (elapsed % one_hour) % one_minute ) / one_second );

            parts[0] = (parts[0].length == 1) ? '0' + parts[0] : parts[0];
            parts[1] = (parts[1].length == 1) ? '0' + parts[1] : parts[1];
            parts[2] = (parts[2].length == 1) ? '0' + parts[2] : parts[2];

            face.innerText = parts.join(':');

            requestAnimationFrame(tick);

        }*/
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
            getPolygraphdata();
            getEquipmentsState();
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
                            var div = document.getElementById('recordreals');
                            div.scrollTop = div.scrollHeight;


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