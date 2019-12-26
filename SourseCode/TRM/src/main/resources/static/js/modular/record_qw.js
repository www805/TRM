/**
 * 问答笔录操作
 * @type {string}
 */

var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd" id="record_qw">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" times=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w"   times=""></label></div>\
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
                showrecord(times,null);
            }
        }
    })

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
}




$(function () {
    $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    $(window).resize(function(){
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    })
})