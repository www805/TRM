function opneModal_1() {
    var html='     <div class="layui-row " >\n' +
        '        <div  style="height: 650px; " >\n' +
        '            <div class="layui-col-md3 "style="border:1px solid #F2F2F2;" >\n' +
        '                <div class="layui-card">\n' +
        '                    <div class="layui-card-header">\n' +
        '                        <div class="layui-form-item" >\n' +
        '                            <div class="layui-inline" >\n' +
        '                                <label class="layui-form-mid">范围</label>\n' +
        '                                <div class="layui-input-inline" style="width: 100px;">\n' +
        '                                    <input type="text" name="price_min" placeholder="" autocomplete="off" class="layui-input">\n' +
        '                                </div>\n' +
        '                                <div class="layui-form-mid">关键字</div>\n' +
        '                                <div class="layui-input-inline" style="width: 100px;">\n' +
        '                                    <input type="text" name="price_max" placeholder="" autocomplete="off" class="layui-input">\n' +
        '                                </div>\n' +
        '                                <div class="layui-input-inline" style="width: 100px;">\n' +
        '                                    <button class=" layui-btn layui-btn-sm layui-btn-normal">百搭按钮</button>\n' +
        '                                </div>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div style="height: 600px; overflow-x: hidden; overflow-y: scroll;">\n' +
        '                        <div class="layui-card-body" style="padding: 0;">\n' +
        '                            <table class="layui-table" lay-even="" lay-skin="nob">\n' +
        '                                <tbody>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td>笔录标题</td>\n' +
        '                                </tr>\n' +
        '                                </tbody>\n' +
        '                            </table>\n' +
        '                            <div id="test-laypage-demo2"><div class="layui-box layui-laypage layui-laypage-molv" id="layui-laypage-3"><a href="javascript:;" class="layui-laypage-prev layui-disabled" data-page="0">上一页</a><span class="layui-laypage-curr"><em class="layui-laypage-em" style="background-color:#1E9FFF;"></em><em>1</em></span><a href="javascript:;" data-page="2">2</a><a href="javascript:;" data-page="3">3</a><a href="javascript:;" data-page="4">4</a><a href="javascript:;" data-page="5">5</a><span class="layui-laypage-spr">…</span><a href="javascript:;" class="layui-laypage-last" title="尾页" data-page="10">10</a><a href="javascript:;" class="layui-laypage-next" data-page="2">下一页</a></div></div>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div class="layui-col-md9" style="border:1px solid #F2F2F2;" >\n' +
        '                <div class="layui-card">\n' +
        '                    <div class="layui-card-header">\n' +
        '                        选择的笔录标题\n' +
        '                        <a class="layui-icon  layui-icon-more layuiadmin-badge" title="详情查看" th:href="@{/cweb/police/policePage/togetRecordById}"></a>\n' +
        '                    </div>\n' +
        '                    <div style="height: 600px; overflow-x: hidden; overflow-y: scroll;">\n' +
        '                        <div class="layui-card-body">\n' +
        '                            <table class="layui-table" lay-even="" lay-skin="nob">\n' +
        '                                <tbody >\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                <tr>\n' +
        '                                    <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                                </tr>\n' +
        '                                </tbody>\n' +
        '                            </table>\n' +
        '                        </div>\n' +
        '\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>\n';


    var index = layer.open({
        title:'笔录选择',
        content: html,
        area: ['800px', '650px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

function getRecordById() {
    var url=getActionURL(getactionid_manage().getRecordById_getRecordById);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordById);
}

function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var record=data.record;
            var caseAndUserInfo=data.caseAndUserInfo;

            if (isNotEmpty(record)){
                $("#recordtitle").text(record.recordname==null?"笔录标题":record.recordname);
                if (isNotEmpty(record.recorddownurl)){
                    wavesurfer.load(record.recorddownurl);
                }

                    var problems=record.problems;
                    $("#recorddetail").html("");
                    if (isNotEmpty(problems)) {
                        for (var z = 0; z< problems.length;z++) {
                            var problem = problems[z];
                            var problemtext=problem.problem==null?"未知":problem.problem;
                            var problemhtml=' <tr><td class="font_red_color">问：'+problemtext+' </td></tr>';
                            var answers=problem.answers;
                            if (isNotEmpty(answers)){
                                for (var j = 0; j < answers.length; j++) {
                                    var answer = answers[j];
                                    var answertext=answer.answer==null?"未知":answer.answer;
                                    problemhtml+='<tr> <td class="font_blue_color">答：'+answertext+' </td></tr>';
                                }
                            }else{
                                problemhtml+='<tr> <td class="font_blue_color">答：... </td></tr>';
                            }
                            $("#recorddetail").append(problemhtml);
                        }
                    }
            }

            var init_casehtml="案件名称：<br>\
                                案件人：<br>\
                                当前案由：<br>\
                                案件时间：<br>\
                                案件编号：<br> ";
            $("#caseAndUserInfo_html").html(init_casehtml);
            if (isNotEmpty(caseAndUserInfo)){
                init_casehtml="案件名称："+caseAndUserInfo.casename+"<br>\
                                 案件人："+caseAndUserInfo.username+"<br>\
                                当前案由："+caseAndUserInfo.cause+"<br>\
                                案件时间："+caseAndUserInfo.occurrencetime+"<br>\
                                案件编号："+caseAndUserInfo.casenum+"<br>";
                $("#caseAndUserInfo_html").html(init_casehtml);
            }
        }
    }else{
        layer.msg(data.message);
    }
}


var wavesurfer;
$(function () {
    wavesurfer = WaveSurfer.create({
        container: '#waveform',
        scrollParent: true,
        waveColor: "#368666",
        progressColor: "#6d9e8b",
        cursorColor: "#fff",
        height: 160,
        hideScrollbar: false
    });

    wavesurfer.on("ready",function () {
        wavesurfer.play();
        $("#recordtime").text(parseInt(wavesurfer.getDuration()));
        $("#currenttime").text(wavesurfer.getCurrentTime());
    });
    wavesurfer.on("audioprocess",function () {
        $("#currenttime").text(wavesurfer.getCurrentTime());
    });


    //播放按钮
    $("#recordplay").click(function(){
        wavesurfer.play();
    });

    //停止按钮
    $("#recordpause").click(function(){
        wavesurfer.pause();
    });


    layui.use(['form', 'slider'], function(){
        var $ = layui.$
            ,slider = layui.slider;

        //定义初始值
        slider.render({
            elem: '#slideTest'
            ,max:'1'
            ,max:'100'
            ,theme: '#1E9FFF' //主题色
            ,change: function(value){
                wavesurfer.setVolume(value/100);//设置音频音量
            }
        });
    });

});