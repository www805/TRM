var recorduser=[];//会议用户集合
var mtssid=null;//当前会议的ssid
var iid=null;
var videourl=null;//视频地址
var iidno=0;//是否第一次获取iid

var recordnameshow="";

var subtractime_q=0;//问的时间差
var subtractime_w=0;//答的时间差


var pdfdownurl=null;//pdf下载地址
var worddownurl=null;//word下载地址


var problems=null;//问答

/*弹出框数据*/
function opneModal_1() {
    var url=getActionURL(getactionid_manage().getRecordById_tomoreRecord);

    var index = layer.open({
        type: 2,
        title:'笔录选择',
        content:url,
        area: ['1000px', '680px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            var recordssid_go = $(window.frames["layui-layer-iframe"+index])[0].recordssid_go;
            recordssid=recordssid_go;
            iid=null;
            videourl=null;
            iidno=0;
            getRecordById();
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

/**
 * 局部刷新
 */
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getRecords_init(currPage,pageSize);
    }else if (len==2){
        getRecords('',arguments[0],arguments[1]);
    }else if(len>2){
        getRecords(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
    }

}
function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var recordtypessid=pageparam.recordtypessid;
        var recordname=pageparam.recordname;


        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
    }

}


//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getRecordById_getRecordById);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid:recordssid,
            }
        };
        ajaxSubmitByJson(url,data,callbackgetRecordById);
    }else{
        console.log("笔录信息未找到__"+recordssid);
    }
}

function setqw() {
    $("#recorddetail").html("");
    for (var z = 0; z< problems.length;z++) {
        var problem = problems[z];

        var problemstarttime=problem.starttime;
        var q_starttime=parseFloat(problemstarttime)+parseFloat(subtractime_q);

        var problemtext=problem.problem==null?"未知":problem.problem;
        var problemhtml='<tr ondblclick="showrecord('+q_starttime+',1,this)" times='+q_starttime+'><td class="font_red_color">问：'+problemtext+' </td></tr>';
        var answers=problem.answers;
        if (isNotEmpty(answers)){
            for (var j = 0; j < answers.length; j++) {
                var answer = answers[j];

                var answerstarttime=answer.starttime;
                var w_starttime=parseFloat(answerstarttime)+parseFloat(subtractime_w);

                var answertext=answer.answer==null?"未知":answer.answer;
                problemhtml+='<tr ondblclick="showrecord('+w_starttime+',2,this)" times='+w_starttime+'> <td class="font_blue_color" >答：'+answertext+' </td></tr>';
            }
        }else{
            problemhtml+='<tr> <td class="font_blue_color">答： </td></tr>';
        }
        $("#recorddetail").append(problemhtml);
    }
}
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var record=data.record;
             pdfdownurl=record.pdfdownurl;//pdf下载地址
             worddownurl=record.worddownurl;//word下载地址

            //提讯数据
            var police_arraignment=record.police_arraignment;
            if (isNotEmpty(police_arraignment)){
                mtssid=police_arraignment.mtssid;//获取会议mtssid
                if (!isNotEmpty(mtssid)) {
                    //不存在会议
                }
            }

            recordnameshow=record.recordname;//当前笔录名称
            getRecord();//实时数据获取
            getPHDataBack();//身心回放获取


            var caseAndUserInfo=data.caseAndUserInfo;
            if (isNotEmpty(record)){
                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);

                    //问题答案
                    var problems1=record.problems;
                    $("#recorddetail").html("");
                    if (isNotEmpty(problems1)) {
                        problems=problems1;
                        setqw();
                    }

                    //会议人员
                    var recordUserInfosdata=record.recordUserInfos;
                    if (isNotEmpty(recordUserInfosdata)){
                        var user1={
                            username:recordUserInfosdata.username
                            ,userssid:recordUserInfosdata.userssid
                            ,grade:2
                        };
                        var user2={
                            username:recordUserInfosdata.adminname
                            ,userssid:recordUserInfosdata.adminssid
                            ,grade:1
                        };
                        recorduser.push(user1);
                        recorduser.push(user2);
                    }
            }

            //案件信息
            $("#caseAndUserInfo_html").html("");
            if (isNotEmpty(caseAndUserInfo)){
                var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+caseAndUserInfo.casename+"</td></tr>\
                                  <tr><td>案件人</td><td>"+caseAndUserInfo.username+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+caseAndUserInfo.cause+"'>"+caseAndUserInfo.cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+caseAndUserInfo.occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+caseAndUserInfo.casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+recordUserInfosdata.adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+recordUserInfosdata.otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordUserInfosdata.recordadminname+"</td> </tr>";
                $("#caseAndUserInfo_html").html(init_casehtml);
            }
        }
    }else{
        layer.msg(data.message);
    }
}


