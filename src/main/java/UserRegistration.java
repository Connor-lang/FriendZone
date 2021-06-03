/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

/**
 *
 * @author Windows 10
 */
public class UserRegistration {

    static Scanner sc;

    //To prompt user input
    public static User input() throws IOException, MalformedURLException, GeoIp2Exception {
        sc = new Scanner(System.in);
        System.out.println("What id ?");
        Integer id = sc.nextInt();
        System.out.println("What is your name?");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Password?");
        String password = sc.nextLine();
        System.out.println("What is your age ?");
        Integer age = sc.nextInt();
        System.out.println("What is your gender ?");
        char gender = sc.next().charAt(0);
        System.out.println("Are you a foodlover ? (Yes / No)");
        Double interest1 = determineBinary(sc.next());
        System.out.println("Do you like watching movies ? (Yes / No)");
        Double interest2 = determineBinary(sc.next());
        System.out.println("Are you a sporty guy ? (Yes / No)");
        Double interest3 = determineBinary(sc.next());
        System.out.println("Are you friendly ? (Yes / No)");
        Double interest4 = determineBinary(sc.next());
        System.out.println("Are you cheerful ? (Yes / No)");
        Double interest5 = determineBinary(sc.next());
        System.out.println("Do you like music ? (Yes / No)");
        Double interest6 = determineBinary(sc.next());
        System.out.println("Are you an introvert ? (Yes / No)");
        Double interest7 = determineBinary(sc.next());
        System.out.println("Are you an extrovert ? (Yes / No)");
        Double interest8 = determineBinary(sc.next());
        System.out.println("Are you a caring person ? (Yes / No)");
        Double interest9 = determineBinary(sc.next());
        System.out.println("Are you a helpful person ? (Yes / No)");
        Double interest10 = determineBinary(sc.next());

        User user = new User(id, name, age, gender, interest1,
                interest2, interest3, interest4, interest5, interest6, interest7, interest8, interest9, interest10);
        Double distanceFromKL = Location.distance(3.1390, 101.6869, user.latitude, user.longitude, "K");
        System.out.println(user.toString());
        return user;
    }

    public static Double determineBinary(String a) {
        if (a.equalsIgnoreCase("Yes")) {
            return 1.0;
        } else if (a.equalsIgnoreCase("No")) {
            return 0.0;
        } else {
            return -1.0;
        }
    }
}

