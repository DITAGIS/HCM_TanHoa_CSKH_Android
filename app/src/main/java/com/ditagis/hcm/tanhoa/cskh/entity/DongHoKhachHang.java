package com.ditagis.hcm.tanhoa.cskh.entity;

public class DongHoKhachHang {
    private String danhBa;
    private String may;
    private int gb;
    private int dm;
    private double tienNuoc;
    private int thueVAT;
    private int phiBVMT;
    private double tongTien;
    private int cscu;
    private int csmoi;
    private int tieuthucu;
    private int tieuthumoi;
    private int nam;
    private int ky;
    private int dot;

    public DongHoKhachHang(String danhBa) {
        this.danhBa = danhBa;
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

    public int getDot() {
        return dot;
    }

    public void setDot(int dot) {
        this.dot = dot;
    }

    public String getDanhBa() {
        return danhBa;
    }

    public void setDanhBa(String danhBa) {
        this.danhBa = danhBa;
    }

    public String getMay() {
        return may;
    }

    public void setMay(String may) {
        this.may = may;
    }

    public int getGb() {
        return gb;
    }

    public void setGb(int gb) {
        this.gb = gb;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public double getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(double tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public int getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(int thueVAT) {
        this.thueVAT = thueVAT;
    }

    public int getPhiBVMT() {
        return phiBVMT;
    }

    public void setPhiBVMT(int phiBVMT) {
        this.phiBVMT = phiBVMT;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public int getCscu() {
        return cscu;
    }

    public void setCscu(int cscu) {
        this.cscu = cscu;
    }

    public int getCsmoi() {
        return csmoi;
    }

    public void setCsmoi(int csmoi) {
        this.csmoi = csmoi;
    }

    public int getTieuthucu() {
        return tieuthucu;
    }

    public void setTieuthucu(int tieuthucu) {
        this.tieuthucu = tieuthucu;
    }

    public int getTieuthumoi() {
        return tieuthumoi;
    }

    public void setTieuthumoi(int tieuthumoi) {
        this.tieuthumoi = tieuthumoi;
    }
}
