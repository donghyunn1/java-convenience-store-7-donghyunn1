package store.domain.order;

import store.domain.product.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderParser {
    private static final Pattern PATTERN = Pattern.compile("\\[(.*?)-(\\d+)\\]");
    private static final String ERROR_INVALID_INPUT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_QUANTITY = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String ERROR_NON_EXIST = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    public static Map<String, Integer> parse(String input, List<Product> products) {
        validateInput(input);
        Map<String, Integer> orderItems = new HashMap<>();
        Matcher matcher = PATTERN.matcher(input);

        while (matcher.find()) {
            String productName = matcher.group(1);
            int orderQuantity = Integer.parseInt(matcher.group(2));

            if (!isProductExist(productName, products)) {
                throw new IllegalArgumentException(ERROR_NON_EXIST);
            }

            validateOrder(productName, orderQuantity, products);
            orderItems.put(productName, orderQuantity);
        }
        return orderItems;
    }


    private static void validateInput(String input) {
        if (input.isEmpty() || input.isBlank()) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
        if (!PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
    }

    private static void validateOrder(String productName, int orderQuantity, List<Product> products) {
        int totalAvailableQuantity = getTotalAvailableQuantity(productName, products);

        if (orderQuantity > totalAvailableQuantity) {
            throw new IllegalArgumentException(ERROR_INVALID_QUANTITY);
        }
    }

    private static boolean isProductExist(String productName, List<Product> products) {
        return products.stream()
                .anyMatch(p -> p.getName().equals(productName));
    }

    private static int getTotalAvailableQuantity(String productName, List<Product> products) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}

