package alessandrosalerno.jlome.order;

// NOTE: These apply to ALL order types (including makeshift orders like cancel
//       orders) with **exactly** the same meaning:
//          - An order is accepted if and only if it is on the order book at return time
//          - An order is rejected if and only if it never reached the order book
//          - An order is filled if and only if the entire request has been handled, and it
//              is not on the order book
//          - An order is cancelled if and only if it was not accepted, not rejected, and not
//              filled, i.e. it was not carried to full completion and was also not parked in
//              the order book
//
// NOTE: Having the same meaning does not necessarily imply having directly intuitive
//       behavior. For example, a successful cancel order will return FILLED, not CANCELLED
//       as per the definition of FILLED above. Similarly, a partially filled taker order
//       will be in a CANCELLED state, as opposed to a maker order which will be in an
//       ACCEPTED state.
//
// Intuitive, informal definitions are listed below next to each element.
public enum JLOMEOrderState {
    ACCEPTED, // Not fully filled, in order book
    REJECTED, // Market closd, malformed/unfillable order, or other trading restriction
    FILLED, // Fully filled
    CANCELLED; // Partially filled, not in order book
}