/**
 * 获取会议实时数据
 */
function getRecord() {
    $("#recordreals").html("");
    if (isNotEmpty(mtssid)) {
        var url=getUrl_manage().getRecord;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetRecord);
    }else{
       console.log("会议未找到__"+mtssid);
    }
}
function callbackgetRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var datas = data.data;
        var loadindex = layer.msg("加载中，请稍等...", {
            icon: 16,
            time:1000
        });
        if (isNotEmpty(datas)) {
            layer.close(loadindex);
            var list=datas.list;

            iid=datas.iid;
            if (iidno==0){
                getPlayUrl();
                iidno=1;
            }
            for (var i = 0; i < list.length; i++) {
                var data=list[i];
                if (isNotEmpty(recorduser)){
                    for (var j = 0; j < recorduser.length; j++) {
                        var user = recorduser[j];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var asrtime=data.asrtime;//时间
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var subtractime=data.subtractime;//时间差
                            //实时会议数据
                            var recordrealshtml="";


                            //实时会议数据
                            if (usertype==1){
                                subtractime_q=subtractime;
                                starttime=parseFloat(starttime)+parseFloat(subtractime_q);
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+','+usertype+',this)" times='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+'</p>\
                                                            <span>'+translatext+'</span> \
                                                      </div >';
                            }else if (usertype==2){
                                subtractime_w=subtractime;
                                starttime=parseFloat(starttime)+parseFloat(subtractime_w);
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+','+usertype+',this)" times='+starttime+'>\
                                                           <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span>'+translatext+'</span> \
                                                      </div >';
                            }



                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").remove();
                            }
                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals');
                            div.scrollTop = div.scrollHeight;
                        }
                    }
                }
            }
            setqw();//重新setqw
        }
    }else{
        layer.msg(data.message);
    }
}

function btn(obj) {
    var selected=$(obj).closest("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $(obj).closest("div[name='btn_div']").attr("showorhide","true");
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $(obj).closest("div[name='btn_div']").attr("showorhide","false");
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}
function exportWord(obj){
    if (isNotEmpty(worddownurl)){
        window.location.href = worddownurl;
        layer.msg("导出成功,等待下载中...");
    }else {
        layer.msg("未找到笔录类型对应模板");
    }
    btn(obj);
}
function exportPdf(obj) {
     if (isNotEmpty(pdfdownurl)){
            layer.open({
                type: 2,
                title: '导出PDF笔录',
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                area: ['893px', '600px'],
                content: pdfdownurl
            });
            layer.msg("导出成功,等待下载中...");
        }else{
         layer.msg("未找到笔录类型对应模板");
        }
      btn(obj);
}
function getPlayUrl() {
    if (isNotEmpty(iid)) {
        var url=getUrl_manage().getPlayUrl;
        var data={
            iid: iid
        };
        ajaxSubmitByJson(url, data, callbackgetPlayUrl);
    }else{
        console.log("直播信息未找到__"+iid);
    }
}
function callbackgetPlayUrl(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
    var data=data.data;
        if (isNotEmpty(data)){
            var iiddata=data.iid;
            var recordFileParams=data.recordFileParams;
            var recordPlayParams=data.recordPlayParams;
            var state;
            if (isNotEmpty(recordFileParams)){
                for (var i = 0; i < recordFileParams.length; i++) {
                    var recordFile = recordFileParams[i];
                    state=recordFile.state;
                }
                if (isNotEmpty(recordPlayParams)&&state==2){
                    for (var i = 0; i < recordPlayParams.length; i++) {
                        var recordPlay = recordPlayParams[i];
                        videourl=recordPlay.playUrl;
                    }
                    initplayer();
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
}




//**身心统计回放
function getPHDataBack() {
    if (isNotEmpty(mtssid)) {
        var url=getUrl_manage().getPHDataBack;
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetPHDataBack);
    }else {
        console.log("会议未找到__"+mtssid);
    }
}

var phdatabackList=null;
function callbackgetPHDataBack(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var datas = data.data;
        if (isNotEmpty(datas)){
            phdatabackList=datas;
        }
    }else {
      layer.msg(data.message);
    }
}


var option = {
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
        type: 'line',
        showSymbol: false,
        hoverAnimation: false,
        itemStyle : {
            normal : {
                color:'#00FF00',
                lineStyle:{
                    color:'#00FF00'
                }
            }
        },
    }]
};

