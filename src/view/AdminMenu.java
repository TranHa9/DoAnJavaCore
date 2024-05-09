package view;

import service.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminMenu {
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();
    private final OrderSerivce orderSerivce = new OrderSerivce();
    private final ReportService reportService = new ReportService();
    private final Scanner scanner = new Scanner(System.in);
    public void showAdminMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("----------MENU ADMIN MANAGEMENT -----------");
            System.out.println("1. Xem tất các sản phẩm:");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Quản lý đơn hàng");
            System.out.println("4. Quản lý người dùng");
            System.out.println("5. Báo cao doanh thu");
            System.out.println("6. Đăng xuất");
            System.out.print("Chọn chức năng: ");
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
                    break;
                case 2:
                    System.out.println("*****************************************");
                    System.out.println("*             QUẢN LÝ SẢN PHẨM          *");
                    System.out.println("*****************************************");
                    productService.showProductAll();
                    showProductManagementMenu();
                    break;
                case 3:
                    orderSerivce.showAllOrders();
                    showOrderManagementMenu();
                    break;
                case 4:
                    System.out.println("*******************************************");
                    System.out.println("*             QUẢN LÝ NGƯỜI DÙNG          *");
                    System.out.println("*******************************************");
                    userService.showAllUser();
                    showUserManagementMenu();
                    break;
                case 5:
                    showSalesReport();
                    break;
                case 6:
                    isQuit = true;
                    return;

            }
        }
    }

    public void showProductManagementMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Sửa sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Tìm kiếm sản phẩm theo số lượng:");
            System.out.println("5. Quay lai");
            System.out.print("Mời bạn chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    if (option < 1 || option > 5) {
                        System.out.println("Chức năng là số từ 1 tới 5, vui lòng nhập lại: ");
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
                    productService.addProductNew();
                    productService.showProductAll();
                    break;
                case 2:
                    productService.updateProduct();
                    productService.showProductAll();
                    break;
                case 3:
                    productService.deleteProduct();
                    productService.showProductAll();
                    break;
                case 4:
                    productService.searchProductByQuantity();
                    return;
                case 5:
                    isQuit = true;
                    return;
            }
        }
    }
    private void showOrderManagementMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("*****************************************");
            System.out.println("*             QUẢN LÝ ĐƠN HÀNG          *");
            System.out.println("*****************************************");
            System.out.println("1. Xác nhận");
            System.out.println("2. Đang giao");
            System.out.println("3. Đã giao hàng");
            System.out.println("4. Đã huỷ");
            System.out.println("5. Quay lại");
            System.out.print("Mời bạn chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = new Scanner(System.in).nextInt();
                    if (option < 1 || option > 5) {
                        System.out.println("Chức năng là số từ 1 tới 5, vui lòng nhập lại: ");
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
                    orderSerivce.confirmOrderAdmin();
                    orderSerivce.showAllOrders();
                    break;
                case 2:
                    orderSerivce.deliveringOrderAdmin();
                    orderSerivce.showAllOrders();
                    break;
                case 3:
                    orderSerivce.successOrderAdmin();
                    orderSerivce.showAllOrders();
                    break;
                case 4:
                    orderSerivce.destroyOrderAdmin();
                    orderSerivce.showAllOrders();
                    break;
                case 5:
                    isQuit = true;
                    return;
            }
        }
    }
    public void showUserManagementMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("1. Thêm sản người dùng");
            System.out.println("2. Sửa sản người dùng");
            System.out.println("3. Xóa sản người dùng");
            System.out.println("4. Quay lai");
            System.out.print("Mời bạn chọn chức năng: ");
            while (!isQuit) {
                try {
                    option = new Scanner(System.in).nextInt();
                    if (option < 1 || option > 4) {
                        System.out.println("Chức năng là số từ 1 tới 4, vui lòng nhập lại: ");
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
                    userService.register();
                    userService.showAllUser();
                    break;
                case 2:
                    userService.editUserAdmin();
                    break;
                case 3:
                    userService.deleteUserAdmin();
                    userService.showAllUser();
                    break;
                case 4:
                    isQuit = true;
                    return;

            }
        }
    }

    public void showSalesReport() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("1. Xem doanh thu theo ngày:");
            System.out.println("2. Xem doanh thu theo tháng:");
            System.out.println("3. Quay lai");
            System.out.print("Mời bạn chọn chức năng: ");
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
                    System.out.println("Vui lòng nhập ngày (ví dụ: 09/05/2024):");
                    String dateStr = scanner.nextLine();
                    reportService.displayDailyRevenue(dateStr);
                    break;
                case 2:
                    System.out.println("Vui lòng nhập tháng (ví dụ: 05):");
                    int month = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Vui lòng nhập năm (ví dụ: 2024):");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    reportService.displayMonthlyRevenue(month,year);
                    break;
                case 3:
                    isQuit = true;
                    return;

            }
        }
    }
}
