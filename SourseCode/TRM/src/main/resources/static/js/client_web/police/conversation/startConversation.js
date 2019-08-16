/*
   开启谈话
 */

var dquserssid;//当前用户ssid
var dqcasessid;//当前案件

var cases;//全部案件
var otheruserinfos;//工作人员

var skipCheckbool=-1;//是否跳过检测：默认-1
var asknum=0;//询问次数

var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表

//开始谈话，添加案件提讯数据
function addCaseToArraignment() {
    var  addUserInfo={};//新增人员的信息
    var  addPolice_case={};//新增案件的信息

    var url=getActionURL(getactionid_manage().startConversation_addCaseToArraignment);


    var cardnum=$("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    if (!isNotEmpty(cardnum)){
        parent.layer.msg("证件号码不能为空");
        return;
    }
    if ($.trim(cardtypetext)=="居民身份证"){
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(cardnum) === false) {
            parent.layer.msg("身份证输入不合法");
            return false;
        }
    }
    var  username=$("#username").val();
    if (!isNotEmpty(username)){
        parent.layer.msg("姓名不能为空");
        return;
    }

    var  casename=$("#casename").val();
    if (!isNotEmpty(casename)){
        parent.layer.msg("案件名称不能为空");
        return;
    }


    var recordadminssid=$("#recordadmin  option:selected").val();
    if (!isNotEmpty(recordadminssid)){
        parent.layer.msg("记录人不能为空");
        return;
    }


  /*  $("#startrecord_btn").attr("onclick","");*/

    //收集人员信息
    var  cardtypessid=$("#cards option:selected").val();
    var  age=$("#age").val();
    var  sex=$("#sex").val();
    var  both=$("#both").val();
    var  politicsstatus=$("#politicsstatus").val();
    var  domicile=$("#domicile").val();
    addUserInfo={
        cardtypessid:cardtypessid,
        cardnum:cardnum,
        username:username,
        age:age,
        sex:sex,
        both:both,
        politicsstatus:politicsstatus,
        domicile:domicile,
    }


    //收集案件信息
    var occurrencetime=$("#occurrencetime").val();
    addPolice_case={
        casename:casename,
        occurrencetime:occurrencetime,
    }



    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
            casessid:dqcasessid,
            adminssid:sessionadminssid,
            recordadminssid:recordadminssid,
            addUserInfo:addUserInfo,
            addPolice_case:addPolice_case,
            asknum:asknum,
            skipCheckbool:skipCheckbool,
            conversationbool:1,//开始谈话
        }
    };
   ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);

   
}
function callbackaddCaseToArraignment(data) {
    $("#startrecord_btn").attr("onclick","addCaseToArraignment();");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;
            if (isNotEmpty(recordssid)&&toUrltype==1){
                //跳转笔录制作
                var index = parent.layer.msg('开始进行笔录', {shade:0.1,time:500
                },function () {
                    var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_CLIENT,nextparam.nextPageId);
                        var toUrl=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
                        location.href=toUrl+"?ssid="+recordssid;
                    }
                });
            }else if(toUrltype==2){
                //跳转笔录查看列表
                /* $("#record_select", window.document).click();*///获取不到直接跳页面
                setpageAction(INIT_CLIENT, "client_web/police/record/addCaseToUser");
                var url = getActionURL(getactionid_manage().addCaseToUser_torecordIndex);
                location.href = url;
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
                        parent.layer.confirm("<span style='color:red'>"+msg+"</span>", {
                            btn: ['开始笔录',"查看笔录列表","取消"], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转笔录制作中");
                                //保存
                                skipCheckbool = 1;
                                addCaseToArraignment();
                                parent.layer.close(index);
                            },
                            btn2: function(index) {
                                console.log("跳转笔录列表")
                                toUrltype=2;
                                skipCheckbool =1;
                                addCaseToArraignment();
                                parent.layer.close(index);
                            },
                            btn3: function(index) {
                                parent.layer.close(index);
                            }
                        });
                    }
                }
            }else {
                parent.layer.msg(data.message);
            }
        }else {
            parent.layer.msg(data.message);
        }
    }
}


