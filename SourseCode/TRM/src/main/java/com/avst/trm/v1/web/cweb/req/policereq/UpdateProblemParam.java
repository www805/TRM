package com.avst.trm.v1.web.cweb.req.policereq;

public class UpdateProblemParam {

    private Integer id;

    //问题
    private String problem;

    //修改的类型
    private String problemtypessid;

    //原类型
    private String problemtypessidV;

    //参考答案
    private String referanswer;

    //问题ssid
    private String ssid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemtypessid() {
        return problemtypessid;
    }

    public void setProblemtypessid(String problemtypessid) {
        this.problemtypessid = problemtypessid;
    }

    public String getProblemtypessidV() {
        return problemtypessidV;
    }

    public void setProblemtypessidV(String problemtypessidV) {
        this.problemtypessidV = problemtypessidV;
    }

    public String getReferanswer() {
        return referanswer;
    }

    public void setReferanswer(String referanswer) {
        this.referanswer = referanswer;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
