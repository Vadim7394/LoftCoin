package ru.vadim7394.loftcoin.data.db.modal;

import android.arch.persistence.room.Embedded;

public class WalletModel {

    @Embedded()
    public Wallet wallet;

    @Embedded()
    public CoinEntity coin;
}
