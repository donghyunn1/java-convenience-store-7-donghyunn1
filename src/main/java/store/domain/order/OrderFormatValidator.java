package store.domain.order;

import java.util.regex.Pattern;

public class OrderFormatValidator {
    private static final Pattern ORDER_FORMAT = Pattern.compile("\\[(.*?)-(\\d+)\\]");
    private static final String ERROR_INVALID_INPUT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public void validate(String input) {
        validateEmpty(input);
        validateFormat(input);
    }

    private void validateFormat(String input) {
        if (input.isEmpty() || input.isBlank()) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
    }

    private void validateEmpty(String input) {
        if (!ORDER_FORMAT.matcher(input).matches()) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
    }

    public Pattern getPattern() {
        return ORDER_FORMAT;
    }
}
