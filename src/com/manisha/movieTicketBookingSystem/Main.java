package com.manisha.movieTicketBookingSystem;

import com.manisha.movieTicketBookingSystem.api.BookingController;
import com.manisha.movieTicketBookingSystem.api.MovieController;
import com.manisha.movieTicketBookingSystem.api.ShowController;
import com.manisha.movieTicketBookingSystem.api.TheatreController;
import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.exceptions.SeatPermanentlyUnavailableException;
import com.manisha.movieTicketBookingSystem.models.Theatre;
import com.manisha.movieTicketBookingSystem.providers.InMemorySeatLockProvider;
import com.manisha.movieTicketBookingSystem.services.*;

import java.util.*;

public class Main {

    public static void main(String[] args)
            throws InvalidStateException, NotFoundException, SeatPermanentlyUnavailableException {
        // write your code here
        //create theatre
        TheatreService theatreService = new TheatreService();
        TheatreController theatreController = new TheatreController(theatreService);
        String theatreId = theatreController.createTheatre("Inox");
        System.out.println("theatreId: "+theatreId);
        Theatre theatre = theatreService.getTheatre(theatreId);

        //create Screen
        String screenId = theatreController.createScreenInTheatre("Screen 1", theatre.getId());
        System.out.println("screenId: "+screenId);

        //create seats
        String seatId = theatreController.createSeatInScreen(screenId, 1, 1);
        System.out.println("seatId: "+seatId);
        //create movie
        MovieService movieService = new MovieService();
        MovieController movieController = new MovieController(movieService);
        String movieId = movieController.createMovie("Avengers");
        System.out.println("movieId: "+movieId);

        //create show
        InMemorySeatLockProvider seatLockProvider = new InMemorySeatLockProvider(300);
        BookingService bookingService = new BookingService(seatLockProvider);
        SeatAvailabilityService seatAvailabilityService =
                new SeatAvailabilityService(bookingService, seatLockProvider);
        ShowService showService = new ShowService();
        ShowController showController = new ShowController(seatAvailabilityService, theatreService,
                showService, movieService);
        Date date = new Date();
        String showId = showController.createShow(movieId, screenId, date, 7200);
        System.out.println("showId: "+showId);
        //create booking
        BookingController bookingController = new BookingController(showService, theatreService,
                bookingService);
        List<String> availableSeatIds = showController.getListOfSeats(showId);
        System.out.println("availableSeatIds: "+availableSeatIds);
        List<String> seatIds = new ArrayList<>();
        seatIds.add(availableSeatIds.get(0));
        String bookingId = bookingController.createBooking("mini", showId, seatIds);
        System.out.println("bookingId: "+bookingId);
    }


}
