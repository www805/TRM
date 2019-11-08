/**
 * 案件统计
 */


//yearstype 1 今年 2去年
function getCaseStatistics(yearstype) {
    myChart.showLoading();
    myChart2.showLoading();
    var url=getActionURL(getactionid_manage().caseStatistics_getCaseStatistics);

    var data={
        token:INIT_CLIENTKEY,
        param:{
            yearstype:yearstype
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCaseStatistics);
}

function callbackgetCaseStatistics(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            $("#record_num").text(data.record_num==null?0:data.record_num);
            $("#case_num").text(data.case_num==null?0:data.case_num);
            $("#case_startnum").text(data.case_startnum==null?0:data.case_startnum);
            $("#case_endnum").text(data.case_endnum==null?0:data.case_endnum);
            $("#record_finishnum").text(data.record_finishnum==null?0:data.record_finishnum);
            $("#record_unfinishnum").text(data.record_unfinishnum==null?0:data.record_unfinishnum);
            $("#record_waitnum").text(data.record_waitnum==null?0:data.record_waitnum);

            var clientname=data.clientname==null?"":data.clientname;
            myChart.hideLoading();
            myChart2.hideLoading();
            myChart.setOption({
                title: {
                    text: data.dq_y+'年'+clientname+'案件审讯统计',
                    subtext: '数据来源'+clientname,
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
                        {value:data.record_unfinishnum_y, name:'进行中审讯'},
                        {value:data.record_finishnum_y, name:'已完成审讯'},
                        {value:data.case_endnum_y, name:'未提讯案件'},
                        {value:data.case_startnum_y, name:'已提讯案件'}]
                },{
            name: '数据来源',
                    data: [ {value:data.record_num_y, name:'审讯', selected:true},
                        {value:data.case_num_y, name:'案件'}]
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
            text: 'xxxx年案件审讯统计',
            subtext: '',
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
            data:['未开始审讯','进行中审讯','已完成审讯','未提讯案件','已提讯案件']
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
               /* label: {
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
                            return Math.round(v.percent)+'%' + '' + v.name;
                        },
                        textStyle : {
                            fontSize : 15
                        }
                    }
                },
                data:[]
            }
        ]
    };
    myChart2.setOption(option2);
});