package entity;

import java.util.List;

public class Cart {
    private List<Order> cartItems;
    public Cart(){}
    public Cart(List<Order> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Order> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Order> cartItems) {
        this.cartItems = cartItems;
    }
    public void addItem(Order order){
        cartItems.add(order);
    }
    @Override
    public String toString() {
        return "Cart{" +
                "cartItems=" + cartItems +
                '}';
    }
}
