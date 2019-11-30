var record_index={};//笔录的上一个光标位置 key:p下标 value：类型

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


var record_pausebool=-1;//笔录是否允许暂停1允许 -1 不允许 默认不允许-1
var record_adjournbool=-1;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1

var occurrencetime_format;//案发时间

var multifunctionbool;



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
                time:10000,
                shade: [0.1,'#fff'], //不显示遮罩
            });
            console.log("开始会议")
            startMC();
        } else if (record_pausebool==1) {
            //继续会议

            startMC_index = layer.msg("再次启动中，请稍等...", {
                icon: 16,
                time:10000,
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
                time:10000,
                shade: [0.1,'#fff'], //不显示遮罩
            });
            console.log("暂停会议")
            pauseOrContinueRercord(1);
        } else {
            $("#startrecord").css("display","block");
            layui.use(['layer','element','form'], function(){
                var layer=layui.layer;
                layer.tips("庭审中~" ,'#startrecord',{time:0, tips: 2});
            });
            layer.msg("庭审中~");
        }
    }else if(type==-1) {
        $("#endrecord").css("display","block");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips("该庭审已经制作过啦~" ,'#endrecord',{time:0, tips:2});
        });
        console.log("会议已结束")
        layer.msg("该庭审已经制作过啦~");
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



    //鼠标双击事件
    if( new Date().getTime() - touchtime < 350 ){
        console.log("现在是双击事件")
        var $html=$('#recorddetail div:eq("'+record_index["key"]+'")');
      /*  var $html=$('#recorddetail div:eq("'+record_index["key"]+'")',editorhtml);*/
        var old= $html.attr("starttime");
        var h=$html.html();
        $html.append(copy_text_html);
        if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
            $html.attr("starttime",starttime);//直接使用最后追加的时间点
        }
    }else{
        console.log("现在是单击事件")
        var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
        var dqselec_left= txt.toString();
        if (3 == event.which&&isNotEmpty(dqselec_left)&&copy_text_html.indexOf(dqselec_left)>-1&&new Date().getTime() - touchtime >700){
            var $html=$('#recorddetail div:eq("'+record_index["key"]+'")');
            /*var $html=$('#recorddetail div:eq("'+record_index["key"]+'")',editorhtml);*/
            var old= $html.attr("starttime");
            var h=$html.html();
            $html.append(dqselec_left);
            dqselec_left="";
            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
            if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
                $html.attr("starttime",starttime);//直接使用最后追加的时间点
            }
        }
        touchtime = new Date().getTime();
    }
    copy_text_html="";
    return false;
}


