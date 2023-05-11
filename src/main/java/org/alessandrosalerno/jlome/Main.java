package org.alessandrosalerno.jlome;

import org.alessandrosalerno.jlome.engine.LightMatchingEngine;
import org.alessandrosalerno.jlome.order.OrderTrades;
import org.alessandrosalerno.jlome.order.Side;

public class Main {
    public static void main(String[] args) {
        LightMatchingEngine engine = new LightMatchingEngine();

        OrderTrades buy = engine.addOrder("EUR/USD", 1.10, 1000, Side.Buy);
        System.out.println("Number of trades = " + buy.trades().length);
        System.out.println("Buy order quantity = " + buy.order().getQuantity());
        System.out.println("Buy order filled = " + buy.order().getCumulativeQuantity());
        System.out.println("Buy order leaves = " + buy.order().getLeft());

        OrderTrades sell = engine.addOrder("EUR/USD", 1.10, 1000, Side.Sell);
        System.out.println();
        System.out.println();
        System.out.println("Number of trades = " + sell.trades().length);
        System.out.println("Buy order quantity = " + buy.order().getQuantity());
        System.out.println("Buy order filled = " + buy.order().getCumulativeQuantity());
        System.out.println("Buy order leaves = " + buy.order().getLeft());
        System.out.println("Trade price = " + sell.trades()[0].getTradePrice());
        System.out.println("Trade quantity = " + sell.trades()[0].getTradeQuantity());
        System.out.println("Trade side = " + sell.trades()[0].getTradeSide());
    }
}