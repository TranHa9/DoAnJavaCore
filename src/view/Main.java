package view;

import entity.Product;
import entity.User;
import service.CartService;
import service.ProductService;
import service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//      Menu menu = new Menu();
//      menu.showMenu();
        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        //productService.addProductNew();
        //productService.updateProduct();
        //productService.deleteProduct();
       //productService.showProductAll();
        //cartService.addCartItem();
        UserMenu userMenu = new UserMenu();
        userMenu.showMenuUser();
    }
}