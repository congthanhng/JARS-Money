package com.cong.thanh.jarsdemo;

public class TaiKhoan {
    private int id;
    private String TenTK;
    private String SoTien;
    private String GhiChu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenTK() {
        return TenTK;
    }

    public void setTenTK(String tenTK) {
        TenTK = tenTK;
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

    public TaiKhoan(int id, String tenTK, String soTien, String ghiChu) {
        this.id = id;
        TenTK = tenTK;
        SoTien = soTien;
        GhiChu = ghiChu;
    }
}
