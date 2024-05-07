package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.OrderItem;
import entity.Product;
import entity.User;
import ultils.Regex;
import view.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserService {
    private final String FILE_USER = "users.json";
    private final List<User> users = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final OrderSerivce orderSerivce = new OrderSerivce();
    private static int AUTO_ID;

    public User createAccount( ) {
        generateUserId();
        System.out.println("Vui lòng nhập email:");
        String email = scanner.nextLine();
        while (!email.matches(Regex.EMAIL_REGEX)) {
            System.out.println("Email không đúng định dạng vui lòng nhập lại");
            email = scanner.nextLine();
        }
        System.out.println("Vui lòng nhập mật khẩu (6 -> 20 ký tự cả chữ thường, chữ hoa và cả số):");
        String password = scanner.nextLine();
        while (!password.matches(Regex.PASSWORD_REGEX)) {
            System.out.println("Mật khẩu không đúng định dạng vui lòng nhập lại");
            password = scanner.nextLine();
        }
        System.out.println("Nhập tên của bạn:");
        String name = scanner.nextLine();
        System.out.println("Nhập số điện thoại (bắt đầu 0 và có 9 so tiep theo):");
        String phone = scanner.nextLine();
        while (!phone.matches(Regex.PHONE_REGEX)) {
            System.out.println("Số điện thoại không đúng định dạng vui lòng nhập lại");
            phone = scanner.nextLine();
        }
        System.out.println("Nhập địa chỉ:");
        String address = scanner.nextLine();
        return new User(AUTO_ID, email, password, name, phone, address, User.Role.USER);
    }
    public void printHeader() {
        System.out.printf("%-5s%-30s%-30s%-20s%-20s%-10s%n", "Id", "Name", "Email", "Phone", "Address", "Role");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
    }
    public void showUserDetail(User user) {
        System.out.printf("%-5s%-30s%-30s%-20s%-20s%-10s%n", user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), user.getRole());
    }

    public void showUser(User user) {
        printHeader();
        showUserDetail(user);
    }

    public void showUsers(List<User> userAll) {
        printHeader();
        for (User user : userAll) {
            showUserDetail(user);
        }
    }

    public void register() {
        User user = createAccount();
        users.add(user);
        showUser(user);
        saveUsersToJsonFile();
    }

    public void generateUserId() {
        List<User> users = getUsersFromJsonFile();
        int maxId = 0;
        for (User user : users) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        AUTO_ID = maxId + 1;
    }

    public User login() {
        List<User> users = getUsersFromJsonFile();
        System.out.println("Vui lòng nhập email:");
        String email = scanner.nextLine();
        while (!email.matches(Regex.EMAIL_REGEX)) {
            System.out.println("Email không đúng định dạng vui lòng nhập lại");
            email = scanner.nextLine();
        }
        System.out.println("Vui lòng nhập mật khẩu:");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Đăng nhập thành công!");
                return user;
            }
        }
        System.out.println("Đăng nhập thất bại");
        return null;
    }

    private int getUserIndex(List<User> users, int userId) {
        // Duyệt qua danh sách người dùng và tìm vị trí của người dùng theo id
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy người dùng
    }
    private void saveUsersToJsonFile() {
        try  {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<User> existingUsers = new ArrayList<>(getUsersFromJsonFile());
            // Duyệt qua danh sách người dùng
            for (User user : users) {
                // Kiểm tra xem người dùng đã tồn tại trong danh sách hiện có chưa
                int index = getUserIndex(existingUsers, user.getId());
                if (index != -1) {
                    // Nếu đã tồn tại, cập nhật người dùng tại vị trí index
                    existingUsers.set(index, user);
                } else {
                    // Nếu không tồn tại, thêm người dùng mới vào danh sách
                    existingUsers.add(user);
                }
            }
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_USER));
            gson.toJson(existingUsers, writer);
            System.out.println("Đăng ký thành công");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveUsersToJsonFile(List<User> users) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_USER));
            gson.toJson(users, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<User> getUsersFromJsonFile() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(FILE_USER));
            List<User> users = Arrays.asList(gson.fromJson(reader, User[].class));
            reader.close();
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public void updateUser() {
        while (true) {
            User user = Main.loggedInUser;
            System.out.println("Thông tin người dùng:");
            showUser(user);
            System.out.println("Mời bạn chọn phần thông tin muốn chỉnh sửa : ");
            System.out.println("1. Tên");
            System.out.println("2. Email");
            System.out.println("3. Mật khẩu");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Địa chỉ");;
            System.out.println("6. Thoát");
            System.out.print("Chọn chức năng: ");
            int options = 0;
            while (true) {
                try {
                    options = scanner.nextInt();
                    scanner.nextLine();
                    if (options < 1 || options > 6) {
                        System.out.println("Chức năng là số từ 1 tới 6, vui lòng nhập lại: ");
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
                    System.out.println("Mời bạn nhập tên mới của bạn:");
                    String newName = scanner.nextLine();
                    user.setName(newName);
                    break;
                case 2:
                    System.out.println("Mời bạn nhập Email mới:");
                    String newEmail = scanner.nextLine();
                    while (!newEmail.matches(Regex.EMAIL_REGEX)) {
                        System.out.println("Email không đúng định dạng vui lòng nhập lại");
                        newEmail = scanner.nextLine();
                    }
                    user.setEmail(newEmail);
                    break;
                case 3:
                    System.out.println("Mời bạn nhập mật khẩu:");
                    String newPassword = scanner.nextLine();
                    while (!newPassword.matches(Regex.PASSWORD_REGEX)) {
                        System.out.println("Mật khẩu không đúng định dạng vui lòng nhập lại");
                        newPassword = scanner.nextLine();
                    }
                    user.setPassword(newPassword);
                    break;
                case 4:
                    System.out.println("Mời bạn nhập số điện thoại mới: ");
                    String newPhone = scanner.nextLine();
                    while (!newPhone.matches(Regex.PHONE_REGEX)) {
                        System.out.println("Số điện thoại không đúng định dạng vui lòng nhập lại");
                        newPhone = scanner.nextLine();
                    }
                    user.setPhone(newPhone);
                    break;
                case 5:
                    System.out.println("Mời bạn nhập địa chỉ:");
                    String newAdress = scanner.nextLine();
                    user.setAddress(newAdress);
                case 6:
                    return;
            }
            System.out.println("Cập nhật thành công ");
            showUser(user);
            users.add(user);
            saveUsersToJsonFile();
        }
    }

    public void showAllUser() {
        List<User> allUsers = getUsersFromJsonFile();
        if (allUsers.isEmpty()) {
            System.out.println("Không có người dùng");
        } else {
            showUsers(allUsers);
        }
    }
    public void editUserAdmin() {
        while (true) {
            System.out.println("Nhập Id người dùng muốn sửa:");
            int userId;
            while (true) {
                try {
                    userId = scanner.nextInt();
                    scanner.nextLine();
                    break; // Thoát khỏi vòng lặp nếu giá trị được nhập vào là số nguyên hợp lệ
                } catch (InputMismatchException e) {
                    System.out.println("Giá trị bạn vừa nhập không phải là một số nguyên. Vui lòng nhập lại.");
                    scanner.nextLine();
                }
            }
            User user = findUserById(userId);
            System.out.println("Thông tin người dùng:");
            showUser(user);
            System.out.println("Mời bạn chọn phần thông tin muốn chỉnh sửa : ");
            System.out.println("1. Tên");
            System.out.println("2. Email");
            System.out.println("3. Mật khẩu");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Địa chỉ");;
            System.out.println("6. Thoát");
            System.out.print("Chọn chức năng: ");
            int options = 0;
            while (true) {
                try {
                    options = scanner.nextInt();
                    scanner.nextLine();
                    if (options < 1 || options > 6) {
                        System.out.println("Chức năng là số từ 1 tới 6, vui lòng nhập lại: ");
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
                    System.out.println("Mời bạn nhập tên mới của bạn:");
                    String newName = scanner.nextLine();
                    user.setName(newName);
                    break;
                case 2:
                    System.out.println("Mời bạn nhập Email mới:");
                    String newEmail = scanner.nextLine();
                    while (!newEmail.matches(Regex.EMAIL_REGEX)) {
                        System.out.println("Email không đúng định dạng vui lòng nhập lại");
                        newEmail = scanner.nextLine();
                    }
                    user.setEmail(newEmail);
                    break;
                case 3:
                    System.out.println("Mời bạn nhập mật khẩu:");
                    String newPassword = scanner.nextLine();
                    while (!newPassword.matches(Regex.PASSWORD_REGEX)) {
                        System.out.println("Mật khẩu không đúng định dạng vui lòng nhập lại");
                        newPassword = scanner.nextLine();
                    }
                    user.setPassword(newPassword);
                    break;
                case 4:
                    System.out.println("Mời bạn nhập số điện thoại mới: ");
                    String newPhone = scanner.nextLine();
                    while (!newPhone.matches(Regex.PHONE_REGEX)) {
                        System.out.println("Số điện thoại không đúng định dạng vui lòng nhập lại");
                        newPhone = scanner.nextLine();
                    }
                    user.setPhone(newPhone);
                    break;
                case 5:
                    System.out.println("Mời bạn nhập địa chỉ:");
                    String newAdress = scanner.nextLine();
                    user.setAddress(newAdress);
                case 6:
                    return;
            }
            System.out.println("Cập nhật thành công ");
            showUser(user);
            users.add(user);
            saveUsersToJsonFile();
            break;
        }
    }

    public User findUserById(int userId) {
        List<User> users = getUsersFromJsonFile();
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
    public void deleteUserAdmin() {
        System.out.println("Mời bạn nhập ID của người dùng cần xóa: ");
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
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("Không tìm thấy người dùng với ID đã nhập.");
            return;
        }
        System.out.println("Người dùng cần xóa là:");
        showUser(user);
        // Lấy danh sách sản phẩm từ file JSON
        List<User> existingUsers = new ArrayList<>(getUsersFromJsonFile());
        // Kiểm tra xem sản phẩm cần xóa có tồn tại trong danh sách không
        int index = getUserIndex(existingUsers, userId);
        if (index != -1) {
            // Nếu sản phẩm tồn tại, xóa sản phẩm khỏi danh sách
            existingUsers.remove(index);
            // Ghi danh sách đã chỉnh sửa vào file JSON
            saveUsersToJsonFile(existingUsers);
            System.out.println("Đã xóa người dùng thành công.");
        }
    }
}
