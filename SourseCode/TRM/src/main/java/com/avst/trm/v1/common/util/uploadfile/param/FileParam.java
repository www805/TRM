package com.avst.trm.v1.common.util.uploadfile.param;

import java.util.Map;

/**
 * 上传文件的对象，这里可以上传文件的类型决定了T的参数对象
 * 每一个上传接口需要的参数都不一样所以用T
 * @param <T>
 */
public class FileParam<T> {

    private T uploadparam;//该上传文件的参数集合

    private String filePath;//上传文件本地存储的路径

    private String actionURL;//上传的服务器的接口地址

    private boolean bool=false;//上传是否成功

    public T getUploadparam() {
        return uploadparam;
    }

    public void setUploadparam(T uploadparam) {
        this.uploadparam = uploadparam;
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
