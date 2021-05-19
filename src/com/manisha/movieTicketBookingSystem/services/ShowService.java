package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.ScreenAlreadyOccupiedException;
import com.manisha.movieTicketBookingSystem.models.Movie;
import com.manisha.movieTicketBookingSystem.models.Screen;
import com.manisha.movieTicketBookingSystem.models.Show;

import java.util.*;


public class ShowService {
    private final HashMap<String, Show> shows;

    public ShowService() {
        this.shows = new HashMap<>();
    }

    public Show getShow(String showId) throws InvalidStateException {
        if (!shows.containsKey(showId)) {
            throw new InvalidStateException("show id is not present ");
        }
        return shows.get(showId);
    }

    public Show createShow(Movie movie, Screen screen, Date startTime, int durationInSeconds) {
        if (!checkIfShowCreationAllowed(screen, startTime, durationInSeconds)) {
            throw new ScreenAlreadyOccupiedException();
        }
        String showId = UUID.randomUUID().toString();
        final Show show = new Show(showId, movie, screen, startTime, durationInSeconds);
        shows.put(showId, show);
        return show;
    }

    private boolean checkIfShowCreationAllowed(Screen screen, Date startTime, int durationInSeconds) {
        return true;
    }

    private List<Show> getShowsForScreen(final Screen screen) {
        final List<Show> response = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getScreen().equals(screen)) {
                response.add(show);
            }
        }
        return response;
    }
}
