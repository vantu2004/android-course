package com.vantu.gridview.model;

public class MonHoc {
    private String name;
    private String desscription;
    private int image;

    public MonHoc() {
    }

    public MonHoc(String name, String desscription, int image) {
        this.name = name;
        this.desscription = desscription;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesscription() {
        return desscription;
    }

    public void setDesscription(String desscription) {
        this.desscription = desscription;
    }
}
