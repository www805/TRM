package com.avst.trm.v1.web.cweb.vo.policevo;

public class ExportWordVO {
    private String word_htmlpath;//预览html地址

    private String word_path;//下载地址

    public String getWord_htmlpath() {
        return word_htmlpath;
    }

    public void setWord_htmlpath(String word_htmlpath) {
        this.word_htmlpath = word_htmlpath;
    }

    public String getWord_path() {
        return word_path;
    }

    public void setWord_path(String word_path) {
        this.word_path = word_path;
    }
}
