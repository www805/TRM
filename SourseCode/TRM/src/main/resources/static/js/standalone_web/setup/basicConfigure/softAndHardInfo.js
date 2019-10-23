
//获取授权信息
function getSQInfo() {
    var url=getActionURL(getactionid_manage().softAndHardInfo_getSQInfo);

    var data = {
        token: INIT_CLIENTKEY,
        param: {}
    };

    ajaxSubmitByJson(url, data, callgetSQInfo);
}

function callgetSQInfo(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            if(isNotEmpty(data.data)){
                var sqinfo = data.data;

                var gnHtml = "";

                for(var i =0; i < sqinfo.gnlist.length; i++){
                    var gn = sqinfo.gnlist[i];
                    gnHtml += "<span class=\"layui-badge layui-bg-blue\" style=\"margin-right: 5px;\">" + gn + "</span>";
                }

                //设置授权信息
                gnHtml = "<tr>\n" +
                    "       <td>授权功能</td>\n" +
                    "       <td>" + gnHtml + "</td>\n" +
                    "     </tr>\n";


                var oemHTML = "<tr>\n" +
                    "       <td>oem</td>\n" +
                    "       <td>" + sqinfo.qtlistp.oem + "</td>\n" +
                    "     </tr>\n";

                var tHTML = "<tr>\n" +
                    "       <td>分支</td>\n" +
                    "       <td>" + sqinfo.qtlistp.t + "</td>\n" +
                    "     </tr>\n";

                var versionHTML = "<tr>\n" +
                    "       <td>版本</td>\n" +
                    "       <td>" + sqinfo.qtlistp.version + "</td>\n" +
                    "     </tr>\n";

                var banbenHTML = "<tr>\n" +
                    "       <td>服务</td>\n" +
                    "       <td>" + sqinfo.qtlistp.banben + "</td>\n" +
                    "     </tr>\n";



                var clientNameHTML = "<tr>\n" +
                    "       <td>单位名称</td>\n" +
                    "       <td>" + sqinfo.sqEntity.clientName + "</td>\n" +
                    "     </tr>\n";

                var unitCodeHTML = "<tr>\n" +
                    "       <td>单位代码</td>\n" +
                    "       <td>" + sqinfo.sqEntity.unitCode + "</td>\n" +
                    "     </tr>\n";

                var startTimeHTML = "<tr>\n" +
                    "       <td>授权开始时间</td>\n" +
                    "       <td>" + sqinfo.sqEntity.startTime + "</td>\n" +
                    "     </tr>\n";

                var foreverBool = sqinfo.sqEntity.foreverBool == 0 ? '临时' : '永久';

                var foreverBoolHTML = "<tr>\n" +
                    "       <td>授权期限</td>\n" +
                    "       <td>" + foreverBool + "</td>\n" +
                    "     </tr>\n";

                var sqDayHTML = "<tr>\n" +
                    "       <td>授权总天数</td>\n" +
                    "       <td>" + sqinfo.sqEntity.sqDay + "</td>\n" +
                    "     </tr>\n";

                var sortNumHTML = "<tr>\n" +
                    "       <td>客户端序号</td>\n" +
                    "       <td>" + sqinfo.sqEntity.sortNum + "</td>\n" +
                    "     </tr>\n";


                if(sqinfo.sqEntity.foreverBool == 1){
                    sqDayHTML = "";
                }

                var sqinfoHTML = "<tbody>\n" +
                    clientNameHTML + unitCodeHTML + startTimeHTML + foreverBoolHTML + sqDayHTML + gnHtml + oemHTML + tHTML + versionHTML + banbenHTML + sortNumHTML +
                    "        </tbody>\n";

                $("#sqinfo").html(sqinfoHTML);

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
