var recorduser=[];//会议用户集合
var mtssid=null;//当前会议的ssid
var videourl=null;//视频地址

var recordnameshow="";

var subtractime_q=0;//问的时间差
var subtractime_w=0;//答的时间差

var iid=null;//打包iid

var getRecordById_data=null;
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答

var first_playstarttime=0;//第一个视频的开始时间
var dq_play=null;//当前视频数据
var recordPlayParams=[];//全部视频数据集合


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
function setqw(problems) {
    if (isNotEmpty(problems)){
        var problemhtml="";
        $("#recorddetail").empty();
        $("#datanull_2").hide();

        for (var z = 0; z< problems.length;z++) {
            var problem = problems[z];

            var problemstarttime=problem.starttime;
            var q_starttime=problemstarttime
            if (isNotEmpty(problemstarttime)&&problemstarttime!=-1) {
                q_starttime= problemstarttime+parseFloat(subtractime_q);
            }

            var problemtext=problem.problem==null?"未知":problem.problem;
             problemhtml+= '<tr>\
                        <td style="padding: 0;width: 100%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>&nbsp;</span><label name="q" contenteditable="false" times="'+q_starttime+'">'+problemtext+'</label></div>';
            problemhtml+=' <div  id="btnadd" style="display: none;"></div></td>\
                        <td style="display: block" id="record_util"></td></tr>';
        }
        return problemhtml;
    }
    return "";
}
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            getRecordById_data=data;
            var record=data.record;

            recordnameshow=record.recordname;//当前笔录名称

            if (isNotEmpty(record)){
                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }

                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);
                $("#recorddetail_strong").html('【笔录问答】<i class="layui-icon layui-icon-edit" style="font-size: 20px;color: red" title="编辑" onclick="open_recordqw()"></i>');

                //会议人员
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recorduser=[];
                    if (gnlist.indexOf("fy_t")!= -1){
                        //法院：人员从拓展表获取
                        var usergrades=recordUserInfosdata.usergrades;
                        if (isNotEmpty(usergrades)) {
                            for (let i = 0; i < usergrades.length; i++) {
                                const other = usergrades[i];
                                var user={
                                    username:other.username
                                    ,userssid:other.userssid
                                    ,grade:other.grade
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
                                  <tr><td>庭审时间</td> <td>"+starttime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+casenum+"</td> </tr>";
                            var usergrades=recordUserInfosdata.usergrades;
                            if (isNotEmpty(usergrades)) {
                                for (let i = 0; i < usergrades.length; i++) {
                                    const other = usergrades[i];
                                    init_casehtml+="<tr><td>"+other.gradename+"</td><td>"+other.username+"</td> </tr>";
                                }
                            }
                    $("#caseAndUserInfo_html").html(init_casehtml);
                }
            }

            //左侧asr识别数据
            var getMCVO=data.getMCVO;
            if (isNotEmpty(getMCVO)&&isNotEmpty(getMCVO.list)){
                set_getRecord(getMCVO);
                $("#asr_html").show();
            }else  {
                getRecordrealByRecordssid();
                $("#recordreals").html('<div id="datanull_3" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无语音对话...可能正在生成中请稍后访问</div>');
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                set_getPlayUrl(getPlayUrlVO);
            }else {
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
function set_getRecord(data){
    if (isNotEmpty(data.list)){
        $("#recordreals").empty();
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
                        var txt=data.txt==null?"...":data.txt;//翻译文本
                        var asrtime=data.asrtime;//时间
                        var starttime=data.starttime;
                        var asrstartime=data.asrstartime;
                        var subtractime=data.subtractime;//时间差
                        //实时会议数据
                        var recordrealshtml="";
                        var translatext=data.keyword_txt==null?"...":data.keyword_txt;//翻译文本


                        //实时会议数据
                        if (usertype==1){
                            subtractime_q=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_q);
                            recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)" times='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+'</p>\
                                                            <span>'+translatext+'</span> \
                                                      </div >';
                        }else{
                            var color=asrcolor[usertype]==null?"#ef8201":asrcolor[usertype];
                            subtractime_w=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_w);
                            recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)"  times='+starttime+'>\
                                                           <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span style="background-color: '+color+'">'+translatext+'</span> \
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
    getRecordrealByRecordssid();
}

function btn(obj) {
    var selected=$(obj).closest("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $(obj).closest("div[name='btn_div']").attr("showorhide","true");
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $(obj).closest("div[name='btn_div']").attr("showorhide","false");
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}
function exportWord(obj){
        //调用导出方法
        layer.msg("导出中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });
        var url=getActionURL(getactionid_manage().getCourtDetail_exportWord);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
            }
        };
        ajaxSubmitByJson(url, paramdata, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    var word_htmlpath=data.word_htmlpath;//预览html地址
                    var word_path=data.word_path;//下载地址
                    window.location.href = word_path;
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
    btn(obj);
}
function exportPdf(obj) {
        //调用导出方法
        layer.msg("导出中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent']
        });
        var url=getActionURL(getactionid_manage().getCourtDetail_exportPdf);
        var paramdata={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
            }
        };
        ajaxSubmitByJson(url, paramdata, function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                var data=data.data;
                if (isNotEmpty(data)){
                    //window.location.href = data;
                    layer.open({
                        id:"pdfid",
                        type: 1,
                        title: '导出PDF笔录',
                        shadeClose: true,
                        shade: false,
                        maxmin: true, //开启最大化最小化按钮
                        area: ['893px', '600px'],
                    });
                    showPDF("pdfid",data);
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
    btn(obj);
}


var dqindex_realtxt=0;//当前显示的下标
var likerealtxtarr=[];//搜索txt
//搜索上
function last_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt--;
        if (dqindex_realtxt<0){
            dqindex_realtxt=0;
            layer.msg("这是第一个~");
        }
        set_dqrealtxt();
    }
}
//搜索下
function next_realtxt() {
    if (isNotEmpty(likerealtxtarr)) {
        dqindex_realtxt++;
        if (dqindex_realtxt>=likerealtxtarr.length-1){
            dqindex_realtxt=likerealtxtarr.length-1;
            layer.msg("这是最后一个~");
        }
        set_dqrealtxt();
    }
}
//搜索赋值
function set_dqrealtxt(){
    mouseoverbool=1;//不滚动
    if (isNotEmpty(likerealtxtarr)) {
        for (let i = 0; i < likerealtxtarr.length; i++) {
            const all = likerealtxtarr[i];
            all.find("a").removeClass("highlight_dq");
        }
        likerealtxtarr[dqindex_realtxt].find("a").addClass("highlight_dq");
        var top= likerealtxtarr[dqindex_realtxt].closest("div").position().top;
        var div = document.getElementById('recordreals_scrollhtml');
        div.scrollTop = top;
    }
}

