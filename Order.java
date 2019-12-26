public class Order {

    String traderName;
    Symbol symbol;
    Side side;
    double price;
    float quantity;

    public Order (String traderName,
                  Symbol symbol,
                  Side side,
                  double price,
                  float quantity) {

        this.traderName = traderName;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    void updateSide(Side side) {
        this.side = side;
    }

    void updatePrice(double price) {
        this.price = price;
    }

    void updateQuantity(float quantity) {
        this.quantity = quantity;
    }
}
