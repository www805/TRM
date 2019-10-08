var pdfdownurl=null;
var worddownurl=null;

var videourl=null;//视频地址
var dq_play=null;//当前播放视频
var recordPlayParams=null;//所有视频

var getRecordById_data=null;
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答
var subtractime_q=0;//问的时间差
var subtractime_w=0;//答的时间差

//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getconversationById_getRecordById);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid:recordssid,
            }
        };
        ajaxSubmitByJson(url,data,callbackgetRecordById);
    }else{
        console.log("笔录信息未找到__"+recordssid);
    }
}


function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            getRecordById_data=data;
            var record=data.record;
            pdfdownurl=record.pdfdownurl;//pdf下载地址
            worddownurl=record.worddownurl;//word下载地址

            //提讯数据
            var police_arraignment=record.police_arraignment;
            if (isNotEmpty(police_arraignment)){
                mtssid=police_arraignment.mtssid;//获取会议mtssid
                if (!isNotEmpty(mtssid)) {

                }
            }

            recordnameshow=record.recordname;//当前笔录名称

            if (isNotEmpty(record)){
                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);

                //会议人员
                var recordUserInfosdata=record.recordUserInfos;

                //案件信息
                var case_=record.case_;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(case_)){
                    var casename=case_.casename==null?"":case_.casename;
                    var username=recordUserInfosdata.username==null?"":recordUserInfosdata.username;
                    var cause=case_.cause==null?"":case_.cause;
                    var occurrencetime=case_.occurrencetime==null?"":case_.occurrencetime;
                    var casenum=case_.casenum==null?"":case_.casenum;
                    var adminname=recordUserInfosdata.adminname==null?"":recordUserInfosdata.adminname;
                    var otheradminname=recordUserInfosdata.otheradminname==null?"":recordUserInfosdata.otheradminname;
                    var recordadminname=recordUserInfosdata.recordadminname==null?"":recordUserInfosdata.recordadminname;
                    var department=case_.department==null?"":case_.department;
                    var recordtypename=record.recordtypename==null?"":record.recordtypename;
                    var userInfos=case_.userInfos;
                    var USERHTNL="";
                    if(null!=userInfos) {for (let i = 0; i < userInfos.length; i++) {const u = userInfos[i];USERHTNL += u.username + "、";} USERHTNL = (USERHTNL .substring(USERHTNL .length - 1) == '、') ? USERHTNL .substring(0, USERHTNL .length - 1) : USERHTNL ;}
                    var  init_casehtml="<tr><td style='width: 30%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>被询(讯)问人</td><td>"+username+"</td> </tr>\
                                  <tr><td>案件嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+adminname+"</td></tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                progrssBar_width="89%";
                set_getPlayUrl(getPlayUrlVO);
            }

        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function sortPlayUrl(a, b) {
    return a.filenum - b.filenum;//由低到高
}

var oldname=[];
function  set_getPlayUrl(data) {
    if (isNotEmpty(data)){
        var iiddata=data.iid;
        var recordFileParams=data.recordFileParams;
         recordPlayParams=data.recordPlayParams;
        var state;
        $("#videos").html("");
        if (isNotEmpty(recordFileParams)&&isNotEmpty(recordPlayParams)){
            recordPlayParams.sort(sortPlayUrl);//重新排序一边
            dq_play=recordPlayParams[0];


            for (let i = 0; i < recordPlayParams.length; i++) {
                var play=recordPlayParams[i];
                var playname=play.filename;
                for (let j = 0; j < recordFileParams.length; j++) {
                    const file = recordFileParams[j];
                    var filename= file.filename;
                    if (filename==playname&&oldname.indexOf(filename)<0){
                        var VIDEO_HTML='<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="'+play.filenum+'"  state="'+file.state+'">视频'+play.filenum+'</span>';
                        $("#videos").append(VIDEO_HTML);
                        oldname.push(filename)
                    }
                }
            }

            var firststate= $("#videos span:eq(0)").attr("state");
            //文件状态,0文件未获取，等待中；1文件正常，生成请求地址中；2文件可以正常使用；-1文件未正常获取，需强制获取；-2文件请求地址有误，需重新生成
            if (firststate==2) {
                videourl=dq_play.playUrl;
                initplayer();
            }else {
                layer.msg("文件获取中...",{icon: 5})
            }

            $("#videos span:eq(0)").removeClass("layui-bg-gray").addClass("layui-bg-black");
            $("#videos span").click(function () {
                $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                var filenum= $(this).attr("filenum");
                var state= $(this).attr("state");
                if (state==2) {
                    for (let i = 0; i < recordPlayParams.length; i++) {
                        const dqdata = recordPlayParams[i];
                        if (dqdata.filenum==filenum){
                            dq_play=dqdata;
                        }
                    }
                    videourl=dq_play.playUrl;
                    initplayer();
                }else {
                    layer.msg("文件获取中...",{icon: 5})
                }
            });
        }
    }
}


