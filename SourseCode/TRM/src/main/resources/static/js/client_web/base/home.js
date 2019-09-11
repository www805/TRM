var clientName="加载中";//默认

//yearstype 1 今年 2去年
function getHome(yearstype) {
    myChart.showLoading();
    myChart2.showLoading();
    var url=getActionURL(getactionid_manage().home_getHome);

    var data={
        token:INIT_CLIENTKEY,
        param:{
            yearstype:yearstype
        }
    };
    ajaxSubmitByJson(url,data,callbackgetHome);
}

function callbackgetHome(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $("#record_num").text(0);
        $("#case_num").text(0);
        $("#template_num").text(0);
        $("#userinfo_num").text(0);
        if (isNotEmpty(data)){
            $("#record_num").text(data.record_num);
            $("#case_num").text(data.case_num);
            $("#template_num").text(data.template_num);
            $("#userinfo_num").text(data.userinfo_num);

            var sqEntity = data.sqEntity; //授权信息
            var sqgnList = data.sqgnList; //授权功能信息
            clientName=sqEntity.clientName;
            var sqEntityHTML = "<p>单位名称：" + sqEntity.clientName + "</p>";//单位名称
            if(sqEntity.foreverBool == true){
                sqEntityHTML += "<p>授权期限：永久</p>";//授权时间
                sqEntityHTML += "<p>单位代码：" + sqEntity.unitCode + "</p>";//单位机器码
                sqEntityHTML += "<p>客户端序号：" + sqEntity.sortNum + "</p>";//排序
            }else{
                sqEntityHTML += "<p>授权期限：临时</p>";//授权总天数
                sqEntityHTML += "<p>授权总天数：" + sqEntity.sqDay + "</p>";//授权已用天数
                var startTime = "";
                if(isNotEmpty(sqEntity.startTime)){
                    startTime = sqEntity.startTime.substring(0, sqEntity.startTime.indexOf(" "));
                }
                sqEntityHTML += "<p>授权开始时间：" + startTime + "</p>";//授权开始时间
            }

            var sqgnListHTML = "";
            for (var i = 0; i < sqgnList.length; i++) {
                sqgnListHTML += "<span class=\"layui-badge layui-bg-blue\" style='margin-right: 5px;'>" + sqgnList[i] + "</span>";//功能
            }

            $("#sqEntity").html(sqEntityHTML);
            $("#sqgnList").html(sqgnListHTML);

            myChart.hideLoading();
            myChart2.hideLoading();
            myChart.setOption({
                title: {
                    text: data.dq_y+'年'+clientName+'案件审讯统计',
                    subtext: '数据来源'+clientName,
                },
                series: [{
                    name: '审讯数',
                    data: data.record_monthnum_y
                },{
                    name: '案件数',
                    data: data.case_monthnum_y
                }]
            });
            myChart2.setOption({
                series: [{
                    name: '详情来源',
                    data: [{value:data.record_waitnum_y,name:'未开始审讯'},
                        {value:data.record_finishnum_y, name:'已完成审讯'},
                        {value:data.record_unfinishnum_y, name:'进行中审讯'},
                        {value:data.case_endnum_y, name:'未提讯案件'},
                        {value:data.case_startnum_y, name:'已提讯案件'},
                        {value:data.template_num_y, name:'模板'},
                        {value:data.userinfo_num_y, name:'人员'}]
                },{
                    name: '数据来源',
                    data: [ {value:data.record_num_y, name:'审讯', selected:true},
                        {value:data.case_num_y, name:'案件'},
                        {value:data.template_num_y, name:'模板'},
                        {value:data.userinfo_num_y, name:'人员'}]
                }]
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


var myChart;
var myChart2;
$(function () {
    $(window).resize(function() {
        myChart.resize();
        myChart2.resize();
    });

    myChart = echarts.init(document.getElementById('main'));
    var option = {
        title: {
            text: 'xxxx年'+clientName+'案件审讯统计',
            subtext: '数据来源'+clientName,
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['案件数','审讯数']
        },
        toolbox: {
            show : true,
            feature : {
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        xAxis: {
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        },
        yAxis: {
            type : 'value'
        },
        series: [
            {
                name: '案件数',
                type: 'bar',
                data: [],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            },
            {
                name: '审讯数',
                type: 'bar',
                data: [],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            }]
    };
    myChart.setOption(option);

    myChart2 = echarts.init(document.getElementById('main2'));
    var option2 = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            data:['未开始审讯','已完成审讯','进行中审讯','未提讯案件','已提讯案件','模板','人员']
        },
        toolbox: {
            show : true,
            feature : {
                saveAsImage : {show: true}
            }
        },
        series: [
            {
                name:'数据来源',
                type:'pie',
                selectedMode: 'single',
                radius: [0, '30%'],
                minAngle: 5,
                avoidLabelOverlap:true,
                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data:[]
            },
            {
                name:'详情来源',
                type:'pie',
                radius: ['40%', '55%'],
                /*label: {
                    normal: {
                        formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                        backgroundColor: '#eee',
                        borderColor: '#aaa',
                        borderWidth: 1,
                        borderRadius: 4,
                        rich: {
                            a: {
                                color: '#999',
                                lineHeight: 22,
                                align: 'center'
                            },
                            hr: {
                                borderColor: '#aaa',
                                width: '100%',
                                borderWidth: 0.5,
                                height: 0
                            },
                            b: {
                                fontSize: 16,
                                lineHeight: 33
                            },
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            }
                        }
                    }
                },*/
               label:{
                    align: 'left',
                    normal:{
                        formatter(v) {
                            let text = Math.round(v.percent)+'%' + '' + v.name
                            if(text.length <= 8)
                            {
                                return text;
                            }else if(text.length > 8 && text.length <= 16){
                                return text = `${text.slice(0,8)}\n${text.slice(8)}`
                            }else if(text.length > 16 && text.length <= 24){
                                return text = `${text.slice(0,8)}\n${text.slice(8,16)}\n${text.slice(16)}`
                            }else if(text.length > 24 && text.length <= 30){
                                return text = `${text.slice(0,8)}\n${text.slice(8,16)}\n${text.slice(16,24)}\n${text.slice(24)}`
                            }else if(text.length > 30){
                                return text = `${text.slice(0,8)}\n${text.slice(8,16)}\n${text.slice(16,24)}\n${text.slice(24,30)}\n${text.slice(30)}`
                            }
                        },
                        textStyle : {
                            fontSize : 8
                        }
                    }
                },
                data:[]
            }
        ]
    };
    myChart2.setOption(option2);
});

//打开开始审讯弹出框
function open_startConversation() {
    var HTML='';
    layer.open({
        id:"startconversation_id",
        type:2,
        title: '填写基础信息',
        shade: 0.3,
        resize:false,
        area: ['50%', '700px'],
        skin: 'startconversation_btn', //样式类名
        content: startConversationURL,
    });
}

//一键谈话添加基础数据跳转审讯页面
var skipCheckbool=-1;//是否跳过检测：默认-1
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表
function to_waitconversationURL() {

    var url=getActionURL(getactionid_manage().home_addCaseToArraignment);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            skipCheckbool:skipCheckbool,
            conversationbool:2,//一键谈话
        }
    };
    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}

function callbackaddCaseToArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;
            if (isNotEmpty(recordssid)&&toUrltype==1){
                var index = layer.msg('开始进行审讯', {icon: 6,shade:0.1,time:500
                },function () {
                    location.href=window.waitconversationURL+"?ssid="+recordssid
                });
            }else if(toUrltype==2){
                location.href =  window.conversationIndexURL;
            }
        }
    }else{
        var data2=data.data;
        if (isNotEmpty(data2)){
            var recordingbool=data2.recordingbool
            var recordssid=data2.recordssid;
            var checkStartRecordVO=data2.checkStartRecordVO;

            if (null!=recordingbool&&recordingbool==true){
                //存在笔录正在进行中，跳转笔录列表，给出提示：建议他先结束制作中的
                if (isNotEmpty(checkStartRecordVO)){
                    var msg=checkStartRecordVO.msg;
                    if (isNotEmpty(msg)){
                        layer.confirm("<span style='color:red'>"+msg+"</span>", {
                            btn: ['开始审讯',"查看审讯列表","取消"], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转审讯制作控制台");
                                //保存
                                skipCheckbool = 1;
                                to_waitconversationURL();
                                layer.close(index);
                            },
                            btn2: function(index) {
                                console.log("跳转审讯列表")
                                toUrltype=2;
                                skipCheckbool =1;
                                to_waitconversationURL();
                                layer.close(index);
                            },
                            btn3: function(index) {
                                layer.close(index);
                            }
                        });
                    }
                }
            }else {
                layer.msg(data.message,{icon: 5});
            }
        }else {
            layer.msg(data.message,{icon: 5});
        }
    }
}


