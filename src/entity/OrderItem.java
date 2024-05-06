package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderItem {
    private User user;
    private List<Product> products;
    private double intoMoney;
    private LocalDateTime orderTime;
    private String status;
    public OrderItem(){}

    public OrderItem(User user, List<Product> products, double intoMoney, LocalDateTime orderTime, String status) {
        this.user = user;
        this.products = products;
        this.intoMoney = intoMoney;
        this.orderTime = orderTime;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(double intoMoney) {
        this.intoMoney = intoMoney;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "user=" + user +
                ", products=" + products +
                ", intoMoney=" + intoMoney +
                ", orderTime=" + orderTime +
                ", status='" + status + '\'' +
                '}';
    }
}
