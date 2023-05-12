package org.alessandrosalerno.jlome.order.processing.backend;

import org.alessandrosalerno.jlome.engine.EngineState;
import org.alessandrosalerno.jlome.market.MarketDepth;
import org.alessandrosalerno.jlome.market.MarketInformation;
import org.alessandrosalerno.jlome.order.*;
import org.alessandrosalerno.jlome.order.processing.OrderPlacementConfirmation;
import org.alessandrosalerno.jlome.order.processing.OrderProcessorBackend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public final class DefaultOrderProcessorBackend implements OrderProcessorBackend {
    private static boolean shouldRun(Order order, double otherPrice) {
        return order.getSide() == Side.Buy && order.getPrice() >= otherPrice
                || order.getSide() == Side.Sell && order.getPrice() <= otherPrice;
    }

    private static Side oppositeSide(Side side) {
        return switch (side) {
            case Buy -> Side.Sell;
            case Sell -> Side.Buy;
            case Unset -> null;
        };
    }

    @Override
    public OrderPlacementConfirmation process(Order order, MarketInformation marketInformation, EngineState state, OrderBook orderBook) {
        List<Trade> trades = new ArrayList<>();

        double bestPrice = (marketInformation.opposite().keySet().size() > 0)
                            ? Collections.min(marketInformation.opposite().keySet())
                            : Double.POSITIVE_INFINITY;

        while (bestPrice != Double.POSITIVE_INFINITY
                && (order.getPrice() == 0 || DefaultOrderProcessorBackend.shouldRun(order, bestPrice))
                && order.getLeft() >= 1e-9) {
            AtomicReference<Double> bestPriceQuantityRef = new AtomicReference<>((double) 0);
            marketInformation.opposite().get(bestPrice).stream()
                                                        .parallel()
                                                        .forEach(o -> bestPriceQuantityRef.updateAndGet(v -> v + o.getLeft()));

            double bestPriceQuantity = bestPriceQuantityRef.get();
            double matchQuantity = Double.min(bestPriceQuantity, order.getLeft());
            assert matchQuantity >= 1e-9;

            order.setCumulativeQuantity(order.getCumulativeQuantity() + matchQuantity);
            order.setLeft(order.getLeft() - matchQuantity);
            trades.add(new Trade(order.getOrderId(),
                                    order.getSymbol(),
                                    bestPrice,
                                    matchQuantity,
                                    order.getSide(),
                                    state.nextTrade()));

            while (matchQuantity >= 1e-9) {
                Order hitOrder = marketInformation.opposite().get(bestPrice).get(0);
                double orderMatchQuantity = Double.min(matchQuantity, hitOrder.getLeft());
                trades.add(new Trade(hitOrder.getOrderId(),
                                        order.getSymbol(),
                                        bestPrice,
                                        orderMatchQuantity,
                                        DefaultOrderProcessorBackend.oppositeSide(order.getSide()),
                                        state.nextTrade()));

                hitOrder.setCumulativeQuantity(hitOrder.getCumulativeQuantity() + orderMatchQuantity);
                hitOrder.setLeft(hitOrder.getLeft() - orderMatchQuantity);
                matchQuantity -= orderMatchQuantity;

                if (hitOrder.getLeft() < 1e-9)
                    marketInformation.opposite()
                                        .get(bestPrice)
                                        .remove(0);
            }

            if (marketInformation.opposite().get(bestPrice).size() == 0)
                marketInformation.opposite().remove(bestPrice);

            bestPrice = (marketInformation.opposite().keySet().size() > 0)
                            ? Collections.min(marketInformation.opposite().keySet())
                            : Double.POSITIVE_INFINITY;
        }

        if (order.getLeft() >= 1e-9) {
            MarketDepth depth = marketInformation.side().setDefault(order.getPrice(), new MarketDepth());
            depth.add(order);
            orderBook.getOrderIdMap().put(order.getOrderId(), order);
        }

        Trade[] tradeArray = new Trade[trades.size()];
        trades.toArray(tradeArray);

        return new OrderPlacementConfirmation(order, tradeArray);
    }
}
