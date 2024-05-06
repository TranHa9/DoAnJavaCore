package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Cart;
import entity.Order;
import entity.Product;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CartService {
    private final Scanner scanner = new Scanner(System.in);
    //private final List<Product> products = new ArrayList<>();
    private final String FILE_PRODUCT = "products.json";
    private final String FILE_ORDER = "order.json";
    private final Cart cart = new Cart();
    private final List<Order> orders = new ArrayList<>();
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

    private List<Order> getOderFromJsonFile() {
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
                    existingOrders.addAll(orders);
                }
            }

            //existingOrders.addAll(orders);
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

    public boolean checkQuantity(Product product, int quantity) {
        return product != null && product.getQuantity() >= quantity;
    }
    public void addCartItem() {
        System.out.println("Vui lòng nhập id sản phẩm bạn muốn mua:");
        int productId;
        while (true) {
            try {
                productId = scanner.nextInt();
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
            }
        }
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("Thông tin không chính xác , vui lòng nhập lại id sản phẩm: ");
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
            }
        }

        if (!checkQuantity(product, quantity)) {
            System.out.println("Số lượng sản phẩm không đủ.");
            return;
        }
        // Kiểm tra xem giỏ hàng có chứa sản phẩm này không
        boolean found = false;
        List<Order> existingOrders = new ArrayList<>(getOderFromJsonFile());
        for (Order order : existingOrders) {
            if (order.getProductId() == productId) {
                //order.setQuantity(order.getQuantity() + quantity); // Cập nhật số lượng sản phẩm trong giỏ hàng
                cart.addItem(new Order(order.getId(), productId, product.getName(), product.getAuthor(), quantity, product.getPrice()));
                found = true;
            }
        }
        if (!found) {
            generateOrderId();
            // Nếu không tìm thấy sản phẩm trong giỏ hàng, thêm sản phẩm mới vào giỏ hàng
            cart.addItem(new Order(AUTO_ID, productId, product.getName(), product.getAuthor(), quantity, product.getPrice()));
        }
        // Cập nhật danh sách đơn hàng
        orders.clear();
        orders.addAll(cart.getCartItems());
        saveOrderToJsonFile();
        System.out.println("Bạn đã thêm vào giỏ hàng thành công.");
    }
}
