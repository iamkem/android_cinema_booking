package com.example.vayo.Model;

public class Booking
{
    private String ID;
    private String MOVIE_NAME;
    private String CINEMA_NAME;
    private String PRICE;
    private String DATE;
    private String STATUS;
    private String SEATS;
    private String USERNAME;


    public Booking() {
    }



    public Booking(String id, String movie_name, String cinema_name, String price, String date, String status, String seats,String user)
    {
        ID=id;
        MOVIE_NAME = movie_name;
        CINEMA_NAME = cinema_name;
        PRICE = price;
        DATE = date;
        STATUS = status;
        SEATS = seats;
        USERNAME = user;

    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getSEATS() {
        return SEATS;
    }

    public void setSEATS(String SEATS) {
        this.SEATS = SEATS;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMOVIE_NAME() {
        return MOVIE_NAME;
    }

    public void setMOVIE_NAME(String MOVIE_NAME) {
        this.MOVIE_NAME = MOVIE_NAME;
    }

    public String getCINEMA_NAME() {
        return CINEMA_NAME;
    }

    public void setCINEMA_NAME(String CINEMA_NAME) {
        this.CINEMA_NAME = CINEMA_NAME;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
