package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Order;
import entity.OrderItem;
import entity.User;
import view.Main;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderSerivce {
    private final String FILE_ORDER_ITEM = "orderItem.json";
    private final CartService cartService = new CartService();
    private final List<OrderItem> orderItems = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private static int AUTO_ID;

    public void generateOrderItemId() {
        List<OrderItem> orderItems = getOrderItemsFromJsonFile();
        int maxId = 0;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getId() > maxId) {
                maxId = orderItem.getId();
            }
        }
        AUTO_ID = maxId + 1;
    }

    private List<OrderItem> getOrderItemsFromJsonFile() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(FILE_ORDER_ITEM));
            List<OrderItem> orderItems = Arrays.asList(gson.fromJson(reader, OrderItem[].class));
            reader.close();
            return orderItems;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private int getOrderItemsIndex(List<OrderItem> orderItems, int orderItemId) {
        // Duyệt qua danh sách đơn hàng và tìm vị trí của đơn hàng theo id
        for (int i = 0; i < orderItems.size(); i++) {
            if (orderItems.get(i).getId() == orderItemId) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy sản phẩm
    }
    private void saveOrderItemToJsonFile(List<OrderItem> orderItems) {
        try  {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<OrderItem> existingOrderItems = new ArrayList<>(getOrderItemsFromJsonFile());
            // Duyệt qua danh sách sản phẩm mới
            for (OrderItem orderItem : orderItems) {
                // Kiểm tra xem sản phẩm đã tồn tại trong danh sách hiện có chưa
                int index = getOrderItemsIndex(existingOrderItems, orderItem.getId());
                if (index != -1) {
                    // Nếu đã tồn tại, cập nhật sản phẩm tại vị trí index
                    existingOrderItems.set(index, orderItem);
                } else {
                    // Nếu không tồn tại, thêm sản phẩm mới vào danh sách
                    existingOrderItems.add(orderItem);
                }
            }
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_ORDER_ITEM));
            gson.toJson(existingOrderItems, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OrderItem ceateOrder() {
        List<Order> cartItems = cartService.getOderFromJsonFile(); // Lấy danh sách các mặt hàng từ tệp JSON
        if (cartItems.isEmpty()) {
            System.out.println("Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi đặt hàng.");
            return null;
        }
        // Tạo một đối tượng Order mới để đại diện cho đơn hàng
        OrderItem newOrder = new OrderItem();
        generateOrderItemId();
        newOrder.setId(AUTO_ID);
        // Lấy ngày giờ hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Tạo định dạng cho ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        // Format ngày giờ theo định dạng
        String dateTime = currentDateTime.format(formatter);
        newOrder.setOrderTime(dateTime);
        // Lấy danh sách các sản phẩm từ các mặt hàng trong giỏ hàng và gán vào đơn hàng mới
        List<Order> orders = cartService.AllCartItems();
        newOrder.setOrders(orders);

        // Tính tổng tiền cho đơn hàng
        double totalPrice = 0;
        for (Order order : cartItems) {
            totalPrice += order.getPrice() * order.getQuantity();
        }
        newOrder.setIntoMoney(totalPrice);
        User user = new User(Main.loggedInUser.getId(), Main.loggedInUser.getEmail(), Main.loggedInUser.getName(), Main.loggedInUser.getPhone(), Main.loggedInUser.getAddress());
        newOrder.setUser(user);

        // Gán trạng thái đơn hàng
        newOrder.setStatus("Chờ xác nhận");

        return newOrder;
    }

    public void placeOrder() {
        // Tạo đơn hàng mới
        OrderItem newOrder = ceateOrder();
        if (newOrder == null) {
            // Nếu không có đơn hàng, không thực hiện gì cả
            return;
        }
        orderItems.add(newOrder);
        // Lưu danh sách đơn hàng vào tệp JSON
        saveOrderItemToJsonFile(orderItems);

        // Xóa tất cả các mặt hàng trong giỏ hàng sau khi đặt hàng
        List<Order> cartItems = cartService.getOderFromJsonFile();
        cartItems = new ArrayList<>();
        cartService.saveOrderToJsonFile(cartItems);
        System.out.println("Đặt hàng thành công. Đơn hàng của bạn đang chờ xác nhận.");
    }

    public void showOrder(OrderItem orderItem) {
        System.out.println("ID đơn hàng: " + orderItem.getId());
        System.out.println("Chi tiết đơn hàng:");
        System.out.printf("%-10s%-20s%-20s%-10s%-10s%n", "ID SP", "Tên sản phẩm", "Tác giả", "Số lượng", "Đơn giá");
        System.out.println("------------------------------------------------------------------");
        for (Order order : orderItem.getOrders()) {
            double totalPrice = order.getPrice() * order.getQuantity();
            System.out.printf("%-10s%-20s%-20s%-10s%-10s%n", order.getProductId(), order.getName(), order.getAuthor(), order.getQuantity(), order.getPrice());
        }
        System.out.println("--------------------------------------");
        System.out.println("Tổng thành tiền: " + orderItem.getIntoMoney());
        System.out.println("--------------------------------------");
        System.out.println("ID người dùng: " + orderItem.getUser().getId());
        System.out.println("Người đặt hàng: " + orderItem.getUser().getName());
        System.out.println("Email: " + orderItem.getUser().getEmail());
        System.out.println("Địa chỉ: " + orderItem.getUser().getAddress());
        System.out.println("Số điện thoại: " + orderItem.getUser().getPhone());
        System.out.println("Thời gian đặt hàng: " + orderItem.getOrderTime());
        System.out.println("Trạng thái đơn hàng: " + orderItem.getStatus());
        System.out.println("--------------------------------------");
    }
    public void showAllOrdersByUser() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        int userId = Main.loggedInUser.getId();
        boolean foundOrder = false;
        for (OrderItem orderItem : allOrders) {
            if (orderItem.getUser().getId() == userId) {
                showOrder(orderItem);
                foundOrder = true;
            }
        }
        if (!foundOrder) {
            System.out.println("Bạn chưa có đơn hàng nào.");
        }
    }

    public void showAllOrders() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        if (allOrders.isEmpty()) {
            System.out.println("Chưa có đơn hàng nào.");
        } else {
            for (OrderItem orderItem : allOrders) {
                if (!orderItem.getStatus().equals("Đã hủy")) {
                    showOrder(orderItem);
                }
            }
        }
    }
    public void destroyOrder() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        int userId = Main.loggedInUser.getId();
        System.out.println("Vui lòng nhập id đơn hàng:");
        int orderItemsId;
        while (true) {
            try {
                orderItemsId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        int index = getOrderItemsIndex(allOrders, orderItemsId);
        if (index == -1) {
            System.out.println("Không tìm thấy đơn hàng có ID: " + orderItemsId);
            return;
        }
        OrderItem orderItem = allOrders.get(index);
        if (orderItem.getUser().getId() != userId) {
            System.out.println("Bạn không có đơn hàng với Id: " + orderItemsId);
            return;
        }
        String newStatus = "Đã hủy";
        OrderItem orderToUpdate = allOrders.get(index);
        orderToUpdate.setStatus(newStatus);
        allOrders.set(index, orderToUpdate);
        saveOrderItemToJsonFile(allOrders);
        System.out.println("Đơn hàng đã được hủy.");
    }

    public void destroyOrderAdmin() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        System.out.println("Vui lòng nhập ID người dùng:");
        int userId;
        while (true) {
            try {
                userId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        System.out.println("Vui lòng nhập id đơn hàng:");
        int orderItemsId;
        while (true) {
            try {
                orderItemsId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        int index = -1;
        for (int i = 0; i < allOrders.size(); i++) {
            OrderItem orderItem = allOrders.get(i);
            if (orderItem.getUser().getId() == userId && orderItem.getId() == orderItemsId) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Không tìm thấy đơn hàng có ID: " + orderItemsId + "của người dùng có ID: " + userId);
            return;
        }
        String newStatus = "Đã hủy";
        OrderItem orderToUpdate = allOrders.get(index);
        orderToUpdate.setStatus(newStatus);
        allOrders.set(index, orderToUpdate);
        saveOrderItemToJsonFile(allOrders);
        System.out.println("Đơn hàng đã được hủy.");
    }
    public void confirmOrderAdmin() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        System.out.println("Vui lòng nhập ID người dùng:");
        int userId;
        while (true) {
            try {
                userId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        System.out.println("Vui lòng nhập id đơn hàng:");
        int orderItemsId;
        while (true) {
            try {
                orderItemsId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        int index = -1;
        for (int i = 0; i < allOrders.size(); i++) {
            OrderItem orderItem = allOrders.get(i);
            if (orderItem.getUser().getId() == userId && orderItem.getId() == orderItemsId) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Không tìm thấy đơn hàng có ID: " + orderItemsId + "của người dùng có ID: " + userId);
            return;
        }

        String newStatus = "Đã xác nhận";
        OrderItem orderToUpdate = allOrders.get(index);
        orderToUpdate.setStatus(newStatus);
        allOrders.set(index, orderToUpdate);
        saveOrderItemToJsonFile(allOrders);
        System.out.println("Đơn hàng đã được xác nhận.");
    }

    public void deliveringOrderAdmin() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        System.out.println("Vui lòng nhập ID người dùng:");
        int userId;
        while (true) {
            try {
                userId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        System.out.println("Vui lòng nhập id đơn hàng:");
        int orderItemsId;
        while (true) {
            try {
                orderItemsId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        int index = -1;
        for (int i = 0; i < allOrders.size(); i++) {
            OrderItem orderItem = allOrders.get(i);
            if (orderItem.getUser().getId() == userId && orderItem.getId() == orderItemsId) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Không tìm thấy đơn hàng có ID: " + orderItemsId + "của người dùng có ID: " + userId);
            return;
        }
        String newStatus = "Đang giao";
        OrderItem orderToUpdate = allOrders.get(index);
        orderToUpdate.setStatus(newStatus);
        allOrders.set(index, orderToUpdate);
        saveOrderItemToJsonFile(allOrders);
        System.out.println("Đơn hàng đang được giao.");
    }
    public void successOrderAdmin() {
        List<OrderItem> allOrders = getOrderItemsFromJsonFile();
        System.out.println("Vui lòng nhập ID người dùng:");
        int userId;
        while (true) {
            try {
                userId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        System.out.println("Vui lòng nhập id đơn hàng:");
        int orderItemsId;
        while (true) {
            try {
                orderItemsId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        int index = -1;
        for (int i = 0; i < allOrders.size(); i++) {
            OrderItem orderItem = allOrders.get(i);
            if (orderItem.getUser().getId() == userId && orderItem.getId() == orderItemsId) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Không tìm thấy đơn hàng có ID: " + orderItemsId + "của người dùng có ID: " + userId);
            return;
        }
        String newStatus = "Đã giao hàng";
        OrderItem orderToUpdate = allOrders.get(index);
        orderToUpdate.setStatus(newStatus);
        allOrders.set(index, orderToUpdate);
        saveOrderItemToJsonFile(allOrders);
        System.out.println("Đơn hàng đã giao thành công.");
    }
}

