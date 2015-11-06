package animals.mariolin.com.yourfaultprog2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Mario Lin on 10/12/15.
 */
public class PhotoActivity extends Activity {
    String instagramUrl = "https://api.instagram.com/v1/media/search?lat={lat}&lng={lng}&distance=3000&access_token=1931277428.2a3d32a.cd7944e7b1cf4a6f93ae0f7e692f5b0f";
    public final String TAG = "Photos";

    ArrayList<String> images = new ArrayList<String>();
    boolean shown = false;
    int count = 0;
    WebView webView;
    Button moreBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();

        double[] coords = intent.getDoubleArrayExtra("extra");
        instagramUrl = instagramUrl.replace("{lat}", String.valueOf(coords[1]));
        instagramUrl = instagramUrl.replace("{lng}", String.valueOf(coords[0]));
        Log.d(TAG, instagramUrl);

        webView = (WebView) findViewById(R.id.webView);
        fetchData();
        if (images.size() > count) {
            webView.loadUrl(images.get(count));
            count++;
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "No images to show";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        moreBtn = (Button) findViewById(R.id.more);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (count == images.size()) {
                    count = 0;
                }
                if (count < images.size()) {
                    webView.loadUrl(images.get(count));
                    count += 1;
                }
            }
        });
    }


    public void fetchData() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(instagramUrl);
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

                    JSONObject obj = new JSONObject(sb.toString());
                    JSONArray features = obj.getJSONArray("data");
                    JSONObject image;
                    JSONObject standard;
                    String link;
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject json = features.getJSONObject(i);
                        image = json.getJSONObject("images");
                        standard = image.getJSONObject("standard_resolution");
                        link = standard.getString("url");
//                        if (!shown) {
//                            Log.d(TAG, link.toString());
//                            webView.loadUrl(link.toString());
//                            shown = true;
//                        }
                        images.add(link.toString());

                    }

                } catch (IOException e) {
                    Log.d(TAG, "IO Exception");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d(TAG, "JSON Exception");
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

