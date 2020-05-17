package ru.badrudin.api.model;

public class InstrumentBuilder {

    private String ticker;
    private String board;
    private double price;
    private double low;
    private double high;

    public InstrumentBuilder setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public InstrumentBuilder setBoard(String board) {
        this.board = board;
        return this;
    }

    public InstrumentBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public InstrumentBuilder setLow(double low) {
        this.low = low;
        return this;
    }

    public InstrumentBuilder setHigh(double high) {
        this.high = high;
        return this;
    }

    public Instrument build() {
        return new Instrument(ticker, board, price, low, high);
    }
}
