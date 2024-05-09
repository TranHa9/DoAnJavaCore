package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Cart;
import entity.Order;
import entity.OrderItem;
import entity.Product;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CartService {
    private final Scanner scanner = new Scanner(System.in);
    private final String FILE_PRODUCT = "products.json";
    private final String FILE_ORDER = "order.json";
    private final Cart cart = new Cart();
    private final List<Order> orders = new ArrayList<>();
    private final ProductService productService = new ProductService();
    private static int AUTO_ID;

    public CartService() {
        cart.setCartItems(new ArrayList<>());
    }
    public void generateOrderId() {
        List<Order> orders = getOderFromJsonFile();
        int maxId = 0;
        for (Order order : orders) {
            if (order.getId() > maxId) {
                maxId = order.getId();
            }
        }
        AUTO_ID = maxId + 1;
    }

    public void printHeader(){
        System.out.printf("%-5s%-20s%-20s%-20s%-20s%-30s%-10s%n", "Id", "Tên", "Tác giả", "Thể loại","Giá bán","Mô tả","Số lượng");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void showBookDetail(Product product) {
        System.out.printf("%-5s%-20s%-20s%-20s%-20s%-30s%-10s%n",product.getId(),product.getName(),product.getAuthor(),product.getCategory(),
                product.getPrice(),product.getDescription(),product.getQuantity());
    }

    public void showBook(Product product) {
        printHeader();
        showBookDetail(product);
    }
    private List<Product> getProductsFromJsonFile() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(FILE_PRODUCT));
            List<Product> products = Arrays.asList(gson.fromJson(reader, Product[].class));
            reader.close();
            return products;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Order> getOderFromJsonFile() {
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
    //Kiểm tra sản phẩm có tồn tại trong giỏ
    private int getOderIndex(List<Order> orders, int getProductId) {
        // Duyệt qua danh sách giỏ hành và tìm vị trí của sản phẩm theo id
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getProductId() == getProductId) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy sản phẩm
    }
    private void saveOrderToJsonFile() {
        try  {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Order> existingOrders = new ArrayList<>(getOderFromJsonFile());
            // Duyệt qua danh sách sản phẩm mới trong giỏ
                for (Order order : orders) {
                // Kiểm tra xem sản phẩm trong giỏ đã tồn tại chưa
                int index = getOderIndex(existingOrders, order.getProductId());
                if (index != -1) {
                    // Nếu đã tồn tại, cập nhật sản phẩm tại vị trí index
                    existingOrders.set(index, order);
                } else {
                    // Nếu không tồn tại, thêm sản phẩm mới sản phẩm vào giỏ
                    existingOrders.add(order);
                }
            }
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_ORDER));
            gson.toJson(existingOrders, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Product findProductById(int productId) {
        List<Product> products = getProductsFromJsonFile();
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
    public Order findProductById1(int productId) {
        List<Order> orders = getOderFromJsonFile();
        for (Order order : orders) {
            if (order.getProductId() == productId) {
                return order;
            }
        }
        return null;
    }
    //Kiểm tra số lượng sản phẩm muốn mua có vượt quá số lượng kho
    public boolean checkQuantity(Product product, int quantity) {
        return product != null && product.getQuantity() >= quantity;
    }

    //Thêm sản phẩm vào giỏ hàng
    public void addCartItem() {
        System.out.println("Vui lòng nhập id sản phẩm:");
        int productId;
        while (true) {
            try {
                productId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Thông tin không chính xác , vui lòng nhập lại id sản phẩm: ");
           return;
        }
        Order order = findProductById1(productId);
        if (order != null) {
            System.out.println("Sản phẩm đã có trong giỏ hàng: ");
            return;
        }
        System.out.println("Vui lòng nhập số lượng sản phẩm:");
        int quantity;
        while (true) {
            try {
                quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity <= 0) {
                    System.out.println("Số lượng sách phải là số dương và lớn hơn 0, vui lòng nhập lại ");
                    continue;
                }
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }

        if (!checkQuantity(product, quantity)) {
            System.out.println("Số lượng sản phẩm trong kho không đủ.");
            return;
        }
        // Trừ số lượng sản phẩm trong kho sau khi kiểm tra số lượng sản phẩm trong kho đủ
        int newQuantity = product.getQuantity() - quantity;
        // Cập nhật sản phẩm trong file JSON
        productService.updateQuantityProduct(product, newQuantity);

        generateOrderId();
        // Nếu không tìm thấy sản phẩm trong giỏ hàng, thêm sản phẩm mới vào giỏ hàng
        cart.addItem(new Order(AUTO_ID, productId, product.getName(), product.getAuthor(), quantity, product.getPrice()));

        orders.clear();
        orders.addAll(cart.getCartItems());
        saveOrderToJsonFile();
        System.out.println("Bạn đã thêm vào giỏ hàng thành công.");
    }

    public void updateQuantity() {
        System.out.println("Vui lòng nhập id sản phẩm:");
        int productId;
        while (true) {
            try {
                productId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        Product product = findProductById(productId);
        Order order = findProductById1(productId);
        if (order == null) {
            System.out.println("Sản phẩm đã chưa có trong giỏ hàng: ");
            return;
        }
        System.out.println("Vui lòng nhập số lượng sản phẩm:");
        int quantity;
        while (true) {
            try {
                quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity <= 0) {
                    System.out.println("Số lượng sách phải là số dương và lớn hơn 0, vui lòng nhập lại ");
                    continue;
                }
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }

        if (!checkQuantity(product, quantity)) {
            System.out.println("Số lượng sản phẩm trong kho không đủ.");
            return;
        }

        // Trừ số lượng sản phẩm trong kho sau khi kiểm tra số lượng sản phẩm trong kho đủ
        int newQuantity = (product.getQuantity() + order.getQuantity()) - quantity;
        // Cập nhật sản phẩm trong file JSON
        productService.updateQuantityProduct(product, newQuantity);

        cart.addItem(new Order(order.getId(), productId, product.getName(), product.getAuthor(), quantity, product.getPrice()));
        // Cập nhật danh sách đơn hàng
        orders.clear();
        orders.addAll(cart.getCartItems());
        saveOrderToJsonFile();
        System.out.println("Bạn đã thêm vào giỏ hàng thành công.");
    }

    //Hiển thị các sản phẩm trong giỏ hàng
    public void displayCartItems() {
        List<Order> cartItems = getOderFromJsonFile(); // Lấy danh sách các mặt hàng từ tệp JSON
        if (cartItems.isEmpty()) {
            System.out.println("Giỏ hàng của bạn đang trống.");
        } else {
            System.out.println("Các sản phẩm trong giỏ hàng:");
            System.out.printf("%-5s%-10s%-20s%-20s%-15s%-10s%-20s%n", "Id","Id Sp", "Tên", "Tác giả",  "Giá bán", "Số lượng", "Thành tiền");
            System.out.println("--------------------------------------------------------------------------------------");
            double total = 0;
            for (Order order : cartItems) {
                double subtotal = order.getQuantity() * order.getPrice(); // Tính thành tiền của mỗi sản phẩm
                total += subtotal; // Tổng thành tiền của tất cả sản phẩm trong giỏ hàng
                System.out.printf("%-5s%-10s%-20s%-20s%-15s%-10s%-20s%n", order.getId(),order.getProductId(), order.getName(), order.getAuthor(),
                        order.getPrice(), order.getQuantity(), subtotal);
            }
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Tổng tiền: " + total);
        }
    }
    public List<Order>  AllCartItems() {
        List<Order> cartItems = getOderFromJsonFile(); // Lấy danh sách các mặt hàng từ tệp JSON
        if (cartItems.isEmpty()) {
            System.out.println("Giỏ hàng của bạn đang trống.");
        }
        return cartItems;
    }
    public void saveOrderToJsonFile(List<Order> orders) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_ORDER));
            gson.toJson(orders, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Xóa sản phẩm trong giỏ hàng
    public void removeCartItem() {
        List<Order> cartItems = new ArrayList<>(getOderFromJsonFile()); // Lấy danh sách các sản phẩm từ tệp JSON
        System.out.println("Vui lòng nhập id sản phẩm cần xóa trong giỏ:");
        int productId;
        while (true) {
            try {
                productId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        // Kiểm tra xem sản phẩm cần xóa có tồn tại trong danh sách không
        int index = getOderIndex(cartItems, productId);
        if (index != -1) {
            // Nếu sản phẩm tồn tại, xóa sản phẩm khỏi danh sách
            cartItems.remove(index);
            // Ghi danh sách đã chỉnh sửa vào file JSON
            saveOrderToJsonFile(cartItems);
            System.out.println("Đã xóa sản phẩm thành công.");
        }else {
            System.out.println("Không tìm thấy sản phẩm trong giỏ hàng có ID là " + productId);
        }
    }
}
