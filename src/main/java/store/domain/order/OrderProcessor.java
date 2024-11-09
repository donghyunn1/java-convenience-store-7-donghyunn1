package store.domain.order;

import store.domain.product.Product;
import store.domain.product.ProductRepository;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionService;
import store.domain.receipt.Receipt;
import store.view.InputView;

import java.util.Map;

public class OrderProcessor {

    private final ProductRepository productRepository;
    private final PromotionService promotionService;
    private final InputView inputView;

    public OrderProcessor(ProductRepository productRepository, PromotionService promotionService, InputView inputView) {
        this.productRepository = productRepository;
        this.promotionService = promotionService;
        this.inputView = inputView;
    }

    public Receipt process(Map<String, Integer> orderItems, boolean useMembership) {
        Receipt receipt = new Receipt();

        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            processOrderItem(entry.getKey(), entry.getValue(), receipt);
        }

        if (useMembership) {
            promotionService.applyMembershipDiscount(receipt);
        }

        return receipt;
    }

    private void processOrderItem(String productName, int quantity, Receipt receipt) {
        productRepository.findPromotionProduct(productName)
                .ifPresentOrElse(product -> processPromotionProduct(product, quantity, receipt),
                        () -> processRegularProduct(productName, quantity, receipt)
                );
    }

    private void processPromotionProduct(Product product, int orderQuantity, Receipt receipt) {
        // 프로모션 수량 계산
        Promotion promotion = promotionService.getPromotion(product.getPromotion());
        int buyCount = promotion.getBuy();
        int getCount = promotion.getGet();

        // 프로모션 세트 수 계산
        int maxSets = orderQuantity / (buyCount + getCount);
        int promotionQuantity = maxSets * buyCount;
        int freeQuantity = maxSets * getCount;
        int remainingQuantity = orderQuantity - (promotionQuantity + freeQuantity);

        // 프로모션 미적용 수량에 대한 사용자 확인
        if (remainingQuantity > 0) {
            boolean purchaseNonPromotion = inputView.inputNonPromotionPurchase(
                    product.getName(),
                    remainingQuantity
            );
            if (!purchaseNonPromotion) {
                remainingQuantity = 0;
            }
        }

        // 구매 처리
        if (promotionQuantity > 0) {
            receipt.addItem(product, promotionQuantity);
            receipt.addFreeItem(product.getName(), freeQuantity);
            receipt.addPromotionalDiscount(freeQuantity * product.getPrice());
            product.decreaseQuantity(promotionQuantity + freeQuantity);
        }

        if (remainingQuantity > 0) {
            receipt.addItem(product, remainingQuantity);
            product.decreaseQuantity(remainingQuantity);
        }
    }

    private void processRegularProduct(String productName, int quantity, Receipt receipt) {
        productRepository.findRegularProduct(productName).ifPresent(product -> {
            int availableQuantity = Math.min(quantity, product.getQuantity());
            if (availableQuantity > 0) {
                receipt.addItem(product, availableQuantity);
                product.decreaseQuantity(availableQuantity);
            }
        });
    }
}
