package com.manisha.movieTicketBookingSystem.providers;

import com.manisha.movieTicketBookingSystem.exceptions.SeatTemporaryUnavailableExceptions;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.SeatLock;
import com.manisha.movieTicketBookingSystem.models.Show;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InMemorySeatLockProvider implements SeatLockProvider{
    private final HashMap<Show, HashMap<Seat, SeatLock>> locks;
    private final int lockTimeout;

    public InMemorySeatLockProvider(@NonNull final int lockTimeout) {
        this.lockTimeout = lockTimeout;
        this.locks=new HashMap<>();
    }

    @Override
    synchronized public void lockSeats(Show show, List<Seat> seats, String user) {
        for(Seat seat:seats){
            if(isSeatLocked(seat,show)){
                throw new SeatTemporaryUnavailableExceptions();
            }
        }
        for(Seat seat:seats){
            lockSeat(show,seat,user,lockTimeout);
        }
    }

    private void lockSeat(Show show, Seat seat, String user,int timeoutInSeconds) {
        if(!locks.containsKey(show)){
            locks.put(show,new HashMap<>());
        }
        final SeatLock seatLock=new SeatLock(seat,show,timeoutInSeconds,new Date(),user);
        locks.get(show).put(seat,seatLock);
    }

    private boolean isSeatLocked(Seat seat,Show show) {
        return locks.containsKey(show) && locks.get(show).containsKey(seat) && !locks.get(show).get(seat).isLockExpired();
    }

    @Override
    public void unLockSeat(Show show, Seat seat, String user) {

        if(!locks.containsKey(show)){
            return;
        }
        locks.get(show).remove(seat);
    }
    public  void unLockSeats(Show show ,List<Seat> seats,String user){
        for(Seat seat:seats){
            if(validateLock(show,seat,user)){
                unLockSeat(show,seat,user);
            }
        }
    }

    @Override
    public boolean validateLock(Show show, Seat seat, String user) {
        return isSeatLocked(seat,show)&& locks.get(show).get(seat).getLockedBy().equals(user);
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        final List<Seat> lockedSeats = new ArrayList<>();
        if (!locks.containsKey(show)) {
            return lockedSeats;
        }


        for (Seat seat : locks.get(show).keySet()) {

            if (isSeatLocked(seat,show)) {
                lockedSeats.add(seat);
            }
        }
        return lockedSeats;
    }
}
