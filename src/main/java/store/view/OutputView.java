package store.view;

import store.domain.product.Product;

public class OutputView {

    private static final String PRINT_STORE = "안녕하세요. W편의점입니다.";
    private static final String PRINT_CURRENT_PRODUCT = "현재 보유하고 있는 상품입니다.";
    private static final String STRING_FORMAT = "- %s %,d원 %s %s";

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
