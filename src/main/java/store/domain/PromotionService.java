package store.domain;


import camp.nextstep.edu.missionutils.Console;

public class PromotionService {

    public boolean applyPromotion(Product product, int purchaseQuantity) {
        String promotionName = product.getPromotion();
        if (promotionName.equals("탄산2+1") && (purchaseQuantity == 2)) {
            return askBonus(product);
        }
        if ((promotionName.equals("MD추천상품") || promotionName.equals("반짝할인")) && (purchaseQuantity == 1)) {
            return askBonus(product);
        }
        return false;
    }

    private boolean askBonus(Product product) {
        System.out.println("현재 " + product.getName() + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        String userInput = Console.readLine();
        return userInput.equals("Y");
    }
}
