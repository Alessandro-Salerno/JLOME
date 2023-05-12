package org.alessandrosalerno.jlome.order.processing;

import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.Trade;

public record OrderPlacementConfirmation(Order order,
                                         Trade[] trades) {
}
