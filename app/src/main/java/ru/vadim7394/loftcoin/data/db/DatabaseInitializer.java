package ru.vadim7394.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DatabaseInitializer {

    public void init(Context context) {
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("loftcoin.realm")
                .build();

        Realm.setDefaultConfiguration(configuration);

    }
}
