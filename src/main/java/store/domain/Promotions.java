package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Promotions {

    List<Promotion> promotions = new ArrayList<>();

    public Promotions() {
        this.promotions = PromotionFileReader.readPromotions();
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }


}
