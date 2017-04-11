package lmohs.cmu.edu.beerfinder;

import android.os.AsyncTask;
import android.os.Build;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is used by the Main Activity as a delegate to asynchronously query the server backend for
 * the list of bars. When the server has answered, the message is interpreted and casted into a list of bars.
 * Finally, a callabck to the Main Activity triggers the update of the main UI.
 * Created by lukasmohs on 27/03/17.
 */
public class Connection {

    // The backend URL
    private String SERVICEURL = "https://beerfinderbackend.herokuapp.com/getBeer";
    //private String SERVICEURL = "http://128.237.190.61:8080/BeerFinder/getBeer"; //local test address

    private MainActivity instance;

    /**
     * This is the delegate function, which is called by the Main Activity to invoke the asynchronous call to the server
     * @param callback
     * @param lat
     * @param lon
     * @param radius
     */
    public void getBeer(MainActivity callback, double lat, double lon, int radius) {
        // Keep the pointer to the Main Activity for the callback
        instance = callback;
        // Retrieve the OS and device name for the logging of the client activity
        String os = "Android_" + System.getProperty("os.version").replaceAll("\\s+","");
        String deviceName = getDeviceName().replaceAll("\\s+","");
        // Instantiate the AsyncBackendCall
        new AsyncBackendCall().execute(lat+"", lon+"", radius + "", os,  deviceName);
    }

    /**
     * This private class extends the abstract AsyncTask class. It overrides the doInBackground()
     * method, which is invoke after the class instantiation. It realizes the asynchronous backend call
     * and after completion, the onPostExecute() method is automatically executed.
     */
    private class AsyncBackendCall extends AsyncTask<String, Void, String> {

        /**
         * This method is invoked, when the doInBackground() method has finished and takes its output
         * as an input. In this case, the server response in parsed into bar entity classes by using the
         * parseServerResponseIntoBars() method of the XMLParser. Then, the list of bars is handed over to
         * the MainActivity by calling the onDownloadReady() on the callback instance.
         * @param res
         */
        protected void onPostExecute(String res) {
            // Convert the plain backend server answer to a ArrayList of Bar objects
            ArrayList<Bar> bars = XMLParser.parseServerResponseIntoBars(res);
            // Invoke the onDownloadReady() method of the MainActivity to update the UI with Bars
            instance.onDownloadReady(bars);
        }

        /**
         * This overridden method is automatically executed after instantiation of the AsyncBackendCall class.
         * It first creates a HTTP GET request, which provides all parameters retrieved from the user and the phone.
         * Then, the request is sent and the response is collected and returned as a String to the onPostExecute() method
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            String res = "";
            try {
                // Create new URL based on the backend server location and add all query parameters
                url = new URL(SERVICEURL
                        + "?lat="       + params[0]
                        + "&lon="       + params[1]
                        + "&radius="    + params[2]
                        + "&os="        + params[3]
                        + "&device="    + params[4]);
                // Open the connection
                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                // Read the response into a String
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    res += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            // Return the full server response as a String
            return res;
        }
    }

    /**
     * This method uses the public "Build" class to retrieve the device name from the operating system
     * @return a String representation of the device
     */
    private String getDeviceName() {
        // Return either the model name itself if it includes the manufacturer or alternatively combine both
        if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            return Build.MODEL;
        } else {
            return Build.MANUFACTURER + " - " + Build.MODEL;
        }
    }
}
