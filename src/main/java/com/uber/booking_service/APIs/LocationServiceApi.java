package com.uber.booking_service.APIs;

import com.uber.booking_service.DTO.DriverLocationDto;
import com.uber.booking_service.DTO.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("api/location/nearby/drivers")
    Call<DriverLocationDto[]> getNearByDrivers(@Body NearbyDriversRequestDto nearbyDriversRequestDto);
}
