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

    public void print() {
        printHeader();
        printItems();
        printFreeItems();
        printDivider();
        printSummary();
    }

    private void printHeader() {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t\t수량\t\t금액");
    }

    private void printItems() {
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.printf("%s\t\t\t%d\t\t%,d\n", product.getName(), quantity, product.getPrice() * quantity);
        }
    }

    private void printFreeItems() {
        System.out.println("=============증     정===============");
        for (Map.Entry<String, Integer> entry : freeItems.entrySet()) {
            System.out.printf("%s\t\t\t%d\n", entry.getKey(), entry.getValue());
        }
    }

    private void printDivider() {
        System.out.println("====================================");
    }

    private void printSummary() {
        System.out.printf("총구매액\t\t\t%d %,d\n", getTotalQuantity(), getTotalAmount());
        System.out.printf("행사할인\t\t\t-%,d\n", promotionDiscount);
        System.out.printf("내실돈\t\t\t\t\t%,d\n", getFinalAmount());
    }
}
