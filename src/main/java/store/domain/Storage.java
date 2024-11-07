package store.domain;

public class Storage {

    public void updateStorage(Product product, int purchaseQuantity, boolean applyBonus) {
        int updatedQuantity = product.getQuantity() - purchaseQuantity;

        if (applyBonus) {
            updatedQuantity -= 1;
        }

        product.setQuantity(updatedQuantity);
    }
}
