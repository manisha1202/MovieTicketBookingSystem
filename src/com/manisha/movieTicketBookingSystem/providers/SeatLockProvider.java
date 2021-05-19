package com.manisha.movieTicketBookingSystem.providers;

import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;

import java.util.List;

public interface SeatLockProvider {
    void lockSeats(Show show,List<Seat> seats,String user);
    void unLockSeat(Show show,Seat seats, String user);
    void unLockSeats(Show show, List<Seat> seats, String user);
    boolean validateLock(Show show,Seat seat,String user);
    List<Seat> getLockedSeats(Show show);
}
