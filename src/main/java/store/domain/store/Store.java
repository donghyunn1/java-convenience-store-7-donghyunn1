package store.domain.store;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.file.FileLoader;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.receipt.Receipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {

    private static final int MEMBERSHIP_DISCOUNT_RATE = 30;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Product> products;
    private final Map<String, Promotion> promotions;

    public Store() {
        this.products = new ArrayList<>(FileLoader.loadProducts());
        this.promotions = new HashMap<>(FileLoader.loadPromotions());
    }

    public void displayProducts() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");

        for (Product product : products) {
            String quantityStatus = product.getQuantity() > 0 ?
                    String.valueOf(product.getQuantity()) + "개" : "재고 없음";
            String promotionInfo = product.getPromotion() != null ?
                    " " + product.getPromotion() : "";
            System.out.printf("- %s %,d원 %s%s\n",
                    product.getName(), product.getPrice(), quantityStatus, promotionInfo);
        }
    }

    public Receipt processOrder(Map<String, Integer> orderItems, boolean useMemberShip) {
        Receipt receipt = new Receipt();
        Map<String, Integer> freeItems = new HashMap<>();

        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            String productName = entry.getKey();
            int orderQuantity = entry.getValue();

            Product product = findFirstAvailableProduct(productName);
            if (product == null) continue;

            if (isPromotionProduct(product)) {
                processPromotionProduct(product, orderQuantity, receipt, freeItems);
            } else {
                processRegularProduct(product, orderQuantity, receipt);
            }
        }

        receipt.setFreeItems(freeItems);

        if (useMemberShip) {
            applyMembershipDiscount(receipt);
        }

        return receipt;
    }

    public Map<String, Integer> promotionAddSuggestion(Map<String, Integer> orderItems) {
        Map<String, Integer> updatedOrders = new HashMap<>(orderItems);

        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            String productName = entry.getKey();
            int orderQuantity = entry.getValue();

            Product product = findFirstAvailableProduct(productName);
            if (product == null || !isPromotionProduct(product)) continue;

            Promotion promo = promotions.get(product.getPromotion());
            int buyCount = promo.getBuy();
            int getCount = promo.getGet();

            // 프로모션 조건 미달 체크
            if (orderQuantity > 0 && orderQuantity < (buyCount + getCount) &&
                    product.getQuantity() >= (buyCount + getCount)) {
                System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", productName, getCount);
                if (Console.readLine().equalsIgnoreCase("Y")) {
                    updatedOrders.put(productName, buyCount + getCount);
                }
            }
        }
        return updatedOrders;
    }

    private void applyMembershipDiscount(Receipt receipt) {
        int nonPromotionAmount = receipt.getItems().entrySet().stream()
                .filter(entry -> !isPromotionApplied(entry.getKey()))
                .mapToInt(entry -> entry.getValue().getTotalPrice())
                .sum();

        int membershipDiscount = (nonPromotionAmount * MEMBERSHIP_DISCOUNT_RATE / 100);

        membershipDiscount = Math.min(membershipDiscount, MAX_MEMBERSHIP_DISCOUNT);

        receipt.setMembershipDiscount(membershipDiscount);
    }

    private Product findFirstAvailableProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getQuantity() > 0)
                .findFirst()
                .orElse(null);
    }

    private void processPromotionProduct(Product product, int orderQuantity, Receipt receipt, Map<String, Integer> freeItems) {

        Promotion promo = promotions.get(product.getPromotion());
        int buyCount = promo.getBuy();
        int getCount = promo.getGet();

        // 프로모션 재고와 일반 재고 찾기
        Product promotionProduct = findPromotionProduct(product.getName());
        Product regularProduct = findRegularProduct(product.getName());

        // 프로모션 세트 조건 미달 체크
        if (orderQuantity > 0 && orderQuantity < (buyCount + getCount) &&
                promotionProduct.getQuantity() >= (buyCount + getCount)) {
            if (askAddItems(product, getCount)) {
                orderQuantity = buyCount + getCount;
            }
        }

        // 프로모션 상품으로 처리할 수 있는 최대 수량 계산
        int maxPromotionQuantity = promotionProduct.getQuantity();
        int maxFullSets = maxPromotionQuantity / (buyCount + getCount);
        int maxPromotionSets = Math.min(orderQuantity / (buyCount + getCount), maxFullSets);

        // 프로모션 세트로 처리되는 수량
        int promotionPaidItems = maxPromotionSets * buyCount;
        int promotionFreeItems = maxPromotionSets * getCount;
        int totalPromotionSetItems = promotionPaidItems + promotionFreeItems;

        // 남은 프로모션 재고 계산
        int remainingPromotionStock = maxPromotionQuantity - totalPromotionSetItems;
        int remainingOrderQuantity = orderQuantity - totalPromotionSetItems;

        // 프로모션 미적용 수량 계산 (남은 프로모션 재고 + 필요한 일반 재고)
        int nonPromotionItems = remainingOrderQuantity;
        if (nonPromotionItems > 0) {
            if (!askNonPromotionPurchase(product, nonPromotionItems)) {
                orderQuantity = totalPromotionSetItems;
                nonPromotionItems = 0;
                remainingOrderQuantity = 0;
            }
        }

        // 프로모션 상품 처리 (세트)
        if (promotionPaidItems > 0) {
            receipt.addItem(promotionProduct, promotionPaidItems);
            freeItems.put(product.getName(), promotionFreeItems);
            receipt.addPromotionalDiscount(promotionFreeItems * product.getPrice());
            promotionProduct.setQuantity(promotionProduct.getQuantity() -
                    (promotionPaidItems + promotionFreeItems));
        }

        // 남은 수량 처리
        if (remainingOrderQuantity > 0) {
            // 먼저 남은 프로모션 재고 사용
            if (remainingPromotionStock > 0) {
                int usePromotionStock = Math.min(remainingPromotionStock, remainingOrderQuantity);
                receipt.addItem(promotionProduct, usePromotionStock);
                promotionProduct.setQuantity(promotionProduct.getQuantity() - usePromotionStock);
                remainingOrderQuantity -= usePromotionStock;
            }

            // 그 다음 일반 재고 사용
            if (remainingOrderQuantity > 0 && regularProduct != null) {
                int useRegularStock = Math.min(remainingOrderQuantity, regularProduct.getQuantity());
                receipt.addItem(regularProduct, useRegularStock);
                regularProduct.setQuantity(regularProduct.getQuantity() - useRegularStock);
            }
        }
    }

    private boolean askAddItems(Product product, int getCount) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n",
                product.getName(), getCount);
        return Console.readLine().equalsIgnoreCase("Y");
    }

    private boolean askNonPromotionPurchase(Product product, int quantity) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n", product.getName(), quantity);
        String answer = Console.readLine();
        return answer.equals("Y");
    }

    private boolean isPromotionApplied(String productName) {
        return findPromotionProduct(productName) != null && findPromotionProduct(productName).getQuantity() > 0;
    }

    private boolean isPromotionProduct(Product product) {
        return product.getPromotion() != null && product.getQuantity() > 0
                && promotions.containsKey(product.getPromotion());
    }

    private void processRegularProduct(Product product, int orderQuantity, Receipt receipt) {
        int purchaseQuantity = Math.min(orderQuantity, product.getQuantity());
        if (purchaseQuantity > 0) {
            receipt.addItem(product, purchaseQuantity);
            product.setQuantity(product.getQuantity() - purchaseQuantity);
        }
    }

    private Product findPromotionProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getPromotion() != null)
                .filter(p -> p.getQuantity() > 0)
                .findFirst()
                .orElse(null);
    }

    private Product findRegularProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getPromotion() == null)
                .findFirst()
                .orElse(null);
    }

    private int getTotalAvailableQuantity(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
