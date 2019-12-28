/*
 笔录通用：笔录制作前中后js
 */

//回放：定位差值
var open_positiontime_index=null;
var positiontime=0;
function open_positiontime(url) {
    var html='  <form class="layui-form site-inline" style="margin-top: 20px;padding-right: 35px;">\
               <div class="layui-form-item">\
                   <label class="layui-form-label"><span style="color: red;">*</span>定位差值</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="positiontimem" id="positiontimem" lay-verify="positiontimem" autocomplete="off" placeholder="请输入定位差值(秒)" value="' + parseFloat(positiontime)/1000 + '"  class="layui-input">\
                    </div>\
                     <div class="layui-form-mid layui-word-aux" style="float: right;margin-right: 0px">请输入差值在-10到-1或者1到10区间的值以及0(秒)</div>\
                </div>\
            </form>';
    layui.use(['layer','element','form','laydate'], function() {
        var form = layui.form;
        open_positiontime_index=layer.open({
            type:1,
            title:'编辑定位差值',
            content:html,
            area: ['25%', '30%'],
            btn: ['确定','取消'],
            success:function(layero, index){
                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();
            },
            yes:function(index, layero){
                //自定义验证规则
                form.verify({
                    positiontimem:function (value) {
                        if (!(/\S/).test(value)) {
                            return "请输入定位差值";
                        }
                        if (!((value<=-1&&value>=-10)||(value<=10&&value>=1)||value==0)) {
                            return "请输入差值在-10到-1或者1到10区间的值以及0(秒)";
                        }
                    }
                });
                //监听提交
                form.on('submit(fromContent)', function(data){
                    updateRecord(url);
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
    });
}
function updateRecord(url){
    var positiontime=$("#positiontimem").val();
    if (!isNotEmpty(positiontime)){
        layer.msg("请输入定位差值",{icon:5});
        return;
    }

    positiontime=parseFloat(positiontime)*1000;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: recordssid,
            positiontime:positiontime
        }
    };
    ajaxSubmitByJson(url, data, callbackupdateRecord);
}
function callbackupdateRecord(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var param=data.param;
            if (isNotEmpty(param)){
                layer.close(open_positiontime_index);
                positiontime=param.positiontime;//更新值
                layer.msg("保存成功",{icon:6,time:500},function () {
                    $("#positiontime").val(parseFloat(positiontime)/1000);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


//制作中：获取会议缓存以及会议通道缓存
function getMCCacheParamByMTssid() {
    if (mcbool==1||mcbool==3){
        var url=getUrl_manage().getMCCacheParamByMTssid;
        var d={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
            }
        };
        ajaxSubmitByJson(url, d, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    MCCache=data;
                }
            }
        });
    }
}
function getTDCacheParamByMTssid() {
    if ((mcbool==1||mcbool==3)&&isNotEmpty(dq_recorduser)) {//会议正常的时候
        var url=getUrl_manage().getTDCacheParamByMTssid;
        var d={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid,
                userssid:dq_recorduser,
            }
        };

        ajaxSubmitByJson(url, d, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    TDCache=data;
                    fdrecordstarttime=data.fdrecordstarttime==null?0:data.fdrecordstarttime;

                    //第一行上时间:视频时间：用于问答笔录
                    var lable=  $('#first_originaltr label[name="q"]');
                    if (isNotEmpty(lable)){
                        setFocus(lable);
                    }

                }
            }
        });
    }
}