var myChart;
var date1 = [];
var data1 = [];
var init_br = 1;
var date_br = [];
var data_br = [];
function addData_br(shift,data,date) {
    date_br.push(date);
    data_br.push(data);
    if (shift) {
        date_br.shift();
        data_br.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_br++;
    addData_br(false,0,init_br);
}


var init_bp = 1;
var date_bp = [];
var data_bp = [];
function addData_bp(shift,data,date) {
    date_bp.push(date);
    data_bp.push(data);
    if (shift) {
        date_bp.shift();
        data_bp.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_bp++;
    addData_bp(false,0,init_bp);
}

var init_hr = 1;
var date_hr = [];
var data_hr = [];
function addData_hr(shift,data,date) {
    date_hr.push(date);
    data_hr.push(data);
    if (shift) {
        date_hr.shift();
        data_hr.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_hr++;
    addData_hr(false,0,init_hr);
}

date1=date_hr;
data1=data_hr;
var init_hrv = 1;
var date_hrv = [];
var data_hrv = [];
function addData_hrv(shift,data,date) {
    date_hrv.push(date);
    data_hrv.push(data);
    if (shift) {
        date_hrv.shift();
        data_hrv.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_hrv++;
    addData_hrv(false,0,init_hrv);
}

var init_relax = 1;
var date_relax = [];
var data_relax = [];
function addData_relax(shift,data,date) {
    date_relax.push(date);
    data_relax.push(data);
    if (shift) {
        date_relax.shift();
        data_relax.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_relax++;
    addData_relax(false,0,init_relax);
}

var init_spo2 = 1;
var date_spo2 = [];
var data_spo2 = [];
function addData_spo2(shift,data,date) {
    date_spo2.push(date);
    data_spo2.push(data);
    if (shift) {
        date_spo2.shift();
        data_spo2.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_spo2++;
    addData_spo2(false,0,init_spo2);
}

var init_stress = 1;
var date_stress = [];
var data_stress = [];
function addData_stress(shift,data,date) {
    date_stress.push(date);
    data_stress.push(data);
    if (shift) {
        date_stress.shift();
        data_stress.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_stress++;
    addData_stress(false,0,init_stress);
}


function main1() {
    $("#main1").css( 'width',$("#showmsg").width() );
    $(window).resize(function() {
        myChart.resize();
    });
    myChart = echarts.init(document.getElementById('main1'),'dark');
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
            data: date1
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
                    lineStyle:{
                        color:'#00FF00'
                    }
                }
            },
            data: data1
        }]
    };
    myChart.setOption(option);
}

/**
 * 身心监测按钮组
 */
function select_monitor(obj) {
    $(obj).attr("isn","1");
    $(obj).siblings().attr("isn","-1");

    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
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

//查看全部按钮
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

//视频进度
function showrecord(times,usertype,obj) {
    $("#recorddetail td").removeClass("highlight_right");
    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
    if (isNotEmpty(times)&&times!=-1&&isNotEmpty(usertype)){
        var locationtime=times;
        locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
        changeProgrss(parseFloat(locationtime));

        var recorddetailtrlen= $("#recorddetail tr").length;
        $("#recorddetail tr").each(function (i,e) {
            var t1=$(this).attr("times");
            if (t1==times) {
                $("td",this).addClass("highlight_right");
                var top=$(this).position().top;
                $("#recorddetail_scrollhtml").animate({scrollTop:top},500);
            }
            /***-------------------手写笔录时间戳顺序正确的话可以使用-------------------***/
            /*
             var start=t1;
             var end=0;
             if (i>=recorddetailtrlen-1) {
                 end= start;//下一个区间
             }else {
                 end= $("#recorddetail tr:eq("+(i+1)+")").attr("times");//下一个区间
             }
            if (times>=start&&times<end) {
                 console.log(times+"start__"+start+"__end__"+end)
                 $("td",this).addClass("highlight_right");
                 var top=$("td",this).position().top;
                 $("#recorddetail_scrollhtml").animate({scrollTop:top-100},500);
             }*/
            /***-------------------手写笔录时间戳顺序正确的话可以使用-------------------***/
        });
      $("#recordreals div").each(function (i,e) {
            var t2=$(this).attr("times");
                if (t2==times) {
                    $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                    var top=$(this).position().top;
                    $("#recordreals_scrollhtml").animate({scrollTop:top},500);
                }
        });
    }
}


$(function () {
    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    newDate.setDate(newDate.getDate());
   var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()];
    setinterval1= setInterval( function() {
        var seconds = new Date().getSeconds();
        var sec=( seconds < 10 ? "0" : "" ) + seconds;
        var minutes = new Date().getMinutes();
        var min=( minutes < 10 ? "0" : "" ) + minutes;
        var hours = new Date().getHours();
        var hour=( hours < 10 ? "0" : "" ) + hours;

        if (isNotEmpty(select_monitorall_iframe_body)) {
            select_monitorall_iframe_body.find("#dqtime").html(date+ hour + "：" + min + "：" + sec);
        }

    },1000);



        //视频实时进度
        SewisePlayer.onPlayTime(function(time, id){
            //定位_____________________start_______________________
             $("#recordreals span").css("color","#fff").removeClass("highlight");

            var locationtime=time;
            locationtime=locationtime*1000<0?0:locationtime*1000; //秒转时间戳


              /*  //左侧
                var recordrealsdivlen=$("#recordreals div").length;//识别长度
                $("#recordreals div").each(function (i,e) {
                    var t=$(this).attr("times");
                    var start=t;
                    var end=0;
                    if (i>=recordrealsdivlen-1) {
                        end= t;//下一个区间
                    }else {
                        end= $("#recordreals div:eq("+(i+1)+")").attr("times");//下一个区间
                    }
                    if (locationtime>=parseFloat(start)&&(parseFloat(start)==parseFloat(end)||locationtime<=parseFloat(end))) {
                        console.log(locationtime+"__start__"+start+"__end__"+end)
                        $("#recordreals span").css("color","#fff").removeClass("highlight_left");
                        $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                        var top=$(this).position().top;
                        $("#recordreals_scrollhtml").animate({scrollTop:top},500);
                    }
                });*/


                //中间
                /*var arrph=[];
                if (isNotEmpty(phdatabackList)){
                    locationtime=time; //时间戳转秒
                    for (var i = 0; i < phdatabackList.length; i++) {
                        var phdataback = phdatabackList[i];
                        var num=phdataback.num;
                        var startph=num;
                        var endph=0;
                        if (i>= phdatabackList.length-1) {
                            endph= num;//下一个区间
                        }else {
                            endph=phdatabackList[i+1].num;
                        }
                        if (locationtime>=startph&&locationtime<=endph) {
                            var start_i=i<0?0:i;
                            var end_i=(i+50)>=phdatabackList.length?phdatabackList.length:(i+50);
                            arrph= phdatabackList.slice(start_i,end_i);
                        }
                    }
                    phdata(arrph);
                }*/
            //定位_____________________end_______________________
        });



});
function phdata(datad) {
        if (isNotEmpty(datad)){

            //将本次时间戳位置放中间--start--
            var dqdata=datad[0];
            var dqnum=0;
            var dqobj=null;
            if(isNotEmpty(dqdata)){
                dqnum=dqdata.num;
                var dqphBataBackJson=dqdata.phBataBackJson;
                dqobj=eval("(" + dqphBataBackJson + ")");
            }
            //将本次时间戳位置放中间--end--



            var status=0;//状态

            var hr=0;//心率
            var br=0;//呼吸次数
            var relax=0;//轻松值
            var stress=0;//紧张值
            var bp=0;//血压变化
            var spo2=0;//血氧
            var hrv=0;//心率变异

            var hr_snr=0;
            var fps=0;
            var stress_snr=0;
            //数据收集
            var date_hr2 = [];
            var data_hr2 = [];

            var date_br2 = [];
            var data_br2 = [];

            var date_relax2 = [];
            var data_relax2 = [];

            var date_stress2 = [];
            var data_stress2 = [];

            var date_bp2 = [];
            var data_bp2 = [];

            var date_spo22 = [];
            var data_spo22 = [];

            var date_hrv2 = [];
            var data_hrv2= [];

            for (var i = 0; i < 50; i++) {
                var num=0;//底部时间戳
                var data = datad[i];
                if(isNotEmpty(data)){
                    num=data.num;
                    var phBataBackJson=data.phBataBackJson;
                    var obj=eval("(" + phBataBackJson + ")");
                    if (isNotEmpty(obj)) {
                         hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                         br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                         relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                         stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                         bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                         spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                         hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);
                    }
                }

                date_hr2.push(num);
                data_hr2.push(hr);

                date_br2.push(num);
                data_br2.push(br);

                date_relax2.push(num);
                data_relax2.push(relax);

                date_stress2.push(num);
                data_stress2.push(stress);

                date_bp2.push(num);
                data_bp2.push(bp);

                date_spo22.push(num);
                data_spo22.push(spo2);

                date_hrv2.push(num);
                data_hrv2.push(hrv);
            }

            //开始赋值
            date_hr=date_hr2;
            data_hr=data_hr2;
            date_br=date_br2;
            data_br=data_br2;
            date_relax=date_relax2;
            data_relax=data_relax2;
            date_stress=date_stress2;
            data_stress=data_stress2;
            date_bp=date_bp2;
            data_bp=data_bp2;
            date_spo2=date_spo22;
            data_spo2=data_spo22;
            date_hrv=date_hrv2;
            data_hrv=data_hrv2;

            $("#monitor_btn span").each(function (e) {
                var type=$(this).attr("type");
                var name=$(this).text();
                var isn=$(this).attr("isn");
                if (isn==1){
                    if (type=="hr") {
                        date1=date_hr;
                        data1=data_hr;
                    }else if (type=="hrv") {
                        date1=date_hrv;
                        data1=data_hrv;
                    }else if (type=="br") {
                        date1=date_br;
                        data1=data_br;
                    }else if (type=="relax") {
                        date1=date_relax;
                        data1=data_relax;
                    }else if (type=="stress") {
                        date1=date_stress;
                        data1=data_stress;
                    }else if (type=="bp") {
                        date1=date_bp;
                        data1=data_bp;
                    }else if (type=="spo2") {
                        date1=date_spo2;
                        data1=data_spo2;
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
            if (null!=select_monitorall_iframe){
                select_monitorall_iframe.myMonitorall.setOption({
                    xAxis: {
                        data: date_hr
                    },
                    series: [{
                        data: data_hr
                    }]
                });
                select_monitorall_iframe.myMonitorall2.setOption({
                    xAxis: {
                        data: date_hrv
                    },
                    series: [{
                        data: data_hrv
                    }]
                });
                select_monitorall_iframe.myMonitorall3.setOption({
                    xAxis: {
                        data: date_br
                    },
                    series: [{
                        data: data_br
                    }]
                });
                select_monitorall_iframe.myMonitorall4.setOption({
                    xAxis: {
                        data: date_relax
                    },
                    series: [{
                        data: data_relax
                    }]
                });
                select_monitorall_iframe.myMonitorall5.setOption({
                    xAxis: {
                        data: date_stress
                    },
                    series: [{
                        data: data_stress
                    }]
                });
                select_monitorall_iframe.myMonitorall6.setOption({
                    xAxis: {
                        data: date_bp
                    },
                    series: [{
                        data: data_bp
                    }]
                });
                select_monitorall_iframe.myMonitorall7.setOption({
                    xAxis: {
                        data: date_spo2
                    },
                    series: [{
                        data: data_spo2
                    }]
                });
            }


            if (isNotEmpty(dqobj)){
                status=dqobj.status;

                relax=dqobj.relax.toFixed(0);
                stress=dqobj.stress.toFixed(0);
                bp=dqobj.bp.toFixed(0);
                spo2=dqobj.spo2.toFixed(0);
                hr=dqobj.hr.toFixed(0);
                hrv=dqobj.hrv.toFixed(0);
                br=dqobj.br.toFixed(0);

                hr_snr=dqobj.hr_snr.toFixed(1);
                fps=dqobj.fps.toFixed(1);
                stress_snr=dqobj.stress_snr.toFixed(1);
            }
            //开始填数据
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
        }
}
