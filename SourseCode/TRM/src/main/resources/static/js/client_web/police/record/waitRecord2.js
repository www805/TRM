/**
 笔录分出来的js
 **/

//*******************************************************************图表区域start****************************************************************//
/**
 * 监测数据
 */
function getPolygraphdata() {
    if (isNotEmpty(mtssid)&&isNotEmpty(MCCache)){
        var polygraphnum=MCCache.polygraphnum;//本次会议开启的测谎仪个数
        if (null!=polygraphnum&&polygraphnum>0){
            var url=getUrl_manage().getPolygraphdata;
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    mtssid: mtssid
                }
            };
            ajaxSubmitByJson(url, data, callbackgetPolygraphdata);
        }
    }else{
        console.log("身心监测信息未找到__"+mtssid);
    }
}
function callbackgetPolygraphdata(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data=data.data;
        if (isNotEmpty(data)){
            var obj=data.t;
            if (isNotEmpty(obj)) {
                dqphdata(obj,true,null);
            }
        }
    }else{
        /* layer.msg(data.message);*///不需要弹出错误信息
    }
}





//*******************************************************************伸缩按钮start****************************************************************//
function shrink(obj) {
    var shrink_bool=$(obj).attr("shrink_bool");
    if (shrink_bool==1){

        $("#shrink_html").hide();
        $(obj).attr("shrink_bool","-1");
        $("i",obj).attr("class","layui-icon layui-icon-spread-left");

        $("#notshrink_html1").attr("class","layui-col-md9");
        $("#layui-layer"+recordstate_index).hide();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }else{
        $("#shrink_html").show();
        $(obj).attr("shrink_bool","1");
        $("i",obj).attr("class","layui-icon layui-icon-shrink-right");

        $("#notshrink_html1").attr("class","layui-col-md6");
        $("#layui-layer"+recordstate_index).show();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }


}
//*******************************************************************伸缩按钮end****************************************************************//


//*******************************************************************告知书start****************************************************************//
var notificationListdata=null;
function getNotifications() {
    var url=getActionURL(getactionid_manage().waitRecord_getNotifications);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            currPage:1,
            pageSize:100
        }
    };
    ajaxSubmitByJson(url, data, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var pagelist=data.pagelist;
                $("#notificationList").html("");
                if (isNotEmpty(pagelist)){
                    notificationListdata=pagelist;
                    for (var i = 0; i < pagelist.length; i++) {
                        var l = pagelist[i];
                        var l_html="<tr>\
                                      <td>"+l.notificationname+"</td>\
                                      <td style='padding-bottom: 0;'>\
                                          <div class='layui-btn-container'>\
                                          <button  class='layui-btn layui-btn-danger' onclick='previewgetNotifications(\""+l.ssid+"\");'>打开</button>\
                                          <button  class='layui-btn layui-btn-normal' onclick='downloadNotification(\""+l.ssid+"\")'>直接下载</button>\
                                          </div>\
                                          </td>\
                                 </tr>";
                        $("#notificationList").append(l_html);
                    }
                }
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
        layui.use(['layer','element','upload'], function(){
            var layer = layui.layer; //获得layer模块
            var element = layui.element;
            var upload = layui.upload;
            //使用模块

            var url=getActionURL(getactionid_manage().waitRecord_uploadNotification);

            //执行实例
            var uploadInst = upload.render({
                elem: "#uploadFile" //绑定元素
                ,url: url //上传接口
                , acceptMime: '.doc, .docx' //只允许上传图片文件
                ,exts: 'doc|docx' //只允许上传压缩文件
                ,before: function(obj){
                }
                ,done: function(res){
                    if("SUCCESS" == res.actioncode){
                        layer.msg(res.message,{time:500},function () {
                            getNotifications();
                        });
                    }
                }
                ,error: function(res){
                    console.log("请求异常回调");
                }
            });
        });
    })
}

//获取告知书列表
function open_getNotifications() {
    var html= '<table class="layui-table"  lay-skin="nob">\
        <colgroup>\
        <col>\
        <col  width="200">\
        </colgroup>\
        <tbody id="notificationList">\
        </tbody>\
        <thead>\
            <tr>\
            <td></td><td style="float: right"><button  class="layui-btn layui-btn-warm" id="uploadFile">上传告知书</button></td>\
            </tr>\
         </thead>\
        </table>';
    var index = layer.open({
        type:1,
        title:'选择告知书',
        content:html,
        shadeClose:false,
        shade:0,
        area: ['893px', '600px']
    });
    getNotifications();
}

//下载告知书
function downloadNotification(ssid) {
    var url=getActionURL(getactionid_manage().waitRecord_downloadNotification);
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url, data, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var base_filesave=data.base_filesave;
                if (isNotEmpty(base_filesave)) {
                    var recorddownurl=base_filesave.recorddownurl;
                    layer.msg("下载中，请稍后...",{icon: 6});
                    window.location.href=recorddownurl;
                }
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
    });
}

//打开告知书
var previewgetNotifications_index=null;
var dqrecorddownurl_htmlreads=null;//读取阅读txt

