package store.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ProductFileReader {
    private static final String PATH_PRODUCTS_FILE = "src/main/resources/products.md";

    public static List<Product> readProducts() {
        return readProducts(PATH_PRODUCTS_FILE);
    }

    public static List<Product> readProducts(String filename) {
        try {
            Stream<String> lines = Files.lines(Paths.get(filename));
            return lines.skip(1)
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .map(parts -> new Product(parts[0].trim(), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()),
                            parsePromotion(parts[3].trim())))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private static Promotion parsePromotion(String promotionName) {
        if (promotionName.isEmpty() || promotionName.equals("null")) {
            return null;
        }
        return new Promotion(promotionName);
    }
}
