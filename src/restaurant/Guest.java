package restaurant;

import java.io.Serializable;

public class Guest extends User implements Serializable{
private static final long serialVersionUID = 1L;
private String phone;
    public Guest() {
    }

    public Guest(String username, String password, String name, String role, String phone) {
        super(username, password, name, role, phone);
    }

    public void viewReservations(String phone) {
        Receptionist receptionist = new Receptionist();
        receptionist.loadReservations(phone);
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            System.out.println("Thank you for your rating of " + rating + " stars!");
        } else {
            System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
        }
    }

    @Override
    public void displayOptions() {
        System.out.println("1. View Menu");
        System.out.println("2. View Reservations");
        System.out.println("3. Set Rating");
        System.out.println("4. Exit");
    }
}