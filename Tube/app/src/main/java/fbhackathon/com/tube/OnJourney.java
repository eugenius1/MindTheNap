package fbhackathon.com.tube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fbhackathon.com.tube.MapData.Line;
import fbhackathon.com.tube.MapData.Station;

public class OnJourney extends AppCompatActivity {

    private Station current;
    private Station destination;
    private Line line;
    private TextView currentTextView;
    private TextView destinationTextView;
    private ListView stopsListView;
    private Button startJourneyButton;
    private Button resetPositionButton;
    private boolean direction;
    private List<String> stops = new ArrayList<>();
    private List<String> remainingStops = new ArrayList<>();
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_journey);
        line = (Line) getIntent().getSerializableExtra("line");
        current = (Station) getIntent().getSerializableExtra("start");
        destination = (Station) getIntent().getSerializableExtra("end");
        currentTextView = (TextView) findViewById(R.id.current_station);
        destinationTextView = (TextView) findViewById(R.id.end_station);
        stopsListView = (ListView) findViewById(R.id.remaining_stations);
        startJourneyButton = (Button) findViewById(R.id.start_journey_button);
        resetPositionButton = (Button) findViewById(R.id.reset_position_button);
        currentTextView.setText(current.getName());
        destinationTextView.setText(destination.getName());
        direction = line.findDirection(current, destination);
        makeListOfStops();

        startJourneyButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      Intent intent = new Intent(OnJourney.this, SpeechInputNewActivity.class);
                                                      String[] stopsArr = stops.toArray(new String[stops.size()]);
                                                      intent.putExtra("stops", stopsArr);
                                                      startActivity(intent);
                                                  }
                                              }
        );
        resetPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnJourney.this, ResetLocation.class);
                intent.putExtra("stops", stops.toArray(new String[stops.size()]));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String newStation = data.getStringExtra("newStation");
            resetCurrentStation(newStation);
        }
    }

    public void jumpToStation(String stationName) {
        if (stationName.equals(destination.getName())) {
            // TODO: tell the user to get off
            return;
        }
        for (int i = 0; i < remainingStops.size(); i++) {
            if (remainingStops.get(i).equals(stationName)) {
                remainingStops = remainingStops.subList(i + 1, remainingStops.size());
                break;
            }
        }
        updateCurrentStation(stationName);
        updateStopsListView(remainingStops);
    }

    public void resetCurrentStation(String stationName) {
        if (stationName.equals(destination.getName())) {
            Toast.makeText(this, "You can't set yourself at the destination", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).equals(stationName)) {
                remainingStops = stops.subList(i + 1, stops.size());
                break;
            }
        }
        updateCurrentStation(stationName);
        updateStopsListView(remainingStops);
    }

    private void updateCurrentStation(String stationName) {
        current = line.findStation(stationName);
        currentTextView.setText(current.getName());
    }

    private void makeListOfStops() {
        Station current = this.current;
        while (!line.getNext(current, direction).equals(destination)) {
            stops.add(line.getNext(current, direction).getName());
            current = line.getNext(current, direction);
        }
        stops.add(destination.getName());
        remainingStops = new ArrayList<>(stops);
        updateStopsListView(stops);
    }

    private void updateStopsListView(List<String> stations) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations);
        stopsListView.setAdapter(adapter);
    }


}
