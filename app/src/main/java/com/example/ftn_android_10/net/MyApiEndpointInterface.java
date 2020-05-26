package com.example.ftn_android_10.net;



import com.example.ftn_android_10.net.model1.SearchResult;
import com.example.ftn_android_10.net.model2.Detail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MyApiEndpointInterface {

    @GET("/")
    Call<SearchResult> getMovieByName(@QueryMap Map<String, String> options);

    @GET("/")
    Call<Detail> getMovieData(@QueryMap Map<String, String> options);

}
