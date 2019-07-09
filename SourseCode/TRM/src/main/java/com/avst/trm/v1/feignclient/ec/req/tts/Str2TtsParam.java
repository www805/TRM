package com.avst.trm.v1.feignclient.ec.req.tts;

public class Str2TtsParam extends BaseParam{

    private String text;//需要识别的text文件

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
