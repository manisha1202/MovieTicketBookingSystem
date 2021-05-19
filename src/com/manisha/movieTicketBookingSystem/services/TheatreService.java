package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Screen;
import com.manisha.movieTicketBookingSystem.models.Seat;
import com.manisha.movieTicketBookingSystem.models.Theatre;
import lombok.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class TheatreService {
    private final HashMap<String, Theatre> theatres;
    private final HashMap<String, Screen> screens;
    private final HashMap<String, Seat> seats;

    public TheatreService() {
        this.theatres = new HashMap<>();
        this.screens = new HashMap<>();
        this.seats = new HashMap<>();
    }

    public Seat getSeat(@NonNull final String seatId) throws NotFoundException {
        if(!seats.containsKey(seatId)){
            throw new NotFoundException("Seat id not found.");
        }
        return seats.get(seatId);
    }
    public Theatre getTheatre(@NonNull final  String theatreId) throws NotFoundException {
        if(!theatres.containsKey(theatreId)){
            throw new NotFoundException("Theatre id not found.");
        }
        return theatres.get(theatreId);
    }
    public Screen getScreen(@NonNull final String screenId) throws NotFoundException {
        if(!screens.containsKey(screenId)){
            throw new NotFoundException("Screen id not found.");
        }
        return screens.get(screenId);
    }
    public Theatre createTheatre(@NonNull final String theatreName){
        String theatreId= UUID.randomUUID().toString();
        Theatre theatre=new Theatre(theatreId,theatreName);
        theatres.put(theatreId,theatre);
        return theatre;
    }
    public Screen createScreenInTheatre(@NonNull final String screenName,
                                        @NonNull final Theatre theatre){
        Screen screen=createScreen(screenName,theatre);
        theatre.addScreen(screen);
        return screen;
    }

    private Screen createScreen(String screenName, Theatre theatre) {
        String screenId=UUID.randomUUID().toString();
        Screen screen=new Screen(screenId,screenName,theatre);
        screens.put(screenId,screen);
        return screen;
    }

    public Seat createSeatInScreen(@NonNull final  Screen screen,@NonNull final int rowNo,
                                     @NonNull final int seatNo){
        String seatId=UUID.randomUUID().toString();
        Seat seat=new Seat(seatId,rowNo,seatNo);
        screen.addSeat(seat);
        seats.put(seatId,seat);
        return seat;
    }
}
