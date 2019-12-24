/**
 * 归属页面：addCaseToUser
 */
function getBaseData() {
    var url=getActionURL(getactionid_manage().addCaseToUser_getBaseData);
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

            //全部国籍
            $('#nationality option').not(":lt(1)").remove();
            if (isNotEmpty(nationalityList)) {
                for (var i = 0; i < nationalityList.length; i++) {
                    var l = nationalityList[i];
                    $("#nationality").append("<option value='"+l.ssid+"' title='"+l.enname+"'> "+l.zhname+"</option>");
                }
            }
            //全部民族
            $('#national option').not(":lt(1)").remove();
            if (isNotEmpty(nationalList)) {
                for (var i = 0; i < nationalList.length; i++) {
                    var l = nationalList[i];
                    $("#national").append("<option value='"+l.ssid+"' title='"+l.nationname+"'>"+l.nationname+"</option>");
                }
            }

            //全部工作单位
            $("#otherworkname_text").html("");
            if (isNotEmpty(workunitList)){
                workunits=workunitList;
                for (let i = 0; i < workunitList.length; i++) {
                    const l = workunitList[i];
                    $("#otherworkname_text").append("<dd lay-value='"+l.ssid+"' >"+l.workname+"</dd>");
                }
            }

            //全部用户
            $('#otheruserinfos option').not(":lt(1)").remove();
            $('#recordadmin option').not(":lt(1)").remove();
            if (isNotEmpty(adminList)){
                otheruserinfos=adminList;
                for (var i = 0; i < adminList.length; i++) {
                    var u= adminList[i];
                    if (u.ssid!=sessionadminssid&&isNotEmpty(sessionadminssid)) {
                        $("#otheruserinfos").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                        $("#recordadmin").append("<option value='"+u.ssid+"' >"+u.username+"</option>");
                    }
                }
            }

            //全部证件类型
            $('#cards').html("");
            $("#tab_content select[name='tab_card']").html("");
            if (isNotEmpty(cardtypeList)){
                cards=cardtypeList;
                for (var i = 0; i < cardtypeList.length; i++) {
                    var l = cardtypeList[i];
                    $("#cards").append("<option value='"+l.ssid+"' title='"+l.typename+"'> "+l.typename+"</option>");
                    $("#tab_content select[name='tab_card']").each(function(){
                        $(this).append("<option value='"+l.ssid+"'  title='"+l.typename+"'> "+l.typename+"</option>");
                    });
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


