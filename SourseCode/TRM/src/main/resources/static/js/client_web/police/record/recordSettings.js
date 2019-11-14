var layer;
var recordSet_index;
var recordCaseInfo_index;
var yuntaikz_index;
var diskrec_continuettime;
var inOrOut = 0;
var kelumoshi = 1; //刻录模式开光
var PtFormHTML = "";
var ptjsonArr = [];
var ptjsonValues = [];
var setintervalKey;
var outcang;
var ptdjct;
var fdtype = "FD_AVST";
var fdStateInfo;
var CDNumModel_index;
var getCDNumberMsg;


//使用模块
var html=
    '<div class="layui-form layui-row" style="margin-left: 15px;margin-top: 8px;">\n' +
    '    <div style="z-index: 88;">\n' +
    '        <div class="layui-inline">\n' +
    '            <label class="layui-form-label" style="width: auto;padding-left: 0;">刻录选时</label>\n' +
    '            <div class="layui-input-inline" style="width: 100px;">\n' +
    '                <select name="burntime" id="burntime" lay-filter="burntime">\n' +
    '                    <option value="1">1小时</option>\n' +
    '                    <option value="4">4小时</option>\n' +
    '                    <option value="8">8小时</option>\n' +
    '                    <option value="12">12小时</option>\n' +
    '                    <option value="16">16小时</option>\n' +
    '                    <option value="20">20小时</option>\n' +
    '                    <option value="24">24小时</option>\n' +
    '                </select>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '    <div style="margin-bottom: 20px;">\n' +
    '        <input type="button" class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="getdvdOutOrIn(this)" value="光盘出仓" />\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" id="kelumoshi" onclick="changeBurnMode();">刻录模式</button>\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="getstartRec_Rom();">光盘刻录</button>\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;padding: 0 4px;" onclick="getstopRec_Rom();">停止光盘刻录</button>\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="getCDNumber();">光盘序号</button>\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="addptdj(true);">信息叠加</button>\n' +
    '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="getptdjconst();">案件信息</button>\n' +
    '    </div>\n' +
    '</div>';

function recordSet() {
//getstopRec_Rom()光盘结束   getstartRec_Rom()光盘开始
    recordSet_index = layer.open({
        type: 1,
        id: "layer_recordSet",
        title: '设备操作',
        closeBtn: 1,
        skin: 'layui-layer-lan',
        shade: 0,
        offset: ['130px', '450px'],
        area: ['450px', '360px'],
        shadeClose: true,
        content: html,
        success: function (layero, index) {
            // layer.min(index);
            //获取刻录选时
            getBurnTime();
            layui.use('form', function() {
                var form = layui.form;
                form.render();
            });
        }
    });

}

function recordCaseInfo() {

    //使用模块
    var html= '<form id="caseInfoModelYes" name="caseInfoModelYes" class="layui-form layui-main site-inline" action="" style="margin-top: 30px;width: 800px;">\n' +
        PtFormHTML +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">叠加时间</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <select name="ptdjct" id="ptdjct">\n' +
        '                        <option value="5">5秒</option>\n' +
        '                        <option value="15">15秒</option>\n' +
        '                        <option value="30">30秒</option>\n' +
        '                        <option value="60">60秒</option>\n' +
        '                        <option value="90">90秒</option>\n' +
        '                        <option value="120">120秒</option>\n' +
        '                    </select>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="button"  class="layui-btn layui-btn-normal" onclick="addptdj();" value="叠加片头" />\n' +
        '                    <button class="layui-btn layui-btn-normal" onclick="layer.close(recordCaseInfo_index);return false;">关闭</button>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </form>';

    recordCaseInfo_index = layer.open({
        type: 1,
        id: "layer_recordCaseInfo",
        title: '片头叠加',
        closeBtn: 1,
        skin: 'layui-layer-lan',
        shade: 0,
        offset: 'auto',
        area: ['900px', 'auto'],
        shadeClose: true,
        content: html,
        success: function (layero, index) {
            // layer.min(index);
            layui.use('form', function() {
                var form = layui.form;
                form.render();
            });
        }
    });

}


