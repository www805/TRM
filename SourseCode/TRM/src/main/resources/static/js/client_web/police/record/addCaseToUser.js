var cases=null;//案件数据
var otheruserinfos=null;//询问人员数据


function addCaseToArraignment() {
    var url=getActionURL(getactionid_manage().addCaseToUser_addCaseToArraignment);

    var cardnum1=$("#cardnum1").val();
    if (!isNotEmpty(cardnum1)){
        layer.msg("证件号码不能为空");
        return;
    }

    var recordtypessid= $("td[recordtypebool='true']",parent.document).attr("recordtype");
    if (!isNotEmpty(recordtypessid)){
        layer.msg("系统异常");
        return;
    }



    var casessid=$("#casename  option:selected").val();
    if (!isNotEmpty(casessid)){
        layer.msg("案件不能为空");
        return;
    }

    var adminssid=$("#adminssid1").val();
    var otheradminssid=$("#otheruserinfos  option:selected").val();
    if (!isNotEmpty(otheradminssid)){
        layer.msg("询问人二不能为空");
        return;
    }

    var recordadminssid=$("#recordadmin  option:selected").val();
    if (!isNotEmpty(recordadminssid)){
        layer.msg("记录人不能为空");
        return;
    }
    var recordplace=$("#recordplace").val();
    var askobj=$("#askobj  option:selected").val();
    if (!isNotEmpty(askobj)){
        layer.msg("询问对象不能为空");
        return;
    }
    var asknum=$("#asknum  option:selected").val();



    var data={
        token:INIT_CLIENTKEY,
        param:{
            casessid:casessid,
            adminssid:adminssid,
            otheradminssid:otheradminssid,
            recordadminssid:recordadminssid,
            recordplace:recordplace,
            askobj:askobj,
            asknum:asknum,
            recordtypessid:recordtypessid
        }
    };
    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}

function callbackaddCaseToArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
            if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                setpageAction(INIT_CLIENT,nextparam.nextPageId);
                var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
                parent.location.href=url+"?ssid="+data;
            }
        }
    }else{
        layer.msg(data.message);
    }
}

/**
 * 获取笔录类型列表
 */
