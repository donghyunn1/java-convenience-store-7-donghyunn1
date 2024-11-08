package store.domain.store;

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

    private void applyPromotionDiscount(Product product, int sets, Promotion promotion,
                                        Receipt receipt, Map<String, Integer> freeItems) {

        int totalItems = sets * (promotion.getBuy() + promotion.getGet());
        int freeItemCount = sets * promotion.getGet();

        freeItems.put(product.getName(),
                freeItems.getOrDefault(product.getName(), 0) + freeItemCount);

        receipt.addItem(product, totalItems - freeItemCount);
        product.setQuantity(product.getQuantity() - totalItems);
        receipt.addPromotionalDiscount(freeItemCount * product.getPrice());
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
