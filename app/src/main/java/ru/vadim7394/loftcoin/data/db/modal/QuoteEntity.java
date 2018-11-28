package ru.vadim7394.loftcoin.data.db.modal;

import io.realm.RealmObject;

public class QuoteEntity extends RealmObject {
    public double price;
    public float percentChange1h;
    public float percentChange24h;
    public float percentChange7d;
}