function getRecordtypes() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getRecordtypes);
    var pid=$("#pid option:selected").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            pid:pid
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordtypes);
}
function callbackgetRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var list=data.getRecordtypesVOParamList;
           gettree(list);
        }
    }else{
        layer.msg(data.message);
    }
}
function gettree(data){
    if (isNotEmpty(data)){
        for (var i = 0; i < data.length; i++) {
            var l = data[i];
            var html='<div class="layui-colla-item" style="border: 1px solid #FFFFFF">\
                                                       <h2 class="layui-colla-title layui-elem-quote" style="background-color: #FFFFFF;margin-bottom:0;border-left: 5px solid #1E9FFF;">'+l.typename+'</h2>\
                                                       <div class="layui-colla-content layui-show" style="padding: 0;border: 0">\
                                                           <table class="layui-table"  lay-skin="nob" >';
            if (l.police_recordtypes.length>0){
                for (var j = 0; j < l.police_recordtypes.length; j++) {
                    var ls = l.police_recordtypes[j];
                    html+=' <tr><td recordtype='+ls.ssid+' recordtypebool="false">'+ls.typename+'</td></tr>';
                }
            }
            html+='</table> </div></div>';
            $("#recotdtypes").append(html);
        }
    }
    $('#recotdtypes td').eq(0).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
    $('#recotdtypes td').eq(0).attr("recordtypebool","true");

    $('#recotdtypes td').click(function() {
        var obj=this;
       layer.confirm('人员案件信息将会重置，确定要切换笔录类型吗', {
            btn: ['确认','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            $('#recotdtypes td').not(obj).css({"background-color":"#ffffff","color":"#000000"});
           $('#recotdtypes td').not(obj).attr("recordtypebool","false");

           $(obj).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
           $(obj).attr("recordtypebool","true");

           $("iframe").prop("src","/cweb/police/policePage/toaddCaseToUserDetail");/*target="ifranmehtml"  href="/cweb/police/policePage/toaddCaseToUserDetail"*/
            layer.close(index);
        }, function(index){
            layer.close(index);
        });

    });
    layui.use(['element','form'], function(){
        var element = layui.element;
        var form=layui.form;
        element.render();
        form.render();
    });
}

/**
 * 获取国籍
 */
function getNationalitys(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getNationalitys);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetNationalitys);
}
function callbackgetNationalitys(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#nationality option').not(":lt(1)").remove();
        if (isNotEmpty(data)){
            if (isNotEmpty(data)) {
                    for (var i = 0; i < data.length; i++) {
                        var l = data[i];
                        $("#nationality").append("<option value='"+l.ssid+"' title='"+l.enname+"'> "+l.zhname+"</option>");
                    }
            }
        }
    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

/**
 * 获取民族
 */
function getNationals(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getNationals);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetNationals);
}
function callbackgetNationals(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#national option').not(":lt(1)").remove();
        if (isNotEmpty(data)) {
            for (var i = 0; i < data.length; i++) {
                var l = data[i];
                $("#national").append("<option value='"+l.ssid+"' title='"+l.nationname+"'>"+l.nationname+"</option>");
            }
        }
    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

/**
 * 获取证件类型
 */
function getCards(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getCards);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetCards);
}
function callbackgetCards(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        $('#cards1 option').not(":lt(1)").remove();
        $('#cards2 option').not(":lt(1)").remove();
        $('#cards3 option').not(":lt(1)").remove();
        $('#cards4 option').not(":lt(1)").remove();
        if (isNotEmpty(data)){
            if (isNotEmpty(data)) {
                for (var i = 0; i < data.length; i++) {
                    var l = data[i];
                    $("#cards1").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                    $("#cards2").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                    $("#cards3").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                    $("#cards4").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

/**
 * 获取人员信息
 */
function getUserByCard(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getUserByCard);
    var cards1=$("#cards1 option:selected").val();
    var cardnum1=$("#cardnum1").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            cardtypesssid:cards1,
            cardnum:cardnum1
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserByCard);
}
function callbackgetUserByCard(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var userinfo=data.userinfo;
             cases=data.cases;
             otheruserinfos=data.otheruserinfos;
            
            if (isNotEmpty(userinfo)){
                /*人员信息*/
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
                $("#sex").find("option[value='"+userinfo.sex+"']").attr("selected",true);
                $("#national").find("option[value='"+userinfo.nationalssid+"']").attr("selected",true);
                $("#nationality").find("option[value='"+userinfo.nationalityssid+"']").attr("selected",true);
                $("#educationlevel").find("option[value='"+userinfo.educationlevel+"']").attr("selected",true);
                $("#politicsstatus").find("option[value='"+userinfo.politicsstatus+"']").attr("selected",true);
            }

            $('#casename option').not(":lt(1)").remove();
            if (isNotEmpty(cases)){
                setcases(cases);
            }

            $('#otheruserinfos option').not(":lt(1)").remove();
            $('#recordadmin option').not(":lt(1)").remove();
            if (isNotEmpty(otheruserinfos)){
                for (var i = 0; i < otheruserinfos.length; i++) {
                    var u= otheruserinfos[i];
                    $("#otheruserinfos").append("<option value='"+u.ssid+"' >"+u.username+"</option>")
                    $("#recordadmin").append("<option value='"+u.ssid+"' >"+u.username+"</option>")
                }
            }

        }

        /*其他在场人员数据渲染*/
        $("#cards2").find("option[value='2']").attr("selected",true);
        $("#cardnum2").val("4301978784564564");
        $("#username2").val("李四");
        $("#phone2").val("19646852384");
        $("#sex2").find("option[value='1']").attr("selected",true);
        $("#language").find("option[value='2']").attr("selected",true);

        $("#cards3").find("option[value='2']").attr("selected",true);
        $("#cardnum3").val("44532456478787874");
        $("#username3").val("李吴");
        $("#phone3").val("1486787787");
        $("#sex3").find("option[value='1']").attr("selected",true);

        $("#cards4").find("option[value='2']").attr("selected",true);
        $("#cardnum4").val("42454656457457458");
        $("#username4").val("李留");
        $("#phone4").val("79814132454");
        $("#sex4").find("option[value='1']").attr("selected",true);
        $("#relation").find("option[value='2']").attr("selected",true);


    }else{
        layer.msg(data.message);
    }
    layui.use(['element','form'], function(){
        var element = layui.element;
        var form=layui.form;
        element.render();
        form.render();
    });
}
function setcases(cases){
    $('#casename option').not(":lt(1)").remove();
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c= cases[i];
            $("#casename").append("<option value='"+c.ssid+"' title='"+c.casename+"'>"+c.occurrencetime+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+c.casename+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+c.casenum+"</option>")
        }
    }
}



$(function () {
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;
        var laydate = layui.laydate;
        form.on('select(change_card)', function(data){
            var val=data.value;
            $("input:not('#adminssid1'):not('#workname1'):not('#recordplace')").val("");
            $('select').not("#cards1").prop('selectedIndex', 0);

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
           $("#adminssid1").val(sessionusername);
            $("#workname1").val(sessionusername);
            $("#recordplace").val(sessionworkname);
            //使用模块
            form.render();
        });

        form.on('select(change_case)', function(data){
            var casessid=data.value;
            var title=data.elem[data.elem.selectedIndex].title;
           /* $("#casename").find("option[value='"+casessid+"']").text(title);
           form.render();

            setcases(cases);*/
            $("#cause").val("");
            $("#casenum").val("");
            $("#occurrencetime").val("");
            $("#starttime").val("");
            $("#endtime").val("");
            $("#caseway").val("");
            if (isNotEmpty(cases)){
                for (var i = 0; i < cases.length; i++) {
                    var c = cases[i];
                    if (casessid==c.ssid){
                        $("#cause").val(c.cause);
                        $("#casenum").val(c.casenum);
                        $("#occurrencetime").val(c.occurrencetime);
                        $("#starttime").val(c.starttime);
                        $("#endtime").val(c.endtime);
                        $("#caseway").val(c.caseway);
                    }
                }
            }
        });

        form.on('select(change_otheruserinfos)', function(data){
            var adminssid=data.value;
            if (isNotEmpty(cases)){
                for (var i = 0; i < otheruserinfos.length; i++) {
                    var u = otheruserinfos[i];
                    if (adminssid==u.ssid){
                        $("#otherworkname").val(u.workname);
                    }
                }
            }
            form.render('select','change_otheruserinfos');
        });


    });
});
