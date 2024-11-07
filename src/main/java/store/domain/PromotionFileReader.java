package store.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PromotionFileReader {
    private static final String PATH_PROMOTIONS_FILE = "src/main/resources/promotions.md";

    public static List<Promotion> readPromotions() {
        return readPromotions(PATH_PROMOTIONS_FILE);
    }

    public static List<Promotion> readPromotions(String filePath) {
        List<Promotion> promotions = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String promotionName = data[0].trim();
                int countOfPurchase = Integer.parseInt(data[1].trim());
                int countOfGet = Integer.parseInt(data[2].trim());
                LocalDateTime startDate = LocalDateTime.parse(data[3].trim(),dateFormat);
                LocalDateTime endDate = LocalDateTime.parse(data[4].trim(),dateFormat);

                Promotion promotion = new Promotion(promotionName, countOfPurchase, countOfGet, startDate, endDate);
                promotions.add(promotion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promotions;
    }
}
