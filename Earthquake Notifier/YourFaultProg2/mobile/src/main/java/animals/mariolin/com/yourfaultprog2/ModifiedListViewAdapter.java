package animals.mariolin.com.yourfaultprog2;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Created by Mario Lin on 10/12/15.
 */


public class ModifiedListViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;

    public ModifiedListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.columns, null);

            txtFirst=(TextView) convertView.findViewById(R.id.magnitude);
            txtSecond=(TextView) convertView.findViewById(R.id.location);


        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(Constants.FIRST_COLUMN));
        txtSecond.setText(map.get(Constants.SECOND_COLUMN));

        return convertView;
    }

}