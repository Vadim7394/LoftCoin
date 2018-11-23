package ru.vadim7394.loftcoin.screens.main.rate;

import java.util.List;

import ru.vadim7394.loftcoin.data.api.model.Coin;

public interface RateView {
    void setCoins(List<Coin> data);

    void setRefreshing(boolean refreshing);

    void showCurrencyDialog();
}
