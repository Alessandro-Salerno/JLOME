package org.alessandrosalerno.jlome.order;

public record OrderTrades(Order order,
                          Trade[] trades) {
}
