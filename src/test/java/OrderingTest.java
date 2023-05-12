import org.alessandrosalerno.jlome.engine.LightMatchingEngine;
import org.alessandrosalerno.jlome.order.processing.OrderPlacementConfirmation;
import org.alessandrosalerno.jlome.order.Side;

public class OrderingTest {
    public static void main(String[] args) {
        LightMatchingEngine engine = new LightMatchingEngine();

        System.out.println("BUY");
        OrderPlacementConfirmation buy = engine.addOrder("EUR/USD", 1.10, 1000, Side.Buy);
        System.out.println("Number of trades = " + buy.trades().length);
        System.out.println("Buy order quantity = " + buy.order().getQuantity());
        System.out.println("Buy order filled = " + buy.order().getCumulativeQuantity());
        System.out.println("Buy order leaves = " + buy.order().getLeft());

        System.out.println("\n\nSELL");
        OrderPlacementConfirmation sell = engine.addOrder("EUR/USD", 1.10, 700, Side.Sell);
        System.out.println("Number of trades = " + sell.trades().length);
        System.out.println("Buy order quantity = " + buy.order().getQuantity());
        System.out.println("Buy order filled = " + buy.order().getCumulativeQuantity());
        System.out.println("Buy order leaves = " + buy.order().getLeft());
        System.out.println("Trade price = " + sell.trades()[0].getTradePrice());
        System.out.println("Trade quantity = " + sell.trades()[0].getTradeQuantity());
        System.out.println("Trade side = " + sell.trades()[0].getTradeSide());

        System.out.println("ID MAP");
        engine.getOrderBooks().get("EUR/USD").getOrderIdMap().keySet().forEach(System.out::println);

        System.out.println("ASKS");
        engine.getOrderBooks().get("EUR/USD").getAsks().keySet().forEach(System.out::println);

        System.out.println("BIDS");
        engine.getOrderBooks().get("EUR/USD").getBids().keySet().forEach(System.out::println);
    }
}
