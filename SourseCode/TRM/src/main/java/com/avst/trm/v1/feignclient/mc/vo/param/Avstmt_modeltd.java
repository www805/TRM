package com.avst.trm.v1.feignclient.mc.vo.param;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * InnoDB free: 38912 kB
 * </p>
 *
 * @author Mht
 * @since 2019-05-13
 */
public class Avstmt_modeltd extends Model<Avstmt_modeltd> {

    private static final long serialVersionUID = 1L;

    /**
     * 会议模板通道
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 会议模板ssid
     */
    private String mtmodelssid;

    /**
     * 设备通道ssid
     */
    private String tdssid;

    /**
     * 嵌入式设备ssid
     */
    private String fdssid;

    /**
     * 测谎仪ssid
     */
    private String polygraphssid;

    /**
     * 是否需要测谎,1使用/-1不使用
     */
    private Integer usepolygraph;

    /**
     * 是否语音识别,1使用/-1不使用
     */
    private Integer useasr;

    /**
     * 语音识别服务ssid
     */
    private String asrssid;

    /**
     * 通道级别,1主麦/2副麦（用于关联特殊的处理，比如只有被提讯人才会进行测谎）
     */
    private Integer grade;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    private String ssid;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFdssid() {
        return fdssid;
    }

    public void setFdssid(String fdssid) {
        this.fdssid = fdssid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getMtmodelssid() {
        return mtmodelssid;
    }

    public void setMtmodelssid(String mtmodelssid) {
        this.mtmodelssid = mtmodelssid;
    }
    public String getTdssid() {
        return tdssid;
    }

    public void setTdssid(String tdssid) {
        this.tdssid = tdssid;
    }
    public String getPolygraphssid() {
        return polygraphssid;
    }

    public void setPolygraphssid(String polygraphssid) {
        this.polygraphssid = polygraphssid;
    }
    public Integer getUsepolygraph() {
        return usepolygraph;
    }

    public void setUsepolygraph(Integer usepolygraph) {
        this.usepolygraph = usepolygraph;
    }
    public Integer getUseasr() {
        return useasr;
    }

    public void setUseasr(Integer useasr) {
        this.useasr = useasr;
    }
    public String getAsrssid() {
        return asrssid;
    }

    public void setAsrssid(String asrssid) {
        this.asrssid = asrssid;
    }
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }
    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }
    public Integer getInteger1() {
        return integer1;
    }

    public void setInteger1(Integer integer1) {
        this.integer1 = integer1;
    }
    public Integer getInteger2() {
        return integer2;
    }

    public void setInteger2(Integer integer2) {
        this.integer2 = integer2;
    }
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Avstmt_modeltd{" +
        "id=" + id +
        ", mtmodelssid=" + mtmodelssid +
        ", tdssid=" + tdssid +
        ", polygraphssid=" + polygraphssid +
        ", usepolygraph=" + usepolygraph +
        ", useasr=" + useasr +
        ", asrssid=" + asrssid +
        ", grade=" + grade +
        ", createtime=" + createtime +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        ", ssid=" + ssid +
        "}";
    }
}
