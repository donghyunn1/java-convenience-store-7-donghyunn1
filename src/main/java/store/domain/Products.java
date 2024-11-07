package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Products {

    private static List<Product> products = ProductFileReader.readProducts();

    public void displayProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public Product findProductByName(String productName) {
        List<Product> matchingProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                matchingProducts.add(product);
            }
        }
        for (Product product : matchingProducts) {
            if (product.getQuantity() > 0) {
                return product;
            }
        }
        return null;

    }
}
