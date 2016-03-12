package fbhackathon.com.tube.MapData;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import fbhackathon.com.tube.OnJourney;
import fbhackathon.com.tube.R;

public class MapMaker extends Activity {

    static TubeMap londonMap;
    private Spinner lineSelector;
    private Spinner startStationSelector;
    private Spinner endStationSelector;
    private Button startJourney;
    private Line line;
    private Station startStation;
    private Station endStation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapmaker);

        lineSelector = (Spinner) findViewById(R.id.line_selector);
        startStationSelector = (Spinner) findViewById(R.id.start_station_selector);
        endStationSelector = (Spinner) findViewById(R.id.end_station_selector);
        startJourney = (Button) findViewById(R.id.start_journey);

        try {
            londonMap = parse(getResources().openRawResource(R.raw.victoria));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<String> lineNames = londonMap.getAllLineNames();
        startStationSelector.setClickable(false);
        endStationSelector.setClickable(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lineNames);
        lineSelector.setAdapter(adapter);
        lineSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                line = londonMap.getLine(lineNames.get(position));
                List<String> stationNames = line.getAllStationNames();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapMaker.this, R.layout.support_simple_spinner_dropdown_item, stationNames);
                startStationSelector.setClickable(true);
                endStationSelector.setClickable(true);
                startStationSelector.setAdapter(adapter);
                endStationSelector.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                startStationSelector.setClickable(false);
                endStationSelector.setClickable(false);
            }
        });
        startStationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startStation = line.findStation(line.getAllStationNames().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        endStationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endStation = line.findStation(line.getAllStationNames().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startStation.equals(endStation)) {
                    throw new RuntimeException("TODO: Handle this!");
                }
                Intent intent = new Intent(MapMaker.this, OnJourney.class);
                intent.putExtra("line", line);
                intent.putExtra("start", startStation);
                intent.putExtra("end", endStation);
                startActivity(intent);
            }
        });

    }


    public static TubeMap parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            return read(parser);
        } finally {
            in.close();
        }
    }

    private static TubeMap read(XmlPullParser parser) throws XmlPullParserException, IOException {
        int event;
        String text = null;
        String name;
        Stack<String> names = new Stack<>();
        List<Station> stations = new ArrayList<>();
        Map<String, Line> lines = new HashMap<>();
        try {
            event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "name":
                                names.push(text);
                                break;
                            case "station":
                                stations.add(new Station(names.pop()));
                                break;
                            case "line":
                                String lineName = names.pop();
                                lines.put(lineName, new Line(lineName, stations));
                                stations = new ArrayList<>();
                                break;
                            case "tubeMap":
                                return new TubeMap(names.pop(), lines);
                            default:
                                throw new RuntimeException("Invalid XML");
                        }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Should not reach here");
    }
}
