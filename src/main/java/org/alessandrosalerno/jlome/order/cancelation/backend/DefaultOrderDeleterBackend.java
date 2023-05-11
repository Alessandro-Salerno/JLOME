package org.alessandrosalerno.jlome.order.cancelation.backend;

import org.alessandrosalerno.jlome.market.Market;
import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.cancelation.OrderDeleterBackend;

public class DefaultOrderDeleterBackend implements OrderDeleterBackend {
    @Override
    public Order delete(Order order, MarketInformation marketInformation) {
        assert marketInformation.side().containsKey(order.getPrice());
        Market priceLevel = marketInformation.side().get(order.getPrice());

        int index;
        for (index = 0; index < priceLevel.size(); ) {
            if (priceLevel.get(index).getOrderId() == order.getOrderId()) {
                priceLevel.remove(index);
                break;
            }

            index++;
        }

        if (index == priceLevel.size())
            return null;

        if (marketInformation.side().get(order.getPrice()).isEmpty())
            marketInformation.side().remove(order.getPrice());

        order.setLeft(0);
        return order;
    }
}
