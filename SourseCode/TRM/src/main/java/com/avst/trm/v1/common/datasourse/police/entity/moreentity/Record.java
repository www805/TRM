package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_arraignment;
import com.avst.trm.v1.common.datasourse.police.entity.Police_record;

import java.util.List;

/**
 * 笔录录音文件
 */
public class Record extends Police_record {
    private String pdfdownurl;//pdf下载地址

    private String pdfrealurl;//pdf真实地址

    private String worddownurl;

    private String wordrealurl;

    private String wordheaddownurl;

    private String wordheadrealurl;

    private String wordheaddownurl_html;

    private List<RecordToProblem> problems;//笔录问题

    private RecordUserInfos recordUserInfos;//笔录询问 和被询问人信息

    private Police_arraignment police_arraignment;//提讯信息

    private Case case_;

    private Integer mcbool;//会议状态

    private String recordtypename;//案件类型名称


    private Integer recordfilestate=-1;//视频文件状态针对已完成的笔录 -1未存在视频 0上传进行中 1已完成

    public Integer getRecordfilestate() {
        return recordfilestate;
    }

    public void setRecordfilestate(Integer recordfilestate) {
        this.recordfilestate = recordfilestate;
    }

    public String getWordheaddownurl_html() {
        return wordheaddownurl_html;
    }

    public void setWordheaddownurl_html(String wordheaddownurl_html) {
        this.wordheaddownurl_html = wordheaddownurl_html;
    }

    public String getWordheaddownurl() {
        return wordheaddownurl;
    }

    public void setWordheaddownurl(String wordheaddownurl) {
        this.wordheaddownurl = wordheaddownurl;
    }

    public String getWordheadrealurl() {
        return wordheadrealurl;
    }

    public void setWordheadrealurl(String wordheadrealurl) {
        this.wordheadrealurl = wordheadrealurl;
    }

    public Case getCase_() {
        return case_;
    }

    public void setCase_(Case case_) {
        this.case_ = case_;
    }

    public String getRecordtypename() {
        return recordtypename;
    }

    public void setRecordtypename(String recordtypename) {
        this.recordtypename = recordtypename;
    }

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
