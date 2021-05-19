package com.manisha.movieTicketBookingSystem.exceptions;

public class SeatPermanentlyUnavailableException extends Exception{
    public SeatPermanentlyUnavailableException(String msg){
        super(msg);
    }
}
