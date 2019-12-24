/**
 * 此处js 不需要没有证件号码没有详情人员信息 只需要名字
 * @type {null}
 */
var dquserssid=null;//当前被告人ssid
var dqcasessid=null;//当前案件ssid

var skipCheckbool=-1;//是否跳过检测：默认-1
var skipCheckCasebool=-1;//是否跳过案件检查(法庭)
var skipCheckCaseNumbool=-1;
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表

//当前用户类型 1原告 2被告  3被代理人 8原代理人
var dq_userinfograde=USERINFOGRADE2;//默认为被告
var userinfogrades={};


//开始笔录按钮=====================================================================================================start
function addCaseToArraignment() {
    var url=getActionURL(getactionid_manage().quickCourt_addCaseToArraignment);
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
    var oldusernamelist=$("input[name="+userinfogradessid+"]").val();
    var olduserinfo=null;
    if (isNotEmpty(oldusernamelist)){
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
        olduserinfo["username"]=oldusername;
        olduserinfo["userinfogradessid"]=userinfogradessid;
    }
    userinfogrades[""+userinfogradessid+""]=olduserinfo;

    if (!isNotEmpty(userinfogrades[""+USERINFOGRADE2+""])){
        layer.msg("被告不能为空",{icon: 5});
        return;
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
                if((isNotEmpty(arraignmentexpand[i].username)&&isNotEmpty(newarraignmentexpand[j].username)&& arraignmentexpand[i].username==newarraignmentexpand[j].username)){
                    bool=false;
                    layer.msg("用户名称不能重复使用",{icon: 5});
                    return;
                }
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
            skipCheckCaseNumbool:skipCheckCaseNumbool,
            multifunctionbool:3,//庭审默认多功能
            arraignmentexpand:arraignmentexpand,
            arrUserExpandParams:arrUserExpandParams
        }
    };
    $("#startrecord_btn").attr("lay-filter","");
    $("#startrecord_btn2").attr("lay-filter","");

    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}
function callbackaddCaseToArraignment(data) {
    $("#startrecord_btn").attr("lay-filter","startrecord_btn");
    $("#startrecord_btn2").attr("lay-filter","startrecord_btn2");
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
                        var toUrl=getActionURL(getactionid_manage().quickCourt_towaitCourt);
                        location.href=toUrl+"?ssid="+recordssid;
                    }
                });
            }else if(toUrltype==2){
                //跳转笔录查看列表:后期统一列表显示页面
                var url = getActionURL(getactionid_manage().quickCourt_torecordIndex);
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


            var custommsgbool=data2.custommsgbool;

            var casenumingbool=data2.casenumingbool;
            var casessid=data2.casessid;

            //案件暂停状态
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
                        if (custommsgbool==1){
                            addCaseToArraignment2();
                        }else {
                            addCaseToArraignment();
                        }
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
                            if (custommsgbool==1){
                                addCaseToArraignment2();
                            }else {
                                addCaseToArraignment();
                            }
                            layer.close(index);
                        },
                        btn2: function(index) {
                            console.log("跳转笔录列表")
                            toUrltype=2;
                            skipCheckbool =1;
                            if (custommsgbool==1){
                                addCaseToArraignment2();
                            }else {
                                addCaseToArraignment();
                            }
                            layer.close(index);
                        },
                        btn3: function(index) {
                            layer.close(index);
                        }
                    });
                }
            }else if (null!=casenumingbool&&casenumingbool==true&&isNotEmpty(casessid)) {
                layer.confirm("<span style='color:red'>案件编号已存在，是否需要重新开启上一份笔录</span>", {
                    btn: ['确定',"取消"], //按钮
                    shade: [0.1,'#fff'], //不显示遮罩
                    btn1:function(index) {
                        console.log("获取上一个案件信息并且使用最后一次提讯信息");
                        //继续上一次提讯
                        addCaseToArraignment_Backfill(casessid);
                        layer.close(index);
                    },
                    btn2: function(index) {
                        layer.close(index);
                    }
                });

         }else {
                layer.msg(data.message,{icon: 5});
            }
        }else {
            layer.msg(data.message,{icon: 5});
        }
    }
}


