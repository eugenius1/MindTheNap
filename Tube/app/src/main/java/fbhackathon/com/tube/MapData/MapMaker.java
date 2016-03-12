package fbhackathon.com.tube.MapData;


import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MapMaker {

    static TubeMap londonMap;

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
