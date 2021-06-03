
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HP
 */
public class RDatabase {

    private static Connection conn = null;
    static final String URL = "jdbc:sqlite:C:\\Users\\HP\\Documents\\NetBeansProjects\\My Project\\User_Data.db";
    private PreparedStatement preparedStatement;
    private static final ArrayList<String> arrayList = new ArrayList<>();
    
    public static void getUserCoordinates() {
        try {
            conn = DriverManager.getConnection(URL);
            String query = "SELECT Latitude, Longitude FROM USER_DATA";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Fetch Data");
            resultSet.next();
            System.out.println(resultSet.getDouble("Latitude"));
            System.out.println(resultSet.getDouble("Longitude"));
        } catch (SQLException ex) {
        }
    }

    public static void storeDataToDB(User user) throws FileNotFoundException {
        try {
            conn = DriverManager.getConnection(URL);
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO USER_DATA values (" + user.id + ",'" + user.name + "','" + user.password + "'," + user.age + ",'" + user.gender + "',"
                    + user.foodlover + "," + user.movie + "," + user.sporty + "," + user.friendly + "," + user.cheerful
                    + "," + user.music + "," + user.introvert + "," + user.extrovert + "," + user.caring + "," + user.helpful
                    + "," + user.latitude + "," + user.longitude + ",'" + user.city + "','" + user.state + "','" + user.country + "')");

            conn.close();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public static ArrayList getUserData() {
        try {
            conn = DriverManager.getConnection(URL);
            String query = "SELECT ID, Name, Age, City, State, Country FROM USER_DATA";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            for (int i = 0; i < 30; i++) {
                resultSet.next();
                arrayList.add("ID : " + resultSet.getInt("ID") + "\nName : " + resultSet.getString("Name")
                + "\nAge : " + resultSet.getInt("Age") + "\nCity : " + resultSet.getString("City") + "\nState : " + 
                        resultSet.getString("State") + "\nCountry : " + resultSet.getString("Country") + "\n");
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Got an exception");
        }
        return arrayList;
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        getUserData();
    }
}
