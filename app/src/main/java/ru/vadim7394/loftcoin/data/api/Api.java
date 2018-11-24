package ru.vadim7394.loftcoin.data.api;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import ru.vadim7394.loftcoin.data.api.model.RateResponse;

public interface Api {

    @GET("ticker")
    Observable<RateResponse> ticker(@Query("structure") String structure, @Query("convert") String convert);

    void ticker();
}
