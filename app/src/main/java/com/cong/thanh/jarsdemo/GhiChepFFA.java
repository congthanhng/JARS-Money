package com.cong.thanh.jarsdemo;

public class GhiChepFFA {
    private String MucChi,SoTien,GhiChu,NgayChi;

    public String getMucChi() {
        return MucChi;
    }

    public void setMucChi(String mucChi) {
        MucChi = mucChi;
    }

    public String getSoTien() {
        return SoTien;
    }

    public void setSoTien(String soTien) {
        SoTien = soTien;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getNgayChi() {
        return NgayChi;
    }

    public void setNgayChi(String ngayChi) {
        NgayChi = ngayChi;
    }

    public GhiChepFFA(String mucChi, String soTien, String ghiChu, String ngayChi) {
        MucChi = mucChi;
        SoTien = soTien;
        GhiChu = ghiChu;
        NgayChi = ngayChi;
    }
}
