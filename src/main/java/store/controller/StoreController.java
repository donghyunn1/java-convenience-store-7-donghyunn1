package store.controller;

import store.domain.*;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final OrderService orderService;

    public StoreController(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = new Products();
        this.orderService = new OrderService(products, new PromotionService(), new Storage());
    }

    public void run() {
        while (true) {
            outputView.showStore();
            outputView.showProduct(products);
            List<OrderItem> orderItems = orderService.processOrder(inputView.orderInput());
            showOrderDetails(orderItems);
        }
    }

    public void showOrderDetails(List<OrderItem> orderItems) {

        System.out.println();
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t\t수량\t\t금액");

        for (OrderItem orderItem : orderItems) {
            System.out.printf("%-3s\t\t\t%d\t\t%s\n", orderItem.getProduct().getName(), orderItem.getQuantity(), orderItem.getTotalPrice());
        }
    }
}