//获取笔录信息
function getRecordById() {
    var url=getActionURL(getactionid_manage().waitCourt_getRecordById);
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
                        var html=' <iframe src="'+wordheaddownurl_html+'"  frameborder="0" class="layui-iframe"  id="wordheaddownurl" name="wordheaddownurl" style="height: 100%;width: 100%"></iframe>';
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
                        $("#fd").show();
                        $("#initec ul li").removeClass("layui-this");
                        $("#initec .layui-tab-item").removeClass("layui-show");
                        $("#case").addClass("layui-this");
                        $("#caseitem").addClass("layui-show");

                        $("#overRecord_btn").attr("src","/uimaker/images/record_stop_2.png");
                        $("#adjourn_btn").attr("src","/uimaker/images/record_zt.png");
                        $("#adjourn_btn").html("休庭");
                        $("#overRecord_btn").html("结束");

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
                        $("#adjourn_btn").html("休庭");
                        $("#overRecord_btn").html("结束");
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
                            layer.tips('该庭审已经制作过啦~' ,'#endrecord',{time:0, tips: 2});
                        });
                        $("#start_over_btn").text("庭审已结束").attr("onclick","img_bool(this,-1)");
                    }else if (null!=mcbool&&(mcbool==1||mcbool==3)){
                        if (multifunctionbool==2){
                            $("#pauserecord").attr({"src":"/uimaker/images/record7.png","onclick":"img_bool(this,1);"});
                        }else if (multifunctionbool==3) {
                            $("#pauserecord").attr({"src":"/uimaker/images/record3.png","onclick":"img_bool(this,1);"});
                        }
                        //存在会议状态正常
                        if (mcbool==1){
                            $("#startrecord").css("display","block");
                            var tips_msg="庭审中~";
                            var start_over_btn_msg="庭审中";
                            if (record_pausebool==1) {
                                tips_msg="点击我可以暂停~";
                                start_over_btn_msg="暂停庭审";
                            }
                            $("#start_over_btn").text(start_over_btn_msg).attr("onclick","img_bool(this,2)");
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips(tips_msg ,'#startrecord',{time:0, tips: 2});
                            });

                        } else if (mcbool==3&&record_pausebool==1) {
                            $("#pauserecord").css("display","block");
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                layer.tips('点击我可以再次启动制作~' ,'#pauserecord',{time:0, tips: 2});
                            });
                            $("#start_over_btn").text("继续庭审").attr("onclick","img_bool(this,1)");
                        }
                    }else {
                        $("#pauserecord").css("display","block");
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                            layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 2});
                        });
                        $("#start_over_btn").text("开始庭审").attr("onclick","img_bool(this,1)");
                    }
                }



                //询问人和被询问人信息
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recorduser=[];
                   if (gnlist.indexOf(FY_T)!= -1){
                       //法院：人员从拓展表获取
                       var usergrades=recordUserInfosdata.usergrades;
                       if (isNotEmpty(usergrades)) {
                           for (let i = 0; i < usergrades.length; i++) {
                               const other = usergrades[i];
                               var user={
                                   username:other.username
                                   ,userssid:other.userssid
                                   ,grade:other.grade
                                   ,gradename:other.gradename
                                   ,gradeintroduce:other.gradeintroduce

                               };
                               recorduser.push(user);
                           }
                       }
                   }else {
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
                    var  init_casehtml="<tr><td style='width: 40%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>庭审时间</td> <td>"+starttime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                    var usergrades=recordUserInfosdata.usergrades;
                    if (isNotEmpty(usergrades)) {
                        var newarr = [];//新数组
                        for(var i=0;i<usergrades.length;i++){
                            var bool=true;
                            for(var j=0;j<newarr.length;j++){
                                if((isNotEmpty(usergrades[i].grade)&&isNotEmpty(newarr[j].grade)&& usergrades[i].grade==newarr[j].grade)){
                                    newarr[j].username=newarr[j].username+"、"+ usergrades[i].username;
                                    bool=false;
                                }
                            };
                            if (bool){
                                newarr.push(usergrades[i]);
                            }
                        };
                        record_switchusers=newarr;
                        for (let i = 0; i < newarr.length; i++) {
                            const  other= newarr[i];
                            $("#caseAndUserInfo_html").append("<tr type='"+other.grade+"'><td>"+other.gradename+"</td><td>"+other.username+"</td> </tr>");
                            dqswitchusers.push(other.grade)
                        }

                    }
                }
                getMCCacheParamByMTssid();//获取缓存
                getTDCacheParamByMTssid();

                getRecordrealByRecordssid();
                setInterval( function() {
                    setRecordreal();//3秒实时保存
                },3000);
                setInterval( function() {
                    setRecordProtect();//5秒缓存一次
                },5000);

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
    $("#start_over_btn").text("庭审开启中").attr("onclick","");
    if (isNotEmpty(getRecordById_data)){
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});


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
                layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 2});
            });
            $("#start_over_btn").text("开始庭审").attr("onclick","img_bool(this,1)");
        });
    }
}
function callbackstartMC(data) {
    layer.close(startMC_index);
    if(null!=data&&data.actioncode=='SUCCESS'){
        $("#record_img img").css("display","none");
        $("#pauserecord").attr("onclick","img_bool(this,1);");
        var tips_msg="庭审作中~";
        var start_over_btn_msg="庭审中";
        if (record_pausebool==1){
            tips_msg="点击我可以暂停~";
            start_over_btn_msg="暂停庭审";
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
        $("#start_over_btn").text(start_over_btn_msg).attr("onclick","img_bool(this,2)");
        $("#startrecord").css("display","block");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips(tips_msg ,'#startrecord',{time:0, tips: 2});
        });

        var data=data.data;
        if (isNotEmpty(data)){
            var startMCVO=data.startMCVO;
            var polygraphnum=startMCVO.polygraphnum==null?0:startMCVO.polygraphnum;
            var recordnum=startMCVO.recordnum==null?0:startMCVO.recordnum;
            var asrnum=startMCVO.asrnum==null?0:startMCVO.asrnum;

            var mtssiddata=startMCVO.mtssid;
            useretlist=startMCVO.useretlist;

            /*if (isNotEmpty(useretlist)) {
                for (var i = 0; i < useretlist.length; i++) {
                    var useret = useretlist[i];
                    var userssid1 = useret.userssid;
                    if (userssid1 == dq_recorduser) {
                        liveurl = useret.livingurl;//开始会议后默认使用副麦预览地址
                        console.log("当前liveurl————"+liveurl)
                    }
                }
                initplayer();//初始化地址
            }*/

            mtssid=mtssiddata;
            mcbool=1;//正常开启

            getMCCacheParamByMTssid();//获取缓存
            getTDCacheParamByMTssid();

            var con="已开启：<br>语音识别开启数："+asrnum;
            layer.msg(con, {time: 2000});
        }

    }else{
        $("#MtState").text("未启动");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("未启动");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});


        var data2=data.data;
        if (isNotEmpty(data2)){
            var checkStartRecordVO=data2.checkStartRecordVO;
            var recordbool=data2.recordbool; //笔录开始状态
            $("#record_img img").css("display","none");
            if (null!=recordbool&&recordbool==true){
                $("#endrecord").css("display","block");
                $("#start_over_btn").text("庭审已结束").attr("onclick","img_bool(this,-1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips("该庭审已经制作过啦~" ,'#endrecord',{time:0, tips: 2});
                });
            }else {
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                $("#start_over_btn").text("开始庭审").attr("onclick","img_bool(this,1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips:2});
                });
            }

            if (null!=checkStartRecordVO){
                var msg=checkStartRecordVO.msg;
                parent.layer.confirm("开启失败(<span style='color:red'>"+msg+"</span>)，请先结束正在进行中的庭审", {
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
        $("#start_over_btn").text("开始庭审").attr("onclick","img_bool(this,1)");
        $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips:2});
        });
    }
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
            var con=msg+"：<br>语音识别"+msg+"数："+asrnum;
            layer.msg(con, {time: 2000});
            if (pauseOrContinue==1){
                $("#pauserecord").css("display","block");
                $("#start_over_btn").text("继续庭审").attr("onclick","img_bool(this,1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 2});
                });
            } else {
                $("#startrecord").css("display","block");
                $("#start_over_btn").text("暂停庭审").attr("onclick","img_bool(this,2)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips: 2});
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
                $("#start_over_btn").text("暂停庭审").attr("onclick","img_bool(this,2)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips:2});
                });
            } else if (pauseOrContinue==2){//请求继续
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                $("#start_over_btn").text("暂停庭审").attr("onclick","img_bool(this,1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 2});
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
        var url=getActionURL(getactionid_manage().waitCourt_addRecord);
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
                    var url=getActionURL(getactionid_manage().waitCourt_torecordIndex);
                    window.location.href=url;
                })
            }else {
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


    layer.confirm(msgtxt2+'<br/>'+msgtxt, {
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
        /*if (isNotEmpty(fdCacheParams)){
            for (var i = 0; i < fdCacheParams.length; i++) {
                var fdCacheParam = fdCacheParams[i];
                liveurl = fdCacheParam.livingUrl;//开始会议后默认使用副麦预览地址
                console.log("当前liveurl————"+liveurl)
            }
            initplayer();
        }*/
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
                            var translatext=data.keyword_txt==null?"":data.keyword_txt;//翻译文本
                            var gradename=user.gradename==null?"未知":user.gradename;


                            //实时会议数据
                                //1放左边
                                var color=asrcolor[usertype]==null?"#0181cc":asrcolor[usertype];
                            var fontcolor="#ffffff";
                            if (gnlist.indexOf(NX_O)!= -1){
                                color="#ffffff";
                                fontcolor="#000000";
                                recordrealshtml='<div style="margin:10px 0px" userssid='+userssid+' starttime='+starttime+'>\
                                                            <span style="background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;">'+gradename+'： '+translatext+' </span>\
                                                      </div >';
                            }else {
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+gradename+'】 '+asrstartime+' </p>\
                                                            <span onmousedown="copy_text(this,event)" style="background-color: '+color+';color: '+fontcolor+';" >'+translatext+'</span> \
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
            if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                focuslable(trtd_html,1,'q');
            } else {
                lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                setFocus(lable);
            }
            event.preventDefault();
            break;
        case 38:
            console.log("上一句")
            var index=(trindex-1)<=0?0:(trindex-1);
                if(trindex!=0){
                    lable=$('#recorddetail tr:eq("'+index+'") label[name="q"]');
                    setFocus(lable);
                }
                event.preventDefault();
            break;
        case 40:
            console.log("下一句")
            var index=(trindex+1)>=trlength?trlength:(trindex+1);
            lable=$('#recorddetail tr:eq("'+index+'") label[name="q"]');
            setFocus(lable);
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
            if (isNotEmpty(TDCache)&&isNotEmpty(MCCache)&&isNotEmpty(fdrecordstarttime)&&fdrecordstarttime>0) {
                var useasr=TDCache.useasr==null?-1:TDCache.useasr;//是否使用语言识别，1使用，-1 不使用
                var asrnum=MCCache.asrnum==null?0:MCCache.asrnum;
                console.log("直播的开始时间："+fdrecordstarttime+";是否开启语音识别："+useasr+"__语音识别数__"+asrnum)
               /* if ((useasr==-1&&isNotEmpty(mtssid))||(asrnum<1&&isNotEmpty(mtssid))){   后期改为不需要判断是否开启了语音识别：回车都加时间*/
                    var dqtime=new Date().getTime();
                    var qw_type=el.getAttribute("name");
                    if (isNotEmpty(qw_type)){
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
                /*}else {
                    console.log("使用语音识别时间~")
                }*/
            }else{
                console.log("TDCache___"+TDCache+"___MCCache___"+MCCache+"___fdrecordstarttime___"+fdrecordstarttime);
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

    $(obj).removeClass("layui-btn-primary").addClass("layui-btn-normal").siblings().removeClass("layui-btn-normal").addClass("layui-btn-primary");
}

//*******************************************************************点击end****************************************************************//





//默认问答
var trtd_html='<p><br/></p>';

//lable type 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    if (type==1){
        $('#recorddetail tr:eq("'+record_index["key"]+'")').after(html);
        if (isNotEmpty(qw)){
            qwfocus= $('#recorddetail tr:eq("'+(record_index["key"]+1)+'") label[name="'+qw+'"]');
            record_index["key"]=record_index["key"]+1;
        }
    }  else if (type==0) {
        $("#recorddetail").prepend(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:eq(0) label[name="'+qw+'"]');
            record_index["key"]=$('#recorddetail tr:eq(0)').index();
        }
    }else if (type==2){
        //判断laststarttime_ue是否为空，
        // 为空判断是否存在光标获取光标在第几行在该标签后边追加
        //不为空获取最后一个p标签在该标签后边追加
       var lastp=null;
        if (isNotEmpty(laststarttime_ue)){
             lastp=$("p[starttime="+laststarttime_ue+"]:last",editorhtml);
            if (!isNotEmpty(lastp)){
                lastp=$("p[starttime]:not(:empty)",editorhtml).last();
                if (!isNotEmpty(lastp)){
                    lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
                }
            }
        }else {
            //光标追加
            //获取光标所在的p标签
            lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
        }

        if (isNotEmpty(lastp)) {
            $(html).insertAfter(lastp);//[lastp.length-1]
        }else {
            //p标签未获取到，使用append
            console.log("p标签未获取到，使用append")
            var divid=$("p[starttime="+laststarttime_ue+"]:last",editorhtml).closest("div").attr("id");
            if (!isNotEmpty(divid)){
                divid = TOWORD.util.getdivByChildnode(ue);
            }
            $("#"+divid,editorhtml).append(html);
        }
    }
}

/**
 * 获取上一个标签的样式
 */
function getlastp_style() {
    var lastp=null;
    var pstyle="";
    if (isNotEmpty(laststarttime_ue)){
        lastp=$("p[starttime="+laststarttime_ue+"]:last",editorhtml);
        if (!isNotEmpty(lastp)){
            lastp=$("p[starttime]:not(:empty)",editorhtml).last();
            if (!isNotEmpty(lastp)){
                lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
            }
        }
    }else {
        //光标追加
        //获取光标所在的p标签
        lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
    }

    if (!isNotEmpty(lastp)) {
        var divid=$("p[starttime="+laststarttime_ue+"]:last",editorhtml).closest("div").attr("id");
        if (!isNotEmpty(divid)){
            divid = TOWORD.util.getdivByChildnode(ue);
        }
        lastp= $("#"+divid+" p:last",editorhtml);
    }
    if (isNotEmpty(lastp)){
        pstyle=$("span",lastp).attr("style");
        if(typeof(pstyle) == "undefined"){
            pstyle="";
        }
       var newpstyle = $(lastp).attr("style");
        if(typeof(newpstyle) == "undefined"){
            newpstyle="";
        }
        console.log("span的样式："+pstyle);
        console.log("p标签的样式："+newpstyle)
        return pstyle+newpstyle;
    }
    return pstyle;
}


/***************************************笔录实时问答start*************************************************/
/*笔录实时保存*/
function setRecordreal() {
    var url=getActionURL(getactionid_manage().waitCourt_setRecordreal);
    var recordToProblems=[];//题目集合
    $("div",editorhtml).each(function (i) {
        var q=$(this).html();
        recordToProblems.push({
            problem:q,
            starttime:-1,
            answers:null
        });
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
        }
    }else{
        //layer.msg(data.message,{icon: 5});
    }
}

//获取缓存实时问答
function getRecordrealByRecordssid() {
    var url=getActionURL(getactionid_manage().waitCourt_getRecordrealByRecordssid);
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
                var problemhtml="";
                for (var z = 0; z< problems.length;z++) {
                    var problem = problems[z];
                    var problemtext=problem.problem==null?"未知":problem.problem;
                    problemhtml+=problemtext;
                }
                TOWORD.page.importhtml(problemhtml);
                laststarttime_ue=$("p[starttime]:not(:empty)",editorhtml).last().attr("starttime");//获取最后一个laststarttime_ue
                 console.log("退出再进来找到的最后时间点?__-__"+laststarttime_ue)

              /*  var problemhtml= setqw(problems);
                focuslable(problemhtml,2,'w');*/
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }


}



/***************************************笔录实时问答end*************************************************/

//整合问答笔录html
function setqw(problems){
    if (isNotEmpty(problems)) {
        var problemhtml="";
        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];
            var problemtext=problem.problem==null?"未知":problem.problem;
            problemhtml+= '<p style="margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: " starttime="'+problem.starttime+'">'+problemtext+'</p>';
        }
        return problemhtml;
    }
    return null;
}





