package store.domain.receipt;

import store.domain.product.Product;

import java.util.HashMap;
import java.util.Map;

public class Receipt {

    private Map<String, ReceiptItem> items;
    private int promotionDiscount;
    private int membershipDiscount;

    public Receipt() {
        this.items = new HashMap<>();
        this.promotionDiscount = 0;
        this.membershipDiscount = 0;
    }

    public void addItem(Product product, int quantity) {
        String productName = product.getName();
        if (!items.containsKey(productName)) {
            items.put(productName, new ReceiptItem(product.getPrice()));
        }
        items.get(productName).addQuantity(quantity);
    }

    public void addFreeItem(String productName, int freeQuantity) {
        if (freeQuantity <= 0 || !items.containsKey(productName)) {
            return;
        }
        items.get(productName).addFreeQuantity(freeQuantity);
    }

    public void setFreeItems(Map<String, Integer> freeItems) {
        freeItems.forEach(this::addFreeItem);
    }

    public void addPromotionalDiscount(int promotionDiscount) {
        this.promotionDiscount += promotionDiscount;
    }

    public int getTotalAmount() {
        return items.values().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public Map<String, ReceiptItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(ReceiptItem::getQuantity)
                .sum();
    }

    public void setMembershipDiscount(int membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public int getFinalAmount() {
        return getTotalAmount()  - promotionDiscount - membershipDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}
