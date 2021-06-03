/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Windows 10
 */

public class FriendZone {

    public static void displayLinkedList(LinkedList<User> list) {
        Iterator<User> iterator = list.iterator();
        while (iterator.hasNext()) {
            User temp = iterator.next();
            System.out.println(temp.toString());
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, MalformedURLException, GeoIp2Exception, ClassNotFoundException {

        User newUser = UserRegistration.input();

        LinkedHashMap<User, Double> distance = Location.sortByDistanceFromUserLocation(newUser, 100.0);

        LinkedHashMap<User, Double> list = Location.sortByInterests(newUser, distance);

        Iterator it = list.entrySet().iterator();
        System.out.println("\nThe Users who are within 100(km) around you :");
        while (it.hasNext()) {
            Map.Entry<User, Double> pair = (Map.Entry<User, Double>) it.next();
            System.out.println(pair.getKey().toString() + " Distance = " + pair.getValue());
        }
        RDatabase.storeDataToDB(newUser);
    }
}