//定时器关闭
var setinterval1=null;
//导出word
var exportWord_index=null;
var exportPdf_index=null;

var currenttime;
var yesterdaytime;
$(function () {




  /*  $(document).keypress(function (e) {
        if (e.which == 13) {
            focuslable(trtd_html,2,'q');
            event.preventDefault();
        }
    });*/

    //导出
    $("#dc_li li").click(function () {
        var type=$(this).attr("type");
        if (type==1){
            exportWord_index=layer.msg("导出中，请稍等...", {
                icon: 16,
                shade: [0.1, 'transparent'],
                time:10000
            });
           /* exportWord();*/
            exporttemplate_ue(1);
        }else  if(type==2){
            exportPdf_index=layer.msg("导出中，请稍等...", {
                icon: 16,
                shade: [0.1, 'transparent'],
                time:10000
            });
           /* exportPdf();*/
            exporttemplate_ue(2);
        }
    });
    //常用问答点击
    $("#cywd_li li").click(function () {
        var text=$(this).text();
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==record_index["key"]&&value==record_index["value"]) {
                $(this).append(text);
            }
        });
    });
    //常用时间点击
    $("#cysj_li li").click(function () {
        var type=$(this).attr("type");
        var text=$(this).text();
        if (type==1){
            var time="";
            //当前时间
            if (isNotEmpty(currenttime)){
                time=currenttime;
            }
        }else  if(type==2){
            //昨天时间
            if (isNotEmpty(yesterdaytime)){
                time=yesterdaytime;
            }
        }else  if(type==3){
            //案发时间
            if (isNotEmpty(occurrencetime_format)){
                time=occurrencetime_format;
            }else {
                layer.msg("案发时间未设置",{icon:6})
            }
        }
        if (isNotEmpty(time)){
            $("#recorddetail label").each(function(){
                var lastindex=$(this).closest("tr").index();
                var value=$(this).attr("name");
                if (lastindex==record_index["key"]&&value==record_index["value"]) {
                    $(this).append(time);
                }
            });
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

        if (isNotEmpty(mtssid)&&isNotEmpty(TDCache)) {
            var usepolygraph=TDCache.usepolygraph==null?-1:TDCache.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
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




//自动甄别初始化
    var last_identifys = {};//每个人上一次甄别内容 格式：usertype：{starttime:123,translatext:"sasas",oldtranslatext:"sad",gradeintroduce:"gradeintroduce"}
    var lastusertype=-1;//上一个类型

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
                        var usertype=user.grade;//1、询问人2被询问人 对应别色类型
                        if (data.userssid==userssid){
                                var username=user.username==null?"未知":user.username;//用户名称
                                var translatext=data.txt==null?"":data.txt;//翻译文本
                                var starttime=data.starttime;
                                var asrstartime=data.asrstartime;
                                var recordrealshtml="";
                                var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本
                                var gradename=user.gradename==null?"未知":user.gradename;
                                var gradeintroduce=user.gradeintroduce==null?"未知":user.gradeintroduce;

                                var p_span_HTML="";
                                //实时会议数据

                                var color=asrcolor[usertype]==null?"#0181cc":asrcolor[usertype];
                                var fontcolor="#ffffff";
                                if (gnlist.indexOf(NX_O)!= -1){
                                    color="#ffffff";
                                    fontcolor="#000000";
                                    p_span_HTML='<span  style="background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;">'+gradename+'： '+translatext+' </span>';
                                    recordrealshtml='<div style="margin:10px 0px" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';
                                }else {
                                    p_span_HTML='<p>【'+gradename+'】 '+asrstartime+' </p>\
                                            <span onmousedown="copy_text(this,event)"  style="background-color: '+color+';color: '+fontcolor+';" >'+translatext+'</span>';
                                    recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';
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

                                //自动甄别开启没
                                var record_switch_bool=$("#record_switch_bool").attr("isn");
                                if (record_switch_bool==1){
                                    if (isNotEmpty(dqswitchusers)&&dqswitchusers.includes(usertype)) {
                                        console.log(last_identifys)
                                        gradeintroduce=gradeintroduce+"：";
                                        if (lastusertype!=-1){
                                            var last_identify=last_identifys[""+lastusertype+""];//上一个数据
                                            if (isNotEmpty(last_identify)) {
                                                var newlast_identify={};
                                                var last_starttime = null;
                                                var last_translatext = null;
                                                var last_oldtranslatext = null;
                                                if (isNotEmpty(lastusertype)&&usertype==lastusertype) {
                                                    last_starttime=last_identify.starttime;
                                                    last_translatext=last_identify.translatext;
                                                    last_oldtranslatext=last_identify.oldtranslatext;
                                                    if (last_starttime==starttime){
                                                        last_translatext=translatext;
                                                    }else{
                                                        last_oldtranslatext+=last_translatext;
                                                        last_translatext=translatext;
                                                    }
                                                    $("p[usertype="+usertype+"]:last",editorhtml).html(gradeintroduce+last_oldtranslatext+last_translatext);
                                                }else {
                                                    last_identify=last_identifys[""+usertype+""];//新用户的上一个数据
                                                    if (isNotEmpty(last_identify)){
                                                        last_starttime=last_identify.starttime;
                                                        last_translatext=last_identify.translatext;
                                                        last_oldtranslatext=last_identify.oldtranslatext;
                                                        if (last_starttime==starttime) {
                                                            last_translatext=translatext;
                                                            $("p[usertype="+usertype+"]:last",editorhtml).html(gradeintroduce+last_oldtranslatext+last_translatext);
                                                        }else {
                                                            last_oldtranslatext="";
                                                            last_translatext=translatext;
                                                            var trtd_html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'">'+gradeintroduce+last_translatext+'</p>';
                                                            focuslable(trtd_html,2,null);
                                                            laststarttime_ue=starttime;//更新最后一个
                                                            resetpage();
                                                        }
                                                    } else {
                                                        last_oldtranslatext="";
                                                        last_translatext=translatext;
                                                        var trtd_html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'">'+gradeintroduce+last_translatext+'</p>';
                                                        focuslable(trtd_html,2,null);
                                                        laststarttime_ue=starttime;//更新最后一个
                                                        resetpage();
                                                    }
                                                }

                                                newlast_identify={
                                                    starttime:starttime,
                                                    translatext:last_translatext,
                                                    oldtranslatext:last_oldtranslatext,
                                                }
                                                last_identifys[""+usertype+""]=newlast_identify;
                                            }else {
                                                console.log("我居然是空的"+usertype)
                                                //初始化追加新的
                                                var trtd_html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'">'+gradeintroduce+translatext+'</p>';
                                                focuslable(trtd_html,2,null);
                                                laststarttime_ue=starttime;//更新最后一个
                                                resetpage();
                                                var newlast_identify={
                                                    starttime:starttime,
                                                    translatext:translatext,
                                                    oldtranslatext:"",
                                                }
                                                last_identifys[""+usertype+""]=newlast_identify;
                                            }
                                        } else {
                                            //初始化追加新的
                                            var trtd_html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'">'+gradeintroduce+translatext+'</p>';
                                            focuslable(trtd_html,2,null);
                                            laststarttime_ue=starttime;//更新最后一个
                                            resetpage();
                                            var newlast_identify={
                                                starttime:starttime,
                                                translatext:translatext,
                                                oldtranslatext:"",
                                            }
                                            last_identifys[""+usertype+""]=newlast_identify;
                                        }
                                        lastusertype=usertype;
                                    }else {
                                        console.log("我没在里面啊啊啊啊__"+usertype)
                                        last_identifys[""+usertype+""]=null;
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
            if (!isNotEmpty(laststarttime_ue)) {
                con="确定要开启自动甄别吗(<span style='color: red'>将在光标处追加</span>)";//
            }else {
                con="确定要开启自动甄别吗(<span style='color: red'>根据最后追加</span>)";//（<span style='color: red'>将在光标处加入自动甄别</span>）
            }

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



/**
 笔录分出来的js
 **/
//*******************************************************************伸缩按钮start****************************************************************//
function shrink(obj) {
    layer.closeAll("tips");

    var shrink_bool=$(obj).attr("shrink_bool");
    if (shrink_bool==1){

        $("#shrink_html").hide();
        $(obj).attr("shrink_bool","-1");
        $("i",obj).attr("class","layui-icon layui-icon-spread-left");

        $("#notshrink_html1").attr("class","layui-col-md12");
        $("#layui-layer"+recordstate_index).hide();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }else{
        $("#shrink_html").show();
        $(obj).attr("shrink_bool","1");
        $("i",obj).attr("class","layui-icon layui-icon-shrink-right");

        $("#notshrink_html1").attr("class","layui-col-md9");
        $("#layui-layer"+recordstate_index).show();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }
}
//*******************************************************************伸缩按钮end****************************************************************//


//*******************************************************************告知书start****************************************************************//
var notificationListdata=null;
function getNotifications() {
    var url=getActionURL(getactionid_manage().waitCourt_getNotifications);
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
                }
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
        layui.use(['layer','element','upload'], function(){
            var layer = layui.layer; //获得layer模块
            var element = layui.element;
            var upload = layui.upload;
            //使用模块

            var url=getActionURL(getactionid_manage().waitCourt_uploadNotification);

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
    var url=getActionURL(getactionid_manage().waitCourt_downloadNotification);
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
                    layer.msg("下载中，请稍后...",{icon: 6});
                    window.location.href=recorddownurl;
                }
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
    });
}

//打开告知书
var previewgetNotifications_index=null;
var dqrecorddownurl_htmlreads=null;//读取阅读txt

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

    var url=getActionURL(getactionid_manage().waitCourt_downloadNotification);
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

                            if (!isNotEmpty(gnlist)||!gnlist.includes(TTS_F)){
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
                            dqrecorddownurl_htmlreads=data.recorddownurl_htmlreads;
                            if (isNotEmpty(dqrecorddownurl_htmlreads)){
                                t1 = window.setInterval(function (args) {
                                    var text=dqrecorddownurl_htmlreads[len];
                                    if (!isNotEmpty(audioplay)&&len==0){
                                        str2Tts(text);
                                        len++;
                                    } else if (audioplay.ended) {
                                        str2Tts(text);
                                        len++;
                                    }
                                    if (len>dqrecorddownurl_htmlreads.length-1){
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
            layer.msg(data.message,{icon: 5});
        }
    });

}

//告知书朗读
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
        layer.msg(data.message,{icon: 5});
    }
}

//*******************************************************************告知书start****************************************************************//

//*******************************************************************左侧搜索块start****************************************************************//
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
        for (var i = 0; i < likerealtxtarr.length; i++) {
            var all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        var div = document.getElementById('recordreals_scrollhtml');
        div.scrollTop = top;
    }
}
function recordreals_select() {
    mouseoverbool_left=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];
    var recordrealshtml= $("#recordreals").html();
    recordrealshtml=recordrealshtml.replace(/(<\/?a.*?>)/g, '');
    $("#recordreals").html(recordrealshtml);

    $("#recordreals div").each(function (i,e) {
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
//*******************************************************************左侧搜索块end****************************************************************//

//*******************************************************************左侧授权模块显示start****************************************************************//
var gnlist=null;
function getgnlist() {
    var url=getActionURL(getactionid_manage().waitCourt_gnlist);
    var data={
        token:INIT_CLIENTKEY,
        param:{

        }
    };
    ajaxSubmitByJson(url, data, callbackgnlist);
}
function callbackgnlist(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var lists=data.lists;
            if (isNotEmpty(lists)){
                gnlist=lists;
                if (!isNotEmpty(gnlist)||!gnlist.includes(RECORD_F)){
                    layer.msg("请先获取笔录授权",{time:2000,icon:16,shade: 0.3},function () {
                        window.history.go(-1);
                        return false;
                    })
                }
            }
        }
    }else {
        layer.msg(data.message,{icon: 5});
    }
}
//*******************************************************************左侧授权模块显示endt****************************************************************//


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

//*******************************************************************获取各个状态start****************************************************************//
function  getEquipmentsState() {
    if (isNotEmpty(mtssid)&&isNotEmpty(MCCache)){
        var recordnum=MCCache.recordnum==null?0:MCCache.recordnum;//本次会议开启的录音/像个数
        var asrnum=MCCache.asrnum==null?0:MCCache.asrnum;//本次会议开启的语音识别个数
        var polygraphnum=MCCache.polygraphnum==null?0:MCCache.polygraphnum;//本次会议开启的测谎仪个数

        var fdrecord=TDCache.fdrecord==null?-1:TDCache.fdrecord;//是否需要录像，1使用，-1 不使用
        var usepolygraph=TDCache.usepolygraph==null?-1:TDCache.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
        var useasr=TDCache.useasr==null?-1:TDCache.useasr;//是否使用语言识别，1使用，-1 不使用

        var url=getUrl_manage().getEquipmentsState;
        var data = {
            token: INIT_CLIENTKEY,
            param: {
                mtssid: mtssid,
                fdrecord:fdrecord,
                usepolygraph:usepolygraph,
                useasr:useasr,
                recordnum:recordnum,
                asrnum:asrnum,
                polygraphnum:polygraphnum
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
            var MtClass = "layui-badge layui-bg-gray";
            var AsrClass = "layui-badge layui-bg-gray";
            var LiveClass = "layui-badge layui-bg-gray";
            var PolygraphClass = "layui-badge layui-bg-gray";

            var MtState = data.mtState;
            $("#mtbool_txt").html("正常检测");
            if (MtState == 0) {
                MtText = "未启动";
                MtClass = "layui-badge layui-bg-gray";
            } else if (MtState == 1) {
                MtText = "正常";
                MtClass = "layui-badge layui-bg-green";
            } else if (MtState == -1) {
                MtText = "异常";
                MtClass = "layui-badge";
            }else if (MtState == 3) {
                //==3 是真实的
                MtText = "暂停中";
                MtClass = "layui-badge layui-bg-gray";
                mcbool=MtState;
                $("#mtbool_txt").html(MtText)
            }
            var AsrState = data.asrState;
            if (AsrState == 0) {
                AsrText = "未启动";
                AsrClass = "layui-badge layui-bg-gray";
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
                LiveClass = "layui-badge layui-bg-gray";
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
                PolygraphClass = "layui-badge layui-bg-gray";
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
    }else {
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});
        $("#LiveState").text("加载中");
        $("#LiveState").attr({"LiveState": "", "class": "layui-badge layui-bg-gray"});
        $("#PolygraphState").text("加载中");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "layui-badge layui-bg-gray"});
    }
}
//*******************************************************************获取各个状态end****************************************************************//


function setRecordProtect() {
    var url=getActionURL(getactionid_manage().waitCourt_setRecordProtect);

    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
            mtssid:mtssid,
        }
    };
    ajaxSubmitByJson(url, data, callbacksetRecordProtect);
}
function callbacksetRecordProtect(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("笔录实时本地保存成功__"+data);
        }
    }else{
        //layer.msg(data.message,{icon: 5});
    }
}







