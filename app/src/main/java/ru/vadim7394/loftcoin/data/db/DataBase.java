package ru.vadim7394.loftcoin.data.db;

import java.util.List;

import io.reactivex.Flowable;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface DataBase {

    Flowable<List<CoinEntity>> getCoins();

    void saveCoins(List<CoinEntity> coins);

    CoinEntity getCoin(String symbol);
}
