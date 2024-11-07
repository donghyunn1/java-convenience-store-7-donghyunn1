package store.controller;

import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Products;
import store.view.InputView;
import store.view.OutputView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;

    public StoreController(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = new Products();
    }

    public void run() {
        while (true) {
            outputView.showStore();
            outputView.showProduct(products);
            List<OrderItem> orderItems = processOrder(inputView.orderInput());
            showOrderDetails(orderItems);
        }
    }

    private List<OrderItem> processOrder(String input) {
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
                    product.setQuantity(quantityOfProduct - purchaseQuantity);
                }
            }
        }
        return orderItems;
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
