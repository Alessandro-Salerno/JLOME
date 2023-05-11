package org.alessandrosalerno.jlome.order.processing;

import org.alessandrosalerno.jlome.engine.EngineState;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;
import org.alessandrosalerno.jlome.order.OrderTrades;

public interface OrderProcessor {
    public OrderTrades process(Order order, OrderBook orderBook, EngineState state);
}
