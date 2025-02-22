package restaurant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resID;
    private Date dateTime;
    private int gID;
    private int tableID;
    private String guestName;
    private String phone;
    private double totalPayment;
    private List<Meal> orderedMeals;

    public Reservation() {
    }

    public Reservation(int resID, Date dateTime, int tableID, String guestName, int gID, String phone,List<Meal> orderedMeals) {
        this.resID = resID;
        this.dateTime = dateTime;
        this.tableID = tableID;
        this.guestName = guestName;
        this.gID = gID;
        this.phone = phone;
        this.orderedMeals = orderedMeals;
    }

    public double calculatePayment() {
        this.totalPayment = 0.0;
        for (Meal meal : orderedMeals) {
            totalPayment += meal.getPrice();
        }
        return totalPayment;
    }

    public int getgID() {
        return gID;
    }

    public void setgID(int gID) {
        this.gID = gID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getTableID() {
        return tableID;
    }

    public String getPhone() {
        return phone;
    }

    public double getPayment() {
        return totalPayment;
    }

    public int getResID() {
        return resID;
    }

    public String getGuestName() {
        return guestName;
    }

    public List<Meal> getOrderedMeals() {
        return orderedMeals;
    }

    public void addMeal(Meal meal) {
        orderedMeals.add(meal);
        totalPayment += meal.getPrice();
    }

    public void removeMeal(Meal meal) {
        orderedMeals.remove(meal);
        totalPayment -= meal.getPrice();
    }

    public void setOrderedMeals(List<Meal> orderedMeals) {
        this.orderedMeals = orderedMeals;
        calculatePayment(); // Recalculate the total payment whenever meals are set
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "resID=" + resID +
                ", dateTime=" + dateTime +
                ", tableID=" + tableID +
                ", guestName='" + guestName + '\'' +
                ", phone='" + phone + '\'' +
                ", totalPayment=" + totalPayment +
                ", orderedMeals=" + orderedMeals +
                '}';
    }
}