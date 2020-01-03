var recorduser=[];//会议用户集合

var dq_recorduser=null;//当前被询问人ssid

var mcbool=null;//会议状态
var  mouseoverbool_left=-1;//是否滚动-1滚1不滚




var getRecordById_data=null;//单份笔录返回的全部数据

var record_pausebool=-1;//笔录是否允许暂停1允许 -1 不允许 默认不允许-1
var record_adjournbool=-1;//笔录是否显示休庭按钮，用于案件已存在休庭笔录的时候不显示 1显示 -1 不显示 默认-1

var multifunctionbool;
var asrbool=0;//使用语音识别的个数
var phbool=0;//使用身心检测的个数



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
                var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().waitCourt_getRecordrealByRecordssid);
                getRecordrealByRecordssid(getRecordrealByRecordssidUrl);
                setInterval( function() {
                    var setRecordrealUrl=getActionURL(getactionid_manage().waitCourt_setRecordreal);
                    setRecordreal(setRecordrealUrl);
                },3000);
                setInterval( function() {
                    var setRecordProtectUrl=getActionURL(getactionid_manage().waitCourt_setRecordProtect);
                    setRecordProtect(setRecordProtectUrl);//5秒缓存一次
                },5000);

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//**********************************************************关于会议***********************************************start
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



//**********************************************************关于会议*************************************************end




$(function () {
    //导出
    $("#dc_li li").click(function () {
        var type=$(this).attr("type");
        layer.msg("导出中，请稍等...", { icon: 16, shade: [0.1, 'transparent'],time:6000});
        if (type==1){
            var exporttemplate_ueUrl=getActionURL(getactionid_manage().waitCourt_exporttemplate_ue);
            exporttemplate_ue(1,exporttemplate_ueUrl);
        }else  if(type==2){
            var exporttemplate_ueUrl=getActionURL(getactionid_manage().waitCourt_exporttemplate_ue);
            exporttemplate_ue(2,exporttemplate_ueUrl);
        }
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

        if (isNotEmpty(mtssid)&&isNotEmpty(TDCache)) {
            var usepolygraph=TDCache.usepolygraph==null?-1:TDCache.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
            if (isNotEmpty(mcbool)&&(mcbool==1||mcbool==3)){
                if (gnlist.indexOf(NX_O)== -1){
                    getEquipmentsState();
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
                                    p_span_HTML='<a>'+gradename+'：</a><span  id="translatext">'+translatext+' </span>';
                                    recordrealshtml='<div style="margin:10px 0px;background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;" userssid='+userssid+' starttime='+starttime+' id="asrdiv" ondblclick="copy_text(this,event)">'+p_span_HTML+'</div >';
                                }else {
                                    p_span_HTML='<p>【'+gradename+'】 '+asrstartime+' </p>\
                                            <span  id="translatext">'+translatext+'</span>';
                                    recordrealshtml='<div class="atalk" style="background-color: '+color+';color: '+fontcolor+';" userssid='+userssid+' starttime='+starttime+' id="asrdiv" ondblclick="copy_text(this,event)">'+p_span_HTML+'</div >';
                                }


                                var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                                if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                    $("#recordreals div[userssid="+userssid+"]:last").html(p_span_HTML);
                                }else {
                                    $("#recordreals").append(recordrealshtml);
                                }



                            $("#asritem").off("mouseout").bind("mouseout",function(event) {
                                var div = document.getElementById("asritem");
                                var x = event.clientX;
                                var y = event.clientY;
                                var divx1 = div.offsetLeft;
                                var divy1 = div.offsetTop;
                                var divx2 = div.offsetLeft + div.offsetWidth;
                                var divy2 = div.offsetTop + div.offsetHeight;
                                if (x < divx1 || x > divx2 || y < divy1 || y > divy2) {
                                    mouseoverbool_left=-1;
                                }else {
                                    mouseoverbool_left=1
                                }
                            });

                            $("#tooltip").hover(function(){
                                mouseoverbool_left=1;
                            });
                            console.log(mouseoverbool_left)
                                if (mouseoverbool_left==-1){
                                    $("#tooltip").remove();//移除标记提示
                                    window.getSelection().removeAllRanges();
                                    dq_recordrealsspan=null;
                                    var div = document.getElementById('recordreals_scrollhtml');
                                    div.scrollTop = div.scrollHeight;
                                }

                                //法院加了打点标记操作
                                if (gnlist.indexOf(NX_O)!= -1){
                                    $("#defaultsearch").hide();
                                    $("#tagtxtsearch").show();

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
        overbtn();
    });
});

function overbtn() {
    var setRecordrealUrl=getActionURL(getactionid_manage().waitCourt_setRecordreal);
    setRecordreal(setRecordrealUrl);
    var addRecordUrl=getActionURL(getactionid_manage().waitCourt_addRecord);
    var backurl=getActionURL(getactionid_manage().waitCourt_torecordIndex);
    addRecord(addRecordUrl,backurl);//回放不需要跳转地址
}




//甄别人员设置
var record_switchusers=[];//全部角色人员
var dqswitchusers=[];//已选人员
function set_record_switchusers() {
    if (!isNotEmpty(record_switchusers)){
        layer.msg("未找到可甄别角色",{icon: 5});
        return;
    }
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



//左侧打点:暂时只支持宁夏版本
function tagtext(type) {
    if (!isNotEmpty(type)) {
        return;
    }
    if (isNotEmpty(window.getSelection())&&isNotEmpty(window.getSelection().anchorNode)) {
        var stratnode=window.getSelection().anchorNode;//选择文本开始的节点
        var parentdiv = $(stratnode).closest("div");//获取选中区域的父节点div;
        if (isNotEmpty(parentdiv)) {
            var userssid=$(parentdiv).attr("userssid");
            var starttime=$(parentdiv).attr("starttime");//语音识别时间标识
            if (isNotEmpty(userssid)&&isNotEmpty(starttime)){
                console.log("开始进行标记操作了")
                //选择的是语音识别内容
                $("span",parentdiv).attr("contenteditable",true);
                if (type==1){
                    document.execCommand('foreColor',false,'red');
                } else {
                    document.execCommand('removeFormat');
                }
                $("span",parentdiv).attr("contenteditable",false);
                var tagtxt=$("span",parentdiv).html();//打点标记文本
                setMCTagTxtreal(userssid,starttime,tagtxt);
                window.getSelection().removeAllRanges();
            }else {
                layer.msg("未找到选中的语音识别内容",{icon:5})
            }
        } else {
            layer.msg("请确认是否选中语音识别内容",{icon:5})
        }
    }else {
        layer.msg("请先选择语音识别内容进行标记操作",{icon:5})
    }
}
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

