package com.uber.booking_service.Service.Impl;

import com.uber.booking_service.APIs.LocationServiceApi;
import com.uber.booking_service.DTO.*;
import com.uber.booking_service.Repositories.BookingRepository;
import com.uber.booking_service.Repositories.DriverRepository;
import com.uber.booking_service.Repositories.PassengerRepository;
import com.uber.booking_service.Service.BookingService;
import com.uber_project.entity_provider.Models.Booking;
import com.uber_project.entity_provider.Models.BookingStatus;
import com.uber_project.entity_provider.Models.Driver;
import com.uber_project.entity_provider.Models.Passenger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepostiory;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;
//    private static  final String driverLocationUrl= "http://localhost:7002/api/location";

    BookingServiceImpl(BookingRepository bookingRepository , PassengerRepository passengerRepository, LocationServiceApi locationServiceApi, DriverRepository driverRepository){
        this.bookingRepostiory = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.restTemplate = new RestTemplate();
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
    }

    @Override
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto) {

        Optional<Passenger> passenger = passengerRepository.findById(createBookingRequestDto.getPassengerId());

        NearbyDriversRequestDto nearbyDriversRequestDto= NearbyDriversRequestDto.builder()
                .latitude(createBookingRequestDto.getStartLocation().getLatitude())
                .longitude(createBookingRequestDto.getStartLocation().getLongitude())
                .radius(5)
                .build();
        getNearByDriversAsync(nearbyDriversRequestDto);
//        ResponseEntity<DriverLocationDto[]> driverLocationResponseList= restTemplate.postForEntity( driverLocationUrl+"/nearby/drivers",nearbyDriversRequestDto, DriverLocationDto[].class );
//
//        if(driverLocationResponseList.hasBody() && driverLocationResponseList.getBody().length!=0){
//            List<DriverLocationDto> driversList= List.of(driverLocationResponseList.getBody());
//            for(DriverLocationDto driverLocationDto: driversList){
//                System.out.println(driverLocationDto.getDriverId() + " " + driverLocationDto.getLatitude() + " "+ driverLocationDto.getLongitude());
//            }
//        }

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

    private void getNearByDriversAsync(NearbyDriversRequestDto nearbyDriversRequestDto){
        Call<DriverLocationDto[]> call= locationServiceApi.getNearByDrivers(nearbyDriversRequestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<DriverLocationDto> driversList= List.of(response.body());
                    for(DriverLocationDto driverLocationDto: driversList){
                        System.out.println(driverLocationDto.getDriverId() + " " + driverLocationDto.getLatitude() + " "+ driverLocationDto.getLongitude());
                    }
                }
                else{
                    System.out.println("Async call filed");
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                    throwable.printStackTrace();
            }
        });
    }


    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto updateBookingRequestDto, Long bookingId) {
        Optional<Driver> driver = driverRepository.findById(updateBookingRequestDto.getDriverId().get());
        bookingRepostiory.updateBookingStatusAndDriverById(BookingStatus.SCHEDULED, driver.get(), bookingId);
        Optional<Booking> booking = bookingRepostiory.findById(bookingId);
        return UpdateBookingResponseDto.builder()
                .bookingId(booking.get().getId())
                .bookingStatus(booking.get().getBookingStatus().toString())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }
}
