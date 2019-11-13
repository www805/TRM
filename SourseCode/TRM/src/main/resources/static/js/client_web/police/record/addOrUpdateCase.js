
var userinfos_=[];//案件人
var dquserssid=null;



/**
 * 获取案件信息
 * @cmparam ssid
 */
function getCaseBySsid() {
    if (!isNotEmpty(ssid)){
       return;
    }

    var url=getActionURL(getactionid_manage().addOrUpdateCase_getCaseBySsid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casessid:ssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCaseBySsid);
}
function callbackgetCaseBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var case_=data.case_;
                if (isNotEmpty(case_)){
                    $("#casename").val(case_.casename);
                    $("#cause").val(case_.cause);
                    $("#casenum").val(case_.casenum);
                    $("#occurrencetime").val(case_.occurrencetime);
                    $("#starttime").val(case_.starttime);
                    $("#endtime").val(case_.endtime);
                    $("#userssid").find("option[value='"+case_.userssid+"']").attr("selected",true);
                    $("#caseway").val(case_.caseway);

                    var userInfos=case_.userInfos;
                    if (isNotEmpty(userInfos)){
                        set_userinfo(userInfos);
                    }
                }
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

function set_userinfo(userInfos) {
    $("#user_tbody tbody").html("");
    if (isNotEmpty(userInfos)){
        var TBODY_HTML="";
        userinfos_=userInfos;
        for (let i = 0; i < userInfos.length; i++) {
            const userInfo = userInfos[i];
            var sex=userInfo.sex==1?"男":(userInfos.sex==2?"女":"未知");
            var cards=userInfo.cards;

            var username=userInfo.username==null?"":userInfo.username;
            var cardtypename=userInfo.cardtypename==null?"":userInfo.cardtypename;
            var cardnum=userInfo.cardnum==null?"":userInfo.cardnum;
            var both=userInfo.both==null?"":userInfo.both;
            var domicile=userInfo.domicile==null?"":userInfo.domicile;
            var arraignment_num=userInfo.arraignment_num==null?0:userInfo.arraignment_num;
            TBODY_HTML+='<tr>\
                                        <td>'+(i+1)+'</td>\
                                        <td>'+username+'</td>\
                                        <td>'+cardtypename+'</td>\
                                        <td>'+cardnum+'</td>\
                                        <td>'+both+'</td>\
                                        <td>'+domicile+'</td>\
                                        <td>\
                                            <input  class="layui-btn layui-btn-normal layui-btn-xs" type="button" value="修改" onclick=tr_addOrUpdate(this,2)>\
                                            <input  class="layui-btn layui-btn-danger layui-btn-xs"  type="button" value="删除" onclick=tr_remove(this,'+arraignment_num+');>\
                                         </td>\
                                        </tr>';
            userInfo["xh"]=i+1;
        }
        $("#user_tbody tbody").html(TBODY_HTML)
    }
}

//删除某个案件人
function tr_remove(obj,arraignment_num) {
    if (isNotEmpty(arraignment_num)&&arraignment_num>0){
        layer.msg("该用户已被提讯"+arraignment_num+"次不允许删除",{icon:5});
        return false;
    }
    var user_xh= $(obj).parents("tr").find("td:eq(0)").text().trim();
    if (isNotEmpty(user_xh)&&isNotEmpty(userinfos_)){
        var new_userinfos_=[];
        for (let i = 0; i < userinfos_.length; i++) {
            const userinfo = userinfos_[i];
            if (null!=userinfo.xh&&userinfo.xh!=user_xh){
                new_userinfos_.push(userinfo);
            }
        }
        userinfos_=new_userinfos_;
        set_userinfo(userinfos_);//重新赋值
    }else {
        console.log("user_xh__"+user_xh+"__userinfos__"+userinfos_)
    }
    return false;
}



