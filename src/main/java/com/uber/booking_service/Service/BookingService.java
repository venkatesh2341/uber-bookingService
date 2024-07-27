package com.uber.booking_service.Service;

import com.uber.booking_service.DTO.CreateBookingRequestDto;
import com.uber.booking_service.DTO.CreateBookingResponseDto;
import com.uber.booking_service.DTO.UpdateBookingRequestDto;
import com.uber.booking_service.DTO.UpdateBookingResponseDto;
import com.uber_project.entity_provider.Models.Booking;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto);
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto updateBookingRequestDto, Long bookingId);
}
