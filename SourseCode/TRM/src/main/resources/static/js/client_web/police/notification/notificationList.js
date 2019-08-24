//获取告知书
function getNotificationList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().notification_getNotifications);
    var notificationname=$("input[name='notificationname']").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            notificationname: notificationname,
            currPage:currPage,
            pageSize:pageSize
        }
    };

    ajaxSubmitByJson(url,data,callNotificationList);
}
//查询告知书
function getNotificationList(notificationname, currPage, pageSize) {
    var url=getActionURL(getactionid_manage().notification_getNotifications);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            notificationname: notificationname,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url, data, callNotificationList);
}

//删除告知书
function deleteNotificationById(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 5});
        return;
    }

    layer.confirm('确定要删除该告知书吗', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().notification_deleteNotificationById);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: ssid
            }
        };

        ajaxSubmitByJson(url,data,callDeleteNotificationById);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });


}

//上传告知书
// function uploadNotification() {
//     var url=getActionURL(getactionid_manage().notification_uploadNotification);
//
//     var data = {
//         token: INIT_CLIENTKEY,
//         param: {
//             ssid: ssid
//         }
//     };
//     ajaxSubmitByJson(url, data, calluploadNotification);
// }

//下载告知书
function downloadNotification(ssid) {
    var url=getActionURL(getactionid_manage().notification_downloadNotification);

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ssid: ssid
        }
    };
    ajaxSubmitByJson(url, data, calldownloadNotification);
}

function calldownloadNotification(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var filesave=data.data;
        if (isNotEmpty(filesave)){
            var base_filesave=filesave.base_filesave;
            if (isNotEmpty(base_filesave)) {
                layer.msg("下载中，请稍后...",{icon: 5});
                // window.location.href = filesave.recorddownurl;

                var url = base_filesave.recorddownurl;

                // alert(url);

                var a = document.createElement('a');          // 创建一个a节点插入的document
                var event = new MouseEvent('click');           // 模拟鼠标click点击事件
                a.download = filesave.uploadfilename;                  // 设置a节点的download属性值
                a.href = url;                                 // 将图片的src赋值给a节点的href
                a.dispatchEvent(event);

                // DownLoadReportIMG(url);

                // window.location.href=url.replace("image/png", "image/octet-stream");

                // var host = "http://" + window.location.host;
                // host = host.replace(":8080","");
                // window.location.href = host + filesave.recorddownurl;
                // oDownLoad(host + filesave.recorddownurl);
                // setTimeout("window.location.reload()",1500);


            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callNotificationList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            var listcountsize = data.data.pageparam.recordCount;
            if (listcountsize == 0) {
                $("#wushuju").show();
                $("#pagelistview").hide();
            } else {
                $("#wushuju").hide();
                $("#pagelistview").show();
            }
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
}

function callDeleteNotificationById(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            layer.msg(data.data,{icon: 6});
            setTimeout("window.location.reload()",1500);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

/**
 * 局部刷新
 */
function getNotificationListParam() {

    var len = arguments.length;

    if (len == 0) {
        var currPage = 1;
        var pageSize = 10;//测试
        getNotificationList_init(currPage, pageSize);
    }  else if (len == 2) {
        getNotificationList('', arguments[0], arguments[1]);
    } else if (len > 2) {
        getNotificationList(arguments[0], arguments[1], arguments[2]);
    }
}

function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var notificationname=$("input[name='notificationname']").val();
        var arrparam=new Array();
        arrparam[0]=notificationname;
        showpage("paging",arrparam,'getNotificationListParam',currPage,pageCount,pageSize);
    }
}

function opneModal_1(problem) {

    var typename = "";
    var ordernum = "0";

    if(isNotEmpty(problem)) {
        typename = problem.typename;
        ordernum = problem.ordernum;
    }

    var html='  <form class="layui-form site-inline" style="margin-top: 20px">\
               <div class="layui-form-item">\
                   <label class="layui-form-label">类型名称</label>\
                    <div class="layui-input-block">\
                    <input type="text" name="typename" lay-verify="title" autocomplete="off" placeholder="请输入类型名称" value="' + typename + '"  class="layui-input">\
                    </div>\
                </div>\
                <div class="layui-form-item">\
                    <label class="layui-form-label">排序</label>\
                    <div class="layui-input-block">\
                    <input type="number" name="ordernum" lay-verify="title" autocomplete="off" placeholder="请输入排序" value="' + ordernum + '"  class="layui-input">\
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

            if (isNotEmpty(typename)){
                AddOrUpdateTemplateType();//修改
            }else{
                AddOrUpdateTemplateType(1);//新增
            }
        },
        btn2:function(index, layero){
            layer.close(index);
        }
    });
}

function DownLoadReportIMG(imgPathURL) {
    //如果隐藏IFRAME不存在，则添加
    if (!document.getElementById("IframeReportImg"))
        $('<iframe style="display:none;" id="IframeReportImg" name="IframeReportImg" onload="DoSaveAsIMG();" width="0" height="0" src="about:blank"></iframe>').appendTo("body");
    if (document.all.IframeReportImg.src != imgPathURL) {
        //加载图片
        document.all.IframeReportImg.src = imgPathURL;
    }
    else {
        //图片直接另存为
        DoSaveAsIMG();
    }
}
function DoSaveAsIMG() {
    if (document.all.IframeReportImg.src != "about:blank")
        window.frames["IframeReportImg"].document.execCommand("SaveAs");
}
