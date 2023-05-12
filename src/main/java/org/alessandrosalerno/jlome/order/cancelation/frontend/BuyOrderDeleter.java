package org.alessandrosalerno.jlome.order.cancelation.frontend;

import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;
import org.alessandrosalerno.jlome.order.cancelation.OrderDeleter;
import org.alessandrosalerno.jlome.order.cancelation.OrderDeleterBackend;

public final class BuyOrderDeleter implements OrderDeleter {
    private OrderDeleterBackend backend;

    public BuyOrderDeleter(OrderDeleterBackend backend) {
        this.backend = backend;
    }

    @Override
    public Order delete(Order order, OrderBook orderBook) {
        return this.backend.delete(order, new MarketInformation(orderBook.getBids(), orderBook.getAsks()));
    }
}
