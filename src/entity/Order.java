package entity;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int productId;
    private String name;
    private String Author;
    private int quantity;
    private double price;
    public Order(){}
    public Order(int id, int productId, String name, String author, int quantity, double price) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        Author = author;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", Author='" + Author + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
