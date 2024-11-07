package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Promotion {

    private String promotionName;
    private int requiredQuantity;
    private int countOfBonus;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String promotionName, int requiredQuantity, int countOfBonus, LocalDate startDate, LocalDate endDate) {
        this.promotionName = promotionName;
        this.requiredQuantity = requiredQuantity;
        this.countOfBonus = countOfBonus;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public boolean isActive(LocalDate currentDate) {
        return startDate.isBefore(currentDate) && endDate.isAfter(currentDate);
    }
}
