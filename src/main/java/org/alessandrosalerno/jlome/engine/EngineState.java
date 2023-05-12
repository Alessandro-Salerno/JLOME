package org.alessandrosalerno.jlome.engine;

public final class EngineState {
    private int currentOrderId;
    private int currentTradeId;

    public EngineState() {
        this.currentOrderId = 0;
        this.currentTradeId = 0;
    }

    public int nextOrder() {
        return ++this.currentOrderId;
    }

    public int nextTrade() {
        return ++this.currentTradeId;
    }

    public int getCurrentOrderId() {
        return this.currentOrderId;
    }

    public int getCurrentTradeId() {
        return this.currentTradeId;
    }
}
