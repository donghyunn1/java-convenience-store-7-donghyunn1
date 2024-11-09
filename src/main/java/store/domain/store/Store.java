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

    public Receipt processOrder(Map<String, Integer> orderItems) {
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
        return receipt;
    }

    private Product findFirstAvailableProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getQuantity() > 0)
                .findFirst()
                .orElse(null);
    }

    private void processPromotionProduct(Product product, int orderQuantity, Receipt receipt, Map<String, Integer> freeItems) {

        Promotion promotion = promotions.get(product.getPromotion());
        int buyCount = promotion.getBuy();
        int getCount = promotion.getGet();

        //프로모션 조건에 맞게 가져오지 않은 경우
        if (orderQuantity < (buyCount + getCount) && product.getQuantity() >= (buyCount + getCount)) {

            // 추가 구매 제안
            if (askAddItems(product, buyCount, getCount)) {
                orderQuantity = buyCount + getCount;
                processPromotionSet(product, buyCount, getCount, receipt, freeItems);
            } else {
                processRegularProduct(product, orderQuantity, receipt);
            }
        }
    }

    private void processPromotionSet(Product product, int paidItems, int freeItemCount,
                                     Receipt receipt, Map<String, Integer> freeItems) {

        receipt.addItem(product, paidItems);
        freeItems.put(product.getName(),
                freeItems.getOrDefault(product.getName(), 0) + freeItemCount);
        receipt.addPromotionalDiscount(freeItemCount * product.getPrice());
        product.setQuantity(product.getQuantity() - (paidItems + freeItemCount));
    }

    private boolean askAddItems(Product product, int buyCount, int getCount) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", product.getName(), getCount);
        String answer = Console.readLine();
        return answer.equals("Y");
    }

    private boolean isPromotionProduct(Product product) {
        return product.getPromotion() != null && product.getQuantity() > 0
                && promotions.containsKey(product.getPromotion());
    }

    private void processRegularProduct(Product product, int orderQuantity, Receipt receipt) {
        int purchaseQuantity = Math.min(orderQuantity, product.getQuantity());
        if (purchaseQuantity > 0) {
            addRegularItems(product, purchaseQuantity, receipt);
        }
    }

    private void addRegularItems(Product product, int quantity, Receipt receipt) {
        receipt.addItem(product, quantity);
        product.setQuantity(product.getQuantity() - quantity);
    }

    private List<Product> findProductsByName(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getQuantity() > 0)
                .toList();
    }
}
