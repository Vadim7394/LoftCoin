package ru.vadim7394.loftcoin.screens.main.rate;

import ru.vadim7394.loftcoin.data.model.Fiat;

public interface RatePresenter {
    void attachView(RateView view);

    void detachView();

    void getRate();

    void loadRate(Boolean fromRefresh);

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);

}
