package store.domain;

public class Product {

    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    @Override
    public String toString() {
        String promotionName = "";
        if (promotion != null) {
            promotionName = promotion.getPromotionName();
        }
            return "- " + this.name + " " + this.price + "원 " + this.quantity + "개 " + promotionName;
    }
}
