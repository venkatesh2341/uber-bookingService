package com.uber.booking_service.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriversRequestDto {
    private Double latitude;
    private Double longitude;
    private Integer radius;
}