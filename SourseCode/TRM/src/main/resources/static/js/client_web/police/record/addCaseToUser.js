var cases=null;//全部案件数据
var othercases=null;//出开自己全部的案件信息
var otheruserinfos=null;//其他询问人员数据
var cards=null;//全部证件类型
var workunits=null;//全部的工作单位
var modelList=null;//全部模板列表
var wordList=null;//全部模板列表

var dquserssid=null;//当前用户的ssid
var dqcasessid=null;//当前案件ssid
var dqmodelssid=null;//当前所选的会议模板ssid
var dqwordssid=null;//当前笔录模板ssid
var dqotheruserinfossid=null;//当前询问人(新增询问人回显)
var dqotherworkssid=null;//当前询问人对应的工作单位
var dqothercasessid;//当前其他案件的ssid

var skipCheckbool=-1;//是否跳过检测：默认-1
var skipCheckCasebool=-1;
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表




//开始笔录按钮
function addCaseToArraignment() {
    var  addUserInfo={};//新增人员的信息
    var  addPolice_case={};//新增案件的信息

  var url=getActionURL(getactionid_manage().addCaseToUser_addCaseToArraignment);
    var recordtypessid= $("td[recordtypebool='true']",parent.document).attr("recordtype");
    if (!isNotEmpty(recordtypessid)){
        parent.layer.msg("未找到选择的笔录类型",{icon: 5});
        return;
    }

    var multifunctionbool=2;//功能类型:
    var multifunction_bool=$("#multifunctionbool",parent.document).prop("checked");
        if (multifunction_bool==true){
            multifunctionbool=2;
        }else if (multifunction_bool==false){
            multifunctionbool=3;
        }

    var cardnum=$("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    if (!isNotEmpty(cardnum)){
        parent.layer.msg("证件号码不能为空",{icon: 5});
        return;
    }


    var  username=$("#username").val();
    if (!isNotEmpty(username)){
        parent.layer.msg("姓名不能为空",{icon: 5});
        return;
    }

    var  casename=$("#casename").val();
    if (!isNotEmpty(casename)){
        parent.layer.msg("案件名称不能为空",{icon: 5});
        return;
    }

    var asknum=$("#asknum").val();
    var recordname=$("#recordname").val();
    if (!isNotEmpty(recordname)){
        parent.layer.msg("笔录名称不能为空",{icon: 5});
        return;
    }

    var recordadminssid=$("#recordadmin  option:selected").val();
    if (!isNotEmpty(recordadminssid)){
        parent.layer.msg("记录人不能为空",{icon: 5});
        return;
    }
    var recordplace=$("#recordplace").val();
    var askobj=$("#askobj").val();
    if (!isNotEmpty(askobj)){
        parent.layer.msg("询问对象不能为空",{icon: 5});
        return;
    }

    //其他人
    var usertos=[];
    var ck=true;
    var ck_msg="";
    $("#tab_body #tab_content .layui-tab-item").each(function(){
        var tab_cardnum=$(this).find("input[name='tab_cardnum']").val();
        var index=$(this).index();
        if (isNotEmpty(tab_cardnum)){
            var otheruserssid_=$(this).find("input[name='tab_otheruserssid']").val();
            var relation_=$(this).find("select[name='tab_relation']").val();
            var language_=$(this).find("select[name='tab_language']").val();
            var usertype_=$(this).parent().parent().find(".layui-tab-title li").eq(index).attr("usertype");
            var usertitle_=$(this).parent().parent().find(".layui-tab-title li").eq(index).attr("lay-id");

            var cardtypessid_=$(this).find("select[name='tab_card']").val();
            var cardtypetext_=$(this).find("select[name='tab_card'] option:selected").text();

            var cardnum_=$(this).find("input[name='tab_cardnum']").val();
            var username_=$(this).find("input[name='tab_username']").val();
            var sex_=$(this).find("select[name='tab_sex']").val();
            var phone_=$(this).find("input[name='tab_phone']").val();


            //开始验证
            var bool=  checkByIDCard(cardnum_);
            if (!bool){
                $(this).find("input[name='tab_cardnum']").focus();
                ck_msg="其他在场人员身份证号码无效";
                ck=false;
                layui.use(['element'], function(){
                    var element = layui.element;
                    element.tabChange("tabAdd_filter", usertitle_);
                });
                return false;
            }

            if (cardnum==cardnum_){
                $(this).find("input[name='tab_cardnum']").focus();
                ck_msg="被询问人不能作为其他在场人员";
                ck=false;
                layui.use(['element'], function(){
                    var element = layui.element;
                    element.tabChange("tabAdd_filter", usertitle_);
                });
                return false;
            }

            for (let i = 0; i < usertos.length; i++) {
                const userto = usertos[i];
                if (userto.cardnum==cardnum_) {
                    ck_msg="其他在场人员不能重复";
                    ck=false;
                    layui.use(['element'], function(){
                        var element = layui.element;
                        element.tabChange("tabAdd_filter", usertitle_);
                    });
                    return false;
                }
            }


            if (!isNotEmpty(username_)){
                $(this).find("input[name='tab_username']").focus();
                ck_msg="其他在场人员姓名不能为空";
                ck=false;
                layui.use(['element'], function(){
                    var element = layui.element;
                    element.tabChange("tabAdd_filter", usertitle_);
                });
                return false;
            }

            if (isNotEmpty(phone_)&&!(/^0?(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[89])[0-9]{8}$/.test(phone_))){
                $(this).find("input[name='tab_phone']").focus();
                ck_msg="其他在场人员联系电话无效";
                ck=false;
                layui.use(['element'], function(){
                    var element = layui.element;
                    element.tabChange("tabAdd_filter", usertitle_);
                });
                return false;
            }




            var usertos_={
                otheruserssid:otheruserssid_,
                relation:relation_,
                language:language_,
                usertype:usertype_==null?3:usertype_,
                usertitle:usertitle_,
                cardtypessid:cardtypessid_,//当前证件类型ssid
                cardnum:cardnum_,//当前证件号码
                username:username_,//姓名
                sex:sex_,//性别
                phone:phone_ //联系电话
            }
            usertos.push(usertos_);
        }
    });
    if(!ck){
        parent.layer.msg(ck_msg,{icon: 5});
        return;
    }



    $("#startrecord_btn").attr({"lay-filter":"","disabled":"disabled"});

    //收集人员信息
    var cardtypetext=$("#cards option:selected").text();
    var nationality = $("#nationality option:selected").text();//国籍
    if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
        var bool=checkByIDCard(cardnum);
        if (bool){
            //回填身份证分析数据
            $("#both").val(getAnalysisIdCard(cardnum,1));
            $("#sex").val(getAnalysisIdCard(cardnum,2));
            $("#age").val(getAnalysisIdCard(cardnum,3));
        }
    }

    var  cardtypessid=$("#cards option:selected").val();
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


    //收集案件信息
    var cause=$("#cause").val();
    var casenum=$("#casenum").val();
    var occurrencetime=$("#occurrencetime").val();
    var caseway=$("#caseway").val();
    var starttime=$("#starttime").val();
    var endtime=$("#endtime").val();
    var department=$("#department").val();
    addPolice_case={
        casename:casename,
        cause:cause,
        casenum:casenum,
        occurrencetime:occurrencetime,
        starttime:starttime,
        endtime:endtime,
        caseway:caseway,
        department:department
    }


    //收集询问人二和对应工作单位等信息
    var otheruserinfoname=$("#otheruserinfos").val();
    var otherworkname=$("#otherworkname").val();


    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
            casessid:dqcasessid,
            adminssid:sessionadminssid,
            otheradminssid:dqotheruserinfossid,
            otherworkssid:dqotherworkssid,
            recordadminssid:recordadminssid,
            recordplace:recordplace,
            askobj:askobj,
            asknum:asknum,
            recordtypessid:recordtypessid,
            recordname:recordname,
            mtmodelssid:dqmodelssid,
            wordtemplatessid:dqwordssid,
            usertos:usertos,
            addUserInfo:addUserInfo,
            addPolice_case:addPolice_case,
            otheruserinfoname:otheruserinfoname,
            otherworkname:otherworkname,
            skipCheckbool:skipCheckbool,
            skipCheckCasebool:skipCheckCasebool,
            multifunctionbool:multifunctionbool,
        }
    };
  ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}
