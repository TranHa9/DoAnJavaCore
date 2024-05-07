package view;

import entity.User;
import service.OrderSerivce;
import service.UserService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private final UserService userService = new UserService();
    private final AdminMenu adminMenu = new AdminMenu();
    private final UserMenu userMenu = new UserMenu();
    private final OrderSerivce orderSerivce = new OrderSerivce();
    private final Scanner scanner = new Scanner(System.in);
    public void showMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("---------- BÁN SÁCH ONLINE -----------");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("3. Thoát");
            System.out.print("Xin mời chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = new Scanner(System.in).nextInt();
                    if (option < 1 || option > 3) {
                        System.out.println("Chức năng là số từ 1 tới 3, vui lòng nhập lại: ");
                        continue;
                    }
                    break;
                } catch (InputMismatchException ex) {
                    System.out.print("Lựa chọn phải là một số nguyên, vui lòng nhập lại: ");
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    User user = userService.login();
                    if(user != null) {
                        if (user.getRole().equals(User.Role.USER)) {
                            Main.loggedInUser = user;
                            userMenu.showMenuUser();
                            break;
                        } else if (user.getRole().equals(User.Role.ADMIN)){
                            adminMenu.showAdminMenu();
                            break;
                        }
                    }
                    break;
                case 2:
                    userService.register();
                    break;
                case 3:
                    isQuit = true;
                    return;

           }
        }
    }
}
