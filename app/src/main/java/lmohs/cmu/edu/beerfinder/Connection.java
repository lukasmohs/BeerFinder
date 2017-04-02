package lmohs.cmu.edu.beerfinder;

import android.os.AsyncTask;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lukasmohs on 27/03/17.
 */

public class Connection {

    private String SERVICEURL = "https://beerfinderbackend.herokuapp.com/getBeer";
    //private String SERVICEURL = "http://128.237.190.61:8080/BeerFinder/getBeer";

    private MainActivity instance;

    public void getBeer(MainActivity callback, double lat, double lon, int radius) {
        instance = callback;
        String os = "Android " + System.getProperty("os.version");
        new AsyncBackendCall().execute(lat+"", lon+"", radius + "", os);

    }

    private class AsyncBackendCall extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String res) {
            ArrayList<Bar> bars = XMLParser.parseServerResponseIntoBars(res);
            instance.onDownloadReady(bars);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            String res = "";
            try {
                url = new URL(SERVICEURL
                        + "?lat=" + params[0]
                        + "&lon=" + params[1]
                        + "&radius="+ params[2]
                        +  "&os=" + params[3]);
                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
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
            return res;
        }
    }

}
