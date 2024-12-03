package store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.promotion.PromotionService;
import store.domain.receipt.Receipt;

public class PromotionServiceTest {
    private PromotionService promotionService;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        promotionService = new PromotionService();
        receipt = new Receipt();
    }

    @Test
    @DisplayName("멤버십 할인이 정상적으로 적용되어야 한다")
    void applyMembershipDiscount() {
        // given
        Product product = new Product("일반상품", 10000, 10, null);
        receipt.addItem(product, 1);

        // when
        promotionService.applyMembershipDiscount(receipt);

        // then
        Assertions.assertThat(receipt.getMembershipDiscount()).isEqualTo(3000);
    }

    @Test
    @DisplayName("멤버십 할인이 최대 금액을 초과하지 않아야 한다")
    void membershipDiscountShouldNotExceedMaximum() {
        // given
        Product product = new Product("고가상품", 100000, 10, null);
        receipt.addItem(product, 1);

        // when
        promotionService.applyMembershipDiscount(receipt);

        // then
        Assertions.assertThat(receipt.getMembershipDiscount()).isEqualTo(8000);
    }
}
