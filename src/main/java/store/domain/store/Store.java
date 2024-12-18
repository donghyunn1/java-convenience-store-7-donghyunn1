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

        if (!promo.isValidOnDate(DateTimes.now().toLocalDate())) {
            processRegularProduct(product,orderQuantity,receipt);
            return;
        }
        int buyCount = promo.getBuy();
        int getCount = promo.getGet();
        Product promotionProduct = findPromotionProduct(product.getName());
        Product regularProduct = findRegularProduct(product.getName());
        int maxPromotionQuantity = promotionProduct.getQuantity();
        int maxFullSets = maxPromotionQuantity / (buyCount + getCount);
        int maxPromotionSets = Math.min(orderQuantity / (buyCount + getCount), maxFullSets);

        int promotionPaidItems = maxPromotionSets * buyCount;  // 실제 구매 수량
        int promotionFreeItems = maxPromotionSets * getCount;  // 증정 수량
        int totalPromotionSetItems = promotionPaidItems + promotionFreeItems;

        int remainingPromotionStock = maxPromotionQuantity - totalPromotionSetItems;
        int remainingOrderQuantity = orderQuantity - totalPromotionSetItems;

        int nonPromotionItems = remainingOrderQuantity;
        if (nonPromotionItems > 0) {
            if (!askNonPromotionPurchase(product, nonPromotionItems)) {
                orderQuantity = totalPromotionSetItems;
                nonPromotionItems = 0;
                remainingOrderQuantity = 0;
            }
        }

        if (promotionPaidItems > 0) {
            receipt.addItem(promotionProduct, promotionPaidItems);  // 실제 구매 수량만 추가
            receipt.setFreeItems(Map.of(product.getName(), promotionFreeItems));  // 증정 수량 설정
            receipt.addPromotionalDiscount(promotionFreeItems * product.getPrice());
            promotionProduct.setQuantity(promotionProduct.getQuantity() - totalPromotionSetItems);
        }

        if (remainingOrderQuantity > 0) {
            if (remainingPromotionStock > 0) {
                int usePromotionStock = Math.min(remainingPromotionStock, remainingOrderQuantity);
                receipt.addItem(promotionProduct, usePromotionStock);  // 추가 구매 수량
                promotionProduct.setQuantity(promotionProduct.getQuantity() - usePromotionStock);
                remainingOrderQuantity -= usePromotionStock;
            }

            if (remainingOrderQuantity > 0 && regularProduct != null) {
                int useRegularStock = Math.min(remainingOrderQuantity, regularProduct.getQuantity());
                if (useRegularStock > 0) {
                    receipt.addItem(regularProduct, useRegularStock);
                    regularProduct.setQuantity(regularProduct.getQuantity() - useRegularStock);
                }
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
        return product.getPromotion() != null &&
                promotions.containsKey(product.getPromotion()) &&
                isValidPromotion(product);
    }

    private boolean isValidPromotion(Product product) {
        String promotionName = product.getPromotion();
        if (promotionName == null || !promotions.containsKey(promotionName)) {
            return false;
        }

        Promotion promotion = promotions.get(promotionName);
        return promotion != null && promotion.isValidOnDate(DateTimes.now().toLocalDate());
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


    public List<Product> getProducts() {
        return new ArrayList<>(products);  // 방어적 복사
    }

}
