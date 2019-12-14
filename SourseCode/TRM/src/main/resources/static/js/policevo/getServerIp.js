var modeltds = [];
var serverIpData;
var trmipMap;
var trmipKey="";
// var trmipNum=0;

//修改会议模板ip
function updateServerIp(){

    var url = getActionURL(getactionid_manage().serverip_updateServerIp);
    // var url = "/sweb/base/ip/updateServerIp";

    var trmip = $("#trmip").val();
    var flushbonadingip = $("#flushbonadingip").val();
    var asrip = $("#asrip").val();
    var polygraphip = $("#polygraphip").val();
    var storageip = $("#storageip").val();
    var ttsetinfoip = $("#ttsetinfoip").val();

    var data={
        trmip: trmip,
        flushbonadingip: {
            etip: flushbonadingip,
            ssid: serverIpData['flushbonadingip'].equipmentssid
        },
        asrip: {
            etip: asrip,
            ssid: serverIpData['asrip'].equipmentssid
        },
        polygraphip: {
            etip: polygraphip,
            ssid: serverIpData['polygraphip'].equipmentssid
        },
        storageip: {
            etip: storageip,
            ssid: serverIpData['storageip'].ssid
        },
        ttsetinfoip: {
            etip: ttsetinfoip,
            ssid: serverIpData['ttsetinfoip'].ssid
        }
    }

    ajaxSubmitByJson(url,data,callUpdateServerIp);
}


//修改ip
function updateIp(){

    // var url = getActionURL(getactionid_manage().serverip_updateServerIp);
    var url = "/sweb/base/ip/updateIp";

    var serviceName = $("#serviceName").html();
    var trmip = $("#tip").val();
    var subnetMask = $("#subnetMask").val();
    var gateway = $("#gateway").val();

    var data={
        name: serviceName,
        ip: trmip,
        subnetMask: subnetMask,
        gateway: gateway
    }


    console.log(data);
    ajaxSubmitByJson(url,data,callUpdateServerIp);
}

/**
 * 打开修改IP页面
 */
