var cases=null;//案件数据
var otheruserinfos=null;//其他询问人员数据
var cards=null;//全部证件类型


var dquserssid=null;//当前用户的ssid
var dqcasessid=null;//当前案件ssid

//开始笔录按钮
function addCaseToArraignment() {
    var  addUserInfo={};//新增人员的信息
    var  addPolice_case={};//新增案件的信息

  var url=getActionURL(getactionid_manage().addCaseToUser_addCaseToArraignment);
    var recordtypessid= $("td[recordtypebool='true']",parent.document).attr("recordtype");
    if (!isNotEmpty(recordtypessid)){
        parent.layer.msg("未找到选择的笔录类型");
        return;
    }

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

    var otheradminssid=$("#otheruserinfos  option:selected").val();
    if (!isNotEmpty(otheradminssid)){
        parent.layer.msg("询问人二不能为空");
        return;
    }

    var recordadminssid=$("#recordadmin  option:selected").val();
    if (!isNotEmpty(recordadminssid)){
        parent.layer.msg("记录人不能为空");
        return;
    }
    var recordplace=$("#recordplace").val();
    var askobj=$("#askobj  option:selected").val();
    if (!isNotEmpty(askobj)){
        parent.layer.msg("询问对象不能为空");
        return;
    }

    var asknum=$("#asknum").val();
    var recordname=$("#recordname").val();
    if (!isNotEmpty(recordname)){
        parent.layer.msg("笔录名称不能为空");
        return;
    }

    //收集人员信息
    var cardtypessid=$("#cards option:selected").val();
  /*  var  username=$("#username").val();*/
    var  beforename=$("#beforename").val();
    var  nickname= $("#nickname").val();
    var  age=$("#age").val();
    var  sex=$("#sex").val();
    var  both=$("#both").val();
    var  nationalssid=$("#national").val();
    var  nationalityssid=$("#nationality").val();
    var  professional=$("#professional").val();
    var  educationlevel=$("#educationlevel").val();
    var  politicsstatus=$("#politicsstatus").val();
    var  phone=$("#phone").val();
    var  domicile=$("#domicile").val();
    var  residence=$("#residence").val();
    var  workunits=$("#workunits").val();
    addUserInfo={
        cardtypessid:cardtypessid,
        cardnum:cardnum,
        username:username,
        beforename:beforename,
        nickname:nickname,
        age:age,
        sex:sex,
        both:both,
        nationalssid:nationalssid,
        nationalityssid:nationalityssid,
        professional:professional,
        educationlevel:educationlevel,
        politicsstatus:politicsstatus,
        phone:phone,
        domicile:domicile,
        residence:residence,
        workunits:workunits
    }


    //收集人员信息
   /* var casename=$("#casename").val();*/
    var cause=$("#cause").val();
    var casenum=$("#casenum").val();
    var occurrencetime=$("#occurrencetime").val();
    var caseway=$("#caseway").val();
    var starttime=$("#starttime").val();
    var endtime=$("#endtime").val();
    addPolice_case={
        casename:casename,
        cause:cause,
        casenum:casenum,
        occurrencetime:occurrencetime,
        starttime:starttime,
        endtime:endtime,
        caseway:caseway
    }

    //其他人
    var usertos=[];
    $("#tab_body #tab_content .layui-tab-item").each(function(){
        var tab_cardnum=$(this).find("input[name='tab_cardnum']").val();
        var index=$(this).index();
        if (isNotEmpty(tab_cardnum)){
            var otheruserssid=$(this).find("input[name='tab_otheruserssid']").val();
            var relation=$(this).find("select[name='tab_relation']").val();
            var language=$(this).find("select[name='tab_language']").val();
            var usertype=$(this).parent().parent().find(".layui-tab-title li").eq(index).attr("usertype");
            var usertitle=$(this).parent().parent().find(".layui-tab-title li").eq(index).text();
            var usertos={
                otheruserssid:otheruserssid,
                relation:relation,
                language:language,
                usertype:usertype==null?3:usertype,
                usertitle:usertitle,
            }
            usertos.push(usertos);
        }
    });

    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
            casessid:dqcasessid,
            adminssid:sessionadminssid,
            otheradminssid:otheradminssid,
            recordadminssid:recordadminssid,
            recordplace:recordplace,
            askobj:askobj,
            asknum:asknum,
            recordtypessid:recordtypessid,
            recordname:recordname,
            usertos:usertos,
            addUserInfo:addUserInfo,
            addPolice_case:addPolice_case
        }
    };
  ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}
function callbackaddCaseToArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var ssid=data.data;
        if (isNotEmpty(ssid)){
            var index = parent.layer.msg('开始进行笔录', {shade:0.1,time:500
            },function () {
                var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
                if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                    setpageAction(INIT_CLIENT,nextparam.nextPageId);
                    var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
                    parent.location.href=url+"?ssid="+ssid;
                }
                parent.layer.close(index);
            });
        }
    }else{
        parent.layer.msg(data.message);
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
        var cardnum=$("#ifranmehtml").contents().find("#cardnum").val();
        if (isNotEmpty(cardnum)) {
            layer.confirm('人员案件信息将会重置，确定要切换笔录类型吗', {
                btn: ['确认','取消'], //按钮
                shade: [0.1,'#fff'], //不显示遮罩
            }, function(index){
                $('#recotdtypes td').not(obj).css({"background-color":"#ffffff","color":"#000000"});
                $('#recotdtypes td').not(obj).attr("recordtypebool","false");

                $(obj).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
                $(obj).attr("recordtypebool","true");

                var url=getActionURL(getactionid_manage().addCaseToUser_toaddCaseToUserDetail);

                $("iframe").prop("src",url);
                layer.close(index);
            }, function(index){
                layer.close(index);
            });
        }else{
            $('#recotdtypes td').not(obj).css({"background-color":"#ffffff","color":"#000000"});
            $('#recotdtypes td').not(obj).attr("recordtypebool","false");

            $(obj).css({"background-color":"#1E9FFF","color":"#FFFFFF"});
            $(obj).attr("recordtypebool","true");

            var url=getActionURL(getactionid_manage().addCaseToUser_toaddCaseToUserDetail);

            $("iframe").prop("src",url);
        }
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
        parent.layer.msg(data.message);
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
        parent.layer.msg(data.message);
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
        cards=data;
        $('#cards').html("");
        $("#tab_content select[name='tab_card']").html("");
        if (isNotEmpty(data)){
            for (var i = 0; i < data.length; i++) {
                var l = data[i];
                $("#cards").append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                $("#tab_content select[name='tab_card']").each(function(){
                    $(this).append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                });
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

//获取全部管理员
function getAdminList() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getAdminList);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetAdminList);
}
function callbackgetAdminList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
         otheruserinfos=data.data;
        $('#otheruserinfos option').not(":lt(1)").remove();
        $('#recordadmin option').not(":lt(1)").remove();
        if (isNotEmpty(otheruserinfos)){
            for (var i = 0; i < otheruserinfos.length; i++) {
                var u= otheruserinfos[i];
                if (u.ssid!=sessionadminssid) {
                    $("#otheruserinfos").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    $("#recordadmin").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                }
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
}

/**
 * 获取人员信息
 */
function getUserByCard(){
    var cardnum =  $("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    var cardtypesssid=$("#cards option:selected").val();
     init_form();//初始化表单
    var bool=checkout_cardnum(cardnum,cardtypetext);
    if (!bool){
        return;
    }
    var url=getActionURL(getactionid_manage().addCaseToUser_getUserByCard);

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
            
            //案件select
           /* if (isNotEmpty(cases)){
                setcases(cases);
            }*/
        }
        /*其他在场人员数据渲染*/

    }else{
        parent.layer.msg(data.message);
    }
    layui.use('form', function(){
        var $ = layui.$;
        var form = layui.form;

        form.render();
    });
}
function setcases(cases){
    $("#casename_ssid").html("");
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c= cases[i];
            $("#casename_ssid").append("<dd lay-value='"+c.ssid+"' onmousedown='select_case(this);'>"+c.casename+"</dd>");
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
                        var recordname=""+username+"《"+casename+"》"+recordtypename+"_第"+(parseInt(c.asknum)+1)+"版";

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

/**
 * 其他在场人员的添加
 */
function tabAdd(){
    var html='<div class="layui-row layui-col-space10">\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">证件类型</label>\
                                                                <div class="layui-input-block">\
                                                                    <select name="tab_card" lay-verify="required"  disabled  lay-filter="tab_cardfilter">\
                                                                    <option value=""></option>\
                                                                    </select>\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">证件号码</label>\
                                                                <div class="layui-input-block">\
                                                                    <input type="text" name="tab_cardnum"  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">\
                                                                      <i class="layui-icon layui-icon-search" style="position: absolute;top:8px;right: 8px;" onclick="getUserByCard_other(this);"></i>\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">姓名</label>\
                                                                <div class="layui-input-block">\
                                                                    <input type="text" name="tab_username"  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">性别</label>\
                                                                <div class="layui-input-block">\
                                                                    <select name="tab_sex" lay-verify="required" >\
                                                                        <option value=""></option>\
                                                                        <option value="1">男</option>\
                                                                        <option value="2">女</option>\
                                                                        <option value="-1">未知</option>\
                                                                    </select>\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">联系电话</label>\
                                                                <div class="layui-input-block">\
                                                                    <input type="text" name="tab_phone"  lay-verify="required" placeholder="" autocomplete="off" class="layui-input">\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg12">\
                                                                  <input name="tab_otheruserssid" style="display: none;">\
                                                                   <button  class="layui-btn layui-btn-danger  layui-btn-sm"  onclick="tab_reset(this)" style="float: right">重置</button>\
                                                            </div>\
                                                        </div>';

    layui.use(['element','form'], function(){
        var form = layui.form;
        var element=layui.element;
        //使用模块
        parent.layer.prompt({title: '请输入标其他在场人员标题', formType: 0}, function(title, index){
            element.tabAdd('tabAdd_filter', {
                title:title
                ,content:html
            });
            $("#tab_content select[name='tab_card']").html("");
            if (isNotEmpty(cards)){
                for (var i = 0; i < cards.length; i++) {
                    var l = cards[i];
                    $("#tab_content select[name='tab_card']").each(function(){
                        $(this).append("<option value='"+l.ssid+"' > "+l.typename+"</option>");
                    });
                }
            }
            element.render();
            form.render();
            parent.layer.close(index);
        });

    });
}

//其他在场人员的查询
function getUserByCard_other(obj){
    var url=getActionURL(getactionid_manage().addCaseToUser_getUserByCard);
    var cards=$(obj).closest(".layui-tab-item").find("select[name='tab_card']").val();
    var cardnum=$(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val();

    var dqcardssid=$("#cards option:selected").val();
    var dqcardnum=$("#cardnum").val();


    if (!isNotEmpty(dqcardssid)||!isNotEmpty(dqcardnum)){ //判断主要人员信息是否搜索
        parent.layer.msg("请先获取人员基本信息");
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }
    if (dqcardssid==cards&&dqcardnum==cardnum){
        parent.layer.msg("被询问人不能作为其他在场人员");
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }
    //判断证件是否已经被搜索
    var num=0;
    $("#tab_content select[name='tab_card']").each(function(){
       var card_val=$(this).val();
       var card_num=$(this).closest(".layui-tab-item").find("input[name='tab_cardnum']").val();
        if (card_val==cards&&card_num==cardnum){
            num++;
        }
    });
    if (num>1){
        parent.layer.msg("该在场人员已存在");
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }
    var data={
        token:INIT_CLIENTKEY,
        param:{
            cardtypesssid:cards,
            cardnum:cardnum
        }
    };
    ajaxSubmitByJson(url,data,function callbackgetUserByCard_other(data){
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                var userinfo=data.userinfo;
                if (isNotEmpty(userinfo)){
                    /*人员信息*/

                    $(obj).closest(".layui-tab-item").find("input[name='tab_username']").val(userinfo.username);
                    $(obj).closest(".layui-tab-item").find("select[name='tab_sex']").val(userinfo.sex);
                    $(obj).closest(".layui-tab-item").find("input[name='tab_phone']").val(userinfo.phone);
                    $(obj).closest(".layui-tab-item").find("input[name='tab_otheruserssid']").val(userinfo.ssid);
                }
            }
        }else{
            parent.layer.msg(data.message);
            $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        }
        layui.use('form', function(){
            var form = layui.form;
            form.render();
        });
    });
}

