package alessandrosalerno.jlome.order;

public interface JLOMEBookableTradeOrder extends JLOMEStatefulTradeOrder {
    long getPrice();

    long getAvailable();

    void setPrice(long price);

    void setAvailable(long available);
}