function recordreals_select() {
    mouseoverbool=1;//不滚动
    var likerealtxt = $("#recordreals_select").val();
    dqindex_realtxt=0;
    likerealtxtarr=[];

    var recordrealshtml= $("#recordreals").html();
    recordrealshtml=recordrealshtml.replace(/(<\/?a.*?>)/g, '');
    $("#recordreals").html(recordrealshtml);
    $("#recordreals div").each(function (i,e) {
        var spantxt=$(this).find("span").text();
        if (isNotEmpty(likerealtxt)){
            if (spantxt.indexOf(likerealtxt) >= 0) {
                var html=$(this).find("span").html();
                html = html.split(likerealtxt).join('<a class="highlight_all">'+ likerealtxt +'</a>');
                $(this).find("span").html(html);
                likerealtxtarr.push($(this).find("span"));
            }
        }
    });

    if (isNotEmpty(likerealtxtarr)){
        set_dqrealtxt();
    }else {
        /*layer.msg("没有找到内容~");*/
    }
}


$(function () {
    $("#baocun").click(function () {
      addRecord();
    });

    var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
    var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var newDate = new Date();
    newDate.setDate(newDate.getDate());
    var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()];
    setinterval1= setInterval( function() {
        var seconds = new Date().getSeconds();
        var sec=( seconds < 10 ? "0" : "" ) + seconds;
        var minutes = new Date().getMinutes();
        var min=( minutes < 10 ? "0" : "" ) + minutes;
        var hours = new Date().getHours();
        var hour=( hours < 10 ? "0" : "" ) + hours;

    },1000);

    setInterval( function() {
        setRecordreal();//5秒实时保存
    },3000)

    //检测视频是否播完，播完自动进入下一个视频
    SewisePlayer.onPlayTime(function(time, id){
        var totaltime=SewisePlayer.duration()==null?0:SewisePlayer.duration();
        if (parseFloat(time)==parseFloat(totaltime)&&isNotEmpty(dq_play)&&isNotEmpty(recordPlayParams)) {
            var dqfilenum=dq_play.filenum; //1
            if (dqfilenum<recordPlayParams.length){  //3
                dq_play=recordPlayParams[dqfilenum];
                videourl=dq_play.playUrl;
                initplayer();
                //样式跟着改变
                $("#videos span").each(function () {
                    var filenum=$(this).attr("filenum");
                    if (filenum==dq_play.filenum){
                        $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        return false;
                    }
                });
            }
        }


        /!*此处开始定位*!/
        if (isNotEmpty(time)&&time>0){
            var locationtime=time*1000<0?0:time*1000; //秒转时间戳
            locationtime=locationtime+dq_play.recordstarttime+(parseFloat(dq_play.repeattime)*1000)-first_playstarttime;

            //左侧
            var recordrealsdivlen=$("#recordreals div").length;//识别长度
            $("#recordreals div").each(function (i,e) {
                var t=$(this).attr("times");
                var start=t;
                var end=0;
                if (i>=recordrealsdivlen-1) {
                    end= t;//下一个区间
                }else {
                    end= $("#recordreals div:eq("+(i+1)+")").attr("times");//下一个区间
                }
                if (locationtime>=parseFloat(start)&&(parseFloat(start)==parseFloat(end)||locationtime<=parseFloat(end))) {
                    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
                    $("span",this).css("color","#FFFF00 ").addClass("highlight_left");

                    $("#record_hoverhtml").hover(
                        function(){
                            mouseoverbool=1
                        } ,
                        function(){
                            mouseoverbool=-1;
                        });

                    if (parseInt(mouseoverbool)==-1&&parseInt(mouseoverbool)!=1){
                        var top=$(this).position().top;
                        var div = document.getElementById('recordreals_scrollhtml');
                        div.scrollTop = top;
                    }
                    return false;
                }
            });
        }
    });




});





