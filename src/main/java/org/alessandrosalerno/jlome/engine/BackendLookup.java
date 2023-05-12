package org.alessandrosalerno.jlome.engine;

import org.alessandrosalerno.jlome.order.cancelation.OrderDeleterBackend;
import org.alessandrosalerno.jlome.order.processing.OrderProcessorBackend;

public final class BackendLookup {
    private OrderProcessorBackend orderProcessorBackend;
    private OrderDeleterBackend orderDeleterBackend;

    public BackendLookup(OrderProcessorBackend orderProcessorBackend, OrderDeleterBackend orderDeleterBackend) {
        this.orderProcessorBackend = orderProcessorBackend;
        this.orderDeleterBackend = orderDeleterBackend;
    }

    public OrderProcessorBackend getOrderProcessorBackend() {
        return this.orderProcessorBackend;
    }

    public void setOrderProcessorBackend(OrderProcessorBackend orderProcessorBackend) {
        this.orderProcessorBackend = orderProcessorBackend;
    }

    public OrderDeleterBackend getOrderDeleterBackend() {
        return this.orderDeleterBackend;
    }

    public void setOrderDeleterBackend(OrderDeleterBackend orderDeleterBackend) {
        this.orderDeleterBackend = orderDeleterBackend;
    }
}
