
import java.io.IOException;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HP
 */
public class GetLocation {

    static java.io.File database = new java.io.File("GeoLite2-City.mmdb");
    

    public static InetAddress getInetAddress() throws MalformedURLException, IOException, GeoIp2Exception {

        URL checkIP = new URL("http://checkip.amazonaws.com");
        BufferedReader input = new BufferedReader(new InputStreamReader(
                checkIP.openStream()));
        String ip = input.readLine();
        InetAddress inetAddress = InetAddress.getByName(ip);
        return inetAddress;
    }
    
    public static InetAddress getInetAddress(String ip) throws MalformedURLException, IOException, GeoIp2Exception {

        InetAddress inetAddress = InetAddress.getByName(ip);
        return inetAddress;
    }

    public static Double[] getCoordinates(InetAddress inetAddress) throws IOException, GeoIp2Exception {
        Double[] coordinates = new Double[2];
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
        CityResponse response = dbReader.city(inetAddress);
        Double latitude = response.getLocation().getLatitude();
        Double longitude = response.getLocation().getLongitude();
        coordinates[0] = latitude;
        coordinates[1] = longitude;
        return coordinates;
    }

    public static String getCity(InetAddress inetAddress) throws IOException, GeoIp2Exception {
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
        CityResponse response = dbReader.city(inetAddress);
        String city = response.getCity().getName();
        return city;
    }

    public static String getState(InetAddress inetAddress) throws IOException, GeoIp2Exception {
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
        CityResponse response = dbReader.city(inetAddress);
        String state = response.getLeastSpecificSubdivision().getName();
        return state;
    }

    public static String getCountry(InetAddress inetAddress) throws IOException, GeoIp2Exception {
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
        CityResponse response = dbReader.city(inetAddress);
        String city = response.getCity().getName();
        return city;
    }
    
    public static void main(String[] args) throws IOException, MalformedURLException, GeoIp2Exception {
        System.out.println(getInetAddress());
        System.out.println(getCountry(getInetAddress()));
    }
}
