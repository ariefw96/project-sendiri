package com.sendiri.microservices.demoproject.service;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class MsCallService {

    public Boolean verify(String jwt) throws IOException {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        String url = "api/v1/auth/verify";
        Call<Boolean> verifyToken = service.verifyToken(url,jwt);
        Response<Boolean> response = verifyToken.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            return false;
        }
    }

}