//添加或者修改某个案件人 1、添加 2修改
function tr_addOrUpdate(obj,type) {
    var dquser_xh= null;//当前序号
    var dq_userinfo=null;//当前用户信息
    if (isNotEmpty(type)&&type==2) {
         dquser_xh= $(obj).parents("tr").find("td:eq(0)").text().trim();
    }
      if (isNotEmpty(dquser_xh)&&isNotEmpty(userinfos_)){
          for (let i = 0; i < userinfos_.length; i++) {
              const userinfo = userinfos_[i];
              if (null!=userinfo.xh&&userinfo.xh==dquser_xh){
                  dq_userinfo=userinfo;
                  dquserssid=userinfo.ssid;
              }
          }
      }


      layui.use('form', function(){
          var form = layui.form;
          layer.open({
              type: 1,
              title:'人员基本信息',
              content:open_HTML,
              area: ['50%', '680px'],
              btn: ['确定','取消'],
              success:function(layero, index){
                  layero.addClass('layui-form');//添加form标识
                  layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                  form.render();

                  getNationalitys();
                  getNationals();
                  getCards();
              },
              yes:function(index, layero){
                  var cardtypetext = $("#cards option:selected").text();
                  var nationality = $("#nationality option:selected").text();//国籍
                  form.verify({
                      cardnum:function (value) {
                          if (!(/\S/).test(value)) {
                              return "请输入证件号码"
                          }
                          if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
                              var checkidcard_bool=checkByIDCard(value);
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
                      }
                  });


                  form.on("submit(fromContent)", function (data) {
                      var username=$("#username").val();
                      var cardnum=$("#cardnum").val();
                      if (!isNotEmpty(cardnum)){
                          layer.msg("证件号码不能为空",{icon: 5});
                          return;
                      }
                      if (!isNotEmpty(username)){
                          layer.msg("姓名不能为空",{icon: 5});
                          return;
                      }

                      if ($.trim(cardtypetext) == "居民身份证" &&($.trim(nationality)=="中国"||!isNotEmpty(nationality))) {
                          //回填身份证分析数据
                          $("#both").val(getAnalysisIdCard(cardnum,1));
                          $("#sex").val(getAnalysisIdCard(cardnum,2));
                          $("#age").val(getAnalysisIdCard(cardnum,3));
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



                       //判断用户是否存在：
                      if (isNotEmpty(userinfos_)) {
                          for (let i = 0; i < userinfos_.length; i++) {
                              const userinfo = userinfos_[i];
                              if (isNotEmpty(userinfo)){
                                  var old_cardtypessid=userinfo.cardtypessid;
                                  var old_cardnum=userinfo.cardnum;
                                  var old_userssid=userinfo.ssid;
                                  if (type==2){
                                      if ((cardtypessid==old_cardtypessid&&cardnum==old_cardnum&&userinfo.xh!=dquser_xh||(isNotEmpty(dquserssid)&&dquserssid==old_userssid&&dquserssid!=dq_userinfo.ssid))) {
                                          layer.msg("该用户已存在，请勿重复添加",{icon: 5});
                                          return;
                                      }
                                  }else if (type==1){
                                      if ((cardtypessid==old_cardtypessid&&cardnum==old_cardnum)||(isNotEmpty(dquserssid)&&dquserssid==old_userssid)) {
                                          layer.msg("该用户已存在，请勿重复添加",{icon: 5});
                                          return;
                                      }
                                  }
                              }
                          }
                      }



                      var  addUserInfo={
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
                          workunits:workunits,
                          cardtypename:cardtypetext,
                          ssid:dquserssid
                      }




                      //存在用户找到这一行修改数据并显示
                      if (type==2){
                          for (let i = 0; i < userinfos_.length; i++) {
                              const userinfo = userinfos_[i];
                              if (null!=userinfo.xh&&userinfo.xh==dquser_xh){
                                  addUserInfo["ssid"]=dquserssid;
                                  userinfos_[i]=addUserInfo;
                              }
                          }
                      } else if (type==1){
                          userinfos_.push(addUserInfo);
                      }

                      set_userinfo(userinfos_);
                      layer.close(index);
                  });
              },
              btn2:function(index, layero){
                  layer.close(index);
              }
          });

          if (isNotEmpty(dq_userinfo)&&isNotEmpty(dquser_xh)){
              var time1= setInterval(function () {
                  var selects=$("#cards option").length;//查看身份证下拉是否有数据
                  if (null!=selects&&selects>0){
                      //回显用户信息
                      var userinfo=dq_userinfo;
                      $("#cards").val(userinfo.cardtypessid);
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

                      clearInterval(time1);
                      form.render();
                  }
              },200)

          }
      });
    return false;
}


function addOrUpdateCase() {
    var url=null;
    if (isNotEmpty(ssid)){
        url=getActionURL(getactionid_manage().addOrUpdateCase_updateCase);
    }else{
        url=getActionURL(getactionid_manage().addOrUpdateCase_addCase);
    }
    var casename=$("#casename").val();
    var cause=$("#cause").val();
    var casenum=$("#casenum").val();
    var occurrencetime=$("#occurrencetime").val();
    var starttime=$("#starttime").val();
    var endtime=$("#endtime").val();
    var caseway=$("#caseway").val();
    var userssid=$("#userssid").val();


    if (!isNotEmpty(casename)) {
        layer.msg("请输入案件名称");
        $("#casename").focus();
        return;
    }
    if (!isNotEmpty(userinfos_)){
        layer.msg("至少添加一个案件人",{icon:5});
        $("#casename").focus();
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid:ssid,
            casename:casename,
            cause:cause,
            casenum:casenum,
            occurrencetime:occurrencetime,
            starttime:starttime,
            endtime:endtime,
            caseway:caseway,
            userssid:userssid,
            userInfos:userinfos_
        }
    };
    ajaxSubmitByJson(url,data,callbackaddOrUpdateCase);
}
function callbackaddOrUpdateCase(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("保存成功",{icon: 6,time:500},function () {
                    var nextparam=getAction(getactionid_manage().addOrUpdateCase_updateCase);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_CLIENT,nextparam.nextPageId);
                        var url=getActionURL(getactionid_manage().main_tocaseIndex);
                        window.location.href=url;
                    }
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function getUserinfoList() {
    var url=getActionURL(getactionid_manage().addOrUpdateCase_getUserinfoList);
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

function getUserByCard(){

    var cardnum =  $("#cardnum").val();
    var cardtypetext=$("#cards option:selected").text();
    var cardtypesssid=$("#cards option:selected").val();



    dquserssid=null;//当前用户的ssid

    var form=layui.form;



    var url=getActionURL(getactionid_manage().addOrUpdateCase_getUserByCard);

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

                //回填身份证分析数据
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





        }
    }else{
      /*  parent.layer.msg(data.message,{icon: 5});*/
    }
    layui.use('form', function(){
        var $ = layui.$;
        var form = layui.form;
        form.render();
    });
}


