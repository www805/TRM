
var recorduser=[];//会议用户集合：多个
var dq_recorduser=null;//当前被询问人ssid

var mcbool=null;//会议状态
var recordbool=null;//笔录状态 -1 -2暂时用于导出判断不存在数据库

var casebool=null;//案件状态

var  mouseoverbool_left=-1;//是否滚动-1滚1不滚

var MCCache=null;//会议缓存数据
var TDCache=null;//会议通道缓存：不可借用会议缓存json转换识别（转换失败原因：疑似存在线程对象）
var fdrecordstarttime=0;//直播开始时间戳（用于计算回车笔录时间锚点）

var getRecordById_data=null;//单份笔录返回的全部数据


var record_pausebool=-1;//笔录是否允许暂停1允许 -1 不允许 默认不允许-1
var record_adjournbool=-1;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1


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
                layer.tips("笔录中~" ,'#startrecord',{time:0, tips: 2});
            });
            layer.msg("笔录中~");
        }
    }else if(type==-1) {
        $("#endrecord").css("display","block");
        layui.use(['layer','element','form'], function(){
            var layer=layui.layer;
            layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips:2});
        });
        console.log("会议已结束")
        layer.msg("该笔录已经制作过啦~");
    }
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
                            layer.tips('该笔录已经制作过啦~' ,'#endrecord',{time:0, tips: 2});
                        });
                        $("#start_over_btn").text("笔录已结束").attr("onclick","img_bool(this,-1)");
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
                            var start_over_btn_msg="笔录中";
                            if (record_pausebool==1) {
                                tips_msg="点击我可以暂停~";
                                start_over_btn_msg="暂停笔录";
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
                            $("#start_over_btn").text("继续笔录").attr("onclick","img_bool(this,1)");
                        }
                    }else {
                        $("#pauserecord").css("display","block");
                        layui.use(['layer','element','form'], function(){
                            var layer=layui.layer;
                            layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 2});
                        });
                        $("#start_over_btn").text("开始笔录").attr("onclick","img_bool(this,1)");
                        $("#overRecord_btn").hide();
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
                              /* if (other.ssid==USERINFOGRADE2&&!isNotEmpty(dq_recorduser)){
                                   //被告
                                   dq_recorduser=other.userssid;
                               }*/
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
                                  <tr><td>案件时间</td> <td>"+starttime+"</td> </tr>\
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
    $("#start_over_btn").text("笔录开启中").attr("onclick","");
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
            $("#start_over_btn").text("开始笔录").attr("onclick","img_bool(this,1)");
        });
    }
}
function callbackstartMC(data) {
    layer.close(startMC_index);
    if(null!=data&&data.actioncode=='SUCCESS'){
        $("#record_img img").css("display","none");
        $("#pauserecord").attr("onclick","img_bool(this,1);");
        var tips_msg="笔录作中~";
        var start_over_btn_msg="笔录中";
        if (record_pausebool==1){
            tips_msg="点击我可以暂停~";
            start_over_btn_msg="暂停笔录";
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

            mtssid=mtssiddata;
            mcbool=1;//正常开启

            getMCCacheParamByMTssid();//获取缓存
            getTDCacheParamByMTssid();

            var con="已开启：<br>语音识别开启数："+asrnum;
            layer.msg(con, {time: 2000});
            $("#overRecord_btn").show();
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
                $("#start_over_btn").text("笔录已结束").attr("onclick","img_bool(this,-1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips("该笔录已经制作过啦~" ,'#endrecord',{time:0, tips: 2});
                });
            }else {
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                $("#start_over_btn").text("开始笔录").attr("onclick","img_bool(this,1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips:2});
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
        $("#start_over_btn").text("开始笔录").attr("onclick","img_bool(this,1)");
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
                $("#start_over_btn").text("继续笔录").attr("onclick","img_bool(this,1)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 2});
                });
            } else {
                $("#startrecord").css("display","block");
                $("#start_over_btn").text("暂停笔录").attr("onclick","img_bool(this,2)");
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
                $("#start_over_btn").text("暂停笔录").attr("onclick","img_bool(this,2)");
                layui.use(['layer','element','form'], function(){
                    var layer=layui.layer;
                    layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips:2});
                });
            } else if (pauseOrContinue==2){//请求继续
                $("#pauserecord").css("display","block").attr("onclick","img_bool(this,1);");
                $("#start_over_btn").text("暂停笔录").attr("onclick","img_bool(this,1)");
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
                }
            }
        });
    }
}


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
function addRecord() {
    setRecordreal();
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
                            /*var txt=data.txt==null?"":data.txt;//翻译文本*/
                            var translatext=data.tagtext==null?data.txt:data.tagtext;//需要保留打点标记的文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";
                            //var translatext=data.keyword_txt==null?"":data.keyword_txt;//翻译文本
                            var gradename=user.gradename==null?"未知":user.gradename;


                            //实时会议数据
                                //1放左边
                            var color=asrcolor[usertype]==null?"#0181cc":asrcolor[usertype];
                            var fontcolor="#ffffff";
                            if (gnlist.indexOf(NX_O)!= -1){
                                color="#ffffff";
                                fontcolor="#000000";
                                recordrealshtml='<div style="margin:10px 0px;background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;" userssid='+userssid+' starttime='+starttime+'>\
                                                            <a>'+gradename+'：</a><span  ondblclick="copy_text(this)"> '+translatext+' </span>\
                                                      </div >';

                            }else {
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+gradename+'】 '+asrstartime+' </p>\
                                                            <span  style="background-color: '+color+';color: '+fontcolor+';"   ondblclick="copy_text(this)">'+translatext+'</span> \
                                                      </div >';
                            }

                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"][starttime="+starttime+"]").remove();
                            }

                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals_scrollhtml');
                            div.scrollTop = div.scrollHeight;
                            tagtext();
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
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

    $(obj).removeClass("layui-btn-primary").addClass("layui-btn-normal").siblings().removeClass("layui-btn-normal").addClass("layui-btn-primary");
}

