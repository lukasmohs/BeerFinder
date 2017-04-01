package lmohs.cmu.edu.beerfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSearchButton();

    }

    public void onDownloadReady(String res){
        System.out.println(res);
        ((TextView)findViewById(R.id.textView)).setText(res);
    }

    private void initSearchButton() {

        final MainActivity callback = this;
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

        final Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    String message = "Please give the App permission to access the location service";
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, mLocationListener);

                new Connection().getBeer(callback,mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                        mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), 300);
            }
        });
    }



}
