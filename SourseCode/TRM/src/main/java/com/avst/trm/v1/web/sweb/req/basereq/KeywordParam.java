package com.avst.trm.v1.web.sweb.req.basereq;

import com.avst.trm.v1.common.util.baseaction.Page;

public class KeywordParam extends Page {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
