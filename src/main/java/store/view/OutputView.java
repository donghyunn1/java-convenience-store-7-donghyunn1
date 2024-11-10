package store.view;

import store.domain.product.Product;
import store.domain.receipt.Receipt;

public class OutputView {

    private static final String PRINT_STORE = "안녕하세요. W편의점입니다.";
    private static final String PRINT_CURRENT_PRODUCT = "현재 보유하고 있는 상품입니다.";
    private static final String STRING_FORMAT = "- %s %,d원 %s %s";

    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String RECEIPT_COLUMNS = "%-8s %6s %12s";
    private static final String RECEIPT_ITEMS_FORMAT = "%-8s %6d %,12d";
    private static final String FREE_ITEMS_HEADER = "=============증     정===============";
    private static final String FREE_ITEMS_FORMAT = "%-8s %6d";
    private static final String RECEIPT_DIVIDER = "====================================";
    private static final String RECEIPT_TOTAL = "총구매액   %6d %,12d";
    private static final String RECEIPT_PROMOTION = "행사할인         %,12d";
    private static final String RECEIPT_MEMBERSHIP = "멤버십할인       %,12d";
    private static final String RECEIPT_FINAL = "내실돈           %,12d";

    public void printIntroduction() {
        System.out.println(PRINT_STORE);
        System.out.println(PRINT_CURRENT_PRODUCT);
        System.out.println();
    }

    public void printProduct(Product product) {
        String quantity = getQuantityStatus(product);
        String promotionInfo = getPromotionInfo(product);

        System.out.printf(STRING_FORMAT, product.getName(), product.getPrice(), quantity, promotionInfo);
        System.out.println();
    }

    public void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printReceiptItems(receipt);
        printFreeItems(receipt);
        printReceiptDivider();
        printReceiptSummary(receipt);
    }

    private void printReceiptHeader() {
        System.out.println(RECEIPT_HEADER);
        System.out.printf(RECEIPT_COLUMNS + "%n", "상품명", "수량", "금액");
    }

    private void printReceiptItems(Receipt receipt) {
        receipt.getItems().forEach((name, item) -> {
            System.out.printf(RECEIPT_ITEMS_FORMAT + "%n", name, item.getQuantity(), item.getPrice() * item.getQuantity());
        });
    }

    private void printFreeItems(Receipt receipt) {
        System.out.println(FREE_ITEMS_HEADER);
        receipt.getItems().forEach((name, item) -> {
            if (item.getFreeQuantity() > 0) {
                System.out.printf(FREE_ITEMS_FORMAT + "%n", name, item.getFreeQuantity());
            }
        });
    }

    private void printReceiptDivider() {
        System.out.println(RECEIPT_DIVIDER);
    }

    private void printReceiptSummary(Receipt receipt) {
        System.out.printf(RECEIPT_TOTAL + "%n", receipt.getTotalQuantity(), receipt.getTotalAmount());
        System.out.printf(RECEIPT_PROMOTION + "%n", -receipt.getPromotionDiscount());
        System.out.printf(RECEIPT_MEMBERSHIP + "%n", -receipt.getMembershipDiscount());
        System.out.printf(RECEIPT_FINAL + "%n", receipt.getFinalAmount());
        System.out.println();
    }

    public void printError(String message) {
        System.out.println(message);
    }

    private String getQuantityStatus(Product product) {
        if (product.getQuantity() > 0) {
            return product.getQuantity() + "개";
        }
        return "재고 없음";
    }

    private String getPromotionInfo(Product product) {
        if (product.getPromotion() != null) {
            return product.getPromotion();
        }
        return "";
    }
}