function getServerIpPath(trmipNum){

    var trmList = trmipMap[trmipKey];
    var trmip = trmList[trmipNum];

    var name = trmip.name==null?'':trmip.name;
    var ip = trmip.ip==null?'':trmip.ip;
    var subnetMask = trmip.subnetMask==null?'':trmip.subnetMask;
    var gateway = trmip.gateway==null?'':trmip.gateway;

    var html = '  <form class="layui-form site-inline" >\
               <div class="layui-form-item">\
                    <label class="layui-form-label">网卡名称</label>\
                    <div class="layui-input-block">\
                        <h1 style="padding-top: 7px;font-weight: bold;font-size: 15px;" id="serviceName">' + name + '</h1>\
                    </div>\
               </div>\
               <div class="layui-form-item">\
                   <label class="layui-form-label">ip</label>\
                    <div class="layui-input-block">\
                        <input type="text" name="ip" lay-verify="ip" id="tip" autocomplete="off" placeholder="请输入ip" value="' + ip + '"  class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">子网掩码</label>\
                    <div class="layui-input-block">\
                        <input type="text" name="subnetMask" id="subnetMask" lay-verify="title" autocomplete="off" placeholder="请输入子网掩码" value="' + subnetMask + '"  class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">默认网关</label>\
                    <div class="layui-input-block">\
                        <input type="text" name="gateway" id="gateway" lay-verify="title" autocomplete="off" placeholder="请输入默认网关" value="' + gateway + '"  class="layui-input">\
                    </div>\
                </div>\
            </form>';


    var index = layer.open({
        title:'修改IP',
        content: html,
        area: ['500px', '350px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            layer.close(index);

            updateIp();//修改

            // if (isNotEmpty(typename)){
            //     AddOrUpdateTemplateType();//修改
            // }else{
            //     AddOrUpdateTemplateType(1);//新增
            // }
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}



//遍历网卡中的ip
function getNetworkPath(){
    var trmList = trmipMap[trmipKey];
    var nrHTML = "";

    for (var i = 0; i < trmList.length; i++) {
        var serviceip = trmList[i];
        var ip = serviceip.ip==null?'':serviceip.ip;
        var subnetMask = serviceip.subnetMask==null?'':serviceip.subnetMask;
        var gateway = serviceip.gateway==null?'':serviceip.gateway;

        nrHTML += '    <tr>\n' +
            '      <td>' + (i + 1) + '</td>\n' +
            '      <td>' + ip + '</td>\n' +
            '      <td>' + subnetMask + '</td>\n' +
            '      <td>' + gateway + '</td>\n' +
            '      <td><button class="layui-btn layui-btn-sm layui-btn-normal" onclick="getServerIpPath(' + i + ');">修改</button></td>\n' +
            '    </tr>\n';

    }

    var html = '<h3>' + trmipKey + '</h3><table class="layui-table">\n' +
        '  <colgroup>\n' +
        '    <col width="60">\n' +
        '    <col width="200">\n' +
        '    <col>\n' +
        '    <col>\n' +
        '    <col>\n' +
        '  </colgroup>\n' +
        '  <thead>\n' +
        '    <tr>\n' +
        '      <th>id</th>\n' +
        '      <th>ip</th>\n' +
        '      <th>子网掩码</th>\n' +
        '      <th>默认网关</th>\n' +
        '      <th>操作</th>\n' +
        '    </tr> \n' +
        '  </thead>\n' +
        '  <tbody>\n' +
        nrHTML+
        '  </tbody>\n' +
        '</table>';

    var index = layer.open({
        title:'网卡信息',
        content: html,
        area: ['900px', 'auto'],
        btn: ['返回'],
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

/**
 * 获取笔录客户端ip和会议模板
 */
function getServerIpList(){
    var url = getActionURL(getactionid_manage().serverip_getServerIpList);
    // var url = "/sweb/base/ip/getServerIpList";

    ajaxSubmit(url,null,callgetServerIpList);
}

/**
 * 通过会议模板查到相关设备ip
 * @param num
 */
function getServerIpALL(num){
    var url = getActionURL(getactionid_manage().serverip_getServerIpALL);
    // var url = "/sweb/base/ip/getServerIpALL";

    if(modeltds.length == 0){
        layer.msg("会议模板数组为空", {icon:5});
        return;
    }
    var modeltd = modeltds[num];

    data = {
        asrssid: modeltd.asrssid,
        polygraphssid: modeltd.polygraphssid,
        fdssid: modeltd.fdssid
    }

    ajaxSubmit(url,data,callgetServerIpALL);
}

function callgetServerIpList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var serverIplist = data.data;
        modeltds = serverIplist.modeltds;
        // $("#trmip").val(serverIplist.trmip);

        console.log(serverIplist.trmipMap);

        var jc = 0;
        trmipMap = serverIplist.trmipMap;
        for(var key in trmipMap){
            if(jc == 0){
                trmipKey = key;
            }
            jc++;
            // console.log(key)    //键
            // consolelog(trmipMap[key])  //值

            $("#trmip").append("<option value='" + key + "' >" + key + "</option>");
        }


        // for (var j = 0; j < trmipList.length; j++) {
        //     var trmip = trmipList[j];
        //     $("#trmip").append("<option value='" + j + "' >" + trmip.ip + "</option>");
        // }

        for (var i = 0; i < modeltds.length; i++) {
            var modeltd = modeltds[i];
            $("#mctemplate").append("<option value='" + i + "' >" + modeltd.explain + (modeltd.grade == 1 ? " - 主麦" : " - 副麦") + "</option>");
        }

        layui.use('form', function () {
            var form = layui.form;
            form.render();
        });
        getServerIpALL(0);
    }else{
        layer.msg(data.message, {icon:5});
    }
}

function callgetServerIpALL(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        // console.log(data);

        serverIpData = data.data;
        $("#polygraphip").val(serverIpData['polygraphip'].etip);
        $("#flushbonadingip").val(serverIpData['flushbonadingip'].etip);
        $("#asrip").val(serverIpData['asrip'].etip);
        $("#storageip").val(serverIpData['storageip'].etip);
        $("#ttsetinfoip").val(serverIpData['ttsetinfoip'].etip);

    }else{
        layer.msg(data.message, {icon:5});
    }
}

function callUpdateServerIp(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("操作成功",{icon: 6});
        setTimeout("window.location.reload()",1500);
    }else{
        layer.msg(data.message, {icon:5});
    }
}


$(function () {
    layui.use(['form', 'layedit', 'laydate', 'laypage', 'layer', 'upload'], function () {
        var $ = layui.jquery
            , upload = layui.upload
            , form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate
            ,laypage = layui.laypage
            ,element = layui.element;

        form.on('select(trmip_filter)', function (data) {
            // console.log(data);
            trmipKey = data.value;
        });

        form.on('select(mctemplate_filter)', function (data) {
            //请求
            // console.log(data);
            getServerIpALL(data.value);
        });

        form.verify({
            setip: function(value, item){ //value：表单的值、item：表单的DOM对象
                if(''==value){
                    return "IP地址不能为空";
                }
                if(!(/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value))){
                    return '请输入一个正确的IP地址';
                }
            }
        });

        form.on('submit(formDemo)', function (data) {
            updateServerIp();
            return false;
        });

    });
});

