package org.alessandrosalerno.jlome.order;

public class Trade {
    private int orderId;
    private String symbol;
    private double tradePrice;
    private double tradeQuantity;
    private Side tradeSide;
    private int tradeId;

    public Trade(int orderId, String symbol, double tradePrice, double tradeQuantity, Side tradeSide, int tradeId) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.tradePrice = tradePrice;
        this.tradeQuantity = tradeQuantity;
        this.tradeSide = tradeSide;
        this.tradeId = tradeId;
    }

    public Trade() {
        this.orderId = 0;
        this.symbol = "";
        this.tradePrice = 0;
        this.tradeQuantity = 0;
        this.tradeSide = Side.Unset;
        this.tradeId = 0;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getTradePrice() {
        return this.tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public double getTradeQuantity() {
        return this.tradeQuantity;
    }

    public void setTradeQuantity(double tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public Side getTradeSide() {
        return this.tradeSide;
    }

    public void setTradeSide(Side tradeSide) {
        this.tradeSide = tradeSide;
    }

    public int getTradeId() {
        return this.tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }
}
