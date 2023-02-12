package com.endproject.endproject;

public class Filter {
    String img_Company;
    String companyName;
    int id;

    public String getImg_Company() {
        return img_Company;
    }

    public void setImg_Company(String img_Company) {
        this.img_Company = img_Company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Filter(String img_Company, String companyName, int id) {
        this.img_Company = img_Company;
        this.companyName = companyName;
        this.id = id;
    }
}