//语音识别颜色
var asrcolor=["#AA66CC","#0181cc","#ef8201","#99CC00","#e30000"," #ff80bf","#00b8e6","#00802b","#6f0000","#3333ff","#e64d00","#688b00","#b35900","#5c8a8a","#999966","#b3b3b3","#3366cc"];


///////////////////************导出**********************//////////
function exportWord() {
    var url=getActionURL(getactionid_manage().waitCourt_exportWord);
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
}

function exportPdf(){
    var url=getActionURL(getactionid_manage().waitCourt_exportPdf);
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
}
///////////////////************导出**********************//////////

///////////////////////////////**********************************************************百度编辑器**************start
var laststarttime_ue=null;//最后一个识别的starttime
function  resetpage() {
    var divid=null;
    if (isNotEmpty(laststarttime_ue)) {
        divid=$("p[starttime="+laststarttime_ue+"]",editorhtml).closest("div").attr("id");
    }else {
        divid = TOWORD.util.getdivByChildnode(ue);
    }


        if(!isNotEmpty(divid)){
            return;
        }else{
            TOWORD.currentdivnum=parseInt(divid.replace(/[^0-9]/ig,""));
        }
        console.log(" TOWORD.currentdivnum=222==="+ TOWORD.currentdivnum)
        var psheight=TOWORD.page.getAllPHeightByDivid(divid);
        var pseight_old=TOWORD.divheightmap[divid];
        if(isNotEmpty(psheight)){
            //对比上一次的高度，有变化的话，触发重新排版
            if(isNotEmpty(pseight_old)&&psheight!=pseight_old){
                //重新排版，写入toWord里面
                console.log("重新排版，写入toWord里面");
                TOWORD.page.reTypesetting(ue,pseight_old,psheight,divid);
            }
            console.log(pseight_old+":pseight_old----pseight:"+psheight+"---divid:"+divid);
        }

}


