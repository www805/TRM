
function userlogin() {
    var url=getActionURL(getactionid_manage().login_userlogin);

    var loginaccount =$('input[name="loginaccount"]').val();
    var password =$('input[name="password"]').val();
    var data={
        loginaccount:loginaccount,
        password:password
    };
    ajaxSubmit(url,data,callbackgetAdminInfoPage);
}