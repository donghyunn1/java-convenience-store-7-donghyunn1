package store.domain;

public class OrderItem {

    Product product;
    private int quantity;
    private int totalPrice;

    public OrderItem(Product product, int purchaseQuantity, int totalPrice) {
        this.product = product;
        this.quantity = purchaseQuantity;
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
