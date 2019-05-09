function opneModal_1(recordtypessid) {
    var html='  <form class="layui-form site-inline" style="margin-top: 20px">\
               <div class="layui-form-item">\
                   <label class="layui-form-label">类型名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入类型名称" class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">排序</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="title" lay-verify="title" autocomplete="off" placeholder="请输入排序" class="layui-input">\
                    </div>\
                </div>\
            </form>';


    var index = layer.open({
        title:'模板类型编辑',
        content: html,
        area: ['500px', '300px'],
        btn: ['确定', '取消'],
        yes:function(index, layero){
            layer.close(index);
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}