package store.controller;

import store.domain.Products;
import store.view.InputView;
import store.view.OutputView;

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
        outputView.showStore();
        outputView.showProduct(products);
    }
}
