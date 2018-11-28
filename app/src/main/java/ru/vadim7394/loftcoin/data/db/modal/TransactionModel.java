package ru.vadim7394.loftcoin.data.db.modal;

import android.arch.persistence.room.Embedded;

public class TransactionModel {
    @Embedded()
    public Transaction transaction;
    @Embedded()
    public CoinEntity coin;
}
