/*
   开启谈话
 */

var dquserssid;//当前用户ssid
var dqcasessid;//当前案件
var dqothercasessid;//当前其他案件的ssid

var cases;//全部案件
var othercases=null;
var otheruserinfos;//工作人员

var skipCheckbool=-1;//是否跳过检测：默认-1
var skipCheckCasebool=-1;
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
   /* if ($.trim(cardtypetext)=="居民身份证"){
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(cardnum) === false) {
            parent.layer.msg("身份证输入不合法");
            return false;
        }
    }*/
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


    $("#startrecord_btn").attr("lay-filter","");

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
            skipCheckCasebool:skipCheckCasebool,
            conversationbool:1,//开始谈话
        }
    };
   ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);

   
}
function callbackaddCaseToArraignment(data) {
    $("#startrecord_btn").attr("lay-filter","startrecord_btn");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;
            if (isNotEmpty(recordssid)&&toUrltype==1){
                var index = parent.layer.msg('开始进行审讯', {shade:0.1,time:500
                },function () {
                    //跳转笔录制作
                    var url=getActionURL(getactionid_manage().startConversation_towaitRecord);
                    if (parentbool==-1){
                        window.location.href=url+"?ssid="+recordssid
                    } else {
                        parent.location.href=url+"?ssid="+recordssid
                    }

                });
            }else if(toUrltype==2){
                if (parentbool==-1){
                    window.location.href =  window.parent.conversationIndexURL;
                }else {
                    parent.location.href =  window.parent.conversationIndexURL;
                }

            }
        }
    }else{
        var data2=data.data;
        if (isNotEmpty(data2)){
            var recordingbool=data2.recordingbool
            var recordssid=data2.recordssid;
            var checkStartRecordVO=data2.checkStartRecordVO;



            var case_=data2.case_;
            var caseingbool=data2.caseingbool;

            if (null!=caseingbool&&caseingbool==true&&isNotEmpty(case_)){
                var casename=case_.casename==null?"":case_.casename;
                var cause=case_.cause==null?"":case_.cause;
                var occurrencetime=case_.occurrencetime==null?"":case_.occurrencetime;
                var casenum=case_.casenum==null?"":case_.casenum;
                var department=case_.department==null?"":case_.department;
                var userInfos=case_.userInfos;
                var USERHTNL="";
                if(null!=userInfos) {for (let i = 0; i < userInfos.length; i++) {const u = userInfos[i];USERHTNL += u.username + "、";} USERHTNL = (USERHTNL .substring(USERHTNL .length - 1) == '、') ? USERHTNL .substring(0, USERHTNL .length - 1) : USERHTNL ;}
                var  init_casehtml=" <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>案件嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>";
                var TABLE_HTML='<table class="layui-table" lay-even lay-skin="nob" style="table-layout: fixed">'+init_casehtml+' <tbody id="case_html"></tbody>\
                </table>';
                parent.layer.open({
                    type:1,
                    title: '案件信息(案件正在<strong style="color: red">休庭</strong>中...)',
                    shade: 0.3,
                    resize:false,
                    area: ['35%', '400px'],
                    content: TABLE_HTML,
                    btn: ['继续笔录', '取消',]
                    ,yes: function(index, layero){
                        //按钮【按钮一】的回调
                        skipCheckCasebool = 1;
                        addCaseToArraignment();
                        parent.layer.close(index);
                    },
                    btn2: function(index) {
                        parent.layer.close(index);
                    }
                });

            }else if (null!=recordingbool&&recordingbool==true){
                //存在笔录正在进行中，跳转笔录列表，给出提示：建议他先结束制作中的
                if (isNotEmpty(checkStartRecordVO)){
                    var msg=checkStartRecordVO.msg;
                    if (isNotEmpty(msg)){
                        parent.layer.confirm("<span style='color:red'>"+msg+"</span>", {
                            btn: ['开始审讯',"查看审讯列表","取消"], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转审讯制作控制台");
                                //保存
                                skipCheckbool = 1;
                                addCaseToArraignment();
                                parent.layer.close(index);
                            },
                            btn2: function(index) {
                                console.log("跳转审讯列表")
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
                var othercasesdata=data.othercases;
                cases=casesdata;
                othercases=othercasesdata;
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
    if (!isNotEmpty(dqcasessid)&&isNotEmpty(othercases)) {
        for (var i = 0; i < othercases.length; i++) {
            var c = othercases[i];
            if (c.casename.trim()==casename.trim()) {
                dqcasessid=c.ssid;
                asknum=c.arraignments==null?0:c.arraignments.length;
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


function open_othercases() {
    var dqcardssid=$("#cards option:selected").val();
    var dqcardnum=$("#cardnum").val();
    if (!isNotEmpty(dqcardssid)||!isNotEmpty(dqcardnum)){
        parent.layer.msg("请先获取人员基本信息",{icon:5});
        return;
    }
    if (!isNotEmpty(othercases)){
        parent.layer.msg("暂未找到其他案件",{icon:5});
        return;
    }
    var CASE_HTML='<form class="layui-form layui-row" ><table class="layui-table" lay-skin="line" style="table-layout: fixed;">\
                     <tbody id="othercases_html" >';
    for (let i = 0; i < othercases.length; i++) {
        const othercase = othercases[i];
        CASE_HTML+='<tr  ssid="'+othercase.ssid+'"><td >'+othercase.casename+'</td></tr>';
    }
    CASE_HTML+='</tbody>\
                </table></form>';
    parent.layer.open({
        type:1,
        title: '选择其他案件',
        shade: 0.3,
        resize:false,
        area: ['25%', '400px'],
        content: CASE_HTML,
        btn: ['确认', '取消']
        ,yes: function(index, layero){
            //回填案件信息
            if (isNotEmpty(dqothercasessid)&&isNotEmpty(othercases)) {
                dqcasessid=dqothercasessid;
                $("#casename").val("");
                for (var i = 0; i < othercases.length; i++) {
                    var c = othercases[i];
                    if (dqcasessid==c.ssid){
                        $("#casename").val(c.casename);
                        if (isNotEmpty(c.occurrencetime)){
                            $("#occurrencetime").val(c.occurrencetime);
                        }
                    }
                }
            }
            $("#casename_ssid").html("");
            dqothercasessid=null;
            parent.layer.close(index);
        },
        btn2: function(index) {
            dqothercasessid=null;
            parent.layer.close(index);
        }
    });

    $("#othercases_html tr",parent.document).click(function () {
        $(this).css({"background-color":" #f2f2f2"}).siblings().css({"background-color":" #fff"});
        dqothercasessid= $(this).attr("ssid");
    });

}


//检验主身份证号码
function checkout_cardnum(cardnum,cardtypetext) {
    var nationality = $("#nationality option:selected").text();//国籍
    if (!($.trim(nationality)=="中国"||!isNotEmpty(nationality))){
        return true;
    }
    if ($.trim(cardtypetext)=="居民身份证"&&isNotEmpty(cardnum)||!isNotEmpty(cardtypetext)){
        var reg = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        if(reg.test(cardnum) === false) {
          /*  parent.layer.msg("身份证输入不合法");*/
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
                        parent.layer.msg(retmsg,{icon:6,time:1000},function () {
                            getUserByCard();
                        });
                    }
            },
            error: function (e) {
            }
        });
    });

});


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
