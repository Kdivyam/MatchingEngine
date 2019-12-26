import java.util.Hashtable;

public class OrderBookTest {


    public static void testA() throws InterruptedException {

        OrderBookImpl orderBook = new OrderBookImpl();

        // Generate symbols
        Symbol msft = new Symbol(1, "MSFT");
        Symbol aapl = new Symbol(2, "AAPL");
        Symbol fb = new Symbol(3, "FB");

        Symbol[] symbolArray = {msft, aapl, fb};

        orderBook.initialize(symbolArray);

        // Test 1 [Two orders cancel out] **********

        Order orderA = new Order("Mike", msft, Side.BUY, 152.5, 100);
        orderBook.handleOrder(orderA);
        Hashtable<Integer, Order> restingOrders = orderBook.getRestingOrders();

        Order orderB = new Order("James", msft, Side.SELL, 152.4, 100);
        Hashtable<Integer, Order> filledOrders = orderBook.handleOrder(orderB);

        restingOrders = orderBook.getRestingOrders();

        // Test 2 [Best order wins] **********

        orderA = new Order("Mike", msft, Side.BUY, 152.5, 100);
        orderBook.handleOrder(orderA);
        restingOrders = orderBook.getRestingOrders();

        orderB = new Order("Bob", msft, Side.BUY, 153.6, 100);
        orderBook.handleOrder(orderB);
        restingOrders = orderBook.getRestingOrders();

        Order orderC = new Order("James", msft, Side.SELL, 152.4, 100);
        filledOrders = orderBook.handleOrder(orderC);
        restingOrders = orderBook.getRestingOrders();





    }

    public static void testB() throws InterruptedException {
        OrderBookImpl orderBook = new OrderBookImpl();

    }


    public static void main(String[] args) throws InterruptedException {
        testA();
        testB();
    }
}
