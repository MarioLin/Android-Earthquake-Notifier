package animals.mariolin.com.youanimal;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AnimalView extends Activity {
    private Spinner compareSpinner;
    private Spinner compareToSpinner;
    private Button convertButton;
    private TextView convertedText;
    private EditText yearsField;
    private final AnimalConversions animalConversions = new AnimalConversions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_view);

        compareSpinner = (Spinner) findViewById(R.id.compareSpinner);
        compareToSpinner = (Spinner) findViewById(R.id.secondSpinner);
        convertButton = (Button) findViewById(R.id.convertButton);
        convertedText = (TextView) findViewById(R.id.convertedYearsTextLabel);
        yearsField = (EditText) findViewById(R.id.yearsField);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConvertedText();
            }
        });

        compareSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
        compareToSpinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_animal_view, menu);
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

    public void displayConvertedText() {
        int years = Integer.parseInt(yearsField.getText().toString());
        String firstAnimal = (String) compareSpinner.getSelectedItem();
        String secondAnimal = (String) compareToSpinner.getSelectedItem();

        int[] time = animalConversions.convert(firstAnimal,secondAnimal,years);

        String source = years + " " + firstAnimal + " years converts to: <br>" + "<b>" + time[0] + " year(s) and " + time[1] + " month(s)</b> " + "<br>in " + secondAnimal + " age.";

        convertedText.setText(Html.fromHtml(source));
    }

}
