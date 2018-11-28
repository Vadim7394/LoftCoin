package ru.vadim7394.loftcoin.data.db.modal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.vadim7394.loftcoin.data.model.Fiat;

public class CoinEntity extends RealmObject {

    @PrimaryKey
    public int id;

    public String name;
    public String symbol;
    public String slug;
    public int rank;
    public long updated;

    public QuoteEntity usd;

    public QuoteEntity rub;

    public QuoteEntity eur;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getSlug() {
        return slug;
    }

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
