/**
 * 谈话审讯中
 * */
var getRecordById_data=null;//
var recorduser=[];//被询问人和询问人集合
var dq_recorduser;
var liveurl;
var dq_livingurl;
var dq_previewurl;
var mcbool=null;

var casebool=null;//案件状态


function getRecordById() {
    var url=getActionURL(getactionid_manage().waitconversation_getRecordById);
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

            if(isNotEmpty(getRecordById_data)){
               var record_adjournbool=getRecordById_data.record_adjournbool;
                if (record_adjournbool==1||record_adjournbool=="1"){
                    //显示暂停按钮
                    $("#adjourn_btn").show();
                }

                getFDState();

                //获取默认的片头信息
                getptdjconst(1);

                setInterval(function () {
                    getFDState();
                }, 1000);


                setInterval(function () {
                    putRecessStatus();
                }, 10000);


                //获取刻录选时
                getBurnTime();
            }

            var record=data.record;
            if (isNotEmpty(record)){
                //获取提讯会议ssid
                mcbool=record.mcbool;

                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    var mtssiddata=police_arraignment.mtssid;
                    if (isNotEmpty(mtssiddata)){
                        mtssid=mtssiddata;

                        if ((!isNotEmpty(mcbool)||!(mcbool==1||mcbool==3))&&isNotEmpty(mtssiddata)){
                            //存在会议但是状态为空或者不等于1
                            $("#mtbool_txt").text("审讯已结束");
                            layer.confirm('审讯已结束', function(index){
                                //do something
                                overRecord();
                                layer.close(index);
                            });
                        }else if (null!=mcbool&&(mcbool==1||mcbool==3)){
                            //存在会议状态正常
                            $("#mtbool_txt").text("审讯中");
                            getRecordrealing();
                        }else {
                            layui.use(['layer','element','form'], function(){
                                var layer=layui.layer;
                                var layer=layui.layer;
                                layer.tips('点击将开启场景模板对应的设备，进行制作' ,'#pauserecord',{time:0, tips: 2});
                            });
                        }



                    }else if (mcbool!=1&&!isNotEmpty(mtssid)) {
                        layer.confirm('是否直接开启审讯', {
                            btn: ['确认','取消'], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                        }, function(index){
                            $("#mtbool_txt").text("审讯启动中");
                            startMC();
                            $("#startbtn").css({"visibility":"hidden"});
                            layer.close(index);
                        }, function(index){
                            $("#startbtn").css({"visibility":"visible"});
                            layer.close(index);
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
                    dq_recorduser=recordUserInfosdata.userssid;//当前被审讯人
                }
                var case_=record.case_;
                if (isNotEmpty(case_)){
                    casebool=case_.casebool==null?"":case_.casebool;
                }
            }
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
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
            play_x=1.99;
            initplayer();
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
}

var mtssid=null;//会议ssid
var useretlist=null;

var startMC_index=null;
function startMC() {
    $("#startbtn").attr("onclick","");
     startMC_index = layer.msg("加载中，请稍等...", {
        icon: 16,
        time:100000
    });
    if (isNotEmpty(getRecordById_data)){
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



            setTimeout(function () {
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
            },1000)
        }
    }else {
        layer.msg("请稍等",{time:1000,icon:5},function () {
            getRecordById();
            $("#startbtn").attr("onclick","startMC();")
        });
    }
}
function callbackstartMC(data) {
    if (isNotEmpty(startMC_index)){
        layer.close(startMC_index);
    }
    if(null!=data&&data.actioncode=='SUCCESS'){
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
                    }
                }

                play_x=1.99;
                initplayer();//初始化地址
            }
            mtssid=mtssiddata;
            mcbool=1;//正常开启
            $("#mtbool_txt").text("审讯中");

            var con="审讯已开启";
            layer.msg(con, {time: 2000,icon:6});
            $("#startbtn").css({"visibility":"hidden"}).attr("onclick","");
        }
    }else{
        var data2=data.data;
        if (isNotEmpty(data2)){
            var checkStartRecordVO=data2.checkStartRecordVO;
            if (null!=checkStartRecordVO){
                var msg=checkStartRecordVO.msg;
                parent.layer.confirm("审讯开启失败(<span style='color:red'>"+msg+"</span>)，请先结束正在进行中的审讯", {
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
        $("#startbtn").css({"visibility":"visible"}).attr("onclick","startMC();");

        layer.msg("审讯开启失败",{icon:5});
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

    layer.confirm(msgtxt2+'<br>'+msgtxt, {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        if (isNotEmpty(recordssid)){
            var url=getActionURL(getactionid_manage().waitconversation_addRecord);
            //需要收拾数据
            var recordToProblems=[];//题目集合
            if (state==1){
                casebool=3;//需要暂停
            }
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid: recordssid,
                    recordbool:2,//关闭
                    recordToProblems:recordToProblems,
                    casebool:casebool,
                    mtssid:mtssid //会议ssid用于审讯结束时关闭会议
                }
            };
            $("#overRecord_btn").attr("click","");

            ajaxSubmitByJson(url, data, calladdRecord);
        }else{
            layer.msg("系统异常",{icon:5});
        }
        overRecord_loadindex = layer.msg("保存中，请稍等...", {
            typy:1,
            icon: 16,
            shade: [0.1, 'transparent']
        });
    }, function(index){
        layer.close(index);
    });
}


function calladdRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            if (isNotEmpty(overRecord_loadindex)) {
                layer.close(overRecord_loadindex);
            }
            layer.msg("审讯结束",{time:500,icon:6},function () {
                window.history.go(-1);
            })
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
    $("#overRecord_btn").attr("click","overRecord();");
}

$(function () {
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
    },1000);
})

