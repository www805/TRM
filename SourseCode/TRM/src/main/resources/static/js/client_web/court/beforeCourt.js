var dquserssid=null;//当前被告人ssid
var dqcasessid=null;//当前案件ssid
var dqmodeltypenum=null;//当前所选的会议模板类型



var skipCheckbool=-1;//是否跳过检测：默认-1
var skipCheckCasebool=-1;//是否跳过案件检查(法庭)
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表

//当前用户类型 1原告 2被告  3被代理人 8原代理人
var dq_userinfograde=USERINFOGRADE2;//默认为被告
var userinfogrades={}


var userinfograde1_name=USERINFOGRADE1_NAME;//原告
var userinfograde2_name=USERINFOGRADE2_NAME;//被告
var userinfograde3_name=USERINFOGRADE3_NAME;//被告诉讼代理人
var userinfograde4_name=USERINFOGRADE4_NAME;//审判长
var userinfograde5_name=USERINFOGRADE5_NAME;//书记员
var userinfograde6_name=USERINFOGRADE6_NAME;//陪审员
var userinfograde7_name=USERINFOGRADE7_NAME;//审判员
var userinfograde8_name=USERINFOGRADE8_NAME;//原告诉讼代理人


//开始笔录按钮=====================================================================================================start
function addCaseToArraignment() {
    var url=getActionURL(getactionid_manage().beforeCourt_addCaseToArraignment);
    var recordtypessid= $("#recotdtypes").val();
    if (!isNotEmpty(recordtypessid)){
        layer.msg("请选择案件类型",{icon: 5});
        return;
    }

    var  username=$("#username").val();
    if (!isNotEmpty(username)){
        layer.msg(userinfograde2_name+"不能为空",{icon: 5});
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
        layer.msg(userinfograde4_name+"不能为空",{icon: 5});
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

//检测人员不分验证
function checkuserinfograde(userinfogradetype) {
    var userinfograde=userinfogrades[""+userinfogradetype+""];
    if (isNotEmpty(userinfograde)&&isNotEmpty(userinfogradetype)){
        var con="";
        if (userinfogradetype==USERINFOGRADE1){con=userinfograde1_name;} else if (userinfogradetype==USERINFOGRADE2){ con=userinfograde2_name;}else if (userinfogradetype==USERINFOGRADE3){con=userinfograde3_name;}else if (userinfogradetype==USERINFOGRADE8){con=userinfograde8_name;}
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
//开始笔录按钮=======================================================================================================end





//用户=============================================================================================================start
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
        getUserByCard();
        var bool=checkByIDCard(cardnum);
        if (bool){
            $("#both").val(getAnalysisIdCard(cardnum,1));
            $("#sex").val(getAnalysisIdCard(cardnum,2));
            $("#age").val(getAnalysisIdCard(cardnum,3));
        }
    }
}

//获取人员信息：当前点击的usertype 当前dq_usertype
function getUserByCard(usertype){
    var cardnum =  $("#cardnum").val();
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
    }

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
//用户===============================================================================================================end

