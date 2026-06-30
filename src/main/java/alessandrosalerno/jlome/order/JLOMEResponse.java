package alessandrosalerno.jlome.order;

import java.util.Collections;
import java.util.Iterator;

import alessandrosalerno.jlome.book.JLOMEMatch;

public record JLOMEResponse(JLOMEOrderState state,
        long unitsFilled,
        long totalPrice,
        Iterator<JLOMEMatch> matches) {
    public JLOMEResponse(JLOMEOrderState state, long unitsFilled, long totalPrice) {
        this(state, unitsFilled, totalPrice, Collections.emptyIterator());
    }
}
