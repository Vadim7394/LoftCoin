package ru.vadim7394.loftcoin.data.db.modal;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Wallet {

    @PrimaryKey
    @NonNull
    public String walletId;

    public int currencyId;

    public double amount;

    public Wallet(@NonNull String walletId, int currencyId, double amount) {
        this.walletId = walletId;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}
