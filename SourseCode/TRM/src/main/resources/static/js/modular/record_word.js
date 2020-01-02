/**
 *word版本不
 * @returns {jQuery.fn.init|jQuery|HTMLElement|*}
 */

//找到最后一个p
function getlastp() {
    var lastp=null;
    if (isNotEmpty(laststarttime_ue)){
        lastp=$("p[starttime="+laststarttime_ue+"]:last",editorhtml);
        if (!isNotEmpty(lastp)){
            lastp=$("p[starttime]:not(:empty)",editorhtml).last();
            if (!isNotEmpty(lastp)){
                lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
                lastp=$(lastp);
            }
        }
    }else {
        //光标追加
        //获取光标所在的p标签
        lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
        lastp=$(lastp);
    }
    //latsp不存在查找divid
    if (!isNotEmpty(lastp)) {
        var divid=$("p[starttime="+laststarttime_ue+"]:last",editorhtml).closest("div").attr("id");
        if (!isNotEmpty(divid)){
            divid = TOWORD.util.getDivIdByUE(ue);
        }
        lastp= $("#"+divid+" p:last",editorhtml);
    }
    return lastp;
}

//找到最后一个p的style
function getlastp_style() {
    var pstyle="";
    var lastp=getlastp();
    if (isNotEmpty(lastp)){
        pstyle=$("span",lastp).attr("style");
        if(typeof(pstyle) == "undefined"){
            pstyle="";
        }
        var newpstyle = $(lastp).attr("style");
        if(typeof(newpstyle) == "undefined"){
            newpstyle="";
        }
        console.log("span的样式："+pstyle);
        console.log("p标签的样式："+newpstyle)
        return pstyle+newpstyle;
    }
    return pstyle;
}

//word存在文本改变 重置一下页面
var laststarttime_ue=null;//最后一个识别的starttime
function  resetpage() {
    var divid=null;
    if (isNotEmpty(laststarttime_ue)) {
        divid=$("p[starttime="+laststarttime_ue+"]",editorhtml).closest("div").attr("id");
    }else {
        divid = TOWORD.util.getDivIdByUE(ue);
    }

    if(!isNotEmpty(divid)){
        return;
    }else{
        TOWORD.currentdivnum=parseInt(divid.replace(/[^0-9]/ig,""));
    }
    console.log(" TOWORD.currentdivnum=222==="+ TOWORD.currentdivnum)
    var psheight=TOWORD.page.getAllPHeightByDivid(divid);
    var pseight_old=TOWORD.divheightmap[divid];
    if(isNotEmpty(psheight)){
        //对比上一次的高度，有变化的话，触发重新排版
        if(isNotEmpty(pseight_old)&&psheight!=pseight_old){
            //重新排版，写入toWord里面
            console.log("重新排版，写入toWord里面");
            TOWORD.page.reTypesetting(ue,pseight_old,psheight,divid);
        }
        console.log(pseight_old+":pseight_old----pseight:"+psheight+"---divid:"+divid);
    }

}


