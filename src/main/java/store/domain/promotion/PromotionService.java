package store.domain.promotion;

import store.domain.file.FileLoader;
import store.domain.product.Product;
import store.domain.receipt.Receipt;

import java.util.Map;

public class PromotionService {

    private static final int MEMBERSHIP_DISCOUNT_RATE = 30;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    private final Map<String, Promotion> promotions;

    public PromotionService() {
        this.promotions = FileLoader.loadPromotions();
    }

    public void applyMembershipDiscount(Receipt receipt) {
        int nonPromotionAmount = calculateNonPromotionAmount(receipt);
        int membershipDiscount = calculateMembershipDiscount(nonPromotionAmount);
        receipt.setMembershipDiscount(membershipDiscount);
    }

    private int calculateNonPromotionAmount(Receipt receipt) {
        return receipt.getItems().entrySet().stream()
                .filter(entry -> !isPromotionApplied(entry.getKey()))
                .mapToInt(entry -> entry.getValue().getTotalPrice())
                .sum();
    }

    private boolean isPromotionApplied(String productName) {
        return promotions.containsKey(productName);
    }

    private int calculateMembershipDiscount(int amount) {
        int discount = (amount * MEMBERSHIP_DISCOUNT_RATE / 100);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }

    public boolean isPromotionProduct(Product product) {
        return product.getPromotion() != null &&
                promotions.containsKey(product.getPromotion());
    }

    public int calculatePromotionDiscount(Product product, int orderQuantity) {
        if (!isPromotionProduct(product)) {
            return 0;
        }

        Promotion promotion = promotions.get(product.getPromotion());
        int buyCount = promotion.getBuy();
        int getCount = promotion.getGet();

        int sets = orderQuantity / (buyCount + getCount);
        return sets * getCount * product.getPrice();
    }

    public int calculateFreeItemCount(Product product, int orderQuantity) {
        if (!isPromotionProduct(product)) {
            return 0;
        }
        Promotion promotion = promotions.get(product.getPromotion());
        int buyCount = promotion.getBuy();
        int getCount = promotion.getGet();

        int sets = orderQuantity / (buyCount + getCount);
        return sets * getCount;
    }

    public Promotion getPromotion(String promotionName) {
        return promotions.get(promotionName);
    }
}
