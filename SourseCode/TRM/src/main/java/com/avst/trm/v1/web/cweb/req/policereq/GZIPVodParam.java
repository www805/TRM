package com.avst.trm.v1.web.cweb.req.policereq;

/**
 * 打包VOD回放的请求
 */
public class GZIPVodParam {

    private String iid;//数据存储的iid

    private String zipfilename;//这个打包文件的文件名，不带后缀的那种

    public String getZipfilename() {
        return zipfilename;
    }

    public void setZipfilename(String zipfilename) {
        this.zipfilename = zipfilename;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}
