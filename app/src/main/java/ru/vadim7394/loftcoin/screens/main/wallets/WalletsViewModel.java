package ru.vadim7394.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.Transaction;
import ru.vadim7394.loftcoin.data.db.modal.Wallet;

public abstract class WalletsViewModel extends AndroidViewModel {
    public WalletsViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void getWallets();

    public abstract void onWalletChanged(int position);

    public abstract void onNewWalletClick();

    public abstract void onCurrencySelected(CoinEntity coin);

    public abstract LiveData<List<Transaction>> transactions();

    public abstract LiveData<List<Wallet>> wallets();

    public abstract LiveData<Boolean> walletsVisible();

    public abstract LiveData<Boolean> newWalletVisible();

    public abstract LiveData<Object> selectCurrency();

    public abstract LiveData<Object> scrollToNewWallet();
}
