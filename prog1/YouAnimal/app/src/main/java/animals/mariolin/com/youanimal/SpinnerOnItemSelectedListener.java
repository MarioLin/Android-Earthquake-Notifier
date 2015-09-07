package animals.mariolin.com.youanimal;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Mario Lin on 9/7/15.
 */
public class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
        Log.v("Debug", parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
