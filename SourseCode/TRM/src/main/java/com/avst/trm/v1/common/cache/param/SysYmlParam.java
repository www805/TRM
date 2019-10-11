package com.avst.trm.v1.common.cache.param;

import java.util.Map;

/**
 *
 */
public class SysYmlParam {
    private  Map<String, Object> avstYml;//配置文件的全部内容

    private  Map<String, Object> branchYml;//分支特性

    private  Map<String, Object> oemYml;//oem特性

    private String gnlist;//授权列表

    public String getGnlist() {
        return gnlist;
    }

    public void setGnlist(String gnlist) {
        this.gnlist = gnlist;
    }

    public Map<String, Object> getAvstYml() {
        return avstYml;
    }

    public void setAvstYml(Map<String, Object> avstYml) {
        this.avstYml = avstYml;
    }

    public Map<String, Object> getBranchYml() {
        return branchYml;
    }

    public void setBranchYml(Map<String, Object> branchYml) {
        this.branchYml = branchYml;
    }

    public Map<String, Object> getOemYml() {
        return oemYml;
    }

    public void setOemYml(Map<String, Object> oemYml) {
        this.oemYml = oemYml;
    }


}
