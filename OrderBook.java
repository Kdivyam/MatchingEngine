import java.util.Hashtable;

public interface OrderBook {

    // Set the symbols allowed for trading
    void initialize(Symbol[] symbols);

    // Handle incoming order
    Hashtable<Integer, Order> handleOrder(Order order);

    // Returns all symbols currently in OrderBook
    Symbol[] getSymbols();

    // Returns list of all filled orders in OrderBook
    Hashtable<Integer, Order> getFilledOrders();

    // Returns list of all resting orders in OrderBook
    Hashtable<Integer, Order> getRestingOrders();

    // Update order details if order is resting(unfilled)
    Boolean updateSide(int orderId, Side side);
    Boolean updatePrice(int orderId, double price);
    Boolean updateQuantity(int orderId, float quantity);
}