//导出模板：exporttype 1word 2pdf url请求地址
function exporttemplate_ue(exporttype,url) {
    if (isNotEmpty(exporttype)&&isNotEmpty(recordssid)) {
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                exporttype:exporttype,
            }
        };
        ajaxSubmitByJson(url,paramdata,callbackexporttemplate_ue);
    }
    $("div[name='btn_div']").attr("showorhide","false");
    $("div[name='btn_div']").removeClass("layui-form-selected");
}
function callbackexporttemplate_ue(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var exporttype=data.exporttype;
            var word_downurl=data.word_downurl;//word导出地址
            var pdf_downurl=data.pdf_downurl;//pdf导出地址
            if (isNotEmpty(exporttype)) {
                if (exporttype==1&&isNotEmpty(word_downurl)){
                    window.location.href = word_downurl;
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                } else if (exporttype==2&&isNotEmpty(pdf_downurl)){
                    layer.open({
                        id:"pdfid",
                        type: 1,
                        title: '导出PDF',
                        shadeClose: true,
                        maxmin: true, //开启最大化最小化按钮
                        area: ['893px', '600px'],
                    });
                    showPDF("pdfid",pdf_downurl);
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }else {
                    layer.msg("导出失败",{icon: 5});
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//左侧语音文本点击追加到右侧
function copy_text(obj,e) {
    var lastp = TOWORD.util.getpByRange(ue);//获取光标所在p
    if (!isNotEmpty(lastp)) {
        layer.msg("请先在笔录界面加入光标")
        return;
    }
    var starttime=$(obj).attr("starttime");
    var txt=$("#translatext",obj).text();
    var oldstarttime=$(lastp).attr("starttime");//获取光标所在位置
    //样式
    var  pstyle=$("span",lastp).attr("style");
    if(typeof(pstyle) == "undefined"){
        pstyle="";
    }
    var newpstyle = $(lastp).attr("style");
    if(typeof(newpstyle) == "undefined"){
        newpstyle="";
    }
    $(lastp).append(txt);
    if (!isNotEmpty(oldstarttime)&&isNotEmpty(starttime)){
        $(lastp).attr("starttime",starttime);
        $(lastp).attr("style",newpstyle+pstyle);
    }
}


///////////////////////////////**********************************************************自动甄别**************start
//usertype 角色类型  starttime语音识别开始时间 gradeintroduce角色简称 translatext当前识别文本
var last_identifys = {};//每个人上一次甄别内容 格式：usertype：{firsttime:第一句时间，starttime:当前句时间}
var lastusertype=-1;//上一个角色类型
function identify(usertype,starttime,gradeintroduce,translatext) {
    var last_identify=last_identifys[""+lastusertype+""];//上一个角色
    var dq_identify=last_identifys[""+usertype+""];//当前角色

    if (lastusertype!=-1){
        if (isNotEmpty(last_identify)){
            if (usertype==lastusertype) {
                var thisp= $("p[usertype="+usertype+"][starttime="+last_identify.firsttime+"]:last",editorhtml);
                if (isNotEmpty(thisp)) {
                    var span_last = $("span[starttime]:not(:empty)",thisp).last();
                    var span_laststarttime=$(span_last).attr("starttime");
                    if (isNotEmpty(span_laststarttime)&&span_laststarttime==starttime) {
                        $(span_last).html(translatext);
                    }else {
                        $(thisp).append("<span starttime="+starttime+">"+translatext+"</span>");
                    }
                    TOWORD.page.checkAndDealSpanHeight(thisp[0],true);
                    last_identifys[""+usertype+""].starttime=starttime;
                }else {
                    addidentify(usertype,starttime,gradeintroduce,translatext);
                }
            }else {
                if (isNotEmpty(dq_identify)) {
                    var thisp= $("p[usertype="+usertype+"][starttime="+dq_identify.firsttime+"]:last",editorhtml);
                    if (dq_identify.starttime==starttime&&isNotEmpty(thisp)){
                        var span_last = $("span[starttime]:not(:empty)",thisp).last();
                        var span_laststarttime=$(span_last).attr("starttime");
                        if (isNotEmpty(span_laststarttime)&&span_laststarttime==starttime) {
                            $(span_last).last().html(translatext);
                        }else {
                            $(thisp).append("<span starttime="+starttime+">"+translatext+"</span>");
                        }
                        TOWORD.page.checkAndDealSpanHeight(thisp[0],true);
                        last_identifys[""+usertype+""].starttime=starttime;
                        usertype=lastusertype;//不改变上一个
                    } else {
                        addidentify(usertype,starttime,gradeintroduce,translatext);
                    }
                }else {
                    addidentify(usertype,starttime,gradeintroduce,translatext);
                }
            }
        }else {
            console.log("存在上一个角色不应该进来的吧******************************")
        }
    }else{
        console.log("初始化一下自动甄别******************************")
        addidentify(usertype,starttime,gradeintroduce,translatext);
    }
    lastusertype=usertype;
}
//自动甄别追加新的一行
function addidentify(usertype,starttime,gradeintroduce,translatext) {
    var html='<p starttime="'+starttime+'" usertype="'+usertype+'" style="'+getlastp_style()+'"><span>'+gradeintroduce+'</span><span starttime="'+starttime+'">'+translatext+'</span></p>';
    var lastp=getlastp();
    if (isNotEmpty(lastp)) {
        $(html).insertAfter(lastp);
        TOWORD.page.checkAndDealSpanHeight(lastp[0],true);
    }else {
        console.log("追加的P未找到啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊**********************************")
    }
    laststarttime_ue=starttime;
    resetpage();
    last_identifys[""+usertype+""]={firsttime:starttime,starttime:starttime};
}

///////////////////////////////**********************************************************自动甄别**************end

//笔录对话实时保存
function setRecordreal(url) {
    if (isNotEmpty(editorhtml)){
        var recordToProblems=[];//题目集合
        $("div",editorhtml).each(function (i) {
            var q=$(this).html();
            recordToProblems.push({
                problem:q,
                starttime:-1,
                answers:null
            });
        });
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                recordToProblems:recordToProblems
            }
        };
        ajaxSubmitByJson(url, data, callbacksetRecordreal);
    }
}
function callbacksetRecordreal(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("笔录实时保存成功__"+data);
        }
    }else{
        //layer.msg(data.message,{icon: 5});
        console.log(data.message);
    }
}

//获取缓存实时问答

//获取缓存实时问答
function getRecordrealByRecordssid(url) {
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid
        }
    };
    ajaxSubmitByJson(url, data, callbackgetRecordrealByRecordssid);
}
function callbackgetRecordrealByRecordssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var problems=data;
            $("#recorddetail").html("");
            if (isNotEmpty(problems)) {
                var problemhtml="";
                for (var z = 0; z< problems.length;z++) {


                    var problem = problems[z];
                    var problemtext=problem.problem==null?"未知":problem.problem;
                    problemhtml+=problemtext;
                }
                TOWORD.page.importhtml(problemhtml);
                if (recordingbool==1){
                    laststarttime_ue=$("p[starttime]:not(:empty)",editorhtml).last().attr("starttime");//获取最后一个laststarttime_ue
                    console.log("退出再进来找到的最后时间点?__-__"+laststarttime_ue)
                }else if (recordingbool==2){
                    ue.setDisabled();

                    $("p span[starttime]:not(:empty)",editorhtml).dblclick(function () {
                        var contenteditable=$("body",editorhtml).attr("contenteditable");
                        if (isNotEmpty(contenteditable)&&contenteditable=="false") {
                            //开始定位视频位置
                            var times=$(this).attr("starttime");
                            if (times!="-1"&&isNotEmpty(times)){
                                //时间差需要处理
                                var usertype=$(this).closest("p").attr("usertype");
                                if (isNotEmpty(usertype)){
                                    times=parseInt(times)+subtractime[""+usertype+""];
                                    showrecord(times,null);
                                }
                            }
                        }
                    })
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }


}

$(function () {
    $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    $(window).resize(function(){
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    })
})


