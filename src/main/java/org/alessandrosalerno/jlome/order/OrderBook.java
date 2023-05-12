package org.alessandrosalerno.jlome.order;

import org.alessandrosalerno.jlome.market.MarketSide;

import java.util.HashMap;
import java.util.Map;

public class OrderBook {
    private MarketSide asks;
    private MarketSide bids;
    private final Map<Integer, Order> orderIdMap;

    public OrderBook() {
        this.asks = new MarketSide();
        this.bids = new MarketSide();
        this.orderIdMap = new HashMap<>();
    }

    public MarketSide getAsks() {
        return this.asks;
    }

    public MarketSide getBids() {
        return this.bids;
    }

    public Map<Integer, Order> getOrderIdMap() {
        return this.orderIdMap;
    }
}
