/**
 * 问答笔录操作
 * @type {string}
 */

var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd" id="record_qw">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" onkeydown="qw_keydown(this,event);" name="q" starttime=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" onkeydown="qw_keydown(this,event);" name="w"   starttime=""></label></div>\
               <div  id="btnadd" ></div>\
                </td>\
                <td id="record_util">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   </div>\
                </td>\
                </tr>';

//最后一行添加按钮初始化
function addbtn() {
    var btnhtml='<button type="button"  class="layui-btn layui-btn-warm" style="border-radius: 50%;width: 45px;height: 45px;padding:0px"  title="添加一行自定义问答" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon" style="font-size: 45px" >&#xe608;</i></button>';
    $("#recorddetail tr").each(function () {
        $("#btnadd",this).html("");
    });
    $('#recorddetail tr:last #btnadd').html(btnhtml);

    //鼠标聚焦位置
    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
}

//tr工具按钮==start
function tr_remove(obj) {
    var bool=$(obj).parents("tr").attr("automaticbool");
    if (isNotEmpty(bool)&&bool==1){
        laststarttime_qq=-1;
        laststarttime_ww=-1;
        last_type=-1;//1问题 2是答案
        qq="";
        qqq="";
        ww="";
        www="";
    }
    td_lastindex={};
    $(obj).parents("tr").remove();
    addbtn();
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
    addbtn();
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
    addbtn();
}
//tr工具按钮==end



//回车+上下键
function qw_keydown(obj,event) {
    var e = event || window.event;
    var keyCode = e.keyCode;

    var dqname=$(obj).attr("name");
    var trindex= $(obj).closest("tr").index();
    var trlength=$("#recorddetail tr").length;
    var lable=null;
    switch(keyCode){
        case 13:
            if (dqname=="q") {
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="w"]');//定位本行的答
                setFocus(lable);
            }else {
                if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                    focuslable(trtd_html,1,'q');
                } else {
                    lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                    setFocus(lable);
                }
            }
            event.preventDefault();
            break;
        case 38:
            var name=dqname=="q"?"w":"q";
            var index=(trindex-1)<=0?0:(trindex-1);
            if (dqname=="w"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                if(trindex!=0){
                    lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                    setFocus(lable);
                }
                event.preventDefault();
            }
            break;
        case 40:
            var index=(trindex+1)>=trlength?trlength:(trindex+1);
            var name=dqname=="q"?"w":"q";
            if (dqname=="q"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                setFocus(lable);
            }
            break;
        default: break;
    }
}

//聚焦
function setFocus(el) {
    if (isNotEmpty(el)){
        el = el[0];
        if (window.getSelection) {//ie11 10 9 ff safari
            el.focus(); //解决ff不获取焦点无法定位问题
            var range = window.getSelection();//创建range
            range.selectAllChildren(el);//range 选择obj下所有子内容
            range.collapseToEnd();//光标移至最后
        }
        else if (document.selection) {//ie10 9 8 7 6 5
            var range = document.createRange();
            range.selectNodeContents(el);
            range.collapse(false);
            var sel = window.getSelection();
            if(sel.anchorOffset!=0){
                return;
            };
            sel.removeAllRanges();
            sel.addRange(range);
        }
        event.preventDefault();
    }
};

function contextMenu() {
    $('#recorddetail label').bind('mouseup', function(e) {
        if (3 == e.which) {
            document.execCommand('removeFormat');
        }  else if (1 == e.which) {
            document.execCommand('backColor',false,'yellow');
        }
    });
}

