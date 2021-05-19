package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.services.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.manisha.movieTicketBookingSystem.constants.TestConstants.TEST_MOVIE_ID;
import static com.manisha.movieTicketBookingSystem.constants.TestConstants.TEST_MOVIE_NAME;
import static com.manisha.movieTicketBookingSystem.generator.MockGenerator.getMovie;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieControllerTest {
    private MovieService movieService;
    private MovieController movieController;

    @Before
    public void setup() {
        movieService= mock(MovieService.class);
        movieController=new MovieController(movieService);
    }

    @Test
    public void createMovieShouldReturnMovieId(){
        when(movieService.createMovie(TEST_MOVIE_NAME)).thenReturn(getMovie());
        String movieId=movieController.createMovie(TEST_MOVIE_NAME);
        assertEquals(TEST_MOVIE_ID,movieId);
    }
}
