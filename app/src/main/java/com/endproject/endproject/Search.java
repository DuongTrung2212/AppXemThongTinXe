package com.endproject.endproject;

import com.google.gson.annotations.SerializedName;

public class Search {
    @SerializedName("id")
    private int idCar;

    @SerializedName("image")
    String imgCar;
    @SerializedName("image")
    String img_bg;

    @SerializedName("name")
    String nameCar;
    String companyCar;

    public Search(int idCar, String imgCar, String img_bg, String nameCar, String companyCar) {
        this.idCar = idCar;
        this.imgCar = imgCar;
        this.img_bg = img_bg;
        this.nameCar = nameCar;
        this.companyCar = companyCar;
    }

    public int getIdCar() {
        return idCar;
    }

    public void setIdCar(int idCar) {
        this.idCar = idCar;
    }

    public String getImgCar() {
        return imgCar;
    }

    public void setImgCar(String imgCar) {
        this.imgCar = imgCar;
    }

    public String getImg_bg() {
        return img_bg;
    }

    public void setImg_bg(String img_bg) {
        this.img_bg = img_bg;
    }

    public String getNameCar() {
        return nameCar;
    }

    public void setNameCar(String nameCar) {
        this.nameCar = nameCar;
    }

    public String getCompanyCar() {
        return companyCar;
    }

    public void setCompanyCar(String companyCar) {
        this.companyCar = companyCar;
    }
}
