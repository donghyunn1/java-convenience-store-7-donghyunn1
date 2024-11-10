package store.domain.store;

import store.domain.order.OrderItem;
import store.domain.product.Product;

import java.util.List;

public class StoreValidator {
    private static final String ERROR_NON_EXIST = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_QUANTITY = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final List<Product> products;

    public StoreValidator(List<Product> products) {
        this.products = products;
    }

    public void validate(OrderItem orderItem) {
        validateProductExists(orderItem.getProductName());
        validateQuantity(orderItem);
    }

    private void validateProductExists(String productName) {
        if (!isProductExist(productName)) {
            throw new IllegalArgumentException(ERROR_NON_EXIST);
        }
    }

    private boolean isProductExist(String productName) {
        return products.stream()
                .anyMatch(p -> p.getName().equals(productName));
    }

    private void validateQuantity(OrderItem orderItem) {
        int availableQuantity = getTotalAvailableQuantity(orderItem.getProductName());
        if (orderItem.getQuantity() > availableQuantity) {
            throw new IllegalArgumentException(ERROR_INVALID_QUANTITY);
        }
    }

    private int getTotalAvailableQuantity(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
