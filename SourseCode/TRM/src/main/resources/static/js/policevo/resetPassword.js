
/**
 * 密码导入重置
 */
function keyfile_click(){
    $("#keyfile").click();
}

function reset_keyfile(obj) {
    var file = document.getElementById("keyfile").files[0];
    if (isNotEmpty(file)){
        $("#showfilename").text(file.name);
    }
}


var resetPasswordindex=null;
function resetPassword(){
    if (isNotEmpty(ssid)){
        resetPasswordindex=layer.msg("密码重置中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });

        $("#progress").css("visibility","visible");
        $("#progress .layui-progress-text").text("0%");
        $("#progress .layui-progress-bar").width("0%");


        var init_password=$("#init_password").val();
        var url=getActionURL(getactionid_manage().resetPassword_resetPassword);
        var data={
            userssid:ssid,
            init_password:init_password
        };

        var file = document.getElementById("keyfile").files[0];
        var formData = new FormData();
        formData.append("userssid", ssid);
        formData.append("init_password", init_password);
        formData.append("file", file);

        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        xhr.open("post", url, true);
        xhr.timeout = 500000;
        xhr.onload = function(data) {
            $("[lay-filter='progress_demo']").css("visibility","hidden");
            callbackresetPassword(xhr.responseText);
        };
        xhr.upload.addEventListener("progress", function (e) {
            var completePercent = Math.round(e.loaded / e.total * 100)+ "%";
            $("#progress").css("visibility","visible");
            $("#progress .layui-progress-text").text(completePercent);
            $("#progress .layui-progress-bar").width(completePercent);
        }, false);
        xhr.send(formData);
    }
}
function callbackresetPassword(str) {
    var data = eval('(' + str + ')');
    $("#progress").css("visibility","hidden");
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("用户密码已重置",{icon: 6,time:500,shade: [0.1,'#fff']},function () {
            window.history.go(-1);return false;
        });

    }else {
        layer.msg(data.message,{icon: 5});
    }
}