package com.ditagis.hcm.tanhoa.cskh.entity;

public class HoaDon {
    private String ky;
    private int nam;
    private String soHoaDon;
    private String ngayCapNhat;
    private int tienHD;

    public HoaDon() {
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public String getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(String soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public String getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(String ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getTienHD() {
        return tienHD;
    }

    public void setTienHD(int tienHD) {
        this.tienHD = tienHD;
    }
}
