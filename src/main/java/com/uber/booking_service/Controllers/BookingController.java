package com.uber.booking_service.Controllers;

import com.uber.booking_service.DTO.CreateBookingRequestDto;
import com.uber.booking_service.DTO.CreateBookingResponseDto;
import com.uber.booking_service.Service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    BookingController(BookingService bookingService){
        this.bookingService= bookingService;
    }

    @PostMapping("/createbooking")
    public ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody CreateBookingRequestDto createBookingRequestDto){

        return new ResponseEntity<>(bookingService.createBooking(createBookingRequestDto), HttpStatus.CREATED);
    }
}
