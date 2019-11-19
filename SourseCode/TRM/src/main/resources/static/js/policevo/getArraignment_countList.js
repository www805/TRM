var counttime = null;

function getArraignment_count_init(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);
    var data={
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callCountList);
}
function getArraignment_countList(username,starttime,endtime,currPage,pageSize){
    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);
    var data={
        username:username,
        starttime:starttime,
        endtime:endtime,
        currPage:currPage,
        pageSize:pageSize
    };

    //加载层-风格4
    var index = layer.msg('加载中', {
        icon: 16
        ,shade: 0.01
        ,time: 6000, //20s后自动关闭
    });

    ajaxSubmit(url,data,callCountList);

    layer.close(index);
}
//查询
function getArraignment_count_search(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);

    var username=$("#username").val();
    var starttime = "";
    var endtime = "";

    if (isNotEmpty(counttime)){
        var arr = counttime.split("~");
        starttime=arr[0].trim();
        endtime=arr[1].trim();
    }

    var data={
        username:username,
        starttime:starttime,
        endtime:endtime,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callCountList);
}

function getArraignment_count_Excel(){
    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countPrint);
    var username=$("#username").val();
    var starttime = "";
    var endtime = "";

    if (isNotEmpty(counttime)){
        var arr = counttime.split("~");
        starttime=arr[0].trim();
        endtime=arr[1].trim();
    }

    var data={
        username:username,
        starttime:starttime,
        endtime:endtime
    };

    ajaxSubmit(url,data,callCountExcelList);
}

function callCountExcelList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var downurl=data.data;
            if (isNotEmpty(downurl)) {
                layer.msg(data.message,{icon: 6});
                window.location.href = downurl;
            }

            layui.use('form', function(){
                var form =  layui.form;
                form.render();
            });
        }
    }else{
        layer.msg(data.message,{icon:5});
    }
}

function callCountList(data){
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
function getArraignment_countByParam(){
    var len=arguments.length;

    // alert(len);
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getArraignment_count_init(currPage,pageSize);
    }else if (len==2){
        getArraignment_countList('',arguments[0],arguments[1]);
    }else if(len>2){
        getArraignment_countList(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var username=pageparam.username;
        // var counttime=pageparam.counttime;
        var starttime="";
        var endtime="";

        if (isNotEmpty(counttime)){
            var arr = counttime.split("~");
            starttime=arr[0].trim();
            endtime=arr[1].trim();
        }

        var arrparam=new Array();
        arrparam[0] = username;
        arrparam[1] = starttime;
        arrparam[2] = endtime;

        showpage("paging",arrparam,'getArraignment_countByParam',currPage,pageCount,pageSize);
    }
}
