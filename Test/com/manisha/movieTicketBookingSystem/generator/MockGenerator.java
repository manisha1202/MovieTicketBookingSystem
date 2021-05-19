package com.manisha.movieTicketBookingSystem.generator;

import com.manisha.movieTicketBookingSystem.models.*;

import java.util.Collections;
import java.util.Date;

import static com.manisha.movieTicketBookingSystem.constants.TestConstants.*;

public final class MockGenerator {
    public static Show getShow() {
        return new Show(TEST_SHOW_ID, getMovie(), getScreen(), new Date(), 7200);
    }

    public static Movie getMovie() {
        return new Movie(TEST_MOVIE_ID, TEST_MOVIE_NAME);
    }

    public static Screen getScreen() {
        return new Screen(TEST_SCREEN_ID, TEST_SCREEN_NAME, getTheatre());
    }

    public static Theatre getTheatre() {
        return new Theatre(TEST_THEATRE_ID, TEST_THEATRE_NAME);
    }

    public static Seat getSeat() {
        return new Seat(TEST_SEAT_ID, TEST_ROW_NUM, TEST_SEAT_NUM);
    }
    public static Booking getBooking(){
        return new Booking(TEST_BOOKING_ID,getShow(),TEST_USER_ID,
                Collections.singletonList(getSeat()));
    }
}
