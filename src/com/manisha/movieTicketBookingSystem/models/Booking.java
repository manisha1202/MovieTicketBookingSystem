package com.manisha.movieTicketBookingSystem.models;

import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Booking {
    @EqualsAndHashCode.Include
    private final String id;
    private final Show show;
    private final String user;
    private final List<Seat> seatsBooked;
    private BookingStatus bookingStatus;

    public Booking(@NonNull String id, @NonNull Show show, @NonNull String user, @NonNull
            List<Seat> seats) {
        this.id = id;
        this.show = show;
        this.user = user;
        this.seatsBooked = seats;
        this.bookingStatus = BookingStatus.Created;
    }

    public boolean isConfirmed() {
        return this.bookingStatus == BookingStatus.Confirmed;
    }

    public void confirmBooking() throws InvalidStateException {
        if (this.bookingStatus != BookingStatus.Created) {
            throw new InvalidStateException("");
        }
        this.bookingStatus = BookingStatus.Confirmed;
    }

    public void expireBooking() throws InvalidStateException {
        if (this.bookingStatus != BookingStatus.Created) {
            throw new InvalidStateException("");
        }
        this.bookingStatus = BookingStatus.Expired;
    }
}
