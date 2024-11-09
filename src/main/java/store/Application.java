package store;

import camp.nextstep.edu.missionutils.Console;
import store.domain.order.OrderParser;
import store.domain.receipt.Receipt;
import store.domain.store.Store;

import java.util.Map;


public class Application {
    public static void main(String[] args) {
        try {
            Store store = new Store();
            boolean continueOrder = true;

            while(continueOrder) {
                processOrder(store);
                continueOrder = askMoreOrders();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processOrder(Store store) {
        try {
            store.displayProducts();
            String orderInput = receiveOrder();
            Map<String, Integer> orderItems = OrderParser.parse(orderInput);

            Map<String, Integer> finalOrderItems = store.promotionAddSuggestion(orderItems);

            boolean isMemberShip = useMembership();

            Receipt receipt = store.processOrder(finalOrderItems, isMemberShip);
            receipt.print();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean useMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine().equals("Y");
    }

    private static String receiveOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    private static boolean askMoreOrders() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String answer = Console.readLine();
        return answer.equalsIgnoreCase("y");
    }
}