//*******************************************************************案件人员信息编辑start****************************************************************//
var casetouser_iframe=null;
var casetouser_body=null;
function  open_casetouser() {
    layer.open({
        type: 2,
        title:'人员案件基本信息',
        content:tocaseToUserURL,
        area: ['80%', '90%'],
        btn: ['确定','取消'],
        success:function(layero, index){
            casetouser_iframe = window['layui-layer-iframe' + index];
            casetouser_body=layer.getChildFrame('body', index);
            casetouser_iframe.recordssid=recordssid;
            casetouser_iframe.setcaseToUser(getRecordById_data);
        },
        yes:function(index, layero){
            var formSubmit=layer.getChildFrame('body', index);
            var submited = formSubmit.find('#permissionSubmit')[0];
            submited.click();
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}
//*******************************************************************案件人员信息编辑end****************************************************************//

//*******************************************************************笔录问答编辑start****************************************************************//
function open_recordqw() {
    //切换界面
    $("#recorddetail #record_qw").css({"width":"100%"});
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


    $("#recorddetail label[name='q'],label[name='w']").keydown(function () {
        qw_keydown(this,event);
    })
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
    var btnhtml='<button type="button"  class="layui-btn layui-btn-warm" style="border-radius: 50%;width: 45px;height: 45px;padding:0px"  title="添加一行" onclick="focuslable(trtd_html,2,\'q\');"><i class="layui-icon" style="font-size: 45px" >&#xe608;</i></button>';
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
            console.log("回车")
            if (trlength==(trindex+1)){//最后一行答直接追加一行问答
                focuslable(trtd_html,1,'q');
            } else {
                lable=$('#recorddetail tr:eq("'+(trindex+1)+'") label[name="q"]');//定位到下一行的问
                setFocus(lable);
            }
            event.preventDefault();
            break;
        case 38:
            console.log("上一句")
            var index=(trindex-1)<=0?0:(trindex-1);
            if(trindex!=0){
                lable=$('#recorddetail tr:eq("'+index+'") label[name="q"]');
                setFocus(lable);
            }
            event.preventDefault();
            break;
        case 40:
            console.log("下一句")
            var index=(trindex+1)>=trlength?trlength:(trindex+1);
            lable=$('#recorddetail tr:eq("'+index+'") label[name="q"]');
            setFocus(lable);
            break;
        default: break;
    }
}




/*笔录实时保存*/
function setRecordreal() {

    var url=getActionURL(getactionid_manage().getCourtDetail_setRecordreal);

    var recordToProblems=[];//题目集合
    $("#recorddetail td.onetd").each(function (i) {
        var arr={};
        var answers=[];//答案集合
        var q=$(this).find("label[name='q']").html();
        var q_starttime=$(this).find("label[name='q']").attr("times");
        //经过筛选的q
        var ws=$(this).find("label[name='w']");
        var w_starttime=$(this).find("label[name='w']").attr("times");
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
    var url=getActionURL(getactionid_manage().getCourtDetail_getRecordrealByRecordssid);
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


//保存按钮
//recordbool 1进行中 2已结束    0初始化 -1导出word -2导出pdf
var overRecord_index=null;
function addRecord() {
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
            layer.msg('保存成功',{icon:6});
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
function sortPlayUrl(a, b) {
    return a.filenum - b.filenum;//由低到高
}
function  set_getPlayUrl(data) {
    if (isNotEmpty(data)){
        iid=data.iid;
        var recordFileParams=data.recordFileParams;
        recordPlayParams=data.recordPlayParams;
        var state;
        $("#videos").empty();
        if (isNotEmpty(recordFileParams)&&isNotEmpty(recordPlayParams)){
            recordPlayParams.sort(sortPlayUrl);//重新排序一边
            dq_play=recordPlayParams[0];
            first_playstarttime=parseFloat(dq_play.recordstarttime);
            var oldname=[];
            for (let i = 0; i < recordPlayParams.length; i++) {
                var play=recordPlayParams[i];
                var playname=play.filename;
                for (let j = 0; j < recordFileParams.length; j++) {
                    const file = recordFileParams[j]
                    var filename= file.filename;
                    if (filename==playname&&oldname.indexOf(filename)<0) {
                        var VIDEO_HTML = '<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="' + play.filenum + '"  state="' + file.state + '">视频' + play.filenum + '</span>';
                        $("#videos").append(VIDEO_HTML);
                        play["start_range"] = parseFloat(play.recordstarttime) - parseFloat(first_playstarttime);
                        play["end_range"] = parseFloat(play.recordendtime) - parseFloat(first_playstarttime);
                        recordPlayParams[i] = play;
                        //时间毫秒区域计算结束------
                        oldname.push(filename);
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
    }else {
        $("#videos").html('<div id="datanull_1" style="font-size: 18px; text-align: center; margin: 10px;color: rgb(144, 162, 188)">暂无视频...可能正在生成中请稍后访问</div>');
    }
}

//视频进度
function showrecord(times,oldtime) {
    $("#recorddetail label").removeClass("highlight_right");
    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
    times=parseFloat(times);
    if (isNotEmpty(times)&&times!=-1&&first_playstarttime!=0&&isNotEmpty(dq_play)&&isNotEmpty(recordPlayParams)){
        var isnvideo=0;//是否有视频定位点
        //检测点击的时间戳是否在当前视频中，不在切换视频并且定位
        for (let i = 0; i < recordPlayParams.length; i++) {
            const recordPlayParam = recordPlayParams[i];
            var start_range=recordPlayParam.start_range;
            var end_range=recordPlayParam.end_range;
            if (parseFloat(times)>=parseFloat(start_range)&&parseFloat(times)<=parseFloat(end_range)) {
                if (dq_play.filenum==recordPlayParam.filenum){
                    var  locationtime=(first_playstarttime+times)-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);
                    locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                    changeProgrss(parseFloat(locationtime));
                } else {
                    //赋值新视频,计算新的时间
                    dq_play=recordPlayParam;
                    videourl=dq_play.playUrl;
                    var locationtime=(first_playstarttime+times)-parseFloat(dq_play.recordstarttime)-(parseFloat(dq_play.repeattime)*1000);//重新计算时间
                    locationtime=locationtime/1000<0?0:locationtime/1000; //时间戳转秒
                    initplayer(parseFloat(locationtime));

                    //样式跟着改变
                    $("#videos span").each(function () {
                        var filenum=$(this).attr("filenum");
                        if (filenum==dq_play.filenum){
                            $(this).removeClass("layui-bg-gray").addClass("layui-bg-black").siblings().addClass("layui-bg-gray");
                        }
                    });
                }
                isnvideo++;
            }
        }
        if (isnvideo==0){
            layer.msg("没有找到视频定位点",{time:500})
        }


        var recorddetailtrlen= $("#recorddetail label").length;
        $("#recorddetail label").each(function (i,e) {
            var t1=$(this).attr("times");
            var name=$(this).attr("name");
            if (t1==times) {//需要减去差值
                $(this).addClass("highlight_right");
                var top=$(this).position().top;
                var div = document.getElementById('recorddetail_scrollhtml');
                div.scrollTop = top;
                return false;
            }
        });

        $("#recordreals div").each(function (i,e) {
            var t2=$(this).attr("times");
            if (t2==times) {
                $("span",this).css("color","#FFFF00 ").addClass("highlight_left");
                var top=$(this).position().top;
                var div = document.getElementById('recordreals_scrollhtml');
                div.scrollTop = top;
                return false;
            }
        });
    }
}


//默认问答
var trtd_html='<tr>\
        <td style="padding: 0;width: 80%;" class="onetd" id="record_qw">\
            <div class="table_td_tt font_red_color"><span>&nbsp;</span><label contenteditable="true" name="q" times=""></label></div>\
               <div  id="btnadd" ></div>\
                </td></tr>';
//*******************************************************************笔录问答编辑end****************************************************************//
//语音识别颜色
var asrcolor=["#AA66CC","#0099CC","#ef8201","#99CC00","#CC0000"," #ff80bf","#00b8e6","#00802b","#b30000","#3333ff","#e64d00","#739900","#b35900","#5c8a8a","#999966","#b3b3b3","##3366cc"];