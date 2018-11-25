package ru.vadim7394.loftcoin.screens.currencies;

import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface CurrenciesBottomSheetListener {
    void onCurrencySelected(CoinEntity coin);
}
