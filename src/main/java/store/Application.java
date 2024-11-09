package store;

import camp.nextstep.edu.missionutils.Console;
import store.controller.StoreController;
import store.domain.order.OrderParser;
import store.domain.receipt.Receipt;
import store.domain.store.Store;

import java.util.Map;


public class Application {
    public static void main(String[] args) {

        StoreController storeController = new StoreController();
        storeController.run();
    }
}
