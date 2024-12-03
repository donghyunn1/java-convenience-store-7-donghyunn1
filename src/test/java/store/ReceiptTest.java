package store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptItem;

public class ReceiptTest {
    private Receipt receipt;
    private Product product;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        product = new Product("테스트상품", 1000, 10, null);
    }

    @Test
    @DisplayName("상품 추가시 총 금액이 올바르게 계산되어야 한다")
    void calculateTotalAmount() {
        // given
        receipt.addItem(product, 3);

        // when
        int totalAmount = receipt.getTotalAmount();

        // then
        Assertions.assertThat(totalAmount).isEqualTo(3000);
    }

    @Test
    @DisplayName("무료 상품이 정상적으로 추가되어야 한다")
    void addFreeItems() {
        // given
        receipt.addItem(product, 2);

        // when
        receipt.addFreeItem(product.getName(), 1);

        // then
        ReceiptItem item = receipt.getItems().get(product.getName());
        Assertions.assertThat(item.getQuantity()).isEqualTo(3);
        Assertions.assertThat(item.getFreeQuantity()).isEqualTo(1);
        Assertions.assertThat(item.getPaidQuantity()).isEqualTo(2);
    }
}
