package service;

import entity.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private final OrderSerivce orderSerivce = new OrderSerivce();
    public List<OrderItem> AllOrderDelivered() {
        List<OrderItem> deliveredOrders = new ArrayList<>();
        List<OrderItem> allOrders  = orderSerivce.getOrderItemsFromJsonFile();
        for (OrderItem orderItem : allOrders) {
            if (orderItem.getStatus().equals("Đã giao hàng")) {
                deliveredOrders.add(orderItem);
            }
        }
        if (deliveredOrders.isEmpty()) {
            System.out.println("Chưa có đơn hàng nào đã giao thành công.");
        }
        return deliveredOrders;
    }
    //Thông kê doanh thu theo ngày
    public void displayDailyRevenue(String date) {
        double dailyRevenue = 0;

        // Lấy danh sách đơn hàng đã giao thành công từ file JSON
        List<OrderItem> allOrders = AllOrderDelivered();

        // Lặp qua từng đơn hàng để tính tổng doanh thu cho ngày đó
        for (OrderItem orderItem : allOrders) {
            // Kiểm tra xem đơn hàng được tạo vào ngày đang xét không
            if (orderItem.getOrderTime().startsWith(date)) {
                // Cộng vào tổng doanh thu
                dailyRevenue += orderItem.getIntoMoney();
            }
        }
        // Hiển thị thông tin doanh thu
        System.out.println("------------------------------------------");
        System.out.println("Doanh thu ngày " + date + ": " + dailyRevenue);
        System.out.println("------------------------------------------");
    }
    //Thông kê doanh thu theo tháng
    public void displayMonthlyRevenue(int month, int year) {
        double monthlyRevenue = 0;

        // Lấy danh sách đơn hàng đã giao thành công từ file JSON
        List<OrderItem> deliveredOrders = AllOrderDelivered();

        // Định dạng chuỗi ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Lặp qua từng đơn hàng để tính tổng doanh thu cho tháng đó
        for (OrderItem orderItem : deliveredOrders) {
            // Lấy ngày đặt hàng từ đơn hàng
            LocalDate orderDate = LocalDate.parse(orderItem.getOrderTime(), formatter);

            // Kiểm tra xem đơn hàng được đặt trong tháng và năm đang xét không
            if (orderDate.getYear() == year && orderDate.getMonthValue() == month) {
                // Cộng vào tổng doanh thu
                monthlyRevenue += orderItem.getIntoMoney();
            }
        }
        // Hiển thị thông tin doanh thu
        System.out.println("------------------------------------------");
        System.out.println("Doanh thu tháng " + month + "/" + year + ": " + monthlyRevenue);
        System.out.println("------------------------------------------");
    }
}
