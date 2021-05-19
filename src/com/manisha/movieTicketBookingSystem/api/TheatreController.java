package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Screen;
import com.manisha.movieTicketBookingSystem.models.Theatre;
import com.manisha.movieTicketBookingSystem.services.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    public String createTheatre(@NonNull final String theatreName){
        return theatreService.createTheatre(theatreName).getId();
    }
     public String createScreenInTheatre(@NonNull final String screenName,
                                         @NonNull final String theatreName)
             throws NotFoundException {
        final Theatre theatre=theatreService.getTheatre(theatreName);
        return  theatreService.createScreenInTheatre(screenName,theatre).getId();
     }
     public String createSeatInScreen(@NonNull final  String screenId,@NonNull final int rowNo,
                                      @NonNull final int seatNo) throws NotFoundException {
        final Screen screen=theatreService.getScreen(screenId);
        return theatreService.createSeatInScreen(screen,rowNo,seatNo).getId();
     }
}
