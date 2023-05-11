package org.alessandrosalerno.jlome.order.cancelation;

import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;

public interface OrderDeleter {
    public Order delete(Order order, OrderBook orderBook);
}
