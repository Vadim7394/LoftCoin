package ru.vadim7394.loftcoin.data.db;

import java.util.List;

import io.reactivex.Flowable;
import ru.vadim7394.loftcoin.data.db.modal.Transaction;
import ru.vadim7394.loftcoin.data.db.modal.TransactionModel;
import ru.vadim7394.loftcoin.data.db.modal.Wallet;
import ru.vadim7394.loftcoin.data.db.modal.WalletModel;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

public interface DataBase {

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<WalletModel>> getWallets();

    CoinEntity getCoin(String symbol);

    Flowable<List<TransactionModel>> getTransactions(String walletId);

    void saveWallet(Wallet wallet);

    void saveCoins(List<CoinEntity> coins);

    void saveTransaction(List<Transaction> transactions);


}
