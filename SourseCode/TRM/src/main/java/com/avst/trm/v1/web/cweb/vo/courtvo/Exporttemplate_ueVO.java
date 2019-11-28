package com.avst.trm.v1.web.cweb.vo.courtvo;

public class Exporttemplate_ueVO {
    private Integer exporttype;//导出类型：1word 2pdf

    private String word_downurl;//word导出地址

    private String pdf_downurl;//pdf导出地址

    public Integer getExporttype() {
        return exporttype;
    }

    public void setExporttype(Integer exporttype) {
        this.exporttype = exporttype;
    }

    public String getWord_downurl() {
        return word_downurl;
    }

    public void setWord_downurl(String word_downurl) {
        this.word_downurl = word_downurl;
    }

    public String getPdf_downurl() {
        return pdf_downurl;
    }

    public void setPdf_downurl(String pdf_downurl) {
        this.pdf_downurl = pdf_downurl;
    }
}
