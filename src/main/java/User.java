
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Windows 10
 */
public class User {
    int id;String name;String password;int age;char gender;double foodlover;double movie;double sporty;
    double friendly;double cheerful;double music;double introvert;double extrovert;
    double caring;double helpful;double latitude;double longitude;Integer similarity = 0; String city = ""; String state = ""; String country = ""; //Cluster cluster;
     

    public User(int id, String name, int age, char gender, double foodlover, double movie, double sporty, double friendly, double cheerful, 
            double music, double introvert, double extrovert, double caring, double helpful) throws IOException, MalformedURLException, GeoIp2Exception { //double DistanceFromKL
        InetAddress inetAddress = GetLocation.getInetAddress();
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.foodlover = foodlover;
        this.movie = movie;
        this.sporty = sporty;
        this.friendly = friendly;
        this.cheerful = cheerful;
        this.music = music;
        this.introvert = introvert;
        this.extrovert = extrovert;
        this.caring = caring;
        this.helpful = helpful;
        Double[] coordinates = GetLocation.getCoordinates(inetAddress);
        latitude = coordinates[0];
        longitude = coordinates[1];
        city = GetLocation.getCity(inetAddress); 
        state = GetLocation.getState(inetAddress); 
        country = GetLocation.getCountry(inetAddress);
    }
    
    public User(int id, String name, String password, int age, char gender, double foodlover, double movie, double sporty, double friendly, double cheerful, 
            double music, double introvert, double extrovert, double caring, double helpful, double latitude, double longitude) throws IOException, MalformedURLException, GeoIp2Exception { //double DistanceFromKL
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.foodlover = foodlover;
        this.movie = movie;
        this.sporty = sporty;
        this.friendly = friendly;
        this.cheerful = cheerful;
        this.music = music;
        this.introvert = introvert;
        this.extrovert = extrovert;
        this.caring = caring;
        this.helpful = helpful;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public String toString(){
        String temp = "User id = " + id + ", Name = " + name + ", Password = " + password + ", age = " + age + ", Gender = " + gender + ", Foodlover = " + foodlover + ", Movie = " + movie + ", Sporty = " + sporty +
                ", Friendly = " + friendly + ", Cheerful = " + cheerful + ", Music = " + music + ", Introvert = " + introvert + ", Extrovert = " + extrovert + ", Caring = "  
                + caring + ", Helpful = " + helpful + " Latitude = " + latitude + ", Longitude = " + longitude + 
                ", Similarity = " + similarity + ", City = " + city + ", State = " + state + " Country = " + country; // ", DistanceFromKL = " + distanceFromKL + ", Cluster = " + cluster.toString();
        return temp;
    }
    
    public Double[] getFeatures(){
        Double[]features = {foodlover, movie, sporty,friendly, cheerful, music, introvert, extrovert, caring, helpful};
        return features;
    }
}
