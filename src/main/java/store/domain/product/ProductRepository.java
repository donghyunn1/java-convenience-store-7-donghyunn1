package store.domain.product;

import store.domain.file.FileLoader;

import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = FileLoader.loadProducts();
    }

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> findPromotionProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getPromotion() != null)
                .filter(p -> p.getQuantity() > 0)
                .findFirst();
    }

    public Optional<Product> findRegularProduct(String productName) {
        return products.stream()
                .filter(p -> p.getName().equals(productName))
                .filter(p -> p.getPromotion() == null)
                .findFirst();
    }
}
