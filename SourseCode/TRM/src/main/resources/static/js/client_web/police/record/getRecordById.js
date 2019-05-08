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