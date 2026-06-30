package alessandrosalerno.jlome.book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import alessandrosalerno.jlome.order.JLOMEBookableTradeOrder;
import alessandrosalerno.jlome.order.JLOMEManagementOrder;
import alessandrosalerno.jlome.order.JLOMEOrder;
import alessandrosalerno.jlome.order.JLOMEOrderSide;
import alessandrosalerno.jlome.order.JLOMEOrderState;
import alessandrosalerno.jlome.order.JLOMEResponse;
import alessandrosalerno.jlome.order.JLOMEStatefulTradeOrder;
import alessandrosalerno.jlome.order.JLOMETradeOrder;

public class JLOMEOrderBook {
    // TODO: replace with fastutil
    private final NavigableMap<Long, JLOMEPriceLevel> bids;
    private final NavigableMap<Long, JLOMEPriceLevel> offers;
    private final Map<Long, JLOMEOrderBookEntry> byOrderId;

    public JLOMEOrderBook() {
        this.bids = new TreeMap<>();
        this.offers = new TreeMap<>();
        this.byOrderId = new HashMap<>();
    }

    public JLOMEResponse submit(JLOMEOrder order) {
        if (order instanceof JLOMETradeOrder tradeOrder) {
            return this.submit(tradeOrder);
        }

        if (order instanceof JLOMEManagementOrder managementOrder) {
            return this.submit(managementOrder);
        }

        return new JLOMEResponse(JLOMEOrderState.REJECTED, 0, 0);
    }

    public Optional<JLOMEOrderBookEntry> getOrder(long orderId) {
        return Optional.ofNullable(this.byOrderId.get(orderId));
    }

    public Optional<Collection<JLOMEBookableTradeOrder>> getBidPriceLevel(long price) {
        JLOMEPriceLevel level = this.bids.get(price);
        if (level != null) {
            return Optional.ofNullable(Collections.unmodifiableCollection(level));
        }
        return Optional.empty();
    }

    public Optional<Collection<JLOMEBookableTradeOrder>> getOfferPriceLevel(long price) {
        JLOMEPriceLevel level = this.offers.get(price);
        if (level != null) {
            return Optional.ofNullable(Collections.unmodifiableCollection(level));
        }
        return Optional.empty();
    }

    private JLOMEResponse submit(JLOMETradeOrder order) {
        if (order instanceof JLOMEStatefulTradeOrder statefulOrder) {
            return this.submit(statefulOrder);
        }

        return new JLOMEResponse(JLOMEOrderState.REJECTED, 0, 0);
    }

    private JLOMEResponse submit(JLOMEManagementOrder order) {

    }

    private JLOMEResponse submit(JLOMEStatefulTradeOrder order) {
        return switch (order.getSide()) {
            case JLOMEOrderSide.BUY -> this.submitBuy(order);
            case JLOMEOrderSide.SELL -> this.submitSell(order);
            default -> new JLOMEResponse(JLOMEOrderState.REJECTED, 0, 0);
        };
    }

    private JLOMEResponse submitBuy(JLOMEStatefulTradeOrder order) {
        Iterator<Map.Entry<Long, JLOMEPriceLevel>> it = this.offers.entrySet()
                .iterator();
        JLOMEResponse res = this.match(it, order);

        if (order instanceof JLOMEBookableTradeOrder bookableOrder &&
                res.state() == JLOMEOrderState.CANCELLED) {
            this.addToBook(this.bids, bookableOrder);
            return new JLOMEResponse(JLOMEOrderState.ACCEPTED, res.unitsFilled(),
                    res.totalPrice(), res.matches());
        }

        return res;
    }

    private JLOMEResponse submitSell(JLOMEStatefulTradeOrder order) {
        Iterator<Map.Entry<Long, JLOMEPriceLevel>> it = this.bids.descendingMap()
                .entrySet().iterator();
        JLOMEResponse res = this.match(it, order);

        if (order instanceof JLOMEBookableTradeOrder bookableOrder &&
                res.state() == JLOMEOrderState.CANCELLED) {
            this.addToBook(this.offers, bookableOrder);
            return new JLOMEResponse(JLOMEOrderState.ACCEPTED, res.unitsFilled(),
                    res.totalPrice(), res.matches());
        }

        return res;
    }

    private void addToBook(NavigableMap<Long, JLOMEPriceLevel> side, JLOMEBookableTradeOrder order) {
        JLOMEPriceLevel level = side.getOrDefault(order.getPrice(), new JLOMEPriceLevel());
        level.add(order);
        this.byOrderId.put(order.getOrderId(), new JLOMEOrderBookEntry(order, level));
    }

    private JLOMEResponse match(Iterator<Map.Entry<Long, JLOMEPriceLevel>> book,
            JLOMEStatefulTradeOrder incomingOrder) {
        if (!incomingOrder.isMatchable()) {
            return new JLOMEResponse(JLOMEOrderState.CANCELLED, 0, 0);
        }

        long totalFilled = 0;
        long totalPrice = 0;
        Collection<JLOMEMatch> matches = new ArrayList<>();

        OUTER: while (book.hasNext()) {
            Map.Entry<Long, JLOMEPriceLevel> entry = book.next();
            long price = entry.getKey();

            if (!incomingOrder.isPriceSufficient(price)) {
                break;
            }

            Iterator<JLOMEBookableTradeOrder> level = entry.getValue().iterator();

            while (level.hasNext()) {
                if (!incomingOrder.isFillable()) {
                    break OUTER;
                }

                JLOMEBookableTradeOrder bookOrder = level.next();

                if (!bookOrder.isFillable()) {
                    level.remove();
                    continue;
                }

                incomingOrder.initiateMatch();
                bookOrder.initiateMatch();

                if (!incomingOrder.isCompatibleWith(bookOrder) ||
                        !bookOrder.isCompatibleWith(incomingOrder)) {
                    incomingOrder.discardMatch();
                    bookOrder.discardMatch();
                    continue;
                }

                long avail = bookOrder.getAvailable();

                // NOTE: this is violating the contract. At this point, avail should
                // be guaranteed to be > 0 by copntract.
                if (avail <= 0) {
                    incomingOrder.discardMatch();
                    bookOrder.discardMatch();
                    level.remove();
                    continue;
                }

                long incomingAccepted = normalize(avail, incomingOrder.offer(avail, price));

                if (incomingAccepted == 0) {
                    incomingOrder.discardMatch();
                    bookOrder.discardMatch();
                    continue;
                }

                long bookAccepted = normalize(incomingAccepted, bookOrder.offer(incomingAccepted, price));

                if (bookAccepted == 0) {
                    incomingOrder.discardMatch();
                    bookOrder.discardMatch();
                    continue;
                }

                long amount = Math.min(incomingAccepted, bookAccepted);
                incomingOrder.fill(amount, price);
                bookOrder.fill(amount, price);
                incomingOrder.finalizeMatch();
                bookOrder.finalizeMatch();

                totalFilled += amount;
                totalPrice += amount * price;
                matches.add(new JLOMEMatch(incomingOrder, bookOrder, amount, price));
            }
        }

        if (!incomingOrder.isFillable()) {
            return new JLOMEResponse(JLOMEOrderState.FILLED, totalFilled, totalPrice);
        }

        return new JLOMEResponse(JLOMEOrderState.CANCELLED, totalFilled, totalPrice, matches.iterator());
    }

    private static long normalize(long avail, long accepted) {
        return Math.max(0, Math.min(avail, accepted));
    }
}
