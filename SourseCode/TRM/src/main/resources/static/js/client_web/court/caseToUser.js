//*******************************************************************案件人员信息编辑start****************************************************************//


var recordssid=null;
var cardtypessid=null;

var dq_userinfograde=USERINFOGRADE2;
var userinfogrades={};
var casenum_userinfos=[];//根据案件编号回填的多角色人员
var casenum_case=null;




function setcaseToUser(getRecordById_data) {
    setTimeout(function () {
        layui.use(['layer','laydate','form'], function() {
            var form = layui.form;
            var laydate = layui.laydate;

            if (isNotEmpty(getRecordById_data)) {
                var record=getRecordById_data.record;
                if (isNotEmpty(record)) {
                    if (isNotEmpty(parent.mtssid)){
                        $('#presidingjudge,#recordadminssid,#otheradminssid,#judges,#recotdtypes').attr('disabled', 'disabled');
                    }

                    //案件信息
                    var case_ = record.case_;
                    if (isNotEmpty(case_)){
                        casenum_case=case_.casenum;
                        $("#casename").val(case_.casename);
                        $("#cause").val(case_.cause);
                        $("#asknum").val(0);
                        $("#casenum").val(case_.casenum);
                        if (isNotEmpty(case_.starttime)){
                            $("#starttime").val(case_.starttime);
                        }
                    }
                    //笔录类型ssid
                    $("#recotdtypes").val(record.recordtypessid);
                    //笔录word模板
                    select_Model2(record.wordtemplatessid,null);
                    //提讯信息
                    var police_arraignment=record.police_arraignment;
                    if (isNotEmpty(police_arraignment)){
                        $("#asknum").val(police_arraignment.asknum);
                        select_Model(police_arraignment.mtmodelssid,null);
                    }

                    var recordUserInfos=record.recordUserInfos;
                    if (isNotEmpty(recordUserInfos)){
                        var usergrades=recordUserInfos.usergrades;
                        if (isNotEmpty(usergrades)){
                            for (let i = 0; i < usergrades.length; i++) {
                                const usergrade = usergrades[i];
                                var usergradessid=usergrade.ssid;
                                var userssid=usergrade.userssid;


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
                                        console.log(userinfo)
                                        var userinfogradeinput=$("input[name='"+usergradessid+"']").val();
                                        userinfo["userinfogradessid"]=usergradessid;
                                        if (isNotEmpty(userinfogradeinput)) {
                                            $("input[name='"+usergradessid+"']").val(userinfogradeinput+"；"+userinfo.username);
                                            //存储ssid
                                            var userinfograde_input=$("input[name='"+usergradessid+"_']").val();
                                            $("input[name='"+usergradessid+"_']").val(userinfograde_input+"；"+userinfo.ssid);

                                            casenum_userinfos.push(userinfo);
                                        }else {
                                            $("input[name='"+usergradessid+"']").val(userinfo.username);

                                            //存储ssid
                                            $("input[name='"+usergradessid+"_']").val(userinfo.ssid);
                                            if (isNotEmpty(userinfo)&&dq_userinfograde==usergradessid){
                                                userinfogrades[""+usergradessid+""]=userinfo;//
                                                var cardnum=userinfo.cardnum;
                                                $("#cardnum").val(cardnum);
                                                setuserval(dq_userinfograde)
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
                                                userinfogrades[""+usergradessid+""]=userinfo;//
                                            }

                                        }
                                    }
                                }
                            }



                        }
                    }

                }

            }

            form.verify({
                recotdtypes:[ /\S/,"请选择案件类型"],
                casename:[ /\S/,"请输入案件名称"],
            });

            form.on('submit(permissionSubmit)', function(data) {
                var  username=$("#username").val();
                if (!isNotEmpty(username)){
                    layer.msg(userinfograde2_name+"不能为空",{icon: 5});
                    return;
                }

                //案件信息收集
                var  casename=$("#casename").val();
                if (!isNotEmpty(casename)){
                    layer.msg("案件名称不能为空",{icon: 5});
                    return;
                }
                var cause=$("#cause").val();
                var casenum=$("#casenum").val();
                var starttime=$("#starttime").val();
                var case_={
                    cause:cause,
                    casenum:casenum,
                    starttime:starttime,
                    casename:casename
                }

                //收集提讯信息
                var asknum=$("#asknum").val();
                var otheradminssid=$("#otheradminssid").val();
                var recordadminssid=$("#recordadminssid").val();
                var arraignment={
                    asknum:asknum,
                    otheradminssid:otheradminssid,
                    recordadminssid:recordadminssid,
                }

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
                    layer.msg(userinfograde4_name+"不能为空",{icon: 5});
                    return;
                }
                //条件存在会议：
                if (isNotEmpty(parent.mtssid)){
                    //各个人员角色人员数量不可变动:对比全部人员判断各角色对应的人员数量是否有变动；有变动提示人员数不可改动
                    var userinfograde8_=$("input[name='"+USERINFOGRADE8+"_']").val();
                    var userinfograde1_=$("input[name='"+USERINFOGRADE1+"_']").val();
                    var userinfograde2_=$("input[name='"+USERINFOGRADE2+"_']").val();
                    var userinfograde3_=$("input[name='"+USERINFOGRADE3+"_']").val();

                    //判断角色是否有增减
                    var userinfograde8=$("input[name='"+USERINFOGRADE8+"']").val();
                    var userinfograde1=$("input[name='"+USERINFOGRADE1+"']").val();
                    var userinfograde2=$("input[name='"+USERINFOGRADE2+"']").val();
                    var userinfograde3=$("input[name='"+USERINFOGRADE3+"']").val();
                    console.log(userinfograde8)

                    userinfograde8_=userinfograde8_.split("；");
                    userinfograde8_ = userinfograde8_.filter(function (x) { return x && x.trim() });
                    userinfograde8=userinfograde8.split("；");
                    userinfograde8 = userinfograde8.filter(function (x) { return x && x.trim() });

                    userinfograde1_=userinfograde1_.split("；");
                    userinfograde1_ = userinfograde1_.filter(function (x) { return x && x.trim() });
                    userinfograde1=userinfograde1.split("；");
                    userinfograde1 = userinfograde1.filter(function (x) { return x && x.trim() });

                    userinfograde2_=userinfograde2_.split("；");
                    userinfograde2_ = userinfograde2_.filter(function (x) { return x && x.trim() });
                    userinfograde2=userinfograde2.split("；");
                    userinfograde2 = userinfograde2.filter(function (x) { return x && x.trim() });

                    userinfograde3_=userinfograde3_.split("；");
                    userinfograde3_ = userinfograde3_.filter(function (x) { return x && x.trim() });
                    userinfograde3=userinfograde3.split("；");
                    userinfograde3 = userinfograde3.filter(function (x) { return x && x.trim() });

                    if (userinfograde8_.length!=userinfograde8.length){//人员改动
                        layer.msg("笔录已开，【"+userinfograde8_name+"】不可增删",{icon: 5});
                        return;
                    }

                    if (userinfograde1_.length!=userinfograde1.length){//人员改动
                        layer.msg("笔录已开，【"+userinfograde1_name+"】不可增删",{icon: 5});
                        return;
                    }
                    if (userinfograde2_.length!=userinfograde2.length){//人员改动
                        layer.msg("笔录已开，【"+userinfograde2_name+"】不可增删",{icon: 5});
                        return;
                    }
                    if (userinfograde3_.length!=userinfograde3.length){//人员改动
                        layer.msg("笔录已开，【"+userinfograde3_name+"】不可增删",{icon: 5});
                        return;
                    }


                }



                var d_={
                    token:INIT_CLIENTKEY,
                    param:{
                        case_:case_,
                        arraignment:arraignment,
                        recordssid:recordssid,
                        arraignmentexpand:arraignmentexpand,
                        arrUserExpandParams:arrUserExpandParams,
                    }
                };
                var caseToUser_updateCaseToUser_courtUrl=getActionURL(getactionid_manage().caseToUser_updateCaseToUser_court);
                $("#permissionSubmit").attr("lay-filter","");
                ajaxSubmitByJson(caseToUser_updateCaseToUser_courtUrl,d_,function (data) {
                    if(null!=data&&data.actioncode=='SUCCESS'){
                        var data=data.data;
                        //更新数据:需要处理
                        parent.layer.msg("人员案件基本信息编辑成功",{icon:6,time:500},function () {
                            parent.getRecordById();
                            parent.layer.closeAll('iframe'); //关闭弹框
                        });
                    }else{
                        parent.layer.msg(data.message,{icon: 5});
                    }
                    $("#permissionSubmit").attr("lay-filter","permissionSubmit");
                });
            });

            form.render();
        });
    },350);
}