function yuntaikz() {

    //使用模块
    var yuntaikzHtml=
        '<div class="layui-form layui-row" style="margin-left: 15px;margin-top: 8px;">\n' +
        '    <div style="margin-bottom: 20px;z-index: 88;">\n' +
        '        <div class="layui-inline">\n' +
        '            <label class="layui-form-label" style="width: auto;">通道1</label>\n' +
        '            <div class="layui-input-inline" style="width: 60px;">\n' +
        '                <select name="ptzch" id="ptzch">\n' +
        '                    <option value="1">1</option>\n' +
        '                    <option value="2">2</option>\n' +
        '                    <option value="3">3</option>\n' +
        '                    <option value="4">4</option>\n' +
        '                    <option value="5">5</option>\n' +
        '                    <option value="6">6</option>\n' +
        '                    <option value="7">7</option>\n' +
        '                    <option value="8">8</option>\n' +
        '                </select>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '\n' +
        '    <div style="float: left;margin-right: 50px;">\n' +
        '        <div style="padding-left: 55px;">\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2" onclick="getyuntaiControl(\'up\');">&#xeb9b;</button>\n' +
        '        </div>\n' +
        '        <div class="layui-btn-group">\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2" onclick="getyuntaiControl(\'left\');">&#xe604;</button>\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2" onclick="getyuntaiControl(\'down\');">&#xeb9c;</button>\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2" onclick="getyuntaiControl(\'right\');">&#xeb9d;</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '\n' +
        '    <div style="float: left;">\n' +
        '        <div class="layui-form-item" style="margin-bottom: 5px;">\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" onclick="getyuntaiControl(\'focus_decrease\');" >&#xe611;</button>\n' +
        '            <label class="layui-form-label" style="width: auto;float: left;">聚焦</label>\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" onclick="getyuntaiControl(\'focus_increase\');" >&#xe638;</button>\n' +
        '        </div>\n' +
        '\n' +
        '        <div class="layui-form-item" style="margin-bottom: 5px;">\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" onclick="getyuntaiControl(\'depth_near\');" >&#xe611;</button>\n' +
        '            <label class="layui-form-label" style="width: auto;float: left;">镜头</label>\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" onclick="getyuntaiControl(\'depth_far\');" >&#xe638;</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>';



    yuntaikz_index = layer.open({
        type: 1,
        id: "layer_yuntaikz",
        title: '云台控制',
        closeBtn: 1,
        skin: 'layui-layer-lan',
        shade: 0,
        offset: ['310px', '490px'],
        area: ['420px', '300px'],
        content: yuntaikzHtml,
        success: function (layero, index) {
            // layer.min(index);
            layui.use('form', function() {
                var form = layui.form;
                form.render();
            });
        }
    });

}

//片头叠加
function addptdj(str) {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_ptdj);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_ptdj);
    }
    // var url = "/cweb/police/record/ptdj";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法片头叠加", {icon: 5});
        return;
    }

    if(!isNotEmpty(str)){
        ptjsonValues = [];

        var params = getFormData("caseInfoModelYes");

        for (var i = 0; i < params.length; i++) {
            if (ptjsonArr.length > i) {
                ptjsonValues.push(params[i]["value"])
            }
        }
    }

    ptdjct = $("#ptdjct").val();
    if(!isNotEmpty(ptdjct)){
        ptdjct = "10";
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            ct: ptdjct,
            lineList: ptjsonValues,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callptdj);
}

//获取设备状态
function getFDState() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getFDState);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getFDState);
    }
    // var url = "/cweb/police/record/getFDState";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取设备状态", {icon: 5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callFDState);
}

//获取刻录选时
function getBurnTime() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getBurnTime);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getBurnTime);
    }
    // var url = "/cweb/police/record/getBurnTime";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取设备状态", {icon: 5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetBurnTime);
}

//修改刻录选时
function updateBurnTime() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_updateBurnTime);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_updateBurnTime);
    }
    // var url = "/cweb/police/record/updateBurnTime";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取设备状态", {icon: 5});
        return;
    }

    var burntime = $("#burntime").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            burntime: burntime,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callBase);
}


//设备录像重点标记
function viewKeyMark() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_viewKeyMark);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_viewKeyMark);
    }

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法设置录像重点标记", {icon: 5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);
}

//刻录模式选择
function changeBurnMode(num) {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_changeBurnMode);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_changeBurnMode);
    }
    // var url = "/cweb/police/record/changeBurnMode";

    if (kelumoshi == 0) {
        return ;
    }

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取设备状态", {icon: 5});
        return;
    }

    var burn_mode = 0;
    if (fdStateInfo.burn_mode == 0) {
        burn_mode = 2;
    }

    //单机版调的单选框，如果有传入指定的刻录模式就按照指定的来切换
    if(isNotEmpty(num)){
        burn_mode = num;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            dx:burn_mode,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callBase);
}

