var recordSet_index;
var recordCaseInfo_index;

function recordSet() {

    //使用模块
    var html=
        '<div class="layui-form layui-row" style="margin-left: 15px;margin-top: 8px;">\n' +
        '    <div style="margin-bottom: 20px;">\n' +
        '        <button class="layui-btn layui-btn-normal">进出仓</button>\n' +
        '        <button class="layui-btn layui-btn-normal" onclick="recordCaseInfo();">案件信息</button>\n' +
        '        <button class="layui-btn layui-btn-normal">光盘开始</button>\n' +
        '        <button class="layui-btn layui-btn-normal">光盘结束</button>\n' +
        '    </div>\n' +
        '\n' +
        '    <div style="margin-bottom: 20px;z-index: 88;">\n' +
        '        <div class="layui-inline">\n' +
        '            <label class="layui-form-label" style="width: auto;padding-left: 0;">刻录选时</label>\n' +
        '            <div class="layui-input-inline" style="width: 100px;">\n' +
        '                <select name="quiz">\n' +
        '                    <option value="1">1小时</option>\n' +
        '                    <option value="2">2小时</option>\n' +
        '                    <option value="3">3小时</option>\n' +
        '                    <option value="4">4小时</option>\n' +
        '                    <option value="4">4小时</option>\n' +
        '                    <option value="5">5小时</option>\n' +
        '                    <option value="6">6小时</option>\n' +
        '                    <option value="7">7小时</option>\n' +
        '                    <option value="8">8小时</option>\n' +
        '                    <option value="9">9小时</option>\n' +
        '                    <option value="10">10小时</option>\n' +
        '                    <option value="11">11小时</option>\n' +
        '                    <option value="12">12小时</option>\n' +
        '                    <option value="13">13小时</option>\n' +
        '                    <option value="14">14小时</option>\n' +
        '                    <option value="15">15小时</option>\n' +
        '                    <option value="16">16小时</option>\n' +
        '                    <option value="17">17小时</option>\n' +
        '                    <option value="18">18小时</option>\n' +
        '                    <option value="19">19小时</option>\n' +
        '                    <option value="20">20小时</option>\n' +
        '                    <option value="21">21小时</option>\n' +
        '                    <option value="22">22小时</option>\n' +
        '                    <option value="23">23小时</option>\n' +
        '                    <option value="24">24小时</option>\n' +
        '                </select>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '\n' +
        '        <div class="layui-inline">\n' +
        '            <label class="layui-form-label" style="width: auto;">通道1</label>\n' +
        '            <div class="layui-input-inline" style="width: 60px;">\n' +
        '                <select name="quiz">\n' +
        '                    <option value="1">1</option>\n' +
        '                    <option value="2">2</option>\n' +
        '                    <option value="3">3</option>\n' +
        '                    <option value="4">4</option>\n' +
        '                    <option value="5">5</option>\n' +
        '                    <option value="6">6</option>\n' +
        '                    <option value="7">7</option>\n' +
        '                    <option value="8">8</option>\n' +
        '                </select>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '\n' +
        '        <div class="layui-inline">\n' +
        '            <label class="layui-form-label" style="width: auto;">速度</label>\n' +
        '            <div class="layui-input-inline" style="width: 60px;">\n' +
        '                <select name="quiz">\n' +
        '                    <option value="1">1</option>\n' +
        '                    <option value="2">2</option>\n' +
        '                    <option value="3">3</option>\n' +
        '                    <option value="4">4</option>\n' +
        '                    <option value="5">5</option>\n' +
        '                    <option value="6">6</option>\n' +
        '                    <option value="7">7</option>\n' +
        '                    <option value="8">8</option>\n' +
        '                    <option value="9">9</option>\n' +
        '                    <option value="10">10</option>\n' +
        '                </select>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '\n' +
        '    <div style="float: left;margin-top: 30px;margin-right: 50px;">\n' +
        '        <div style="padding-left: 55px;">\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2">&#xeb9b;</button>\n' +
        '        </div>\n' +
        '        <div class="layui-btn-group">\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2">&#xe604;</button>\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2">&#xeb9c;</button>\n' +
        '            <button class="layui-btn layui-btn-normal iconfont2">&#xeb9d;</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '\n' +
        '    <div style="float: left;">\n' +
        '        <div class="layui-form-item" style="margin-bottom: 5px;">\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe611;</button>\n' +
        '            <label class="layui-form-label" style="width: auto;float: left;">光圈</label>\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe638;</button>\n' +
        '        </div>\n' +
        '\n' +
        '        <div class="layui-form-item" style="margin-bottom: 5px;">\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe611;</button>\n' +
        '            <label class="layui-form-label" style="width: auto;float: left;">聚焦</label>\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe638;</button>\n' +
        '        </div>\n' +
        '\n' +
        '        <div class="layui-form-item" style="margin-bottom: 5px;">\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe611;</button>\n' +
        '            <label class="layui-form-label" style="width: auto;float: left;">镜头</label>\n' +
        '            <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-radius layui-btn-normal icon-diy iconfont2" >&#xe638;</button>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '</div>';

    recordSet_index = layer.open({
        type: 1,
        id: "layer_recordSet",
        title: '刻录设置',
        closeBtn: 1,
        skin: 'layui-layer-lan',
        shade: 0,
        offset: ['130px', '400px'],
        area: ['460px', '400px'],
        content: html,
        success: function (layero, index) {
            // layer.min(index);

            layui.use('form', function() {
                var form = layui.form;
                form.render();
            });
        }
    });

}


