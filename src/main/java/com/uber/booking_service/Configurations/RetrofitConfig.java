package com.uber.booking_service.Configurations;

import com.netflix.discovery.EurekaClient;
import com.uber.booking_service.APIs.LocationServiceApi;
import lombok.Builder;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {


    private  final EurekaClient eurekaClient;
    RetrofitConfig(EurekaClient eurekaClient){
        this.eurekaClient = eurekaClient;
    }

    private String getServiceUrl(String serviceName){
        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi locationServiceApi(){
        return new Retrofit.Builder()
                .baseUrl(getServiceUrl("LOCATIONSERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }
}

