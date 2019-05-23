var cases=null;//案件数据
var otheruserinfos=null;//其他询问人员数据
var cards=null;//全部证件类型

var dqcardssid=null;//当前人员证件ssid
var dqcardnum=null;//当前人员证件号码
var dquserssid=null;//当前用户的ssid

//开始笔录按钮
function addCaseToArraignment() {
   /* var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
        setpageAction(INIT_CLIENT,nextparam.nextPageId);
        var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
        parent.location.href=url+"?ssid=80d914a33e094e1daa07d7740b4fed28";
    }*/
  var url=getActionURL(getactionid_manage().addCaseToUser_addCaseToArraignment);

    var cardnum=$("#cardnum").val();
    if (!isNotEmpty(cardnum)){
        layer.msg("证件号码不能为空");
        return;
    }

    var recordtypessid= $("td[recordtypebool='true']",parent.document).attr("recordtype");
    if (!isNotEmpty(recordtypessid)){
        layer.msg("未找到选择的笔录类型");
        return;
    }



    var casessid=$("#casename  option:selected").val();
    if (!isNotEmpty(casessid)){
        layer.msg("案件不能为空");
        return;
    }


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

    var asknum=$("#asknum").val();
    var recordname=$("#recordname").val();
    if (!isNotEmpty(recordname)){
        layer.msg("笔录名称不能为空");
        return;
    }
      var arr=[];
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
            arr.push(usertos);
        }
    });


    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
            casessid:casessid,
            adminssid:sessionadminssid,
            otheradminssid:otheradminssid,
            recordadminssid:recordadminssid,
            recordplace:recordplace,
            askobj:askobj,
            asknum:asknum,
            recordtypessid:recordtypessid,
            recordname:recordname,
            usertos:arr
        }
    };
  ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}
function callbackaddCaseToArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var ssid=data.data;
        if (isNotEmpty(ssid)){
            var index = layer.msg('开始进行笔录', {shade:0.1
            },function () {
                var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
                if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                    setpageAction(INIT_CLIENT,nextparam.nextPageId);
                    var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
                    parent.location.href=url+"?ssid="+ssid;
                }
                layer.close(index);
            });
        }
    }else{
        layer.msg(data.message);
    }
}

/**
 * 获取笔录类型列表左侧显示
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
    var cards=$("#cards option:selected").val();
    var cardnum=$("#cardnum").val();
     dqcardssid=cards;
     dqcardnum=cardnum;

    var data={
        token:INIT_CLIENTKEY,
        param:{
            cardtypesssid:cards,
            cardnum:cardnum
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserByCard);
}
function callbackgetUserByCard(data){
    layui.use(['form'], function(){
        var form=layui.form;
        $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#occurrencetime'):not('#starttime'):not('#endtime'):not('#asknum')").val("");
        $('select').not("#cards").prop('selectedIndex', 0);
        $('#casename option').not(":lt(1)").remove();
        form.render('select');
    });
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var userinfo=data.userinfo;
            cases=data.cases;
            otheruserinfos=data.otheruserinfos;
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

            //案件select
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

    }else{
        layer.msg(data.message);
    }
    layui.use('form', function(){
        var $ = layui.$;
        var form = layui.form;

        form.render();
    });
}
function setcases(cases){
    $('#casename option').not(":lt(1)").remove();
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c= cases[i];
            //  $("#casename").append("<option value='"+c.ssid+"' title='"+c.casename+"'>"+c.occurrencetime+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+c.casename+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+c.casenum+"</option>")
            $("#casename").append("<option value='"+c.ssid+"' title='"+c.casename+"'>"+c.casename+"</option>")
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
        layer.prompt({title: '请输入标其他在场人员标题', formType: 0}, function(title, index){
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
            layer.close(index);
        });

    });
}

//其他在场人员的查询
function getUserByCard_other(obj){
    var url=getActionURL(getactionid_manage().addCaseToUser_getUserByCard);
    var cards=$(obj).closest(".layui-tab-item").find("select[name='tab_card']").val();
    var cardnum=$(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val();

    if (!isNotEmpty(dqcardssid)||!isNotEmpty(dqcardnum)){ //判断主要人员信息是否搜索
        layer.msg("请先获取人员基本信息");
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }
    if (dqcardssid==cards&&dqcardnum==cardnum){
        layer.msg("被询问人不能作为其他在场人员");
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
        layer.msg("该在场人员已存在");
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
            layer.msg(data.message);
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
            $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#occurrencetime'):not('#starttime'):not('#endtime'):not('#asknum')").val("");
            $('select ').not("#cards").prop('selectedIndex', 0)
            $('#casename option').not(":lt(1)").remove();
            form.render();
        });

        form.on('select(change_case)', function(data){
            var casessid=data.value;
            $("#cause").val("");
            $("#casenum").val("");
            $("#occurrencetime").val("");
            $("#starttime").val("");
            $("#endtime").val("");
            $("#caseway").val("");
            $("#asknum").val("0");
            $("#recordname").val("");



            if (isNotEmpty(cases)){
                for (var i = 0; i < cases.length; i++) {
                    var c = cases[i];
                    if (casessid==c.ssid){
                        var casename=$("#casename").find("option:selected").text();
                        var recordtypename=$("td[recordtypebool='true']",parent.document).text();
                        var recordname="《"+casename+"》"+recordtypename+"_第"+(parseInt(c.asknum)+1)+"版";


                        $("#cause").val(c.cause);
                        $("#casenum").val(c.casenum);
                        $("#occurrencetime").val(c.occurrencetime);
                        $("#starttime").val(c.starttime);
                        $("#endtime").val(c.endtime);
                        $("#caseway").val(c.caseway);
                        $("#asknum").val(c.asknum);
                        $("#recordname").val(recordname);
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
