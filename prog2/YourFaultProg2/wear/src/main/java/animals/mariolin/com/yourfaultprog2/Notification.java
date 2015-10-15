package animals.mariolin.com.yourfaultprog2;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Mario Lin on 10/14/15.
 */
public class Notification extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //first, let's extract that extra hour information we passed over
        Intent intent = getIntent();
        String hour = intent.getStringExtra("Magnitude"); //note this is case sensitive

        Log.v("DEBUG", hour);
    }
}

