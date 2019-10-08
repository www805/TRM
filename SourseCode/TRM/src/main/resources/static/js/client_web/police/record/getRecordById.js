var recorduser=[];//会议用户集合
var mtssid=null;//当前会议的ssid
var videourl=null;//视频地址

var recordnameshow="";

var subtractime_q=0;//问的时间差
var subtractime_w=0;//答的时间差


var pdfdownurl=null;//pdf下载地址
var worddownurl=null;//word下载地址

var phdatabackList=null;//身心回放数据

var first_playstarttime=0;//第一个视频的开始时间
var dq_play=null;//当前视频数据
var recordPlayParams=[];//全部视频数据集合

var phSubtracSeconds=0;

var iid=null;//打包iid

var getRecordById_data=null;
var td_lastindex={};//td的上一个光标位置 key:tr下标 value：问还是答


/**
 * 局部刷新
 */
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getRecords_init(currPage,pageSize);
    }else if (len==2){
        getRecords('',arguments[0],arguments[1]);
    }else if(len>2){
        getRecords(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
    }

}
function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var recordtypessid=pageparam.recordtypessid;
        var recordname=pageparam.recordname;


        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
    }

}


//获取案件信息
function getRecordById() {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().getRecordById_getRecordById);
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
            var q_starttime=parseFloat(problemstarttime)+parseFloat(subtractime_q);

            var problemtext=problem.problem==null?"未知":problem.problem;
             problemhtml+= '<tr>\
                        <td style="padding: 0;width: 95%;" class="onetd" id="record_qw">\
                            <div class="table_td_tt font_red_color" ><span>问：</span><label name="q" contenteditable="false" times="'+q_starttime+'">'+problemtext+'</label></div>';
            var answers=problem.answers;
            if (isNotEmpty(answers)){
                for (var j = 0; j < answers.length; j++) {
                    var answer = answers[j];

                    var answerstarttime=answer.starttime;
                    var w_starttime=parseFloat(answerstarttime)+parseFloat(subtractime_w);

                    var answertext=answer.answer==null?"未知":answer.answer;
                    problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" times="'+w_starttime+'" >'+answertext+'</label></div>';
                }
            }else{
                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label  name="w"  contenteditable="false" ></label></div>';

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
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            getRecordById_data=data;
            var record=data.record;
            pdfdownurl=record.pdfdownurl;//pdf下载地址
            worddownurl=record.worddownurl;//word下载地址



            recordnameshow=record.recordname;//当前笔录名称

            if (isNotEmpty(record)){
                var wordheaddownurl_html=record.wordheaddownurl_html;
                if (isNotEmpty(wordheaddownurl_html)){
                    $("#wordheaddownurl").attr("src",wordheaddownurl_html);
                } else {
                    layer.msg("未找到模板头文件",{icon:5});
                }

                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);


                //会议人员
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
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
                    recorduser=[];
                    recorduser.push(user1);
                    recorduser.push(user2);
                }

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
                                  <tr><td>询问人二</td> <td>"+otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordadminname+"</td> </tr>\
                                  <tr><td>办案部门</td><td>"+department+"</td> </tr>\
                                  <tr><td>笔录类型</td><td>"+recordtypename+"</td> </tr>";
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




            var phDataBackVoParams=data.phDataBackVoParams;
            if (isNotEmpty(phDataBackVoParams)){
                phdatabackList=phDataBackVoParams;
                phSubtracSeconds=phdatabackList[0].phSubtracSeconds==null?0:phdatabackList[0].phSubtracSeconds;
                $("#fd_ph_HTML").attr("class","layui-col-md5").show();
                $("#record_qw_HTML").attr("class","layui-col-md7").show();
                $("#ph_HTML").show();
                main1();//身心统计回放
            }

            var getPlayUrlVO=data.getPlayUrlVO;
            if (isNotEmpty(getPlayUrlVO)) {
                $("#fd_ph_HTML").attr("class","layui-col-md5").show();
                $("#record_qw_HTML").attr("class","layui-col-md7").show();
                $("#fd_HTML").show();
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
                        }else if (usertype==2){
                            subtractime_w=subtractime==null?0:subtractime;
                            starttime=parseFloat(starttime)+parseFloat(subtractime_w);
                            recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+' ondblclick="showrecord('+starttime+',null)" times='+starttime+'>\
                                                           <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span>'+translatext+'</span> \
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


            for (let i = 0; i < recordPlayParams.length; i++) {
                var play=recordPlayParams[i];
                var playname=play.filename;
                for (let j = 0; j < recordFileParams.length; j++) {
                    const file = recordFileParams[j];
                    var filename= file.filename;
                    if (filename==playname){
                        var VIDEO_HTML='<span style="height: 50px;width: 50px;background:  url(/uimaker/images/videoback.png)  no-repeat;background-size:100% 100%; " class="layui-badge layui-btn layui-bg-gray"   filenum="'+play.filenum+'"  state="'+file.state+'">视频'+play.filenum+'</span>';
                        $("#videos").append(VIDEO_HTML);
                        play["start_range"]=parseFloat(play.recordstarttime)-parseFloat(first_playstarttime);
                        play["end_range"]=parseFloat(play.recordendtime)-parseFloat(first_playstarttime);
                        recordPlayParams[i]=play;
                        //时间毫秒区域计算结束------
                        console.log("["+play["start_range"]+","+play["end_range"]+"]");
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
        var url=getActionURL(getactionid_manage().getRecordById_exportWord);
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
        var url=getActionURL(getactionid_manage().getRecordById_exportPdf);
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
                    showPDF("pdfid",pdfdownurl);
                    layer.msg("导出成功,等待下载中...",{icon: 6});
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
    btn(obj);
}




//排行榜开始
var RANKING_INDEX=null;
function select_monitorallranking(obj) {
    var RANKING_HTML='<div class="layui-row layui-form " > \
        <div class="layui-col-md12 layui-form-item" style="margin-top: 10px" >\
        <div  id="rank_btn" style="display:inline">\
        <span class="layui-badge layui-btn" isn="1"  type="stress" >紧张值</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="hr" >心率</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1"  type="spo2" >血氧</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="hrv" >心率变异</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1"  type="bp">血压变化</span>\
        <span class="layui-badge layui-bg-gray layui-btn" isn="-1" type="br">呼吸次数</span>\
        </div>\
        </div>\
        <div class="layui-col-md12 layui-form-item"  >\
        <table class="layui-table" lay-skin="nob">\
         <tr >\
            <th colspan="3" style="text-align: left" ><span style="color: red" id="rank_title">紧张值</span>异常排行榜(TOP20)\
            <span class="layui-table-sort layui-inline"><i class="layui-edge layui-table-sort-asc" title="升序" onclick="set_phranking(null,1)"></i><i class="layui-edge layui-table-sort-desc" title="降序"  onclick="set_phranking(null,2)"></i></span></th>\
        </tr>\
        <tbody id="ranking_html">\
        </tbody>\
        </table>\
        </div>\
      </div>';
    if (RANKING_INDEX==null){
        RANKING_INDEX= layer.open({
            title: '身心检测异常排行榜',
             type: 1,
            content: RANKING_HTML,
            offset:'r', area:["20%", "100%"],
            anim:2,
            shade: 0,
            closeBtn:0,
            btn: [],
            resize:false,
            move: false,
            success: function(layero, index){
                set_phranking("stress",1);
                $("#rank_btn span").click(function () {
                    $("#rank_btn span").attr("isn","-1").addClass("layui-bg-gray");
                    $(this).attr("isn","1").removeClass("layui-bg-gray");
                    $("#rank_title").text($(this).text());
                    var type= $(this).attr("type");
                    set_phranking(type,1)
                });
            }
        });
    } else {
        layer.close(RANKING_INDEX);
        RANKING_INDEX=null;
    }
}

function sortphranking_desc(a, b) {
    return b.data - a.data;
}
function sortphranking_asc(a, b) {
    return a.data - b.data;
}
//type 身心检测参数问题  sorttype排序类型默认1
function set_phranking(type,sorttype) {
    if (isNotEmpty(sorttype)){
      if (!isNotEmpty(type)){
           type= $("#rank_btn span[isn=1]").attr("type");
      }
      if (isNotEmpty(type)){
          //筛选数据
          var newphdata=[];
          if (isNotEmpty(phdatabackList)){
              for (let i = 0; i < phdatabackList.length; i++) {
                  const ph = phdatabackList[i];
                  var num=ph.num;
                  var date=ph.phdate;
                  var dqphBataBackJson=ph.phBataBackJson;
                  dqphBataBackJson=eval("(" + dqphBataBackJson + ")");
                  var newarr={};
                  if (dqphBataBackJson.hr_snr>=0.1&&dqphBataBackJson[type]!=null){
                      newarr["num"]=num;
                      newarr["date"]=date;
                      if (type=="stress") {
                          if (!(dqphBataBackJson[type]>=0&&dqphBataBackJson[type]<=30)){
                              newarr["data"]=dqphBataBackJson[type];

                          }
                      }
                      if (type=="hr") {
                          if (!(dqphBataBackJson[type]>=60&&dqphBataBackJson[type]<=100)){
                              newarr["data"]=dqphBataBackJson[type];
                          }
                      }
                      if (type=="spo2") {
                          if (!(dqphBataBackJson[type]>=94)){
                              newarr["data"]=dqphBataBackJson[type];
                          }
                      }
                      if (type=="hrv") {
                          if (!(dqphBataBackJson[type]>=-10&&dqphBataBackJson[type]<=10)){
                              newarr["data"]=dqphBataBackJson[type];
                          }
                      }
                      if (type=="bp") {
                          if (!(dqphBataBackJson[type]>=-10&&dqphBataBackJson[type]<=10)){
                              newarr["data"]=dqphBataBackJson[type];
                          }
                      }
                      if (type=="br") {
                          if (!(dqphBataBackJson[type]>=12&&dqphBataBackJson[type]<=20)){
                              newarr["data"]=dqphBataBackJson[type];
                          }
                      }
                      if (newarr["data"]!=null){
                          newphdata.push(newarr);
                      }
                      //放松值不计算：没有范围
                  }
              }
          }
          if (sorttype==1){
              newphdata.sort(sortphranking_desc);
          } else {
              newphdata.sort(sortphranking_asc);
          }

          $("#ranking_html").empty();
          var morenHTML='<tr style="border-bottom: 1px solid #ccc">\
                        <td colspan="2" style="text-align: center">暂无数据</td>\
                        </tr>';
          var HTML="";
          if (isNotEmpty(newphdata)){
              newphdata=newphdata.slice(0,20);
              for (let i = 0; i <newphdata.length; i++) {
                  var data=newphdata[i].data;
                  var num=newphdata[i].num;
                  var date=newphdata[i].date;
                  var rankstyle="background-color: #8EB9F5 !important";
                  var rankicon="";
                  if (i<3) {
                      if (i==0){
                          rankstyle='background-color: red !important';
                      } else if (i==1){
                          rankstyle='background-color: #FF8547 !important';
                      } else if (i==2){
                          rankstyle='background-color: #FFAC38 !important';
                      }
                      rankicon='<i class="layui-icon layui-icon-fire" style="color: red;"></i>';
                  }
                  HTML+='<tr style="border-bottom: 1px solid #ccc" num="'+num+'">\
                        <td style="text-align: left"><span class="layui-badge" style="'+rankstyle+'">'+(i+1)+'</span>&nbsp;'+data+' '+rankicon+'</td>\
                        <td  style="text-align: right"><i class="layui-icon layui-icon-log"> '+date+'</i></td>\
                        </tr>';
              }
          }
          $("#ranking_html").html(HTML==""?morenHTML:HTML);

          //开始定位视频和语音识别：提前5-8秒
          $("#ranking_html tr").dblclick(function () {
              var num=$(this).attr("num");//秒数
              if (null!=num){
                  //定位视频和asr
                  phSubtracSeconds=parseInt(phSubtracSeconds);
                  var  newnum=num-phSubtracSeconds==null?0:(num-phSubtracSeconds<0?0:num-phSubtracSeconds);
                  showrecord(parseFloat(newnum*1000),parseFloat(num*1000));
                  //身心检测显示
                  var type= $("#rank_btn span[isn=1]").attr("type");
                  var $tb=$('#monitor_btn span[type='+type+']');
                  $("#monitor_btn span").attr("isn","-1").addClass("layui-bg-gray");
                  $($tb).attr("isn","1").removeClass("layui-bg-gray");
                  var name=$($tb).text();
                  myChart.setOption({
                      title: {
                          text: name,
                      },
                      series: [{
                          name:name,
                      }]
                  });
              }
          });
      }
    }
}
//排行榜结束




var option = {
    title: {
        text: '紧张值',
    },
    tooltip: {
        trigger: 'axis',
        formatter: '{a}: {c}'
    },
    xAxis: {
        type: 'category',
        splitLine: {
            show: false
        },
        show: false,
        data: date1
    },
    yAxis: {
        type: 'value',
        boundaryGap: [0, '100%'],
        splitLine: {
            show: false
        },
        show: true
    },
    grid: {
        x:30,
        y:45,
        x2:30,
        y2:10,
    },
    series: [{
        name: '紧张值',
        type: 'line',
        showSymbol: false,
        hoverAnimation: false,
        data: data1,
        markLine: {//警戒线标识
            silent: true,
            lineStyle: {
                normal: {
                    color: '#00CD68'                   // 这儿设置安全基线颜色
                }
            },
        }
    }]
};

var myChart;
var date1 = [];
var data1 = [];
var init_br = 1;
var date_br = [];
var data_br = [];
function addData_br(shift,data,date) {
    date_br.push(date);
    data_br.push(data);
    if (shift) {
        date_br.shift();
        data_br.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_br++;
    addData_br(false,0,init_br);
}


var init_bp = 1;
var date_bp = [];
var data_bp = [];
function addData_bp(shift,data,date) {
    date_bp.push(date);
    data_bp.push(data);
    if (shift) {
        date_bp.shift();
        data_bp.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_bp++;
    addData_bp(false,0,init_bp);
}

var init_hr = 1;
var date_hr = [];
var data_hr = [];
function addData_hr(shift,data,date) {
    date_hr.push(date);
    data_hr.push(data);
    if (shift) {
        date_hr.shift();
        data_hr.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_hr++;
    addData_hr(false,0,init_hr);
}

date1=date_hr;
data1=data_hr;
var init_hrv = 1;
var date_hrv = [];
var data_hrv = [];
function addData_hrv(shift,data,date) {
    date_hrv.push(date);
    data_hrv.push(data);
    if (shift) {
        date_hrv.shift();
        data_hrv.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_hrv++;
    addData_hrv(false,0,init_hrv);
}

var init_relax = 1;
var date_relax = [];
var data_relax = [];
function addData_relax(shift,data,date) {
    date_relax.push(date);
    data_relax.push(data);
    if (shift) {
        date_relax.shift();
        data_relax.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_relax++;
    addData_relax(false,0,init_relax);
}

var init_spo2 = 1;
var date_spo2 = [];
var data_spo2 = [];
function addData_spo2(shift,data,date) {
    date_spo2.push(date);
    data_spo2.push(data);
    if (shift) {
        date_spo2.shift();
        data_spo2.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_spo2++;
    addData_spo2(false,0,init_spo2);
}

var init_stress = 1;
var date_stress = [];
var data_stress = [];
function addData_stress(shift,data,date) {
    date_stress.push(date);
    data_stress.push(data);
    if (shift) {
        date_stress.shift();
        data_stress.shift();
    }
}
for (var i = 1; i < 50; i++) {
    init_stress++;
    addData_stress(false,0,init_stress);
}


function main1() {
    $("#main1").css( 'width',$("#showmsg").width() );
    $(window).resize(function() {
        myChart.resize();
    });
    myChart = echarts.init(document.getElementById('main1'),'dark');
    myChart.setOption(option);
}

/**
 * 身心监测按钮组
 */
function select_monitor(obj) {
    $(obj).attr("isn","1");
    $(obj).siblings().attr("isn","-1");

    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    var name=$(obj).text();
    myChart.setOption({
        title: {
            text: name,
        },
        series: [{
            name:name,
        }]
    });
}

//查看全部按钮
var select_monitorall_iframe=null;
var select_monitorall_iframe_body=null;
var select_monitorall_indedx=null;
function select_monitorall(obj) {
    if (select_monitorall_indedx==null){
        select_monitorall_indedx= layer.open({
            type: 2
            , skin: 'layui-layer-lan' //样式类名
            ,title: "身心检测"
            ,area: ['40%','100%']
            ,shade: 0
            ,id: 'layer_monitorall' //设定一个id，防止重复弹出
            ,offset: 'l'
            ,resize: true
            ,content: togetPolygraphurl
            ,success:function (layero,index) {
                select_monitorall_iframe = window['layui-layer-iframe' + index];
                select_monitorall_iframe_body=layer.getChildFrame('body', index);
                select_monitorall_iframe.monitorall1(option);
                select_monitorall_iframe.myMonitorall.setOption({
                    title: {
                        text: "心率",
                    },
                    xAxis: {
                        data: date_br
                    },
                    series: [{
                        name:"心率",
                        data: data_br
                    }]
                });
                select_monitorall_iframe.monitorall2(option);
                select_monitorall_iframe.myMonitorall2.setOption({
                    title: {
                        text: "心率变异",
                    },
                    xAxis: {
                        data: date_hrv
                    },
                    series: [{
                        name:"心率变异",
                        data: data_hrv
                    }]
                });
                select_monitorall_iframe.monitorall3(option);
                select_monitorall_iframe.myMonitorall3.setOption({
                    title: {
                        text: "呼吸次数",
                    },
                    xAxis: {
                        data: date_br
                    },
                    series: [{
                        name:"呼吸次数",
                        data: data_br
                    }]
                });
                select_monitorall_iframe.monitorall4(option);
                select_monitorall_iframe.myMonitorall4.setOption({
                    title: {
                        text: "放松值",
                    },
                    xAxis: {
                        data: date_relax
                    },
                    series: [{
                        name:"放松值",
                        data: data_relax
                    }]
                });
                select_monitorall_iframe.monitorall5(option);
                select_monitorall_iframe.myMonitorall5.setOption({
                    title: {
                        text: "紧张值",
                    },
                    xAxis: {
                        data: date_stress
                    },
                    series: [{
                        name:"紧张值",
                        data: data_stress
                    }]
                });
                select_monitorall_iframe.monitorall6(option);
                select_monitorall_iframe.myMonitorall6.setOption({
                    title: {
                        text: "血压变化",
                    },
                    xAxis: {
                        data: date_bp
                    },
                    series: [{
                        name:"血压变化",
                        data: data_bp
                    }]
                });
                select_monitorall_iframe.monitorall7(option);
                select_monitorall_iframe.myMonitorall7.setOption({
                    title: {
                        text: "血氧",
                    },
                    xAxis: {
                        data: date_spo2
                    },
                    series: [{
                        name:"血氧",
                        data: data_spo2
                    }]
                });
            },
            cancel: function(index, layero){
                select_monitorall_iframe=null;
                select_monitorall_iframe_body=null;
                layer.close(index)
            }
        });
    }else {
        layer.close(select_monitorall_indedx);
        select_monitorall_indedx=null;
    }
}

//视频进度
function showrecord(times,oldtime) {
    $("#recorddetail label").removeClass("highlight_right");
    $("#recordreals span").css("color","#fff").removeClass("highlight_left");
    if (isNotEmpty(times)&&times!=-1&&first_playstarttime!=0&&isNotEmpty(dq_play)&&isNotEmpty(recordPlayParams)){
        times=parseFloat(times);
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
            if (t1==times) {
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

        if (isNotEmpty(select_monitorall_iframe_body)) {
            /*  select_monitorall_iframe_body.find("#dqtime").html(date+ hour + "：" + min + "：" + sec);*/

            var date=newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ';
            var week=dayNames[newDate.getDay()];
            var time=hour + "：" + min + "：" + sec;
            select_monitorall_iframe_body.find("#dqtime1").html(date);
            select_monitorall_iframe_body.find("#dqtime2").html(week+time);
        }

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

            //中间
            var arrph=[];
            var dq_phdataback=null;
            if (isNotEmpty(phdatabackList)){
                locationtime=locationtime/1000<0?0:locationtime/1000; //秒转时间戳//时间戳转秒
                for (var i = 0; i < phdatabackList.length; i++) {
                    var phdataback = phdatabackList[i];
                    var num=phdataback.num;
                    var startph=num;
                    var endph=0;
                    if (i>= phdatabackList.length-1) {
                        endph= num;//下一个区间
                    }else {
                        endph=phdatabackList[i+1].num;
                    }
                    if (locationtime>=parseFloat(startph)&&(parseFloat(startph)==parseFloat(endph)||locationtime<=parseFloat(endph))) {
                        dq_phdataback = phdatabackList[i];
                        var start_i=(i-26)<0?0:(i-26);
                        var end_i=(i+25)>=phdatabackList.length?phdatabackList.length:(i+25);
                        arrph= phdatabackList.slice(start_i,end_i);
                    }
                }
                phdata(arrph,dq_phdataback);
            }
        }
    });


});

//将数据传给统计图：datad全部数据，dqdata当前数据
function phdata(datad,dqdata) {
    if (isNotEmpty(datad)){

        //将本次时间戳位置放中间--start--
        var dqnum=0;//当前序号
        var dqobj=null;//当前json值
        if(isNotEmpty(dqdata)){
            dqnum=dqdata.num;
            var dqphBataBackJson=dqdata.phBataBackJson;
            dqobj=eval("(" + dqphBataBackJson + ")");
        }
        //将本次时间戳位置放中间--end--

        var status=0;//状态
        var hr=0;//心率
        var br=0;//呼吸次数
        var relax=0;//轻松值
        var stress=0;//紧张值
        var bp=0;//血压变化
        var spo2=0;//血氧
        var hrv=0;//心率变异
        var hr_snr=0;
        var fps=0;
        var stress_snr=0;

        var emotion=-1;



        //数据收集
        var date_hr2 = [];
        var data_hr2 = [];

        var date_br2 = [];
        var data_br2 = [];

        var date_relax2 = [];
        var data_relax2 = [];

        var date_stress2 = [];
        var data_stress2 = [];

        var date_bp2 = [];
        var data_bp2 = [];

        var date_spo22 = [];
        var data_spo22 = [];

        var date_hrv2 = [];
        var data_hrv2= [];

        for (var i = 0; i < 50; i++) {
            var num=0;//底部时间戳
            var data = datad[i];
            if(isNotEmpty(data)){
                num=data.num;
                var phBataBackJson=data.phBataBackJson;
                var obj=eval("(" + phBataBackJson + ")");
                if (isNotEmpty(obj)) {
                    hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                    br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                    relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                    stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                    bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                    spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                    hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);

                    emotion=obj.emotion.toFixed(0)==null?0:obj.emotion.toFixed(0);
                }
            }

            date_hr2.push(num);
            data_hr2.push(parseFloat(hr));

            date_br2.push(num);
            data_br2.push(parseFloat(br));

            date_relax2.push(num);
            data_relax2.push(parseFloat(relax));

            date_stress2.push(num);
            data_stress2.push(parseFloat(stress));

            date_bp2.push(num);
            data_bp2.push(parseFloat(bp));

            date_spo22.push(num);
            data_spo22.push(parseFloat(spo2));

            date_hrv2.push(num);
            data_hrv2.push(parseFloat(hrv));
        }

        //开始赋值
        date_hr=date_hr2;
        data_hr=data_hr2;
        date_br=date_br2;
        data_br=data_br2;
        date_relax=date_relax2;
        data_relax=data_relax2;
        date_stress=date_stress2;
        data_stress=data_stress2;
        date_bp=date_bp2;
        data_bp=data_bp2;
        date_spo2=date_spo22;
        data_spo2=data_spo22;
        date_hrv=date_hrv2;
        data_hrv=data_hrv2;


        //当前数据
        if (isNotEmpty(dqobj)){
            status=dqobj.status;

            relax=dqobj.relax.toFixed(0);
            stress=dqobj.stress.toFixed(0);
            bp=dqobj.bp.toFixed(0);
            spo2=dqobj.spo2.toFixed(0);
            hr=dqobj.hr.toFixed(0);
            hrv=dqobj.hrv.toFixed(0);
            br=dqobj.br.toFixed(0);

            hr_snr=dqobj.hr_snr.toFixed(1);
            fps=dqobj.fps.toFixed(1);
            stress_snr=dqobj.stress_snr.toFixed(1);

            emotion=dqobj.emotion==null?6:dqobj.emotion;//为空默认表情：平静
        }


        //图标规划
        var dqx=dqnum;
        dqx=dqx.toString();
        var dqy=0;
        var itemStyle_color="red";
        var itemStyle_color_hr=itemStyle_color;
        var itemStyle_color_hrv=itemStyle_color;
        var itemStyle_color_br=itemStyle_color;
        var itemStyle_color_relax=itemStyle_color;
        var itemStyle_color_stress=itemStyle_color;
        var itemStyle_color_bp=itemStyle_color;
        var itemStyle_color_spo2=itemStyle_color;
        var dqpieces=[];
        var pieces_hr=[{ gt: -2,lte: -1,color: 'red'},{gt:60,lte: 100,color: '#00CD68'}];
        var pieces_hrv=[{ gt: -11,lte: 10,color: '#00CD68'}];
        var pieces_br=[{ gt: 11,lte: 20,color: '#00CD68'}];
        var pieces_relax=[{ gt: -2,lte: -1,color: '#00CD68'},{gt:-1,color: '#00CD68'}];
        var pieces_stress=[{ gt: -1,lte: 30,color: '#00CD68'},{ gt: 30,lte: 50,color: '#ffff33'},{ gt: 50,lte: 70,color: '#ff944d'},{ gt: 70,lte: 100,color: '#ff4c1e'}];
        var pieces_bp=[{ gt: -11,lte: 10,color: '#00CD68'}];
        var pieces_spo2=[{ gt: -1,lte: 94,color: 'red'},{ gt: 94,color: '#00CD68'}];


        var dqmarkLinedata=[];
        var dqmarkLinedata_hr=[{ yAxis: 60}, {yAxis: 100}];
        var dqmarkLinedata_hrv=[{yAxis: -10}, { yAxis: 10 }];
        var dqmarkLinedata_br=[{yAxis: 12}, { yAxis: 20 }];
        var dqmarkLinedata_relax=[];
        var dqmarkLinedata_stress=[{yAxis: 30}, { yAxis: 50 }, { yAxis: 70 }, { yAxis: 100 }];
        var dqmarkLinedata_bp=[{yAxis: -10}, { yAxis: 10 }];
        var dqmarkLinedata_spo2=[{yAxis: 94}];




        var dq_type=null;
        $("#monitor_btn span").each(function (e) {
            var type=$(this).attr("type");
            var name=$(this).text();
            var isn=$(this).attr("isn");
            if (isn==1){
                dq_type=type;
                if (type=="hr") {
                    date1=date_hr;
                    data1=data_hr;
                    dqy=hr;
                    dqmarkLinedata=dqmarkLinedata_hr;
                    dqpieces=pieces_hr;
                    if (hr>=60&&hr<=100){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="hrv") {
                    date1=date_hrv;
                    data1=data_hrv;
                    dqy=hrv;
                    dqmarkLinedata=dqmarkLinedata_hrv;
                    dqpieces=pieces_hrv;
                    if (hrv>=-10&&hrv<=10){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="br") {
                    date1=date_br;
                    data1=data_br;
                    dqy=br;
                    dqmarkLinedata=dqmarkLinedata_br;
                    dqpieces=pieces_br;
                    if (br>=12&&br<=20){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="relax") {
                    date1=date_relax;
                    data1=data_relax;
                    dqy=relax;
                    dqmarkLinedata=dqmarkLinedata_relax;
                    dqpieces=pieces_relax;
                    if (null!=relax){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="stress") {
                    date1=date_stress;
                    data1=data_stress;
                    dqy=stress;
                    dqmarkLinedata=dqmarkLinedata_stress;
                    dqpieces=pieces_stress;
                    if (stress>=0&&stress<=30){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="bp") {
                    date1=date_bp;
                    data1=data_bp;
                    dqy=bp;
                    dqmarkLinedata=dqmarkLinedata_bp;
                    dqpieces=pieces_bp;
                    if (bp>=-10&&bp<=10){
                        itemStyle_color="#00CD68";
                    }
                }else if (type=="spo2") {
                    date1=date_spo2;
                    data1=data_spo2;
                    dqy=spo2;
                    dqmarkLinedata=dqmarkLinedata_spo2;
                    dqpieces=pieces_spo2;
                    if (spo2>=94){
                        itemStyle_color="#00CD68";
                    }
                }
            }
        });



        myChart.setOption({
            xAxis: {
                data: date1
            },
            visualMap:{
                show:false,
                pieces:dqpieces,
                outOfRange: {
                    color: 'red'
                }
            },
            series: [{
                data: data1,
                markPoint: {
                    data: [
                        {name: '当前值', value:dqy, xAxis:dqx, yAxis: dqy}
                    ],
                    itemStyle:{
                        color:itemStyle_color,
                    }
                },
                markLine: {
                    data: dqmarkLinedata
                }
            }]
        });


        var redcolor="#00CD68";
        if (hr>=60&&hr<=100){
            itemStyle_color_hr=redcolor;
        }
        if (hrv>=-10&&hrv<=10){
            itemStyle_color_hrv=redcolor;
        }
        if (br>=12&&br<=20){
            itemStyle_color_br=redcolor;
        }
        if (null!=relax){
            itemStyle_color_relax=redcolor;
        }
        if (stress>=0&&stress<=30){
            itemStyle_color_stress=redcolor;
        }
        if (bp>=-10&&bp<=10){
            itemStyle_color_bp=redcolor;
        }
        if (spo2>=94){
            itemStyle_color_spo2=redcolor;
        }



        if (null!=select_monitorall_iframe){
            select_monitorall_iframe.myMonitorall.setOption({
                xAxis: {
                    data: date_hr
                },
                visualMap: {
                    show:false,
                    pieces:pieces_hr,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_hr,
                    markPoint: {
                        data: [
                            {name: '当前值', value:hr, xAxis:dqx, yAxis: hr}
                        ],
                        itemStyle:{
                            color:itemStyle_color_hr,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_hr
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall2.setOption({
                xAxis: {
                    data: date_hrv
                },
                visualMap: {
                    show:false,
                    pieces:pieces_hrv,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_hrv,
                    markPoint: {
                        data: [
                            {name: '当前值', value:hrv, xAxis:dqx, yAxis: hrv}
                        ],
                        itemStyle:{
                            color:itemStyle_color_hrv,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_hrv
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall3.setOption({
                xAxis: {
                    data: date_br
                },
                visualMap: {
                    show:false,
                    pieces:pieces_br,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_br,
                    markPoint: {
                        data: [
                            {name: '当前值', value:br, xAxis:dqx, yAxis: br}
                        ],
                        itemStyle:{
                            color:itemStyle_color_br,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_br
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall4.setOption({
                xAxis: {
                    data: date_relax
                },
                visualMap: {
                    show:false,
                    pieces:pieces_relax,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_relax,
                    markPoint: {
                        data: [
                            {name: '当前值', value:relax, xAxis:dqx, yAxis: relax}
                        ],
                        itemStyle:{
                            color:itemStyle_color_relax,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_relax
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall5.setOption({
                xAxis: {
                    data: date_stress
                },
                visualMap: {
                    show:false,
                    pieces:pieces_stress,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_stress,
                    markPoint: {
                        data: [
                            {name: '当前值', value:stress, xAxis:dqx, yAxis: stress}
                        ],
                        itemStyle:{
                            color:itemStyle_color_stress,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_stress
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall6.setOption({
                xAxis: {
                    data: date_bp
                },
                visualMap: {
                    show:false,
                    pieces:pieces_bp,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_bp,
                    markPoint: {
                        data: [
                            {name: '当前值', value:bp, xAxis:dqx, yAxis: bp}
                        ],
                        itemStyle:{
                            color:itemStyle_color_bp,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_bp
                    }
                }]
            });
            select_monitorall_iframe.myMonitorall7.setOption({
                xAxis: {
                    data: date_spo2
                },
                visualMap: {
                    show:false,
                    pieces:pieces_spo2,
                    outOfRange: {
                        color: 'red'
                    }
                },
                series: [{
                    data: data_spo2,
                    markPoint: {
                        data: [
                            {name: '当前值', value:spo2, xAxis:dqx, yAxis: spo2}
                        ],
                        itemStyle:{
                            color:itemStyle_color_spo2,
                        }
                    },
                    markLine: {
                        data: dqmarkLinedata_spo2
                    }
                }]
            });
        }

        //开始填数据
        var stress_text="未知";
        if (stress>=0&&stress<=30){
            stress_text="<span style='color: #00CD68'>正常</span>";
        }else if (stress>30&&stress<=50){
            stress_text="<span style='color: #e4e920'>轻度紧张</span>";
        }else if (stress>50&&stress<=70){
            stress_text="<span style='color: #ff840f'>中度紧张</span>";
        }else if (stress>70&&stress<=100){
            stress_text="<span style='color: #e90717'>高度紧张</span>";
        }

        $("#xthtml #xt1").html(' '+stress_text+'   ');
        $("#xthtml #xt2").html(' '+relax+'  ');
        $("#xthtml #xt3").html(' '+stress+'  ');
        $("#xthtml #xt4").html(' '+bp+'  ');
        $("#xthtml #xt5").html(' '+spo2+'  ');
        $("#xthtml #xt6").html(' '+hr+'  ');
        $("#xthtml #xt7").html(' '+hrv+'  ');
        $("#xthtml #xt8").html(' '+br+'  ');

        if (isNotEmpty(select_monitorall_iframe_body)) {
            select_monitorall_iframe_body.find("#monitorall #xt1").html(' '+stress_text+'   ');
            select_monitorall_iframe_body.find("#monitorall #xt2").html('放松值：'+relax+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt3").html('紧张值：'+stress+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt4").html('血压变化：'+bp+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt5").html('血氧：'+spo2+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt6").html('心率：'+hr+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt7").html('心率变异：'+hrv+'  ');
            select_monitorall_iframe_body.find("#monitorall #xt8").html('呼吸：'+br+'  ');
        }

        var snrtext="fps：0&nbsp;hr_snr：0&nbsp;stress_snr：0";
        snrtext="fps："+fps+"&nbsp;hr_snr："+hr_snr+"&nbsp;stress_snr："+stress_snr+"";
        $("#snrtext").html(snrtext);

        //表情
        var moodsrc="/uimaker/images/emojis/6.png";
        var moodtitle="平静";
        if(emotion!=null){
            moodsrc="/uimaker/images/emojis/"+emotion+".png";
            if (emotion==0){moodtitle="生气";}
            else  if(emotion==1){moodtitle="厌恶";}
            else  if(emotion==2){moodtitle="恐惧";}
            else  if(emotion==3){moodtitle="高兴";}
            else  if(emotion==4){moodtitle="伤心";}
            else  if(emotion==5){moodtitle="惊讶";}
            else  if(emotion==6){moodtitle="平静";}
        }
        select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#mood").attr({"src":moodsrc},{"title":moodtitle});



        var tsmsg="身心监测加载中...";
        var tsmsg_state=-1;
        var tscss={"color":" #31708f","background-color": "#d9edf7"," border-color": "#bce8f1"};
        if (null!=hr_snr&&hr_snr>=0.1){
            tsmsg_state=1;
            tsmsg="身心准确监测中";
            tscss={"color": "#3c763d","background-color":"#dff0d8","border-color":"#d6e9c6"};
        }else{
            tsmsg="监测准确度较低，请调整光线，减少身体晃动";
            tscss={"color": "#a94442","background-color":"#f2dede","border-color":"#ebccd1"};
        }
        $("#showmsg,#open_showmsg").css(tscss);
        $("#showmsg strong,#open_showmsg strong").text(tsmsg);
        if (isNotEmpty(select_monitorall_iframe_body)) {
            select_monitorall_iframe_body.find("#open_showmsg").css(tscss);
            select_monitorall_iframe_body.find("#open_showmsg strong").text(tsmsg);
            select_monitorall_iframe_body.find("#snrtext2").html(snrtext);
        }

        var monitoralltext="状态： "+stress_text+"\
                                                                <span  id=\"monitorall_hr\">心率： "+hr+"</span>\
                                                                <span  id=\"monitorall_hrv\">心率变异： "+hrv+"</span>\
                                                               <span  id=\"monitorall_br\">呼吸次数： "+br+"</span>\
                                                                <span  id=\"monitorall_relax\">放松值： "+relax+"</span>\
                                                                <span  id=\"monitorall_stress\">紧张值： "+stress+"</span>\
                                                                <span  id=\"monitorall_bp\">血压变化： "+bp+"</span>\
                                                                <span  id=\"monitorall_spo2\">血氧： "+spo2+"</span>";
        $("#monitorall_stressstate,#monitorall_hr,#monitorall_hrv,#monitorall_br,#monitorall_relax,#monitorall_stress,#monitorall_bp,#monitorall_spo2").removeClass("highlight_monitorall");
        $("#monitoralltext").html(monitoralltext);
        select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt1,#xt2,#xt3,#xt4,#xt5,#xt6,#xt7,#xt8").removeClass("highlight_monitorall");
        if (!(hr>=60&&hr<=100)&&tsmsg_state==1){
            $("#monitorall_hr").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt6").addClass("highlight_monitorall");
        }
        if (!(hrv>=-10&&hrv<=10)&&tsmsg_state==1){
            $("#monitorall_hrv").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt7").addClass("highlight_monitorall");
        }
        if (!(br>=12&&br<=20)&&tsmsg_state==1){
            $("#monitorall_br").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt8").addClass("highlight_monitorall");
        }
        if (!(stress>=0&&stress<=30)&&tsmsg_state==1){
            $("#monitorall_stress").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt3").addClass("highlight_monitorall");
        }
        if (!(bp>=-10&&bp<=10)&&tsmsg_state==1){
            $("#monitorall_bp").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt4").addClass("highlight_monitorall");
        }
        if (!(spo2>=94)&&tsmsg_state==1){
            $("#monitorall_spo2").addClass("highlight_monitorall");
            select_monitorall_iframe_body==null?null:select_monitorall_iframe_body.find("#xt5").addClass("highlight_monitorall");
        }


    }
}

//导出下载
var gZIPVod_index;
var gZIPVod_Url;
function gZIPVod(){
    if (isNotEmpty(iid)){
        $("#gZIPVod_html").css("display","none");
        gZIPVod_index=layer.msg("打包中，请稍等...", {
            icon: 16,
            shade: [0.1, 'transparent'],
            time: 100000
        });
        var url=getActionURL(getactionid_manage().getRecordById_gZIPVod);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                iid:iid,
                zipfilename:recordnameshow
            }
        };
        ajaxSubmitByJson(url,data,callbackgZIPVod);
    } else {
        layer.msg("请先确认视频文件是否生成...",{icon: 5});
    }

}
var timer_zIPVodProgress;
function callbackgZIPVod(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        layer.close(gZIPVod_index);
        gZIPVod_Url=data;

        //开始请求获取进度
         timer_zIPVodProgress=setInterval(function () {
            zIPVodProgress();
        },1000)

    }else {
        layer.msg(data.message,{icon: 5});
    }
}

function zIPVodProgress() {
    var url=getActionURL(getactionid_manage().getRecordById_zIPVodProgress);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            iid:iid,
            zipfilename:recordnameshow
        }
    };
    ajaxSubmitByJson(url,data,callbackzIPVodProgress);
}
function callbackzIPVodProgress(data) {
    $("#gZIPVod_html").css("display","block");
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        console.log(data)
        //开始显示进度
        if (isNotEmpty(data)){
            $("#gZIPVod_html .layui-col-md9").empty();
            var totalzipnum=data.totalzipnum==null?0:data.totalzipnum;//总共有多少个需要打包的文件
            var overzipnum=data.overzipnum==null?0:data.overzipnum;//已经完成了多少个文件
            var shu=(overzipnum/totalzipnum)*100;
            shu=parseInt(shu);
            var HTML='<div class="layui-progress " lay-showPercent="true" style="margin:8px" lay-filter="progress">\
                <div class="layui-progress-bar" lay-percent="'+shu+'%"></div>\
                </div>';
            $("#gZIPVod_html .layui-col-md9").html(HTML);

            layui.use(['layer','element','slider','form'], function(){
                var element = layui.element;
                element.render('progress');
                //使用模块
            });


        }
    }else if(null!=data&&data.actioncode=='SUCCESS_NOTHINGTODO'){
        $("#gZIPVod_html .layui-col-md9").html(data.message);
       setTimeout(function () {
            $("#gZIPVod_html").css("display","none");
        },5000)

        clearInterval(timer_zIPVodProgress);
        if (isNotEmpty(gZIPVod_Url)){
            var $a = $("<a></a>").attr("href", gZIPVod_Url).attr("download", "打包文件");
            $a[0].click();
        }
    }else {
        console.log(data.message);
        //进度请求失败
        $("#gZIPVod_html .layui-col-md9").html(data.message);
    }
}


//*******************************************************************案件人员信息编辑start****************************************************************//
function  open_casetouser() {
    layui.use(['layer','element','form','laydate'], function() {
        var form = layui.form;
        var laydate=layui.laydate;
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

                var url=getActionURL(getactionid_manage().getRecordById_updateCaseToUser);
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
                    if(null!=data&&data.actioncode=='SUCCESS'){
                        var data=data.data;
                        getRecordById();
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
        <div class="layui-card" >\
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
         <div class="layui-card" >\
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
    $("#recorddetail #record_qw").css({"width":"80%"});
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

    var url=getActionURL(getactionid_manage().getRecordById_setRecordreal);

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
    var url=getActionURL(getactionid_manage().getRecordById_getRecordrealByRecordssid);
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
                $("#recorddetail .table_td_tt").dblclick(function () {
                    var contenteditable=$("label",this).attr("contenteditable");
                    if (isNotEmpty(contenteditable)&&contenteditable=="false") {
                        //开始定位视频位置
                        var times=$("label",this).attr("times");
                        showrecord(times,null);
                    }
                })
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
        var url=getActionURL(getactionid_manage().getRecordById_addRecord);
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
        <td style="padding: 0;width: 80%;" class="onetd" id="record_qw">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(this,event);" q_starttime=""></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(this,event);"  w_starttime=""placeholder=""></label></div>\
               <div  id="btnadd"></div>\
                </td>\
                <td id="record_util">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                   </div>\
                </td>\
                </tr>';
//*******************************************************************笔录问答编辑end****************************************************************//