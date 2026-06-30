package alessandrosalerno.jlome.book;

import alessandrosalerno.jlome.order.JLOMEBookableTradeOrder;

public record JLOMEOrderBookEntry(JLOMEBookableTradeOrder order, JLOMEPriceLevel priceLevel) {
}
