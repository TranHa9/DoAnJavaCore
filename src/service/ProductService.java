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
import java.util.*;

public class ProductService {
    private final String FILE_PRODUCT = "products.json";
    private final List<Product> products = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private static int AUTO_ID;

    public void generateUserId() {
        List<Product> products = getProductsFromJsonFile();
        int maxId = 0;
        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }
        AUTO_ID = maxId + 1;
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

    private int getProductIndex(List<Product> products, int productId) {
        // Duyệt qua danh sách sản phẩm và tìm vị trí của sản phẩm theo id
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy sản phẩm
    }
    private void saveProductsToJsonFile() {
        try  {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Product> existingProducts = new ArrayList<>(getProductsFromJsonFile());
            // Duyệt qua danh sách sản phẩm mới
            for (Product product : products) {
                // Kiểm tra xem sản phẩm đã tồn tại trong danh sách hiện có chưa
                int index = getProductIndex(existingProducts, product.getId());
                if (index != -1) {
                    // Nếu đã tồn tại, cập nhật sản phẩm tại vị trí index
                    existingProducts.set(index, product);
                } else {
                    // Nếu không tồn tại, thêm sản phẩm mới vào danh sách
                    existingProducts.add(product);
                }
            }
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_PRODUCT));
            gson.toJson(existingProducts, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void showBooks(List<Product> productsAll) {
        printHeader();
        for (Product product : productsAll) {
            showBookDetail(product);
        }
    }

    public void addProductNew(){
        System.out.println("Vui lòng nhập tên sách");
        String name = scanner.nextLine();
        System.out.println("Vui lòng nhập thể loại:");
        String category = scanner.nextLine();
        System.out.println("Vui lòng nhập tên tác giả:");
        String author = scanner.nextLine();
        System.out.println("Vui lòng nhập giá bán:");
        double price;
        while (true) {
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                if (price < 0) {
                    System.out.println("Giá 1 quyển sách phải là số dương , vui lòng nhập lại ");
                    continue;
                }
                break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
            } catch (InputMismatchException e) {
                System.out.println("Giá trị bạn vừa nhập không phải là một số tự nhiên . Vui lòng nhập lại.");
                scanner.nextLine();
            }
        }
        System.out.println("Mô tả về sách:");
        String description = scanner.nextLine();
        System.out.println("Vui lòng nhập số lượng:");
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
        generateUserId();
        Product product = new Product(AUTO_ID,name,category,author,price,description,quantity);
        products.add(product);
        showBook(product);
        saveProductsToJsonFile();
        System.out.println("Thêm sách mới thành công");
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

    public void updateProduct() {
        while (true) {
            System.out.println("Mời bạn nhập ID của sách : ");
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
                System.out.println("Thông tin không chính xác , vui lòng nhập lại : ");
                continue;
            }
            System.out.println("Sách cần sửa là:");
            showBook(product);
            System.out.println("Mời bạn chọn phần thông tin muốn chỉnh sửa : ");
            System.out.println("1. Tên sách");
            System.out.println("2. Thể loại");
            System.out.println("3. Tên Tác giả");
            System.out.println("4. Giá bán");
            System.out.println("5. Mô tả sách");
            System.out.println("6. Số lượng");
            System.out.println("7. Thoát");
            System.out.print("Chọn chức năng: ");
            int options = 0;
            while (true) {
                try {
                    options = scanner.nextInt();
                    scanner.nextLine();
                    if (options < 1 || options > 7) {
                        System.out.println("Chức năng là số từ 1 tới 7, vui lòng nhập lại: ");
                        continue;
                    }
                    break;
                } catch (InputMismatchException ex) {
                    System.out.print("Lựa chọn phải là một số nguyên, vui lòng nhập lại: ");
                    scanner.nextLine();
                }
            }
            switch (options) {
                case 1:
                    System.out.println("Mời bạn nhập tên sách mới");
                    String newName = scanner.nextLine();
                    product.setName(newName);
                    break;
                case 2:
                    System.out.println("Mời bạn nhập thể loại mới");
                    String newCategory = scanner.nextLine();
                    product.setCategory(newCategory);
                    break;
                case 3:
                    System.out.println("Mời bạn nhập tên giả mới");
                    String newAuthor = scanner.nextLine();
                    product.setAuthor(newAuthor);
                    break;
                case 4:
                    System.out.println("Mời bạn nhập giá mới: ");
                    double newPrice;
                    while (true) {
                        try {
                            newPrice = scanner.nextDouble();
                            scanner.nextLine();
                            if (newPrice <= 0) {
                                System.out.println("Giá 1 quyển sách phải là 1 số dương");
                                continue;
                            }
                            break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số tự nhiên hợp lệ
                        } catch (InputMismatchException e) {
                            System.out.println("Giá trị bạn vừa nhập không phải là một số tự nhiên . Vui lòng nhập lại.");
                            scanner.nextLine();
                        }
                    }
                    product.setPrice(newPrice);
                    break;
                case 5:
                    System.out.println("Mời bạn nhập mô tả sách mới");
                    String newDescription = scanner.nextLine();
                    product.setDescription(newDescription);
                case 6:
                    System.out.println("Vui lòng nhập số lượng mới:");
                    int newQuantity;
                    while (true) {
                        try {
                            newQuantity = scanner.nextInt();
                            scanner.nextLine();
                            if (newQuantity <= 0) {
                                System.out.println("Số lượng sách phải là số dương và lớn hơn 0, vui lòng nhập lại ");
                                continue;
                            }
                            break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
                        } catch (InputMismatchException e) {
                            System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                            scanner.nextLine();
                        }
                    }
                    product.setQuantity(newQuantity);
                    break;
                case 7:
                    return;
            }
            System.out.println("Cập nhật thành công ");
            showBook(product);
            products.add(product);
            saveProductsToJsonFile();
            break;
        }
    }
    public void updateQuantityProduct(Product product, int quantity) {
            product.setQuantity(quantity);
            products.add(product);
            saveProductsToJsonFile();
    }
    public void deleteProduct() {
        System.out.println("Mời bạn nhập ID của sản phẩm cần xóa: ");
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
            System.out.println("Không tìm thấy sản phẩm với ID đã nhập.");
            return;
        }
        System.out.println("Sách cần xóa là:");
        showBook(product);
        // Lấy danh sách sản phẩm từ file JSON
        List<Product> existingProducts = new ArrayList<>(getProductsFromJsonFile());
        // Kiểm tra xem sản phẩm cần xóa có tồn tại trong danh sách không
        int index = getProductIndex(existingProducts, productId);
        if (index != -1) {
            // Nếu sản phẩm tồn tại, xóa sản phẩm khỏi danh sách
            existingProducts.remove(index);
            // Ghi danh sách đã chỉnh sửa vào file JSON
            saveProductsToJsonFile(existingProducts);
            System.out.println("Đã xóa sản phẩm thành công.");
        }
    }
    private void saveProductsToJsonFile(List<Product> products) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_PRODUCT));
            gson.toJson(products, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Hiển thị tất cả sản phẩm
    public void showProductAll() {
        List<Product> productsAll = getProductsFromJsonFile();
        try {
            if (productsAll.isEmpty())
                System.out.println("Chưa có sản phẩm nào trong shop.");
            else {
                System.out.println("Danh sách Sản Phẩm:");
                showBooks(productsAll);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Tìm kiếm theo tên sản phẩm
    public void searchProductByName() {
        List<Product> products = getProductsFromJsonFile();
        List<Product> foundProducts = new ArrayList<>();
        System.out.println("Vui lòng nhập tên sách:");
        String name = scanner.nextLine();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                foundProducts.add(product);
            }
        }
        if (foundProducts.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào!");
        } else {
            System.out.println("Các sản phẩm được tìm thấy:");
            showBooks(foundProducts);
        }
    }
    //Hiển thị danh mục thể loại
    public void displayCategories() {
        List<Product> products = getProductsFromJsonFile();
        Set<String> categories = new HashSet<>(); // Sử dụng Set để đảm bảo các thể loại là duy nhất
        for (Product product : products) {
            categories.add(product.getCategory());
        }
        System.out.println("Các thể loại sản phẩm có sẵn:");
        for (String category : categories) {
            System.out.println(category);
        }
    }
    public void searchProductByCategory() {
        List<Product> products = getProductsFromJsonFile();
        List<Product> foundProducts = new ArrayList<>();
        System.out.println("Vui lòng nhập thể loại:");
        String category = scanner.nextLine();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                foundProducts.add(product);
            }
        }
        if (foundProducts.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào!");
        } else {
            System.out.println("Các sản phẩm được tìm thấy theo thể loại:");
            showBooks(foundProducts);
        }
    }
}
