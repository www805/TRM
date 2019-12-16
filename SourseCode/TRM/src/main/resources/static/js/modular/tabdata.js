
//身心检测
function initph() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//语音识别
function initasr() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//案件
function initcase() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}
//情绪报告
function initmood() {
    $(".layui-tab-content").css("height","90%");
    $("#templatetoproblem").css("height","initial");
}

function initcase_header() {
    $(".layui-tab-content").css("height","0%");
    $("#templatetoproblem").css("height","62%");
}


//*******************************************************************左侧搜索块start****************************************************************//
var dqindex_realtxt=0;//当前显示的下标
var likerealtxtarr=[];//搜索txt
//搜索上
function last_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt--;
        if (dqindex_realtxt<0){
            dqindex_realtxt=0;
            layer.msg("这是第一个~");
        }
        set_dqrealtxt();
    }
}
//搜索下
function next_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt++;
        if (dqindex_realtxt>=likerealtxtarr.length-1){
            dqindex_realtxt=likerealtxtarr.length-1;
            layer.msg("这是最后一个~");
        }
        set_dqrealtxt();
    }
}
//搜索赋值
function set_dqrealtxt(){
    mouseoverbool_left=1;//不滚动
    if (isNotEmpty(likerealtxtarr)) {
        for (var i = 0; i < likerealtxtarr.length; i++) {
            var all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        var div = document.getElementById('recordreals_scrollhtml');
        div.scrollTop = top;
    }
}
function recordreals_select() {
    mouseoverbool_left=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];
    var recordrealshtml= $("#recordreals").html();
    recordrealshtml=recordrealshtml.replace(/(<\/?a.*?>)/g, '');
    $("#recordreals").html(recordrealshtml);

    $("#recordreals div").each(function (i,e) {
        var spantxt=$(this).find("span").text();
        if (isNotEmpty(likerealtxt)){
            if (spantxt.indexOf(likerealtxt) >= 0) {
                var html=$(this).find("span").html();
                html = html.split(likerealtxt).join('<a class="highlight_all">'+ likerealtxt +'</a>');
                $(this).find("span").html(html);
                likerealtxtarr.push($(this).find("span"));
            }
        }
    });

    if (isNotEmpty(likerealtxtarr)){
        set_dqrealtxt();
    }else {
        /*layer.msg("没有找到内容~");*/
    }
}
//*******************************************************************左侧搜索块end****************************************************************//