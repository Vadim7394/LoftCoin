package ru.vadim7394.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vadim7394.loftcoin.data.api.model.Coin;
import ru.vadim7394.loftcoin.data.api.Api;
import ru.vadim7394.loftcoin.data.api.model.RateResponse;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntityMapper;
import ru.vadim7394.loftcoin.data.model.Fiat;
import ru.vadim7394.loftcoin.data.perfs.Prefs;

public class RatePresenterImpl implements RatePresenter {

    private Api api;
    private Prefs prefs;
    private DataBase database;
    private CoinEntityMapper mapper;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Nullable
    private RateView view;


    public RatePresenterImpl(Api api, Prefs prefs, DataBase database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {

        disposables.dispose();
        this.view = null;
    }

    @Override
    public void getRate() {
        Disposable disposable = database.getCoins()
                .observeOn(Schedulers.io())
                .subscribe(
                        coinEntities -> {
                            if (view != null) {
                                view.setCoins(coinEntities);
                            }
                        },
                        throwable -> {

                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadRate(Boolean fromRefresh) {
        if (!fromRefresh) {
            if (view != null) {
                view.showProgress();
            }
        }

        Disposable disposable = api.ticker("array", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> mapper.mapCoins(rateResponse.data))
                .map(coinEntities -> {
                    database.saveCoins(coinEntities);
                    return new Object();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object -> {
                            if (view != null) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }
                        },
                        throwable -> {
                            if (fromRefresh) {
                                view.setRefreshing(false);
                            } else {
                                view.hideProgress();
                            }
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void onRefresh() {
        loadRate(true);
    }

    @Override
    public void onMenuItemCurrencyClick() {
        view.showCurrencyDialog();
    }

    @Override
    public void onFiatCurrencySelected(Fiat currency) {
        prefs.setFiatCurrency(currency);
        loadRate(false);
    }
}