//导出模板
function exporttemplate_ue(exporttype) {
    if (isNotEmpty(exporttype)&&isNotEmpty(recordssid)) {
        var url=getActionURL(getactionid_manage().waitCourt_exporttemplate_ue);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                exporttype:exporttype,
            }
        };
        ajaxSubmitByJson(url,paramdata,callbackexporttemplate_ue);
    }
}

function callbackexporttemplate_ue(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var exporttype=data.exporttype;
            var word_downurl=data.word_downurl;//word导出地址
            var pdf_downurl=data.pdf_downurl;//pdf导出地址
            if (isNotEmpty(exporttype)) {
               if (exporttype==1&&isNotEmpty(word_downurl)){
                   window.location.href = word_downurl;
                   layer.msg("导出成功,等待下载中...",{icon: 6});
               } else if (exporttype==2&&isNotEmpty(pdf_downurl)){
                   layer.open({
                       id:"pdfid",
                       type: 1,
                       title: '导出PDF',
                       shadeClose: true,
                       maxmin: true, //开启最大化最小化按钮
                       area: ['893px', '600px'],
                   });
                   showPDF("pdfid",pdf_downurl);
                   layer.msg("导出成功,等待下载中...",{icon: 6});
               }else {
                   layer.msg("导出失败",{icon: 5});
               }
            }
        }
    }else{
         layer.msg(data.message,{icon: 5});
    }
}

