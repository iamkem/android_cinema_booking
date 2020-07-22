package com.example.vayo.Model;

public class BookingUser
{
    private String UserName;
    private String BookingId;

    public BookingUser(String userName, String bookingId) {
        UserName = userName;
        BookingId = bookingId;
    }

    public BookingUser() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }
}
