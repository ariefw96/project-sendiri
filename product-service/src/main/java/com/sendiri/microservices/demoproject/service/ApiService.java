package com.sendiri.microservices.demoproject.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET
    Call<Boolean> verifyToken(@Url String url, @Query("jwt") String jwt);

}
