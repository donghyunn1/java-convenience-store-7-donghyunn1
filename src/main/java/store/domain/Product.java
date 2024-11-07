package store.domain;

public class Product {

    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
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

    public String getPromotion() {
        return promotion;
    }

    @Override
    public String toString() {
        if (this.promotion.equals("null")) {
            this.promotion = "";
        }
        return "- " + this.name + " " + this.price + "원 " + this.quantity + "개 " + this.promotion;
    }
}
