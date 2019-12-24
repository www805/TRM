

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
//案件===================================================================end

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