//lable type 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    var qwfocus=null;

    if (null!=td_lastindex["key"]&&type==1){
        $('#recorddetail tr:eq("'+td_lastindex["key"]+'")').after(html);
        if (isNotEmpty(qw)){
            qwfocus= $('#recorddetail tr:eq("'+(td_lastindex["key"]+1)+'") label[name="'+qw+'"]');
            td_lastindex["key"]=td_lastindex["key"]+1;
        }
    }  else if (type==0) {
        $("#recorddetail").prepend(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:eq(0) label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:eq(0)').index();
        }
    }else {
        $("#recorddetail").append(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:last label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:last').index();
        }
    }

    if (isNotEmpty(qw)){
        setFocus(qwfocus);
    }
    addbtn();
    contextMenu();

    $("#recorddetail  span[starttime]:not(:empty)").dblclick(function () {
        var contenteditable=$(this).closest("label").attr("contenteditable");
        if (isNotEmpty(contenteditable)&&contenteditable=="false") {
            //开始定位视频位置
            var times=$(this).attr("starttime");
            if (times!="-1"&&isNotEmpty(times)){
                var name=$("label",this).attr("name");
                if (name == "q"){
                    times=parseInt(times)+subtractime_q;
                }else if( name=="w") {
                    times=parseInt(times)+subtractime_w;
                }
                showrecord(times);
            }
        }
    })

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
}


//导出word or pdf
function btn() {
    var selected=$("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $("div[name='btn_div']").attr("showorhide","true");
        $("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
    }
}
function exportWord(url){
    var paramdata={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
        }
    };
    ajaxSubmitByJson(url, paramdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var word_htmlpath=data.word_htmlpath;//预览html地址
                var word_path=data.word_path;//下载地址
                window.location.href = word_path;
                layer.msg("导出成功,等待下载中...",{icon: 6});
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
    });
    btn();
}
function exportPdf(url) {
    var paramdata={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
        }
    };
    ajaxSubmitByJson(url, paramdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.open({
                    id:"pdfid",
                    type: 1,
                    title: '导出PDF笔录',
                    shadeClose: true,
                    shade: false,
                    maxmin: true, //开启最大化最小化按钮
                    area: ['893px', '600px'],
                });
                showPDF("pdfid",pdfdownurl);
                layer.msg("导出成功,等待下载中...",{icon: 6});
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
    });
    btn();
}

//左侧语音文本点击追加到右侧
var copy_text_html="";
var touchtime = new Date().getTime();
function copy_text(obj,event) {
    var text=$(obj).html();
    copy_text_html=text;
    var classc=$(obj).closest("div").attr("class");
    var starttime=$(obj).closest("div").attr("starttime");

    var qw=null;
    if ((classc=="atalk"&&1 == event.which)||(classc=="btalk"&&3 == event.which)) {//左键并且为问||右键并且为答
        qw="q";
    }else  if ((classc=="btalk"&&1 == event.which)||(classc=="atalk"&&3 == event.which)){//左键并且为答 || 右键并且为问
        qw="w";
    }


    //鼠标双击事件
    if( new Date().getTime() - touchtime < 350 ){
        console.log("现在是双击事件")
        var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+qw+'"]');
        var old= $html.attr(qw+"_starttime");
        var h=$html.html();
        $html.append(copy_text_html);
        if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
            $html.attr(qw+"_starttime",starttime);//直接使用最后追加的时间点
        }
    }else{
        console.log("现在是单击事件")
        var txt = window.getSelection?window.getSelection():document.selection.createRange().text;
        var dqselec_left= txt.toString();
        if (3 == event.which&&isNotEmpty(dqselec_left)&&copy_text_html.indexOf(dqselec_left)>-1&&new Date().getTime() - touchtime >700){
            if (classc=="btalk") {
                qw="w";
            }else if(classc=="atalk"){
                qw="q";
            }
            var $html=$('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="'+qw+'"]');
            var old= $html.attr(qw+"_starttime");
            var h=$html.html();
            $html.append(dqselec_left);
            dqselec_left="";
            window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
            if (!isNotEmpty(old)||!isNotEmpty(h)) {//开始时间为空或者文本为空时追加时间点
                $html.attr(qw+"_starttime",starttime);//直接使用最后追加的时间点
            }
        }
        touchtime = new Date().getTime();
    }
    copy_text_html="";
    return false;
}




$(function () {
    $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    $(window).resize(function(){
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    })
})