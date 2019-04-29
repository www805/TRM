package com.avst.trm.v1.common.datasourse.base.entity.moreentity;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_downserver;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DownserverAndDatainfo extends Base_datasheet_downserver {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastuploadtime;

    private Integer uploadcount;

    private String upserverip;

    private String dsdssid;//base_datasynchroni_downserver的ssid

    private String dataname;

    private String dataname_cn;

    private String mappername;

    private String dssid;//base_datainfo的ssid

    private String  typename;//类型名称

    public Date getLastuploadtime() {
        return lastuploadtime;
    }

    public void setLastuploadtime(Date lastuploadtime) {
        this.lastuploadtime = lastuploadtime;
    }

    public Integer getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(Integer uploadcount) {
        this.uploadcount = uploadcount;
    }

    public String getUpserverip() {
        return upserverip;
    }

    public void setUpserverip(String upserverip) {
        this.upserverip = upserverip;
    }

    public String getDsdssid() {
        return dsdssid;
    }

    public void setDsdssid(String dsdssid) {
        this.dsdssid = dsdssid;
    }

    @Override
    public String getDataname() {
        return dataname;
    }

    @Override
    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getDataname_cn() {
        return dataname_cn;
    }

    public void setDataname_cn(String dataname_cn) {
        this.dataname_cn = dataname_cn;
    }

    public String getMappername() {
        return mappername;
    }

    public void setMappername(String mappername) {
        this.mappername = mappername;
    }

    public String getDssid() {
        return dssid;
    }

    public void setDssid(String dssid) {
        this.dssid = dssid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
