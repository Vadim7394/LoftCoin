package ru.vadim7394.loftcoin.data.db.room;

import java.util.List;

import io.reactivex.Flowable;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public class DatabaseImplRoom implements DataBase {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().SaveCoin(coins);
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }
}