function recordCaseInfo() {

    //使用模块
    var html= '<form class="layui-form layui-main site-inline" action="" style="margin-top: 30px;width: 800px;">\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">案件编号</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="loginaccount" id="loginaccount"   lay-verify="required" placeholder="请输入案件编号" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">案件名称</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入案件名称" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">案件类型</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="loginaccount" id="loginaccount"   lay-verify="required" placeholder="请输入案件类型" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">案 &nbsp;&nbsp;由</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入案由" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">审讯类型</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="loginaccount" id="loginaccount"   lay-verify="required" placeholder="请输入审讯类型" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">办案部门</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入办案部门" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">被询(讯)问人</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="loginaccount" id="loginaccount"   lay-verify="required" placeholder="请输入被询(讯)问人" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">询(讯)问人</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入询(讯)问人" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label" style="width: 100px;padding-left: 0;padding-right: 10px;">录制(记录)问人</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入录制(记录)问人" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">询(讯)问地址</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="text" name="username" id="username" required  lay-verify="required" placeholder="请输入询(讯)问地址" autocomplete="off" class="layui-input">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <label class="layui-form-label">叠加时间</label>\n' +
        '                <div class="layui-input-block">\n' +
        '                    <select name="workunitssid" id="workunitssid">\n' +
        '                        <option value="5">5秒</option>\n' +
        '                        <option value="15">15秒</option>\n' +
        '                        <option value="20">20秒</option>\n' +
        '                        <option value="30">30秒</option>\n' +
        '                        <option value="60">60秒</option>\n' +
        '                    </select>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '            <div class="layui-form-item">\n' +
        '                <div class="layui-input-block">\n' +
        '                    <input type="button"  class="layui-btn layui-btn-normal" onclick="addOrUpdateUser();" value="叠加片头" />\n' +
        '                    <button class="layui-btn layui-btn-normal" onclick="layer.close(recordCaseInfo_index);return false;">关闭</button>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </form>';

    recordCaseInfo_index = layer.open({
        type: 1,
        id: "layer_recordCaseInfo",
        title: '片头叠加',
        closeBtn: 1,
        skin: 'layui-layer-lan',
        shade: 0,
        offset: 'auto',
        area: ['900px', '730px'],
        content: html,
        success: function (layero, index) {
            // layer.min(index);
            layui.use('form', function() {
                var form = layui.form;
                form.render();
            });
        }
    });

}