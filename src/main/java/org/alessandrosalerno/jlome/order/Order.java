package org.alessandrosalerno.jlome.order;

public class Order {
    private int orderId;
    private String symbol;
    private double price;
    private double quantity;
    private double cumulativeQuantity;
    private double left;
    private Side side;

    public Order(int orderId, String symbol, double price, double quantity, Side side) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.cumulativeQuantity = 0;
        this.left = quantity;
        this.side = side;
    }

    public Order() {
        this.orderId = 0;
        this.symbol = "";
        this.price = 0;
        this.quantity = 0;
        this.cumulativeQuantity = 0;
        this.left = 0;
        this.side = Side.Unset;
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

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCumulativeQuantity() {
        return this.cumulativeQuantity;
    }

    public void setCumulativeQuantity(double cumulativeQuantity) {
        this.cumulativeQuantity = cumulativeQuantity;
    }

    public double getLeft() {
        return this.left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
