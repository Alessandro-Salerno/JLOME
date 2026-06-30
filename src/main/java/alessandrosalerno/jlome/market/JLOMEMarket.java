package alessandrosalerno.jlome.market;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import alessandrosalerno.jlome.book.JLOMEOrderBook;
import alessandrosalerno.jlome.order.JLOMEBookableTradeOrder;
import alessandrosalerno.jlome.order.JLOMEOrder;
import alessandrosalerno.jlome.order.JLOMEOrderState;
import alessandrosalerno.jlome.order.JLOMEResponse;

public class JLOMEMarket {
    // NOTE: This specifically uses a single-threaded executor service as it is
    // guaranteed to run tasks in FIFO order, thus establishing a total order over
    // submitted tasks for a given market.
    private final ExecutorService executor;
    private final JLOMEOrderBook book;
    private JLOMEMarketState state;

    public JLOMEMarket() {
        this.state = JLOMEMarketState.CLOSED;
        this.book = new JLOMEOrderBook();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public Future<?> open() {
        return this.executor.submit(() -> {
            this.state = JLOMEMarketState.OPEN;
        });
    }

    public Future<Iterator<JLOMEResponse>> open(Iterator<JLOMEOrder> savedOrders) {
        return this.executor.submit(() -> {
            // NOTE: this may not be the best for performance, but it's good enough because
            // opening and closing markets is infrequent
            List<JLOMEResponse> r = new ArrayList<>();
            savedOrders.forEachRemaining((order) -> {
                r.add(this.submitLogic(order));
            });
            this.state = JLOMEMarketState.OPEN;
            return r.iterator();
        });
    }

    public Future<?> close() {
        return this.executor.submit(() -> {
            this.state = JLOMEMarketState.CLOSED;
        });
    }

    public Future<JLOMEResponse> submit(JLOMEOrder order) {
        return this.executor.submit(() -> {
            if (this.state != JLOMEMarketState.OPEN) {
                return new JLOMEResponse(JLOMEOrderState.REJECTED, 0, 0);
            }

            return this.submitLogic(order);
        });
    }

    private JLOMEResponse submitLogic(JLOMEOrder order) {
        if (order instanceof JLOMEBookableTradeOrder bookableOrder
                && bookableOrder.getAvailable() <= 0) {
            return new JLOMEResponse(JLOMEOrderState.REJECTED, 0, 0);
        }

        return this.book.submit(order);
    }
}
