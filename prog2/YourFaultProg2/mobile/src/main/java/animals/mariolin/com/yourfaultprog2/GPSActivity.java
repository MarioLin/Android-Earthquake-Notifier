package animals.mariolin.com.yourfaultprog2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.common.api.Status;

import java.util.Arrays;

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
    private LatLng CALIFORNIA = new LatLng(37, -120);

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
        Intent intent = getIntent();
        magPlace = intent.getStringExtra(MainActivity.CLICK);
        coordinates = intent.getDoubleArrayExtra(MainActivity.COORDINATES);
        Log.d(TAG, magPlace);
        Log.d(TAG, Arrays.toString(coordinates));
        // Maps
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gMap = mapFragment.getMap();
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
    }

    @Override
    public void onMapReady(GoogleMap map) {

        // Instantiates a new CircleOptions object and defines the center and radius
        LatLng coord = new LatLng(coordinates[1], coordinates[0]);
        Log.d(TAG, coordinates[1] + ", " + coordinates[0]);
        CircleOptions circleOptions = new CircleOptions()
                .center(coord)
                .radius(8500)
                .fillColor(0x30ff0000)
                .strokeColor(Color.RED).strokeWidth((float) 2.5); // In meters
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Get back the mutable Circle

        gMap.addMarker(new MarkerOptions()
                .position(coord));
        gMap.clear();
        Circle circle = gMap.addCircle(circleOptions);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
    }
}
