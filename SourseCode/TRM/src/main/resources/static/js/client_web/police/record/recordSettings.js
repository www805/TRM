var layer;
var recordSet_index;
var recordCaseInfo_index;
var yuntaikz_index;
var diskrec_continuettime;
var inOrOut = 0;
var PtFormHTML = "";
var ptjsonArr = [];
var ptjsonValues = [];
var setintervalKey;
var outcang;
var ptdjct;
var fdtype = "FD_AVST";

function recordSet() {

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
        '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="getptdjconst();">案件信息</button>\n' +
        '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="">光盘开始</button>\n' +
        '        <button class="layui-btn layui-btn-normal" style="margin-top: 10px;" onclick="">光盘结束</button>\n' +
        '    </div>\n' +
        '</div>';
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
    var html=
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

//片头叠加
function addptdj(str) {
    var url=getActionURL(getactionid_manage().waitconversation_ptdj);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_ptdj);
    }
    // var url = "/cweb/police/record/ptdj";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法片头叠加",{icon: 2});
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

    var url=getActionURL(getactionid_manage().waitconversation_getFDState);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getFDState);
    }
    // var url = "/cweb/police/record/getFDState";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法获取设备状态",{icon: 2});
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

//获取当前配置片头字段
function getptdjconst(qidong) {
    var url=getActionURL(getactionid_manage().waitconversation_getptdjconst);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getptdjconst);
    }
    // var url = "/cweb/police/record/getptdjconst";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法获取当前配置片头字段",{icon: 2});
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
    var url=getActionURL(getactionid_manage().waitconversation_getdvdOutOrIn);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getdvdOutOrIn);
    }
    // var url = "/cweb/police/record/getdvdOutOrIn";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法进行光盘出仓/进仓",{icon: 2});
        return;
    }

    clearTimeout(outcang);

    $("#cd_hint").show();
    $("#cd2_hint").show();
    //1进仓，2出仓
    if (inOrOut == 2) {
        inOrOut = 1;
        $(obj).val("光盘出仓");
        $("#cd_bin").html("进仓");
        $("#cd2_bin").html("进仓");
    }else{
        inOrOut = 2;
        $(obj).val("光盘进仓");
        $("#cd_bin").html("出仓");
        $("#cd2_bin").html("出仓");
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            inOrOut:inOrOut,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);

    outcang = setTimeout(function () {
        $("#cd_hint").hide();
        $("#cd2_hint").hide();
    }, 20000);
}

//开始光盘刻录
function getstartRec_Rom() {
    var url=getActionURL(getactionid_manage().waitconversation_getstartRec_Rom);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getstartRec_Rom);
    }
    // var url = "/cweb/police/record/getstartRec_Rom";

    if(!isNotEmpty(getRecordById_data) || !isNotEmpty(getRecordById_data.record.police_arraignment.mtssid)){
        layer.msg("笔录尚未开始，无法开始光盘刻录",{icon: 2});
        return;
    }
    if(!isNotEmpty(getRecordById_data.record.police_arraignment.mtssid)){
        layer.msg("会议ssid为空或没开始笔录，无法开始光盘刻录",{icon: 2});
        return;
    }

    var burntime = $("#burntime").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            fdType: fdtype,
            iid: getRecordById_data.record.police_arraignment.mtssid,
            burntime: burntime,
            flushbonadingetinfossid:getRecordById_data.modeltds[0].fdssid
        }
    };
    ajaxSubmitByJson(url,data,callgetdvdOutOrIn);
}

//结束光盘刻录
function getstopRec_Rom() {
    var url=getActionURL(getactionid_manage().waitconversation_getstopRec_Rom);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getstopRec_Rom);
    }
    // var url = "/cweb/police/record/getstopRec_Rom";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法结束光盘刻录",{icon: 2});
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

