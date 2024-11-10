package store.domain.order;

import store.domain.product.Product;
import store.domain.store.StoreValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderParser {
    private final OrderFormatValidator formatValidator;
    private final StoreValidator storeValidator;

    public OrderParser(List<Product> products) {
        this.formatValidator = new OrderFormatValidator();
        this.storeValidator = new StoreValidator(products);
    }

    public Map<String, Integer> parse(String input) {
        formatValidator.validate(input);
        return parseOrderItems(input);
    }

    private Map<String, Integer> parseOrderItems(String input) {
        Map<String, Integer> orderItems = new HashMap<>();
        Matcher matcher = formatValidator.getPattern().matcher(input);

        while (matcher.find()) {
            OrderItem orderItem = createOrderItem(matcher);
            storeValidator.validate(orderItem);
            orderItems.put(orderItem.getProductName(), orderItem.getQuantity());
        }
        return orderItems;
    }

    private OrderItem createOrderItem(Matcher matcher) {
        String productName = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new OrderItem(productName, quantity);
    }
}

