package com.example.cinemabooking.Model;

public class Movies {
    private String ID;
    private String NAME;
    private String CATEGORY;
    private String DESCRIPTION;
    private String PRICE;
    private String LENGTH;
    private byte[] image;


    public Movies() {
    }

    public Movies(String id,String name, byte [] i) {
        ID=id;
        NAME = name;
        image = i;
    }


    public Movies(String id, String name, String category, String description, String price, String length, byte[] i)
    {
        ID=id;
        NAME = name;
        CATEGORY = category;
        DESCRIPTION = description;
        PRICE = price;
        LENGTH = length;
        image = i;
    }

    public Movies(String id, String name, String category, String description, String price, String length)
    {
        ID=id;
        NAME = name;
        CATEGORY = category;
        DESCRIPTION = description;
        PRICE = price;
        LENGTH = length;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getPRICE() {
        return PRICE;
    }

    public String getLENGTH() {
        return LENGTH;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public void setLENGTH(String LENGTH) {
        this.LENGTH = LENGTH;
    }

    public String getName() {
        return NAME;
    }

    public String getId(){return ID;}

    public void setId(String id){ID=id;}

    public void setName(String movie) {
        NAME = movie;
    }

}
