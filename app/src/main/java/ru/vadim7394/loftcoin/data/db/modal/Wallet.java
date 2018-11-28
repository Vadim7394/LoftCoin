package ru.vadim7394.loftcoin.data.db.modal;


import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Wallet extends RealmObject {

    @PrimaryKey
    public String walletId;

    public double amount;

    public CoinEntity coin;

    public Wallet() {

    }

    public Wallet(@NonNull String walletId, double amount, CoinEntity coin) {
        this.walletId = walletId;
        this.amount = amount;
        this.coin = coin;
    }
}
