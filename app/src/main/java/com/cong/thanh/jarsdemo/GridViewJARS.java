package com.cong.thanh.jarsdemo;

public class GridViewJARS {
    private String NameJars;
    private String PhanTram;
    private int Hinhjars;
    private String TienBanDau;
    private String TienConLai;

    public GridViewJARS(String nameJars, String phanTram, int hinhjars, String tienBanDau, String tienConLai) {
        NameJars = nameJars;
        PhanTram = phanTram;
        Hinhjars = hinhjars;
        TienBanDau = tienBanDau;
        TienConLai = tienConLai;
    }

    public String getNameJars() {
        return NameJars;
    }

    public void setNameJars(String nameJars) {
        NameJars = nameJars;
    }

    public String getPhanTram() {
        return PhanTram;
    }

    public void setPhanTram(String phanTram) {
        PhanTram = phanTram;
    }

    public int getHinhjars() {
        return Hinhjars;
    }

    public void setHinhjars(int hinhjars) {
        Hinhjars = hinhjars;
    }

    public String getTienBanDau() {
        return TienBanDau;
    }

    public void setTienBanDau(String tienBanDau) {
        TienBanDau = tienBanDau;
    }

    public String getTienConLai() {
        return TienConLai;
    }

    public void setTienConLai(String tienConLai) {
        TienConLai = tienConLai;
    }
}
