package com.avst.trm.v1.web.cweb.vo.policevo;

import com.avst.trm.v1.common.util.gzip.GZIPCacheParam;

public class ExportUdiskProgressVO {
    private GZIPCacheParam gzipCacheParam;
    private String ssid;

    public GZIPCacheParam getGzipCacheParam() {
        return gzipCacheParam;
    }

    public void setGzipCacheParam(GZIPCacheParam gzipCacheParam) {
        this.gzipCacheParam = gzipCacheParam;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
