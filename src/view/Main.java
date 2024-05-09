package view;

import entity.User;

public class Main {
    public static User loggedInUser;
    public static void main(String[] args) {
      Menu menu = new Menu();
      menu.showMenu();
    }

}