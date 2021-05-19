package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.BadRequestException;
import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Booking;
import com.manisha.movieTicketBookingSystem.services.BookingService;
import com.manisha.movieTicketBookingSystem.services.PaymentsService;
import lombok.NonNull;

public class PaymentController {
    private final PaymentsService paymentsService;
    private final BookingService bookingService;

    public PaymentController(
            PaymentsService paymentsService,
            BookingService bookingService) {
        this.paymentsService = paymentsService;
        this.bookingService = bookingService;
    }

    public void paymentFailed(@NonNull final String bookingId,@NonNull final String user)
            throws NotFoundException, BadRequestException {
        Booking booking = bookingService.getBooking(bookingId);
        paymentsService.processPaymentFailed(booking,user);
    }
    public void paymentSuccess(@NonNull final String bookingId,@NonNull final String user)
            throws NotFoundException, InvalidStateException, BadRequestException {
        Booking booking=bookingService.getBooking(bookingId);
        bookingService.confirmBooking(booking,user);
    }
}
