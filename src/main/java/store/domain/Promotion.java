package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDateTime;
import java.util.Date;

public class Promotion {

    private String promotionName;
    private int requiredQuantity;
    private int countOfBonus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Promotion(String promotionName, int requiredQuantity, int countOfBonus, LocalDateTime startDate, LocalDateTime endDate) {
        this.promotionName = promotionName;
        this.requiredQuantity = requiredQuantity;
        this.countOfBonus = countOfBonus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Promotion(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getCountOfBonus() {
        return countOfBonus;
    }

    public boolean isActive() {
        return startDate.isBefore(DateTimes.now()) && endDate.isAfter(DateTimes.now());
    }
}
