package restaurant;

import java.io.Serializable;

public class Meal implements Serializable {
    private StringBuffer name;
    private String category;
    private double price;

    public Meal() {
    }

    public Meal(StringBuffer name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    Meal(String mealName, double mealPrice) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public StringBuffer getName() {
        return name;
    }

    public void setName(StringBuffer name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void display() {
        System.out.println(name + " - " + category + " - $" + price);
    }

     @Override
    public String toString() {
        return "Meal [name=" + name + ", category=" + category + ", price=" + price + "]";
    }
}