function callbackaddCaseToArraignment(data) {
    $("#startrecord_btn").attr("lay-filter","startrecord_btn");
    $("#startrecord_btn").removeAttr("disabled")
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;

            //控制跳转
            var multifunctionbool=data.multifunctionbool;//功能控制跳转

            if (isNotEmpty(recordssid)&&toUrltype==1&&isNotEmpty(multifunctionbool)){
                //跳转笔录制作
                var index = parent.layer.msg('开始进行笔录', {shade:[0.1,"#fff"],icon:6,time:500
                },function () {
                    if (multifunctionbool==1){
                        //跳转一键提讯
                        var toUrl=getActionURL(getactionid_manage().addCaseToUser_towaitconversation);
                        parent.location.href=toUrl+"?ssid="+recordssid;
                    } else if (multifunctionbool==2||multifunctionbool==3){
                        var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
                        if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                            setpageAction(INIT_CLIENT,nextparam.nextPageId);
                            var toUrl=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
                            parent.location.href=toUrl+"?ssid="+recordssid;
                        }
                    }
                });
            }else if(toUrltype==2){
                //跳转笔录查看列表:后期统一列表显示页面
                setpageAction(INIT_CLIENT, "client_web/police/record/addCaseToUser");
                var url = getActionURL(getactionid_manage().addCaseToUser_torecordIndex);
                parent.location.href = url
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
                var starttime=case_.starttime==null?"":case_.starttime;
                var casenum=case_.casenum==null?"":case_.casenum;
                var department=case_.department==null?"":case_.department;
               var userInfos=case_.userInfos;
                var USERHTNL="";
                if(null!=userInfos) {for (let i = 0; i < userInfos.length; i++) {const u = userInfos[i];USERHTNL += u.username + "、";} USERHTNL = (USERHTNL .substring(USERHTNL .length - 1) == '、') ? USERHTNL .substring(0, USERHTNL .length - 1) : USERHTNL ;}
                var  init_casehtml=" <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>案件嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+starttime+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>";
                var TABLE_HTML='<form class="layui-form layui-row" style="margin: 10px"><table class="layui-table" lay-even lay-skin="nob" style="table-layout: fixed;">'+init_casehtml+' <tbody id="case_html"></tbody></table></form>';
                parent.layer.open({
                    type:1,
                    title: '案件信息(案件正在<strong style="color: red">暂停</strong>中...)',
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

            }else if (null!=recordingbool&&recordingbool==true&&isNotEmpty(checkStartRecordVO)){
                //存在笔录正在进行中，跳转笔录列表，给出提示：建议他先结束制作中的
                    var msg=checkStartRecordVO.msg;
                    if (isNotEmpty(msg)){
                        var btn=['开始笔录',"查看审讯列表","取消"];
                        btn2= function(index) {
                            console.log("跳转笔录列表")
                            toUrltype=2;
                            skipCheckbool =1;
                            addCaseToArraignment();
                            parent.layer.close(index);
                        };
                        if (gnlist.indexOf(HK_O)!=-1){
                            btn=['开始谈话',"取消"];
                            btn2=function(index) {
                                parent.layer.close(index);
                            };
                        }
                        parent.layer.confirm("<span style='color:red'>"+msg+"</span>", {
                            btn:btn, //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                             btn1:function(index) {
                                 console.log("跳转笔录制作中");
                                 //保存
                                 skipCheckbool = 1;
                                 addCaseToArraignment();
                                 parent.layer.close(index);
                             },
                            btn2:btn2,
                             btn3: function(index) {
                                 parent.layer.close(index);
                             }
                         });
                    }
            }else {
                parent.layer.msg(data.message,{icon: 5});
            }
        }else {
            parent.layer.msg(data.message,{icon: 5});
        }
    }
}

