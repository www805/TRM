var etypessid;
var ssid;
var ftpdata;
var looktg;

//查询单个存储服务的磁盘信息
function getFileSpaceByssid(ssidd) {
    var url=getActionURL(getactionid_manage().fileSpaceAction_getFileSpaceByssid);
    // var url = "/filespace/getFileSpaceByssid";
    ssid = ssidd;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url,data,callGetFileSpaceByssid);
}

//查询路径下的所有文件
function getFileSpaceAll(path) {
    var url=getActionURL(getactionid_manage().fileSpaceAction_getFileSpaceAll);
    // var url = "/filespace/getFileSpaceAll";

    if(!isNotEmpty(path)){
        path = $('#remove').attr("name");
    }else {
        // path = path.replace(/\\/g, "\\\\");
        console.log(path);
    }
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            path: path
        }
    };
    ajaxSubmitByJson(url, data, callGetFileSpaceAll);
}

//删除当前路径下的所有文件
function delFileSpaceAll(path) {
    var url=getActionURL(getactionid_manage().fileSpaceAction_delFileSpaceAll);
    // var url = "/filespace/delFileSpaceAll";
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            path: path,
        }
    };
    ajaxSubmitByJson(url, data, callDelFileSpaceByPath);
}

//删除单个文件
function delFileSpaceByPath(path) {
    var url=getActionURL(getactionid_manage().fileSpaceAction_delFileSpaceByPath);
    // var url = "/filespace/delFileSpaceByPath";
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            path: path,
        }
    };
    ajaxSubmitByJson(url, data, callDelFileSpaceByPath);
}

function callDelFileSpaceByPath(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("操作成功",{icon: 6});
        // setTimeout("window.location.reload()",1500);
        setTimeout("$('#refresh').click();",500);
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callGetFileSpaceByssid(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            var storage = data.data;
            // console.log(storage);


            var filePathSpace = storage.filePathSpace;
            var filePathSpaceByParentNodePath = storage.filePathSpaceByParentNodePath;

            if(isNotEmpty(filePathSpace) && isNotEmpty(filePathSpaceByParentNodePath)){

                var sysVersion = "";
                if(isNotEmpty(filePathSpace.driverPath)){
                    var driverPath = filePathSpace.driverPath;
                    ftpdata = driverPath.replace(/\//g, "\\");
                    $("#ftpdatapath").html("当前路径：" + ftpdata).attr("name",ftpdata);
                    var drinum = driverPath.indexOf(":");
                    if (drinum < 0) {
                        drinum = driverPath.indexOf("：");
                    }
                    sysVersion = driverPath.substring(0,drinum);
                }

                $("#sysVersion").html(sysVersion + "盘");
                $("#companyname").html(filePathSpace.totalSpace_str);
                $("#sysStartTime").html(filePathSpace.freeSpace_str);
                $("#workTime").html(filePathSpace.driverPath);
                $("#basenum").html(filePathSpace.useSpace_str);

            }else{
                layer.msg("无法找到该文件路径", {icon: 5});
                alert("无法找到该文件路径");
                javascript :history.back(-1);
            }

            if(isNotEmpty(filePathSpaceByParentNodePath)){
                getDivall(filePathSpaceByParentNodePath);//渲染页面
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function callGetFileSpaceAll(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            var storage = data.data;
            // console.log(storage);

            var filePathSpace = storage.filePathSpace;
            var filePathSpaceByParentNodePath = storage.filePathSpaceByParentNodePath;


            $("#ftpdatapath").html("当前路径：" + filePathSpace.driverPath).attr("name",filePathSpace.driverPath);

            if (ftpdata == filePathSpace.driverPath + "\\") {
                $("#looktg").hide();
            } else {
                var path = filePathSpace.driverPath;
                var lastNum = path.lastIndexOf("\\");
                path = path.substring(0, lastNum);
                path = path.replace(/\\/g, "\\\\");
                $("#looktg").show().attr("onclick", "getFileSpaceAll('" + path + "');");
            }

            getDivall(filePathSpaceByParentNodePath);//渲染页面
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//显示获取到的文件
function getDivall(filePathSpaceByParentNodePath) {

    var divallHTML = "";
    if(isNotEmpty(filePathSpaceByParentNodePath)){
        for (var i = 0; i < filePathSpaceByParentNodePath.length; i++) {
            var driver = filePathSpaceByParentNodePath[i];
            var pngClass = "";
            $(".menu-one").show();
            if(!driver.folderBool){
                pngClass = "img_file";
                $(".menu-one").hide();
            }

            divallHTML += '<li class="' + pngClass + '" name="' + driver.driverPath + '" title="' + driver.driverName + '" ondblclick=\'getFileSpaceAll();\'><span class="layui-badge layui-bg-cyan">' + driver.useSpace_str + '</span><input type="text" class="changename" value="' + driver.driverName + '" disabled/></li>';
        }
    }

    $("#divall").html(divallHTML);
}


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}
