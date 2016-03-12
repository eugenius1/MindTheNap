package fbhackathon.com.tube;

import android.content.Context;
import android.util.Log;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

import java.util.ArrayList;
import java.util.List;

import fbhackathon.com.tube.MapData.Line;
import fbhackathon.com.tube.MapData.MapMaker;
import fbhackathon.com.tube.MapData.TubeMap;

/**
 * Created by Chaiyong on 3/12/16.
 */
public class FuzzyStringMatcher {

    private List<String> stations = new ArrayList<String>();
    private MapMaker mapMaker = new MapMaker();
    private TubeMap tubeMap;

    public FuzzyStringMatcher(Context context) {
        try {
            tubeMap = mapMaker.parse(context.getResources().openRawResource(R.raw.victoria));
            List<String> lineNames = tubeMap.getAllLineNames();
            for (int i=0; i<lineNames.size(); i++) {
                Line line = tubeMap.getLine(lineNames.get(i));
                stations.addAll(line.getAllStationNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int checkSoundexDifference(String s1, String s2) {
        Soundex soundex = new Soundex();
        int diff = 0;
        try {
            diff = soundex.difference(s1, s2);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public String findBestMatch(String s1) {
        int max = 0;
        String bestMatch = "";
        for (int i = 0; i < stations.size(); i++) {
            Log.d("stations", stations.get(i));
            int diff = checkSoundexDifference(s1, stations.get(i));
            if (diff > max) {
                max = diff;
                bestMatch = stations.get(i);
            }
        }

        if (max > 2) {
            return bestMatch;
        } else {
            return "not_found";
        }
    }
}
