package com.manisha.movieTicketBookingSystem.services;

import com.manisha.movieTicketBookingSystem.exceptions.BadRequestException;
import com.manisha.movieTicketBookingSystem.models.Booking;
import com.manisha.movieTicketBookingSystem.providers.InMemorySeatLockProvider;
import com.manisha.movieTicketBookingSystem.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PaymentsService {
    Map<Booking, Integer> bookingFailures;
    private final int allowedRetries;
    private final SeatLockProvider seatLockProvider;

    public PaymentsService(int allowedRetries,
                           InMemorySeatLockProvider inMemorySeatLockProvider) {
        this.allowedRetries = allowedRetries;
        this.seatLockProvider = inMemorySeatLockProvider;
        bookingFailures=new HashMap<>();
    }

    public void processPaymentFailed(@NonNull final  Booking booking, String user)
            throws BadRequestException {
        if(!booking.getUser().equals(user)){
            throw new BadRequestException("Payment failed wrong user.");
        }
        if(!bookingFailures.containsKey(booking)){
            bookingFailures.put(booking,0);
        }
        final int currentFailuresCount=bookingFailures.get(booking);
        final int newFailuresCount=currentFailuresCount+1;
        bookingFailures.put(booking,newFailuresCount);
        if(newFailuresCount>allowedRetries){
            seatLockProvider.unLockSeats(booking.getShow(),booking.getSeatsBooked(),user);
        }

    }
}