//光盘序号
function getCDNumber() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getCDNumber);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getCDNumber);
    }
    // var url = "/cweb/police/record/getCDNumber";

    if (fdStateInfo.roma_disktype != 10 && fdStateInfo.romb_disktype != 10) {
        layer.msg("光盘状态，当前不是可播放的",{icon: 5});
        return;
    }

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取设备状态", {icon: 5});
        return;
    }

    getCDNumberMsg = layer.msg("加载中，请稍等...", {
        icon: 16,
        time:10000
    });

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callCDNumber);
}

//获取当前配置片头字段
function getptdjconst(qidong) {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getptdjconst);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getptdjconst);
    }
    // var url = "/cweb/police/record/getptdjconst";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法获取当前配置片头字段", {icon: 5});
        // layer.msg("笔录尚未开始，无法打开案件信息",{icon: 2});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    if (!isNotEmpty(qidong)) {
        ajaxSubmitByJson(url,data,callgetptdjconst);
    }else{
        ajaxSubmitByJson(url,data,callgetbiluptdjconst);
    }
}

//光盘出仓/进仓
function getdvdOutOrIn(obj) {
    var strtitle = "笔录";
    url = getActionURL(getactionid_manage().waitRecord_getdvdOutOrIn);
    if (!isNotEmpty(url)) {
        strtitle = "审讯";
        var url = getActionURL(getactionid_manage().waitconversation_getdvdOutOrIn);
    }
    // var url = "/cweb/police/record/getdvdOutOrIn";

    if (!isNotEmpty(getRecordById_data)) {
        layer.msg(strtitle + "尚未开始，无法进行光盘出仓/进仓", {icon: 5});
        return;
    }

    // clearTimeout(outcang);

    // $("#cd_hint").show();
    // $("#cd2_hint").show();
    //1进仓，2出仓
    // if (inOrOut == 2) {
    //     inOrOut = 1;
    //     $(obj).val("光盘出仓");
    //     $("#cd_bin").html("进仓");
    //     $("#cd2_bin").html("进仓");
    // } else {
        inOrOut = 2;
    //     $(obj).val("光盘进仓");
    //     $("#cd_bin").html("出仓");
    //     $("#cd2_bin").html("出仓");
    // }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            fdType: fdtype,
            inOrOut: inOrOut,
            flushbonadingetinfossid: getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url, data, callgetdvdOutOrIn);

    // outcang = setTimeout(function () {
    //     $("#cd_hint").hide();
    //     $("#cd2_hint").hide();
    // }, 20000);
}


//开始光盘刻录
function getstartRec_Rom() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getstartRec_Rom);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getstartRec_Rom);
    }
    // var url = "/cweb/police/record/getstartRec_Rom";

    if(!isNotEmpty(getRecordById_data) || !isNotEmpty(mtssid)){
        layer.msg(strtitle + "尚未开始，无法开始光盘刻录", {icon: 5});
        return;
    }
    if(!isNotEmpty(mtssid)){
        layer.msg("会议ssid为空或没开始笔录，无法开始光盘刻录",{icon: 5});
        return;
    }

    var burntime = $("#burntime").val();

    var bmode = "bmode";
    if(fdStateInfo.burn_mode == 2){
        bmode = "exchange";
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            iid: mtssid,
            burntime: burntime,
            bmode: bmode,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);
    $("#kelumoshi").removeClass("layui-btn-normal").addClass(" layui-btn-disabled");
    kelumoshi = 0;
}

//结束光盘刻录
function getstopRec_Rom() {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getstopRec_Rom);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getstopRec_Rom);
    }
    // var url = "/cweb/police/record/getstopRec_Rom";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法结束光盘刻录", {icon:5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);
    $("#kelumoshi").removeClass("layui-btn-disabled").addClass(" layui-btn-normal");
    kelumoshi = 1;
}

//云台控制
function getyuntaiControl(ptzaction) {
    var strtitle = "笔录";
    var url=getActionURL(getactionid_manage().waitRecord_getyuntaiControl);
    if(!isNotEmpty(url)){
        strtitle = "审讯";
        url=getActionURL(getactionid_manage().waitconversation_getyuntaiControl);
    }
    // var url = "/cweb/police/record/getyuntaiControl";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg(strtitle + "尚未开始，无法进行云台控制", {icon:5});
        return;
    }

    //up、down、left、right、focus_increase(聚焦+）、 focus_decrease(聚焦-)、depth_far（倍变+）、depth_near（倍 变-）、stop

    var ptzch = $("#ptzch").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            ptzaction: ptzaction,
            ptzch: ptzch,
            flushbonadingetinfossid: getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);
}
//上报休庭心跳
function putRecessStatus() {
    if (isNotEmpty(recordssid) && isNotEmpty(mtssid)) {
        var url=getActionURL(getactionid_manage().waitRecord_putRecessStatus);
        if(!isNotEmpty(url)){
            url=getActionURL(getactionid_manage().waitconversation_putRecessStatus);
        }
        // var url = "/cweb/police/record/putRecessStatus";


        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callputRecessStatus);
    }else{
        console.log("笔录ssid和会议ssid为空，无法提交暂停检测心跳");
        // layer.msg("笔录ssid和会议ssid为空，无法提交暂停检测心跳",{icon: 5});
    }
}

