package fbhackathon.com.tube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import fbhackathon.com.tube.MapData.Line;
import fbhackathon.com.tube.MapData.Station;
import fbhackathon.com.tube.SoundReplayService.SoundReplayService;

public class OnJourney extends AppCompatActivity {

    private Station current;
    private Station destination;
    private Line line;
    private TextView currentTextView;
    private TextView destinationTextView;
    private TextView youAreNowAt;
    private ListView stopsListView;
    private Button startJourneyButton;
    private Button resetPositionButton;
    private boolean direction;
    private List<String> stops = new ArrayList<>();
    String[] stopsArr;
    private List<String> remainingStops = new ArrayList<>();
    private static final int REQUEST_CODE = 1;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_journey);
        line = (Line) getIntent().getSerializableExtra("line");
        current = (Station) getIntent().getSerializableExtra("start");
        destination = (Station) getIntent().getSerializableExtra("end");
        currentTextView = (TextView) findViewById(R.id.current_station);
        destinationTextView = (TextView) findViewById(R.id.end_station);
        youAreNowAt = (TextView) findViewById(R.id.textView4);
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
//                                                      Intent intent = new Intent(OnJourney.this, ProgressPageActivity.class);
//                                                      String[] stopsArr = stops.toArray(new String[stops.size()]);
//                                                      intent.putExtra("stops", stopsArr);
//                                                      startActivity(intent);
                                                      startJourneyButton.setVisibility(View.INVISIBLE);
                                                      resetPositionButton.setVisibility(View.INVISIBLE);
                                                      callSpeechRecognition();
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

    private void callSpeechRecognition() {
        stopsArr = new String[stops.size()];
        stopsArr = stops.toArray(stopsArr);
        Intent intent = new Intent(OnJourney.this, SpeechInputNewActivity.class);
        intent.putExtra("stops", stopsArr);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String newStation = data.getStringExtra("newStation");
            resetCurrentStation(newStation);
        } else if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String resultString = data.getStringExtra("stationName");
                jumpToStation(resultString);
                if (data.hasExtra("result")) {
                    String spokenText = data.getStringExtra("result");
                    Log.d("SpokenText", spokenText);
                    Intent tts = new Intent(this, SoundReplayService.class);
                    tts.setData(Uri.parse("file://tubeApp/" + spokenText));
                    startService(tts);
                }
                if (!resultString.equals(destination.getName())) {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            callSpeechRecognition();
                        }
                    }, 3000);
                }
            } else {
                //currentTextView.setText("N/A");
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        callSpeechRecognition();
                    }
                }, 3000);
            }
        }
    }

    public void jumpToStation(String stationName) {
        if (stationName.equals(destination.getName())) {
            // TODO: tell the user to get off
            currentTextView.setText("You've arrived at");
            stopsListView.setVisibility(View.INVISIBLE);
            youAreNowAt.setVisibility(View.INVISIBLE);
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
        try {
            current = line.findStation(stationName);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
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
