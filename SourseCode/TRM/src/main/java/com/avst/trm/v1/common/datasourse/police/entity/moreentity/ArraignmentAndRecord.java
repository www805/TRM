package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;

import java.util.List;

/**
 * 提讯数据
 */
public class ArraignmentAndRecord extends Police_arraignment {
        private String recordname;

        private String recordbool;

        private  String recordadminname;

        private String adminname;

        private String recordssid;//笔录ssid

        private String recordtypessid;

        private String recordtypename;

        public String getRecordtypessid() {
            return recordtypessid;
        }

        public void setRecordtypessid(String recordtypessid) {
            this.recordtypessid = recordtypessid;
        }

    public String getRecordtypename() {
        return recordtypename;
    }

    public void setRecordtypename(String recordtypename) {
        this.recordtypename = recordtypename;
    }

    @Override
    public String getRecordssid() {
        return recordssid;
    }

    @Override
    public void setRecordssid(String recordssid) {
        this.recordssid = recordssid;
    }

    public String getRecordadminname() {
        return recordadminname;
    }

    public void setRecordadminname(String recordadminname) {
        this.recordadminname = recordadminname;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }

    public String getRecordbool() {
        return recordbool;
    }

    public void setRecordbool(String recordbool) {
        this.recordbool = recordbool;
    }

}
