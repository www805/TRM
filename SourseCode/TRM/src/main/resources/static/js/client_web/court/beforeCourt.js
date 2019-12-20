var dquserssid=null;//当前被告人ssid
var dqcasessid=null;//当前案件ssid
var dqmodelssid=null;//当前所选的会议模板ssid
var dqwordssid=null;//当前笔录模板ssid

var cases=null;//我的全部案件数据
var othercases=null;//除开自己全部的案件信息


var skipCheckbool=-1;//是否跳过检测：默认-1
var skipCheckCasebool=-1;//是否跳过案件检查(法庭)
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表

//当前用户类型 1原告 2被告  3被代理人 8原代理人
var dq_userinfograde=USERINFOGRADE2;//默认为被告



var userinfogrades={}

var casenum_userinfos=[];//根据案件编号回填的多角色人员
var casenum_case;////根据案件编号回填的案件ssid


//开始笔录按钮===================================================================start
function addCaseToArraignment() {
    var url=getActionURL(getactionid_manage().beforeCourt_addCaseToArraignment);
    var recordtypessid= $("#recotdtypes").val();
    if (!isNotEmpty(recordtypessid)){
        layer.msg("请选择案件类型",{icon: 5});
        return;
    }

    var  username=$("#username").val();
    if (!isNotEmpty(username)){
        layer.msg("被告不能为空",{icon: 5});
        return;
    }

    //收集案件信息
    var  casename=$("#casename").val();
    if (!isNotEmpty(casename)){
        layer.msg("案件名称不能为空",{icon: 5});
        return;
    }
    var cause=$("#cause").val();
    var casenum=$("#casenum").val();
    var starttime=$("#starttime").val();
    var addPolice_case={
        cause:cause,
        casenum:casenum,
        starttime:starttime,
        casename:casename
    }

    var asknum=$("#asknum").val();
    var recordplace=$("#recordplace").val();


    //收集当前显示人员的人员信息
    var userinfogradessid=dq_userinfograde;
    var oldcardnum=$("#cardnum").val();
    var oldusernamelist=$("input[name="+userinfogradessid+"]").val();
    var olduserinfo=null;
    //只有身份证号码|\姓名不为空时保存
    if (isNotEmpty(oldusernamelist)||isNotEmpty(oldcardnum)){
        var oldusername=null;
        if (isNotEmpty(oldusernamelist)) {
            var oldusername=oldusernamelist.split("；");
            oldusername = oldusername.filter(function (x) { return x && x.trim() });
            if (oldusername.length>0) { oldusername=oldusername[0];}
        }

        var olduserinfo=userinfogrades[""+userinfogradessid+""];
        if (!isNotEmpty(olduserinfo)){
            olduserinfo={};
        }
        olduserinfo["cardnum"]=oldcardnum;
        olduserinfo["username"]=oldusername;
        olduserinfo["age"]=$("#age").val();
        olduserinfo["sex"]=$("#sex").val();
        olduserinfo["both"]=$("#both").val();
        olduserinfo["nationalssid"]=$("#national").val();
        olduserinfo["educationlevel"]=$("#educationlevel").val();
        olduserinfo["phone"]=$("#phone").val();
        olduserinfo["domicile"]=$("#domicile").val();
        olduserinfo["residence"]=$("#residence").val();
        olduserinfo["workunits"]=$("#workunits").val();
        olduserinfo["issuingauthority"]=$("#issuingauthority").val();
        olduserinfo["validity"]=$("#validity").val();
        olduserinfo["userinfogradessid"]=userinfogradessid;
    }
    userinfogrades[""+userinfogradessid+""]=olduserinfo;
    if(!isNotEmpty(oldcardnum)){
        //去掉回显身份证验证
        $("#age").val("");
        $("#sex").prop('selectedIndex', 0);
        $("#both").val("");
    }

    /**
     * 收集外部人员
     * @type {Array}
     */
    var arraignmentexpand=[];
    if (isNotEmpty(userinfogrades[""+USERINFOGRADE2+""])){
        //被告
        var bool=checkuserinfograde(USERINFOGRADE2);
        if (!bool){
            return;
        }
        arraignmentexpand.push(userinfogrades[""+USERINFOGRADE2+""]);
        var otherusers=setusers(USERINFOGRADE2);
        if (isNotEmpty(otherusers)) {
            arraignmentexpand=arraignmentexpand.concat(otherusers);
        }
    }


    if (isNotEmpty(userinfogrades[""+USERINFOGRADE1+""])){
        //原告
        var bool=checkuserinfograde(USERINFOGRADE1);
        if (!bool){
            return;
        }
        arraignmentexpand.push(userinfogrades[""+USERINFOGRADE1+""]);
        var otherusers=setusers(USERINFOGRADE1);
        if (isNotEmpty(otherusers)) {
            arraignmentexpand=arraignmentexpand.concat(otherusers);
        }
    }

    if (isNotEmpty(userinfogrades[""+USERINFOGRADE3+""])){
        //被告代理人
        var bool=checkuserinfograde(USERINFOGRADE3);
        if (!bool){
            return;
        }
        arraignmentexpand.push(userinfogrades[""+USERINFOGRADE3+""]);
        var otherusers=setusers(USERINFOGRADE3);
        if (isNotEmpty(otherusers)) {
            arraignmentexpand=arraignmentexpand.concat(otherusers);
        }
    }

    if (isNotEmpty(userinfogrades[""+USERINFOGRADE8+""])){
        //原告代理人
        var bool=checkuserinfograde(USERINFOGRADE8);
        if (!bool){
            return;
        }
        arraignmentexpand.push(userinfogrades[""+USERINFOGRADE8+""]);
        var otherusers=setusers(USERINFOGRADE8);
        if (isNotEmpty(otherusers)) {
            arraignmentexpand=arraignmentexpand.concat(otherusers);
        }
    }



    //检测是否重复
    if (isNotEmpty(arraignmentexpand)){
        var newarraignmentexpand = [];//新数组
        for(var i=0;i<arraignmentexpand.length;i++){
            var bool=true;
            for(var j=0;j<newarraignmentexpand.length;j++){
                if((isNotEmpty(arraignmentexpand[i].cardnum)&&isNotEmpty(newarraignmentexpand[j].cardnum)&& arraignmentexpand[i].cardnum==newarraignmentexpand[j].cardnum)){
                    bool=false;
                    layer.msg("用户身份证号码不能重复使用",{icon: 5});
                    return;
                }/*else if (isNotEmpty(arraignmentexpand[i].username)&&isNotEmpty(newarraignmentexpand[j].username)&&arraignmentexpand[i].username==newarraignmentexpand[j].username){//先只判断名称不能重复  &&(!isNotEmpty(arraignmentexpand[i].cardnum)&&!isNotEmpty(newarraignmentexpand[j].cardnum))
                    bool=false;
                    layer.msg("用户名称不能重复使用",{icon: 5});
                    return;
                }*/
            };
            if (bool){
                newarraignmentexpand.push(arraignmentexpand[i]);
            }
        };
    }


    /**
     * 收集内部人员------------------------
     * @type {Array}
     */
    var arrUserExpandParams=[];
    var otheradminssid=$("#otheradminssid").val();
   if (isNotEmpty(otheradminssid)) {
       //陪审员
       arrUserExpandParams.push({
           userssid:otheradminssid,
           userinfogradessid:USERINFOGRADE6,
       });
   }
    var recordadminssid=$("#recordadminssid").val();
    if (isNotEmpty(otheradminssid)) {
        //书记员
        arrUserExpandParams.push({
            userssid:recordadminssid,
            userinfogradessid:USERINFOGRADE5,
        });
    }
    var judges=$("#judges").val();
    if (isNotEmpty(judges)) {
        //审判员
        arrUserExpandParams.push({
            userssid:judges,
            userinfogradessid:USERINFOGRADE7,
        });
    }
    var presidingjudge=$("#presidingjudge").val();
    if (isNotEmpty(presidingjudge)) {
        //审判长
        arrUserExpandParams.push({
            userssid:presidingjudge,
            userinfogradessid:USERINFOGRADE4,
        });
    }else {
        layer.msg("审判长不能为空",{icon: 5});
        return;
    }




    var mtmodelssidname=$("#modelssid").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
            casessid:dqcasessid,
            adminssid:presidingjudge,//审判长作为主要询问人
            otheradminssid:otheradminssid,
            recordplace:recordplace,
            recordadminssid:recordadminssid,
            asknum:asknum,
            recordtypessid:recordtypessid,
            mtmodelssid:dqmodelssid,
            mtmodelssidname:mtmodelssidname,//模板名称
            wordtemplatessid:dqwordssid,
            addPolice_case:addPolice_case,
            addUserInfo:userinfogrades[""+USERINFOGRADE2+""],//被询问人信息：被告
            skipCheckbool:skipCheckbool,
            skipCheckCasebool:skipCheckCasebool,
            multifunctionbool:3,//庭审默认多功能
            arraignmentexpand:arraignmentexpand,
            arrUserExpandParams:arrUserExpandParams
        }
    };
    $("#startrecord_btn").attr("lay-filter","");
    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}
