package alessandrosalerno.jlome.book;

import alessandrosalerno.jlome.order.JLOMEBookableTradeOrder;
import alessandrosalerno.jlome.order.JLOMEOrder;
import alessandrosalerno.jlome.order.JLOMEStatefulTradeOrder;

public final class JLOMEMatch {
    private static class OrderData {
        private JLOMEOrder order;

        public OrderData(JLOMEOrder order) {
            this.order = order;
        }

        @SuppressWarnings("unused")
        public long getOrderId() {
            return this.order.getOrderId();
        }

        @SuppressWarnings("unused")
        public long getIssuerId() {
            return this.order.getIssuerId();
        }
    }

    private OrderData incomingOrder;
    private OrderData bookOrder;
    private long unitPrice;
    private long unitCount;

    public JLOMEMatch(JLOMEStatefulTradeOrder incomingOrder, JLOMEBookableTradeOrder bookOrder, long unitCount,
            long unitPrice) {
        this.incomingOrder = new OrderData(incomingOrder);
        this.bookOrder = new OrderData(bookOrder);
    }

    public OrderData getIncomingOrder() {
        return this.incomingOrder;
    }

    public OrderData getBookOrder() {
        return this.bookOrder;
    }

    public long getUnitPrice() {
        return this.unitPrice;
    }

    public long getUnitCount() {
        return this.unitCount;
    }
}
