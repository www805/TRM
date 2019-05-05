function getPermissionsByMenu() {
    var url=getActionURL(getactionid_manage().main_getPermissionsByMenu);
    ajaxSubmit(url,null,callbackgetPermissionsByMenu);
}

function callbackgetPermissionsByMenu(data) {
  //  console.log("main__"+data);
}