//*******************************************************************点击end****************************************************************//





//默认问答
var trtd_html='<p><br/></p>';

//lable type 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    if (type==1){
    }  else if (type==0) {
    }else if (type==2){
        //判断laststarttime_ue是否为空，
        // 为空判断是否存在光标获取光标在第几行在该标签后边追加
        //不为空获取最后一个p标签在该标签后边追加
       var lastp=getlastp();
        if (isNotEmpty(lastp)) {
            $(html).insertAfter(lastp);//[lastp.length-1]
            TOWORD.page.checkAndDealSpanHeight(lastp[0],true);
        }else {
          console.log("追加的P未找到啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊**********************************")
        }
    }
}

function getlastp() {
    var lastp=null;
    if (isNotEmpty(laststarttime_ue)){
        lastp=$("p[starttime="+laststarttime_ue+"]:last",editorhtml);
        if (!isNotEmpty(lastp)){
            lastp=$("p[starttime]:not(:empty)",editorhtml).last();
            if (!isNotEmpty(lastp)){
                lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
                lastp=$(lastp);
            }
        }
    }else {
        //光标追加
        //获取光标所在的p标签
        lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
        lastp=$(lastp);
    }
    //latsp不存在查找divid
    if (!isNotEmpty(lastp)) {
        var divid=$("p[starttime="+laststarttime_ue+"]:last",editorhtml).closest("div").attr("id");
        if (!isNotEmpty(divid)){
            divid = TOWORD.util.getDivIdByUE(ue);
        }
        lastp= $("#"+divid+" p:last",editorhtml);
    }
    return lastp;
}

/**
 * 获取上一个标签的样式
 */
function getlastp_style() {
    var pstyle="";
    var lastp=getlastp();
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
            console.log("笔录实时保存成功__"+data);
        }
    }else{
        //layer.msg(data.message,{icon: 5});
    }
}

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
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }


}



