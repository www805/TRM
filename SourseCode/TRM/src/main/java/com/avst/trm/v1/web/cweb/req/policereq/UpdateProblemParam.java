package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;

public class UpdateProblemParam extends Police_problem {

    private String problemssid;

    private String problemtypessid;

    public String getProblemssid() {
        return problemssid;
    }

    public void setProblemssid(String problemssid) {
        this.problemssid = problemssid;
    }

    public String getProblemtypessid() {
        return problemtypessid;
    }

    public void setProblemtypessid(String problemtypessid) {
        this.problemtypessid = problemtypessid;
    }
}
