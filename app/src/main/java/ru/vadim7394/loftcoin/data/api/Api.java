package ru.vadim7394.loftcoin.data.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import ru.vadim7394.loftcoin.data.model.RateResponse;

public interface Api {

    @GET("ticker")
    Call<RateResponse> ticker(@Query("structure") String structure, @Query("convert") String convert);
}
