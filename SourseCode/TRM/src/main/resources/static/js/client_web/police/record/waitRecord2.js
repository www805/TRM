/**
 笔录分出来的js
 **/

//*******************************************************************图表区域start****************************************************************//
/**
 * 监测数据
 */
function getPolygraphdata() {
    if (isNotEmpty(mtssid)) {
        var url=getUrl_manage().getPolygraphdata;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetPolygraphdata);
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
                myChart.hideLoading();
                var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                var br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                var  status=obj.status;
                var status_text="未知";
                if (status==0){
                    status_text="正常";
                }else if (status==1){
                    status_text="紧张";
                }else if (status==2){
                    status_text="生理疲劳";
                }else if (status==3){
                    status_text="昏昏欲睡";
                }
                var relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                var bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                var spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                var hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);

                $("#xthtml #xt1").html(' '+status_text+'   ');
                $("#xthtml #xt2").html(' '+relax+'  ');
                $("#xthtml #xt3").html(' '+stress+'  ');
                $("#xthtml #xt4").html(' '+bp+'  ');
                $("#xthtml #xt5").html(' '+spo2+'  ');
                $("#xthtml #xt6").html(' '+hr+'  ');
                $("#xthtml #xt7").html(' '+hrv+'  ');
                $("#xthtml #xt8").html(' '+br+'  ');



                if (isNotEmpty(select_monitorall_iframe_body)) {
                    select_monitorall_iframe_body.find("#monitorall #xt1").html(' '+status_text+'   ');
                    select_monitorall_iframe_body.find("#monitorall #xt2").html(' '+relax+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt3").html(' '+stress+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt4").html(' '+bp+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt5").html(' '+spo2+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt6").html(' '+hr+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt7").html(' '+hrv+'  ');
                    select_monitorall_iframe_body.find("#monitorall #xt8").html(' '+br+'  ');
                }

                var hr_snr=obj.hr_snr.toFixed(1)==null?0:obj.hr_snr.toFixed(1);
                var fps=obj.fps.toFixed(1)==null?0:obj.fps.toFixed(1);
                var stress_snr=obj.stress_snr.toFixed(1)==null?0:obj.stress_snr.toFixed(1);
                var snrtext="fps：0&nbsp;hr_snr：0&nbsp;stress_snr：0";
                snrtext="fps："+fps+"&nbsp;hr_snr："+hr_snr+"&nbsp;stress_snr："+stress_snr+"";


                $("#snrtext").html(snrtext);
                $("#snrtext3").html(snrtext);

                var tsmsg="身心监测加载中...";
                var tscss={"color":" #31708f","background-color": "#d9edf7"," border-color": "#bce8f1"};
                if (isNotEmpty(hr_snr)&&hr_snr>=0.1){
                    tsmsg="身心准确监测中";
                    tscss={"color": "#3c763d","background-color":"#dff0d8","border-color":"#d6e9c6"};
                }else{
                    tsmsg="监测准确度较低，请调整光线，减少身体晃动";
                    tscss={"color": "#a94442","background-color":"#f2dede","border-color":"#ebccd1"};
                }

                $("#showmsg,#open_showmsg").css(tscss);
                $("#showmsg strong,#open_showmsg strong").text(tsmsg);
                if (isNotEmpty(select_monitorall_iframe_body)) {
                    select_monitorall_iframe_body.find("#open_showmsg").css(tscss);
                    select_monitorall_iframe_body.find("#open_showmsg strong").text(tsmsg);
                    select_monitorall_iframe_body.find("#snrtext2").html(snrtext);
                }

                addData_hr(true,hr);
                addData_hrv(true,hrv);
                addData_br(true,br);
                addData_relax(true,relax);
                addData_stress(true,stress);
                addData_bp(true,bp);
                addData_spo2(true,spo2);

                var dqmarkLinedata=[];
                var dqmarkLinedata_hr=[{ yAxis: 60}, {yAxis: 100}];
                var dqmarkLinedata_hrv=[{yAxis: -10}, { yAxis: 10 }];
                var dqmarkLinedata_br=[{yAxis: 12}, { yAxis: 20 }];
                var dqmarkLinedata_relax=[];
                var dqmarkLinedata_stress=[{yAxis: 30}, { yAxis: 50 }, { yAxis: 70 }, { yAxis: 100 }];
                var dqmarkLinedata_bp=[{yAxis: -10}, { yAxis: 10 }];
                var dqmarkLinedata_spo2=[{yAxis: 94}];

                $("#monitor_btn span").each(function (e) {
                    var type=$(this).attr("type");
                    var name=$(this).text();
                    var isn=$(this).attr("isn");
                    if (isn==1){
                        if (type=="hr") {
                            date1=date_hr;
                            data1=data_hr;
                            dqmarkLinedata=dqmarkLinedata_hr;

                        }else if (type=="hrv") {
                            date1=date_hrv;
                            data1=data_hrv;
                            dqmarkLinedata=dqmarkLinedata_hrv;

                        }else if (type=="br") {
                            date1=date_br;
                            data1=data_br;
                            dqmarkLinedata=dqmarkLinedata_br;

                        }else if (type=="relax") {
                            date1=date_relax;
                            data1=data_relax;
                        }else if (type=="stress") {
                            date1=date_stress;
                            data1=data_stress;
                            dqmarkLinedata=dqmarkLinedata_stress;

                        }else if (type=="bp") {
                            date1=date_bp;
                            data1=data_bp;
                            dqmarkLinedata=dqmarkLinedata_bp;

                        }else if (type=="spo2") {
                            date1=date_spo2;
                            data1=data_spo2;
                            dqmarkLinedata=dqmarkLinedata_spo2;
                        }
                    }
                });
                myChart.setOption({
                    xAxis: {
                        data: date1
                    },
                    series: [{
                        data: data1
                    }]
                });

                myChart.setOption({
                    xAxis: {
                        data: date1
                    },
                    series: [{
                        data: data1,
                        markLine: {
                            data: dqmarkLinedata
                        }
                    }]
                });

                if (null!=select_monitorall_iframe){
                    select_monitorall_iframe.myMonitorall.setOption({
                        xAxis: {
                            data: date_hr
                        },
                        series: [{
                            data: data_hr
                            ,markLine: {
                                data: dqmarkLinedata_hr
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall2.setOption({
                        xAxis: {
                            data: date_hrv
                        },
                        series: [{
                            data: data_hrv
                            ,markLine: {
                                data: dqmarkLinedata_hrv
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall3.setOption({
                        xAxis: {
                            data: date_br
                        },
                        series: [{
                            data: data_br
                            ,markLine: {
                                data: dqmarkLinedata_br
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall4.setOption({
                        xAxis: {
                            data: date_relax
                        },
                        series: [{
                            data: data_relax
                            , markLine: {
                                data: dqmarkLinedata_relax
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall5.setOption({
                        xAxis: {
                            data: date_stress
                        },
                        series: [{
                            data: data_stress
                            , markLine: {
                                data: dqmarkLinedata_stress
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall6.setOption({
                        xAxis: {
                            data: date_bp
                        },
                        series: [{
                            data: data_bp
                            ,markLine: {
                                data: dqmarkLinedata_bp
                            }
                        }]
                    });
                    select_monitorall_iframe.myMonitorall7.setOption({
                        xAxis: {
                            data: date_spo2
                        },
                        series: [{
                            data: data_spo2
                            ,markLine: {
                                data: dqmarkLinedata_spo2
                            }
                        }]
                    });
                }
            }
        }
    }else{
        /* layer.msg(data.message);*///不需要弹出错误信息
    }
}



/**
 * 身心监测按钮组
 */
function select_monitor(obj) {
    $(obj).attr("isn","1");
    $(obj).siblings().attr("isn","-1");

    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    myChart.showLoading();
    var name=$(obj).text();
    myChart.setOption({
        title: {
            text: name,
        },
        series: [{
            name:name,
        }]
    });
}


//显示全部图表

var option = {
    title: {
        text: '心率',
    },
    tooltip: {
        trigger: 'axis',
        formatter: '{a}: {c}'
    },
    xAxis: {
        type: 'category',
        splitLine: {
            show: false
        },
        show: false,
    },
    yAxis: {
        type: 'value',
        boundaryGap: [0, '100%'],
        splitLine: {
            show: false
        },
        show: true
    },
    grid: {
        x:30,
        y:45,
        x2:30,
        y2:10,
    },
    series: [{
        name: '心率',
        type: 'line',
        showSymbol: false,
        hoverAnimation: false,
        itemStyle : {
            normal : {
                color:'#00FF00',
            }
        },
        markLine: {//警戒线标识
            silent: true,
            lineStyle: {
                normal: {
                    color: 'red'                   // 这儿设置安全基线颜色
                }
            },
        }
    }]
};
var select_monitorall_iframe=null;
var select_monitorall_iframe_body=null;

function select_monitorall(obj) {
    layer.open({
        type: 2
        , skin: 'layui-layer-lan' //样式类名
        ,title: "身心检测"
        ,area: ['40%','98%']
        ,shade: 0
        ,id: 'layer_monitorall' //设定一个id，防止重复弹出
        ,offset: 'l'
        ,resize: true
        ,content: togetPolygraphurl
        ,success:function (layero,index) {
            select_monitorall_iframe = window['layui-layer-iframe' + index];
            select_monitorall_iframe_body=layer.getChildFrame('body', index);
            select_monitorall_iframe.monitorall1(option);
            select_monitorall_iframe.myMonitorall.setOption({
                title: {
                    text: "心率",
                },
                xAxis: {
                    data: date_br
                },
                series: [{
                    name:"心率",
                    data: data_br
                }]
            });
            select_monitorall_iframe.monitorall2(option);
            select_monitorall_iframe.myMonitorall2.setOption({
                title: {
                    text: "心率变异",
                },
                xAxis: {
                    data: date_hrv
                },
                series: [{
                    name:"心率变异",
                    data: data_hrv
                }]
            });
            select_monitorall_iframe.monitorall3(option);
            select_monitorall_iframe.myMonitorall3.setOption({
                title: {
                    text: "呼吸次数",
                },
                xAxis: {
                    data: date_br
                },
                series: [{
                    name:"呼吸次数",
                    data: data_br
                }]
            });
            select_monitorall_iframe.monitorall4(option);
            select_monitorall_iframe.myMonitorall4.setOption({
                title: {
                    text: "放松值",
                },
                xAxis: {
                    data: date_relax
                },
                series: [{
                    name:"放松值",
                    data: data_relax
                }]
            });
            select_monitorall_iframe.monitorall5(option);
            select_monitorall_iframe.myMonitorall5.setOption({
                title: {
                    text: "紧张值",
                },
                xAxis: {
                    data: date_stress
                },
                series: [{
                    name:"紧张值",
                    data: data_stress
                }]
            });
            select_monitorall_iframe.monitorall6(option);
            select_monitorall_iframe.myMonitorall6.setOption({
                title: {
                    text: "血压变化",
                },
                xAxis: {
                    data: date_bp
                },
                series: [{
                    name:"血压变化",
                    data: data_bp
                }]
            });
            select_monitorall_iframe.monitorall7(option);
            select_monitorall_iframe.myMonitorall7.setOption({
                title: {
                    text: "血氧",
                },
                xAxis: {
                    data: date_spo2
                },
                series: [{
                    name:"血氧",
                    data: data_spo2
                }]
            });
        },
        cancel: function(index, layero){
            select_monitorall_iframe=null;
            select_monitorall_iframe_body=null;
            layer.close(index)
        }
    });
}


/*初始化数据--------------------------------------------start-----------------------------------*/
var myChart;
var date1 = [];
var data1 = [];

var init_br = 1;
var date_br = [];
var data_br = [];
function addData_br(shift,data) {
    init_br++;
    date_br.push(init_br);
    data_br.push(data);
    if (shift) {
        date_br.shift();
        data_br.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_br(false,0);
}

var init_bp = 1;
var date_bp = [];
var data_bp = [];
function addData_bp(shift,data) {
    init_bp++;
    date_bp.push(init_bp);
    data_bp.push(data);
    if (shift) {
        date_bp.shift();
        data_bp.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_bp(false,0);
}

var init_hr = 1;
var date_hr = [];
var data_hr = [];
function addData_hr(shift,data) {
    init_hr++;
    date_hr.push(init_hr);
    data_hr.push(data);
    if (shift) {
        date_hr.shift();
        data_hr.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hr(false,0);
}

date1=date_hr;
data1=data_hr;
var init_hrv = 1;
var date_hrv = [];
var data_hrv = [];
function addData_hrv(shift,data) {
    init_hrv++;
    date_hrv.push(init_hrv);
    data_hrv.push(data);
    if (shift) {
        date_hrv.shift();
        data_hrv.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hrv(false,0);
}

var init_relax = 1;
var date_relax = [];
var data_relax = [];
function addData_relax(shift,data) {
    init_relax++;
    date_relax.push(init_relax);
    data_relax.push(data);
    if (shift) {
        date_relax.shift();
        data_relax.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_relax(false,0);
}

var init_spo2 = 1;
var date_spo2 = [];
var data_spo2 = [];
function addData_spo2(shift,data) {
    init_spo2++;
    date_spo2.push(init_spo2);
    data_spo2.push(data);
    if (shift) {
        date_spo2.shift();
        data_spo2.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_spo2(false,0);
}

var init_stress = 1;
var date_stress = [];
var data_stress = [];
function addData_stress(shift,data) {
    init_stress++;
    date_stress.push(init_stress);
    data_stress.push(data);
    if (shift) {
        date_stress.shift();
        data_stress.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_stress(false,0);
}
/*初始化数据--------------------------------------------end-----------------------------------*/
function main1() {
    $("#main1").css( 'width',$("#leftdiv").width());
    $(window).resize(function() {
        myChart.resize();
    });
    myChart = echarts.init(document.getElementById('main1'),'dark');
    myChart.setOption(option);
}
//*******************************************************************图表区域end****************************************************************//


//*******************************************************************伸缩按钮start****************************************************************//
function shrink(obj) {
    var shrink_bool=$(obj).attr("shrink_bool");
    if (shrink_bool==1){
        $("#shrink_html").hide();
        $(obj).attr("shrink_bool","-1");
        $("i",obj).attr("class","layui-icon layui-icon-spread-left");
        $("#notshrink_html").attr("class","layui-col-md12");

        $("#layui-layer"+recordstate_index).hide();

    }else{
        $("#shrink_html").show();
        $(obj).attr("shrink_bool","1");
        $("#notshrink_html").attr("class","layui-col-md9");
        $("i",obj).attr("class","layui-icon layui-icon-shrink-right");
        $("#layui-layer"+recordstate_index).show();
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
                }
            }
        }else{
            layer.msg(data.message);
        }
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
                    layer.msg("下载中，请稍后...");
                    window.location.href=recorddownurl;
                }
            }
        }else{
            layer.msg(data.message);
        }
    });
}

//打开告知书
var previewgetNotifications_index=null;
var dqrecorddownurl_htmlread=null;//读取阅读txt

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

                            if (!isNotEmpty(gnlist)||!gnlist.includes("tts")){
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
                            dqrecorddownurl_htmlread=data.recorddownurl_htmlread;
                            if (isNotEmpty(dqrecorddownurl_htmlread)){




                                var arr=new Array();
                                arr=dqrecorddownurl_htmlread.split(/[，|,]/);//注split可以用字符或字符串分割
                                for (var i = 0; i < arr.length; i++) {
                                    if(!isNotEmpty(arr[i])){
                                        arr.splice(i,1);
                                        i = i-1;
                                    }
                                }
                                t1 = window.setInterval(function (args) {
                                    var text=arr[len];
                                    if (audioplay==null){
                                        str2Tts(text);
                                        len++;
                                    } else if (audioplay.ended) {
                                        str2Tts(text);
                                        len++;
                                    }
                                    if (len>arr.length-1){
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
            layer.msg(data.message);
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
                $("#layui-layer"+previewgetNotifications_index).find(".layui-layer-btn0").text("朗读中").attr('disabled',true);
                audioplay =new Audio();
                audioplay.src = uploadpath;
                audioplay.play();
            }
        }
    }else {
        layer.msg(data.message);
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
        for (let i = 0; i < likerealtxtarr.length; i++) {
            const all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        $("#recordreals_scrollhtml").animate({scrollTop:top},500);
    }
}
function recordreals_select() {
    mouseoverbool_left=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];
    $("#recordreals div").each(function (i,e) {
        var spantxt=$(this).find("span").text();
        $(this).find("span").html(spantxt);
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

//*******************************************************************左侧授权模块显示start****************************************************************//
function getgnlist() {
    var url=getActionURL(getactionid_manage().waitRecord_gnlist);
    var data={
        token:INIT_CLIENTKEY,
        param:{

        }
    };
    ajaxSubmitByJson(url, data, callbackgnlist);
}
var gnlist=null;
function callbackgnlist(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var lists=data.lists;
            if (isNotEmpty(lists)){
                gnlist=lists;
                if (!isNotEmpty(gnlist)||!gnlist.includes("record")){
                    layer.msg("请先获取笔录授权",{time:2000,icon:16,shade: 0.3},function () {
                        window.history.go(-1);
                        return false;
                    })
                }
                //控制显示
                for (let i = 0; i < gnlist.length; i++) {
                    var list = gnlist[i];
                    if (list=="asr") {
                        /*  $("#asr").show();
                          $("#initec ul li").removeClass("layui-this");
                          $("#initec .layui-tab-item").removeClass("layui-show");
                          $("#asr").addClass("layui-this");
                          $("#asritem").addClass("layui-show");*/
                    }else if (list=="fd") {
                        /*$("#fd").show();*/
                    }else if (list=="ph") {
                        /* $("#ph").show();*/
                        $("#xthtml").css("visibility","visible");
                    }
                }
            }
        }
    }else {
        layer.msg(data.message);
    }
}
//*******************************************************************左侧授权模块显示endt****************************************************************//





