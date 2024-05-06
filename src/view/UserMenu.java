package view;

import service.CartService;
import service.ProductService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserMenu {
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    public  void showMenuUser() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("*****************************************");
            System.out.println("*       CHÀO MỪNG BẠN ĐẾN VỚI SHOP SÁCH     *");
            System.out.println("*****************************************");
            System.out.println("1. Xem tất cả sản phẩm");
            System.out.println("2. Tìm kiếm sản phẩm theo tên");
            System.out.println("3. Tìm kiếm theo thể loại");
            System.out.println("4. Giỏ hàng");
            System.out.println("5. Thông tin cá nhân");
            System.out.println("6. Quay lại");
            System.out.print("Mời bạn chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = new Scanner(System.in).nextInt();
                    if (option < 1 || option > 6) {
                        System.out.println("Chức năng là số từ 1 tới 6, vui lòng nhập lại: ");
                        continue;
                    }
                    break;
                } catch (InputMismatchException ex) {
                    System.out.print("Lựa chọn phải là một số nguyên, vui lòng nhập lại: ");
                }
            }
            switch (option) {
                case 1:
                    productService.showProductAll();
                    showProductMenu();
                    break;
//                case 2:
//                    showProductManagementMenu();
//                    break;
//                case 3:
//                    showOrderManagementMenu();
//                    break;
//                case 4:
//                    showSalesReport();
//                    break;
//                case 5:
//                    showUserManagementMenu();
//                    break;
                case 6:
                    isQuit = true;
                    return;
            }
        }
    }
    public void showProductMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println();
            System.out.print("1. Thêm vào sản phẩm vào giỏ hàng\t");
            System.out.println("2. Quay lai");
            System.out.print("Mời bạn chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = new Scanner(System.in).nextInt();
                    if (option < 1 || option > 2) {
                        System.out.println("Chức năng là số từ 1 tới 2, vui lòng nhập lại: ");
                        continue;
                    }
                    break;
                } catch (InputMismatchException ex) {
                    System.out.print("Lựa chọn phải là một số nguyên, vui lòng nhập lại: ");
                }
            }
            switch (option) {
                case 1:
                    cartService.addCartItem();
                    break;
                case 2:
                    isQuit = true;
                    return;

            }
        }
    }
}