/**
 * 获得 设备现场的音频振幅
 */
function getFDAudPowerMap(flushbonadingetinfossid) {

    if(!isNotEmpty(flushbonadingetinfossid)){
        if(!isNotEmpty(getRecordById_data.modeltds[0].fdssid)){
            layer.msg("必须开启会议才可以请求波形");
            return;
        }
        flushbonadingetinfossid = getRecordById_data.modeltds[0].fdssid;
    }

    var url=getActionURL(getactionid_manage().waitRecord_getFDAudPowerMap);

    if (!isNotEmpty(url)) {
        url=getActionURL(getactionid_manage().waitconversation_getFDAudPowerMap);
        // url = "/cweb/police/record/getFDAudPowerMap";
    }
    if (!isNotEmpty(url)) {
        url=getActionURL(getactionid_manage().homepage_getFDAudPowerMap);
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            flushbonadingetinfossid:flushbonadingetinfossid
        }
    };
    ajaxSubmitByJson(url,data,callFDAudPowerMap);
}
function callFDAudPowerMap(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            var data=data.data;
            if (isNotEmpty(data)){
                var list=data.audpowList;
                if (isNotEmpty(list)) {
                    var audpowHTML = "";
                    var audpowHTML2 = "";

                    $("#bars").html("");
                    $("#bars2").html("");
                    for (var i = 0; i < list.length; i++) {
                        var audpow = list[i];
                        audpowHTML += '<div class="bar"><div style="height: ' + audpow + '%;"></div></div>\n';
                        audpowHTML2 += '<div class="bar2"><div style="height: ' + audpow + '%;"></div></div>\n';
                    }
                    $("#bars").html(audpowHTML);
                    $("#bars2").html(audpowHTML2);
                    // layui.form.render();
                }
            }

        }

    }else{
        // layer.msg(data.message,{icon: 5});
    }
    // layui.use('form', function(){
    //     var form = layui.form;
    //     form.render();
    // });
}

function callputRecessStatus(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {

    }else{
        //layer.msg(data.message);
    }
}


function callptdj(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("片头叠加成功",{icon: 6});
            layer.close(recordCaseInfo_index);
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
}

