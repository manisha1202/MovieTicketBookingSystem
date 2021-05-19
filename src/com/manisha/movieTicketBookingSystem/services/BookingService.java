package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.exceptions.BadRequestException;
import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.exceptions.SeatPermanentlyUnavailableException;
import com.manisha.movieTicketBookingSystem.models.Booking;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;
import com.manisha.movieTicketBookingSystem.providers.InMemorySeatLockProvider;
import com.manisha.movieTicketBookingSystem.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    //stores all the bookings
    private final HashMap<String, Booking> showBookings;
    //acquires lock on the seat
    private SeatLockProvider seatLockProvider;

    public BookingService(
            InMemorySeatLockProvider inMemorySeatLockProvider) {
        this.seatLockProvider = inMemorySeatLockProvider;
        this.showBookings = new HashMap<>();
    }

    public List<Seat> getBookedSeats(@NonNull final Show show) {
        return getAllBookings(show).stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeatsBooked)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Booking> getAllBookings(@NonNull final Show show) {
        List<Booking> response = new ArrayList<>();
        for (Booking booking : showBookings.values()) {
            if (booking.getShow().equals(show)) {
                response.add(booking);
            }
        }
        return response;
    }

    public Booking createBooking(@NonNull final Show show, @NonNull final List<Seat> seats,
                                 @NonNull final String userId)
            throws SeatPermanentlyUnavailableException {
        if (anySeatAlreadyBooked(show, seats)) {
            throw new SeatPermanentlyUnavailableException("Seat unavailable!!!");
        }
        //acquire lock on the seat
        seatLockProvider.lockSeats(show, seats, userId);
        final String bookingId = UUID.randomUUID().toString();
        //do the booking
        final Booking booking = new Booking(bookingId, show, userId, seats);
        showBookings.put(bookingId, booking);
        return booking;
    }

    private boolean anySeatAlreadyBooked(final Show show, final List<Seat> seats) {
        final List<Seat> bookedSeats = getBookedSeats(show);
        for (Seat seat : seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    public void confirmBooking(@NonNull final Booking booking, @NonNull final String user)
            throws InvalidStateException, BadRequestException {
        if (!booking.getUser().equals(user)) {
            throw new BadRequestException("Payment failed Wrong User");
        }
        for (Seat seat : booking.getSeatsBooked()) {
            if (!seatLockProvider.validateLock(booking.getShow(), seat, user)) {
                throw new BadRequestException("Booking not confirmed wrong user.");
            }
        }
        booking.confirmBooking();
    }

    public Booking getBooking(String bookingId) throws NotFoundException {
        if (!showBookings.containsKey(bookingId)) {
            throw new NotFoundException("Booking id not found.");
        }
        return showBookings.get(bookingId);
    }
}