function callbackaddCaseToArraignment(data) {
    $("#startrecord_btn").attr("lay-filter","startrecord_btn");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;
            //控制跳转
            var multifunctionbool=data.multifunctionbool;//功能控制跳转
            if (isNotEmpty(recordssid)&&toUrltype==1&&isNotEmpty(multifunctionbool)){
                //跳转笔录制作
                var index = layer.msg('开始进行笔录', {shade:[0.1,"#fff"],icon:6,time:500
                },function () {
                    if (multifunctionbool==1){
                    } else if (multifunctionbool==2||multifunctionbool==3){
                        var toUrl=getActionURL(getactionid_manage().beforeCourt_towaitCourt);
                        location.href=toUrl+"?ssid="+recordssid;
                    }
                });
            }else if(toUrltype==2){
                //跳转笔录查看列表:后期统一列表显示页面
                var url = getActionURL(getactionid_manage().beforeCourt_torecordIndex);
                location.href = url
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
                layer.open({
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
                        layer.close(index);
                    },
                    btn2: function(index) {
                        layer.close(index);
                    }
                });

            }else if (null!=recordingbool&&recordingbool==true&&isNotEmpty(checkStartRecordVO)){
                //存在笔录正在进行中，跳转笔录列表，给出提示：建议他先结束制作中的
                var msg=checkStartRecordVO.msg;
                if (isNotEmpty(msg)){
                    layer.confirm("<span style='color:red'>"+msg+"</span>", {
                        btn: ['开始笔录',"查看笔录列表","取消"], //按钮
                        shade: [0.1,'#fff'], //不显示遮罩
                        btn1:function(index) {
                            console.log("跳转笔录制作中");
                            //保存
                            skipCheckbool = 1;
                            addCaseToArraignment();
                            layer.close(index);
                        },
                        btn2: function(index) {
                            console.log("跳转笔录列表")
                            toUrltype=2;
                            skipCheckbool =1;
                            addCaseToArraignment();
                            layer.close(index);
                        },
                        btn3: function(index) {
                            layer.close(index);
                        }
                    });
                }
            }else {
                layer.msg(data.message,{icon: 5});
            }
        }else {
            layer.msg(data.message,{icon: 5});
        }
    }
}
//开始笔录按钮===================================================================end



