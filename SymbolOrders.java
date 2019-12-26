import java.util.*;

public class SymbolOrders {

    Comparator<BidRank> sellOrderPriority
            = Comparator.comparingDouble(BidRank::getPrice)
            .thenComparingInt(BidRank::getOrderId);

    Comparator<BidRank> buyOrderPriority
            = (Comparator.comparingDouble(BidRank::getPrice)).reversed()
            .thenComparingInt(BidRank::getOrderId);

    // Separate priority queues for buy/sell orders
    PriorityQueue<BidRank> buyOrders = new PriorityQueue<BidRank>(buyOrderPriority);
    PriorityQueue<BidRank> sellOrders = new PriorityQueue<BidRank>(sellOrderPriority);

    // Past-filled orders
    Order[] filledOrders;

    Hashtable<Integer, BidRank> bidRankTracker = new Hashtable<Integer, BidRank>();


    Hashtable<Integer, Float> addOrder(Order order, int orderId) {

        if (order.side.equals("BUY")) {
            return handleBuyOrder(order, orderId);
        } else {
            return handleSellOrder(order, orderId);
        }

    }

    private Hashtable<Integer, Float> handleBuyOrder(Order order, int orderId) {

        Hashtable<Integer, Float> matchedOrders = new Hashtable<Integer, Float>();

        float reqQuantity = order.quantity;

        while (reqQuantity > 0) {

            // Case 1: There is a satisfying order...
            if (buyOrders.peek().getPrice() <= order.price) {

                // Case 1a: Required quantity is met
                if (order.quantity <= buyOrders.peek().getQuantity()) {

                    matchedOrders.put(buyOrders.peek().getOrderId(), reqQuantity);

                    float unfilledQuantity = buyOrders.peek().getQuantity() - reqQuantity;
                    if (reqQuantity == 0) {
                        buyOrders.poll();
                    }
                    buyOrders.peek().updateQuantity(unfilledQuantity);

                    break;

                }

                // We need more orders!
                 else {

                    BidRank topBidRank = buyOrders.poll();
                    reqQuantity -= topBidRank.getQuantity();

                    matchedOrders.put(topBidRank.getOrderId(), topBidRank.getQuantity());
                }
            }

            // Case 2: No satisfying order, add to resting orders
            else {

                BidRank newBidRank = new BidRank(orderId, order.price, order.quantity);
                sellOrders.add(newBidRank);

                break;

            }
        }

        return matchedOrders;

    }

    private Hashtable<Integer, Float> handleSellOrder(Order order, int orderId) {

        Hashtable<Integer, Float> matchedOrders = new Hashtable<Integer, Float>();

        float reqQuantity = order.quantity;

        while (reqQuantity > 0) {

            // Case 1: There is a satisfying order...
            if (sellOrders.peek().getPrice() <= order.price) {

                // Case 1a: Required quantity is met
                if (order.quantity <= sellOrders.peek().getQuantity()) {

                    matchedOrders.put(sellOrders.peek().getOrderId(), reqQuantity);

                    float unfilledQuantity = sellOrders.peek().getQuantity() - reqQuantity;
                    if (reqQuantity == 0) {
                        sellOrders.poll();
                    }
                    sellOrders.peek().updateQuantity(unfilledQuantity);

                    break;

                }

                // We need more orders!
                else {

                    BidRank topBidRank = sellOrders.poll();
                    reqQuantity -= topBidRank.getQuantity();

                    matchedOrders.put(topBidRank.getOrderId(), topBidRank.getQuantity());
                }
            }

            // Case 2: No satisfying order, add to resting orders
            else {

                BidRank newBidRank = new BidRank(orderId, order.price, order.quantity);
                buyOrders.add(newBidRank);

                break;

            }
        }

        return matchedOrders;

    }

    void removeOrder(int orderId, Side side) {

        BidRank bidRank = bidRankTracker.get(orderId);

        if (side.equals("BUY")) {
            buyOrders.remove(bidRank);
        }
        else {
            sellOrders.remove(bidRank);
        }

    }

}
