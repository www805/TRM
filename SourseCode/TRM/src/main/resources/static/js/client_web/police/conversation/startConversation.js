/*
   开启谈话
 */

//
/*
function getUserByCard(){

    var cardnum =  $("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    var cardtypesssid=$("#cards option:selected").val();


    dquserssid=null;//当前用户的ssid
    dqcasessid=null;//当前案件ssid
    cases=null;
    var form=layui.form;
    $("#casename_ssid").html("");
    /!* $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#asknum')").val("");not('#occurrencetime'):not('#starttime'):not('#endtime'):*!/
    $("#casename,#recordname").val("");
    $("#asknum").val(0);
    $("#casename_ssid").html("");

    /!* init_form();//初始化表单*!/
    if (!isNotEmpty(cardnum)){
        return;
    }
    var bool=checkout_cardnum(cardnum,cardtypetext);
    if (!bool){
        return;
    }


    var url=getActionURL(getactionid_manage().addCaseToUser_getUserByCard);

    var data={
        token:INIT_CLIENTKEY,
        param:{
          /!*  cardtypesssid:cardtypesssid,*!///不填写默认使用身份证ssid
            cardnum:cardnum
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserByCard);
}
function callbackgetUserByCard(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var userinfo=data.userinfo;
            if (isNotEmpty(userinfo)){
                dquserssid=userinfo.ssid;

                /!*人员信息*!/
                $("#username").val(userinfo.username);
                $("#beforename").val(userinfo.beforename);
                $("#nickname").val(userinfo.nickname);
                $("#both").val(userinfo.both);
                $("#professional").val(userinfo.professional);
                $("#phone").val(userinfo.phone);
                $("#domicile").val(userinfo.domicile);
                $("#residence").val(userinfo.residence);
                $("#workunits").val(userinfo.workunits);
                $("#age").val(userinfo.age);

                $("#sex").val(userinfo.sex);
                $("#national").val(userinfo.nationalssid);
                $("#nationality").val(userinfo.nationalityssid);
                $("#educationlevel").val(userinfo.educationlevel);
                $("#politicsstatus").val(userinfo.politicsstatus);
            }

            if (isNotEmpty(dquserssid)){
                getCaseById();
            }

            var cardnum =  $("#cardnum").val();
            var cardtypetext=$("#cards option:selected").text();
            var bool=checkout_cardnum(cardnum,cardtypetext);
            if (!bool){
                return;
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
    layui.use('form', function(){
        var $ = layui.$;
        var form = layui.form;
        form.render();
    });
}

function getUserinfoList() {
    var url=getActionURL(getactionid_manage().startConversation_getUserinfoList);
    if (isNotEmpty(url)){
        var cardnum=$("#cardnum").val();
        var data={
            token:INIT_CLIENTKEY,
            param:{
                /!*  cardtypesssid:cardtypesssid,*!/ //不填写默认使用身份证ssid
                cardnum:cardnum
            }
        };
        ajaxSubmitByJson(url,data,callbackgetUserinfoList);
    }
}
function callbackgetUserinfoList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;

            $("#cardnum_ssid").html("");
            if (isNotEmpty(data)){
                var userinfos=data.userinfos;
                if (isNotEmpty(userinfos)){
                    for (var i = 0; i < userinfos.length; i++) {
                        var userinfo = userinfos[i];
                        $("#cardnum_ssid").append("<dd lay-value='"+userinfo.cardnum+"' onmousedown='select_cardnum(this);'>"+userinfo.cardnum+"</dd>");
                    }
                }else{
                    $("#cardnum_ssid").append('<p class="layui-select-none">无匹配项</p>');
                }
                $("#cardnum_ssid").css("display","block");
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
    layui.use('form', function(){
        var form =  layui.form;

        form.render();
    });
}







function setcases(cases){
    $("#casename_ssid").html("");
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c= cases[i];
            if (c.casebool!=2){
                $("#casename_ssid").append("<dd lay-value='"+c.ssid+"' onmousedown='select_case(this);'>"+c.casename+"</dd>");
            }
        }

        if (isNotEmpty(dqcasessid)){
            $("#casename").val(dqcasessid);
            $("#cause").val("");
            $("#casenum").val("");
            $("#caseway").val("");
            $("#asknum").val("0");
            $("#recordname").val("");
            if (isNotEmpty(cases)){
                for (var i = 0; i < cases.length; i++) {
                    var c = cases[i];
                    if (dqcasessid==c.ssid){
                        var casename=$("#casename").find("option:selected").text();
                        var recordtypename=$("td[recordtypebool='true']",parent.document).text();
                        var username=$("#username").val();
                        var recordname=""+username+"《"+casename+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(c.asknum)+1)+"版";

                        $("#cause").val(c.cause);
                        $("#casenum").val(c.casenum);
                        if (isNotEmpty(c.starttime)){
                            $("#starttime").val(c.starttime);
                        }
                        if (isNotEmpty(c.endtime)){
                            $("#endtime").val(c.endtime);
                        }
                        if (isNotEmpty(c.occurrencetime)){
                            $("#occurrencetime").val(c.occurrencetime);
                        }
                        $("#caseway").val(c.caseway);
                        $("#asknum").val(c.asknum);
                        $("#recordname").val(recordname);
                    }
                }
            }
        }
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}
function select_cardnum(obj) {
    $("#cardnum_ssid").css("display","none");
    var cardnum=$(obj).attr("lay-value");
    $("#cardnum").val(cardnum);
    $(obj).focus();
    getUserByCard();
    $("#cardnum_ssid").html("");
}
function select_cardnumblur() {
    $("#cardnum_ssid").css("display","none");
    getUserByCard();
}
function getCaseList() {
    $("#casename_ssid").html("");
    var caselike=[];
    var casename = $("#casename").val();
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c = cases[i];
            if (c.casename.indexOf(casename) >= 0) {
                caselike.push(c);
            }
        }
        if (isNotEmpty(caselike)){
            for (var j = 0; j < caselike.length; j++) {
                var cl=caselike[j];
                if (cl.casebool!=2){
                    $("#casename_ssid").append("<dd lay-value='"+cl.ssid+"' onmousedown='select_case(this);'>"+cl.casename+"</dd>");
                }
            }
        } else{
            $("#casename_ssid").append('<p class="layui-select-none">无匹配项</p>');
        }
        $("#casename_ssid").css("display","block");
    }

}
function select_case(obj) {
    $("#casename_ssid").css("display","none");
    var casename=$(obj).attr("lay-value");
    dqcasessid=casename;
    $("#casename").val($(obj).text());
    $(obj).focus();
    if (isNotEmpty(dqcasessid)){
        $("#cause").val("");
        $("#casenum").val("");
        $("#caseway").val("");
        $("#asknum").val("0");
        $("#recordname").val("");

        if (isNotEmpty(cases)){
            for (var i = 0; i < cases.length; i++) {
                var c = cases[i];
                if (dqcasessid==c.ssid){
                    var username=$("#username").val();
                    var casename=$("#casename").val();
                    var recordtypename=$("td[recordtypebool='true']",parent.document).text();
                    var recordname=""+username+"《"+casename+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(c.asknum)+1)+"版";

                    $("#cause").val(c.cause);
                    $("#casenum").val(c.casenum);
                    $("#caseway").val(c.caseway);
                    $("#asknum").val(c.asknum);
                    $("#recordname").val(recordname);

                    if (isNotEmpty(c.starttime)){
                        $("#starttime").val(c.starttime);
                    }
                    if (isNotEmpty(c.endtime)){
                        $("#endtime").val(c.endtime);
                    }
                    if (isNotEmpty(c.occurrencetime)){
                        $("#occurrencetime").val(c.occurrencetime);
                    }
                }
            }
        }
    }
    $("#casename_ssid").html("");
}
function select_caseblur() {
    $("#casename_ssid").css("display","none");
    var casename=$("#casename").val();
    var recordtypename=$("td[recordtypebool='true']",parent.document).text();
    var username=$("#username").val();
    //需要验证案件ssid
    dqcasessid=null;
    $("#cause").val("");
    $("#casenum").val("");
    $("#caseway").val("");
    $("#asknum").val("0");
    $("#recordname").val("");
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c = cases[i];
            if (c.casename.trim()==casename.trim()) {
                dqcasessid=c.ssid;
                var recordname=""+username+"《"+casename+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(c.asknum)+1)+"版";

                $("#cause").val(c.cause);
                $("#casenum").val(c.casenum);
                $("#caseway").val(c.caseway);
                $("#asknum").val(c.asknum);
                $("#recordname").val(recordname);

                if (isNotEmpty(c.starttime)){
                    $("#starttime").val(c.starttime);
                }
                if (isNotEmpty(c.endtime)){
                    $("#endtime").val(c.endtime);
                }
                if (isNotEmpty(c.occurrencetime)){
                    $("#occurrencetime").val(c.occurrencetime);
                }
                break;
            }
        }
    }
    if (isNotEmpty(casename)&&!isNotEmpty(dqcasessid)){
        var asknum=$("#asknum").val();
        var recordname=""+username+"《"+casename+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"版";
        $("#recordname").val(recordname);
        layui.use(['form','laydate'], function(){
            var form=layui.form;
            var laydate=layui.laydate;
            $("#casenum,#cause,#caseway").val("");
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
}*/
