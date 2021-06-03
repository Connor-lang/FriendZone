
import javafx.application.Application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */
public class Login {
    
    public static String accountOwner;
    public static String recipient;
    
    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.print("Username : ");
        String userName = input.nextLine();
        System.out.print("Password : ");
        String password = input.nextLine();
        if (password.equals("12345")) {
            System.out.println("Correct");
            accountOwner = userName;
            Application.launch(FriendRequestDemo.class, args);
        }
    }
}
