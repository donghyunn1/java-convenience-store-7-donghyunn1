package store.domain.order;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderParser {
    private static final Pattern PATTERN = Pattern.compile("\\[(.*?)-(\\d+)\\]");

    public static Map<String, Integer> parse(String input) {
        Map<String, Integer> orderItems = new HashMap<>();
        Matcher matcher = PATTERN.matcher(input);

        while (matcher.find()) {
            try {
                String productName = matcher.group(1);
                int quantity = Integer.parseInt(matcher.group(2));

                orderItems.put(productName, quantity);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 주문 형식 입니다.");
            }
        }
        return orderItems;
    }

}

