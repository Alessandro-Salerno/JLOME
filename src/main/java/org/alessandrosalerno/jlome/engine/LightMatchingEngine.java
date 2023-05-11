package org.alessandrosalerno.jlome.engine;

import org.alessandrosalerno.jlome.order.Order;
import org.alessandrosalerno.jlome.order.OrderBook;
import org.alessandrosalerno.jlome.order.OrderTrades;
import org.alessandrosalerno.jlome.order.Side;
import org.alessandrosalerno.jlome.order.cancelation.OrderDeleter;
import org.alessandrosalerno.jlome.order.cancelation.backend.DefaultOrderDeleterBackend;
import org.alessandrosalerno.jlome.order.cancelation.frontend.BuyOrderDeleter;
import org.alessandrosalerno.jlome.order.cancelation.frontend.SellOrderDeleter;
import org.alessandrosalerno.jlome.order.processing.OrderProcessor;
import org.alessandrosalerno.jlome.order.processing.backend.DefaultOrderProcessorBackend;
import org.alessandrosalerno.jlome.order.processing.frontend.BuyOrderProcessor;
import org.alessandrosalerno.jlome.order.processing.frontend.SellOrderProcessor;
import org.alessandrosalerno.jlome.tools.DefaultableHashMap;

public class LightMatchingEngine {
    private DefaultableHashMap<String, OrderBook> orderBooks;
    private BackendLookup backendLookup;
    private EngineState state;

    public LightMatchingEngine() {
        this.orderBooks = new DefaultableHashMap<>();
        this.state = new EngineState();
        this.backendLookup = new BackendLookup(new DefaultOrderProcessorBackend(), new DefaultOrderDeleterBackend());
    }

    public LightMatchingEngine(BackendLookup backendLookup) {
        this.orderBooks = new DefaultableHashMap<>();
        this.state = new EngineState();
        this.backendLookup = backendLookup;
    }

    public synchronized OrderTrades addOrder(String sym, double price, double quantity, Side side) {
        assert side == Side.Buy || side == Side.Sell;

        OrderBook orderBook = this.orderBooks.setDefault(sym, new OrderBook());

        int orderId = this.state.nextOrder();;
        Order order = new Order(orderId, sym, price, quantity, side);

        OrderProcessor processor = switch (side) {
            case Buy -> new BuyOrderProcessor(this.backendLookup.getOrderProcessorBackend());
            case Sell -> new SellOrderProcessor(this.backendLookup.getOrderProcessorBackend());
            default -> throw new IllegalStateException("Unexpected value: " + side);
        };

        return processor.process(order, orderBook, this.state);
    }

    public Order cancelOrder(int orderId, String sym) {
        assert this.orderBooks.containsKey(sym);
        OrderBook orderBook = this.orderBooks.get(sym);

        if (orderBook.getOrderidMap().containsKey(orderId))
            return null;

        Order order = orderBook.getOrderidMap().get(orderId);

        OrderDeleter deleter = switch (order.getSide()) {
            case Buy -> new BuyOrderDeleter(this.backendLookup.getOrderDeleterBackend());
            case Sell -> new SellOrderDeleter(this.backendLookup.getOrderDeleterBackend());
            case Unset -> throw new IllegalStateException("Unexpected value: " + order.getSide());
        };

        Order finalorder = deleter.delete(order, orderBook);
        orderBook.getOrderidMap().remove(orderId);
        return finalorder;
    }

    public BackendLookup getBackendLookup() {
        return this.backendLookup;
    }

    public void setBackendLookup(BackendLookup backendLookup) {
        this.backendLookup = backendLookup;
    }
}
