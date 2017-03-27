package lmohs.cmu.edu.beerfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lukasmohs on 27/03/17.
 */

public class Connection {

    private MainActivity instance;

    public void getBeer(MainActivity callback) {
        instance = callback;
        new AsyncBackendCall().execute("here");

    }

    private class AsyncBackendCall extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String res) {
            instance.onDownloadReady(res);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            String res = "";
            try {
                url = new URL("https://whispering-lowlands-87866.herokuapp.com/getBeer");

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
