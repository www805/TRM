function keyfile_click(){
    $("#keyfile").click();
}

function keyfile(obj) {
    var file = document.getElementById("keyfile").files[0];
    if (isNotEmpty(file)){
        $("#showfilename").text(file.name);
    }
}

var uploadkeyindex=null;
function uploadkey(){
    var file = document.getElementById("keyfile").files[0];
    if (null!=file){
        uploadkeyindex=layer.msg("上传key中，请等待...", {
            icon: 16,
            shade: [0.1, 'transparent'],
            time:5000
        });

        $("#progress").css("visibility","visible");
        $("#progress .layui-progress-text").text("0%");
        $("#progress .layui-progress-bar").width("0%");


        var url=getActionURL(getactionid_manage().forgotpassword_uploadkey);
        var file = document.getElementById("keyfile").files[0];
        var formData = new FormData();
        formData.append("file", file);
        formData.append("param", null);
        formData.append("token", INIT_CLIENTKEY);


        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        xhr.open("post", url, true);
        xhr.timeout = 500000;
        xhr.onload = function(data) {
            $("[lay-filter='progress_demo']").css("visibility","hidden");
            callbackuploadkey(xhr.responseText);
        };
        xhr.upload.addEventListener("progress", function (e) {
            var completePercent = Math.round(e.loaded / e.total * 100)+ "%";
            $("#progress").css("visibility","visible");
            $("#progress .layui-progress-text").text(completePercent);
            $("#progress .layui-progress-bar").width(completePercent);
        }, false);
        xhr.send(formData);
    } else {
       window.history.go(-1);return false;
    }

}
function callbackuploadkey(str) {
    var data = eval('(' + str + ')');
    $("#progress").css("visibility","hidden");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var filename=data.filename;
            layer.confirm('将检验码【<span style="color: red">'+filename+'</span>】告知管理员进行下一步', {
                btn: ['确认'], //按钮
                shade: [0.1,'#fff'], //不显示遮罩
            }, function(index){
                window.history.go(-1);return false;
                layer.close(index);
            });
        }
    }else {
        layer.msg(data.message,{icon: 5});
    }
}