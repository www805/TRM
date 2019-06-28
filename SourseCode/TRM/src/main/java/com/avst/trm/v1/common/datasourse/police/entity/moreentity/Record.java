package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;

import java.util.List;

/**
 * 笔录录音文件
 */
public class Record extends Police_record {
    private String recorddownurl;//总录音下载地址

    private String recordrealurl;//总录音真实地址

    private String pdfdownurl;//pdf下载地址

    private String pdfrealurl;//pdf真实地址

    private String worddownurl;

    private String wordrealurl;

    private List<RecordToProblem> problems;//笔录问题

    private RecordUserInfos recordUserInfos;//笔录询问 和被询问人信息

    private Police_arraignment police_arraignment;//提讯信息

    private  CaseAndUserInfo caseAndUserInfo;//案件信息

    private Integer mcbool;//会议状态

    public String getWorddownurl() {
        return worddownurl;
    }

    public void setWorddownurl(String worddownurl) {
        this.worddownurl = worddownurl;
    }

    public String getWordrealurl() {
        return wordrealurl;
    }

    public void setWordrealurl(String wordrealurl) {
        this.wordrealurl = wordrealurl;
    }

    public Integer getMcbool() {
        return mcbool;
    }

    public void setMcbool(Integer mcbool) {
        this.mcbool = mcbool;
    }

    public CaseAndUserInfo getCaseAndUserInfo() {
        return caseAndUserInfo;
    }

    public void setCaseAndUserInfo(CaseAndUserInfo caseAndUserInfo) {
        this.caseAndUserInfo = caseAndUserInfo;
    }

    public Police_arraignment getPolice_arraignment() {
        return police_arraignment;
    }

    public void setPolice_arraignment(Police_arraignment police_arraignment) {
        this.police_arraignment = police_arraignment;
    }

    public RecordUserInfos getRecordUserInfos() {
        return recordUserInfos;
    }

    public void setRecordUserInfos(RecordUserInfos recordUserInfos) {
        this.recordUserInfos = recordUserInfos;
    }

    public List<RecordToProblem> getProblems() {
        return problems;
    }

    public void setProblems(List<RecordToProblem> problems) {
        this.problems = problems;
    }

    public String getRecorddownurl() {
        return recorddownurl;
    }

    public void setRecorddownurl(String recorddownurl) {
        this.recorddownurl = recorddownurl;
    }

    public String getRecordrealurl() {
        return recordrealurl;
    }

    public void setRecordrealurl(String recordrealurl) {
        this.recordrealurl = recordrealurl;
    }

    public String getPdfdownurl() {
        return pdfdownurl;
    }

    public void setPdfdownurl(String pdfdownurl) {
        this.pdfdownurl = pdfdownurl;
    }

    public String getPdfrealurl() {
        return pdfrealurl;
    }

    public void setPdfrealurl(String pdfrealurl) {
        this.pdfrealurl = pdfrealurl;
    }

}
