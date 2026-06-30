package alessandrosalerno.jlome.order;

import alessandrosalerno.jlome.market.JLOMEMarket;

/**
 * Interface invariant: Classes implementing this interface may keep internal
 * state. Methods are guaranteed to run in declaration order (reinforced by
 * preconditions) and in a single-threaded environment.
 *
 * When matching orders, all comparative methods are guaranteed to be called for
 * incoming orders **before** they're called on book orders. This prevents rouge
 * orders from clogging up the engine by, for example, always stating
 * isFillable() = false, forcing the engine to iterate through all book orders.
 *
 * Implementations MUST NOT alter state of other JLOMEStatefulOrder instances
 * passed.
 */
public interface JLOMEStatefulTradeOrder extends JLOMETradeOrder {
    /**
     * If false, the order is rejected (incompatible with this market)
     */
    boolean register(JLOMEMarket market);

    /**
     * If false, order book traversal is skipped entirely. Depending on other order
     * characteristics, this may lead to unsatisfiable order orders, or orders that
     * enter the order book regardless.
     *
     * This method is only called once per order submission.
     *
     * @return true if matchable, false otherwise
     */
    boolean isMatchable();

    /**
     * If false, order book traversal is aborted as the order book is always
     * traversed from most to least favorable price for the incoming order's side.
     * If an order rejects a price level deemed insufficient, there's no benefit in
     * attempting other matches.
     *
     * This method is called only once per price level.
     *
     * Preconditions:
     * - this.isMatchable()
     *
     * @param price the current price level
     *
     * @return true if price is sufficient to proceed, false otherwise
     */
    boolean isPriceSufficient(long price);

    /**
     * Preconditions:
     * - this.isPriceSufficient(current price level)
     *
     * If false, then no more matching actions will be taken on this order
     */
    boolean isFillable();

    void initiateMatch();

    /**
     * Preconditions:
     * - other != null
     * - other.side != this.side
     * - this.isFillable()
     * - other.isFillable()
     * 
     * @param other the other order
     *
     * @return true if theoretically compatible (orders may be theoretically
     *         compatible and practically incompatible for any number of reasons)
     */
    boolean isCompatibleWith(JLOMETradeOrder other);

    /**
     * Preconditions:
     * - this.isFillable()
     * - avail > 0
     * - avail, price come from some order o such that this.isCompatibleWith(o) &&
     * o.isCompatibleWith(this)
     * - price = best(o.price for all o such that this.isCompatibleWith(o) &&
     * o.isCompatibleWith(this))
     *
     * @param avail the total available size of the matched order
     * @param price the offered price per unit
     *
     * @return the number of units (<= avail) to use to fill this order. The caller
     *         performs max(0, min(avail, this.fill(avail, price)))
     */
    long offer(long avail, long price);

    /**
     * Preconditions:
     * - this.offer() > 0
     * - amount > 0
     * - amount <= avail from offer call
     * - amount <= this.offer()
     * - price = price from offer call
     *
     * Postconditions:
     * - State coherency
     * - Progress
     *
     * @param amount the number of units to fill
     * @param price  the price per unit
     */
    void fill(long amount, long price);

    void discardMatch();

    void finalizeMatch();

    // NOTE: The sequence of calls is:
    // - avail = bookOrder.getAvailable()
    // - price = bookOrder.getPrice()
    // - incomingAccepted = incomingOrder.offer(avail, price)
    // - incomingAccepted = normalize(incomingAccepted)
    // - bookAccepted = bookOrder.offer(incomingAccepted, price)
    // - bookAccepted = normalize(bookAccepted)
    // - amount = min(incomingAccepted, bookAccepted)
    // - incomingOrder.fill(amount, price)
    // - bookOrder.fill(amount, price)
}
