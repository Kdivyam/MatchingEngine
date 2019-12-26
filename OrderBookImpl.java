import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;

public class OrderBookImpl implements OrderBook {

    // Hash table for holding each symbol's order book
    Hashtable<String, SymbolOrders> symbolOrderBookTable = new Hashtable<String, SymbolOrders>();

    // Hash table holding all seen orders
    Hashtable<Integer, Order> allOrders = new Hashtable<Integer, Order>();

    Hashtable<Integer, Order> restingOrders = new Hashtable<Integer, Order>();

    Hashtable<Integer, Order> filledOrders = new Hashtable<Integer, Order>();

    // Active symbols with open orders
    Symbol[] symbols;

    static int orderCount = 0;

    // Initialize order books for each symbol
    public void initialize(@NotNull Symbol[] symbols) {

        this.symbols = symbols;

        for (Symbol symbol : symbols) {

            // Create new order book for symbol
            SymbolOrders symbolOrders = new SymbolOrders();

            // Add new order book to hash table
            symbolOrderBookTable.put(symbol.symbolName, symbolOrders);
        }

    }

    public Hashtable<Integer, Order> handleOrder(@NotNull Order order) {

        Hashtable<Integer, Order> matchedOrders = new Hashtable<Integer, Order>();

        // Add order to table with an ID
        orderCount ++;

        // Fetch required order book based on symbol
        SymbolOrders symbolOrders = symbolOrderBookTable.get(order.symbol.symbolName);

        // Run matching logic
        Hashtable<Integer, Float> matchedOrderNumbers = symbolOrders.addOrder(order, orderCount);

        if (matchedOrderNumbers.isEmpty()) {
            restingOrders.put(orderCount, order);
        }
        else {
            matchedOrderNumbers.forEach((orderId, quantity) -> {

                Order finalOrder = restingOrders.get(orderId);

                // Update resting order if necessary
                Float leftovers = finalOrder.quantity - quantity;
                if (leftovers == 0) {
                    restingOrders.remove(orderId);
                } else {
                    Order updatedOrder = finalOrder;
                    updatedOrder.updateQuantity(leftovers);
                    restingOrders.put(orderId, updatedOrder);
                }

                finalOrder.quantity = quantity;

                matchedOrders.put(orderId, finalOrder);
                filledOrders.put(orderId, finalOrder);

            });
        }

        return matchedOrders;
    }

    @Override
    public Symbol[] getSymbols() {
        return symbols;
    }

    public Hashtable<Integer, Order> getFilledOrders()  {
        return filledOrders;
    }

    public Hashtable<Integer, Order> getRestingOrders()  {
        return restingOrders;
    }


    public Boolean updateSide(int orderId, Side side) {

        // Resting order check
        if (!restingOrders.containsKey(orderId)) {
            return false;
        }

        // Retrieve order from current order book
        Order order = restingOrders.get(orderId);

        // Update side
        order.updateSide(side);

        removeOrder(orderId);

        // Inject new order
        handleOrder(order);

        return true;
    }

    public Boolean updatePrice(int orderId, double price) {

        // Resting order check
        if (!restingOrders.containsKey(orderId)) {
            return false;
        }

        // Retrieve order from current order book
        Order order = restingOrders.get(orderId);

        // Update side
        order.updatePrice(price);

        removeOrder(orderId);

        // Inject new order
        handleOrder(order);

        return true;
    }

    public Boolean updateQuantity(int orderId, float quantity) {

        // Resting order check
        if (!restingOrders.containsKey(orderId)) {
            return false;
        }

        // Retrieve order from current order book
        Order order = restingOrders.get(orderId);

        // Update side
        order.updateQuantity(quantity);

        removeOrder(orderId);

        // Inject new order
        handleOrder(order);

        return true;
    }

    private void removeOrder(int orderId) {

        // Retrieve old order
        Order order = restingOrders.get(orderId);

        // Fetch required order book based on symbol
        SymbolOrders symbolOrders = symbolOrderBookTable.get(order.symbol.symbolName);

        // Pop old order from symbol order book
        symbolOrders.removeOrder(orderId, order.side);

        // Discard old order
        restingOrders.remove(orderId);


    }
}