var t1=null;
var len=0;
function previewgetNotifications(ssid) {

    if (isNotEmpty(previewgetNotifications_index)) {
        layer.close(previewgetNotifications_index);
        clearInterval(t1);
        if (isNotEmpty(audioplay)){
            audioplay.pause();
        }
        audioplay=null;
        len=0;
    }

    var url=getActionURL(getactionid_manage().waitRecord_downloadNotification);
    var setdata = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };


    ajaxSubmitByJson(url, setdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var recorddownurl_html=data.recorddownurl_html;

                var base_filesave=data.base_filesave;

                if (isNotEmpty(recorddownurl_html)) {
                    previewgetNotifications_index = layer.open({
                        type:2,
                        title:'阅读告知书',
                        content:recorddownurl_html,
                        shadeClose:false,
                        maxmin: true,
                        shade:0,
                        area: ['50%', '700px']
                        ,btn: ['开始朗读', '取消'],
                        id:"notification_read"
                        ,yes: function(index, layero){
                            var dis=$("#layui-layer"+previewgetNotifications_index).find(".layui-layer-btn0").attr('disabled');
                            if (isNotEmpty(dis)){
                                layer.msg("朗读中")
                                return;
                            }

                            if (!isNotEmpty(gnlist)||!gnlist.includes(TTS_F)){
                                layer.msg("请先获取语音播报授权")
                                return;
                            }
                            layer.msg("加载中，请稍等...", {
                                icon: 16,
                                time:1000
                            });

                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;


                            //点击了
                            dqrecorddownurl_htmlreads=data.recorddownurl_htmlreads;
                            if (isNotEmpty(dqrecorddownurl_htmlreads)){
                                t1 = window.setInterval(function (args) {
                                    var text=dqrecorddownurl_htmlreads[len];
                                    if (!isNotEmpty(audioplay)&&len==0){
                                        console.log("audioplay：初始化")
                                        str2Tts(text);
                                        len++;
                                    } else if (isNotEmpty(audioplay)&&audioplay.ended) {
                                        console.log("audioplay.ended："+audioplay.ended)
                                        audioplay=null;
                                        str2Tts(text);
                                        len++;
                                    }
                                    if (len>dqrecorddownurl_htmlreads.length-1){
                                        clearInterval(t1);
                                    }
                                },500);
                            }else {
                                layer.msg("未找到需要朗读的文本");
                            }
                        }
                        ,btn2: function(index, layero){
                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;
                            layer.close(index)
                        }
                        ,cancel: function(index, layero){
                            clearInterval(t1);
                            if (isNotEmpty(audioplay)){
                                audioplay.pause();
                            }
                            audioplay=null;
                            len=0;
                            layer.close(index)
                        }
                    });
                }else {
                    layer.msg("文件未找到，可尝试下载预览");
                }
            }
        }else{
            layer.msg(data.message,{icon: 5});
        }
    });

}

//告知书朗读
function str2Tts(text) {
    if (isNotEmpty(text)){
        var url=getUrl_manage().str2Tts;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                text:text
            }
        };
        ajaxSubmitByJson(url, data, callbackstr2Tts);
    }
}

var audioplay=null;
function callbackstr2Tts(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var uploadpath=data.uploadpath;
            if (isNotEmpty(uploadpath)){
                console.log("开始朗读："+uploadpath)
                $("#layui-layer"+previewgetNotifications_index).find(".layui-layer-btn0").text("朗读中").attr('disabled',true);
                audioplay =new Audio();
                audioplay.src = uploadpath;
                audioplay.play();
            }
        }
    }else {
        layer.msg(data.message,{icon: 5});
    }
}

//*******************************************************************告知书start****************************************************************//

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


//*******************************************************************关键字start****************************************************************//
//已经废弃
function checkKeyword(txt) {
    var url=getActionURL(getactionid_manage().waitRecord_checkKeyword);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            txt:txt
        }
    };

    $.ajax({
        url: url,
        type: "POST",
        async: false,
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(data),
        timeout: 60000,
        success: function callbackcheckKeyword(data) {
            if (null != data && data.actioncode == 'SUCCESS') {
                var vo = data.data;
                if (isNotEmpty(vo)) {
                    txt = vo.txt;
                    console.log(txt)
                    return txt;
                }
            } else {
                console.log(data.message);
            }
        }
    });
    return txt;
}


//*******************************************************************关键字end****************************************************************//

