
//案件================================================================================================================start
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
var dqgetCaseByIdUrl=null;//当前url地址多出调用
function getCaseById(url) {
    if (isNotEmpty(url)) {
        dqgetCaseByIdUrl=url;
    }else if (isNotEmpty(dqgetCaseByIdUrl)){
        url=dqgetCaseByIdUrl;
    }
    var data={
        token:INIT_CLIENTKEY,
        param:{
            userssid:dquserssid,
        }
    };
    ajaxSubmitByJson(dqgetCaseByIdUrl,data,callbakegetCaseById) ;
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

//根据案件编号回查
var cases=null;//我的全部案件数据
var othercases=null;//除开自己全部的案件信息
var casenum_userinfos=[];//根据案件编号回填的多角色人员
var casenum_case;////根据案件编号回填的案件ssid
function getCasesByCasenum(url) {
    var casenum=$("#casenum").val();
    if (isNotEmpty(casenum)){
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
                $("input:not('#casenum'):not('#wordssid'):not('#modelssid'):not('#startrecord_btn'):not('#startrecord_btn2')").val("");
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
                                    userinfogrades[""+usergradessid+""]=userinfo;
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

//案件================================================================================================================end




//案件类型获取:需要传入url=============================================================================================start
function getRecordtypes(url) {
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
//案件类型获取========================================================================================================end


//会议场景获取:需要传入url=============================================================================================start
var modelList=null;//全部模板列表
var dqmodelssid=null;//当前所选的会议模板ssid
var model_index=null;//模板index
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
                    var modeltypename=model.base_modeltype==null?'未知':(model.base_modeltype.modeltypename==null?'':model.base_modeltype.modeltypename);
                    var html="<tr>\
                                       <td>"+(i+1)+"</td>\
                                       <td>"+meetingtypehtml+"</td>\
                                        <td>"+modeltypename+"</td>\
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
                        $("#modelssid2").val(model.explain);
                        dqmodeltypenum=model.modeltypenum;
                    }
                }
            }
        }
    }else {
        console.log("获取会议模板"+data.message)
    }
    set_said(dqmodeltypenum)
}
function select_Model(ssid,explain){
    if (isNotEmpty(ssid)){
        dqmodelssid=ssid;
        if (isNotEmpty(modelList)){
            for (let i = 0; i < modelList.length; i++) {
                const model = modelList[i];
                if (ssid==model.ssid) {
                    explain=model.explain;
                    dqmodeltypenum=model.modeltypenum;
                }
            }
        }
        $("#modelssid").val(explain);
        $("#modelssid2").val(explain);
        layer.close(model_index);
    }
    set_said(dqmodeltypenum)
}
function open_model(){
    var html= '<table class="layui-table"  lay-skin="nob" style="margin-bottom: 30px;">\
       <thead>\
        <tr>\
        <th>序号</th>\
        <th>会议类型</th>\
       <th>模板类型</th>  \
        <th>模板说明</th>\
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

//获取默认会议：需要传入url
function getDefaultMtModelssid(url){
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
    getMc_model();//默认的到获取全部
}
//会议场景获取:需要传入url=============================================================================================end


//笔录word模板:需要传入url=============================================================================================start
var wordList=null;//全部模板列表
var dqwordssid=null;//当前笔录模板ssid
var model_index2=null;
function getWordTemplates(url){
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
//笔录word模板获取:需要传入url=============================================================================================start


//*****************************************************追加“；”**************************************************start
function adduser(type) {
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
        layer.msg("可直接填写人员名称");
    }
}
//*****************************************************追加“；”**************************************************end


//********************************************排列非第一的人员添加*****************************************start
//收集人员数据：其余人员
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

            //用户为回填的信息加入回填数据
            var casenum=$("#casenum").val();
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
//********************************************排列非第一的人员添加*****************************************end

//********************************************base基础数据获取*****************************************start
function getBaseData(url) {
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetgetBaseData);
}
function callbackgetgetBaseData(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var adminList=data.adminList;//全部用户
            var nationalityList=data.nationalityList;//全部国籍
            var nationalList=data.nationalList;//全部民族
            var workunitList=data.workunitList;//全部工作单位
            var cardtypeList=data.cardtypeList;//全部的证件类型


            //全部民族
            $('#national option').not(":lt(1)").remove();
            if (isNotEmpty(nationalList)) {
                for (var i = 0; i < nationalList.length; i++) {
                    var l = nationalList[i];
                    $("#national").append("<option value='"+l.ssid+"' title='"+l.nationname+"'>"+l.nationname+"</option>");
                }
            }

            //全部用户
            $('#otheradminssid option').not(":lt(1)").remove();
            $('#recordadminssid option').not(":lt(1)").remove();
            $('#presidingjudge option').not(":lt(1)").remove();
            $('#judges option').not(":lt(1)").remove();
            if (isNotEmpty(adminList)){
                otheruserinfos=adminList;
                for (var i = 0; i < adminList.length; i++) {
                    var u= adminList[i];
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
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}
//********************************************base基础数据获取*****************************************start


