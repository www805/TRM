package com.avst.trm.v1.outsideinterface.offerclientinterface.v1.police.vo;

public class ControlInfoParamVO {

    //服务注册名称
    private String servername;

    //服务器中文名字
    private String servertitle;

    private String servertitletwo;

    private Integer total_item;//总业务数

    private Integer use_item;//可使用业务数

    //页面地址
    private String url;

    //页面地址2
    private String urltwo;

    //登录账号
    private String loginusername;

    //登录密码
    private String loginpassword;

    //服务状态
    private Integer status;

    //创建时间
    private String createtime;

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getServertitle() {
        return servertitle;
    }

    public void setServertitle(String servertitle) {
        this.servertitle = servertitle;
    }

    public String getServertitletwo() {
        return servertitletwo;
    }

    public void setServertitletwo(String servertitletwo) {
        this.servertitletwo = servertitletwo;
    }

    public Integer getTotal_item() {
        return total_item;
    }

    public void setTotal_item(Integer total_item) {
        this.total_item = total_item;
    }

    public Integer getUse_item() {
        return use_item;
    }

    public void setUse_item(Integer use_item) {
        this.use_item = use_item;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrltwo() {
        return urltwo;
    }

    public void setUrltwo(String urltwo) {
        this.urltwo = urltwo;
    }

    public String getLoginusername() {
        return loginusername;
    }

    public void setLoginusername(String loginusername) {
        this.loginusername = loginusername;
    }

    public String getLoginpassword() {
        return loginpassword;
    }

    public void setLoginpassword(String loginpassword) {
        this.loginpassword = loginpassword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "ControlInfoParamVO{" +
                "servername='" + servername + '\'' +
                ", servertitle='" + servertitle + '\'' +
                ", servertitletwo='" + servertitletwo + '\'' +
                ", total_item=" + total_item +
                ", use_item=" + use_item +
                ", url='" + url + '\'' +
                ", urltwo='" + urltwo + '\'' +
                ", loginusername='" + loginusername + '\'' +
                ", loginpassword='" + loginpassword + '\'' +
                ", status=" + status +
                ", createtime='" + createtime + '\'' +
                '}';
    }

}

