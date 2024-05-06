package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Order;
import entity.Product;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderSerivce {
    private final String FILE_ORDER = "orders.json";
    private List<Order> getOrdersFromJsonFile() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(FILE_ORDER));
            List<Order> orders = Arrays.asList(gson.fromJson(reader, Order[].class));
            reader.close();
            return orders;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private int getProductIndex(List<Product> products, int productId) {
        // Duyệt qua danh sách sản phẩm và tìm vị trí của sản phẩm theo id
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy sản phẩm
    }
//    private void saveProductsToJsonFile() {
//        try  {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            List<Product> existingProducts = new ArrayList<>(getProductsFromJsonFile());
//            // Duyệt qua danh sách sản phẩm mới
//            for (Product product : products) {
//                // Kiểm tra xem sản phẩm đã tồn tại trong danh sách hiện có chưa
//                int index = getProductIndex(existingProducts, product.getId());
//                if (index != -1) {
//                    // Nếu đã tồn tại, cập nhật sản phẩm tại vị trí index
//                    existingProducts.set(index, product);
//                } else {
//                    // Nếu không tồn tại, thêm sản phẩm mới vào danh sách
//                    existingProducts.addAll(products);
//                }
//            }
//            Writer writer = Files.newBufferedWriter(Paths.get(FILE_PRODUCT));
//            gson.toJson(existingProducts, writer);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void displayAllOrders() {
        List<Order> allOrders = getOrdersFromJsonFile();
        if (allOrders.isEmpty()) {
            System.out.println("Không có đơn hàng nào.");
        } else {
            System.out.println("Danh sách đơn hàng:");
            for (Order order : allOrders) {
                System.out.println(order.toString());
            }
        }
    }
//    public void showHistoryOrder() {
//        getOrdersAllFormFile();
//        try {
//            if (ordersAll.isEmpty()) {
//                System.out.println("không có đơn hàng được đặt.");
//            }
//            for (int i = 0; i < ordersAll.size(); i++) {
//                System.out.println("--------------------------------------------");
//                System.out.println((i + 1) + ". Đơn hàng thứ " + (i + 1));
//                showOder(ordersAll.get(i));
//            }
//            System.out.println();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
}