function callgetBurnTime(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var flushbonadinginfo = data.data;
            var burntime = flushbonadinginfo.burntime;
            $("#burntime").val(burntime);
            layui.use('form', function(){
                var form = layui.form;
                form.render();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callCDNumber(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            var cdNumList = data.data.cdNumList;
            var conTop = "<table border=\"0\" cellpadding=\"5\" style='width: 100%;margin-top: 8px;'>";
            var conBottom = "</table>";
            var con = "";
            for (var i = 0; i < cdNumList.length; i++) {
                var Disc_iid = cdNumList[i];
                var rs = Disc_iid.rs;

                if (rs == 1) {
                    var crc32 = Disc_iid.crc32;
                    var md5 = Disc_iid.md5;
                    var iid = Disc_iid.iid;

                    var cdnum = Disc_iid.cdnum;
                    if (cdnum == 0) {
                        cdnum = "光驱1";
                    }else{
                        cdnum = "光驱2";
                    }

                    con += "<tr>\n" +
                        "    <th colspan=\"2\" class='detection'>【" + cdnum + "】</th>\n" +
                        "  </tr>\n" +
                        "<tr>\n" +
                        "       <td style='text-align: right;' class=\"detection\">内容CRC校验码：</td>\n" +
                        "       <td >" + crc32 + "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "       <td style='text-align: right;' class=\"detection\">光盘编号：</td>\n" +
                        "       <td >" + iid + "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "       <td style='text-align: right;padding-bottom: 10px;' class=\"detection\">哈希值：</td>\n" +
                        "       <td style='padding-bottom: 10px;'>" + md5 + "</td>\n" +
                        "</tr>\n";
                }
            }

            con = conTop + con + conBottom;

            if (con != "") {
                layer.close(getCDNumberMsg);

                //弹窗层
                CDNumModel_index = layer.open({
                    type: 1,
                    closeBtn: 1,
                    skin: 'layui-layer-lan',
                    shade: 0,
                    title: "光盘序号",
                    area: ['500px', 'auto'], //宽高
                    shadeClose: true,
                    content: con
                });


            }else{
                layer.msg("暂时未找到",{icon: 6});
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callBase(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功",{icon: 6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function getptdjinfo() {
    if (!isNotEmpty(ptdjct)) {
        ptdjct = 90;
    }
    return {
        fdType: fdtype, //FD_AVST
        ct: parseInt(ptdjct), //显示时间
        lineList: ptjsonValues,  //显示内容
        flushbonadingetinfossid: getRecordById_data.modeltds[0].fdssid   //设备ssid
    };
}

function callgetdvdOutOrIn(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功",{icon: 6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callgetptdjconst(data){
    // console.log("==================================");
    // console.log(data);
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)) {
            var ptdjmaps = data.data.ptdjmaps;
            ptjsonArr = data.data.ptdjtitles;

            PtFormHTML = "";

            for (var i = 0; i < ptjsonArr.length; i++) {
                var ptjsonName = ptjsonArr[i];
                var ptjsonValue = "";
                if (isNotEmpty(ptdjmaps)) {
                    ptjsonValue = matchPtdjKey(ptdjmaps[ptjsonName]);
                }

                ptjsonValue=ptjsonValue==null?"":ptjsonValue;
                PtFormHTML += '<div class="layui-form-item">\n' +
                    '   <label class="layui-form-label">' + ptjsonName + '</label>\n' +
                    '       <div class="layui-input-block">\n' +
                    '       <input type="text" lay-verify="required" value="' + ptjsonValue + '" placeholder="请输入' + ptjsonName + '" autocomplete="off" class="layui-input">\n' +
                    '   </div>\n' +
                    '</div>';
            }
        }

        recordCaseInfo();
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callgetbiluptdjconst(data){
    // console.log("==================================");
    // console.log(data);
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)) {
            var ptdjmaps = data.data.ptdjmaps;
            ptjsonArr = data.data.ptdjtitles;
            for (var i = 0; i < ptjsonArr.length; i++) {
                var ptjsonName = ptjsonArr[i];
                var ptjsonValue = "";
                if (isNotEmpty(ptdjmaps)) {
                    ptjsonValue = matchPtdjKey(ptdjmaps[ptjsonName]);
                }
                ptjsonValue=ptjsonValue==null?"":ptjsonValue;
                ptjsonValues.push(ptjsonValue);
            }
        }
    }else{
        // layer.msg(data.message,{icon: 2});
    }
}

//实时设备刻录状态显示
function callFDState(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)) {
            // console.log(data.data);

            // clearTimeout(setintervalKey);

            fdStateInfo = data.data;


            if (fdStateInfo.dvdnum >= 1) {
                //光驱1
                $("#guangqu").show();
                $(".guangpan").show();
                $("#dvd1").show();

                var statusName = fdStateInfo.roma_status;
                if (statusName >= 1 && statusName <= 14) {
                    statusName = getRomStatus(fdStateInfo.roma_status);
                } else {
                    statusName = getDisktype(fdStateInfo.roma_disktype);
                }

                $("#roma_status").html(statusName);//光盘状态
                if(statusName == "刻录中"){
                    $("#roma_status").css("color", "red");
                }else{
                    $("#roma_status").css("color", "#2c5572");
                }
                $("#roma_begintime").html(fdStateInfo.roma_lefttime != 0 ? fdStateInfo.roma_begintime : "00:00:00");//开始直刻的时间
                $("#roma_lefttime").html(fdStateInfo.roma_lefttime == 0 ? "00:00:00" : changeTimes(fdStateInfo.roma_lefttime));//直刻剩余的倒计时
                $("#roma_setburntime").html(fdStateInfo.roma_setburntime == 0 ? "无" : fdStateInfo.roma_lefttime != 0 ? fdStateInfo.roma_setburntime + "小时" : "无");//刻录时间
            }

            if (fdStateInfo.dvdnum >= 2) {
                //光驱2
                $("#guangqu").show();
                $(".guangpan").show();
                $("#dvd2").show();

                var statusName = fdStateInfo.romb_status;
                if (statusName >= 1 && statusName <= 14) {
                    statusName = getRomStatus(fdStateInfo.romb_status);
                } else {
                    statusName = getDisktype(fdStateInfo.romb_disktype);
                }

                $("#romb_status").html(statusName);//光盘状态
                if(statusName == "刻录中"){
                    $("#romb_status").css("color", "red");
                }else{
                    $("#romb_status").css("color", "#2c5572");
                }
                $("#romb_begintime").html(fdStateInfo.romb_lefttime != 0 ? fdStateInfo.romb_begintime : "00:00:00");//开始直刻的时间
                $("#romb_lefttime").html(fdStateInfo.romb_lefttime == 0 ? "00:00:00" : changeTimes(fdStateInfo.romb_lefttime));//直刻剩余的倒计时
                $("#romb_setburntime").html(fdStateInfo.romb_setburntime == 0 ? "无" : fdStateInfo.romb_lefttime != 0 ? fdStateInfo.romb_setburntime + "小时" : "无");//刻录时间
            }

            $("#diskrec_isrec").html(fdStateInfo.diskrec_isrec == 1 ? "<span style='color: red;'>录像中</span>" : "未录像");//是否正在本地硬盘录像

            $("#disk_totalspace").html(getfilesize(fdStateInfo.disk_totalspace + 100000));//硬盘总容量
            $("#disk_freespace").html(baifnebi(fdStateInfo.disk_freespace, fdStateInfo.disk_totalspace) + "%");//硬盘剩余容量

            $("#diskrec_begintime").html(fdStateInfo.diskrec_begintime);//本地硬盘开始录像时间

            $("#bsettimerrecburntime").html(fdStateInfo.selburntime == null ? "1小时" : changeTimes(fdStateInfo.selburntime, 1) + "小时");//刻录选时
            $("#burn_mode").html(getBurnMode(fdStateInfo.burn_mode));//当前刻录模式 0:直刻模式 1:硬盘导刻模式 2:接力刻录模式

            if(fdStateInfo.diskrec_isrec == 1){
                diskrec_continuettime = fdStateInfo.diskrec_continuettime;
                jisuantime();
                // setintervalKey = setInterval(function () {
                //     jisuantime();
                // }, 1000);
            }else{
                $("#diskrec_continuettime").html("00:00");//硬盘录像持续时间
            }

            if(isNotEmpty(getRecordById_data) && fdStateInfo.roma_status == 1 || fdStateInfo.romb_status == 1){
                $("#kelumoshi").removeClass("layui-btn-normal").addClass(" layui-btn-disabled");
                kelumoshi = 0;
            }else {
                $("#kelumoshi").removeClass("layui-btn-disabled").addClass(" layui-btn-normal");
                kelumoshi = 1;
            }


            /** 单机版开始 **/
            var residueNum = 0;
            if (fdStateInfo.disk_freespace > 0 && fdStateInfo.disk_totalspace > 0) {
                residueNum = baifnebi(fdStateInfo.disk_freespace, fdStateInfo.disk_totalspace);
            }

            $("#cp_freespace").css("width", residueNum + "%");//硬盘剩余容量进度条
            $("#cp_freespace span").html(residueNum + "%");//硬盘剩余容量文字显示
            $("#cp_tag").html(getfilesize(fdStateInfo.disk_freespace + 100000) + "可用 / 共" + getfilesize(fdStateInfo.disk_totalspace + 100000));//硬盘总容量

            $("#cp_isrec").html(fdStateInfo.diskrec_isrec == 1 ? "录像中" : "未开始");//是否正在本地硬盘录像



            var roma_Num = 0;
            if (fdStateInfo.roma_discCapUsed > 0 && fdStateInfo.roma_discCap > 0) {
                roma_Num = 100 - baifnebi(fdStateInfo.roma_discCapUsed, fdStateInfo.roma_discCap);
            }

            $("#roma_freespace").css("width", roma_Num + "%");//硬盘剩余容量进度条
            $("#roma_freespace span").html(roma_Num + "%");//硬盘剩余容量文字显示
            var roma_discCapUsed = fdStateInfo.roma_discCap - fdStateInfo.roma_discCapUsed;
            roma_discCapUsed = roma_discCapUsed ? getfilesize(roma_discCapUsed) : 0;
            $("#roma_tag").html(roma_discCapUsed + "可用 / 共" + getfilesize(fdStateInfo.roma_discCap));//硬盘总容量


            var romb_Num = 0;
            if (fdStateInfo.romb_discCapUsed > 0 && fdStateInfo.romb_discCap > 0) {
                romb_Num = 100 - baifnebi(fdStateInfo.romb_discCapUsed, fdStateInfo.romb_discCap);
            }

            $("#romb_freespace").css("width", romb_Num + "%");//硬盘剩余容量进度条
            $("#romb_freespace span").html(romb_Num + "%");//硬盘剩余容量文字显示
            var romb_discCapUsed = fdStateInfo.romb_discCap - fdStateInfo.romb_discCapUsed;
            romb_discCapUsed = romb_discCapUsed ? getfilesize(romb_discCapUsed) : 0;
            $("#romb_tag").html(romb_discCapUsed + "可用 / 共" + getfilesize(fdStateInfo.romb_discCap));//硬盘总容量

            if(fdStateInfo.roma_isburn == 1){
                $("#roma_state").html('<i class="layui-badge-dot layui-bg-green" style="background-color: #5FB878!important;margin-right: 5px;width: 10px;height: 10px;"></i>已开始');
            }else{
                $("#roma_state").html('<i class="layui-badge-dot off-dot" style="margin-right: 5px;width: 10px;height: 10px;"></i>未开始');
            }

            if(fdStateInfo.romb_isburn == 1){
                $("#romb_state").html('<i class="layui-badge-dot layui-bg-green" style="background-color: #5FB878!important;margin-right: 5px;width: 10px;height: 10px;"></i>已开始');
            }else{
                $("#romb_state").html('<i class="layui-badge-dot off-dot" style="margin-right: 5px;width:10px;height: 10px;"></i>未开始');
            }

            if(fdStateInfo.roma_isburn == 1 || fdStateInfo.romb_isburn == 1){
                $("#cp_isburn").html("已开始");
            }else{
                $("#cp_isburn").html("未开始");
            }




            //fdStateInfo.burn_mode
            if (isNotEmpty(fdStateInfo.burn_mode) && fdStateInfo.burn_mode == 2) {
                // $("#burn_mode_a").prop("checked", false);
                // $("#burn_mode_b").prop("checked", true);
                $("#burn_mode_a").removeAttr("checked");
                if (!$("#burn_mode_b").attr("checked") || !$("#burn_mode_b").prop("checked")) {
                    // $("#burn_mode_b").attr("checked", 'checked');

                    $("#burn_mode_b").prop("checked", true);
                }
            } else {
                // $("#burn_mode_b").prop("checked", false);
                // $("#burn_mode_a").prop("checked", true);
                $("#burn_mode_b").removeAttr("checked");
                if (!$("#burn_mode_a").attr("checked") || !$("#burn_mode_a").prop("checked")) {
                    // $("#burn_mode_a").attr("checked", 'checked');

                    $("#burn_mode_a").prop("checked", true);
                }
            }

            layui.use('form', function(){
                var form = layui.form;
                form.render('radio');
            });

        }
    }else{
        layer.msg(data.message,{icon:5});
    }
}

