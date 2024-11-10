package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String INPUT_ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String INPUT_MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String INPUT_ADD_PROMOTION = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String INPUT_NON_PROMOTION_PURCHASE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String INPUT_REORDER = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String INPUT_RETRY_YES = "Y";
    private static final String INPUT_RETRY_NO = "N";
    private static final String ERROR_INVALID_ORDER_INPUT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_INPUT = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    public String inputOrder() {
        while (true) {
            try {
                System.out.println(INPUT_ORDER);
                String input = Console.readLine();
                validateOrderInput(input);
                System.out.println();
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean inputMembership() {
        while (true) {
            try {
                System.out.println(INPUT_MEMBERSHIP);
                String input = Console.readLine().toUpperCase();
                validateInput(input);
                System.out.println();
                return input.equals(INPUT_RETRY_YES);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean inputAddPromotion(String productName, int freeCount) {
        while (true) {
            try {
                System.out.printf(INPUT_ADD_PROMOTION, productName, freeCount);
                System.out.println();
                String input = Console.readLine().toUpperCase();
                validateInput(input);
                System.out.println();
                return input.equals(INPUT_RETRY_YES);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean inputNonPromotionPurchase(String productName, int quantity) {
        while (true) {
            try {
                System.out.printf(INPUT_NON_PROMOTION_PURCHASE, productName, quantity);
                System.out.println();
                String input = Console.readLine().toUpperCase();
                validateInput(input);
                System.out.println();
                return input.equals(INPUT_RETRY_YES);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean inputMoreOrders() {
        while (true) {
            try {
                System.out.println(INPUT_REORDER);
                String input = Console.readLine().toUpperCase();
                validateInput(input);
                return input.equals(INPUT_RETRY_YES);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateOrderInput(String input) {
        if (input.isEmpty() || input.isBlank()) {
            throw new IllegalArgumentException(ERROR_INVALID_ORDER_INPUT);
        }
    }

    private void validateInput(String input) {
        if (!input.equals(INPUT_RETRY_YES) && !input.equals(INPUT_RETRY_NO)) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
    }
}
