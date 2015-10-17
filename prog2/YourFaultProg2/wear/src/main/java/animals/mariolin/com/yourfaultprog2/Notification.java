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
        setContentView(R.layout.notif);
//        first, let's extract that extra hour information we passed over
        Intent intent = getIntent();
        String data = intent.getStringExtra(WatchListenerService.DATA); //note this is case sensitive

        if (data != null) {
            //Programmatically set the text to the actual location and magnitude.
            TextView text = (TextView) findViewById(R.id.notifdisplay);
            text.setText(data);
        }
    }
}

