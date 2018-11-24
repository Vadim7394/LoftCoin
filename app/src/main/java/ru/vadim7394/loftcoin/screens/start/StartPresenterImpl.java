package ru.vadim7394.loftcoin.screens.start;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vadim7394.loftcoin.data.api.Api;
import ru.vadim7394.loftcoin.data.api.model.Coin;
import ru.vadim7394.loftcoin.data.api.model.RateResponse;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntityMapper;
import ru.vadim7394.loftcoin.data.perfs.Prefs;

public class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;

    private Prefs prefs;

    private DataBase database;

    private CoinEntityMapper mapper;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Api api, Prefs prefs, DataBase database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRate() {
        api.ticker("array", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if(response.body() != null) {
                    List<Coin> coins = response.body().data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);

                    database.saveCoins(entities);
                }

                if (view != null) {
                    view.navigateToMainScreen();
                }
            }
            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                Log.e(TAG, "Load rate error", t);
            }
        });
    }
}
