package ru.vadim7394.loftcoin.screens.main.rate;

import java.util.List;

import ru.vadim7394.loftcoin.data.api.model.Coin;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.model.Fiat;

public interface RateView {

    void setCoins(List<CoinEntity> coins);

    void setRefreshing(boolean refreshing);

    void showCurrencyDialog();

    void onCurrencySelected(Fiat currency);

    void showProgress();

    void hideProgress();
}
