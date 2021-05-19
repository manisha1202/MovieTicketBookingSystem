package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Movie;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.UUID;


public class MovieService {
    private final HashMap<String,Movie> movies;

    public MovieService() {
        movies=new HashMap<>();
    }

    public Movie getMovie(String movieId) throws NotFoundException {
        if(!movies.containsKey(movieId)){
            throw new NotFoundException("Movie id not found.");
        }
        return movies.get(movieId);
    }

    public Movie createMovie(String movieName) {
        String movieId= UUID.randomUUID().toString();
        Movie movie=new Movie(movieId,movieName);
        movies.put(movieId,movie);
        return movie;
    }
}
