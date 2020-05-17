package ru.badrudin.api.model;

public class Instrument {

    public String ticker;
    public String board;
    public double price;
    public double low;
    public double high;

    Instrument(String ticker,
               String board,
               double price,
               double low,
               double high) {
        this.ticker = ticker;
        this.board = board;
        this.price = price;
        this.low = low;
        this.high = high;
    }
}
