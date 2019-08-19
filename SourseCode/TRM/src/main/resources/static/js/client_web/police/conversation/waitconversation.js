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
                getFDState();

                //获取默认的片头信息
                getptdjconst(1);

                setInterval(function () {
                    getFDState();
                }, 10000);
            }

            var record=data.record;
            if (isNotEmpty(record)){
                //获取提讯会议ssid
                mcbool=record.mcbool;
                if (mcbool==1){
                    $("#mtbool_txt").text("审讯中");
                }else {
                    $("#mtbool_txt").text("未审讯");
                }

                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    var mtssiddata=police_arraignment.mtssid;
                    if (isNotEmpty(mtssiddata)){
                        mtssid=mtssiddata;
                        getRecordrealing();
                    }else if (mcbool!=1&&!isNotEmpty(mtssid)) {
                        startMC();
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

            }
        }
    }else{
        layer.msg(data.message);
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
            initplayer();
        }
    }else{
        layer.msg(data.message);
    }
}



var mtssid=null;//会议ssid
var useretlist=null;

var startMC_index=null;
function startMC() {
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
        layer.msg("请稍等",{time:1000},function () {
            getRecordById();
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
                initplayer();//初始化地址
            }
            mtssid=mtssiddata;
            mcbool=1;//正常开启
            $("#mtbool_txt").text("审讯中");

            var con="审讯已开启<br>设备录音数："+recordnum;
            layer.msg(con, {time: 2000});
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
        layer.msg("审讯开启失败");
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
function overRecord() {
    layer.confirm('是否结束审讯', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        if (isNotEmpty(recordssid)){
            var url=getActionURL(getactionid_manage().waitconversation_addRecord);
            //需要收拾数据
            var recordToProblems=[];//题目集合
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid: recordssid,
                    recordbool:2,//关闭
                    recordToProblems:recordToProblems,
                    mtssid:mtssid //会议ssid用于审讯结束时关闭会议
                }
            };
            $("#overRecord_btn").attr("click","");
            ajaxSubmitByJson(url, data, calladdRecord);
        }else{
            layer.msg("系统异常");
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
            layer.msg("审讯结束",{time:500},function () {
                window.history.go(-1);
            })
        }
    }else{
        layer.msg(data.message);
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

function zanshimsg() {
    layer.msg("该功能暂时维护中...",{icon: 4});
}
