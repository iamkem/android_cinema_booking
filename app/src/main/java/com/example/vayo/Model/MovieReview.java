package com.example.vayo.Model;

public class MovieReview
{
    private String ID;
    private String MovieID;
    private String Review;
    private String Mark;

    public MovieReview(String ID, String movieID, String review, String mark) {
        this.ID = ID;
        MovieID = movieID;
        Review = review;
        Mark = mark;
    }

    public MovieReview() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMovieID() {
        return MovieID;
    }

    public void setMovieID(String movieID) {
        MovieID = movieID;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;
    }
}
