package ru.vadim7394.loftcoin.data.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.Transaction;
import ru.vadim7394.loftcoin.data.db.modal.Wallet;

@Database(entities = {CoinEntity.class, Wallet.class, Transaction.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
    public abstract WalletDao walletDao();
}
