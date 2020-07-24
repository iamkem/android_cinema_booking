package com.example.cinemabooking.Model;

public class Showings {
    private String showing_ID;
    private String movie_ID;
    private String cinema_ID;

    public Showings(String showing_ID, String movie_ID, String cinema_ID) {
        this.showing_ID = showing_ID;
        this.movie_ID = movie_ID;
        this.cinema_ID = cinema_ID;
    }

    public Showings() {
    }

    public String getShowing_ID() {
        return showing_ID;
    }

    public void setShowing_ID(String showing_ID) {
        this.showing_ID = showing_ID;
    }

    public String getMovie_ID() {
        return movie_ID;
    }

    public void setMovie_ID(String movie_ID) {
        this.movie_ID = movie_ID;
    }

    public String getCinema_ID() {
        return cinema_ID;
    }

    public void setCinema_ID(String cinema_ID) {
        this.cinema_ID = cinema_ID;
    }
}
