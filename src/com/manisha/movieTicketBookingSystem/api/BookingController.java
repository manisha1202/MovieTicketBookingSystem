package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.exceptions.SeatPermanentlyUnavailableException;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;
import com.manisha.movieTicketBookingSystem.services.BookingService;
import com.manisha.movieTicketBookingSystem.services.ShowService;
import com.manisha.movieTicketBookingSystem.services.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BookingController {
    private final ShowService showService;
    private final TheatreService theatreService;
    private final BookingService bookingService;

    /**
     * @param userId  String
     * @param showId  String
     * @param seatIds list of string
     * @return the booking id.
     * @throws InvalidStateException when userId or showId or seatId is invalid.
     * @throws NotFoundException something
     */
    public String createBooking(@NonNull final String userId, @NonNull final String showId,
                                @NonNull final List<String> seatIds)
            throws InvalidStateException, NotFoundException, SeatPermanentlyUnavailableException {
        final Show show = showService.getShow(showId);
        final List<Seat> seats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = theatreService.getSeat(seatId);
            seats.add(seat);
        }
        return bookingService.createBooking(show, seats, userId).getId();
    }
}
