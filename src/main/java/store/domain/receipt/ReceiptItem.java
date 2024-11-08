package store.domain.receipt;

public class ReceiptItem {

    private final int price;
    private int quantity;

    public ReceiptItem(int price) {
        this.price = price;
        this.quantity = 0;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
