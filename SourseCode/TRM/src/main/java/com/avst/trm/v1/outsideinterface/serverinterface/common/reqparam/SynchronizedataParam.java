package com.avst.trm.v1.outsideinterface.serverinterface.common.reqparam;

/**
 * 真正同步数据的参数集合
 * @param <T>
 */
public class SynchronizedataParam<T> {

    private String dataname;//同步的表名

    private Integer type=1;//文件还是数据库，1是数据库，2是文件

    private String ssid;//当前插入的数据的ssid，泛型对象不好取值，外面写一个

    private T t;//存储的数据库对象

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
