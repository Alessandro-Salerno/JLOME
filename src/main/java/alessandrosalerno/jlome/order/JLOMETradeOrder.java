package alessandrosalerno.jlome.order;

/**
 * Interface invariant: Values returned by this interface's get methods
 * immutabe.
 * They shall be set once at a moment guaranteed to preceed the first call to
 * any one of these methods and never be subsequently altered for a given
 * instance.
 */
public interface JLOMETradeOrder extends JLOMEOrder {
    JLOMEOrderSide getSide();
}
