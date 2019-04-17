package com.avst.trm.v1.common.datasourse.base.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 90112 kB
 * </p>
 *
 * @author Admin
 * @since 2019-04-17
 */
public class Base_keyword extends Model<Base_keyword> {

    private static final long serialVersionUID = 1L;

    /**
     * 关键字表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 字体颜色
     */
    private String color;

    /**
     * 关键字文本
     */
    private String text;

    /**
     * 背景颜色
     */
    private String backgroundcolor;

    /**
     * 替换字符：例如:*** 
     */
    private String replacetext;

    /**
     * 是否屏蔽：1屏蔽/-1不屏蔽；默认-1
     */
    private Integer shieldbool;

    /**
     * 创建时间
     */
    private Date createtime;

    private String ssid;

    private String string1;

    private String string2;

    private Integer integer1;

    private Integer integer2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }
    public String getReplacetext() {
        return replacetext;
    }

    public void setReplacetext(String replacetext) {
        this.replacetext = replacetext;
    }
    public Integer getShieldbool() {
        return shieldbool;
    }

    public void setShieldbool(Integer shieldbool) {
        this.shieldbool = shieldbool;
    }
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Base_keyword{" +
        "id=" + id +
        ", color=" + color +
        ", text=" + text +
        ", backgroundcolor=" + backgroundcolor +
        ", replacetext=" + replacetext +
        ", shieldbool=" + shieldbool +
        ", createtime=" + createtime +
        ", ssid=" + ssid +
        ", string1=" + string1 +
        ", string2=" + string2 +
        ", integer1=" + integer1 +
        ", integer2=" + integer2 +
        "}";
    }
}