/***************************************笔录实时问答end*************************************************/
//左侧点击
function copy_text(obj) {
    var lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
    if (!isNotEmpty(lastp)) {
        layer.msg("请先在笔录界面加入光标");''
        return;
    }
    var starttime=$(obj).closest("div").attr("starttime");
    var txt=$(obj).text();
    var oldstarttime=$(lastp).attr("starttime");//获取光标所在位置
    //样式
    var  pstyle=$("span",lastp).attr("style");
    if(typeof(pstyle) == "undefined"){
        pstyle="";
    }
    var newpstyle = $(lastp).attr("style");
    if(typeof(newpstyle) == "undefined"){
        newpstyle="";
    }
    $(lastp).append(txt);
    if (!isNotEmpty(oldstarttime)&&isNotEmpty(starttime)){
        $(lastp).attr("starttime",starttime);
        $(lastp).attr("style",newpstyle+pstyle);
    }




}






//定时器关闭
var setinterval1=null;
//导出word
var exportWord_index=null;
var exportPdf_index=null;

$(function () {



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
                                    p_span_HTML='<a>'+gradename+'：</a><span ondblclick="copy_text(this)">'+translatext+' </span>';
                                    recordrealshtml='<div style="margin:10px 0px;background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';
                                }else {
                                    p_span_HTML='<p>【'+gradename+'】 '+asrstartime+' </p>\
                                            <span  style="background-color: '+color+';color: '+fontcolor+';"  ondblclick="copy_text(this)">'+translatext+'</span>';
                                    recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>'+p_span_HTML+'</div >';
                                }

                                var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                                if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                    $("#recordreals div[userssid="+userssid+"]:last").html(p_span_HTML);
                                }else {
                                    $("#recordreals").append(recordrealshtml);
                                }
                               tagtext();


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
                                    //指定角色的放里面
                                    if (isNotEmpty(dqswitchusers)&&dqswitchusers.includes(usertype)) {
                                        gradeintroduce=gradeintroduce+"：";//前面的角色名称
                                        identify(usertype,starttime,gradeintroduce,translatext)
                                    }else {
                                        console.log("我没在里面啊啊啊啊__"+usertype)
                                        //last_identifys[""+usertype+""]=null;
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
            if (!isNotEmpty(dqswitchusers)){
                layer.msg("未找到可甄别的角色，请先设置",{icon:5})
                return;
            }
            var latsp=getlastp();
            $(latsp).append("<b style='color: red' id='tag'>从这里开始追加</b>");

            if (!isNotEmpty(laststarttime_ue)) {
                con="确定要开启自动甄别吗(<span style='color: red'>将在光标后追加</span>)";//
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
                    last_identifys = {};//每个人上一次甄别内容 格式：usertype：{starttime:时间,translatext:"新文本",oldtranslatext:"原始文本"}
                    lastusertype=-1;//上一个类型
                }
                setTimeout(function () {
                    $("#tag",editorhtml).remove();//去掉刚刚标记的红色z
                },1000)
                layer.close(index);
            }
            ,btn2: function(index, layero){
                $("#tag",editorhtml).remove();//去掉刚刚标记的红色z
                layer.close(index);
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
        divid = TOWORD.util.getDivIdByUE(ue);
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


///////////////////////////////**********************************************************自动甄别**************start
var last_identifys = {};//每个人上一次甄别内容 格式：usertype：{firsttime:第一句时间，starttime:当前句时间}
var lastusertype=-1;//上一个角色类型
function identify(usertype,starttime,gradeintroduce,translatext) {
    var last_identify=last_identifys[""+lastusertype+""];//上一个角色
    var dq_identify=last_identifys[""+usertype+""];//当前角色

    if (lastusertype!=-1){
        if (isNotEmpty(last_identify)){
            if (usertype==lastusertype) {
                var thisp= $("p[usertype="+usertype+"][starttime="+last_identify.firsttime+"]:last",editorhtml);
                if (isNotEmpty(thisp)) {
                    var span_last = $("span[starttime]:not(:empty)",thisp).last();
                    var span_laststarttime=$(span_last).attr("starttime");
                    if (isNotEmpty(span_laststarttime)&&span_laststarttime==starttime) {
                        $(span_last).html(translatext);
                    }else {
                        $(thisp).append("<span starttime="+starttime+">"+translatext+"</span>");
                    }
                    TOWORD.page.checkAndDealSpanHeight(thisp[0],true);
                    last_identifys[""+usertype+""].starttime=starttime;
                }else {
                    addidentify(usertype,starttime,gradeintroduce,translatext);
                }
            }else {
                if (isNotEmpty(dq_identify)) {
                    var thisp= $("p[usertype="+usertype+"][starttime="+dq_identify.firsttime+"]:last",editorhtml);
                    if (dq_identify.starttime==starttime&&isNotEmpty(thisp)){
                            var span_last = $("span[starttime]:not(:empty)",thisp).last();
                            var span_laststarttime=$(span_last).attr("starttime");
                            if (isNotEmpty(span_laststarttime)&&span_laststarttime==starttime) {
                                $(span_last).last().html(translatext);
                            }else {
                                $(thisp).append("<span starttime="+starttime+">"+translatext+"</span>");
                            }
                            TOWORD.page.checkAndDealSpanHeight(thisp[0],true);
                            last_identifys[""+usertype+""].starttime=starttime;
                            usertype=lastusertype;//不改变上一个
                    } else {
                        addidentify(usertype,starttime,gradeintroduce,translatext);
                    }
                }else {
                    addidentify(usertype,starttime,gradeintroduce,translatext);
                }
            }
        }else {
            console.log("存在上一个角色不应该进来的吧******************************")
        }
    }else{
        console.log("初始化一下自动甄别******************************")
        addidentify(usertype,starttime,gradeintroduce,translatext);
    }
    lastusertype=usertype;
}

//追加新的一行哎哎哎
function addidentify(usertype,starttime,gradeintroduce,translatext) {
    var trtd_html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'"><span>'+gradeintroduce+'</span><span starttime="'+starttime+'">'+translatext+'</span></p>';
    focuslable(trtd_html,2,null);
    laststarttime_ue=starttime;
    resetpage();
    last_identifys[""+usertype+""]={firsttime:starttime,starttime:starttime};
}

///////////////////////////////**********************************************************自动甄别**************end




///////////////////////////////**********************************************************左侧打点**************start
function tagtext() {
    $("#recordreals span").bind('mousedown', function(e) {
        if (3 == e.which||1 == e.which){
            var userssid=$(this).closest("div").attr("userssid");
            var starttime=$(this).closest("div").attr("starttime");//语音识别时间标识
            var tagtxt=$(this).html();//打点标记文本
            $(this).attr("contenteditable",true);
            if (3 == e.which) {
                document.execCommand('removeFormat');
            }  else if (1 == e.which) {
                document.execCommand('foreColor',false,'red');
            }
            $(this).attr("contenteditable",false);
            setMCTagTxtreal(userssid,starttime,tagtxt);
        }
    });
}

//打点实时保存
function setMCTagTxtreal(userssid,starttime,tagtxt) {
    if (isNotEmpty(mtssid)&&isNotEmpty(userssid)&&isNotEmpty(starttime)&&isNotEmpty(tagtxt)) {
      var url=getActionURL(getactionid_manage().waitCourt_setMCTagTxtreal);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid,
                userssid:userssid,
                starttime:starttime,
                tagtxt:tagtxt,
            }
        };
        ajaxSubmitByJson(url,paramdata,callbacksetMCTagTxtreal);
    }else {
        console.log("打点实时保存___参数不全")
    }
}
function callbacksetMCTagTxtreal(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        console.log("打点实时保存___成功")
    }else{
        console.log(data.message)
    }
}
///////////////////////////////**********************************************************左侧打点**************end

