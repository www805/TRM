
//获取软硬件信息
function getSystemInfo() {
    var url=getActionURL(getactionid_manage().softAndHardInfo_getSystemInfo);

    var data = {
        token: INIT_CLIENTKEY,
        param: {}
    };

    ajaxSubmitByJson(url, data, callgetSystemInfo);
}

function callgetSystemInfo(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            if(isNotEmpty(data.data)){
                var sqinfo = data.data.sqParam;
                var softinfo = data.data.softwareParam;
                var hardinfo = data.data.hardwareParam;
                if(null!=sqinfo){
                    dealSQ(sqinfo);
                }
                if(null!=softinfo){
                    dealsoft(softinfo);
                }
                if(null!=hardinfo){
                    dealhard(hardinfo);
                }

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function dealSQ(info){
    var gnHtml = "";

    for(var i =0; i < info.gnlist.length; i++){
        var gn = info.gnlist[i];
        gnHtml += "<span class=\"layui-badge layui-bg-blue\" style=\"margin-right: 5px;\">" + gn + "</span>";
    }

    //设置授权信息
    gnHtml = "<tr>\n" +
        "       <td>授权功能</td>\n" +
        "       <td>" + gnHtml + "</td>\n" +
        "     </tr>\n";

    var tHTML = "<tr>\n" +
        "       <td>服务版本</td>\n" +
        "       <td>" + info.qtlistp.t + "</td>\n" +
        "     </tr>\n";

    var versionHTML = "<tr>\n" +
        "       <td>使用版本</td>\n" +
        "       <td>" + info.qtlistp.version +" - "+ info.qtlistp.banben+ "</td>\n" +
        "     </tr>\n";

    var clientNameHTML = "<tr>\n" +
        "       <td>单位名称</td>\n" +
        "       <td>" + info.sqEntity.clientName + "</td>\n" +
        "     </tr>\n";

    var unitCodeHTML = "<tr>\n" +
        "       <td>单位代码</td>\n" +
        "       <td>" + info.sqEntity.unitCode + "</td>\n" +
        "     </tr>\n";

    var startTimeHTML = "<tr>\n" +
        "       <td>授权开始时间</td>\n" +
        "       <td>" + info.sqEntity.startTime + "</td>\n" +
        "     </tr>\n";

    var foreverBool = info.sqEntity.foreverBool == 0 ? '临时' : '永久';

    var foreverBoolHTML = "<tr>\n" +
        "       <td>授权期限</td>\n" +
        "       <td>" + foreverBool + "</td>\n" +
        "     </tr>\n";

    if(info.sqEntity.foreverBool == 0){
        var sqDayHTML = "<tr>\n" +
            "       <td>授权总天数</td>\n" +
            "       <td>" + info.sqEntity.sqDay + "</td>\n" +
            "     </tr>\n";
        foreverBoolHTML+=sqDayHTML;
    }

    var sortNumHTML = "<tr>\n" +
        "       <td>单位编号</td>\n" +
        "       <td>" + info.sqEntity.sortNum + "</td>\n" +
        "     </tr>\n";

    var codeHTML = "<tr>\n" +
        "       <td>授权序列号</td>\n" +
        "       <td>" + info.sqEntity.cpuCode + "</td>\n" +
        "     </tr>\n";

    var sqinfoHTML = "<tbody>\n" +
        clientNameHTML + unitCodeHTML +codeHTML+ startTimeHTML + foreverBoolHTML + gnHtml + tHTML + versionHTML + sortNumHTML +
        "        </tbody>\n";

    $("#sqinfo").html(sqinfoHTML);
}

function dealsoft(info){

    $("#sysVersion").html(info.sysVersion);
    $("#companyname").html(info.companyname);
    $("#sysStartTime").html(info.sysStartTime);
    $("#workTime").html(info.workTime);

}

function dealhard(info){

    $("#version").html(info.version);
    $("#serialnumber").html(info.serialnumber);
    $("#fdid").html(info.fdid);

}