///////////////////////////////**********************************************************百度编辑器**************end

///////////////////////////////**********************************************************甄别人员设置**************start
var record_switchusers=[];//全部角色人员
var dqswitchusers=[];//已选人员
function set_record_switchusers() {
    if (!isNotEmpty(record_switchusers)){
        layer.msg("未找到可甄别角色",{icon: 5});
        return;
    }
    console.log(record_switchusers)
    console.log(dqswitchusers)
    var HTML='<div id="switchusers" style="margin: 30px"></div>';
    layui.use(['transfer','layer','element','form'], function(){
        var transfer = layui.transfer;

        layer.open({
            type: 1,
            title:'甄别角色筛选',
            content:HTML,
            btn: ['确定','取消'],
            success:function(layero, index){
                //渲染
                transfer.render({
                    elem: '#switchusers'  //绑定元素
                    ,id:'switchusers'
                    ,title: ['待选角色', '已选角色']
                    ,width: 150 //定义宽度
                    ,height: 250 //定义高度
                    ,data: record_switchusers
                    ,parseData: function(res){
                    return {
                        "value": res.grade //数据值
                        ,"title": res.gradename //数据标题
                        }
                    }
                     ,value: dqswitchusers
                    });
            },
            yes:function(index, layero){
               var switchusers = transfer.getData('switchusers');
                dqswitchusers=[];
                if (isNotEmpty(switchusers)){
                    for (let i = 0; i < switchusers.length; i++) {
                        const switchuser = switchusers[i];
                        dqswitchusers.push(switchuser.value);
                    }
                }
                layer.close(index);
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });


    });
}

///////////////////////////////**********************************************************甄别人员设置**************end

