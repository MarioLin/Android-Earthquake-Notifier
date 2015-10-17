package animals.mariolin.com.yourfaultprog2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    Map<String, String> states = new HashMap<String, String>();

    private ListView eqList;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> listAdapter;
    MyReceiver receiver;
    private final static String TAG = "MainActivity";
    public final static String CLICK = "Click Message";
    public final static String COORDINATES = "c00rd1nat33";
    private static final String START_ACTIVITY = "/start_activity";

    private HashMap<String, double[]> coordinateMap = new HashMap<String, double[]>();
    GoogleApiClient mApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Register BroadcastReceiver
        //to receive event from our service
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(USGSHelper.ACTION);
        registerReceiver(receiver, intentFilter);
        eqList = (ListView) findViewById(R.id.eqlist);
        listAdapter = new AlternateColorAdapter(this, android.R.layout.simple_list_item_1, listItems);
        eqList.setAdapter(listAdapter);

        eqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GPSActivity.class);
                String selectedFromList = (String) (eqList.getItemAtPosition(position));
                intent.putExtra(CLICK, selectedFromList);
                intent.putExtra(COORDINATES, coordinateMap.get(selectedFromList));
                String[] s = selectedFromList.split(" ", 2);
                mApiClient.connect();
                sendMessage(START_ACTIVITY, s[1] +  "\n" + s[0]);
                startActivity(intent);
            }
        });


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


        Log.d("main", "start");
        Intent i = new Intent(this, USGSHelper.class);
        startService(i);


        //Or start the fast service.
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mApiClient.connect();
                double[] dummyCoord = {-122.4, 37.8};
                String dummyEq = "9M San Francisco, CA";
                coordinateMap.put(dummyEq, dummyCoord);
                listItems.add(0, dummyEq);
                eqList.setAdapter(listAdapter);
                mApiClient.connect();
                sendMessage(START_ACTIVITY, "San Francisco, CA" + "\n" + "9M"); //actually send the message to the watch
            }
        });

        //Or start the fast service.
        final Button refresh = (Button) findViewById(R.id.ref);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), USGSHelper.class);
                //we call getBaseContext instead of 'this' since this is not in the main
                //activity but rather in an onclick.
                startService(i);
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            Double mag = arg1.getDoubleExtra(USGSHelper.MAGNITUDE, 0);
            String place = arg1.getStringExtra(USGSHelper.PLACE);
            double[] coordinates = arg1.getDoubleArrayExtra(USGSHelper.COORDINATES);


            String location = place.split("of")[1];
            String[] loc = location.split(" ");
            StringBuilder strBuilder = new StringBuilder();
            Log.d(TAG, location);
            Log.d(TAG, Arrays.toString(loc));
            for (int i = 0; i < loc.length; i++) {
                String curr = loc[i];
                if (states.containsKey(curr)) {
                    strBuilder.append(states.get(curr));
                }
                else if (curr.length() > 1) {
                    strBuilder.append(curr + " ");
                }
                else {
                    continue;
                }
            }

            String stringValue = mag + "M " + strBuilder.toString();
//            Log.v(TAG, stringValue);
//            Log.v(TAG, Arrays.toString(coordinates));
            Log.d(TAG, stringValue);
            if (!listItems.contains(stringValue)) {
                if (listItems.size() < 10) {
                    listItems.add(stringValue);
                } else {
                    listItems.add(0, stringValue);
                }
            }
            coordinateMap.put(stringValue, coordinates);
            eqList.setAdapter(listAdapter);
            mApiClient.connect();
            sendMessage(START_ACTIVITY, strBuilder.toString() + "\n" + String.valueOf(mag) + "M"); //actually send the message to the watch

        }
    }
    //How to send a message to the WatchListenerService
    private void sendMessage( final String path, final String text ) {
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

