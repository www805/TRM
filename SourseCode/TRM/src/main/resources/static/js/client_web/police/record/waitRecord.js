function opneModal_1() {
    var html='    <div class="layui-row">\n' +
        '        <div  style="height: 380px;">\n' +
        '            <div class="layui-col-md12" >\n' +
        '                <div class="layui-form-item">\n' +
        '                    <div class="layui-input-inline">\n' +
        '                        <input type="text" name="casename" id="casename" lay-verify="required" placeholder="模板名称" autocomplete="off" class="layui-input">\n' +
        '                    </div>\n' +
        '                    <input type="button" class="layui-btn layui-btn-normal" value="查询" onclick="" >\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div class="layui-col-md12 " >\n' +
        '                <button class="layui-btn layui-btn-primary layui-btn layui-btn-xs">原始按钮</button>\n' +
        '                <button class="layui-btn layui-btn-primary layui-btn layui-btn-xs">原始按钮</button>\n' +
        '                <button class="layui-btn layui-btn-primary layui-btn layui-btn-xs">原始按钮</button>\n' +
        '                <button class="layui-btn layui-btn-primary layui-btn layui-btn-xs">原始按钮</button>\n' +
        '                <button class="layui-btn layui-btn-primary layui-btn layui-btn-xs">原始按钮</button>\n' +
        '            </div>\n' +
        '            <div class="layui-col-md12"  style="margin-top: 15px;border: 1px solid #F2F2F2">\n' +
        '                <div class="layui-col-md3" style=" height: 350px;overflow-x: hidden; overflow-y: scroll;">\n' +
        '                    <table class="layui-table" lay-skin="line"  >\n' +
        '                        <tbody id="table4" >\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>贤心</td>\n' +
        '                        </tr>\n' +
        '                        </tbody>\n' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="layui-col-md9" style=" height:350px;overflow-x: hidden; overflow-y: scroll;">\n' +
        '                    <table class="layui-table" lay-skin="line"  >\n' +
        '                        <tbody>\n' +
        '                        <tr>\n' +
        '                            <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_red_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td class="font_blue_color">人生就像是一场修行</td>\n' +
        '                        </tr>\n' +
        '                        </tbody>\n' +
        '                    </table>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '\n' +
        '        </div>\n' +
        '\n' +
        '          </div>\n';


    var index = layer.open({
        title:'变更模板',
        content: html,
        area: ['800px', '650px'],
        btn: ['应用模板', '设置为默认模板'],
        yes:function(index, layero){
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}