//云台控制
function getyuntaiControl(ptzaction) {
    var url=getActionURL(getactionid_manage().waitconversation_getyuntaiControl);
    if(!isNotEmpty(url)){
        url=getActionURL(getactionid_manage().waitRecord_getyuntaiControl);
    }
    // var url = "/cweb/police/record/getyuntaiControl";

    if(!isNotEmpty(getRecordById_data)){
        layer.msg("笔录尚未开始，无法进行云台控制",{icon: 2});
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

function callptdj(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("片头叠加成功",{icon: 1});
            layer.close(recordCaseInfo_index);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function getptdjinfo() {
    if (!isNotEmpty(ptdjct)) {
        ptdjct = 90;
    }
    return {
        fdType: fdtype,
        ct: parseInt(ptdjct),
        lineList: ptjsonValues,
        flushbonadingetinfossid: getRecordById_data.modeltds[0].fdssid
    };
}

function callgetdvdOutOrIn(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功",{icon: 1});
        }
    }else{
        layer.msg(data.message,{icon: 2});
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
        layer.msg(data.message,{icon: 2});
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

            clearTimeout(setintervalKey);

            var fdStateInfo = data.data;
            //光驱1
            $("#roma_status").html(getRomStatus(fdStateInfo.roma_status));//光盘状态
            $("#roma_begintime").html(fdStateInfo.roma_begintime);//开始直刻的时间
            $("#roma_lefttime").html(fdStateInfo.roma_lefttime == 0 ? "00:00:00" : fdStateInfo.roma_lefttime);//直刻剩余的倒计时
            $("#roma_setburntime").html(fdStateInfo.roma_setburntime == 0 ? "无" : fdStateInfo.roma_setburntime);//刻录时间


            //光驱2
            $("#romb_status").html(getRomStatus(fdStateInfo.romb_status));//光盘状态
            $("#romb_begintime").html(fdStateInfo.romb_begintime);//开始直刻的时间
            $("#romb_lefttime").html(fdStateInfo.romb_lefttime == 0 ? "00:00:00" : fdStateInfo.romb_lefttime);//直刻剩余的倒计时
            $("#romb_setburntime").html(fdStateInfo.romb_setburntime == 0 ? "无" : fdStateInfo.romb_setburntime);//刻录时间


            $("#diskrec_isrec").html(fdStateInfo.diskrec_isrec == 1 ? "<span style='color: red;'>录像中</span>" : "未录像");//是否正在本地硬盘录像

            $("#disk_totalspace").html(getfilesize(fdStateInfo.disk_totalspace + 100000));//硬盘总容量
            $("#disk_freespace").html(baifnebi(fdStateInfo.disk_freespace, fdStateInfo.disk_totalspace) + "%");//硬盘剩余容量

            $("#diskrec_begintime").html(fdStateInfo.diskrec_begintime);//本地硬盘开始录像时间

            $("#bsettimerrecburntime").html(fdStateInfo.bsettimerrecburntime == null ? "1小时" : fdStateInfo.bsettimerrecburntime + "小时");//刻录选时
            $("#burn_mode").html(getBurnMode(fdStateInfo.burn_mode));//当前刻录模式 0:直刻模式 1:硬盘导刻模式 2:接力刻录模式

            if(fdStateInfo.diskrec_isrec == 1){
                diskrec_continuettime = fdStateInfo.diskrec_continuettime;
                // jisuantime();
                setintervalKey = setInterval(function () {
                    jisuantime();
                }, 1000);
            }else{
                $("#diskrec_continuettime").html("00:00");//硬盘录像持续时间
            }

        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
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

function matchPtdjKey(key) {

    if("casenum" == key){
        return getRecordById_data.record.caseAndUserInfo.casenum == null ? "" : getRecordById_data.record.caseAndUserInfo.casenum;
    }
    if("casename" == key){
        return getRecordById_data.record.caseAndUserInfo.casename == null ? "" : getRecordById_data.record.caseAndUserInfo.casename;
    }
    if("recordtypename" == key){
        return getRecordById_data.record.recordtypename == null ? "" : getRecordById_data.record.recordtypename;
    }
    if("cause" == key){
        return getRecordById_data.record.caseAndUserInfo.cause == null ? "" : getRecordById_data.record.caseAndUserInfo.cause;
    }
    if("department" == key){
        return getRecordById_data.record.caseAndUserInfo.department == null ? "" : getRecordById_data.record.caseAndUserInfo.department;
    }
    if("username" == key){
        return getRecordById_data.record.caseAndUserInfo.username == null ? "" : getRecordById_data.record.caseAndUserInfo.username;
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