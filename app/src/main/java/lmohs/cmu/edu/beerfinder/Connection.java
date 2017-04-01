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

    private String SERVICEURL = "https://whispering-lowlands-87866.herokuapp.com/getBeer";

    private MainActivity instance;

    public void getBeer(MainActivity callback, double lat, double lon, int radius) {
        instance = callback;
        new AsyncBackendCall().execute(lat+"", lon+"", radius + "");

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
                        + "&radius="+ params[2]);
                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    //System.out.print(current);
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