//用户===================================================================start
function getUserinfoList() {
    var url=getActionURL(getactionid_manage().beforeCourt_getUserinfoList);
    var cardnum=$("#cardnum").val();
    if (!isNotEmpty(cardnum)) {
        $("#cardnum_ssid").html("");
        $("#cardnum_ssid").append('<p class="layui-select-none">无匹配项</p>');
        return;
    }
    //默认卡类型使用身份证
    var data={
        token:INIT_CLIENTKEY,
        param:{
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
        layer.msg(data.message,{icon: 5});
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
    var cardnum=$("#cardnum").val();
    if (isNotEmpty(cardnum)) {
        var bool=checkByIDCard(cardnum);
       /* if (!bool){
            layer.msg("请输入有效的居民身份证号码",{icon: 5});
            return false;
        }*/
        getUserByCard();
        if (bool){
            $("#both").val(getAnalysisIdCard(cardnum,1));
            $("#sex").val(getAnalysisIdCard(cardnum,2));
            $("#age").val(getAnalysisIdCard(cardnum,3));
        }
    }
}




//获取人员信息：当前点击的usertype 当前dq_usertype
function getUserByCard(usertype){
    var cardnum =null;
    if (isNotEmpty(usertype)) {

        //收集切换前的用户信息
        var userinfogradessid=dq_userinfograde;
        var oldcardnum=$("#cardnum").val();
        var oldusernamelist=$("input[name="+userinfogradessid+"]").val();
        var olduserinfo=null;
        //只有身份证号码或者姓名不为空时保存
        if (isNotEmpty(oldusernamelist)||isNotEmpty(oldcardnum)) {
            var oldusername = null;
            if (isNotEmpty(oldusernamelist)) {
                var oldusername = oldusernamelist.split("；");
                oldusername = oldusername.filter(function (x) {
                    return x && x.trim()
                });
                if (oldusername.length > 0) {
                    oldusername = oldusername[0];
                }
            }
            var olduserinfo=userinfogrades[""+userinfogradessid+""];
            if (!isNotEmpty(olduserinfo)){
                olduserinfo={};
            }
            olduserinfo["cardnum"]=oldcardnum;
            olduserinfo["username"]=oldusername;
            olduserinfo["age"]=$("#age").val();
            olduserinfo["sex"]=$("#sex").val();
            olduserinfo["both"]=$("#both").val();
            olduserinfo["nationalssid"]=$("#national").val();
            olduserinfo["educationlevel"]=$("#educationlevel").val();
            olduserinfo["phone"]=$("#phone").val();
            olduserinfo["domicile"]=$("#domicile").val();
            olduserinfo["residence"]=$("#residence").val();
            olduserinfo["workunits"]=$("#workunits").val();
            olduserinfo["issuingauthority"]=$("#issuingauthority").val();
            olduserinfo["validity"]=$("#validity").val();
            olduserinfo["userinfogradessid"]=userinfogradessid;
        }
        userinfogrades[""+userinfogradessid+""]=olduserinfo;

        dq_userinfograde=usertype;
        if (dq_userinfograde==USERINFOGRADE1){
            $("#usertype").html("<span style='color: red;font-weight: bold'>原告</span>个人信息")
        } else if (dq_userinfograde==USERINFOGRADE2){
            $("#usertype").html("<span style='color: red;font-weight: bold'>被告</span>个人信息");
               /* dquserssid=null;
                dqcasessid=null;//当前案件ssid
                cases=null;
                othercases=null;
                $("#asknum").val(0);*/
        }else if (dq_userinfograde==USERINFOGRADE3){
            $("#usertype").html("<span style='color: red;font-weight: bold'>被告诉讼代理人</span>个人信息")
        }else if (dq_userinfograde==USERINFOGRADE8){
            $("#usertype").html("<span style='color: red;font-weight: bold'>原告诉讼代理人</span>个人信息")
        }
        //判断是否存在回显
        if (isNotEmpty(userinfogrades[""+dq_userinfograde+""])){
            setuserval(dq_userinfograde);
            return;
        }
    }else {
        cardnum =  $("#cardnum").val();
    }

   /* if (dq_userinfograde==USERINFOGRADE2){
        dquserssid=null;
        dqcasessid=null;//当前案件ssid
        cases=null;
        othercases=null;
        $("#asknum").val(0);
    }*/
    if (!isNotEmpty(cardnum)){
        $("#user input").val("");
        $('#user  select').prop('selectedIndex', 0);
        console.log("未输入身份证号码");
        return;
    }



    var url=getActionURL(getactionid_manage().beforeCourt_getUserByCard);
    //默认卡类型使用身份证
    var data={
        token:INIT_CLIENTKEY,
        param:{
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
                //用户名称
                var inputname=dq_userinfograde;
                var usernamelist=$("input[name="+inputname+"]").val();
                if (isNotEmpty(usernamelist)){
                    usernamelist=usernamelist.split("；");
                    usernamelist = usernamelist.filter(function (x) {
                        return x && x.trim()
                    });
                    if (usernamelist.length>0) {
                        usernamelist[0] = userinfo.username;
                    }
                    usernamelist=usernamelist.join("；");
                    $("input[name="+inputname+"]").val(usernamelist);
                } else {
                    $("input[name="+inputname+"]").val(userinfo.username);
                }


                /*人员信息*/
                $("#cardnum").val(userinfo.cardnum);
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
                $("#issuingauthority").val(userinfo.issuingauthority);
                $("#validity").val(userinfo.validity);

                if (dq_userinfograde==USERINFOGRADE2){//被告时候才需要更换
                    dquserssid=userinfo.ssid;
                    getCaseById();
                }
                userinfogrades[""+dq_userinfograde+""]=userinfo;//需要更新信息
            }
        }
    }else{
        console.log(data.message)
    }
    layui.use('form', function(){
        var $ = layui.$;
        var form = layui.form;
        form.render();
    });
}


//获取全部管理员
function getAdminList() {
    var url=getActionURL(getactionid_manage().beforeCourt_getAdminList);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetAdminList);
}
function callbackgetAdminList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        otheruserinfos=data.data;

        $('#otheradminssid option').not(":lt(1)").remove();
        $('#recordadminssid option').not(":lt(1)").remove();
        $('#presidingjudge option').not(":lt(1)").remove();
        $('#judges option').not(":lt(1)").remove();
        if (isNotEmpty(otheruserinfos)){
            for (var i = 0; i < otheruserinfos.length; i++) {
                var u= otheruserinfos[i];
                    $("#otheradminssid").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    //书记员默认选中登陆人员
                    if (isNotEmpty(sessionadminssid)&&isNotEmpty(u.ssid)&&sessionadminssid==u.ssid) {
                        $("#recordadminssid").append("<option value='"+u.ssid+"' selected >"+u.username+"</option>");
                    }else {
                        $("#recordadminssid").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    }
                    $("#presidingjudge").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    $("#judges").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
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
//用户===================================================================end



//案件===================================================================start
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
        if (isNotEmpty(cases)){
            for (var i = 0; i < cases.length; i++) {
                var c = cases[i];
                if (dqcasessid==c.ssid){
                    var username=$("#username").val();
                    var casename=$("#casename").val();
                    var asknum=c.arraignments==null?0:c.arraignments.length;
                    var recordtypename=$("td[recordtypebool='true']",document).text();
                    var modelssidname=$("#modelssid").val();
                    var recordname=""+username+"《"+casename.trim()+"》"+""+modelssidname+"_"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";

                    $("#cause").val(c.cause);
                    $("#casenum").val(c.casenum==null?"":c.casenum);
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
    var recordtypename=$("td[recordtypebool='true']",document).text();
    var username=$("#username").val();
    var modelssidname=$("#modelssid").val();
    //需要验证案件ssid
  /*  dqcasessid=null;*/
    if (isNotEmpty(cases)){
        for (var i = 0; i < cases.length; i++) {
            var c = cases[i];
            if (c.casename.trim()==casename.trim()) {
                dqcasessid=c.ssid;
                var asknum=c.arraignments==null?0:c.arraignments.length;
                var recordname=""+username+"《"+casename.trim()+"》"+""+modelssidname+"_"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                $("#cause").val(c.cause);
                $("#casenum").val(c.casenum==null?"":c.casenum);
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
                var recordname=""+username+"《"+casename.trim()+"》"+""+modelssidname+"_"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                $("#cause").val(c.cause);
                $("#casenum").val(c.casenum==null?"":c.casenum);
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
}
function open_othercases() {
    if (!isNotEmpty(othercases)){
        layer.msg("暂未找到其他案件",{icon:5});
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
    layer.open({
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
                        var recordtypename=$("td[recordtypebool='true']",document).text();
                        var modelssidname=$("#modelssid").val();
                        var recordname=""+username+"《"+casename.trim()+"》"+""+modelssidname+"_"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";

                        $("#casename").val(c.casename);
                        $("#cause").val(c.cause);
                        $("#casenum").val(c.casenum==null?"":c.casenum);
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
            layer.close(index);
        },
        btn2: function(index) {
            dqothercasessid=null;
            layer.close(index);
        }
    });

    $("#othercases_html tr",document).click(function () {
        $(this).css({"background-color":" #f2f2f2"}).siblings().css({"background-color":" #fff"});
        dqothercasessid= $(this).attr("ssid");
    });
}
//获取案件
function getCaseById() {
        var url=getActionURL(getactionid_manage().beforeCourt_getCaseById);
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
                            $("#asknum").val("0");
                            if (isNotEmpty(cases)){
                                for (var i = 0; i < cases.length; i++) {
                                    var c = cases[i];
                                    if (dqcasessid==c.ssid){
                                        $("#casename").val(c.casename);
                                        var casename=$("#casename").find("option:selected").text();
                                        var recordtypename=$("td[recordtypebool='true']",document).text();
                                        var username=$("#username").val();
                                        var asknum=c.arraignments==null?0:c.arraignments.length;
                                        var modelssidname=$("#modelssid").val();
                                        var recordname=""+username+"《"+casename.trim()+"》"+""+modelssidname+"_"+recordtypename.replace(/\s+/g, "")+"_第"+(parseInt(asknum)+1)+"次";
                                        $("#cause").val(c.cause);
                                        $("#casenum").val(c.casenum==null?"":c.casenum);
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
                }else {
                    dquserssid=null;
                    dqcasessid=null;//当前案件ssid
                    cases=null;
                    $("#asknum").val(0);
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

//案件===================================================================end



//**************************************检测****************************start


/*
检测人员不分验证
 */
function checkuserinfograde(userinfogradetype) {
    var userinfograde=userinfogrades[""+userinfogradetype+""];
    if (isNotEmpty(userinfograde)&&isNotEmpty(userinfogradetype)){
        var con="";
        if (userinfogradetype==USERINFOGRADE1){con="原告";} else if (userinfogradetype==USERINFOGRADE2){ con="被告";}else if (userinfogradetype==USERINFOGRADE3){con="被告诉讼代理人";}else if (userinfogradetype==USERINFOGRADE8){con="原告诉讼代理人";}
        var username=userinfograde.username;
        var cardnum=userinfograde.cardnum;
        var phone=userinfograde.phone;
        if (!isNotEmpty(username)){
            layer.msg(con+"名称不能为空",{icon: 5});
            setuserval(userinfogradetype);
            return false;
        }
        if (isNotEmpty(cardnum)){
            var bool=checkByIDCard(cardnum);
            if (!bool){
                layer.msg("请输入有效的"+con+"居民身份证号码",{icon: 5});
                $("#cardnum").focus();
                setuserval(userinfogradetype);
                return false;
            }

            //更新用户的身份信息
            userinfogrades[""+userinfogradetype+""].both=getAnalysisIdCard(cardnum,1);
            userinfogrades[""+userinfogradetype+""].sex=getAnalysisIdCard(cardnum,2);
            userinfogrades[""+userinfogradetype+""].age=getAnalysisIdCard(cardnum,3);
        }
        if (isNotEmpty(phone)&&!(/^((1(3|4|5|6|7|8|9)\d{9})|(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/.test(phone))){
            layer.msg("请输入正确"+con+"联系电话",{icon: 5});
            $("#phone").focus();
            setuserval(userinfogradetype);
            return false;
        }
    }
    return true;
}

//检测回填个人信息
function setuserval(userinfogradetype) {
    var virtualuser=userinfogrades[""+userinfogradetype+""];
    if (isNotEmpty(virtualuser)){
        dq_userinfograde=userinfogradetype;

        /*人员信息*/
        $("#cardnum").val(virtualuser.cardnum);
        $("#beforename").val(virtualuser.beforename);
        $("#nickname").val(virtualuser.nickname);
        $("#both").val(virtualuser.both);
        $("#professional").val(virtualuser.professional);
        $("#phone").val(virtualuser.phone);
        $("#domicile").val(virtualuser.domicile);
        $("#residence").val(virtualuser.residence);
        $("#workunits").val(virtualuser.workunits);
        $("#age").val(virtualuser.age);
        $("#sex").val(virtualuser.sex);
        $("#national").val(virtualuser.nationalssid);
        $("#nationality").val(virtualuser.nationalityssid);
        $("#educationlevel").val(virtualuser.educationlevel);
        $("#politicsstatus").val(virtualuser.politicsstatus);
        $("#issuingauthority").val(virtualuser.issuingauthority);
        $("#validity").val(virtualuser.validity);

        if (dq_userinfograde==USERINFOGRADE1){
            $("#usertype").html("<span style='color: red;font-weight: bold'>原告</span>个人信息")
        } else if (dq_userinfograde==USERINFOGRADE2){
            $("#usertype").html("<span style='color: red;font-weight: bold'>被告</span>个人信息");
        }else if (dq_userinfograde==USERINFOGRADE3){
            $("#usertype").html("<span style='color: red;font-weight: bold'>被告诉讼代理人</span>个人信息")
        }else if (dq_userinfograde==USERINFOGRADE8){
            $("#usertype").html("<span style='color: red;font-weight: bold'>原告诉讼代理人</span>个人信息")
        }
        layui.use('form', function(){
            var $ = layui.$;
            var form = layui.form;
            form.render();
        });
    }
}

//收集人员数据
function setusers(userinfogradessid) {
    var arr=[];
    //判断是否多人除开
    var userinfogradeinput=$("input[name='"+userinfogradessid+"']").val();
    userinfogradeinput=userinfogradeinput.split("；");
    userinfogradeinput = userinfogradeinput.filter(function (x) { return x && x.trim() });
    if (userinfogradeinput.length>1) {
        userinfogradeinput=userinfogradeinput.slice(1);//去除第一个
        for (let i = 0; i < userinfogradeinput.length; i++) {
            const username = userinfogradeinput[i];
            //判断用户名
            var userinfo={
                username:username,
                userinfogradessid:userinfogradessid,
            }
            var casenum=$("#casenum").val();
            console.log("casenum_case__"+casenum_case+"__casenum__"+casenum)
            if (isNotEmpty(casenum_userinfos)&&isNotEmpty(casenum_case)&&isNotEmpty(casenum)&&casenum_case==casenum){
                for (let j = 0; j < casenum_userinfos.length; j++) {
                    const casenum_userinfo = casenum_userinfos[j];
                    if (isNotEmpty(casenum_userinfo)&&isNotEmpty(casenum_userinfo.username)&&username==casenum_userinfo.username){
                        userinfo=casenum_userinfo;
                        userinfo["userinfogradessid"]=userinfogradessid;
                    }
                }
            }
            arr.push(userinfo);
        }
    }
    return arr;
}
//**************************************检测****************************end



















// **************************************获取基础信息**************************************start
function getRecordtypes() {
    var url=getActionURL(getactionid_manage().beforeCourt_getRecordtypes);
    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetRecordtypes);
}
function callbackgetRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var list=data.getRecordtypesVOParamList;
            $('#recotdtypes option').not(":lt(1)").remove();
            gets(list);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
var len="-";
function gets(data) {
    if (isNotEmpty(data)){
        for (var i = 0; i < data.length; i++) {
            var l = data[i];
            if (l.pid!=0){
                $("#recotdtypes").append("<option value='"+l.ssid+"' > |"+len+" "+l.typename+"</option>");
            }
            if (l.police_recordtypes.length>0){
                len+=len;
                gets(l.police_recordtypes);
                len="-";
            }
        }
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}
function getNationals(){
    var url=getActionURL(getactionid_manage().beforeCourt_getNationals);
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
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}
// **************************************获取基础信息**************************************end


//**********************************************获取会议模板******************************start
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
        if (!isNotEmpty(explain)&&isNotEmpty(modelList)){
            for (let i = 0; i < modelList.length; i++) {
                const model = modelList[i];
                if (ssid==model.ssid) {
                    explain=model.explain;
                }
            }
        }
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
//**********************************************获取会议模板*******************************end


//******************************************获取全部笔录模板以及默认笔录模板****************************start
//获取默认模板
function getDefaultMtModelssid(){
    var url=getActionURL(getactionid_manage().beforeCourt_getDefaultMtModelssid);
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
function select_Model2(ssid,wordname){
    if (isNotEmpty(ssid)){
        dqwordssid=ssid;
        if (!isNotEmpty(wordname)&&isNotEmpty(wordList)){
            for (let i = 0; i < wordList.length; i++) {
                const word = wordList[i];
                if (ssid==word.ssid) {
                    wordname=word.wordtemplatename;
                }
            }
        }
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
    var url=getActionURL(getactionid_manage().beforeCourt_getWordTemplates);
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
            if (isNotEmpty(wordTemplates)&&isNotEmpty(default_word)) {
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
                    if (wordTemplate.ssid==default_word) {
                        default_wordTemplate=wordTemplate;
                    }
                }
                if (defaultnum==0){
                    dqwordssid=default_wordTemplate.ssid;
                    $("#wordssid").val(default_wordTemplate.wordtemplatename);
                }
            }
        }
    }else {
        console.log("获取会议默认模板ssid"+data.message)
    }
}
//******************************************获取全部笔录模板以及默认笔录模板****************************end


//********************************************追加“；”*****************************************start
function adduser(obj,type) {
    var inputname="userinfograde"+type;
    var inputval=$("input[name="+inputname+"]").val();
    if (isNotEmpty(inputval)){
        var last=inputval.substr(inputval.length-1,1);
        if (last!="；"){
            $("input[name="+inputname+"]").val(inputval+"；");
            layer.msg("追加“；”字符后面可输入人员名称");
        }else {
            layer.msg("可输入人员名称");
        }
    }else {
           layer.msg("可直接填写人员名称或者通过身份证号码查询");
    }
}
//********************************************追加“；”*****************************************end

/**
 * 根据案件编号查询相关信息：法院需求提出
 */
function getCasesByCasenum() {
    var casenum=$("#casenum").val();
    if (isNotEmpty(casenum)){
        var url=getActionURL(getactionid_manage().beforeCourt_getCasesByCasenum);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                casenum:casenum
            }
        };
        ajaxSubmitByJson(url,data,callbackgetCasesByCasenum);
    }
}

function callbackgetCasesByCasenum(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data = data.data;
        if (isNotEmpty(data)) {
            //清空全部的数据，数据使用默认======================
            dquserssid=null;//当前用户的ssid
            dqcasessid=null;//当前案件ssid
            cases=null;
            userinfogrades={};
            casenum_userinfos=[];
            layui.use(['form','laydate'], function(){
                var form=layui.form;
                var laydate=layui.laydate;
                $("input:not('#casenum'):not('#wordssid'):not('#modelssid'):not('#startrecord_btn'):not('#startrecord_btn2')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):not('#cardnum')*/
                $('select').not("#cards").prop('selectedIndex', 0);
                laydate.render({
                    elem: '#starttime' //指定元素
                    ,type:"datetime"
                    ,value: new Date(Date.parse(new Date()))
                    ,format: 'yyyy-MM-dd HH:mm:ss'
                });
                form.render('select');
            });


            //开始回填数据
             var case_=data.case_;
             if (isNotEmpty(case_)){
                 $("#casename").val(case_.casename);
                 $("#cause").val(case_.cause);
                 $("#asknum").val(0);
                 casenum_case=case_.casenum;
                 if (isNotEmpty(case_.starttime)){
                     $("#starttime").val(case_.starttime);
                 }

                 var arraignmentAndRecord=data.arraignmentAndRecord;
                 if (isNotEmpty(arraignmentAndRecord)){
                     $("#recotdtypes").val(arraignmentAndRecord.recordtypessid);
                     select_Model(arraignmentAndRecord.mtmodelssid,null);
                     select_Model2(arraignmentAndRecord.wordtemplatessid,null);
                 }
                 var usergrades=data.usergrades;
                 if (isNotEmpty(usergrades)){
                     for (let i = 0; i < usergrades.length; i++) {
                         const usergrade = usergrades[i];
                         var usergradessid=usergrade.ssid;
                         var userssid=usergrade.userssid;
                         var username=usergrade.username;

                         if (isNotEmpty(userssid)&&isNotEmpty(usergradessid)) {
                             if (usergradessid==USERINFOGRADE4){
                                 $("#presidingjudge").val(userssid);
                             }else if (usergradessid==USERINFOGRADE5){
                                 $("#recordadminssid").val(userssid);
                             }else if (usergradessid==USERINFOGRADE6){
                                 $("#otheradminssid").val(userssid);
                             }else if (usergradessid==USERINFOGRADE7){
                                 $("#judges").val(userssid);
                             }else if (usergradessid==USERINFOGRADE8||usergradessid==USERINFOGRADE1||usergradessid==USERINFOGRADE2||usergradessid==USERINFOGRADE3){
                                 var userinfo=usergrade.userinfo;
                                 var userinfogradeinput=$("input[name='"+usergradessid+"']").val();
                                 if (isNotEmpty(userinfogradeinput)) {
                                     $("input[name='"+usergradessid+"']").val(userinfogradeinput+"；"+username);
                                     casenum_userinfos.push(userinfo)
                                 }else {
                                     $("input[name='"+usergradessid+"']").val(username);
                                     userinfo["userinfogradessid"]=usergradessid
                                     if (isNotEmpty(userinfo)&&dq_userinfograde==usergradessid){
                                         var cardnum=userinfo.cardnum;
                                         $("#cardnum").val(cardnum);
                                         getUserByCard();
                                         var bool=checkByIDCard(cardnum);
                                         if (bool){
                                             $("#both").val(getAnalysisIdCard(cardnum,1));
                                             $("#sex").val(getAnalysisIdCard(cardnum,2));
                                             $("#age").val(getAnalysisIdCard(cardnum,3));
                                         }

                                     }else if (isNotEmpty(userinfo)&&usergradessid!=dq_userinfograde ){
                                         //其他的存储到
                                         var cardnum=userinfo.cardnum;
                                         var bool=checkByIDCard(cardnum);
                                         if (bool){
                                             userinfo.both=getAnalysisIdCard(cardnum,1);
                                             userinfo.sex=getAnalysisIdCard(cardnum,2);
                                             userinfo.age=getAnalysisIdCard(cardnum,3);
                                         }
                                         userinfogrades[""+usergradessid+""]=userinfo;
                                     }
                                     if (usergradessid==USERINFOGRADE2){/*&&dq_userinfograde==USERINFOGRADE2*/
                                         dqcasessid=case_.ssid;
                                         dquserssid=userssid;
                                         getCaseById();
                                     }
                                 }
                             }
                         }
                     }
                 }
             }else{
                 layer.msg("没有找到相关案件",{icon:6})
                 getCaseById();
             }


        }
    }else {
        console.log(data.message);
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

