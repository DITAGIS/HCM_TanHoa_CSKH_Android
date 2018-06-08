package com.ditagis.hcm.tanhoa.cskh.entity;

public class LichSuDocSo {
    private int nam;
    private int ky;
    private String dot;
    private String may;
    private String nhanVienDS;

    public LichSuDocSo(String may) {
        this.may = may;
    }

    public LichSuDocSo() {

    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public int getKy() {
        return ky;
    }

    public void setKy(int ky) {
        this.ky = ky;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    public String getNhanVienDS() {
        return nhanVienDS;
    }

    public void setNhanVienDS(String nhanVienDS) {
        this.nhanVienDS = nhanVienDS;
    }
}
