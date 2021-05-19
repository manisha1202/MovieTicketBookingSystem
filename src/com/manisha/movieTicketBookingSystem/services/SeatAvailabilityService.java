package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;
import com.manisha.movieTicketBookingSystem.providers.InMemorySeatLockProvider;
import com.manisha.movieTicketBookingSystem.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeatAvailabilityService {
    private BookingService bookingService;
    private SeatLockProvider seatLockProvider;

    public SeatAvailabilityService(
            @NonNull final BookingService bookingService,
            @NonNull final InMemorySeatLockProvider inMemorySeatLockProvider) {
        this.bookingService = bookingService;
        this.seatLockProvider = inMemorySeatLockProvider;
    }

    public List<Seat> getAvailableSeats(@NonNull final Show show) {
        final List<Seat> allSeats = show.getScreen().getSeats();
        final List<Seat> unavailableSeats = getUnavailableSeats(show);
        final List<Seat> availableSeats = new ArrayList<>(allSeats);
        availableSeats.removeAll(unavailableSeats);
        return availableSeats;
    }

    private List<Seat> getUnavailableSeats(@NonNull final Show show) {
        final List<Seat> unavailableSeats = bookingService.getBookedSeats(show);
        unavailableSeats.addAll(seatLockProvider.getLockedSeats(show));
        return unavailableSeats;
    }
}
