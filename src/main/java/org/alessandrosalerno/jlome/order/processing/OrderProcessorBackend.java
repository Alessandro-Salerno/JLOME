package org.alessandrosalerno.jlome.order.processing;

import org.alessandrosalerno.jlome.engine.EngineState;
import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;

public interface OrderProcessorBackend {
    public OrderPlacementConfirmation process(Order order, MarketInformation marketInformation, EngineState state, OrderBook orderBook);
}
