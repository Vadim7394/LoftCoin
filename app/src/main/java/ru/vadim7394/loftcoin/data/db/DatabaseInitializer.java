package ru.vadim7394.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import ru.vadim7394.loftcoin.data.db.room.AppDatabase;
import ru.vadim7394.loftcoin.data.db.room.DatabaseImplRoom;

public class DatabaseInitializer {

    public DataBase init (Context context) {
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "loftCoin.db")
                .build();

        return new DatabaseImplRoom(appDatabase);
    }
}
