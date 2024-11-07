package store.domain;

import java.util.List;

public class Products {

    private static List<Product> products = ProductFileReader.readProducts();

    public void displayProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
