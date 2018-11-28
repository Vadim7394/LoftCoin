package ru.vadim7394.loftcoin.data.db;

import java.util.List;

import io.reactivex.Flowable;
import ru.vadim7394.loftcoin.data.db.modal.Transaction;
import ru.vadim7394.loftcoin.data.db.modal.Wallet;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface DataBase {

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<Wallet>> getWallets();

    CoinEntity getCoin(String symbol);

    Flowable<List<Transaction>> getTransactions(String walletId);

    void saveWallet(Wallet wallet);

    void open();

    void close();

    void saveCoins(List<CoinEntity> coins);

    void saveTransaction(List<Transaction> transactions);


}
