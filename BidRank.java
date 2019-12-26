public class BidRank {

    double price;
    int orderId;
    float quantity;

    BidRank(int orderId, double price, float quantity) {
        this.price = price;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    double getPrice() {
        return this.price;
    }

    float getQuantity() {
        return this.quantity;
    }

    int getOrderId() {
        return this.orderId;
    }

    void updateQuantity(float quantity) {
        this.quantity = quantity;
    }
}
