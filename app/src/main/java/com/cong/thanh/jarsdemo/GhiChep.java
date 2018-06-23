package com.cong.thanh.jarsdemo;

import java.util.Calendar;
import java.util.Date;

public class GhiChep {
//    private int imgHinhDanhSach;
    private int id;
    private String SoTien;
    private String DienGiai;
    private String jars;
    private String NameMucChi;
    private String ngaychi;

    public String getNgaychi() {
        return ngaychi;
    }

    public void setNgaychi(String ngaychi) {
        this.ngaychi = ngaychi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoTien() {
        return SoTien;
    }

    public void setSoTien(String soTien) {
        SoTien = soTien;
    }

    public String getDienGiai() {
        return DienGiai;
    }

    public void setDienGiai(String dienGiai) {
        DienGiai = dienGiai;
    }

    public String getJars() {
        return jars;
    }

    public void setJars(String jars) {
        this.jars = jars;
    }

    public String getNameMucChi() {
        return NameMucChi;
    }

    public void setNameMucChi(String nameMucChi) {
        NameMucChi = nameMucChi;
    }

    public GhiChep(int id, String soTien, String dienGiai, String jars, String nameMucChi, String ngaychi) {
        this.id = id;
        SoTien = soTien;
        DienGiai = dienGiai;
        this.jars = jars;
        NameMucChi = nameMucChi;
        this.ngaychi = ngaychi;
    }
}
