package com.avst.trm.v1.feignclient.ec.vo.ph;

/**
 * 实时相机实时图像获取
 */
public class GetPolygraphRealTimeImageVO  {

    private int imgType;//1 base64,2下载地址

    private String imgUrl;//图片下载地址

    private String base64;//base64格式存储的图片

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
