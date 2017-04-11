package lmohs.cmu.edu.beerfinder;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * This MainActivity class is the Controller for the Main UI of the BeerFinder Application
 * The onCreate() method initializes the UI elements by invoking the init methods for the searchButton
 * and the seekBar. It furthermore provides a function to get the Context, the current value of the seekBar
 * and the onDownloadReady() method that is executed to update the UI with Bars that were found in that area.
 */
public class MainActivity extends AppCompatActivity {

    // Default serach radius
    private int radius = 500;
    // This global variable keeps track of the ProgressDialog layover that is shown during the
    // search operation of the application
    private ProgressDialog progress;

    /**
     * This method is invoked, when the App is started. It sets the View to the
     * activity_main.xml definition and initializes the searchButton and the seekBar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSeekBar();
        initSearchButton();
    }

    /**
     *
     * @return the context of the MainActivity
     */
    private Context getContext() {
        return this;
    }

    /**
     * This method is invoked after the asynchronous backend call has ended.
     * It takes the list of retrieved bars as an argument and instantiates the BarsAdapter with it.
     * Then, the ListView is provided with this adapter and the UI gets updated.
     * After completion, the progressDialog overlay is hidden. In case no bar was found,
     * the user is notified with a corresponding message in a Toast.
     * @param bars
     */
    public void onDownloadReady(ArrayList<Bar> bars){
        // Get ListView
        ListView barListView = ((ListView)findViewById(R.id.listView));
        // Instantiate BarsAdapter
        BarsAdapter adapter = new BarsAdapter(this, R.layout.barlistrow, bars);
        // Set BarsAdapter for ListView
        barListView.setAdapter(adapter);
        // Dismiss the progressDialog
        progress.dismiss();
        // In case no bars were found, notify the user
        if(bars.size()==0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Unfortunately, no bar was found in your area," +
                    " please try to increase the radius or move to a city!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     *
     * @return the current search radius
     */
    private int getRadius() {
        return radius;
    }

    /**
     * This method sets the current search radius and updates the corresponding text field
     * @param mRadius
     */
    private void setRadius(int mRadius) {
        radius = mRadius;
        // Update the text radius textView
        ((TextView)findViewById(R.id.radiusView)).setText("Radius: " +mRadius + "m");
    }

    /**
     * This method initializes the search button on the main UI of the Application
     */
    private void initSearchButton() {

        final MainActivity callback = this;
        // First, a new LocationListener is implemented to retrieve the location of the device
        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Get the Search Button in the View
        final Button button = (Button) findViewById(R.id.searchButton);
        // Define an onClickListener:
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Check whether the App has permission to acces the location service
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // If not, ask the user to change the settings by showing a Toast and return
                    String message = "Please give the App permission to access the location service";
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                // Instantiate a ProgressDialog as an overlay and keep it visible until the server has responded
                progress = new ProgressDialog(getContext());
                progress.setTitle("Searching");
                progress.setMessage("Hoping for hops...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, mLocationListener);
                // Delegate the backend call to the Connection class and provide the location of the phone and the selected radius
                new Connection().getBeer(callback,mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                        mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), getRadius());
            }
        });
    }

    /**
     * This method is used to initialize the seekBar, which allows the user to specify the search radius.
     * First some style definitions are made and afterwards an onChangeListener is implemented
     */
    private void initSeekBar() {
        // Get SeekBar from View
        SeekBar sBar = (SeekBar) findViewById(R.id.seekBar);
        // Change the color settings
        sBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        // Define the shape of the movable circle
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(50);
        thumb.setIntrinsicWidth(50);
        sBar.setThumb(thumb);
        // Define the color of the movable circle
        sBar.getThumb().setColorFilter(Color.rgb(100,149,237), PorterDuff.Mode.SRC_IN);
        //  Implement a onChangeListener:
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int newRadius = 500;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                // In case the user changed the state, take the value (between 1 and 10) and multiply it by 100
                // Then call the setRadius() to update the UI and store the new value
                newRadius = progress * 100;
                setRadius(newRadius);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
