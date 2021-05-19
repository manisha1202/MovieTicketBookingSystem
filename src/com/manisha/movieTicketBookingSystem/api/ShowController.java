package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Movie;
import com.manisha.movieTicketBookingSystem.models.Screen;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;
import com.manisha.movieTicketBookingSystem.services.MovieService;
import com.manisha.movieTicketBookingSystem.services.SeatAvailabilityService;
import com.manisha.movieTicketBookingSystem.services.ShowService;
import com.manisha.movieTicketBookingSystem.services.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowController {

    private final SeatAvailabilityService seatAvailabilityService;
    private final TheatreService theatreService;
    private final ShowService showService;
    private final MovieService movieService;

    public String createShow(@NonNull final String movieId, @NonNull final String screenId,
                             @NonNull final Date startTime,
                             final int durationInSeconds) throws NotFoundException {
        final Screen screen=theatreService.getScreen(screenId);
        final Movie movie=movieService.getMovie(movieId);
        return showService.createShow(movie,screen,startTime,durationInSeconds).getId();

    }

    public List<String> getListOfSeats(@NonNull final String showId) throws InvalidStateException {
        final Show show=showService.getShow(showId);
        final List<Seat> availableSeats = seatAvailabilityService.getAvailableSeats(show);
        return availableSeats.stream().map(Seat::getId).collect(Collectors.toList());
    }
}
