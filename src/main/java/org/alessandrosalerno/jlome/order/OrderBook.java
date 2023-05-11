package org.alessandrosalerno.jlome.order;

import org.alessandrosalerno.jlome.market.MarketSide;

import java.util.HashMap;
import java.util.Map;

public class OrderBook {
    private MarketSide asks;
    private MarketSide bids;
    private Map<Integer, Order> orderidMap;

    public OrderBook() {
        this.asks = new MarketSide();
        this.bids = new MarketSide();
        this.orderidMap = new HashMap<>();
    }

    public MarketSide getAsks() {
        return this.asks;
    }

    public MarketSide getBids() {
        return this.bids;
    }

    public Map<Integer, Order> getOrderidMap() {
        return this.orderidMap;
    }
}
