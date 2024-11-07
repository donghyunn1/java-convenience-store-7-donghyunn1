package store.domain;

import camp.nextstep.edu.missionutils.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderProcessor {

    private final Products products;
    private final PromotionService promotionService;

    public OrderProcessor(Products products, PromotionService promotionService) {
        this.products = products;
        this.promotionService = promotionService;
    }

    public List<OrderItem> processOrder(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)-(\\d+)\\]");
        Matcher matcher = pattern.matcher(input);

        List<OrderItem> orderItems = new ArrayList<>();

        while (matcher.find()) {
            String productName = matcher.group(1);
            int purchaseQuantity = Integer.parseInt(matcher.group(2));

            Product product = products.findProductByName(productName);
            if (product != null) {
                int quantityOfProduct = product.getQuantity();

                if (quantityOfProduct >= purchaseQuantity) {
                    int totalPrice = product.getPrice() * purchaseQuantity;
                    orderItems.add(new OrderItem(product, purchaseQuantity, totalPrice));

                    // 프로모션 적용
                    promotionService.applyPromotion(product, purchaseQuantity);

                }

                // 재고 업데이트
                String userInput = Console.readLine();
                if (userInput.equals("Y")) {
                    int updateQuantity = quantityOfProduct - purchaseQuantity - 1;
                    product.setQuantity(updateQuantity);
                }
                if (userInput.equals("N")) {
                    int updateQuantity = quantityOfProduct - purchaseQuantity;
                    product.setQuantity(updateQuantity);
                }
            }
        }
        return orderItems;
    }
}