//*******************************************************************案件人员信息编辑end****************************************************************//

//不做查询用于信息存储
function getUserByCard(usertype){
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
        }else if (dq_userinfograde==USERINFOGRADE3){
            $("#usertype").html("<span style='color: red;font-weight: bold'>被告诉讼代理人</span>个人信息")
        }else if (dq_userinfograde==USERINFOGRADE8){
            $("#usertype").html("<span style='color: red;font-weight: bold'>原告诉讼代理人</span>个人信息")
        }
        //判断是否存在回显
        if (isNotEmpty(userinfogrades[""+dq_userinfograde+""])){
            setuserval(dq_userinfograde);
        }else {
            $("#user input").val("");
            $('#user  select').prop('selectedIndex', 0);
        }
    }
}

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
function checkuserinfograde(userinfogradetype) {
    var userinfograde=userinfogrades[""+userinfogradetype+""];
    if (isNotEmpty(userinfograde)&&isNotEmpty(userinfogradetype)){
        var con="";
        if (userinfogradetype==USERINFOGRADE1){con=userinfograde1_name;} else if (userinfogradetype==USERINFOGRADE2){ con=userinfograde2_name;}else if (userinfogradetype==USERINFOGRADE3){con=userinfograde3_name;}else if (userinfogradetype==USERINFOGRADE8){con=userinfograde8_name;}
        var username=userinfograde.username;
        var cardnum=userinfograde.cardnum;
        var phone=userinfograde.phone;

        //ssid
        var userinfograde_=$("input[name='"+userinfogradetype+"_']").val();
        if (isNotEmpty(userinfograde_)){
            userinfograde_=userinfograde_.split("；");
            userinfograde_ = userinfograde_.filter(function (x) { return x && x.trim() });
            userinfogrades[""+userinfogradetype+""].ssid=userinfograde_[0];
        }


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
        if (isNotEmpty(phone)&&!(/^0?(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[89])[0-9]{8}$/.test(phone))){
            layer.msg("请输入正确"+con+"联系电话",{icon: 5});
            $("#phone").focus();
            setuserval(userinfogradetype);
            return false;
        }


    }
    return true;
}
function select_cardnumblur() {
    $("#cardnum_ssid").css("display","none");
    var cardnum=$("#cardnum").val();
    if (isNotEmpty(cardnum)) {
        // getUserByCard();
        var bool=checkByIDCard(cardnum);
        if (bool){
            $("#both").val(getAnalysisIdCard(cardnum,1));
            $("#sex").val(getAnalysisIdCard(cardnum,2));
            $("#age").val(getAnalysisIdCard(cardnum,3));
        }
    }
    layui.use(['form'], function() {
        var form = layui.form;
        form.render();
    });
}

