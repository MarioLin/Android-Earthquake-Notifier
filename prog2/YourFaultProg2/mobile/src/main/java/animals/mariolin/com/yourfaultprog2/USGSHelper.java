package animals.mariolin.com.yourfaultprog2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

/**
 * Created by Mario Lin on 10/12/15.
 */
public class USGSHelper extends Service{
    private GoogleApiClient mApiClient;
    private String USGSUrl = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2015-10-11&endtime=2015-10-12&eventtype=earthquake&orderby=time&limit=5";
    private String response = "";
    private final String TAG = "USGSService";

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
                    //Scanners scan line by line: if your response is longer than 1 line, you need a loop
                    while (scanner.hasNext()) {
                        response += scanner.nextLine(); //parses the GET request into a string
                    }

                    // parse JSON response

                    JSONObject obj = new JSONObject(response);
                    JSONArray features = obj.getJSONArray("features");
                    for (int i = 0; i< features.length(); i ++) {
                        continue;
                    }

                } catch (IOException e) {
                    Log.v(TAG, "IO Exception");
                    e.printStackTrace();
                } catch(JSONException e) {
                    Log.v(TAG, "JSON Exception");
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
}
