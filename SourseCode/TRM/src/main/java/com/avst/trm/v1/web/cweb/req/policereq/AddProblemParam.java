package com.avst.trm.v1.web.cweb.req.policereq;

public class AddProblemParam{

    private Integer id;
    private String problem;
    private String referanswer;
    private String problemtypessid;
    private String problemtypessidV;

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

    public String getReferanswer() {
        return referanswer;
    }

    public void setReferanswer(String referanswer) {
        this.referanswer = referanswer;
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
}
