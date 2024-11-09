package store.domain.receipt;

import store.domain.product.Product;

import java.util.HashMap;
import java.util.Map;

public class Receipt {

    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String RECEIPT_COLUMNS = "%-8s %6s %12s";
    private static final String ITEM_FORMAT = "%-8s %6d %,12d";
    private static final String FREE_ITEM_HEADER = "=============증     정===============";
    private static final String FREE_ITEM_FORMAT = "%-8s %6d";
    private static final String RECEIPT_DIVIDER = "====================================";
    private static final String TOTAL_FORMAT = "총구매액   %6d %,12d";
    private static final String PROMOTION_DISCOUNT_FORMAT = "행사할인         -%,11d";
    private static final String MEMBERSHIP_DISCOUNT_FORMAT = "멤버십할인       -%,11d";
    private static final String FINAL_AMOUNT_FORMAT = "내실돈           %,12d";

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

    private int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(ReceiptItem::getQuantity)
                .sum();
    }

    public void setMembershipDiscount(int membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
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
        System.out.println(RECEIPT_HEADER);
        System.out.println(String.format(RECEIPT_COLUMNS, "상품명", "수량", "금액"));
    }

    private void printItems() {
        items.forEach((productName, item) ->
                System.out.printf(ITEM_FORMAT + "%n", productName, item.getQuantity(), item.getPrice() * item.getPaidQuantity())
        );
    }

    private void printFreeItems() {
        System.out.println(FREE_ITEM_HEADER);
        items.forEach((productName, item) -> {
            if (item.getFreeQuantity() > 0) {
                System.out.printf(FREE_ITEM_FORMAT + "%n",
                        productName, item.getFreeQuantity());
            }
        });
    }

    private void printDivider() {
        System.out.println(RECEIPT_DIVIDER);
    }

    private void printSummary() {
        System.out.printf(TOTAL_FORMAT + "%n",
                getTotalQuantity(), getTotalAmount());
        System.out.printf(PROMOTION_DISCOUNT_FORMAT + "%n",
                promotionDiscount);
        System.out.printf(MEMBERSHIP_DISCOUNT_FORMAT + "%n",
                membershipDiscount);
        System.out.printf(FINAL_AMOUNT_FORMAT + "%n",
                getFinalAmount());
    }
}
