package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptItem;
import store.domain.store.Store;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreTest {
    private Store store;
    private Map<String, Integer> orderItems;

    @BeforeEach
    void setUp() {
        store = new Store();
        orderItems = new HashMap<>();
    }

    @Test
    @DisplayName("일반 상품 주문시 멤버십 할인만 적용되어야 한다")
    void orderRegularProductWithMembership() {
        // given
        orderItems.put("에너지바", 2);  // 일반 상품
        boolean useMembership = true;

        // when
        Receipt receipt = store.processOrder(orderItems, useMembership);

        // then
        assertThat(receipt.getMembershipDiscount()).isLessThanOrEqualTo(8000);  // 최대 할인액 8000원

        assertThat(receipt.getPromotionDiscount()).isZero();

        assertThat(receipt.getTotalAmount()).isPositive();
    }

    @Test
    @DisplayName("2+1 프로모션 상품 주문시 할인이 정확하게 적용되어야 한다")
    void orderPromotionProduct() {
        // given
        orderItems.put("콜라", 3);  // 2+1 프로모션 상품
        boolean useMembership = false;

        // when
        Receipt receipt = store.processOrder(orderItems, useMembership);

        // then
        ReceiptItem item = receipt.getItems().get("콜라");

        assertThat(item.getQuantity()).isEqualTo(3);

        assertThat(item.getFreeQuantity()).isEqualTo(1);

        assertThat(item.getPaidQuantity()).isEqualTo(2);

        assertThat(receipt.getPromotionDiscount()).isPositive();
    }

    @Test
    @DisplayName("재고보다 많은 수량 주문시 가능한 수량만 처리되어야 한다")
    void orderMoreThanStock() {
        // given
        String productName = "에너지바";
        int excessQuantity = 100;

        int initialStock = store.getProducts().stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getQuantity)
                .orElse(0);

        orderItems.put(productName, excessQuantity);

        // when
        Receipt receipt = store.processOrder(orderItems, false);

        // then
        ReceiptItem item = receipt.getItems().get(productName);

        assertThat(item.getQuantity()).isEqualTo(Math.min(excessQuantity, initialStock));

        assertThat(store.getProducts().stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getQuantity)
                .orElse(-1))
                .isZero();
    }
}