//其他在场人员重置按钮
function tab_reset(obj){
    layui.use(['form'], function(){
        var form=layui.form;
        $(obj).closest(".layui-tab-item").find("input").val("");
        $(obj).closest(".layui-tab-item").find('select').prop('selectedIndex', 0);
        form.render('select');
    });
}

$(function () {
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;

        form.on('select(change_card)', function(data){
            dquserssid=null;//当前用户的ssid
            dqcasessid=null;//当前案件ssid
            cases=null;
                var form=layui.form;
                var laydate=layui.laydate;
                $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#asknum')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
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

                getUserByCard();

                form.render('select');
        });

        form.on('select(change_otheruserinfos)', function(data){
            var adminssid=data.value;
                for (var i = 0; i < otheruserinfos.length; i++) {
                    var u = otheruserinfos[i];
                    if (adminssid==u.ssid){
                        $("#otherworkname").val(u.workname);
                    }
                }
            form.render('select','change_otheruserinfos');
        });


    });
});


//废弃
function getCaseById() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getCaseById);
    var userssid=dquserssid;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:userssid
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
                if (isNotEmpty(cases)){
                    setcases(cases);
                }
            }
        }
    }else{
        parent.layer.msg(data.message);
    }
}


/*
用户
 */
function getUserinfoList() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getUserinfoList);
    var cardtypesssid=$("#cards option:selected").val();
    var cardnum=$("#cardnum").val();
    if (!isNotEmpty(cardnum)) {
        $("#cardnum_ssid").html("");
        $("#cardnum_ssid").append('<p class="layui-select-none">无匹配项</p>');
        return;
    }
    var data={
        token:INIT_CLIENTKEY,
        param:{
            cardtypesssid:cardtypesssid,
            cardnum:cardnum
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserinfoList);
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
function select_cardnum(obj) {
    $("#cardnum_ssid").css("display","none");
    var cardnum=$(obj).attr("lay-value");
    $("#cardnum").val(cardnum);
    getUserByCard();
    $("#cardnum_ssid").html("");
}
function select_cardnumblur() {
    $("#cardnum_ssid").css("display","none");
    getUserByCard();
}

//案件
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
                $("#casename_ssid").append("<dd lay-value='"+cl.ssid+"' onmousedown='select_case(this);'>"+cl.casename+"</dd>");
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
                    var recordname=""+username+"《"+casename+"》"+recordtypename+"_第"+(parseInt(c.asknum)+1)+"版";

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
                var recordname=""+username+"《"+casename+"》"+recordtypename+"_第"+(parseInt(c.asknum)+1)+"版";

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
        var recordname=""+username+"《"+casename+"》"+recordtypename+"_第"+(parseInt(asknum)+1)+"版";
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
}

