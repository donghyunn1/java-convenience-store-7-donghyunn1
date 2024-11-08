package store.domain.file;

import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoader {

    public FileLoader() {
        loadProducts();
        loadPromotions();
    }

    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                products.add(new Product(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        "null".equals(parts[3]) ? null : parts[3]
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 상품 파일을 불러오는데 실패했습니다.", e);
        }
        return products;
    }

    public static Map<String, Promotion> loadPromotions() {
        Map<String, Promotion> promotions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROMOTIONS_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                promotions.put(parts[0], new Promotion(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDate.parse(parts[3]),
                        LocalDate.parse(parts[4])
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 프로모션 파일을 불러오는데 실패했습니다.", e);
        }
        return promotions;
    }
}