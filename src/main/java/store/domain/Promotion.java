package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDateTime;
import java.util.Date;

public class Promotion {

    private String promotionName;
    private int countOfPurchase;
    private int countOfGet;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Promotion(String promotionName, int countOfPurchase, int countOfGet, LocalDateTime startDate, LocalDateTime endDate) {
        this.promotionName = promotionName;
        this.countOfPurchase = countOfPurchase;
        this.countOfGet = countOfGet;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Promotion(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public boolean isPromotionActive() {
        return startDate.isBefore(DateTimes.now()) && endDate.isAfter(DateTimes.now());
    }
}
