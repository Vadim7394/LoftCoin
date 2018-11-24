package ru.vadim7394.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void SaveCoin(List<CoinEntity> coins);

    @Query("SELECT * FROM Coin")
    List<CoinEntity> getCoins();
}