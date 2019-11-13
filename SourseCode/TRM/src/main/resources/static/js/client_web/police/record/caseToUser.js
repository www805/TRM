//*******************************************************************案件人员信息编辑start****************************************************************//


var recordssid;
var cardtypessid=null;
function setcaseToUser(getRecordById_data) {
    layui.use(['layer','laydate','form'], function() {
        var form = layui.form;
        var laydate = layui.laydate;

        if (isNotEmpty(getRecordById_data)) {
        var otheruserinfos=getRecordById_data.adminList;//全部用户，
        var nationalityList=getRecordById_data.nationalityList;//全部国籍
        var nationalList=getRecordById_data.nationalList;//全部民族

        //全部用户
        if(isNotEmpty(otheruserinfos)){
            $('#recordadmin option').not(":lt(1)").remove();
            $('#otheruserinfos option').not(":lt(1)").remove();
            if (isNotEmpty(otheruserinfos)){
                for (var i = 0; i < otheruserinfos.length; i++) {
                    var u= otheruserinfos[i];
                    if (u.ssid!=sessionadminssid) {
                        $("#recordadmin").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                        $("#otheruserinfos").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    }
                }
                form.on('select(otheruserinfos_filter)', function(data){
                    $("#otherworkname").val("");
                    var otheruserinfosssid=data.value;
                    for (var i = 0; i < otheruserinfos.length; i++) {
                        var u = otheruserinfos[i];
                        if (otheruserinfosssid==u.ssid){
                            $("#otherworkname").val(u.workname);
                        }
                    }
                });
                form.render();
            }
        }
        //全部国籍
        if (isNotEmpty(nationalityList)) {
            for (var i = 0; i < nationalityList.length; i++) {
                var l = nationalityList[i];
                $("#nationality").append("<option value='"+l.ssid+"' title='"+l.enname+"'> "+l.zhname+"</option>");
            }
            form.render();
        }
        //全部民族
        if (isNotEmpty(nationalList)) {
            for (var i = 0; i < nationalList.length; i++) {
                var l = nationalList[i];
                $("#national").append("<option value='"+l.ssid+"' title='"+l.nationname+"'>"+l.nationname+"</option>");
            }
            form.render();
        }



        var record=getRecordById_data.record;
        if (isNotEmpty(record)){
            //回显嫌疑人信息
            var recordUserInfos=record.recordUserInfos;
            var case_=record.case_;
            var police_arraignment=record.police_arraignment;
            $("#recordname").val(record.recordname);
            if (isNotEmpty(recordUserInfos)) {
                var userinfo=recordUserInfos.userInfo;
                if (isNotEmpty(userinfo)) {
                    cardtypessid=userinfo.cardtypessid;
                    $("#cards").val(userinfo.cardtypename);
                    $("#cardnum").val(userinfo.cardnum);
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
                //回显询问人信息
                $("#adminname").val(recordUserInfos.adminname);
                $("#otheruserinfos").val(recordUserInfos.otheradminssid);
                $("#recordadmin").val(recordUserInfos.recordadminssid);
                $("#workname").val(recordUserInfos.workunitname1);
                $("#otherworkname").val(recordUserInfos.workunitname2);
            }
            //回显案件人信息
            if (isNotEmpty(case_)){
                $("#casename").val(case_.casename);
                $("#cause").val(case_.cause);
                $("#casenum").val(case_.casenum);
                $("#occurrencetime").val(case_.occurrencetime);
                $("#starttime").val(case_.starttime);
                $("#endtime").val(case_.endtime);
                $("#caseway").val(case_.caseway);
                $("#department").val(case_.department);

            }
            if (isNotEmpty(police_arraignment)){
                $("#askobj").val(police_arraignment.askobj);
                $("#asknum").val(police_arraignment.asknum);
                $("#recordplace").val(police_arraignment.recordplace);
            }

        }
            form.render();
    }

        //开始收集数据
        form.verify({
            cardnum:function (value) {
                var nationality = $("#nationality option:selected").text();//国籍
                var cardtypetext = $("#cardtypetext").val();//
                if (!(/\S/).test(value)) {
                    return "请输入居民身份证号码"
                }
                if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
                    var checkidcard_bool=checkIDCard(value);
                    if (!checkidcard_bool) {
                        return "请输入有效的居民身份证号码";
                    }
                }
            },
            username:[ /\S/,"请输入姓名"],
            phone:function (value) {
                if (isNotEmpty(value)&&!(/^((1(3|4|5|6|7|8|9)\d{9})|(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/.test(value))){
                    return "请输入正确联系电话";
                }
            },
            casename:[ /\S/,"请输入案件名称"],
            recordname:[ /\S/,"请输入笔录名称"],
            recordadmin:[ /\S/,"请输入记录人"],
            askobj:[ /\S/,"请输入询问对象"],
            casenum:function (value) {
                if (isNotEmpty(value)) {
                    if (!(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,}$/).test(value)||!(/^[^ ]+$/).test(value)){
                        return "请输入英文和数字组成5位以上不含空格的案件编号"
                    }
                }
            }
        });

        form.on('submit(permissionSubmit)', function(data) {
            //参数收集
            var  userInfo={};//人员的信息
            var  case_={};//案件的信息
            var  arraignment={};//提讯的信息


            //收集人员信息
            var cardnum=$("#cardnum").val();
            var  username=$("#username").val();
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
            userInfo={
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
            var  casename=$("#casename").val();
            case_={
                casename:casename,
                cause:cause,
                casenum:casenum,
                occurrencetime:occurrencetime,
                starttime:starttime,
                endtime:endtime,
                caseway:caseway,
                department:department
            }

            //收集提讯信息
            var askobj=$("#askobj").val();
            var asknum=$("#asknum").val();
            var recordplace=$("#recordplace").val();
            var otheradminssid=$("#otheruserinfos").val();
            var recordadminssid=$("#recordadmin").val();
            arraignment={
                askobj:askobj,
                asknum:asknum,
                recordplace:recordplace,
                otheradminssid:otheradminssid,
                recordadminssid:recordadminssid,
            }

            var recordname=$("#recordname").val();
            var d_={
                token:INIT_CLIENTKEY,
                param:{
                    userInfo:userInfo,
                    case_:case_,
                    arraignment:arraignment,
                    recordssid:recordssid,
                    recordname:recordname
                }
            };
            var url=getActionURL(getactionid_manage().caseToUser_updateCaseToUser);
            ajaxSubmitByJson(url,d_,function (data) {
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
            });
        });
    });
}


//*******************************************************************案件人员信息编辑end****************************************************************//