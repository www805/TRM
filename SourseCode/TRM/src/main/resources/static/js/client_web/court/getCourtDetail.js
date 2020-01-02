var recorduser=[];//会议用户集合

var mtssid=null;//当前会议的ssid
var videourl=null;//视频地址

var recordnameshow="";


var iid=null;//打包iid

var getRecordById_data=null;

var first_playstarttime=0;//第一个视频的开始时间
var dq_play=null;//当前视频数据
var recordPlayParams=[];//全部视频数据集合

var  mouseoverbool_left=-1;//是否滚动-1滚1不滚
var  mouseoverbool_right=-1;//同上

var phSubtracSeconds=0;//身心回放排行榜
var phdatabackList=null;//身心回放数据


//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getCourtDetail_getRecordById);
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
            iid=data.iid;
            recordnameshow=record.recordname;//当前笔录名称

            if (isNotEmpty(record)){
                positiontime=record.positiontime==null?0:record.positiontime;
                $("#positiontime").val(parseFloat(positiontime)/1000);

                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }

                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname).attr("title",record.recordname==null?"笔录标题":record.recordname);
                $("#recorddetail_strong").html('【笔录问答】<i class="layui-icon layui-icon-edit" style="font-size: 20px;color: red;visibility: hidden" title="编辑" id="open_recordqw" onclick="open_recordqw()"></i>');

                //会议人员
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recorduser=[];
                    if (gnlist.indexOf(FY_T)!= -1){
                        //法院：人员从拓展表获取
                        var usergrades=recordUserInfosdata.usergrades;
                        if (isNotEmpty(usergrades)) {
                            for (let i = 0; i < usergrades.length; i++) {
                                const other = usergrades[i];
                                var user={
                                    username:other.username
                                    ,userssid:other.userssid
                                    ,grade:other.grade
                                    ,gradename:other.gradename
                                    ,gradeintroduce:other.gradeintroduce
                                };
                                recorduser.push(user);
                            }
                        }
                    }else {
                        var user1={
                            username:recordUserInfosdata.username
                            ,userssid:recordUserInfosdata.userssid
                            ,grade:2
                        };
                        var user2={
                            username:recordUserInfosdata.adminname
                            ,userssid:recordUserInfosdata.adminssid
                            ,grade:1
                        };

                        recorduser.push(user1);
                        recorduser.push(user2);
                    }
                }

                //案件信息
                var case_=record.case_;
                $("#caseAndUserInfo_html").html("");
                if (isNotEmpty(case_)){
                    //显示编辑按钮
                    var casebool=case_.casebool;
                    if (isNotEmpty(casebool)&&(casebool==0||casebool==1)){
                        $("#open_recordqw").css("visibility","visible");
                    }

                    var casename=case_.casename==null?"":case_.casename;
                    var username=recordUserInfosdata.username==null?"":recordUserInfosdata.username;
                    var cause=case_.cause==null?"":case_.cause;
                    var starttime=case_.starttime==null?"":case_.starttime;
                    var casenum=case_.casenum==null?"":case_.casenum;
                    var adminname=recordUserInfosdata.adminname==null?"":recordUserInfosdata.adminname;
                    var otheradminname=recordUserInfosdata.otheradminname==null?"":recordUserInfosdata.otheradminname;
                    var recordadminname=recordUserInfosdata.recordadminname==null?"":recordUserInfosdata.recordadminname;
                    var department=case_.department==null?"":case_.department;
                    var recordtypename=record.recordtypename==null?"":record.recordtypename;
                    var userInfos=case_.userInfos;
                    var USERHTNL="";
                    if(null!=userInfos) {for (let i = 0; i < userInfos.length; i++) {const u = userInfos[i];USERHTNL += u.username + "、";} USERHTNL = (USERHTNL .substring(USERHTNL .length - 1) == '、') ? USERHTNL .substring(0, USERHTNL .length - 1) : USERHTNL ;}
                    var  init_casehtml="<tr><td style='width: 40%'>案件名称</td><td>"+casename+"</td></tr>\
                                  <tr><td>嫌疑人</td><td>"+USERHTNL+"</td> </tr>\
                                  <tr><td>当前案由</td><td title='"+cause+"'>"+cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+starttime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>";
                    $("#caseAndUserInfo_html").html(init_casehtml);
                    var usergrades=recordUserInfosdata.usergrades;
                    if (isNotEmpty(usergrades)) {
                        var newarr = [];//新数组
                        for(var i=0;i<usergrades.length;i++){
                            var bool=true;
                            for(var j=0;j<newarr.length;j++){
                                if((isNotEmpty(usergrades[i].grade)&&isNotEmpty(newarr[j].grade)&& usergrades[i].grade==newarr[j].grade)){
                                    newarr[j].username=newarr[j].username+"、"+ usergrades[i].username;
                                    bool=false;
                                }
                            };
                            if (bool){
                                newarr.push(usergrades[i]);
                            }
                        };

                        for (let i = 0; i < newarr.length; i++) {
                            const  other= newarr[i];
                            $("#caseAndUserInfo_html").append("<tr type='"+other.grade+"'><td>"+other.gradename+"</td><td>"+other.username+"</td> </tr>");
                        }
                    }
                }
            }

            var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().getCourtDetail_getRecordrealByRecordssid);
            getRecordrealByRecordssid(getRecordrealByRecordssidUrl);//右侧数据
            setInterval( function() {
                var setRecordrealUrl=getActionURL(getactionid_manage().getCourtDetail_setRecordreal);
                setRecordreal(setRecordrealUrl);
            },3000);

            //左侧asr识别数据
            var getMCVO=data.getMCVO;
            if (isNotEmpty(getMCVO)&&isNotEmpty(getMCVO.list)){
                set_getRecord(getMCVO.list,2);
                $("#asr").show();
            }else  {
                $("#recordreals").html('<div id="datanull_3" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无语音对话...可能正在生成中请稍后访问</div>');
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                set_getPlayUrl(getPlayUrlVO);
            }else {
                //此处加入定时器
                if (isNotEmpty(iid)){
                    getplayurl_setinterval= setInterval(function () {
                        getPlayUrl();
                    },5000)
                }
                $("#videos").html('<div id="datanull_1" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无视频...可能正在生成中请稍后访问</div>');
            }


            //提讯数据
            var police_arraignment=record.police_arraignment;
            if (isNotEmpty(police_arraignment)){
                mtssid=police_arraignment.mtssid;//获取会议mtssid
                if (!isNotEmpty(mtssid)) {
                    //不存在会议
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//数据渲染
/*function set_getRecord(data){
    if (isNotEmpty(data.list)){
        $("#recordreals").empty();
        if (gnlist.indexOf(NX_O)!= -1){
            $("#open_tagtext").show();
        }
        $("#recordreals_selecthtml").show();
        var list=data.list;
        for (var i = 0; i < list.length; i++) {
            var data=list[i];
            if (isNotEmpty(recorduser)){
                for (var j = 0; j < recorduser.length; j++) {
                    var user = recorduser[j];
                    var userssid=user.userssid;
                    if (data.userssid==userssid){
                        var username=user.username==null?"未知":user.username;//用户名称
                        var usertype=user.grade;//1、询问人2被询问人
                       /!* var txt=data.txt==null?"...":data.txt;//翻译文本*!/
                        var translatext=data.tagtext==null?data.txt:data.tagtext;//需要保留打点标记的文本
                        var asrtime=data.asrtime;//时间
                        var starttime=data.starttime;
                        var asrstartime=data.asrstartime;
                        var subtractime_=data.subtractime;//时间差
                        //实时会议数据
                        var recordrealshtml="";
                       // var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本
                        var gradename=user.gradename==null?"未知":user.gradename;
                        subtractime[""+usertype+""]=subtractime_;//存储各个类型人员的时间差值

                        starttime=parseFloat(starttime)+parseFloat(subtractime_);
                        var color=asrcolor[usertype]==null?"#0181cc":asrcolor[usertype];
                        var fontcolor="#ffffff";
                        if (gnlist.indexOf(NX_O)!= -1){
                            color="#ffffff";
                            fontcolor="#000000";
                            recordrealshtml='<div style="margin:10px 0px" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+')" times='+starttime+'>\
                                                            <span style="background-color: '+color+';color: '+fontcolor+';font-size:13.0pt;">'+gradename+'： '+translatext+'</span>\
                                                      </div >';
                        }else {
                            recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+')" times='+starttime+'>\
                                                            <p>【'+gradename+'】 '+asrstartime+'</p>\
                                                            <span style="background-color: '+color+';color:'+fontcolor+';">'+translatext+'</span> \
                                                      </div >';
                        }


                        var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                        if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                            $("#recordreals div[userssid="+userssid+"]:last").remove();
                        }
                        $("#recordreals").append(recordrealshtml);
                        var div = document.getElementById('recordreals');
                        div.scrollTop = div.scrollHeight;
                    }
                }
            }
        }

        var recordreals_selecthtml=document.getElementById("recordreals_selecthtml");
        var IHTML='<span class="layui-table-sort layui-inline" title="语音识别可滚动"><i class="layui-edge layui-table-sort-asc"></i><i class="layui-edge layui-table-sort-desc" "></i></span>';
        if(recordreals_selecthtml.scrollHeight>recordreals_selecthtml.clientHeight||recordreals_selecthtml.offsetHeight>recordreals_selecthtml.clientHeight){
            $("#webkit2").html(IHTML)
        }else {
            $("#webkit2").empty();
        }
    }else {
        $("#recordreals").html('<div id="datanull_3" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无语音对话...可能正在生成中请稍后访问</div>');
    }
    //存在问答需要获取时间差
    var getRecordrealByRecordssidUrl=getActionURL(getactionid_manage().getCourtDetail_getRecordrealByRecordssid);
    getRecordrealByRecordssid(getRecordrealByRecordssidUrl);//右侧数据
}*/

$(function () {
    $("#baocun").click(function () {
      addRecord();
    });

    //定位差值
    $("#positiontime").click(function () {
        var url=getActionURL(getactionid_manage().getCourtDetail_updateRecord);
        open_positiontime(url);
    });

    //导出
    $("#dc_li dd").click(function () {
        layer.msg("导出中，请稍等...", { icon: 16, shade: [0.1, 'transparent'],time:6000});
        var type=$(this).attr("type");
        if (type==1){
            var exporttemplate_ueUrl=getActionURL(getactionid_manage().getCourtDetail_exporttemplate_ue);
            exporttemplate_ue(1,exporttemplate_ueUrl);
        }else  if(type==2){
            var exporttemplate_ueUrl=getActionURL(getactionid_manage().getCourtDetail_exporttemplate_ue);
            exporttemplate_ue(2,exporttemplate_ueUrl);
        }else  if(type==3){
            export_asr();
        }
    });

});

function open_recordqw() {
    $("#wqutil").show();//显示保存按钮
    ue.setEnabled();
}



//保存按钮
//recordbool 1进行中 2已结束  0初始化
var overRecord_index=null;
function addRecord() {
    var setRecordrealUrl=getActionURL(getactionid_manage().getCourtDetail_setRecordreal);
    setRecordreal(setRecordrealUrl);

    if (isNotEmpty(overRecord_index)) {
        layer.close(overRecord_index);
    }
    overRecord_loadindex = layer.msg("保存中，请稍等...", {typy:1, icon: 16,shade: [0.1, 'transparent'], time:10000 });
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getCourtDetail_addRecord);
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
            $("#recorddetail #record_qw").css({"width":"80%"});
            $("#recorddetail #record_util,#btnadd").css({"display":"none"});
            $("#recorddetail label[name='q'],label[name='w']").attr("contenteditable","false");
            $("#wqutil").hide();
            ue.setDisabled();
            layer.msg('保存成功',{icon:6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//导出左侧语音识别
function export_asr() {
    if (isNotEmpty(recordssid)) {
        var url=getActionURL(getactionid_manage().getCourtDetail_export_asr);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
            }
        };
        ajaxSubmitByJson(url,paramdata,callbackexport_asr);
    }
    layer.msg("导出中，请稍等...", {
        icon: 16,
        shade: [0.1, 'transparent'],
        time:6000
    });
    $("div[name='btn_div']").attr("showorhide","false");
    $("div[name='btn_div']").removeClass("layui-form-selected");
}
function callbackexport_asr(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var downurl=data.downurl;//word导出地址
            if (isNotEmpty(downurl)) {
                var $a = $("<a></a>").attr("href", downurl).attr("download", "down");
                $a[0].click();
                layer.msg('语音识别内容导出成功,等待下载中...',{icon:6});
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//打点标记目录
var TAGTEXT_INDEX=null;
function open_tagtext() {
    var TAGTEXT_HTML='<div class="layui-row layui-form " > \
        <div class="layui-col-md12 layui-form-item" style="margin-top: 10px" >\
         <div class="layui-col-md8 layui-col-md-offset2 layui-input-inline">\
             <div class="layui-col-md9"><input type="text" id="tagtext" lay-verify="tagtext" placeholder="请输入标记内容" autocomplete="off" class="layui-input"  oninput="selecttagtext();"></div>\
             <div class="layui-col-md3"> <input type="button"  class="layui-btn layui-btn-normal" value="搜索" onclick="selecttagtext();" /></div>\
         </div>\
        <table class="layui-table" lay-skin="nob">\
        <colgroup>\
        <col width="30">\
        <col width="120">\
        </colgroup>\
         <tr >\
            <th>角色</th>\
            <th>标记内容</th>\
        </tr>\
        <tbody id="tagtext_html">\
        </tbody>\
        </table>\
         </div>\
      </div>';
    if (TAGTEXT_INDEX==null){
        TAGTEXT_INDEX= layer.open({
            title: '打点目录',
            type: 1,
            content: TAGTEXT_HTML,
            offset:'r', area:["20%", "100%"],
            anim:2,
            shade: 0,
            closeBtn:0,
            btn: [],
            resize:false,
            move: false,
            success: function(layero, index){
                selecttagtext();
            }
        });
    } else {
        layer.close(TAGTEXT_INDEX);
        TAGTEXT_INDEX=null;
    }
}
function selecttagtext() {
    var tagtext=$("#tagtext").val();

    //收集打点目录
    $("#tagtext_html").empty();
    var morenHTML='<tr style="border-bottom: 1px solid #ccc">\
                        <td colspan="3" style="text-align: center">暂无数据</td>\
                        </tr>';
    var HTML="";
    if (isNotEmpty(getRecordById_data)){
        var getMCVO=getRecordById_data.getMCVO;
        if (isNotEmpty(getMCVO)&&isNotEmpty(getMCVO.list)){
            var list=getMCVO.list;
            for (var i = 0; i < list.length; i++) {
              var data=list[i];
                var  reg = /<[^>]+>/g;//检测是否包含html标签
                if (isNotEmpty(data.tagtext)&&isNotEmpty(recorduser)&&reg.test(data.tagtext)){
                    if (!isNotEmpty(tagtext)||(isNotEmpty(tagtext)&&data.tagtext.indexOf(tagtext)>0)) {
                        for (var j = 0; j < recorduser.length; j++) {
                            var user = recorduser[j];
                            var userssid=user.userssid;
                            if (data.userssid==userssid){
                                var username=user.username==null?"未知":user.username;//用户名称
                                var usertype=user.grade;//1、询问人2被询问人
                                var translatext=data.tagtext;//需要保留打点标记的文本
                                var asrtime=data.asrtime;//时间
                                var starttime=data.starttime;
                                var asrstartime=data.asrstartime;
                                var subtractime_=data.subtractime;//时间差
                                var gradename=user.gradename==null?"未知":user.gradename;
                                starttime=parseFloat(starttime)+parseFloat(subtractime_);

                                HTML+='<tr ondblclick="showrecord('+starttime+')">\
                                                     <td >'+gradename+'</td>\
                                                      <td >'+translatext+'</td>\
                                                    </tr>';

                            }
                        }
                    }
                }
            }
        }
    }else {
        layer.msg("数据加载中，请稍后再试")
    }
    console.log("进来啦")
    $("#tagtext_html").html(HTML==""?morenHTML:HTML);

}

