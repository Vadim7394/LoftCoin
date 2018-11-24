package ru.vadim7394.loftcoin.data.db.modal;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ru.vadim7394.loftcoin.data.model.Fiat;

@Entity(tableName = "Coin")
public class CoinEntity {

    @PrimaryKey
    public int id;

    public String name;
    public String symbol;
    public String slug;
    public int rank;
    public long updated;

    @Embedded(prefix = "usd_")
    public QuoteEntity usd;

    @Embedded(prefix = "rub_")
    public QuoteEntity rub;

    @Embedded(prefix = "eur_")
    public QuoteEntity eur;

    public QuoteEntity getQuote(Fiat fiat) {
        QuoteEntity quote = null;

        switch (fiat) {
            case USD:
                quote = usd;
                break;
            case EUR:
                quote = eur;
                break;
            case RUB:
                quote = rub;
                break;
        }

        if(quote == null) {
            return usd;
        } else {
            return quote;
        }
    }

}
