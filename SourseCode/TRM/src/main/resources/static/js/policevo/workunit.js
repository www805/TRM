function getworkunitList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().workunit_getworkunitList);
    var workname=$("#workname").val();
    var data={
        workname:workname,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetworkunitList);
}
function getworkunitList(workname,currPage,pageSize){
    var url=getActionURL(getactionid_manage().workunit_getworkunitList);
    var data={
        workname:workname,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetworkunitList);
}


function callbackgetworkunitList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

/**
 * 局部刷新
 */
function getworkunitListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getworkunitList_init(currPage,pageSize);
    }else if (len==2){
        getworkunitList('',arguments[0],arguments[1]);
    }else if(len>2){
        getworkunitList(arguments[0],arguments[1],arguments[2]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var workname=pageparam.workname;
        var arrparam=new Array();
        arrparam[0] = workname;
        showpage("paging",arrparam,'getworkunitListByParam',currPage,pageCount,pageSize);
    }
}


function open_workunit(ssid,oldworkname) {
    if (!isNotEmpty(oldworkname)){
        oldworkname="";
    }

    var html=  '<form class="layui-form layui-form-pane site-inline"  style="margin: 30px;">\
                <div class="layui-form-item">\
                    <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>单位名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="worknamem" id="worknamem" lay-verify="worknamem" autocomplete="off" placeholder="请输入工作单位名称" class="layui-input" value="'+oldworkname+'">\
                    </div>\
                </div>\
            </form>';
    layui.use(['layer','element','form','laydate'], function(){
        var form=layui.form;
        var index = layer.open({
            type:1,
            title:'笔录类型编辑',
            content: html,
            area: ['25%', '30%'],
            btn: ['确定', '取消'],
            success: function (layero, index) {
                layero.addClass('layui-form');//添加form标识
                layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                form.render();
            },
            yes:function(index, layero){
                form.verify({
                    worknamem:[ /\S/,"请输入工作单位名称"],
                });
                form.on("submit(fromContent)", function (data) {
                    var worknamem=$("#worknamem").val();
                    if(!isNotEmpty(worknamem)){
                        layer.msg("请输入工作单位名称",{icon:5});

                        $("#worknamem").focus();
                        return;
                    }

                    var url=null;
                    if (isNotEmpty(ssid)){
                        url=getActionURL(getactionid_manage().workunit_updateWorkunit);
                    } else {
                        url=getActionURL(getactionid_manage().workunit_addWorkunit);
                    }
                    var data={
                        ssid:ssid,
                        workname:worknamem,
                    };
                    ajaxSubmit(url,data,function (data2) {
                        if(null!=data2&&data2.actioncode=='SUCCESS') {
                            var data = data2.data;
                            if (isNotEmpty(data)) {
                                layer.msg("保存成功",{icon:6});
                                getworkunitListByParam();
                                layer.close(index);
                            }
                        }else{
                            layer.msg(data2.message,{icon: 5});
                            return;
                        }
                    });
                });
            },
            btn2:function(index, layero){
                layer.close(index);
            }
        });
    });
}