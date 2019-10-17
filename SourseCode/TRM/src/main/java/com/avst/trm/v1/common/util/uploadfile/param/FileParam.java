package com.avst.trm.v1.common.util.uploadfile.param;

import java.util.Map;

public class FileParam {

    private Map<String,String> paramMap;//该上传文件的参数集合

    private String filePath;//上传文件本地存储的路径

    private String actionURL;//上传的服务器的接口地址

    private boolean bool=false;//上传是否成功

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getActionURL() {
        return actionURL;
    }

    public void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}
