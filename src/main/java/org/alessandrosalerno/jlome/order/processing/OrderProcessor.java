package org.alessandrosalerno.jlome.order.processing;

import org.alessandrosalerno.jlome.engine.EngineState;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;

public interface OrderProcessor {
    public OrderPlacementConfirmation process(Order order, OrderBook orderBook, EngineState state);
}
