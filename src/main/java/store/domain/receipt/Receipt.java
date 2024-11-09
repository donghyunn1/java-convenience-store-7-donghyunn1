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

    public void setFreeItems(Map<String, Integer> freeItems) {
        for (Map.Entry<String, Integer> entry : freeItems.entrySet()) {
            String productName = entry.getKey();
            int freeQuantity = entry.getValue();
            if (!items.containsKey(productName)) {
                continue;
            }
            items.get(productName).addFreeQuantity(freeQuantity);
        }
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

    private int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(ReceiptItem::getQuantity)
                .sum();
    }

    public void setMembershipDiscount(int membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public int getNonPromotionAmount() {
        return getTotalAmount() - promotionDiscount;
    }

    private int getFinalAmount() {
        return getTotalAmount()  - promotionDiscount - membershipDiscount;
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
        System.out.println(String.format("%-8s %6s %12s", "상품명", "수량", "금액"));
    }

    private void printItems() {
        for (Map.Entry<String, ReceiptItem> entry : items.entrySet()) {
            String productName = entry.getKey();
            ReceiptItem item = entry.getValue();
            int totalQuantity = item.getQuantity();
            int pricePerItem = item.getPrice();
            int totalPrice = totalQuantity * pricePerItem;
            System.out.printf("%-8s %6d %,12d\n", productName, totalQuantity, totalPrice);
        }
    }

    private void printFreeItems() {
        System.out.println("=============증     정===============");
        for (Map.Entry<String, ReceiptItem> entry : items.entrySet()) {
            ReceiptItem item = entry.getValue();
            if (item.getFreeQuantity() > 0)
            System.out.printf("%-8s %6d\n", entry.getKey(), item.getFreeQuantity());
        }
    }

    private void printDivider() {
        System.out.println("====================================");
    }

    private void printSummary() {
        System.out.printf("총구매액   %6d %,12d\n", getTotalQuantity(), getTotalAmount());
        System.out.printf("행사할인\t\t\t\t\t-%,d\n", promotionDiscount);
        System.out.printf("멤버십할인\t\t\t\t\t-%,d\n", membershipDiscount);
        System.out.printf("내실돈           %,12d\n", getFinalAmount());
    }
}
