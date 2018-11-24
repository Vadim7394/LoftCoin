package ru.vadim7394.loftcoin.data.db.room;

import java.util.List;

import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public class DatabaseImplRoom implements DataBase {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }

    @Override
    public List<CoinEntity> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().SaveCoin(coins);
    }
}
