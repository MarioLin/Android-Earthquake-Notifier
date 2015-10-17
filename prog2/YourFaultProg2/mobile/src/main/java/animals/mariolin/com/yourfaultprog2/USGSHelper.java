package animals.mariolin.com.yourfaultprog2;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.json.*;

/**
 * Created by Mario Lin on 10/12/15.
 */
public class USGSHelper extends Service{
    private GoogleApiClient mApiClient;
    private String USGSUrl = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=10";
    private String response = "";
    private final String TAG = "USGSService";
    public static final String ACTION = "ACTION";
    double[] coordinates = new double[3];
    private static final String START_ACTIVITY = "/start_activity";

    // Intent unique strings
    public static final String MAGNITUDE = "USGSMAG";
    public static final String PLACE = "usgsPl4c3";
    public static final String COORDINATES = "c00rdinat3s";


    @Override
    public void onCreate() {
        Log.v(TAG, "creating");
        super.onCreate();

        /* Initialize the googleAPIClient for message passing */
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addApi( LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        /* Successfully connected */
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Kick off new work to do
        fetchData();
        return START_STICKY;
    }

    public void fetchData() {
        Log.v(TAG, "start fetch");
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(USGSUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                HttpURLConnection urlConnection = null;


                try {

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    StringBuilder sb = new StringBuilder();
                    Log.v(TAG, "scan");

                    //Scanners scan line by line: if your response is longer than 1 line, you need a loop
                    while (scanner.hasNext()) {
                        sb.append(scanner.nextLine()); //parses the GET request into a string
                    }

//                    Log.v(TAG, sb.toString());

                    // parse JSON response
                    JSONObject obj = new JSONObject(sb.toString());
                    JSONArray features = obj.getJSONArray("features");
                    JSONObject properties;
                    JSONObject geometry;
                    double magnitude;
                    String place;
                    JSONArray jsonCoordinates;

                    for (int i = 0; i< features.length(); i ++) {
                        JSONObject earthquake = features.getJSONObject(i);
//                        Log.d(TAG + " JSONObject", earthquake.toString());

                        properties = earthquake.getJSONObject("properties");
                        geometry = earthquake.getJSONObject("geometry");

                        magnitude = properties.getDouble("mag");
                        place = properties.getString("place");

//                        Log.d(TAG, String.valueOf(magnitude));
//                        Log.d(TAG, place);

                        jsonCoordinates = geometry.getJSONArray("coordinates");

                        for (int j = 0; j < jsonCoordinates.length();j++) {
                            coordinates[j] = jsonCoordinates.getDouble(j);
                        }

                        Intent intent = new Intent();
                        intent.setAction(ACTION);
                        intent.putExtra(MAGNITUDE, magnitude);
                        intent.putExtra(PLACE, place);
                        intent.putExtra(COORDINATES, coordinates);
                        sendBroadcast(intent);
//                        Log.d(TAG, Arrays.toString(coordinates));

                    }

                } catch (IOException e) {
                    Log.d(TAG, "IO Exception");
                    e.printStackTrace();
                } catch(JSONException e) {
                    Log.d(TAG, "JSON Exception");
                    e.printStackTrace();
                }finally
                 {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //how to send a message to the watchlistenerservice
    private void sendMessage( final String path, final String text ) {
        Log.d(TAG, "sending message");
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

}
