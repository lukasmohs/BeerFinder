package lmohs.cmu.edu.beerfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * This class provides a function to convert the server response to a list of Bars
 * Created by lukasmohs on 31/03/17.
 */

public class XMLParser {

    /**
     * This static method takes the JSON formatted server answer and parses it into an ArrayList of
     * Bar objects, which is returned.
     * @param response
     * @return
     */
    public static ArrayList<Bar> parseServerResponseIntoBars(String response) {
        ArrayList barList = new ArrayList<Bar>();

        try {
            // Instantiate a JSON Tokener with the provided JSON message from the server in form of a String
            JSONTokener tokener = new JSONTokener(response);
            // Instatiate a JSON object from the Tokenener
            JSONObject js = new JSONObject(tokener);
            // Loop over all bars in the "bars" JSONArray
            JSONArray ja =  js.getJSONArray("bars");
            for(int i = 0; i< ja.length(); i++) {
                JSONObject o = ja.getJSONObject(i);
                // For each JSONObject, create a Bar object and add it to the ArrayList
                barList.add(new Bar(
                        o.getString("name"), o.getString("address"), o.getString("lat"),
                        o.getString("lon"), o.getString("price")));
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        // Return the list of all Bars
        return barList;
    }

}
