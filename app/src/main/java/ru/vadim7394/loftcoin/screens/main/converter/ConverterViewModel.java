package ru.vadim7394.loftcoin.screens.main.converter;

import android.os.Bundle;

import io.reactivex.Observable;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface ConverterViewModel {
    Observable<String> sourceCurrency();
    Observable<String> destinationCurrency();
    Observable<String> destinationAmount();
    Observable<Object> selectSourceCurrency();
    Observable<Object> selectDestinationCurrency();
    void onSourceAmountChange(String amount);
    void onSourceCurrencyClick();
    void onDestinationCurrencyClick();
    void onSourceCurrencySelected(CoinEntity coin);
    void onDestinationCurrencySelected(CoinEntity coin);
    void saveState(Bundle outState);
}
