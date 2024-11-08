package store.domain.receipt;

import store.domain.product.Product;

import java.util.HashMap;
import java.util.Map;

public class Receipt {

    private Map<Product, Integer> items;
    private Map<String, Integer> freeItems;
    private int promotionDiscount;

    public Receipt() {
        this.items = new HashMap<>();
        this.freeItems = new HashMap<>();
        this.promotionDiscount = 0;
    }

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void setFreeItems(Map<String, Integer> freeItems) {
        this.freeItems = new HashMap<>(freeItems);
    }

    public void addPromotionalDiscount(int promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public int getTotalAmount() {
        return items.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    private int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int getFinalAmount() {
        return getTotalAmount()  - promotionDiscount;
    }
}
