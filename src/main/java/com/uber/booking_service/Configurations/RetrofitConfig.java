package com.uber.booking_service.Configurations;

import com.uber.booking_service.APIs.NearByDriverApi;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {
//    private static final String BASE_URL = "http://localhost:7002/";

    public Retrofit retrofit() {
        return new Retrofit.Builder()
//               .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
