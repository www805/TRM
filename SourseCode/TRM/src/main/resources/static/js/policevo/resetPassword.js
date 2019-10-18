
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