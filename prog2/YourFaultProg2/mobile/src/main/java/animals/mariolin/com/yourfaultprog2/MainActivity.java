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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends Activity {

    private ListView eqList;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> listAdapter;
    MyReceiver receiver;
    private final static String TAG = "MainActivity";
    public final static String CLICK = "Click Message";
    public final static String COORDINATES = "c00rd1nat33";

    private HashMap<String, double[]> coordinateMap = new HashMap<String, double[]>();

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
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        eqList.setAdapter(listAdapter);

        eqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GPSActivity.class);
                String selectedFromList = (String) (eqList.getItemAtPosition(position));
                intent.putExtra(CLICK,selectedFromList);
                intent.putExtra(COORDINATES, coordinateMap.get(selectedFromList));
                startActivity(intent);
            }
        });

        Log.d("main", "start");
        Intent i = new Intent(this, USGSHelper.class);
        startService(i);
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

            String stringValue = mag + "M" + location;
            Log.v(TAG, stringValue);
            Log.v(TAG, Arrays.toString(coordinates));
            listItems.add(0, stringValue);
            coordinateMap.put(stringValue, coordinates);
            eqList.setAdapter(listAdapter);
        }
    }
}
