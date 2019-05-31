package com.avst.trm.v1.feignclient.ec.vo.ph.cmparam;

/**
 * 解析cm测谎仪返回数据集合的实体类
 */
public class XBOX_GetResultParam {

    private Face_rectParam face_rect;

    private Integer age;

    private float br;

    private float bp;

    private Integer emotion;

    private float fps;

    private Integer gender;

    private boolean have_face;

    private float hr;

    private float hr_snr;

    private float hrv;

    private float relax;

    private float spo2;

    private Integer status;

    private float stress;

    private float stress_snr;

    public Face_rectParam getFace_rect() {
        return face_rect;
    }

    public void setFace_rect(Face_rectParam face_rect) {
        this.face_rect = face_rect;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public float getBr() {
        return br;
    }

    public void setBr(float br) {
        this.br = br;
    }

    public float getBp() {
        return bp;
    }

    public void setBp(float bp) {
        this.bp = bp;
    }

    public Integer getEmotion() {
        return emotion;
    }

    public void setEmotion(Integer emotion) {
        this.emotion = emotion;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public boolean isHave_face() {
        return have_face;
    }

    public void setHave_face(boolean have_face) {
        this.have_face = have_face;
    }

    public float getHr() {
        return hr;
    }

    public void setHr(float hr) {
        this.hr = hr;
    }

    public float getHr_snr() {
        return hr_snr;
    }

    public void setHr_snr(float hr_snr) {
        this.hr_snr = hr_snr;
    }

    public float getHrv() {
        return hrv;
    }

    public void setHrv(float hrv) {
        this.hrv = hrv;
    }

    public float getRelax() {
        return relax;
    }

    public void setRelax(float relax) {
        this.relax = relax;
    }

    public float getSpo2() {
        return spo2;
    }

    public void setSpo2(float spo2) {
        this.spo2 = spo2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public float getStress() {
        return stress;
    }

    public void setStress(float stress) {
        this.stress = stress;
    }

    public float getStress_snr() {
        return stress_snr;
    }

    public void setStress_snr(float stress_snr) {
        this.stress_snr = stress_snr;
    }
}
