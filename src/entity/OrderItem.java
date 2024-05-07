package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderItem {
    private int id;
    private User user;
    private List<Order> orders;
    private String orderTime;
    private double intoMoney;
    private String status;
    public OrderItem(){}

    public OrderItem(int id, User user, List<Order> orders, String orderTime, double intoMoney, String status) {
        this.id = id;
        this.user = user;
        this.orders = orders;
        this.orderTime = orderTime;
        this.intoMoney = intoMoney;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(double intoMoney) {
        this.intoMoney = intoMoney;
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
                "id=" + id +
                ", user=" + user +
                ", orders=" + orders +
                ", orderTime='" + orderTime + '\'' +
                ", intoMoney=" + intoMoney +
                ", status='" + status + '\'' +
                '}';
    }
}
