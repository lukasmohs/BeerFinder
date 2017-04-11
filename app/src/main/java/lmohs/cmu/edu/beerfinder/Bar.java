package lmohs.cmu.edu.beerfinder;

/**
 * This entity class wraps the server responses into many of these objects.
 * Created by lukasmohs on 31/03/17.
 */

public class Bar {
    private String name;
    private String address;
    private String lat;
    private String lon;
    private String price;

    /**
     * Constructor
     * @param name
     * @param address
     * @param lat
     * @param lon
     * @param price
     */
    public Bar(String name, String address, String lat, String lon, String price) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.price = price;
    }

    /**
     *
     * @return the name of the Bar as String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the full address of the bar as a String
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return the latitude of the Bar as a String
     */
    public String getLat() {
        return lat;
    }

    /**
     *
     * @return the longitude of the Bar as a String
     */
    public String getLon() {
        return lon;
    }

    /**
     *
     * @return the price classification as a String
     */
    public String getPrice() {
        return price;
    }


}