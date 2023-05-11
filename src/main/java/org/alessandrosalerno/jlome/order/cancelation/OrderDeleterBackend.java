package org.alessandrosalerno.jlome.order.cancelation;

import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.Order;

public interface OrderDeleterBackend {
    public Order delete(Order order, MarketInformation marketInformation);
}
