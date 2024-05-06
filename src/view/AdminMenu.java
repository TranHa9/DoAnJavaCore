package view;

import service.ProductService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminMenu {
    private final ProductService productService = new ProductService();
    public void showAdminMenu() {
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("----------MENU ADMIN MANAGEMENT -----------");
            System.out.println("1. Xem tất các sản phẩm:");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Quản lý đơn hàng");
            System.out.println("4. Báo cáo doanh thu");
            System.out.println("5. Quản lý người dùng");
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
                }
            }
            switch (option) {
//                case 1:
//                    showAllProduct();
//                    break;
                case 2:
                    showProductManagementMenu();
                    break;
                case 3:
                    showOrderManagementMenu();
                    break;
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

    private void showProductManagementMenu(){
        int option = 0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("*****************************************");
            System.out.println("*            QUẢN LÝ ĐƠN HÀNG           *");
            System.out.println("*****************************************");
            System.out.println("1. Xem danh sách đơn hàng");
            System.out.println("2. Cập nhật trang thái đơn hàng");
            System.out.println("3. Quay lại");
            System.out.print("Mời bạn chọn chức năng: ");

            switch (option) {
//                case Contant.SHOW_ORDER:
//                    orderService.showHistoryOrder();
//                    break;
//                case Contant.UPDATE_STATUS_ORDER:
//                    orderService.showHistoryOrder();
//                    Order order = orderService.chooseOrder(orderService.getOrdersAll());
//                    menuOrderStatus(order);
//                    break;
//                case Contant.BACK_2:
//                isQuit = true;
//                    break;
            }
        }
    }
    public void showMenuOrderStatus() {
        int option=0;
        boolean isQuit = false;
        while (!isQuit) {
            System.out.println("*****************************************");
            System.out.println("*             QUẢN LÝ SẢN PHẨM          *");
            System.out.println("*****************************************");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Sửa sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Quay lại");
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
                }
            }

            switch (option) {
                case 1:
                    productService.addProductNew();
                    break;
                case 2:
                    productService.updateProduct();
                    break;
                case 3:
                    productService.deleteProduct();
                    break;
                case 4:
                    isQuit = true;
                    break;
            }
        }
    }
    private void showOrderManagementMenu() {
        int choose;
        while (true){
            System.out.println("*****************************************");
            System.out.println("*             QUẢN LÝ ĐƠN HÀNG          *");
            System.out.println("*****************************************");
            System.out.println("1. Đã xác nhận");
            System.out.println("2. Đang giao");
            System.out.println("4. Giao thành công");
            System.out.println("5. Đã huỷ");
            System.out.println("6. Quay lại");
            System.out.print("Mời bạn chọn chức năng: ");
            //choose = IOUtil.intNumberInteger(1, 6, "Vui lòng nhập lại: ");

//            switch (choose) {
//                case Contant.APPROVE_ORDER:
//                    orderService.setOrderApproved(order);
//                    break;
//                case Contant.PREPARING_ORDER:
//                    orderService.setOrderPreparing(order);
//                    break;
//                case Contant.DELIVERING_ORDER:
//                    orderService.setOrderDelivering(order);
//                    break;
//                case Contant.RECEIVED_ORDER:
//                    orderService.setOrderReceived(order);
//                    break;
//                case Contant.CANCELED_ORDER:
//                    orderService.setOrderCancel(order);
//                    break;
//                case Contant.BACK_3:
//                    break;
//            }
        }
    }
}
