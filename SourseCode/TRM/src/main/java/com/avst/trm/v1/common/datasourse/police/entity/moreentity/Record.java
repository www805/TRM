package com.avst.trm.v1.common.datasourse.police.entity.moreentity;

import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordreal;

import java.util.List;

/**
 * 笔录录音文件
 */
public class Record extends Police_record {
    private String recorddownurl;//总录音下载地址

    private String recordrealurl;//总录音真实地址

    private String pdfdownurl;//pdf下载地址

    private String pdfrealurl;//pdf真实地址

    private List<Recordreal> recordreals;//笔录实时文件s

    private List<Problem> problems;//笔录的问题模板

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
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

    public List<Recordreal> getRecordreals() {
        return recordreals;
    }

    public void setRecordreals(List<Recordreal> recordreals) {
        this.recordreals = recordreals;
    }
}
