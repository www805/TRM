package com.avst.trm.v1.common.util.gzip;

import java.util.HashMap;
import java.util.Map;

public class GZIPCacheParam {

    private int totalzipnum;//总共有多少个需要打包的文件

    private int overzipnum=0;//已经完成了多少个文件

    private Map<String,Boolean> filepathMap=new HashMap<String,Boolean>(); //KEY需要打包文件的路径，volue文件是否打包完成

    private long startTime;//开始zip的时间

    private String zipfilename;//这个压缩文件的名字，也是唯一标识，点播压缩就是iid

    public int getTotalzipnum() {
        return totalzipnum;
    }

    public void setTotalzipnum(int totalzipnum) {
        this.totalzipnum = totalzipnum;
    }

    public int getOverzipnum() {
        return overzipnum;
    }

    public void setOverzipnum(int overzipnum) {
        this.overzipnum = overzipnum;
    }

    public Map<String, Boolean> getFilepathMap() {
        return filepathMap;
    }

    public void setFilepathMap(Map<String, Boolean> filepathMap) {
        this.filepathMap = filepathMap;
    }

    public String getZipfilename() {
        return zipfilename;
    }

    public void setZipfilename(String zipfilename) {
        this.zipfilename = zipfilename;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
