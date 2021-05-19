package com.manisha.movieTicketBookingSystem.api;

import com.manisha.movieTicketBookingSystem.exceptions.BadRequestException;
import com.manisha.movieTicketBookingSystem.exceptions.InvalidStateException;
import com.manisha.movieTicketBookingSystem.exceptions.NotFoundException;
import com.manisha.movieTicketBookingSystem.models.Booking;
import com.manisha.movieTicketBookingSystem.services.BookingService;
import com.manisha.movieTicketBookingSystem.services.PaymentsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.manisha.movieTicketBookingSystem.constants.TestConstants.TEST_BOOKING_ID;
import static com.manisha.movieTicketBookingSystem.constants.TestConstants.TEST_USER_ID;
import static com.manisha.movieTicketBookingSystem.generator.MockGenerator.getBooking;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    private PaymentsService paymentsService;
    private BookingService bookingService;
    private PaymentController paymentController;

    @Before
    public void setup() {
        paymentsService = mock(PaymentsService.class);
        bookingService = mock(BookingService.class);
        paymentController = new PaymentController(paymentsService, bookingService);
    }

    @Test(expected = NotFoundException.class)
    public void paymentFailedShouldReturnNotFoundException()
            throws NotFoundException, BadRequestException {
        doThrow(new NotFoundException("Booking id not found")).when(bookingService)
                .getBooking(TEST_BOOKING_ID);
        paymentController.paymentFailed(TEST_BOOKING_ID, TEST_USER_ID);
    }

    @Test(expected = BadRequestException.class)
    public void paymentFailedShouldThrowBadRequestException()
            throws NotFoundException, BadRequestException {
        doThrow(new BadRequestException("Payment failed wrong user.")).when(paymentsService)
                .processPaymentFailed(getBooking(), TEST_USER_ID);
        doReturn(getBooking()).when(bookingService).getBooking(TEST_BOOKING_ID);
        paymentController.paymentFailed(TEST_BOOKING_ID,TEST_USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void paymentSuccessShouldReturnNotFoundException()
            throws NotFoundException, BadRequestException, InvalidStateException {
        doThrow(new NotFoundException("Booking id not found")).when(bookingService)
                .getBooking(TEST_BOOKING_ID);
        paymentController.paymentSuccess(TEST_BOOKING_ID, TEST_USER_ID);
    }

    @Test(expected = BadRequestException.class)
    public void paymentSuccessShouldThrowBadRequestException()
            throws NotFoundException, BadRequestException, InvalidStateException {
        doThrow(new BadRequestException("Payment failed wrong user.")).when(paymentsService)
                .processPaymentFailed(getBooking(), TEST_USER_ID);
        doReturn(getBooking()).when(bookingService).getBooking(TEST_BOOKING_ID);
        paymentController.paymentSuccess(TEST_BOOKING_ID,TEST_USER_ID);
    }

}
