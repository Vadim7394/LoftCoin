package ru.vadim7394.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.Transaction;
import ru.vadim7394.loftcoin.data.db.modal.Wallet;
import ru.vadim7394.loftcoin.utils.SingleLiveEvent;

public class WalletsViewModelImpl extends WalletsViewModel {

    private static final String TAG = "WalletsViewModelImpl";

    private MutableLiveData<List<Wallet>> walletsItems = new MutableLiveData<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> transactionsItems = new MutableLiveData<>();
    private SingleLiveEvent<Object> scrollToNewWallet = new SingleLiveEvent<>();

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();

    private DataBase database;

    private CompositeDisposable disposables = new CompositeDisposable();

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) application).getDatabase();
        database.open();
    }

    @Override
    protected void onCleared() {
        database.close();
        super.onCleared();
    }

    @Override
    public void getWallets() {
        getWalletsInner();
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.setValue(new Object());
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        Wallet wallet = randomWallet(coin);
        List<Transaction> transactions = randomTransactions(wallet);
        Disposable disposable = Observable.fromCallable(() -> {
            database.saveWallet(wallet);
            database.saveTransaction(transactions);
            return new Object();
        })
                .subscribe(o -> scrollToNewWallet.call());
        disposables.add(disposable);
    }

    private Wallet randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), 10 * random.nextDouble(), coin);
    }

    @Override
    public void onWalletChanged(int position) {
        Wallet wallet = walletsItems.getValue().get(position);
        getTransaction(wallet.walletId);
    }

    @Override
    public LiveData<List<Transaction>> transactions() {
        return transactionsItems;
    }

    @Override
    public LiveData<List<Wallet>> wallets() {
        return walletsItems;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<Object> scrollToNewWallet() {
        return scrollToNewWallet;
    }

    private void getWalletsInner() {
        Disposable disposable = database.getWallets()
                .subscribe(wallets -> {
                    if (wallets.isEmpty()) {
                        walletsVisible.setValue(false);
                        newWalletVisible.setValue(true);
                    } else {
                        walletsVisible.setValue(true);
                        newWalletVisible.setValue(false);

                        if (walletsItems.getValue() == null || walletsItems.getValue().isEmpty()) {
                            Wallet wallet = wallets.get(0);
                            String walletId = wallet.walletId;
                            getTransaction(walletId);
                        }

                        walletsItems.setValue(wallets);
                    }
                });
        disposables.add(disposable);
    }

    private void getTransaction(String walletId) {
        Disposable disposable = database.getTransactions(walletId)
                .subscribe(
                        transactions -> transactionsItems.setValue(transactions)
                );
        disposables.add(disposable);
    }

    private List<Transaction> randomTransactions(Wallet wallet) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transactions.add(randomTransaction(wallet));
        }
        return transactions;
    }

    private Transaction randomTransaction(Wallet wallet) {
        Random random = new Random();
        long startDate = 1483228800000L;
        long nowDate = System.currentTimeMillis();
        long date = startDate + (long) (random.nextDouble() * (nowDate - startDate));
        double amount = 2 * random.nextDouble();
        boolean amountSign = random.nextBoolean();
        return new Transaction(wallet.walletId, amountSign ? amount : -amount, date, wallet.coin);
    }
}