//继续上一个
function addCaseToArraignment_Backfill(casessid) {
    if (isNotEmpty(casessid)){
        var url=getActionURL(getactionid_manage().quickCourt_addCaseToArraignment_Backfill);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                casessid:casessid,
            }
        };
        ajaxSubmitByJson(url,data,callbackaddCaseToArraignment_Backfill);
    }
}
function callbackaddCaseToArraignment_Backfill(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var addcasetoarraignmentvo_data=data.data;
        if (isNotEmpty(addcasetoarraignmentvo_data)) {
            layer.msg("笔录重新开启成功", {time: 500,icon: 6},function () {
                    addcasetoarraignmentvo_data = eval('(' + addcasetoarraignmentvo_data + ')');
                    var recordssid=addcasetoarraignmentvo_data.recordssid;
                    var multifunctionbool=addcasetoarraignmentvo_data.multifunctionbool;

                    if (isNotEmpty(addcasetoarraignmentvo_data)&&isNotEmpty(recordssid)){
                        layer.confirm("<span style='color:red'>新的笔录已生成</span>", {
                            btn:['开始笔录',"查看笔录列表","取消"], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转笔录制作中");
                                var index =layer.msg('开始进行笔录', {shade:[0.1,"#fff"],icon:6,time:500
                                },function () {
                                    var url=getActionURL(getactionid_manage().quickCourt_towaitCourt);
                                    window.location.href=url+"?ssid="+recordssid;
                                });
                                layer.close(index);
                            },
                            btn2: function(index) {
                                console.log("跳转笔录列表")
                                var url = getActionURL(getactionid_manage().quickCourt_torecordIndex);
                                window.location.href = url;
                                layer.close(index);
                            },
                            btn3: function(index) {
                                layer.close(index);
                            }
                        });
                    }
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function addCaseToArraignment2() {
    var casenum=$("#casenum2").val();
    if (!isNotEmpty(casenum)){
        layer.msg("案件编号不能为空",{icon: 5});
        return;
    }
    var addPolice_case={
        casenum:casenum
    }
    var mtmodelssidname=$("#modelssid2").val();
    if (!isNotEmpty(casenum)){
        layer.msg("承办庭不能为空",{icon: 5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            mtmodelssid:dqmodelssid,
            mtmodelssidname:mtmodelssidname,//模板名称
            addPolice_case:addPolice_case,
            multifunctionbool:3,//庭审默认多功能
            custommsgbool:1,//是否需要自定义信息
            skipCheckCasebool:skipCheckCasebool,
            skipCheckbool:skipCheckbool,
            skipCheckCaseNumbool:skipCheckCaseNumbool,
        }
    };
    $("#startrecord_btn").attr("lay-filter","");
    $("#startrecord_btn2").attr("lay-filter","");
    var url=getActionURL(getactionid_manage().quickCourt_addCaseToArraignment);
    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}

//人员信息监测
function checkuserinfograde(userinfogradetype) {
    var userinfograde=userinfogrades[""+userinfogradetype+""];
    if (isNotEmpty(userinfograde)&&isNotEmpty(userinfogradetype)){
        var con="";
        if (userinfogradetype==USERINFOGRADE1){con="原告";} else if (userinfogradetype==USERINFOGRADE2){ con="被告";}else if (userinfogradetype==USERINFOGRADE3){con="被告诉讼代理人";}else if (userinfogradetype==USERINFOGRADE8){con="原告诉讼代理人";}
        var username=userinfograde.username;
        if (!isNotEmpty(username)){
            layer.msg(con+"名称不能为空",{icon: 5});
            return false;
        }
    }
    return true;
}
//用户切换时候保存：非接口
function getUserByCard(usertype){
    if (isNotEmpty(usertype)) {
        //收集切换前的用户信息
        var userinfogradessid=dq_userinfograde;
        var oldusernamelist=$("input[name="+userinfogradessid+"]").val();
        var olduserinfo=null;
        if (isNotEmpty(oldusernamelist)) {
            var oldusername = null;
            if (isNotEmpty(oldusernamelist)) {
                var oldusername=oldusernamelist.split("；");
                oldusername = oldusername.filter(function (x) { return x && x.trim() });
                if (oldusername.length>0) { oldusername=oldusername[0];}
            }
            var olduserinfo=userinfogrades[""+userinfogradessid+""];
            if (!isNotEmpty(olduserinfo)){olduserinfo={};}
            olduserinfo["username"]=oldusername;
            olduserinfo["userinfogradessid"]=userinfogradessid;
        }
        userinfogrades[""+userinfogradessid+""]=olduserinfo;
        dq_userinfograde=usertype;
    }
}
//开始笔录按钮=======================================================================================================end






//退出登录
function userloginout() {
    layer.confirm('确定要退出登录吗？', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().quickCourt_userloginout);
        var data={
            token:INIT_CLIENTKEY
        };
        ajaxSubmitByJson(url,data,callbackuserloginout);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });
}
function callbackuserloginout(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("退出成功",{time:500,icon: 6,},function () {
            var url=getActionURL(getactionid_manage().quickCourt_gotologin);
            if (isNotEmpty(url)){
                top.location.href=url;
            }
        });
    }else{
        layer.msg(data.message, {icon: 5});
    }
}

//进入总控
function gotocontrol() {
    layui.use(['layer','element','form','laydate'], function() {
        var layer = layui.layer; //获得layer模块
        layer.msg("加载中，请稍后...", {
            icon: 16,
            time:15000,
            shade: [0.1,"#fff"],
        });
    });

    if (isNotEmpty(guidepageUrl)){
        top.location.href=guidepageUrl;
    }
}









