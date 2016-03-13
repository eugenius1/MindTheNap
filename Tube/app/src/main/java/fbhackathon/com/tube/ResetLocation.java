package fbhackathon.com.tube;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class ResetLocation extends AppCompatActivity {

    private Button resetLocationButton;
    private Spinner resetStopsListView;
    private String newStation;
    private String[] stops;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_location);
        resetLocationButton = (Button) findViewById(R.id.reset_location_button);
        resetStopsListView = (Spinner) findViewById(R.id.reset_stops);
        stops = getIntent().getStringArrayExtra("stops");
        updateStopsListView(stops);
        resetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("newStation", newStation);
                setResult(RESULT_OK, output);
                finish();
            }
        });
        resetStopsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newStation = stops[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateStopsListView(String[] stations) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations);
        resetStopsListView.setAdapter(adapter);
    }

}
