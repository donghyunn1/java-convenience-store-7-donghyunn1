package store.domain;

import camp.nextstep.edu.missionutils.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderService {

    private final Products products;
    private final PromotionService promotionService;
    private final Storage storage;

    public OrderService(Products products, PromotionService promotionService, Storage storage) {
        this.products = products;
        this.promotionService = promotionService;
        this.storage = storage;
    }

    public List<OrderItem> processOrder(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)-(\\d+)\\]");
        Matcher matcher = pattern.matcher(input);

        List<OrderItem> orderItems = new ArrayList<>();

        while (matcher.find()) {
            String productName = matcher.group(1);
            int purchaseQuantity = Integer.parseInt(matcher.group(2));

            Product product = products.findProductByName(productName);

            int quantityOfProduct = product.getQuantity();

            if (quantityOfProduct >= purchaseQuantity) {
                int totalPrice = product.getPrice() * purchaseQuantity;
                orderItems.add(new OrderItem(product, purchaseQuantity, totalPrice));

                // 프로모션 적용
                boolean applyBonus = promotionService.applyPromotion(product, purchaseQuantity);
                storage.updateStorage(product, purchaseQuantity, applyBonus);
            }
        }
        return orderItems;
    }
}
