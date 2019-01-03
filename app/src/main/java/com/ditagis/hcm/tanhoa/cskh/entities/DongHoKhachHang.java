package com.ditagis.hcm.tanhoa.cskh.entities;

public class DongHoKhachHang {
    private String danhBa;
    private String hopDong;
    private String tenKH;
    private String diaChi;
    private String sdt;
    private int tyLeSH;
    private int tyLeSX;
    private int tyLeDV;
    private int tyLeHC;
    private int GB;
    private int DM;
    private int chiSoCu;
    private int chiSoMoi;
    private int tieuThu;
    private double BVMT;
    private double thueVAT;
    private double thanhTien;
    private int nam;
    private int ky;
    private int tieuThuCu;
    private int tieuThuMoi;
    private double tongTien;



    public DongHoKhachHang(String danhBa, String hopDong, String tenKH, String diaChi, String sdt, int tyLeSH, int tyLeSX, int tyLeDV, int tyLeHC, int GB, int DM, int chiSoCu, int chiSoMoi, int tieuThu, double BVMT, double thueVAT, double thanhTien, int nam, int ky, int tieuThuCu, int tieuThuMoi, double tongTien) {
        this.danhBa = danhBa;
        this.hopDong = hopDong;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.tyLeSH = tyLeSH;
        this.tyLeSX = tyLeSX;
        this.tyLeDV = tyLeDV;
        this.tyLeHC = tyLeHC;
        this.GB = GB;
        this.DM = DM;
        this.chiSoCu = chiSoCu;
        this.chiSoMoi = chiSoMoi;
        this.tieuThu = tieuThu;
        this.BVMT = BVMT;
        this.thueVAT = thueVAT;
        this.thanhTien = thanhTien;
        this.nam = nam;
        this.ky = ky;
        this.tieuThuCu = tieuThuCu;
        this.tieuThuMoi = tieuThuMoi;
        this.tongTien = tongTien;
    }

    public String getDanhBa() {
        return danhBa;
    }

    public String getHopDong() {
        return hopDong;
    }

    public String getTenKH() {
        return tenKH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public int getTyLeSH() {
        return tyLeSH;
    }

    public int getTyLeSX() {
        return tyLeSX;
    }

    public int getTyLeDV() {
        return tyLeDV;
    }

    public int getTyLeHC() {
        return tyLeHC;
    }

    public int getGB() {
        return GB;
    }

    public int getDM() {
        return DM;
    }

    public int getChiSoCu() {
        return chiSoCu;
    }

    public int getChiSoMoi() {
        return chiSoMoi;
    }

    public int getTieuThu() {
        return tieuThu;
    }

    public double getBVMT() {
        return BVMT;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public int getNam() {
        return nam;
    }

    public int getKy() {
        return ky;
    }

    public int getTieuThuCu() {
        return tieuThuCu;
    }

    public int getTieuThuMoi() {
        return tieuThuMoi;
    }

    public double getTongTien() {
        return tongTien;
    }
}