//*******************************************************************案件人员信息编辑start****************************************************************//

function  open_casetouser() {
    layui.use(['layer','laydate','form'], function(){
        var form = layui.form;
        var laydate = layui.laydate;

        layer.open({
            type: 1,
            title:'人员案件基本信息',
            content:open_casetouser_html,
            area: ['80%', '90%'],
            btn: ['确定','取消'],
            success:function(layero, index){
                laydate.render({
                    elem: '#both' //指定元素
                });
                laydate.render({
                    elem: '#occurrencetime' //指定元素
                    ,type:"datetime"
                });
                laydate.render({
                    elem: '#starttime' //指定元素
                    ,type:"datetime"
                });
                laydate.render({
                    elem: '#endtime' //指定元素
                    ,type:"datetime"
                });

                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();

                //开始回填数据
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
                }
            },
            yes:function(index, layero){
                //开始收集数据
                form.verify({
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


                //参数收集
                var  userInfo={};//人员的信息
                var  case_={};//案件的信息
                var  arraignment={};//提讯的信息


                //收集人员信息
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

                var url=getActionURL(getactionid_manage().getconversationById_updateCaseToUser);
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
                ajaxSubmitByJson(url,d_,function (data) {
                    getRecordById();
                    if(null!=data&&data.actioncode=='SUCCESS'){
                        var data=data.data;
                        //更新数据
                        layer.msg("人员案件基本信息编辑成功",{icon:6,time:500},function () {
                            layer.close(index);
                        });

                    }else{
                        layer.msg(data.message,{icon: 5});
                    }
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
        form.render();
    });
}
var open_casetouser_html='<form class="layui-form layui-row" style="margin: 10px" id="user_form">\
    <div class="layui-col-md6" >\
        <div class="layui-card" style="box-shadow:initial;" >\
            <div class="layui-card-header">人员基本信息</div>\
        <div class="layui-card-body  layui-row layui-col-space10"  >\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">证件类型</label>\
                <div class="layui-input-block">\
                     <input type="text" name="cards" id="cards" lay-verify="username" placeholder="" autocomplete="off" class="layui-input" disabled>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>证件号码</label>\
                <div class="layui-input-block">\
                    <input type="text" name="cardnum" lay-verify="cardnum" placeholder="请输入证件号码" autocomplete="off" class="layui-input" id="cardnum" disabled>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>姓名</label>\
                <div class="layui-input-block">\
                    <input type="text" name="username" id="username" lay-verify="username" placeholder="请输入姓名" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">曾用名</label>\
                <div class="layui-input-block">\
                    <input type="text" name="beforename" id="beforename" placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">绰号</label>\
                <div class="layui-input-block">\
                    <input type="text" name="nickname" id="nickname"  placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <div class="layui-col-lg6" >\
                    <label class="layui-form-label">年龄</label>\
                    <div class="layui-input-block"  >\
                        <input type="number" name="age" id="age"  placeholder="" autocomplete="off" class="layui-input" >\
                    </div>\
                </div>\
                <div class="layui-col-lg6">\
                    <label class="layui-form-label" >性别</label>\
                    <div class="layui-input-block" >\
                        <select name="sex"  id="sex" >\
                        <option value=""></option>\
                        <option value="1">男</option>\
                        <option value="2">女</option>\
                        <option value="-1">未知</option>\
                        </select>\
                    </div>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">出生日期</label>\
                <div class="layui-input-block">\
                    <input type="text" name="both"  id="both"  placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">民族</label>\
                <div class="layui-input-block">\
                    <select name="national"  lay-search="" id="national">\
                    <option value=""></option>\
                    </select>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">国籍</label>\
                <div class="layui-input-block">\
                    <select name="city"  lay-search="" id="nationality">\
                    <option value=""></option>\
                    </select>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">职业</label>\
                <div class="layui-input-block">\
                    <input type="text" name="professional"  id="professional" placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">文化程度</label>\
                <div class="layui-input-block">\
                    <select name="educationlevel"   lay-search="" id="educationlevel">\
                    <option value=""></option>\
                    <option value="小学">小学</option>\
                    <option value="初中">初中</option>\
                    <option value="高中">高中</option>\
                    <option value="中等专科">中等专科</option>\
                    <option value="大学专科">大学专科</option>\
                    <option value="大学本科">大学本科</option>\
                    <option value="研究生">研究生</option>\
                    <option value="硕士研究生">硕士研究生</option>\
                    <option value="博士研究生">博士研究生</option>\
                    <option value="半文盲">半文盲</option>\
                    <option value="文盲">文盲</option>\
                    <option value="其他">其他</option>\
                    </select>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">政治面貌</label>\
                <div class="layui-input-block">\
                    <select name="politicsstatus"   lay-search="" id="politicsstatus">\
                    <option value="群众">群众</option>\
                    <option value="中国致公党党员">中国致公党党员</option>\
                    <option value="台湾民主自治同盟盟员">台湾民主自治同盟盟员</option>\
                    <option value="无党派自由人士">无党派自由人士</option>\
                    <option value="中国共产党党员">中国共产党党员</option>\
                    <option value="中国共产党预备党员">中国共产党预备党员</option>\
                    <option value="中国共产主义青年团团员">中国共产主义青年团团员</option>\
                    <option value="中国国民党革命委员会会员">中国国民党革命委员会会员</option>\
                    <option value="中国民主同盟盟员">中国民主同盟盟员</option>\
                    <option value="中国民主建国会会员">中国民主建国会会员</option>\
                    <option value="中国民主促进会会员">中国民主促进会会员</option>\
                    <option value="中国农民民主党员">中国农民民主党员</option>\
                    </select>\
                </div>\
            </div>\
            <div class="layui-col-lg6">\
                <label class="layui-form-label">联系电话</label>\
                <div class="layui-input-block">\
                    <input type="number" name="phone" id="phone" lay-verify="phone" placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg6"></div>\
            <div class="layui-col-lg12">\
                <label class="layui-form-label">户籍地</label>\
                <div class="layui-input-block">\
                    <input type="text" name="domicile"  id="domicile"  placeholder="请输入省市区街道详细地址" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg12">\
                <label class="layui-form-label">现住地</label>\
                <div class="layui-input-block">\
                    <input type="text" name="residence" id="residence"  placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
            <div class="layui-col-lg12">\
                <label class="layui-form-label">工作单位</label>\
                <div class="layui-input-block">\
                    <input type="text" name="workunits" id="workunits"  placeholder="" autocomplete="off" class="layui-input">\
                </div>\
            </div>\
        </div>\
        </div>\
        </div>\
    <div class="layui-col-md6">\
         <div class="layui-card" style="box-shadow:initial;">\
             <div class="layui-card-header">案件基本信息</div>\
          <div >\
    <div class="layui-card-body  layui-row layui-col-space10" >\
        <div class="layui-col-lg12">\
            <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>案件名称</label>\
            <div class="layui-input-block">\
                <input type="text" name="casename" lay-verify="casename" placeholder="请输入案件名称" autocomplete="off" class="layui-input" id="casename" >\
            </div>\
        </div>\
        <div class="layui-col-lg12">\
            <label class="layui-form-label">当前案由</label>\
            <div class="layui-input-block">\
                <input type="text" name="cause"  placeholder="" autocomplete="off" class="layui-input" id="cause" >\
            </div>\
        </div>\
        <div class="layui-col-lg12">\
            <label class="layui-form-label">案件编号</label>\
            <div class="layui-input-block">\
                <input type="text" name="casenum" id="casenum"  lay-verify="casenum" placeholder="" autocomplete="off" class="layui-input">\
            </div>\
        </div>\
        <div class="layui-col-lg12">\
            <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>笔录名称</label>\
            <div class="layui-input-block">\
                 <input style="color: red;" type="text" name="recordname" id="recordname" lay-verify="recordname" placeholder="默认：《案件名》笔录类型名_第n版本" autocomplete="off" class="layui-input">\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">案发时间</label>\
            <div class="layui-input-block">\
                 <input type="text" name="occurrencetime"  id="occurrencetime" lay-verify="recordname" placeholder="请输入案发时间" autocomplete="off" class="layui-input">\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">到案方式</label>\
            <div class="layui-input-block">\
                 <input type="text" name="caseway"  placeholder="" autocomplete="off" class="layui-input" id="caseway" >\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">开始时间</label>\
            <div class="layui-input-block">\
                <input type="text" name="starttime" id="starttime"  placeholder="" autocomplete="off" class="layui-input">\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">结束时间</label>\
            <div class="layui-input-block">\
                <input type="text" name="endtime" id="endtime"  placeholder="" autocomplete="off" class="layui-input">\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">询问人一</label>\
            <div class="layui-input-block">\
                <input type="text" name="adminname"  id="adminname"  placeholder="" autocomplete="off" class="layui-input" disabled  >\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">工作单位</label>\
            <div class="layui-input-block">\
                <input type="text" name="workname" id="workname"  placeholder="" autocomplete="off" class="layui-input" disabled >\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">询问人二</label>\
            <div class="layui-input-block">\
                <select name="otheruserinfos"  id="otheruserinfos"  lay-search="" lay-filter="otheruserinfos_filter" >\
                    <option value=""></option>\
                 </select>\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">工作单位</label>\
            <div class="layui-input-block">\
                 <input type="text" name="otherworkname"  placeholder="" autocomplete="off" class="layui-input" id="otherworkname" disabled>\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>记录人</label>\
            <div class="layui-input-block">\
                <select name="recordadmin"  id="recordadmin"  lay-search="" lay-verify="recordadmin" >\
                <option value=""></option>\
                </select>\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">问话地点</label>\
            <div class="layui-input-block">\
                <input type="text" name="recordplace" id="recordplace"  placeholder="" autocomplete="off" class="layui-input" >\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>询问对象</label>\
            <div class="layui-input-block">\
                 <input type="text" name="askobj" lay-verify="askobj" placeholder="请输入询问对象" autocomplete="off" class="layui-input" id="askobj">\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">询问次数</label>\
            <div class="layui-input-block">\
                <input type="number" name="asknum" id="asknum"  placeholder="" autocomplete="off" class="layui-input" disabled value="0" >\
            </div>\
        </div>\
        <div class="layui-col-lg6">\
            <label class="layui-form-label">办案部门</label>\
            <div class="layui-input-block">\
                <input type="text" name="department" id="department"  placeholder="" autocomplete="off" class="layui-input"  value="0" >\
            </div>\
        </div>\
    </div>\
    </div>\
    </div>\
    </div>\
    </form>';
//*******************************************************************案件人员信息编辑end****************************************************************//


//*******************************************************************笔录问答编辑start****************************************************************//
function open_recordqw() {
    //切换界面
    $("#recorddetail #record_qw").css({"width":"75%"});
    $("#recorddetail #record_util,#btnadd").css({"display":"block"});
    $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","true");
    $("#wqutil").show();

    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
}

//tr工具按钮==start
function tr_remove(obj) {
    var bool=$(obj).parents("tr").attr("automaticbool");
    if (isNotEmpty(bool)&&bool==1){
        laststarttime_qq=-1;
        laststarttime_ww=-1;
        last_type=-1;//1问题 2是答案
        qq="";
        qqq="";
        ww="";
        www="";
    }
    td_lastindex={};
    $(obj).parents("tr").remove();
    addbtn();
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
    addbtn();
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
    addbtn();
}
//tr工具按钮==end


//lable type 1当前光标加一行 2尾部追加 0首部追加 qw光标文还是答null//不设置光标
function focuslable(html,type,qw) {
    if (!isNotEmpty(html)) {html=trtd_html}
    var qwfocus=null;

    if (null!=td_lastindex["key"]&&type==1){
        $('#recorddetail tr:eq("'+td_lastindex["key"]+'")').after(html);
        if (isNotEmpty(qw)){
            qwfocus= $('#recorddetail tr:eq("'+(td_lastindex["key"]+1)+'") label[name="'+qw+'"]');
            td_lastindex["key"]=td_lastindex["key"]+1;
        }
    }  else if (type==0) {
        $("#recorddetail").prepend(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:eq(0) label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:eq(0)').index();
        }
    }else {
        $("#recorddetail").append(html);
        if (isNotEmpty(qw)){
            qwfocus =  $('#recorddetail tr:last label[name="'+qw+'"]');
            td_lastindex["key"]=$('#recorddetail tr:last').index();
        }
    }

    if (isNotEmpty(qw)){
        setFocus(qwfocus);
    }
    addbtn();
    contextMenu();
}

//聚焦
function setFocus(el) {
    if (isNotEmpty(el)){
        el = el[0];
        if (window.getSelection) {//ie11 10 9 ff safari
            el.focus(); //解决ff不获取焦点无法定位问题
            var range = window.getSelection();//创建range
            range.selectAllChildren(el);//range 选择obj下所有子内容
            range.collapseToEnd();//光标移至最后
        }
        else if (document.selection) {//ie10 9 8 7 6 5
            var range = document.createRange();
            range.selectNodeContents(el);
            range.collapse(false);
            var sel = window.getSelection();
            if(sel.anchorOffset!=0){
                return;
            };
            sel.removeAllRanges();
            sel.addRange(range);
        }
        event.preventDefault();
    }
};

//最后一行添加按钮初始化
function addbtn() {
    var btnhtml='<button type="button"  class="layui-btn layui-btn-warm" style="border-radius: 50%;width: 45px;height: 45px;padding:0px"  title="添加一行自定义问答" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon" style="font-size: 45px" >&#xe608;</i></button>';
    $("#recorddetail tr").each(function () {
        $("#btnadd",this).html("");
    });
    $('#recorddetail tr:last #btnadd').html(btnhtml);
}


//回车+上下键
function qw_keydown(obj,event) {
    var e = event || window.event;
    var keyCode = e.keyCode;

    var dqname=$(obj).attr("name");
    var trindex= $(obj).closest("tr").index();
    var trlength=$("#recorddetail tr").length;
    var lable=null;
    switch(keyCode){
        case 13:
            if (dqname=="q") {
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="w"]');//定位本行的答
                setFocus(lable);
            }else {
                if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                    focuslable(trtd_html,1,'q');
                } else {
                    lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                    setFocus(lable);
                }
            }
            event.preventDefault();
            break;
        case 38:
            var name=dqname=="q"?"w":"q";
            var index=(trindex-1)<=0?0:(trindex-1);
            if (dqname=="w"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                if(trindex!=0){
                    lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                    setFocus(lable);
                }
                event.preventDefault();
            }
            break;
        case 40:
            var index=(trindex+1)>=trlength?trlength:(trindex+1);
            var name=dqname=="q"?"w":"q";
            if (dqname=="q"){
                lable=$('#recorddetail tr:eq("'+trindex+'") label[name="'+name+'"]');
                setFocus(lable);
            }else {
                lable=$('#recorddetail tr:eq("'+index+'") label[name="'+name+'"]');
                setFocus(lable);
            }
            break;
        default: break;
    }
}



/*笔录实时保存*/
function setRecordreal() {
    var url=getActionURL(getactionid_manage().getconversationById_setRecordreal);

    var recordToProblems=[];//题目集合
    $("#recorddetail td.onetd").each(function (i) {
        var arr={};
        var answers=[];//答案集合
        var q=$(this).find("label[name='q']").html();
        var q_starttime=$(this).find("label[name='q']").attr("q_starttime");
        //经过筛选的q
        var ws=$(this).find("label[name='w']");
        var w_starttime=$(this).find("label[name='w']").attr("w_starttime");
        if (isNotEmpty(q)){
            if (null!=ws&&ws.length>0){
                for (var j = 0; j < ws.length; j++) {
                    var w =ws.eq(j).html();
                    //经过筛选的w
                    if (isNotEmpty(w)) {
                        answers.push({
                            answer:w,
                            starttime:w_starttime,
                        });
                    }
                }
            }
            recordToProblems.push({
                problem:q,
                starttime:q_starttime,
                answers:answers
            });
        }
    });
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
            recordToProblems:recordToProblems
        }
    };
    ajaxSubmitByJson(url, data, callbacksetRecordreal);
}
function callbacksetRecordreal(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            /* console.log("笔录实时保存成功__"+data);*/
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//获取缓存实时问答
function getRecordrealByRecordssid() {
    var url=getActionURL(getactionid_manage().getconversationById_getRecordrealByRecordssid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid
        }
    };
    ajaxSubmitByJson(url, data, callbackgetRecordrealByRecordssid);
}
function callbackgetRecordrealByRecordssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var problems=data;
            $("#recorddetail").html("");
            if (isNotEmpty(problems)) {
                var problemhtml= setqw(problems);
                focuslable(problemhtml,2,'w');
            }else {
                $("#recorddetail").html('<div id="datanull_2" style="font-size: 18px;text-align: center; margin:10px;color: rgb(144, 162, 188)">暂无笔录问答</div>');
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//整合问答笔录html

function setqw(problems) {
    if (isNotEmpty(problems)){
        var problemhtml="";
        $("#recorddetail").empty();
        $("#datanull_2").hide();

        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];

            var problemstarttime=problem.starttime;
            var q_starttime=parseFloat(problemstarttime)+parseFloat(subtractime_q);

            var problemtext=problem.problem==null?"未知":problem.problem;
            problemhtml+= '<tr>\
                        <td style="padding: 0;width: 95%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>问：</span><label name="q"  ondblclick="showrecord('+q_starttime+',null)" times="'+q_starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];

                    var answerstarttime=answer.starttime;
                    var w_starttime=parseFloat(answerstarttime)+parseFloat(subtractime_w);

                    var answertext=answer.answer==null?"未知":answer.answer;
                    problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  ondblclick="showrecord('+w_starttime+',null)" times="'+w_starttime+'" >'+answertext+'</label></div>';
                }
            }else{
                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"   ></label></div>';

            }
            problemhtml+=' <div  id="btnadd" style="display: none;"></div></td>\
                        <td style="display: none" id="record_util">\
                            <div class="layui-btn-group">\
                            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                        <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                        <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                         </div>\
                        </td>\
                        </tr>';
        }
        $("#recorddetail_strong").html('【谈话笔录】<i class="layui-icon layui-icon-edit" style="font-size: 20px;color: red" title="编辑" onclick="open_recordqw()"></i>');

        return problemhtml;
    }
    return "";
}


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
var overRecord_index=null;
function addRecord() {
    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    overRecord_loadindex = layer.msg("保存中，请稍等...", {typy:1, icon: 16,shade: [0.1, 'transparent'], time:10000 });
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getconversationById_addRecord);
        //需要收拾数据
        var recordToProblems=[];//题目集合
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                justqwbool:true,
            }
        };
        ajaxSubmitByJson(url, data, calladdRecord);
    }else{
        layer.msg("系统异常");
    }
}
function calladdRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            if (isNotEmpty(overRecord_loadindex)) {
                layer.close(overRecord_loadindex);
            }
            $("#recorddetail #record_qw").css({"width":"95%"});
            $("#recorddetail #record_util,#btnadd").css({"display":"none"});
            $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","false");
            $("#wqutil").hide();
            layer.msg('保存成功',{icon:6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function contextMenu() {
    $('#recorddetail label').bind('mousedown', function(e) {
        if (3 == e.which) {
            document.execCommand('removeFormat');
        }  else if (1 == e.which) {
            document.execCommand('backColor',false,'yellow');
        }
    });
}
//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" q_starttime=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  w_starttime=""placeholder=""></label></div>\
               <div  id="btnadd"></div>\
                </td>\
                <td>\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   </div>\
                </td>\
                </tr>';
//*******************************************************************笔录问答编辑end****************************************************************//




$(function () {
    SewisePlayer.onPlayTime(function(time, id) {
        var totaltime = SewisePlayer.duration() == null ? 0 : SewisePlayer.duration();
        if (parseFloat(time) == parseFloat(totaltime) && isNotEmpty(dq_play) && isNotEmpty(recordPlayParams)) {
            var dqfilenum = dq_play.filenum; //1
            if (dqfilenum < recordPlayParams.length) {  //3
                dq_play = recordPlayParams[dqfilenum];
                videourl = dq_play.playUrl;
                initplayer();
                //样式跟着改变
                $("#videos span").each(function () {
                    var filenum = $(this).attr("filenum");
                    if (filenum == dq_play.filenum) {
                        $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        return false;
                    }
                });
            }
        }
    });

    setInterval( function() {
        setRecordreal();//5秒实时保存
    },3000)

    $("#baocun").click(function () {
        addRecord();
    });


    })



