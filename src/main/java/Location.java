/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Windows 10
 */
public class Location {

    //To get LinkedHashMap of people within a certain range and sort them by distance
    public static LinkedHashMap<User, Double> sortByDistanceFromUserLocation(User newUser, Double range) throws ClassNotFoundException, IOException, MalformedURLException, GeoIp2Exception {
        LinkedHashMap<User, Double> distances = new LinkedHashMap<>();
        int count = 1;

        //Loop through the entire List of Users and calculate the distance between the users in the list and the newly registered user(The location lat2 and long 2 define
        //the location of newly registered user)
        try {
            Connection conn = DriverManager.getConnection(RDatabase.URL);
            String query = "SELECT *  FROM USER_DATA WHERE Name != '" + newUser.name + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Fetch Data");

            while (resultSet.next()) {
                double lat1 = resultSet.getDouble("Latitude");
                double long1 = resultSet.getDouble("Longitude");
                double lat2 = newUser.latitude;
                double long2 = newUser.longitude;
                double distance = distance(lat1, long1, lat2, long2, "K");
                System.out.println("Count : " + count + " Distance : " + distance);
                User user = new User(resultSet.getInt("ID"), resultSet.getString("Password"), resultSet.getString("Name"), resultSet.getInt("Age"), resultSet.getString("Gender").charAt(0), resultSet.getDouble("Foodlover"), resultSet.getDouble("Movie"),
                        resultSet.getDouble("Sporty"), resultSet.getDouble("Friendly"), resultSet.getDouble("Cheerful"), resultSet.getDouble("Music"), resultSet.getDouble("Introvert"), resultSet.getDouble("Extrovert"),
                        resultSet.getDouble("Caring"), resultSet.getDouble("Helpful"), resultSet.getDouble("Latitude"), resultSet.getDouble("Longitude"));
                distances.put(user, distance);
                count++;
            }
        } catch (SQLException ex) {
        }

        //Distances LinkedHashMap -> Key : User , Value : Distance between the newly registered user and each current account in the database 
        //To get the people who are within a certain range radius around user
        LinkedHashMap<User, Double> nearestUsers = nearestUsers(distances, range);

        //To sort the above LinkedHashMap by distance
        LinkedHashMap<User, Double> map = sortByValues(nearestUsers);

        //Display
        System.out.println("After Sorting : ");
        Set sortedSet = map.entrySet();
        Iterator it = sortedSet.iterator();
        while (it.hasNext()) {
            Map.Entry<User, Double> me2 = (Map.Entry<User, Double>) it.next();
            System.out.print(me2.getKey().toString() + " Distance From User : ");
            System.out.println(me2.getValue());
        }
        System.out.println("Total Users Within " + range + "(km) radius = " + map.size());

        return map;
    }
        //To Sort a given list by interests, from the user most similar to the newly registered user, to the least, based on 10 interests. 
    public static LinkedHashMap sortByInterests(User newUser, LinkedHashMap nearestUsers) {

        //Get LinkedHashMap of users with their key associated with the similarity between the new UserAccount and the NearestUsers LinkedHashMap
        LinkedHashMap<User, Double> users = new LinkedHashMap<>();
        Set set = nearestUsers.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<User, Double> map = (Map.Entry<User, Double>) it.next();
            User user = map.getKey();
            Double[] features = user.getFeatures();
            Integer similarity = getSimilarity(newUser.getFeatures(), features);
            user.similarity = similarity;
            users.put(user, map.getValue());
        }
        //Sort the users by interests by descending order
        LinkedHashMap<User, Double> sorted = sortByInterestsByDescending(users);

        return sorted;
    }

    private static LinkedHashMap sortByInterestsByDescending(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((User) ((Map.Entry) (o1)).getKey()).similarity)
                        .compareTo(((User) ((Map.Entry) (o2)).getKey()).similarity) * -1;
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    //Return similarity between the new User and another user in the database
    public static Integer getSimilarity(Double[] newUser, Double[] user) {
        Integer similarity = 0;
        for (int i = 0; i < newUser.length; i++) {
            if (newUser[i].compareTo(user[i]) == 0) {
                similarity++;
            }
        }
        return similarity;
    }

    //To find users who are within a certain range(km) around the newly registered user
    public static LinkedHashMap nearestUsers(HashMap map, Double range) {
        LinkedHashMap<User, Double> NearestUserslist = new LinkedHashMap<>();
        Set set = map.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<User, Double> me2 = (Map.Entry<User, Double>) it.next();
            if (me2.getValue() <= range) {
                NearestUserslist.put(me2.getKey(), me2.getValue());
            }
        }
        return NearestUserslist;
    }

    //To Sort LinkedHashMap by values
    private static LinkedHashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    //Method to calculate the geographical distance between two users and the unit specifies either in miles(default), km("K") or nautical miles("N") 
    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

}
