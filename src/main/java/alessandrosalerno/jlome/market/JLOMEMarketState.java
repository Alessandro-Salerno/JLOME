package alessandrosalerno.jlome.market;

public enum JLOMEMarketState {
    OPEN, // Orders accepted and executed in FIFO order
    CLOSED, // All orders rejected, FIFO queue empty
}
