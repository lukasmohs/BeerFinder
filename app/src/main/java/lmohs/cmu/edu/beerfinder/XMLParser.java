package lmohs.cmu.edu.beerfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by lukasmohs on 31/03/17.
 */

public class XMLParser {


    public static ArrayList<Bar> parseServerResponseIntoBars(String response) {
        ArrayList barList = new ArrayList<Bar>();

        try {
            // Instantiate a JSON Tokener with the provided JSON message from a client in form of a String
            JSONTokener tokener = new JSONTokener(response);
            // Instatiate a JSON object from the Tokenener
            JSONObject js = new JSONObject(tokener);
            // Loop over all installers in the in the company
            JSONArray ja =  js.getJSONArray("bars");
            for(int i = 0; i< ja.length(); i++) {
                JSONObject o = ja.getJSONObject(i);

                barList.add(new Bar(
                        o.getString("name"), o.getString("address"), o.getString("lat"),
                        o.getString("lon"), o.getString("price")));
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return barList;
    }

}
