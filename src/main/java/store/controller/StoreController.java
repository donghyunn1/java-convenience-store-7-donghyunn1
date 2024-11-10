package store.controller;

import store.domain.order.OrderParser;
import store.domain.receipt.Receipt;
import store.domain.store.Store;
import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

public class StoreController {
    private final Store store;
    private final InputView inputView;
    private final OutputView outputView;
    private final OrderParser orderParser;

    public StoreController() {
        this.store = new Store();
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.orderParser = new OrderParser(store.getProducts());
    }

    public void run() {
        boolean continueOrder = true;
        while (continueOrder) {
            processOrder();
            continueOrder = inputView.inputMoreOrders();
        }
    }

    private void processOrder() {
        displayProducts();
        Map<String, Integer> orderItems = receiveValidOrder();
        if (orderItems.isEmpty()) {
            return;
        }

        orderItems = store.promotionAddSuggestion(orderItems);
        boolean useMembership = inputView.inputMembership();

        Receipt receipt = store.processOrder(orderItems, useMembership);
        outputView.printReceipt(receipt);
    }

    private Map<String, Integer> receiveValidOrder() {
        while (true) {
            try {
                String orderInput = inputView.inputOrder();
                return orderParser.parse(orderInput);
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }

    private void displayProducts() {
        outputView.printIntroduction();
        store.getProducts()
                .forEach(outputView::printProduct);
    }
}