//初始化页面
function init_form() {
    layer.confirm('所填信息将会清空，确定要重置吗？', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){

        dquserssid=null;//当前用户的ssid
        dqcasessid=null;//当前案件ssid
        cases=null;
        othercases=null;
        dqotheruserinfossid=null;//当前询问人(新增询问人回显)
        dqotherworkssid=null;//当前询问人对应的工作单位
        layui.use(['form','laydate'], function(){
            var form=layui.form;
            var laydate=layui.laydate;
            $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#modelssid'):not('#wordssid')").val("");
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

        layer.close(index);
    }, function(index){
        layer.close(index);
    });
}




//*************************************************用户相关*****************************************
/**
 * 获取人员信息
 */
function getUserByCard(){

    var cardnum =  $("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    var cardtypesssid=$("#cards option:selected").val();
    var nationality = $("#nationality option:selected").text();//国籍


    dquserssid=null;//当前用户的ssid
    dqcasessid=null;//当前案件ssid
    cases=null;
    othercases=null;
    var form=layui.form;
    $("#casename_ssid").html("");
    /* $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#asknum')").val("");not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
    $("#casename,#recordname").val("");
    $("#asknum").val(0);


    /* init_form();//初始化表单*/
    if (!isNotEmpty(cardnum)){
        return;
    }

    if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
        var bool=checkByIDCard(cardnum);
        if (!bool){
            return;
        }
        //回填身份证分析数据
        $("#both").val(getAnalysisIdCard(cardnum,1));
        $("#sex").val(getAnalysisIdCard(cardnum,2));
        $("#age").val(getAnalysisIdCard(cardnum,3));
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

                $("#professional").val(userinfo.professional);
                $("#phone").val(userinfo.phone);
                $("#domicile").val(userinfo.domicile);
                $("#residence").val(userinfo.residence);
                $("#workunits").val(userinfo.workunits);

                $("#national").val(userinfo.nationalssid);
                $("#nationality").val(userinfo.nationalityssid);
                $("#educationlevel").val(userinfo.educationlevel);
                $("#politicsstatus").val(userinfo.politicsstatus);

                $("#both").val(userinfo.both);
                $("#age").val(userinfo.age);
                $("#sex").val(userinfo.sex);
            }

            if (isNotEmpty(dquserssid)){
                getCaseById();
            }

            var cardnum =  $("#cardnum").val();
            var cardtypetext=$("#cards option:selected").text();
            var nationality = $("#nationality option:selected").text();//国籍
            if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
                //回填身份证分析数据
                $("#both").val(getAnalysisIdCard(cardnum,1));
                $("#sex").val(getAnalysisIdCard(cardnum,2));
                $("#age").val(getAnalysisIdCard(cardnum,3));
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
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
            if (c.casebool!=2){
                $("#casename_ssid").append("<dd lay-value='"+c.ssid+"' onmousedown='select_case(this);'>"+c.casename+"</dd>");
            }
        }

        if (isNotEmpty(dqcasessid)){
            $("#casename").val("");
            $("#cause").val("");
            $("#casenum").val("");
            $("#caseway").val("");
            $("#asknum").val("0");
            $("#recordname").val("");
            if (isNotEmpty(cases)){
                for (var i = 0; i < cases.length; i++) {
                    var c = cases[i];
                    if (dqcasessid==c.ssid){
                        $("#casename").val(c.casename);
                        var casename=$("#casename").find("option:selected").text();
                        var recordtypename=$("td[recordtypebool='true']",parent.document).text();
                        var username=$("#username").val();
                        var asknum=c.arraignments==null?0:c.arraignments.length;

                        var modelssidname=$("#modelssid").val();
                        var recordplace=$("#recordplace").val();
                        var cardnum=$("#cardnum").val();
                        var recordname="";
                        //获取类型为2的规则
                       if (isNotEmpty(dq_rule)){
                           recordname=dq_rule;
                           recordname=recordname.replace("${嫌疑人}",username);
                           recordname=recordname.replace("${证件号}",cardnum);
                           recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
                           recordname=recordname.replace("${案件名}",casename);
                           recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
                           recordname=recordname.replace("${地点}",recordplace);
                           recordname=recordname.replace("${17位时间}",gettimess());
                           recordname=recordname.replace("${办案场景}",modelssidname);
                       } else {
                           recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                       }

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
                        $("#asknum").val(asknum);
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
                                                                    <select name="tab_card"   disabled  lay-filter="tab_cardfilter">\
                                                                    <option value=""></option>\
                                                                    </select>\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">证件号码</label>\
                                                                <div class="layui-input-block">\
                                                                    <input type="text" name="tab_cardnum"   placeholder="" autocomplete="off" class="layui-input" onkeyup="value=value.replace(/[\\W]/g,\'\')">\
                                                                      <i class="layui-icon layui-icon-search" style="position: absolute;top:8px;right: 8px;" onclick="getUserByCard_other(this);"></i>\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">姓名</label>\
                                                                <div class="layui-input-block">\
                                                                    <input type="text" name="tab_username"  placeholder="" autocomplete="off" class="layui-input">\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg6">\
                                                                <label class="layui-form-label">性别</label>\
                                                                <div class="layui-input-block">\
                                                                    <select name="tab_sex" >\
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
                                                                    <input type="text" name="tab_phone"   placeholder="请输入手机号码" autocomplete="off" class="layui-input" onkeyup= "value=value.replace(/[^\\d]/g,\'\')">\
                                                                </div>\
                                                            </div>\
                                                            <div class="layui-col-lg12">\
                                                                  <input name="tab_otheruserssid" style="display: none;">\
                                                                   <button  class="layui-btn layui-btn-danger  layui-btn-sm"  onclick="tab_reset(this);return false;" style="float: right">重置</button>\
                                                            </div>\
                                                        </div>';

    layui.use(['element','form'], function(){
        var form = layui.form;
        var element=layui.element;
        //使用模块
        parent.layer.prompt({title: '请输入其他在场人员标题', formType: 0}, function(title, index){
            title   =   title.replace(/\s+/g,"");
            var ck=true;
            var ck_msg="";
            $("#tab_body #tab_content .layui-tab-item").each(function(){
                var usertitle=$(this).parent().parent().find(".layui-tab-title li").eq(index).attr("lay-id");
                if (title==usertitle) {
                    ck_msg="其他在场人员标题不允许重复";
                    ck=false;
                    return false;
                }
            })
            if(!ck){
                parent.layer.msg(ck_msg,{icon: 5});
                return;
            }
            element.tabAdd('tabAdd_filter', {
                title:title
                ,content:html,
                id:title
            });
            $("#tab_content select[name='tab_card']").html("");
            if (isNotEmpty(cards)){
                for (var i = 0; i < cards.length; i++) {
                    var l = cards[i];
                    $("#tab_content select[name='tab_card']").each(function(){
                        $(this).append("<option value='"+l.ssid+"' title='"+l.typename+"'> "+l.typename+"</option>");
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
        parent.layer.msg("请先获取人员基本信息",{icon:5});
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }
    if (dqcardssid==cards&&dqcardnum==cardnum){
        parent.layer.msg("被询问人不能作为其他在场人员",{icon:5});
        $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
        return;
    }

    if(isNotEmpty(cardnum)){
        //判断证件是否已经被搜索
        var num=0;
        $("#tab_content select[name='tab_card']").each(function(){
            var card_val=$(this).val();
            var card_num_=$(this).closest(".layui-tab-item").find("input[name='tab_cardnum']").val();
            if (card_val==cards&&card_num==card_num_){
                num++;
            }
        });
        if (num>1){
            parent.layer.msg("该在场人员已存在",{icon:5});
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
                parent.layer.msg(data.message,{icon: 5});
                $(obj).closest(".layui-tab-item").find("input[name='tab_cardnum']").val("");
            }
            layui.use('form', function(){
                var form = layui.form;
                form.render();
            });
        });
    }else {
        parent.layer.msg("请输入证件号码",{icon:5});
    }
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


function getCaseById() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getCaseById);
    var userssid=dquserssid;
    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:userssid,
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
                if (isNotEmpty(cases)){
                    setcases(cases);
                }
            }
        }
    }else{
        parent.layer.msg(data.message,{icon: 5});
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
        parent.layer.msg(data.message,{icon: 5});
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
    $(obj).focus();
    getUserByCard();
    $("#cardnum_ssid").html("");
}
function select_cardnumblur() {
    $("#cardnum_ssid").css("display","none");
    getUserByCard();
}

//*************************************************用户相关*****************************************


//*************************************************案件相关*****************************************
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
                    var asknum=c.arraignments==null?0:c.arraignments.length;
                    var recordtypename=$("td[recordtypebool='true']",parent.document).text();

                    var modelssidname=$("#modelssid").val();
                    var recordplace=$("#recordplace").val();
                    var cardnum=$("#cardnum").val();
                    var recordname="";
                    //获取类型为2的规则
                    if (isNotEmpty(dq_rule)){
                        recordname=dq_rule;
                        recordname=recordname.replace("${嫌疑人}",username);
                        recordname=recordname.replace("${证件号}",cardnum);
                        recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
                        recordname=recordname.replace("${案件名}",casename);
                        recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
                        recordname=recordname.replace("${地点}",recordplace);
                        recordname=recordname.replace("${17位时间}",gettimess());
                        recordname=recordname.replace("${办案场景}",modelssidname);
                    } else {
                        recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                    }

                    $("#cause").val(c.cause);
                    $("#casenum").val(c.casenum);
                    $("#caseway").val(c.caseway);
                    $("#asknum").val(asknum);
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
                var asknum=c.arraignments==null?0:c.arraignments.length;
                var modelssidname=$("#modelssid").val();
                var recordplace=$("#recordplace").val();
                var cardnum=$("#cardnum").val();
                var recordname="";
                //获取类型为2的规则
                if (isNotEmpty(dq_rule)){
                    recordname=dq_rule;
                    recordname=recordname.replace("${嫌疑人}",username);
                    recordname=recordname.replace("${证件号}",cardnum);
                    recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
                    recordname=recordname.replace("${案件名}",casename);
                    recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
                    recordname=recordname.replace("${地点}",recordplace);
                    recordname=recordname.replace("${17位时间}",gettimess());
                    recordname=recordname.replace("${办案场景}",modelssidname);

                } else {
                    recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                }

                $("#cause").val(c.cause);
                $("#casenum").val(c.casenum);
                $("#caseway").val(c.caseway);
                $("#asknum").val(asknum);
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

    if (!isNotEmpty(dqcasessid)&&isNotEmpty(othercases)) {
        for (var i = 0; i < othercases.length; i++) {
            var c = othercases[i];
            if (c.casename.trim()==casename.trim()) {
                dqcasessid=c.ssid;
                var asknum=c.arraignments==null?0:c.arraignments.length;
                var modelssidname=$("#modelssid").val();
                var recordplace=$("#recordplace").val();
                var cardnum=$("#cardnum").val();
                var recordname="";
                //获取类型为2的规则
                if (isNotEmpty(dq_rule)){
                    recordname=dq_rule;
                    recordname=recordname.replace("${嫌疑人}",username);
                    recordname=recordname.replace("${证件号}",cardnum);
                    recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
                    recordname=recordname.replace("${案件名}",casename);
                    recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
                    recordname=recordname.replace("${地点}",recordplace);
                    recordname=recordname.replace("${17位时间}",gettimess());
                    recordname=recordname.replace("${办案场景}",modelssidname);

                } else {
                    recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                }

                $("#cause").val(c.cause);
                $("#casenum").val(c.casenum);
                $("#caseway").val(c.caseway);
                $("#asknum").val(asknum);
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
        var modelssidname=$("#modelssid").val();
        var recordplace=$("#recordplace").val();
        var cardnum=$("#cardnum").val();
        var recordname="";
        //获取类型为2的规则
        if (isNotEmpty(dq_rule)){
            recordname=dq_rule;
            recordname=recordname.replace("${嫌疑人}",username);
            recordname=recordname.replace("${证件号}",cardnum);
            recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
            recordname=recordname.replace("${案件名}",casename);
            recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
            recordname=recordname.replace("${地点}",recordplace);
            recordname=recordname.replace("${17位时间}",gettimess());
            recordname=recordname.replace("${办案场景}",modelssidname);

        } else {
            recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
        }
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
    $(obj).focus();
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
    $(obj).focus();
    $("#caseway_text").html("");
}
function select_casewayblur() {
    $("#caseway_text").css("display","none");
}

//询问对象
function getAskObjList() {
    $("#askobj_text").html("");
    var askobjlike=[];
    var askobj = $("#askobj").val();
    var askobjList=["被害人","证人"];
    if (isNotEmpty(askobjList)){
        for (var i = 0; i < askobjList.length; i++) {
            var a = askobjList[i];
            if (a.indexOf(askobj) >= 0) {
                askobjlike.push(a);
            }
        }
        if (isNotEmpty(askobjlike)){
            for (var j = 0; j < askobjlike.length; j++) {
                var al=askobjlike[j];
                $("#askobj_text").append("<dd lay-value='"+al+"' onmousedown='select_askobj(this);'>"+al+"</dd>");
            }
        }
        $("#askobj_text").css("display","block");
    }
}
function select_askobj(obj) {
    $("#askobj_text").css("display","none");
    var askobj=$(obj).attr("lay-value");
    $("#askobj").val(askobj);
    $(obj).focus();
    $("#askobj_text").html("");
}
function select_askobjblur() {
    $("#askobj_text").css("display","none");
}

//全部的工作单位
function getOtherworknameList() {
    $("#otherworkname_text").html("");
    dqotherworkssid=null;
    var otherworknamelike=[];
    var otherworkname = $("#otherworkname").val();

    otherworkname = otherworkname.replace(/\s*/g,"");


    if (isNotEmpty(workunits)){
        for (var i = 0; i < workunits.length; i++) {
            var w = workunits[i];
            if (w.workname.indexOf(otherworkname) >= 0) {
                otherworknamelike.push(w);
            }
            if (w.workname==otherworkname) {
                dqotherworkssid=w.ssid;
            }
        }
        if (isNotEmpty(otherworknamelike)){
            for (let i = 0; i < otherworknamelike.length; i++) {
                const l = otherworknamelike[i];
                $("#otherworkname_text").append("<dd lay-value='"+l.ssid+"' onmousedown='select_otherworkname(this);'>"+l.workname+"</dd>");
            }
        }
        $("#otherworkname_text").css("display","block");
    }
}
function select_otherworkname(obj) {
    $("#otherworkname_text").css("display","none");
    var otherworkssid=$(obj).attr("lay-value");
    dqotherworkssid=otherworkssid;
    $("#otherworkname").val($(obj).text());
    $(obj).focus();
    $("#otherworkname_text").html("");
}
function select_otherworknameblur() {
    $("#otherworkname_text").css("display","none");
}


//询问人2
function getOtheruserinfosList() {
    $("#otheruserinfos_text").html("");
    dqotheruserinfossid=null;
    var otheruserinfoslike=[];
    var otheruserinfosval = $("#otheruserinfos").val();
    otheruserinfosval = otheruserinfosval.replace(/\s*/g,"");
    if (isNotEmpty(otheruserinfos)){
        for (var i = 0; i < otheruserinfos.length; i++) {
            var o = otheruserinfos[i];
            if (o.username.indexOf(otheruserinfosval) >= 0) {
                otheruserinfoslike.push(o);
            }
            if (otheruserinfosval==o.username&&o.ssid!=sessionadminssid){
                dqotheruserinfossid=o.ssid;
            }
        }
        if (isNotEmpty(otheruserinfoslike)){
            for (var j = 0; j < otheruserinfoslike.length; j++) {
                var ol=otheruserinfoslike[j];
                if (ol.ssid!=sessionadminssid) {
                    $("#otheruserinfos_text").append("<dd lay-value='"+ol.ssid+"' onmousedown='select_otheruserinfos(this);'>"+ol.username+"</dd>");
                }
            }
        }
        $("#otheruserinfos_text").css("display","block");
    }
}
function select_otheruserinfos(obj) {
    $("#otheruserinfos_text").css("display","none");
    $("#otheruserinfos").val($(obj).text());
    $(obj).focus();
    $("#otheruserinfos_text").html("");
    dqotheruserinfossid=$(obj).attr("lay-value");
    for (var i = 0; i < otheruserinfos.length; i++) {
        var u = otheruserinfos[i];
        if (dqotheruserinfossid==u.ssid){
            $("#otherworkname").val(u.workname);
            dqotherworkssid=u.workunitssid;
        }
    }
}
function select_otheruserinfosblur() {
    $("#otheruserinfos_text").css("display","none");
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
                     <tbody id="othercases_html"  >';
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
                $("#cause").val("");
                $("#casenum").val("");
                $("#caseway").val("");
                $("#asknum").val("0");
                $("#recordname").val("");

                for (var i = 0; i < othercases.length; i++) {
                    var c = othercases[i];
                    if (dqcasessid==c.ssid){
                        var username=$("#username").val();
                        var casename=c.casename;
                        var asknum=c.arraignments==null?0:c.arraignments.length;
                        var recordtypename=$("td[recordtypebool='true']",parent.document).text();

                        var modelssidname=$("#modelssid").val();
                        var recordplace=$("#recordplace").val();
                        var cardnum=$("#cardnum").val();
                        var recordname="";
                        //获取类型为2的规则
                        if (isNotEmpty(dq_rule)){
                            recordname=dq_rule;
                            recordname=recordname.replace("${嫌疑人}",username);
                            recordname=recordname.replace("${证件号}",cardnum);
                            recordname=recordname.replace("${类型}",recordtypename.replace(/\s+/g, ""));
                            recordname=recordname.replace("${案件名}",casename);
                            recordname=recordname.replace("${询问次数}",parseInt(asknum)+1);
                            recordname=recordname.replace("${地点}",recordplace);
                            recordname=recordname.replace("${17位时间}",gettimess());
                            recordname=recordname.replace("${办案场景}",modelssidname);

                        } else {
                            recordname=""+username+"《"+casename.trim()+"》"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                        }

                        $("#casename").val(c.casename);
                        $("#cause").val(c.cause);
                        $("#casenum").val(c.casenum);
                        $("#caseway").val(c.caseway);
                        $("#asknum").val(asknum);
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
//*************************************************案件相关*****************************************


//*************************************************模板相关*****************************************
//获取会议模板
function getMc_model(){
    var url=getUrl_manage().getMc_model;
    var data={
        token:INIT_CLIENTKEY,
        param:{
        }
    };
    ajaxSubmitByJson(url, data, callbackgetMc_model);
}
function callbackgetMc_model(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            modelList=data;
            if (isNotEmpty(modelList)&&isNotEmpty(model_index)){
                $("#modelList").html("");
                for (let i = 0; i < modelList.length; i++) {
                    var model = modelList[i];
                    var meetingtypehtml=model.meetingtype==null?"":(model.meetingtype==1?"<i class='layui-icon layui-icon-video'></i>视频":(model.meetingtype==2?"<i class='layui-icon layui-icon-headset'></i>音频":"未知"));
                    var userecordhtml=model.userecord==null?"":(model.userecord==1?"录制":(model.userecord==-1?"不录制":"未知"));
                    var html="<tr>\
                                       <td>"+(i+1)+"</td>\
                                       <td>"+meetingtypehtml+"</td>\
                                       <td>"+model.explain+"</td>\
                                       <td>"+model.usernum+"</td>\
                                       <td>"+userecordhtml+"</td>\
                                      <td style='padding-bottom: 0;'>\
                                          <div class='layui-btn-container'>\
                                          <button  class='layui-btn layui-btn-warm layui-btn-sm' onclick='select_Model(\""+model.ssid+"\",\""+model.explain+"\")'>选定</button>\
                                          </div>\
                                          </td>\
                                 </tr>";
                    $("#modelList").append(html);
                }
            }

            if (isNotEmpty(dqmodelssid)&&isNotEmpty(modelList)){
                for (let i = 0; i < modelList.length; i++) {
                    var model = modelList[i];
                    if (dqmodelssid==model.ssid){
                        $("#modelssid").val(model.explain);
                    }
                }
            }
        }
    }else {
        console.log("获取会议模板"+data.message)
    }
}
function select_Model(ssid,explain){
    if (isNotEmpty(ssid)){
        dqmodelssid=ssid;
        $("#modelssid").val(explain);
        layer.close(model_index);
    }
}
function open_model(){
    var html= '<table class="layui-table"  lay-skin="nob" style="margin-bottom: 30px;">\
       <thead>\
        <tr>\
        <th>序号</th>\
        <th>会议类型</th>\
        <th>模板名称</th>\
        <th>人员数量</th>\
        <th>是否录制</th>\
        <th>操作</th>\
        </tr>\
        </thead>\
        <tbody id="modelList">\
        </tbody>\
        </table>';
    model_index= layer.open({
        type:1,
        title: '选择场景模板',
        shade: 0.3,
        resize:false,
        area: ['800px', '500px'],
        content: html,
        success: function(layero,index){
            getMc_model();
        }
    });
}
//获取默认会议模板
function getDefaultMtModelssid(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getDefaultMtModelssid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
        }
    };
    ajaxSubmitByJson(url,data,callbackgetDefaultMtModelssid);
}
function callbackgetDefaultMtModelssid(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            dqmodelssid=data;
        }
    }else {
        console.log("获取会议默认模板ssid"+data.message)
    }
}

//获取全部笔录模板以及默认笔录模板
function select_Model2(ssid,wordname){
    if (isNotEmpty(ssid)){
        dqwordssid=ssid;
        $("#wordssid").val(wordname);
        layer.close(model_index2);
    }
}
var model_index2=null;
function open_model2() {
    var html= '<table class="layui-table"  lay-skin="nob" style="margin-bottom: 30px;">\
       <thead>\
        <tr>\
        <th>序号</th>\
        <th>模板名称</th>\
        <th>创建时间</th>\
        <th>操作</th>\
        </tr>\
        </thead>\
        <tbody id="wordList">\
        </tbody>\
        </table>';
    model_index2= layer.open({
        type:1,
        title: '选中笔录模板',
        shade: 0.3,
        resize:false,
        area: ['800px', '500px'],
        content: html,
        success: function(layero,index){
            if (isNotEmpty(wordList)){
                $("#wordList").html("");
                for (let i = 0; i < wordList.length; i++) {
                    var word = wordList[i];
                    var html="<tr>\
                                       <td>"+(i+1)+"</td>\
                                       <td>"+word.wordtemplatename+"</td>\
                                       <td>"+word.createtime+"</td>\
                                       <td style='padding-bottom: 0;'>\
                                          <div class='layui-btn-container'>\
                                            <button  class='layui-btn layui-btn-warm layui-btn-sm' onclick='select_Model2(\""+word.ssid+"\",\""+word.wordtemplatename+"\")'>选定</button>\
                                          </div>\
                                          </td>\
                                 </tr>";
                    $("#wordList").append(html);
                }
            }
        }
    });
}
function getWordTemplates(){
    var url=getActionURL(getactionid_manage().addCaseToUser_getWordTemplates);
    var data={
        token:INIT_CLIENTKEY,
        param:{
        }
    };
    ajaxSubmitByJson(url,data,callbackgetWordTemplates);
}
function callbackgetWordTemplates(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var wordTemplates=data.wordTemplates;
            var default_word=data.default_word;
            if (isNotEmpty(wordTemplates)) {
                wordList=wordTemplates;

                var defaultnum=0;//默认模板数量
                var default_wordTemplate=null;
                for (let i = 0; i < wordTemplates.length; i++) {
                    const wordTemplate = wordTemplates[i];
                    if (wordTemplate.defaultbool==1){
                        defaultnum++;
                        dqwordssid=wordTemplate.ssid;
                        $("#wordssid").val(wordTemplate.wordtemplatename);
                    }
                    if (wordTemplate.ssid==default_word&&isNotEmpty(default_word)) {
                        default_wordTemplate=wordTemplate;
                    }
                }
                if (defaultnum==0&&isNotEmpty(default_wordTemplate)){
                    dqwordssid=default_wordTemplate.ssid;
                    $("#wordssid").val(default_wordTemplate.wordtemplatename);
                }
            }
        }
    }else {
        console.log("获取会议默认模板ssid"+data.message)
    }
}
//*************************************************模板相关*****************************************
function gettimess(){
    var now=new Date();
    var time="";
    time=now.getFullYear()+""+(now.getMonth() + 1)+""+now.getDate()+""+now.getHours()+""+now.getMinutes()+""+now.getSeconds()+""+now.getMilliseconds();
    return  time;
}

$(function () {

    $("#getCardreader_btn").click(function () {
        getIDCardreader();
    });
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;

        form.on('select(change_card)', function(data){
            getUserByCard();
            form.render('select');
        });

        form.on('switch(multifunctionbool_filter)', function(switchdata){
            var obj=switchdata.elem.checked;
            if (obj){
                $("#ifranmehtml").contents().find("#multifunctionbool_showorhide").hide();
            } else {
                $("#ifranmehtml").contents().find("#multifunctionbool_showorhide").show();
            }
        });
    });


});


//重置按钮
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

