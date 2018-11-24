package ru.vadim7394.loftcoin.data.db;

import java.util.List;

import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface DataBase {

    List<CoinEntity> getCoins();

    void saveCoins(List<CoinEntity> coins);
}
