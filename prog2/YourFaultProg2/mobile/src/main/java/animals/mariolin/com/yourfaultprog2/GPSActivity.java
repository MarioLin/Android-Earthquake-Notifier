package animals.mariolin.com.yourfaultprog2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.common.api.Status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mario Lin on 10/12/15.
 */
public class GPSActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    public static String TAG = "GPSActivity";
    public static int UPDATE_INTERVAL_MS = 100;
    public static int FASTEST_INTERVAL_MS = 100;
    private GoogleMap gMap;
    String magPlace;
    double[] coordinates;
    private LatLng CALIFORNIA = new LatLng(37, -122);
    TextView textView;
    TextView magplaceView;
    TextView distView;
    TextView magView;
    Map<String, String> states = new HashMap<String, String>();

    LocationManager locationManager;
    public double currentLat;
    public double currentLong;
    LatLng earthquakeCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // getting data
        Intent intent = getIntent();
        magPlace = intent.getStringExtra(MainActivity.CLICK);
        coordinates = intent.getDoubleArrayExtra(MainActivity.COORDINATES);
        Log.d(TAG, magPlace);
        Log.d(TAG, Arrays.toString(coordinates));
        earthquakeCoord = new LatLng(coordinates[1], coordinates[0]);
        // Maps
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gMap = mapFragment.getMap();
        textView = (TextView) findViewById(R.id.textView);

        magplaceView = (TextView) findViewById(R.id.magplace);
        distView = (TextView) findViewById(R.id.distance);
        magView = (TextView) findViewById(R.id.magnitude);


        // Locations
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");

    }

    @Override
    public void onConnected(Bundle bundle) {

        // Build a request for continual location updates
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS);

        // Send request for location updates
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.getStatus().isSuccess()) {
                            Log.d(TAG, "Successfully requested");
                        } else {
                            Log.e(TAG, status.getStatusMessage());
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}


    @Override
    public void onLocationChanged(Location location) {
        // Do some work here with the location you have received

        Location earthquakeLoc = new Location("earthquake");
        earthquakeLoc.setLatitude(earthquakeCoord.latitude);
        earthquakeLoc.setLongitude(earthquakeCoord.longitude);
        float dist = location.distanceTo(earthquakeLoc);
//        Log.d(TAG, String.valueOf(dist));
//        Log.d(TAG, String.valueOf(curr.getLatitude()));
//        Log.d(TAG, String.valueOf(curr.getLongitude()));
//        Log.d(TAG, String.valueOf(earthquakeLoc.getLatitude()));
//        Log.d(TAG, String.valueOf(earthquakeLoc.getLongitude()));

        distView.setText( String.valueOf((int)dist/1000)+ " km");
    }

    @Override
    public void onMapReady(GoogleMap map) {

        // Instantiates a new CircleOptions object and defines the center and radius
//        Log.d(TAG, coordinates[1] + ", " + coordinates[0]);
        CircleOptions circleOptions = new CircleOptions()
                .center(earthquakeCoord)
                .radius(5000)
                .fillColor(0x30ff0000)
                .strokeColor(Color.RED).strokeWidth((float) 2.5); // In meters
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.setMyLocationEnabled(true);

        // Get back the mutable Circle

        gMap.addMarker(new MarkerOptions()
                .position(earthquakeCoord));
        gMap.clear();
        Circle circle = gMap.addCircle(circleOptions);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(earthquakeCoord, (float)10.5));
//        Log.d(TAG, magPlace);

        //setting text of map view, magnitude and place
        String[] split = magPlace.split(" ");
        String mag = split[0];

        String[] placeArray = Arrays.copyOfRange(split, 1, split.length);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < placeArray.length; i++) {
            String curr = placeArray[i];
            if (states.containsKey(curr)) {
                strBuilder.append(states.get(curr));
            }
            else if (curr.length() > 1) {
                strBuilder.append(curr + " ");
            }
            else {
                if (strBuilder.charAt(strBuilder.length()-1) == ' ') {
                    strBuilder.deleteCharAt(strBuilder.length()-1);
                }
                strBuilder.append(curr);
            }
        }
        magplaceView.setText(strBuilder.toString());
        magView.setText(mag);
    }
}
