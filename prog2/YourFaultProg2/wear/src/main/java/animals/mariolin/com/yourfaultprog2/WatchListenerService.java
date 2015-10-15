package animals.mariolin.com.yourfaultprog2;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Mario Lin on 10/14/15.
 */
public class WatchListenerService extends WearableListenerService {
    private static final String START_ACTIVITY = "/start_activity";
    private final String TAG = "WatchListener";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "receicved");
        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Notification.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("Magnitude", value); //propagate over the magnitude
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}