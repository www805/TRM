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


var option = {
    title: {
        text: '紧张值',
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
        data: date1,//默认数据
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
        name: '紧张值',
        type: 'line',
        showSymbol: false,
        hoverAnimation: false,
        data: data1,//默认数据
        markLine: {//警戒线标识
            silent: true,
            lineStyle: {
                normal: {
                    color: '#00CD68'                   // 这儿设置安全基线颜色
                }
            },
        }
    }]
};

//查看全部==
var select_monitorall_iframe=null;
var select_monitorall_iframe_body=null;
var select_monitorall_indedx=null;
function select_monitorall(obj) {
    if (isNotEmpty(togetPolygraphurl)){
        if (select_monitorall_indedx==null){
            select_monitorall_indedx= layer.open({
                type: 2
                , skin: 'layui-layer-lan' //样式类名
                ,title: "身心检测"
                ,area: ['40%','100%']
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
        }else {
            layer.close(select_monitorall_indedx);
            select_monitorall_indedx=null;
        }
    }
}


//初始化:width宽度
function main1(width) {
    if (!isNotEmpty(width)){ width="100%";}
    $("#main1").css( 'width',width);
    $(window).resize(function() {
        myChart.resize();
    });
    myChart = echarts.init(document.getElementById('main1'),'dark');
    myChart.setOption(option);
}

//身心监测按钮组
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

//排行榜开始
var RANKING_INDEX=null;
function select_monitorallranking(obj) {
    var RANKING_HTML='<div class="layui-row layui-form " > \
        <div class="layui-col-md12 layui-form-item" style="margin-top: 10px" >\
        <div  id="rank_btn" style="display:inline">\
        <span class="layui-badge layui-btn" isn="1"  type="stress" >紧张值</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="hr" >心率</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1"  type="spo2" >血氧</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="hrv" >心率变异</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1"  type="bp">血压变化</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="br">呼吸次数</span>\
        </div>\
        </div>\
        <div class="layui-col-md12 layui-form-item"  >\
        <table class="layui-table" lay-skin="nob">\
         <tr >\
            <th colspan="3" style="text-align: left" ><span style="color: red" id="rank_title">紧张值</span>异常排行榜(TOP20)\
            <span class="layui-table-sort layui-inline"><i class="layui-edge layui-table-sort-asc" title="升序" onclick="set_phranking(null,1)"></i><i class="layui-edge layui-table-sort-desc" title="降序"  onclick="set_phranking(null,2)"></i></span></th>\
        </tr>\
        <tbody id="ranking_html">\
        </tbody>\
        </table>\
        </div>\
      </div>';
    if (RANKING_INDEX==null){
        RANKING_INDEX= layer.open({
            title: '身心检测异常排行榜',
            type: 1,
            content: RANKING_HTML,
            offset:'r', area:["20%", "100%"],
            anim:2,
            shade: 0,
            closeBtn:0,
            btn: [],
            resize:false,
            move: false,
            success: function(layero, index){
                set_phranking("stress",1);
                $("#rank_btn span").click(function () {
                    $("#rank_btn span").attr("isn","-1").addClass("layui-bg-gray");
                    $(this).attr("isn","1").removeClass("layui-bg-gray");
                    $("#rank_title").text($(this).text());
                    var type= $(this).attr("type");
                    set_phranking(type,1)
                });
            }
        });
    } else {
        layer.close(RANKING_INDEX);
        RANKING_INDEX=null;
    }
}

function sortphranking_desc(a, b) {
    return b.data - a.data;
}
function sortphranking_asc(a, b) {
    return a.data - b.data;
}
//type 身心检测参数问题  sorttype排序类型默认1
function set_phranking(type,sorttype) {
    if (isNotEmpty(sorttype)){
        if (!isNotEmpty(type)){
            type= $("#rank_btn span[isn=1]").attr("type");
        }
        if (isNotEmpty(type)){
            //筛选数据
            var newphdata=[];
            if (isNotEmpty(phdatabackList)){
                for (let i = 0; i < phdatabackList.length; i++) {
                    const ph = phdatabackList[i];
                    var num=ph.num;
                    var date=ph.phdate;
                    var dqphBataBackJson=ph.phBataBackJson;
                    dqphBataBackJson=eval("(" + dqphBataBackJson + ")");
                    var newarr={};
                    if (dqphBataBackJson.hr_snr>=0.1&&dqphBataBackJson[type]!=null){
                        newarr["num"]=num;
                        newarr["date"]=date;
                        if (type=="stress") {
                            if (!(dqphBataBackJson[type]>=0&&dqphBataBackJson[type]<=30)){
                                newarr["data"]=dqphBataBackJson[type];

                            }
                        }
                        if (type=="hr") {
                            if (!(dqphBataBackJson[type]>=60&&dqphBataBackJson[type]<=100)){
                                newarr["data"]=dqphBataBackJson[type];
                            }
                        }
                        if (type=="spo2") {
                            if (!(dqphBataBackJson[type]>=94)){
                                newarr["data"]=dqphBataBackJson[type];
                            }
                        }
                        if (type=="hrv") {
                            if (!(dqphBataBackJson[type]>=-10&&dqphBataBackJson[type]<=10)){
                                newarr["data"]=dqphBataBackJson[type];
                            }
                        }
                        if (type=="bp") {
                            if (!(dqphBataBackJson[type]>=-10&&dqphBataBackJson[type]<=10)){
                                newarr["data"]=dqphBataBackJson[type];
                            }
                        }
                        if (type=="br") {
                            if (!(dqphBataBackJson[type]>=12&&dqphBataBackJson[type]<=20)){
                                newarr["data"]=dqphBataBackJson[type];
                            }
                        }
                        if (newarr["data"]!=null){
                            newphdata.push(newarr);
                        }
                        //放松值不计算：没有范围
                    }
                }
            }
            if (sorttype==1){
                newphdata.sort(sortphranking_desc);
            } else {
                newphdata.sort(sortphranking_asc);
            }

            $("#ranking_html").empty();
            var morenHTML='<tr style="border-bottom: 1px solid #ccc">\
                        <td colspan="2" style="text-align: center">暂无数据</td>\
                        </tr>';
            var HTML="";
            if (isNotEmpty(newphdata)){
                newphdata=newphdata.slice(0,20);
                for (let i = 0; i <newphdata.length; i++) {
                    var data=newphdata[i].data;
                    var num=newphdata[i].num;
                    var date=newphdata[i].date;
                    var rankstyle="background-color: #8EB9F5 !important";
                    var rankicon="";
                    if (i<3) {
                        if (i==0){
                            rankstyle='background-color: red !important';
                        } else if (i==1){
                            rankstyle='background-color: #FF8547 !important';
                        } else if (i==2){
                            rankstyle='background-color: #FFAC38 !important';
                        }
                        rankicon='<i class="layui-icon layui-icon-fire" style="color: red;"></i>';
                    }
                    HTML+='<tr style="border-bottom: 1px solid #ccc" num="'+num+'">\
                        <td style="text-align: left"><span class="layui-badge" style="'+rankstyle+'">'+(i+1)+'</span>&nbsp;'+data+' '+rankicon+'</td>\
                        <td  style="text-align: right"><i class="layui-icon layui-icon-log"> '+date+'</i></td>\
                        </tr>';
                }
            }
            $("#ranking_html").html(HTML==""?morenHTML:HTML);

            //开始定位视频和语音识别：提前5-8秒
            $("#ranking_html tr").dblclick(function () {
                var num=$(this).attr("num");//秒数
                if (null!=num){
                    //定位视频和asr
                    phSubtracSeconds=parseInt(phSubtracSeconds);
                    var  newnum=num-phSubtracSeconds==null?0:(num-phSubtracSeconds<0?0:num-phSubtracSeconds);
                    showrecord(parseFloat(newnum*1000),parseFloat(num*1000));
                    //身心检测显示
                    var type= $("#rank_btn span[isn=1]").attr("type");
                    var $tb=$('#monitor_btn span[type='+type+']');
                    $("#monitor_btn span").attr("isn","-1").addClass("layui-bg-gray");
                    $($tb).attr("isn","1").removeClass("layui-bg-gray");
                    var name=$($tb).text();
                    myChart.setOption({
                        title: {
                            text: name,
                        },
                        series: [{
                            name:name,
                        }]
                    });
                }
            });
        }
    }
}
//排行榜结束





//回填当前情绪报告obj当前数据  bool是否需要add
function dqphdata(obj,bool) {
    if (isNotEmpty(obj)) {
        var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
        var br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
        var  status=obj.status;
        var relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
        var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
        var bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
        var spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
        var hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);
        var hr_snr=obj.hr_snr.toFixed(1)==null?0:obj.hr_snr.toFixed(1);
        var fps=obj.fps.toFixed(1)==null?0:obj.fps.toFixed(1);
        var stress_snr=obj.stress_snr.toFixed(1)==null?0:obj.stress_snr.toFixed(1);
        var emotion=obj.emotion==null?6:obj.emotion;//为空默认表情：平静

        if (bool){
            addData_hr(true,hr);
            addData_hrv(true,hrv);
            addData_br(true,br);
            addData_relax(true,relax);
            addData_stress(true,stress);
            addData_bp(true,bp);
            addData_spo2(true,spo2);
        }

        //开始填数据
        var stress_text="未知";
        if (stress>=0&&stress<=30){
            stress_text="<span style='color: #00CD68'>正常</span>";
        }else if (stress>30&&stress<=50){
            stress_text="<span style='color: #e4e920'>轻度紧张</span>";
        }else if (stress>50&&stress<=70){
            stress_text="<span style='color: #ff840f'>中度紧张</span>";
        }else if (stress>70&&stress<=100){
            stress_text="<span style='color: #e90717'>高度紧张</span>";
        }

        $("#xthtml #xt1").html(' '+stress_text+'   ');
        $("#xthtml #xt2").html(' '+relax+'  ');
        $("#xthtml #xt3").html(' '+stress+'  ');
        $("#xthtml #xt4").html(' '+bp+'  ');
        $("#xthtml #xt5").html(' '+spo2+'  ');
        $("#xthtml #xt6").html(' '+hr+'  ');
        $("#xthtml #xt7").html(' '+hrv+'  ');
        $("#xthtml #xt8").html(' '+br+'  ');

        var snrtext="fps："+fps+"&nbsp;hr_snr："+hr_snr+"&nbsp;stress_snr："+stress_snr+"";
        $("#snrtext").html(snrtext);
        $("#snrtext3").html(snrtext);

        //表情
        var moodsrc="/uimaker/images/emojis/6.png";
        var moodtitle="平静";
        if(emotion!=null){
            moodsrc="/uimaker/images/emojis/"+emotion+".png";
            if (emotion==0){moodtitle="生气";}
            else  if(emotion==1){moodtitle="厌恶";}
            else  if(emotion==2){moodtitle="恐惧";}
            else  if(emotion==3){moodtitle="高兴";}
            else  if(emotion==4){moodtitle="伤心";}
            else  if(emotion==5){moodtitle="惊讶";}
            else  if(emotion==6){moodtitle="平静";}
        }

        //提示标题
        var tsmsg="身心监测加载中...";
        var tsmsg_state=-1;
        var tscss={"color":" #31708f","background-color": "#d9edf7"," border-color": "#bce8f1"};
        if (isNotEmpty(hr_snr)&&hr_snr>=0.1){
            tsmsg_state=1;
            tsmsg="身心准确监测中";
            tscss={"color": "#3c763d","background-color":"#dff0d8","border-color":"#d6e9c6"};
        }else{
            tsmsg="监测准确度较低，请调整光线，减少身体晃动";
            tscss={"color": "#a94442","background-color":"#f2dede","border-color":"#ebccd1"};
        }
        $("#showmsg,#open_showmsg").css(tscss);
        $("#showmsg strong,#open_showmsg strong").text(tsmsg);


        //颜色闪闪
        var monitoralltext="状态： "+stress_text+"\
                                                                <span  id=\"monitorall_hr\">心率： "+hr+"</span>\
                                                                <span  id=\"monitorall_hrv\">心率变异： "+hrv+"</span>\
                                                               <span  id=\"monitorall_br\">呼吸次数： "+br+"</span>\
                                                                <span  id=\"monitorall_relax\">放松值： "+relax+"</span>\
                                                                <span  id=\"monitorall_stress\">紧张值： "+stress+"</span>\
                                                                <span  id=\"monitorall_bp\">血压变化： "+bp+"</span>\
                                                                <span  id=\"monitorall_spo2\">血氧： "+spo2+"</span>";
        $("#monitoralltext").html(monitoralltext);
        $("#monitorall_stressstate,#monitorall_hr,#monitorall_hrv,#monitorall_br,#monitorall_relax,#monitorall_stress,#monitorall_bp,#monitorall_spo2").removeClass("highlight_monitorall");
        $("#xthtml span").removeClass("highlight_monitorall");




        //图标规划参数
        var redcolor="#00CD68";
        var itemStyle_color="red";
        var itemStyle_color_hr=itemStyle_color;
        var itemStyle_color_hrv=itemStyle_color;
        var itemStyle_color_br=itemStyle_color;
        var itemStyle_color_relax=itemStyle_color;
        var itemStyle_color_stress=itemStyle_color;
        var itemStyle_color_bp=itemStyle_color;
        var itemStyle_color_spo2=itemStyle_color;


        var dqmarkLinedata=[];
        var dqmarkLinedata_hr=[{ yAxis: 60}, {yAxis: 100}];
        var dqmarkLinedata_hrv=[{yAxis: -10}, { yAxis: 10 }];
        var dqmarkLinedata_br=[{yAxis: 12}, { yAxis: 20 }];
        var dqmarkLinedata_relax=[];
        var dqmarkLinedata_stress=[{yAxis: 30}, { yAxis: 50 }, { yAxis: 70 }, { yAxis: 100 }];
        var dqmarkLinedata_bp=[{yAxis: -10}, { yAxis: 10 }];
        var dqmarkLinedata_spo2=[{yAxis: 94}];

        var dqpieces=[];
        var pieces_hr=[{ gt: -2,lte: -1,color: 'red'},{gt:60,lte: 100,color: '#00CD68'}];
        var pieces_hrv=[{ gt: -11,lte: 10,color: '#00CD68'}];
        var pieces_br=[{ gt: 11,lte: 20,color: '#00CD68'}];
        var pieces_relax=[{ gt: -2,lte: -1,color: '#00CD68'},{gt:-1,color: '#00CD68'}];
        var pieces_stress=[{ gt: -1,lte: 30,color: '#00CD68'},{ gt: 30,lte: 50,color: '#ffff33'},{ gt: 50,lte: 70,color: '#ff944d'},{ gt: 70,lte: 100,color: '#ff4c1e'}];
        var pieces_bp=[{ gt: -11,lte: 10,color: '#00CD68'}];
        var pieces_spo2=[{ gt: -1,lte: 94,color: 'red'},{ gt: 94,color: '#00CD68'}];



        if (isNotEmpty(myChart)){
            myChart.hideLoading();

            //select_monitorall_iframe_body
            if (isNotEmpty(select_monitorall_iframe_body)) {
                select_monitorall_iframe_body.find("#monitorall #xt1").html(''+stress_text+'   ');
                select_monitorall_iframe_body.find("#monitorall #xt2").html('放松值：'+relax+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt3").html('紧张值：'+stress+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt4").html('血压变化：'+bp+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt5").html('血氧：'+spo2+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt6").html('心率：'+hr+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt7").html('心率变异：'+hrv+'  ');
                select_monitorall_iframe_body.find("#monitorall #xt8").html('呼吸：'+br+'  ');

                select_monitorall_iframe_body.find("#snrtext2").html(snrtext);
                //表情
                select_monitorall_iframe_body.find("#mood").attr({"src":moodsrc},{"title":moodtitle});
                //提示标题
                select_monitorall_iframe_body.find("#open_showmsg").css(tscss);
                select_monitorall_iframe_body.find("#open_showmsg strong").text(tsmsg);



                //颜色闪闪
                select_monitorall_iframe_body.find("#xt1,#xt2,#xt3,#xt4,#xt5,#xt6,#xt7,#xt8").removeClass("highlight_monitorall");
                if (hr>=60&&hr<=100){
                    itemStyle_color_hr=redcolor;
                }else if (tsmsg_state==1) {
                    $("#monitorall_hr,#xthtml #xt6").addClass("highlight_monitorall");
                    select_monitorall_iframe_body.find("#xt6").addClass("highlight_monitorall");
                }
                if (hrv>=-10&&hrv<=10){
                    itemStyle_color_hrv=redcolor;
                }else  if (tsmsg_state==1) {
                    $("monitorall_hrv,#xthtml #xt7").addClass("highlight_monitorall");
                    select_monitorall_iframe_body.find("#xt7").addClass("highlight_monitorall");
                }
                if (br>=12&&br<=20){
                    itemStyle_color_br=redcolor;
                }else if (tsmsg_state==1)  {
                    $("monitorall_br,#xthtml #xt8").addClass("highlight_monitorall");
                    select_monitorall_iframe_body.find("#xt8").addClass("highlight_monitorall");
                }
                if (null!=relax){
                    itemStyle_color_relax=redcolor;
                }
                if (stress>=0&&stress<=30){
                    itemStyle_color_stress=redcolor;
                }else  if (tsmsg_state==1) {
                    $("monitorall_stress,#xthtml #xt3").addClass("highlight_monitorall");
                    select_monitorall_iframe_body.find("#xt3").addClass("highlight_monitorall");
                }
                if (bp>=-10&&bp<=10){
                    itemStyle_color_bp=redcolor;
                }else {
                    $("monitorall_bp,#xthtml #xt4").addClass("highlight_monitorall");
                   select_monitorall_iframe_body.find("#xt4").addClass("highlight_monitorall");
                }
                if (spo2>=94){
                    itemStyle_color_spo2=redcolor;
                }else  if (tsmsg_state==1) {
                    $("monitorall_spo2,#xthtml #xt5").addClass("highlight_monitorall");
                    select_monitorall_iframe_body.find("#xt5").addClass("highlight_monitorall");
                }
            }


            var dq_type=null;
            $("#monitor_btn span").each(function (e) {
                var type=$(this).attr("type");
                var name=$(this).text();
                var isn=$(this).attr("isn");
                if (isn==1){
                    dq_type=type;
                    if (type=="hr") {
                        date1=date_hr;
                        data1=data_hr;
                        dqy=hr;
                        dqmarkLinedata=dqmarkLinedata_hr;
                        dqpieces=pieces_hr;
                        if (hr>=60&&hr<=100){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="hrv") {
                        date1=date_hrv;
                        data1=data_hrv;
                        dqy=hrv;
                        dqmarkLinedata=dqmarkLinedata_hrv;
                        dqpieces=pieces_hrv;
                        if (hrv>=-10&&hrv<=10){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="br") {
                        date1=date_br;
                        data1=data_br;
                        dqy=br;
                        dqmarkLinedata=dqmarkLinedata_br;
                        dqpieces=pieces_br;
                        if (br>=12&&br<=20){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="relax") {
                        date1=date_relax;
                        data1=data_relax;
                        dqy=relax;
                        dqmarkLinedata=dqmarkLinedata_relax;
                        dqpieces=pieces_relax;
                        if (null!=relax){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="stress") {
                        date1=date_stress;
                        data1=data_stress;
                        dqy=stress;
                        dqmarkLinedata=dqmarkLinedata_stress;
                        dqpieces=pieces_stress;
                        if (stress>=0&&stress<=30){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="bp") {
                        date1=date_bp;
                        data1=data_bp;
                        dqy=bp;
                        dqmarkLinedata=dqmarkLinedata_bp;
                        dqpieces=pieces_bp;
                        if (bp>=-10&&bp<=10){
                            itemStyle_color="#00CD68";
                        }
                    }else if (type=="spo2") {
                        date1=date_spo2;
                        data1=data_spo2;
                        dqy=spo2;
                        dqmarkLinedata=dqmarkLinedata_spo2;
                        dqpieces=pieces_spo2;
                        if (spo2>=94){
                            itemStyle_color="#00CD68";
                        }
                    }
                }
            });


            var dqx=date1[date1.length-1]==null?0:date1[date1.length-1];
            dqx=dqx.toString();

            myChart.setOption({
                xAxis: {
                    data: date1
                },
                visualMap:{
                    show:false,
                    pieces:dqpieces,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data1,
                    markPoint: {
                        data: [
                            {name: '当前值', value:dqy, xAxis:dqx, yAxis: dqy}
                        ],
                        itemStyle:{
                            color:itemStyle_color,
                        }
                    },
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
                    visualMap: {
                        show:false,
                        pieces:pieces_hr,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_hr,
                        markPoint: {
                            data: [
                                {name: '当前值', value:hr, xAxis:date_hr[date_hr.length-1].toString(), yAxis: hr}
                            ],
                            itemStyle:{
                                color:itemStyle_color_hr,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_hr
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall2.setOption({
                    xAxis: {
                        data: date_hrv
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_hrv,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_hrv,
                        markPoint: {
                            data: [
                                {name: '当前值', value:hrv, xAxis:date_hrv[date_hrv.length-1].toString(), yAxis: hrv}
                            ],
                            itemStyle:{
                                color:itemStyle_color_hrv,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_hrv
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall3.setOption({
                    xAxis: {
                        data: date_br
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_br,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_br,
                        markPoint: {
                            data: [
                                {name: '当前值', value:br, xAxis:date_hrv[date_hrv.length-1].toString(), yAxis: br}
                            ],
                            itemStyle:{
                                color:itemStyle_color_br,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_br
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall4.setOption({
                    xAxis: {
                        data: date_relax
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_relax,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_relax,
                        markPoint: {
                            data: [
                                {name: '当前值', value:relax, xAxis:date_relax[date_relax.length-1].toString(), yAxis: relax}
                            ],
                            itemStyle:{
                                color:itemStyle_color_relax,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_relax
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall5.setOption({
                    xAxis: {
                        data: date_stress
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_stress,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_stress,
                        markPoint: {
                            data: [
                                {name: '当前值', value:stress, xAxis:date_stress[date_stress.length-1].toString(), yAxis: stress}
                            ],
                            itemStyle:{
                                color:itemStyle_color_stress,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_stress
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall6.setOption({
                    xAxis: {
                        data: date_bp
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_bp,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_bp,
                        markPoint: {
                            data: [
                                {name: '当前值', value:bp, xAxis:date_stress[date_stress.length-1].toString(), yAxis: bp}
                            ],
                            itemStyle:{
                                color:itemStyle_color_bp,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_bp
                        }
                    }]
                });
                select_monitorall_iframe.myMonitorall7.setOption({
                    xAxis: {
                        data: date_spo2
                    },
                    visualMap: {
                        show:false,
                        pieces:pieces_spo2,
                        outOfRange: {
                            color: 'red'
                        }
                    },
                    series: [{
                        data: data_spo2,
                        markPoint: {
                            data: [
                                {name: '当前值', value:spo2, xAxis:date_stress[date_stress.length-1].toString(), yAxis: spo2}
                            ],
                            itemStyle:{
                                color:itemStyle_color_spo2,
                            }
                        },
                        markLine: {
                            data: dqmarkLinedata_spo2
                        }
                    }]
                });
            }
        }
    }else {
        console.log("测谎仪data is null")
    }

}


//将数据传给统计图：datad全部数据50，dqdata当前数据
function phdata(datad,dqdata) {
    if (isNotEmpty(datad)){
        myChart.hideLoading();

        //将本次时间戳位置放中间--start--
        var dqnum=0;//当前序号
        var dqobj=null;//当前json值
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

        var emotion=-1;



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

                    emotion=obj.emotion.toFixed(0)==null?0:obj.emotion.toFixed(0);
                }
            }

            date_hr2.push(num);
            data_hr2.push(parseFloat(hr));

            date_br2.push(num);
            data_br2.push(parseFloat(br));

            date_relax2.push(num);
            data_relax2.push(parseFloat(relax));

            date_stress2.push(num);
            data_stress2.push(parseFloat(stress));

            date_bp2.push(num);
            data_bp2.push(parseFloat(bp));

            date_spo22.push(num);
            data_spo22.push(parseFloat(spo2));

            date_hrv2.push(num);
            data_hrv2.push(parseFloat(hrv));
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


        //当前数据
        if (isNotEmpty(dqobj)){
            //回填当前数据
            dqphdata(obj,false);//不需要add只需要渲染当前数据
        }
    }
}











