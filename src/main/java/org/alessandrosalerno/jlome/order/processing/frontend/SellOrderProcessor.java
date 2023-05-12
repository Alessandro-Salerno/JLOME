package org.alessandrosalerno.jlome.order.processing.frontend;

import org.alessandrosalerno.jlome.engine.EngineState;
import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;
import org.alessandrosalerno.jlome.order.processing.OrderPlacementConfirmation;
import org.alessandrosalerno.jlome.order.processing.OrderProcessor;
import org.alessandrosalerno.jlome.order.processing.OrderProcessorBackend;

public final class SellOrderProcessor implements OrderProcessor {
    private final OrderProcessorBackend backend;

    public SellOrderProcessor(OrderProcessorBackend backend) {
        this.backend = backend;
    }

    @Override
    public OrderPlacementConfirmation process(Order order, OrderBook orderBook, EngineState state) {
        return this.backend.process(order, new MarketInformation(orderBook.getAsks(), orderBook.getBids()), state, orderBook);
    }
}