//制作中：笔录实时保存本地
function setRecordProtect(url) {
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
            mtssid:mtssid,
        }
    };
    ajaxSubmitByJson(url, data, callbacksetRecordProtect);
}
function callbacksetRecordProtect(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("笔录实时本地保存成功__"+data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//制作中：伸缩按钮
function shrink(obj) {
    var shrink_bool=$(obj).attr("shrink_bool");
    if (shrink_bool==1){

        $("#shrink_html").hide();
        $(obj).attr("shrink_bool","-1");
        $("i",obj).attr("class","layui-icon layui-icon-spread-left");

        if (isNotEmpty(gnlist)&&gnlist.indexOf(FY_T)!= -1){
            //法院版本的
            $("#notshrink_html1").attr("class","layui-col-md12");
        }else {
            $("#notshrink_html1").attr("class","layui-col-md9");
        }

        $("#layui-layer"+recordstate_index).hide();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }else{
        $("#shrink_html").show();
        $(obj).attr("shrink_bool","1");
        $("i",obj).attr("class","layui-icon layui-icon-shrink-right");


        if (isNotEmpty(gnlist)&&gnlist.indexOf(FY_T)!= -1){
            //法院版本的
            $("#notshrink_html1").attr("class","layui-col-md9");
        }else {
            $("#notshrink_html1").attr("class","layui-col-md6");
        }

        $("#layui-layer"+recordstate_index).show();
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#www").width())});
    }
}

//制作中：会议状态
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


//制作中：告知书=========================================================================start
var notificationListdata=null;
var getNotificationsUrl=null;//获取告知书列表地址
var uploadNotificationUrl=null;//上传告知书地址
var downloadNotificationUrl=null;//下载告知书地址
function getNotifications() {
    var data={
        token:INIT_CLIENTKEY,
        param:{
            currPage:1,
            pageSize:100
        }
    };
    ajaxSubmitByJson(getNotificationsUrl, data, function (data) {
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

            if (isNotEmpty(uploadNotificationUrl)){
                //执行实例
                var uploadInst = upload.render({
                    elem: "#uploadFile" //绑定元素
                    ,url: uploadNotificationUrl //上传接口
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
            } else {
                console.log("上传告知书请求地址 is null")
            }


        });
    })
}

//获取告知书列表
function open_getNotifications(getNotificationsUrl1,uploadNotificationUrl1,downloadNotificationUrl1) {
     getNotificationsUrl=getNotificationsUrl1;//获取告知书列表地址
     uploadNotificationUrl=uploadNotificationUrl1;//上传告知书地址
     downloadNotificationUrl=downloadNotificationUrl1;//下载告知书地址
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
    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };
    ajaxSubmitByJson(downloadNotificationUrl, data, function (data) {
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



    var setdata = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };


    ajaxSubmitByJson(downloadNotificationUrl, setdata, function (data) {
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
//制作中：告知书=======================================================================end

//制作中：结束笔录按钮
var overRecord_index=null;
var overRecord_loadindex =null;
function overRecord(state) {
    var msgtxt2="是否结束？";
    if (state==1){
        msgtxt2="是否暂停？";
    }
    var msgtxt="";
    if (isNotEmpty(fdStateInfo)) {
        var atxt=fdStateInfo.roma_status==null?"":fdStateInfo.roma_status;//1是刻录中
        var btxt=fdStateInfo.romb_status==null?"":fdStateInfo.romb_status;
        if (isNotEmpty(atxt)&&isNotEmpty(btxt)&&atxt=="1"||btxt=="1") {
            msgtxt="<span style='color: red'>*存在光驱正在刻录中，审讯关闭将会停止刻录</span>"
        }
    }
    msgtxt=msgtxt2+'<br/>'+msgtxt;
    layer.confirm(msgtxt, {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        //自动甄别关闭
        $("#record_switch_bool").attr("isn",-1);
        $("#record_switch_bool").removeClass("layui-form-onswitch");
        $("#record_switch_bool").find("em").html("关闭");


        overRecord_index=index;
        recordbool=2;//笔录已完成状态
        if (state==1){
            casebool=3;//需要暂停：案件休庭
        }

        addRecord();//调用保存
        overRecord_loadindex = layer.msg("保存中，请稍等...", {
            typy:1,
            icon: 16,
            shade: [0.1, 'transparent'],
            time:10000
        });
    }, function(index){
        layer.close(index);
    });
}

//制作中：会议暂停或者继续 pauseOrContinue 1请求暂停，2请求继续
function pauseOrContinueRercord(pauseOrContinue) {
    if (isNotEmpty(mtssid)){
        var url=getUrl_manage().pauseOrContinueRercord;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
                ,pauseOrContinue:pauseOrContinue
            }
        };
        ajaxSubmitByJson(url, data, callbackpauseOrContinueRercord);
    }
}
function callbackpauseOrContinueRercord(data) {
    layer.close(startMC_index);
    getMCCacheParamByMTssid();//获取缓存
    getTDCacheParamByMTssid();
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            //1请求暂停，2请求继续
            var pauseOrContinue=data.pauseOrContinue;
            var msg=pauseOrContinue==null?"":(pauseOrContinue==1?"暂停":"继续");
            var recordnum=data.recordnum;//录音设备暂停/停止个数
            var asrnum=data.asrnum;//语音识别服务暂停/停止个数
            var polygraphnum=data.polygraphnum;//测谎仪服务暂停/停止个数

            var con=msg+"：<br>设备"+msg+"数："+recordnum;
            if (isNotEmpty(gnlist)&&gnlist.indexOf(ASR_F)>0&&asrbool>0){
                con+="<br>语音识别"+msg+"数："+asrnum;
            }
            if (isNotEmpty(gnlist)&&gnlist.indexOf(PH_F)>0&&phbool>0){
                con+="<br>身心监测开"+msg+"数："+polygraphnum;
            }
            layer.msg(con, {time: 2000});
            if (pauseOrContinue==1){
                $("#pauserecord").css("display","inline-block");
                $("#start_over_btn").text("继续笔录").attr("onclick","img_bool(this,1)");
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 1});
                    });
            } else {
                $("#startrecord").css("display","inline-block");
                $("#start_over_btn").text("暂停笔录").attr("onclick","img_bool(this,2)");
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips: 1});
                    });
            }

        }
    }else {
        var data2=data.data;
        if (isNotEmpty(data2)){
            var pauseOrContinue=data2.pauseOrContinue;
            $("#record_img img").css("display","none");
            if (pauseOrContinue==1){//请求暂停
                $("#startrecord").css("display","inline-block");
                if (gnlist.indexOf(HK_O)== -1){
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips('点击我可以暂停制作~' ,'#startrecord',{time:0, tips: 1});
                    });
                }
            } else if (pauseOrContinue==2){//请求继续
                $("#pauserecord").css("display","inline-block").attr("onclick","img_bool(this,1);");
                if (gnlist.indexOf(HK_O)== -1){
                    layui.use(['layer','element','form'], function(){
                        var layer=layui.layer;
                        layer.tips('点击我可以再次开启制作~' ,'#pauserecord',{time:0, tips: 1});
                    });
                }
            }
        }
        console.log(data)
        layer.msg(data.message,{icon: 5});
    }
}