/**
 * 获取国籍
 */
function getNationalitys(){
    var url=getActionURL(getactionid_manage().addOrUpdateCase_getNationalitys);
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
        parent.layer.msg(data.message,{icon: 5});
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
    var url=getActionURL(getactionid_manage().addOrUpdateCase_getNationals);
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
        parent.layer.msg(data.message,{icon: 5});
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
    var url=getActionURL(getactionid_manage().addOrUpdateCase_getCards);
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
                $("#cards").append("<option value='"+l.ssid+"' title='"+l.typename+"'> "+l.typename+"</option>");
                $("#tab_content select[name='tab_card']").each(function(){
                    $(this).append("<option value='"+l.ssid+"'  title='"+l.typename+"'> "+l.typename+"</option>");
                });
            }
        }
    }else{
        parent.layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

$(function () {
    layui.use(['form','jquery','laydate'], function() {
        var form=layui.form;

        form.on('select(change_card)', function(data){
            getUserByCard();
            form.render('select');
        });
    });

});




function reset() {
    dquserssid=null;//当前用户的ssid
    layui.use(['form','laydate'], function(){
        var form=layui.form;
        var laydate=layui.laydate;
        $("#user_form input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#modelssid')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
        $('select').not("#cards").prop('selectedIndex', 0);
        form.render('select');
    });
}


    var open_HTML='<form class="layui-form layui-row" style="margin: 10px" id="user_form">\
    <div class="layui-col-md12" >\
        <div class="layui-card" >\
            <div class="layui-card-header">人员基本信息\
            <i class="layui-icon layui-icon-template " id="getCardreader_btn" onclick="getIDCardreader();" title="点击开始读卡" style="position: absolute;right: 15px;color: #1E9FFF;cursor: pointer"></i>\
            </div>\
        <div class="layui-card-body  layui-row layui-col-space10"  >\
        <div class="layui-col-lg6">\
        <label class="layui-form-label">证件类型</label>\
        <div class="layui-input-block">\
        <select name="cards" lay-verify="required" id="cards"  lay-filter="change_card">\
        <option value=""></option>\
        </select>\
        </div>\
        </div>\
        <div class="layui-col-lg6">\
        <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>证件号码</label>\
    <div class="layui-input-block">\
        <div class="layui-form-select">\
        <div class="layui-select-title">\
        <input type="text" name="cardnum" lay-verify="cardnum" placeholder="请输入证件号码" autocomplete="off" class="layui-input" id="cardnum" oninput="getUserinfoList()"  onblur="select_cardnumblur();" onfocus="getUserinfoList()" onkeyup="value=value.replace(/[\\W]/g,\'\')"> \
         <i class="layui-icon layui-icon-search" style="position: absolute;top:8px;right:  8px;" onclick="getUserByCard();"></i>\
        </div>\
        <dl class="layui-anim layui-anim-upbit"  id="cardnum_ssid"   >\
        </dl>\
        </div>\
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
        <div class="layui-col-lg6">\
        </div>\
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
        </div></form>';

