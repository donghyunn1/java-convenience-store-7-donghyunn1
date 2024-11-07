package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Promotions {

    List<Promotion> promotions = PromotionFileReader.readPromotions();

    public Promotions() {
        this.promotions = getPromotions();
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
}
