package ru.vadim7394.loftcoin.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Coin {

    public int id;
    public String name;
    public String symbol;

    @SerializedName("website_slug")
    public String slug;

    public int rank;

    @SerializedName("last_updated")
    public long updated;

    public Map<String, Quote> quotes;
}
