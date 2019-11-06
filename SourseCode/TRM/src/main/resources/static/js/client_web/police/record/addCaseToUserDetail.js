$(function () {
    $("#getCardreader_btn").click(function () {
        var cardtypetext=$("#cards option:selected").text();
        if ($.trim(cardtypetext)!="居民身份证"){
            layer.msg("请先选择身份证类型",{icon:5});
            return;
        }
        $.ajax({
            dataType: "JSONP",
            type: "get",
            url: "http://localhost:8989/api/ReadMsg",
            timeout: 3000,
            success: function (data) {
                if (isNotEmpty(data)){
                    reset();
                    var retmsg=data.retmsg==null?"未知错误":data.retmsg;

                    var CardType=data.CardType;//0身份证 1其他国家身份证 2港澳居住证
                    var username="";
                    if (CardType==0||CardType==2){
                        var bool=checkout_cardnum(data.cardno,"居民身份证");
                        if (!bool){
                            return;
                        }
                        username=data.name==null?"":data.name;

                        var nation=data.nation;
                        $("#national option").each(function () {
                            var txt=$(this).text();
                            var value=$(this).attr("value");
                            if (txt.indexOf(nation)>-1){
                                $("#national").val(value);
                                return;
                            }
                        })
                        var nationality_value=$("#nationality option[title='China']").attr("value");
                        $("#nationality").val(nationality_value);
                    } else if (CardType==1){
                        var nation=data.nation;
                        $("#nationality option").each(function () {
                            var txt=$(this).text();
                            var value=$(this).attr("value");
                            if (txt.indexOf(nation)>-1){
                                $("#nationality").val(value);
                                return;
                            }
                        })
                        username=data.EngName==null?"":data.EngName
                    }
                    $("#username").val(username);
                    $("#cardnum").val(data.cardno);
                    $("#domicile").val(data.address);
                    $("#sex").val(data.sex=="女"?2:(data.sex=="男"?1:-1));
                    layui.use('form', function(){
                        var form =  layui.form;
                        form.render();
                    });
                    layer.msg(retmsg,{icon:6,time:1000},function () {
                        getUserByCard();
                    });
                }
            },
            error: function (e) {
                layer.msg("请先确认身份证识别设备是否插上",{icon:5})
            }
        });
    });
})


function reset() {
    dquserssid=null;//当前用户的ssid
    dqcasessid=null;//当前案件ssid
    cases=null;
    dqotheruserinfossid=null;//当前询问人(新增询问人回显)
    dqotherworkssid=null;//当前询问人对应的工作单位
    layui.use(['form','laydate'], function(){
        var form=layui.form;
        var laydate=layui.laydate;
        $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#modelssid')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
        $("#asknum").val(0);
        $('select').not("#cards").prop('selectedIndex', 0);
        $("#casename_ssid").html("");

        laydate.render({
            elem: '#occurrencetime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });
        laydate.render({
            elem: '#starttime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });
        laydate.render({
            elem: '#endtime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });

        form.render('select');
    });
}