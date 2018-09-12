package com.ditagis.hcm.tanhoa.cskh.entity;

import com.esri.arcgisruntime.geometry.Point;

import java.util.Date;

public class DiemSuCo {
    private String idSuCo;
    private short trangThai;
    private String ghiChu;
    private String nguoiCapNhat;
    private String email;
    private Date ngayCapNhat;
    private Date ngayThongBao;
    private String vitri;
    private String sdt;
    private String nguyenNhan;
    private Point point;
    private byte[] image;
    private short hinhThucPhatHien;

    DiemSuCo() {
    }

    public short getHinhThucPhatHien() {
        return hinhThucPhatHien;
    }

    public void setHinhThucPhatHien(short hinhThucPhatHien) {
        this.hinhThucPhatHien = hinhThucPhatHien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Point getPoint() {
        return point;
    }

    public byte[] getImage() {
        return image;
    }

    public void setIdSuCo(String idSuCo) {
        this.idSuCo = idSuCo;
    }

    public void setTrangThai(short trangThai) {
        this.trangThai = trangThai;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setNguoiCapNhat(String nguoiCapNhat) {
        this.nguoiCapNhat = nguoiCapNhat;
    }

    public void setNgayCapNhat(Date ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public void setNgayThongBao(Date ngayThongBao) {
        this.ngayThongBao = ngayThongBao;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setNguyenNhan(String nguyenNhan) {
        this.nguyenNhan = nguyenNhan;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getIdSuCo() {
        return idSuCo;
    }

    public short getTrangThai() {
        return trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getNguoiCapNhat() {
        return nguoiCapNhat;
    }

    public Date getNgayCapNhat() {
        return ngayCapNhat;
    }

    public Date getNgayThongBao() {
        return ngayThongBao;
    }

    public String getVitri() {
        return vitri;
    }

    public String getSdt() {
        return sdt;
    }

    public String getNguyenNhan() {
        return nguyenNhan;
    }
}
