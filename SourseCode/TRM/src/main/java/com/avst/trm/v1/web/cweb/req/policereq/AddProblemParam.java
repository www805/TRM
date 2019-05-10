package com.avst.trm.v1.web.cweb.req.policereq;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;

public class AddProblemParam extends Police_problem {

    private String problemtypessid;

    public String getProblemtypessid() {
        return problemtypessid;
    }

    public void setProblemtypessid(String problemtypessid) {
        this.problemtypessid = problemtypessid;
    }
}
