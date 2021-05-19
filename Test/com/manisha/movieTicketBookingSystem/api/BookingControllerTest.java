package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.exceptions.SeatPermanentlyUnavailableException;
import com.manisha.movieTicketBookingSystem.models.Booking;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Show;
import com.manisha.movieTicketBookingSystem.services.BookingService;
import com.manisha.movieTicketBookingSystem.services.ShowService;
import com.manisha.movieTicketBookingSystem.services.TheatreService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.manisha.movieTicketBookingSystem.constants.TestConstants.*;
import static com.manisha.movieTicketBookingSystem.generator.MockGenerator.getSeat;
import static com.manisha.movieTicketBookingSystem.generator.MockGenerator.getShow;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    private ShowService showService;
    private TheatreService theatreService;
    private BookingService bookingService;
    private BookingController bookingController;

    @Before
    public void setup() {
        showService = mock(ShowService.class);
        theatreService = mock(TheatreService.class);
        bookingService = mock(BookingService.class);
        bookingController = new BookingController(showService, theatreService, bookingService);
    }

    @Test(expected = InvalidStateException.class)
    public void createBookingShouldThrowInvalidExceptionForInvalidShowId()
            throws InvalidStateException, NotFoundException, SeatPermanentlyUnavailableException {
        when(showService.getShow(anyString())).thenThrow(new InvalidStateException("Invalid show " +
                "Id"));
        bookingController
                .createBooking(TEST_USER_ID, TEST_SHOW_ID, Collections.singletonList(TEST_SEAT_ID));
        verify(bookingService, Mockito.never()).createBooking(any(Show.class), anyList(),
                anyString());
        verify(theatreService, Mockito.never()).getSeat(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void getSeatShouldThrowNotFoundExceptionForInvalidSeatId()
            throws NotFoundException, InvalidStateException, SeatPermanentlyUnavailableException {
        when(theatreService.getSeat(anyString())).thenThrow(new NotFoundException("Seat id not " +
                "found."));
        when(showService.getShow(TEST_SHOW_ID)).thenReturn(getShow());
        bookingController
                .createBooking(TEST_USER_ID, TEST_SHOW_ID, Collections.singletonList(TEST_SEAT_ID));
        verify(bookingService, Mockito.never())
                .createBooking(any(Show.class), anyList(), anyString());
        verify(showService, Mockito.times(1)).getShow(TEST_SHOW_ID);
    }


    @Test(expected = SeatPermanentlyUnavailableException.class)
    public void createBookingShouldThrowSeatPermanentlyUnavailableExceptionForAlreadyBookedSeat()
            throws InvalidStateException, NotFoundException, SeatPermanentlyUnavailableException {
        when(showService.getShow(TEST_SHOW_ID)).thenReturn(getShow());
        when(theatreService.getSeat(TEST_SEAT_ID))
                .thenReturn(new Seat(TEST_SEAT_ID, TEST_ROW_NUM, TEST_SEAT_NUM));
        when(bookingService.createBooking(any(Show.class), anyList(), anyString()))
                .thenThrow(new SeatPermanentlyUnavailableException("Seat unavailable"));
        bookingController
                .createBooking(TEST_USER_ID, TEST_SHOW_ID, Collections.singletonList(TEST_SEAT_ID));
        verify(showService, Mockito.times(1)).getShow(TEST_SHOW_ID);
        verify(theatreService, Mockito.times(1)).getSeat(TEST_SEAT_ID);
    }

    @Test
    public void createBookingShouldReturnBookingId() throws InvalidStateException,
            NotFoundException, SeatPermanentlyUnavailableException {
        when(showService.getShow(TEST_SHOW_ID)).thenReturn(getShow());
        when(theatreService.getSeat(TEST_SEAT_ID)).thenReturn(getSeat());
        when(bookingService.createBooking(any(Show.class), anyList(), anyString()))
                .thenReturn(new Booking(TEST_BOOKING_ID, getShow(), TEST_USER_ID,
                        Collections.singletonList(getSeat())));
        String bookingId = bookingController
                .createBooking(TEST_USER_ID, TEST_SHOW_ID, Collections.singletonList(
                        TEST_SEAT_ID));
        assertEquals(TEST_BOOKING_ID, bookingId);
    }
}
