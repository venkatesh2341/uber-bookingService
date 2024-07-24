package com.uber.booking_service.Service.Impl;

import com.uber.booking_service.DTO.CreateBookingRequestDto;
import com.uber.booking_service.DTO.CreateBookingResponseDto;
import com.uber.booking_service.DTO.DriverLocationDto;
import com.uber.booking_service.DTO.NearbyDriversRequestDto;
import com.uber.booking_service.Repositories.BookingRepository;
import com.uber.booking_service.Repositories.PassengerRepository;
import com.uber.booking_service.Service.BookingService;
import com.uber_project.entity_provider.Models.Booking;
import com.uber_project.entity_provider.Models.BookingStatus;
import com.uber_project.entity_provider.Models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepostiory;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;

    private static  final String driverLocationUrl= "http://localhost:7002/api/location";
    BookingServiceImpl(BookingRepository bookingRepository , PassengerRepository passengerRepository){
        this.bookingRepostiory = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto) {

        Optional<Passenger> passenger = passengerRepository.findById(createBookingRequestDto.getPassengerId());

        NearbyDriversRequestDto nearbyDriversRequestDto= NearbyDriversRequestDto.builder()
                .latitude(createBookingRequestDto.getStartLocation().getLatitude())
                .longitude(createBookingRequestDto.getStartLocation().getLongitude())
                .radius(5)
                .build();
        ResponseEntity<DriverLocationDto[]> driverLocationResponseList= restTemplate.postForEntity( driverLocationUrl+"/nearby/drivers",nearbyDriversRequestDto, DriverLocationDto[].class );

        if(driverLocationResponseList.hasBody() && driverLocationResponseList.getBody().length!=0){
            List<DriverLocationDto> driversList= List.of(driverLocationResponseList.getBody());
            for(DriverLocationDto driverLocationDto: driversList){
                System.out.println(driverLocationDto.getDriverId() + " " + driverLocationDto.getLatitude() + " "+ driverLocationDto.getLongitude());
            }
        }

        Booking booking= Booking.builder()
                .startLocation(createBookingRequestDto.getStartLocation())
                .endLocation(createBookingRequestDto.getEndLocation())
                .passenger(passenger.get())
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .build();
        Booking newBooking = bookingRepostiory.save(booking);
        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }
}
