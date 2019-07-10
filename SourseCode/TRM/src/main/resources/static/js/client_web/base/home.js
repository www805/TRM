//yearstype 1 今年 2去年
function getHome(yearstype) {
    myChart.showLoading();
    myChart2.showLoading();
    var url=getActionURL(getactionid_manage().home_getHome);
    var loginaccount =$("#loginaccount").val();
    var password =$("#password").val();
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

            var sqEntityHTML = "<p>单位名称：" + sqEntity.clientName + "</p>";//单位名称
            sqEntityHTML += "<p>授权开始时间：" + sqEntity.startTime + "</p>";//授权开始时间

            if(sqEntity.foreverBool == true){
                sqEntityHTML += "<p>授权期限：永久</p>";//授权时间
            }else{
                sqEntityHTML += "<p>授权期限：临时</p>";//授权总天数
                sqEntityHTML += "<p>授权已用天数：" + data.workdays + "</p>";//授权已用天数
            }

            sqEntityHTML += "<p>单位代码：" + sqEntity.unitCode + "</p>";//单位机器码
            sqEntityHTML += "<p>客户端序号：" + sqEntity.sortNum + "</p>";//排序

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
                    text: data.dq_y+'年智能提讯系统案件笔录统计',
                },
                series: [{
                    name: '笔录数',
                    data: data.record_monthnum_y
                },{
                    name: '案件数',
                    data: data.case_monthnum_y
                }]
            });
            myChart2.setOption({
                series: [{
                    name: '详情来源',
                    data: [{value:data.record_finishnum_y, name:'已完成笔录'},
                        {value:data.record_unfinishnum_y, name:'进行中笔录'},
                        {value:data.case_endnum_y, name:'未提讯案件'},
                        {value:data.case_startnum_y, name:'已提讯案件'},
                        {value:data.template_num_y, name:'模板'},
                        {value:data.userinfo_num_y, name:'人员'}]
                },{
                    name: '数据来源',
                    data: [ {value:data.record_num_y, name:'笔录', selected:true},
                        {value:data.case_num_y, name:'案件'},
                        {value:data.template_num_y, name:'模板'},
                        {value:data.userinfo_num_y, name:'人员'}]
                }]
            });
        }
    }else{
        layer.msg(data.message);
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
            text: 'xxxx年智能提讯系统案件笔录统计',
            subtext: '数据来源深圳顺泰伟成科技有限公司'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['案件数','笔录数']
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
                name: '笔录数',
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
            data:['已完成笔录','进行中笔录','未提讯案件','已提讯案件','模板','人员']
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
                label: {
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
                },
                data:[]
            }
        ]
    };
    myChart2.setOption(option2);


});