//案由
function getCauseList() {
    $("#cause_text").html("");
    var causelike=[];
    var cause = $("#cause").val();
    var causeList=["人格权纠纷","婚姻家庭纠纷","继承纠纷","不动产登记纠纷","物权保护纠纷","所有权纠纷","用益物权纠纷","担保物权纠纷","占有保护纠纷","合同纠纷","不当得利纠纷","无因管理纠纷","知识产权合同纠纷","知识产权权属、侵权纠纷","不正当竞争纠纷","垄断纠纷","劳动争议","人事争议","海事海商纠纷","与企业有关的纠纷","与公司有关的纠纷","合伙企业纠纷","与破产有关的纠纷","证券纠纷"];
        if (isNotEmpty(causeList)){
            for (var i = 0; i < causeList.length; i++) {
                var c = causeList[i];
                if (c.indexOf(cause) >= 0) {
                    causelike.push(c);
                }
            }
            if (isNotEmpty(causelike)){
                for (var j = 0; j < causelike.length; j++) {
                    var cl=causelike[j];
                    $("#cause_text").append("<dd lay-value='"+cl+"' onmousedown='select_cause(this);'>"+cl+"</dd>");
                }
            }
            $("#cause_text").css("display","block");
        }
}
function select_cause(obj) {
    $("#cause_text").css("display","none");
    var cause=$(obj).attr("lay-value");
    $("#cause").val(cause);
    $("#cause_text").html("");
}
function select_causeblur() {
    $("#cause_text").css("display","none");
}

//到案方式
function getCasewayList() {
    $("#caseway_text").html("");
    var casewaylike=[];
    var caseway = $("#caseway").val();
    var casewayList=["自首","口头传唤","强制传唤","传唤证传唤","拘传","群众扭送","移送","110指令","来所报案","日常工作中发现"];
    if (isNotEmpty(casewayList)){
        for (var i = 0; i < casewayList.length; i++) {
            var c = casewayList[i];
            if (c.indexOf(caseway) >= 0) {
                casewaylike.push(c);
            }
        }
        if (isNotEmpty(casewaylike)){
            for (var j = 0; j < casewaylike.length; j++) {
                var cl=casewaylike[j];
                $("#caseway_text").append("<dd lay-value='"+cl+"' onmousedown='select_caseway(this);'>"+cl+"</dd>");
            }
        }
        $("#caseway_text").css("display","block");
    }
}
function select_caseway(obj) {
    $("#caseway_text").css("display","none");
    var caseway=$(obj).attr("lay-value");
    $("#caseway").val(caseway);
    $("#caseway_text").html("");
}
function select_casewayblur() {
    $("#caseway_text").css("display","none");
}

//检验主身份证号码
function checkout_cardnum(cardnum,cardtypetext) {
    if ($.trim(cardtypetext)=="居民身份证"&&isNotEmpty(cardnum)){
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(cardnum) === false) {
            parent.layer.msg("身份证输入不合法");
            init_form();
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


//初始化页面
function init_form() {
     dquserssid=null;//当前用户的ssid
     dqcasessid=null;//当前案件ssid
     cases=null;
    layui.use(['form','laydate'], function(){
        var form=layui.form;
        var laydate=layui.laydate;
        $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#asknum')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
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