function zanshimsg() {
    layer.msg("该功能暂时维护中...",{icon: 4});
}

//转换百分比
function baifnebi(num, cout) {
    var yu = num / cout * 100;
    var sheng = 100 - yu;
    return Math.floor(sheng);
}

//计算时间
function jisuantime() {
    var oTs = diskrec_continuettime++;

    var timeHtml = "";
    var min = Math.floor(oTs % 3600);
    var h = Math.floor(oTs / 3600);
    var s = Math.floor(min / 60);
    var m = oTs % 60;

    if (s < 10) {
        s = "0" + s;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (h > 0 && h < 100) {
        timeHtml = h + ":" + s + ":" + m;
    }else{
        timeHtml = s + ":" + m;
    }

    $("#diskrec_continuettime").html(timeHtml);//硬盘录像持续时间
}

// 计算文件大小函数(保留两位小数),Size为字节大小
// size：初始文件大小
function getfilesize(size) {
    if (!size)
        return "";

    var num = 1024.00; //byte

    if (size < num)
        return size + "B";
    if (size < Math.pow(num, 2))
        return (size / num).toFixed(2) + "K"; //kb
    if (size < Math.pow(num, 3))
        return (size / Math.pow(num, 2)).toFixed(2) + "M"; //M
    if (size < Math.pow(num, 4))
        return (size / Math.pow(num, 3)).toFixed(2) + "G"; //G
    return (size / Math.pow(num, 4)).toFixed(2) + "T"; //T
}

function getBurnMode(mode) {

    var burn_mode = "";//光盘状态

    switch (mode) {
        case "1":
            burn_mode = "硬盘导刻模式";
            break;
        case "2":
            burn_mode = "接力刻录模式";
            break;
        default:
            burn_mode = "直刻模式";
    }
    return burn_mode;
}

function getDisktype(status) {

    var disktype = "";//光盘状态

    switch (status) {
        case "0":
            disktype = "空闲";
            break;
        case "1":
            disktype = "准备就绪（DVD+R）";
            break;
        case "2":
            disktype = "准备就绪（DVD-R）";
            break;
        case "3":
            disktype = "准备就绪（DVD+R DL）";
            break;
        case "4":
            disktype = "准备就绪（DVD-R DL）";
            break;
        case "5":
            disktype = "准备就绪（BuleRay DVD）";
            break;
        case "10":
            disktype = "可播放的";
            break;
        case "11":
            disktype = "已格式化可直 接刻录的";
            break;
        case "12":
            disktype = "错误的盘片";
            break;
        case "13":
            disktype = "无光盘";
            break;
        default:
            disktype = "未知的";
    }
    return disktype;

}

function getRomStatus(status) {

    var roma_status = "";//光盘状态

    switch (status) {
        case "0":
            roma_status = "空闲";
            break;
        case "1":
            roma_status = "刻录中";
            break;
        case "2":
            roma_status = "正在停止刻录";
            break;
        case "3":
            roma_status = "正在封盘";
            break;
        case "4":
            roma_status = "正在恢复光盘";
            break;
        case "5":
            roma_status = "正在格式化光盘";
            break;
        case "6":
            roma_status = "光盘读取中";
            break;
        case "7":
            roma_status = "正在开始刻录";
            break;
        case "8":
            roma_status = "正在开始格式化光盘";
            break;
        case "9":
            roma_status = "刻录中(数据)";
            break;
        case "10":
            roma_status = "正在停止刻录(数据)";
            break;
        case "11":
            roma_status = "正在封盘(数据)个别机型支持";
            break;
        case "12":
            roma_status = "刻录中(推 送刻录)";
            break;
        case "13":
            roma_status = "正在停止刻录(推送刻录)";
            break;
        case "14":
            roma_status = "正在封盘(推送刻录)";
            break;
        default:
            roma_status = "未知";
    }
    return roma_status;
}
//获取表单里的所有参数
function getFormData(eId) {
    var inData = new Array();
    $("#" + eId).find("input").each(function () {
        if ($(this).attr("real-value") != null) {
            inData.push({"name": $(this).attr("name"), "value": $(this).attr("real-value").trim()});
        } else {
            inData.push({"name": $(this).attr("name"), "value": $(this).val().trim()});
        }
    });
    $("#" + eId).find("select").each(function () {
        inData.push({"name": $(this).attr("name"), "value": $(this).val().trim()});
    });
    $("#" + eId).find("textarea").each(function () {
        inData.push({"name": $(this).attr("name"), "value": $(this).val().trim()});
    });
    return inData;
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//秒数转换时间
function changeTimes(num, status) {
    if(isNotEmpty(status)){
        return num / 3600;
    }

    var hh=Math.floor(num / 3600);
    var hh_="";
    var mm;
    var mm_="";
    var ss;
    var ss_="";
    mm=Math.floor(num % 3600/ 60);

    if(hh > 9){
        hh_=hh+"";
    }else if(hh > 0){
        hh_="0"+hh;
    }else{
        hh_="00";
    }
    if(mm > 9){
        mm_=mm+"";
    }else if(mm > 0){
        mm_="0"+mm;
    }else{
        mm_="00";
    }

    if(mm > 0){
        ss=num % 3600 % 60;
    }else{
        ss=num % 3600;
    }
    if(ss > 9){
        ss_=""+ss;
    }else if(ss > 0){
        ss_="0"+ss;
    }else{
        ss_="00";
    }

    return hh_+":"+mm_+":"+ss_;
}

function matchPtdjKey(key) {

    if("casenum" == key){
        return getRecordById_data.record.case_.casenum == null ? "" : getRecordById_data.record.case_.casenum;
    }
    if("casename" == key){
        return getRecordById_data.record.case_.casename == null ? "" : getRecordById_data.record.case_.casename;
    }
    if("recordtypename" == key){
        return getRecordById_data.record.recordtypename == null ? "" : getRecordById_data.record.recordtypename;
    }
    if("cause" == key){
        return getRecordById_data.record.case_.cause == null ? "" : getRecordById_data.record.case_.cause;
    }
    if("department" == key){
        return getRecordById_data.record.case_.department == null ? "" : getRecordById_data.record.case_.department;
    }
    if("username" == key){
        return getRecordById_data.record.recordUserInfos.username == null ? "" : getRecordById_data.record.recordUserInfos.username;
    }
    if("adminname" == key){
        return getRecordById_data.record.recordUserInfos.adminname == null ? "" : getRecordById_data.record.recordUserInfos.adminname;
    }
    if("recordadminname" == key){
        return getRecordById_data.record.recordUserInfos.recordadminname == null ? "" : getRecordById_data.record.recordUserInfos.recordadminname;
    }
    if("recordplace" == key){
        return getRecordById_data.record.police_arraignment.recordplace == null ? "" : getRecordById_data.record.police_arraignment.recordplace;
    }
    return "";
}