package org.alessandrosalerno.jlome.engine;

import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;
import org.alessandrosalerno.jlome.order.OrderTrades;
import org.alessandrosalerno.jlome.order.Side;
import org.alessandrosalerno.jlome.order.processing.OrderProcessor;
import org.alessandrosalerno.jlome.order.processing.OrderProcessorBackend;
import org.alessandrosalerno.jlome.order.processing.backend.DefaultOrderProcessorBackend;
import org.alessandrosalerno.jlome.order.processing.frontend.BuyOrderProcessor;
import org.alessandrosalerno.jlome.order.processing.frontend.SellOrderProcessor;
import org.alessandrosalerno.jlome.tools.DefaultableHashMap;

public class LightMatchingEngine {
    private DefaultableHashMap<String, OrderBook> orderBooks;
    private OrderProcessorBackend processorBackend;
    private EngineState state;

    public LightMatchingEngine() {
        this.orderBooks = new DefaultableHashMap<>();
        this.state = new EngineState();
        this.processorBackend = new DefaultOrderProcessorBackend();
    }

    public LightMatchingEngine(OrderProcessorBackend processorBackend) {
        this.orderBooks = new DefaultableHashMap<>();
        this.state = new EngineState();
        this.processorBackend = processorBackend;
    }

    public synchronized OrderTrades addOrder(String sym, double price, double quantity, Side side) {
        assert side == Side.Buy || side == Side.Sell;

        OrderBook orderBook = this.orderBooks.setDefault(sym, new OrderBook());

        int orderId = this.state.nextOrder();;
        Order order = new Order(orderId, sym, price, quantity, side);

        OrderProcessor processor = switch (side) {
            case Buy -> new BuyOrderProcessor(this.processorBackend);
            case Sell -> new SellOrderProcessor(this.processorBackend);
            default -> throw new IllegalStateException("Unexpected value: " + side);
        };

        return processor.process(order, orderBook, this.state);
    }

    public OrderProcessorBackend getProcessorBackend() {
        return this.processorBackend;
    }

    public void setProcessorBackend(OrderProcessorBackend processorBackend) {
        this.processorBackend = processorBackend;
    }
}