//全部证件
function getCards(){
    var url=getActionURL(getactionid_manage().startConversation_getCards);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetCards);
}
function callbackgetCards(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#cards').html("");
        if (isNotEmpty(data)){
            for (var i = 0; i < data.length; i++) {
                var l = data[i];
                $("#cards").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}




//根据身份证号码查找信息
function getUserByCard(){
    var cardnum =  $("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    var cardtypesssid=$("#cards option:selected").val();

    dquserssid=null;//当前用户的ssid
    dqcasessid=null;//当前案件ssid
    cases=null;
    $("#casename_ssid").html("");
    $("#casename").val("");
    asknum=0;

    if (!isNotEmpty(cardnum)){
        return;
    }
    var bool=checkout_cardnum(cardnum,cardtypetext);
    if (!bool){
        return;
    }

    var url=getActionURL(getactionid_manage().startConversation_getUserByCard);

    var data={
        token:INIT_CLIENTKEY,
        param:{
            cardtypesssid:cardtypesssid,
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

                /*人员信息*/
                $("#username").val(userinfo.username);
                $("#both").val(userinfo.both);
                $("#phone").val(userinfo.phone);
                $("#domicile").val(userinfo.domicile);
                $("#residence").val(userinfo.residence);
                $("#workunits").val(userinfo.workunits);
                $("#age").val(userinfo.age);
                $("#sex").val(userinfo.sex);
                $("#politicsstatus").val(userinfo.politicsstatus);
            }
            if (isNotEmpty(dquserssid)){
                getCaseById();
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


//证件号码
function getUserinfoList() {
    var url=getActionURL(getactionid_manage().startConversation_getUserinfoList);
    if (isNotEmpty(url)){
        var cardnum=$("#cardnum").val();
        var cardtypesssid=$("#cards option:selected").val();
        var data={
            token:INIT_CLIENTKEY,
            param:{
                cardtypesssid:cardtypesssid,
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
            var cardnum=$("#cardnum").val();
            if (isNotEmpty(data)){
                var userinfos=data.userinfos;
                if (isNotEmpty(userinfos)){
                    for (var i = 0; i < userinfos.length; i++) {
                        var userinfo = userinfos[i];
                        $("#cardnum_ssid").append("<dd lay-value='"+userinfo.cardnum+"' cardtypessid='"+userinfo.cardtypessid+"' cardtypename='"+userinfo.cardtypename+"' onmousedown='select_cardnum(this);'>"+userinfo.cardnum+"</dd>");

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
function select_cardnum(obj) {
    $("#cardnum_ssid").css("display","none");
    var cardnum=$(obj).attr("lay-value");
    var cardtypessid=$(obj).attr("cardtypessid");
    var cardtypename=$(obj).attr("cardtypename");
    $("#cardnum").val(cardnum);
    $(obj).focus();
    getUserByCard();
    $("#cardnum_ssid").html("");
}
function select_cardnumblur() {
    $("#cardnum_ssid").css("display","none");
    getUserByCard();
}

//案件查询
function getCaseById() {
    var url=getActionURL(getactionid_manage().startConversation_getCaseById);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
        }
    };
    ajaxSubmitByJson(url,data,callbakegetCaseById) ;
}
function callbakegetCaseById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var casesdata=data.cases;
                cases=casesdata;
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
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
    asknum=0;
    if (isNotEmpty(dqcasessid)&&isNotEmpty(cases)){

            for (var i = 0; i < cases.length; i++) {
                var c = cases[i];
                if (dqcasessid==c.ssid){
                     $("#occurrencetime").val(c.occurrencetime);
                     asknum=c.asknum;
                }
            }
    }
    $("#casename_ssid").html("");
    return;
}
function select_caseblur() {
    $("#casename_ssid").css("display","none");
    var casename=$("#casename").val();
    //需要验证案件ssid
    dqcasessid=null;
    asknum=0;
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c = cases[i];
            if (c.casename.trim()==casename.trim()) {
                dqcasessid=c.ssid;
                asknum=c.asknum;
                if (isNotEmpty(c.occurrencetime)){
                    $("#occurrencetime").val(c.occurrencetime);
                }
                break;
            }
        }
    }
    if (isNotEmpty(dqcasessid)&&isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c = cases[i];
            if (dqcasessid==c.ssid){
                $("#occurrencetime").val(c.occurrencetime);

            }
        }
    }
}

//获取全部管理员
function getAdminList() {
    var url=getActionURL(getactionid_manage().startConversation_getAdminList);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetAdminList);
}
function callbackgetAdminList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        otheruserinfos=data.data;
        $('#recordadmin option').not(":lt(1)").remove();
        if (isNotEmpty(otheruserinfos)){
            for (var i = 0; i < otheruserinfos.length; i++) {
                var u= otheruserinfos[i];
                if (u.ssid!=sessionadminssid) {
                    $("#recordadmin").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                }
            }
            $("#recordadmin").val(otheruserinfos[0].ssid);//默认选择第一个
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



//检验主身份证号码
function checkout_cardnum(cardnum,cardtypetext) {
    if ($.trim(cardtypetext)=="居民身份证"&&isNotEmpty(cardnum)||!isNotEmpty(cardtypetext)){
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(cardnum) === false) {
            parent.layer.msg("身份证输入不合法");
            /*init_form();*/
            return false;
        }
        //解析身份证
        cardnum = $.trim(cardnum);
        if (cardnum.length==15){
            return true;
        }else  if (cardnum.length==18){
            var birth = cardnum.substring(6, 10) + "-" + cardnum.substring(10, 12) + "-" + cardnum.substring(12, 14);
            $("#both").val(birth);
            var sex = parseInt(cardnum.substr(16, 1)) % 2;
            if (sex==1){
                sex=1;
            }else {
                sex=2;
            }
            $("#sex").val(sex);
            var myDate = new Date();
            var month = myDate.getMonth() + 1;
            var day = myDate.getDate();
            var age = myDate.getFullYear() - cardnum.substring(6, 10) - 1;
            if (cardnum.substring(10, 12) < month || cardnum.substring(10, 12) == month && cardnum.substring(12, 14) <= day) {
                age++;
            }
            $("#age").val(age);
            return true;
        }
        layui.use('form', function(){
            var $ = layui.$;
            var form = layui.form;
            form.render();
        });
    }
    return true;
}

$(function () {
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;

        form.on('select(change_card)', function(data){
            getUserByCard();
            form.render('select');
        });
    });
});