//*******************************************************************获取各个状态start****************************************************************//
function  getEquipmentsState() {
    if (isNotEmpty(mtssid)&&isNotEmpty(MCCache)){
        var recordnum=MCCache.recordnum==null?0:MCCache.recordnum;//本次会议开启的录音/像个数
        var asrnum=MCCache.asrnum==null?0:MCCache.asrnum;//本次会议开启的语音识别个数
        var polygraphnum=MCCache.polygraphnum==null?0:MCCache.polygraphnum;//本次会议开启的测谎仪个数

        var fdrecord=TDCache.fdrecord==null?-1:TDCache.fdrecord;//是否需要录像，1使用，-1 不使用
        var usepolygraph=TDCache.usepolygraph==null?-1:TDCache.usepolygraph;//是否使用测谎仪，1使用，-1 不使用
        var useasr=TDCache.useasr==null?-1:TDCache.useasr;//是否使用语言识别，1使用，-1 不使用

        var url=getUrl_manage().getEquipmentsState;
        var data = {
            token: INIT_CLIENTKEY,
            param: {
                mtssid: mtssid,
                fdrecord:fdrecord,
                usepolygraph:usepolygraph,
                useasr:useasr,
                recordnum:recordnum,
                asrnum:asrnum,
                polygraphnum:polygraphnum
            }
        };

        ajaxSubmitByJson(url, data, callbackgetEquipmentsState);
    }else{
        console.log("设备状态信息位置未找到__"+mtssid);
    }
}
function callbackgetEquipmentsState(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        var data = data.data;
        if (isNotEmpty(data)) {
            //状态： -1异常  0未启动 1正常
            var MtText = "加载中";
            var AsrText = "加载中";
            var LiveText = "加载中";
            var PolygraphText = "加载中";
            var MtClass = "layui-badge layui-bg-gray";
            var AsrClass = "layui-badge layui-bg-gray";
            var LiveClass = "layui-badge layui-bg-gray";
            var PolygraphClass = "layui-badge layui-bg-gray";

            var MtState = data.mtState;
            $("#mtbool_txt").html("正常检测");
            if (MtState == 0) {
                MtText = "未启动";
                MtClass = "layui-badge layui-bg-gray";
            } else if (MtState == 1) {
                MtText = "正常";
                MtClass = "layui-badge layui-bg-green";
            } else if (MtState == -1) {
                MtText = "异常";
                MtClass = "layui-badge";
            }else if (MtState == 3) {
                //==3 是真实的
                MtText = "暂停中";
                MtClass = "layui-badge layui-bg-gray";
                mcbool=MtState;
                $("#mtbool_txt").html(MtText)
            }
            var AsrState = data.asrState;
            if (AsrState == 0) {
                AsrText = "未启动";
                AsrClass = "layui-badge layui-bg-gray";
            } else if (AsrState == 1) {
                AsrText = "正常";
                AsrClass = "layui-badge layui-bg-green";
            } else if (AsrState == -1) {
                AsrText = "异常";
                AsrClass = "layui-badge";
            }
            var LiveState = data.liveState;
            if (LiveState == 0) {
                LiveText = "未启动";
                LiveClass = "layui-badge layui-bg-gray";
            } else if (LiveState == 1) {
                LiveText = "正常";
                LiveClass = "layui-badge layui-bg-green";
            } else if (LiveState == -1) {
                LiveText = "异常";
                LiveClass = "layui-badge";
            }
            var PolygraphState = data.polygraphState;
            if (PolygraphState == 0) {
                PolygraphText = "未启动";
                PolygraphClass = "layui-badge layui-bg-gray";
            } else if (PolygraphState == 1) {
                PolygraphText = "正常";
                PolygraphClass = "layui-badge layui-bg-green";
            } else if (PolygraphState == -1) {
                PolygraphText = "异常";
                PolygraphClass = "layui-badge";
            }


            $("#MtState").text(MtText);
            $("#MtState").attr({"MtState": MtState, "class": MtClass});
            $("#AsrState").text(AsrText);
            $("#AsrState").attr({"AsrState": AsrState, "class": AsrClass});
            $("#LiveState").text(LiveText);
            $("#LiveState").attr({"LiveState": LiveState, "class": LiveClass});
            $("#PolygraphState").text(PolygraphText);
            $("#PolygraphState").attr({"PolygraphState": PolygraphState, "class": PolygraphClass});

        }
    }else {
        $("#MtState").text("加载中");
        $("#MtState").attr({"MtState": "", "class": "layui-badge layui-bg-gray"});
        $("#AsrState").text("加载中");
        $("#AsrState").attr({"AsrState": "", "class": "layui-badge layui-bg-gray"});
        $("#LiveState").text("加载中");
        $("#LiveState").attr({"LiveState": "", "class": "layui-badge layui-bg-gray"});
        $("#PolygraphState").text("加载中");
        $("#PolygraphState").attr({"PolygraphState": "", "class": "layui-badge layui-bg-gray"});
    }
}
//*******************************************************************获取各个状态end****************************************************************//


//*******************************************************************案件人员信息编辑start****************************************************************//
var casetouser_iframe=null;
var casetouser_body=null;
function  open_casetouser() {
    layer.open({
        type: 2,
        title:'人员案件基本信息',
        content:tocaseToUserURL,
        area: ['80%', '90%'],
        btn: ['确定','取消'],
        success:function(layero, index){
            casetouser_iframe = window['layui-layer-iframe' + index];
            casetouser_body=layer.getChildFrame('body', index);
            casetouser_iframe.recordssid=recordssid;
            casetouser_iframe.setcaseToUser(getRecordById_data);

        },
        yes:function(index, layero){
            var formSubmit=layer.getChildFrame('body', index);
            var submited = formSubmit.find('#permissionSubmit')[0];
            submited.click();
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}
//*******************************************************************案件人员信息编辑end****************************************************************//