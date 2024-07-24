package com.uber.booking_service.DTO;

import com.uber_project.entity_provider.Models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingResponseDto {
    private Long bookingId;
    private Optional<Driver> driver;
    private String bookingStatus;
}
