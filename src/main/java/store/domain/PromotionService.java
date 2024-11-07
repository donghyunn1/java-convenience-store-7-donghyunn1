package store.domain;



public class PromotionService {

    public void applyPromotion(Product product, int purchaseQuantity) {

        if (product.getPromotion() != null) {
            String promotionName = product.getPromotion();

            // 탄산 2+1 프로모션 적용
            if (promotionName.equals("탄산2+1") && (purchaseQuantity == 2)) {
                System.out.println("현재 " + product.getName() + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            }

            // MD 추천상품, 반짝할인 1+1 프로모션 적용
            if ((promotionName.equals("MD추천상품") || promotionName.equals("반짝할인")) && (purchaseQuantity == 1)) {
                System.out.println("현재 " + product.getName() + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            }
        }
    }
}
