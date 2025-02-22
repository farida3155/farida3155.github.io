package restaurant;

import java.io.*;
import java.util.ArrayList;

public abstract class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private String role;
    private String phone;

    public User() {
    }

    public User(String username, String password, String name, String role, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
public static void saveUsers(ArrayList<User> users) throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Users.dat"))) {
        oos.writeObject(users);
    }
}

public static ArrayList<User> loadUsers() throws IOException, ClassNotFoundException {
    File userFile = new File("Users.dat");
    if (!userFile.exists() || userFile.length() == 0) {
        return new ArrayList<>(); 
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
        return (ArrayList<User>) ois.readObject();
    }
}

    public abstract void displayOptions();
}

