package store.domain.receipt;

public class ReceiptItem {

    private final int price;
    private int quantity;
    private int freeQuantity;

    public ReceiptItem(int price) {
        this.price = price;
        this.quantity = 0;
        this.freeQuantity = 0;
    }

    public int getPrice() {
        return price;
    }
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void addFreeQuantity(int freeQuantity) {
        this.freeQuantity += freeQuantity;
    }

    public int getQuantity() {
        return quantity + freeQuantity;
    }

    public int getPaidQuantity() {
        return quantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
