function addCaseToArraignment() {
    //添加笔录案件关联，跳转页面

    var nextparam=getAction(getactionid_manage().addCaseToUser_addCaseToArraignment);
    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
        setpageAction(INIT_CLIENT,nextparam.nextPageId);
        var url=getActionURL(getactionid_manage().addCaseToUser_towaitRecord);
        window.location.href=url;
    }

}