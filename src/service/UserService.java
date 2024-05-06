package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.User;
import ultils.Regex;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private final String FILE_USER = "users.json";
    private final List<User> users = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
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
    private void saveUsersToJsonFile() {
        try  {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<User> existingUsers = new ArrayList<>(getUsersFromJsonFile());
            existingUsers.addAll(users);
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_USER));
            gson.toJson(existingUsers, writer);
            System.out.println("Đăng ký thành công");
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
}
