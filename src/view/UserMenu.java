package view;

import entity.OrderItem;
import service.CartService;
import service.OrderSerivce;
import service.ProductService;
import service.UserService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserMenu {
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final OrderSerivce orderSerivce = new OrderSerivce();
    private final UserService userService = new UserService();
    private final Scanner scanner = new Scanner(System.in);
    public  void showMenuUser() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("*********************************************");
            System.out.println("*       CHÀO MỪNG BẠN ĐẾN VỚI SHOP SÁCH     *");
            System.out.println("*********************************************");
            System.out.println("1. Xem tất cả sản phẩm");
            System.out.println("2. Tìm kiếm sản phẩm theo tên");
            System.out.println("3. Danh mục các thể loại");
            System.out.println("4. Giỏ hàng");
            System.out.println("5. Thông tin cá nhân");
            System.out.println("6. Đăng xuất");
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
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    productService.showProductAll();
                    showProductMenu();
                    break;
                case 2:
                    productService.searchProductByName();
                    showProductMenu();
                    break;
                case 3:
                    productService.displayCategories();
                    showCategoriesMenu();
                    break;
                case 4:
                    System.out.println("*****************************************");
                    System.out.println("*                GIỎ HÀNG               *");
                    System.out.println("*****************************************");
                    cartService.displayCartItems();
                    showCartMenu();
                    break;
                case 5:
                    userService.updateUser();
                    break;
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
                    scanner.nextLine();
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
    public void showCategoriesMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println();
            System.out.print("1. Tìm kiếm theo thể loại\t");
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
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    productService.searchProductByCategory();
                    showProductMenu();
                    break;
                case 2:
                    isQuit = true;
                    return;

            }
        }
    }
    public void showCartMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println();
            System.out.println("1. Thêm sản phẩm vào giỏ hàng");
            System.out.println("2. Xoá sản phẩm trong giỏ hàng");
            System.out.println("3. Cập nhật số lượng sản phẩm");
            System.out.println("4. Đặt hàng");
            System.out.println("5. Đơn hàng của bạn:");
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
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    cartService.addCartItem();
                    cartService.displayCartItems();
                    break;
                case 2:
                    cartService.removeCartItem();
                    cartService.displayCartItems();
                    break;
                case 3:
                    cartService.updateQuantity();
                    cartService.displayCartItems();
                    break;
                case 4:
                    OrderItem newOrder = orderSerivce.ceateOrder();
                    if (newOrder != null) {
                        orderSerivce.showOrder(newOrder);
                        showConfirmOrder();
                    } else {
                        System.out.println("Không thể tạo đơn hàng. Vui lòng thử lại sau.");
                    }
                    break;
                case 5:
                    System.out.println("*************************************************");
                    System.out.println("*                 ĐƠN HÀNG CỦA BẠN              *");
                    System.out.println("*************************************************");
                    orderSerivce.showAllOrdersByUser();
                    showMyOrderMenu();
                    break;
                case 6:
                    isQuit = true;
                    return;

            }
        }
    }
    public void showConfirmOrder() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println();
            System.out.print("1. Xác nhận\t");
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
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    orderSerivce.placeOrder();
                    break;
                case 2:
                    isQuit = true;
                    return;

            }
        }
    }
    public void showMyOrderMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println();
            System.out.print("1. Hủy đơn hàng\t");
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
                    scanner.nextLine();
                }
            }
            switch (option) {
                case 1:
                    orderSerivce.destroyOrder();
                    break;
                case 2:
                    isQuit = true;
                    return;

            }
        }
    }
}
