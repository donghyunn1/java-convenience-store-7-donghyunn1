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
    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";
    private static final String NO_STOCK = "재고 없음";

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        Map<String, Boolean> promotionProducts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_PATH))) {
            skipHeader(reader);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Product product = createProduct(parts);
                products.add(product);

                // 프로모션 상품 체크
                if (hasPromotion(product)) {
                    promotionProducts.put(product.getName(), true);
                }
            }

            // 프로모션 상품에 대한 일반 재고 상품 추가
            addRegularProducts(products, promotionProducts);

        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 상품 파일을 불러오는데 실패했습니다.", e);
        }

        return products;
    }

    private static void skipHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private static Product createProduct(String[] parts) {
        return new Product(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                "null".equals(parts[3]) ? null : parts[3]
        );
    }

    private static boolean hasPromotion(Product product) {
        return product.getPromotion() != null && !product.getPromotion().equals("null");
    }

    private static void addRegularProducts(List<Product> products, Map<String, Boolean> promotionProducts) {
        List<Product> regularProducts = new ArrayList<>();

        for (String productName : promotionProducts.keySet()) {
            // 이미 일반 상품이 있는지 확인
            boolean hasRegularProduct = products.stream()
                    .anyMatch(p -> p.getName().equals(productName) && !hasPromotion(p));

            // 일반 상품이 없으면 재고 없음 상품 추가
            if (!hasRegularProduct) {
                Product originalProduct = products.stream()
                        .filter(p -> p.getName().equals(productName) && hasPromotion(p))
                        .findFirst()
                        .orElse(null);

                if (originalProduct != null) {
                    regularProducts.add(new Product(
                            originalProduct.getName(),
                            originalProduct.getPrice(),
                            0,  // 재고 0으로 설정
                            null
                    ));
                }
            }
        }

        products.addAll(regularProducts);
    }

    public static Map<String, Promotion> loadPromotions() {
        Map<String, Promotion> promotions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROMOTIONS_PATH))) {
            skipHeader(reader);
            String line;

            while ((line = reader.readLine()) != null) {
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