package fbhackathon.com.tube;

import android.content.Context;
import android.util.Log;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

import fbhackathon.com.tube.MapData.TubeMap;

/**
 * Created by Chaiyong on 3/12/16.
 */
public class FuzzyStringMatcher {

    private String[] stations;
    private MapMaker mapMaker = new MapMaker();
    private TubeMap tubeMap;

    public FuzzyStringMatcher(Context context, String[] stations) {
       /* try {
            tubeMap = mapMaker.parse(context.getResources().openRawResource(R.raw.victoria));
            List<String> lineNames = tubeMap.getAllLineNames();
            for (int i=0; i<lineNames.size(); i++) {
                Line line = tubeMap.getLine(lineNames.get(i));
                stations.addAll(line.getAllStationNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        this.stations = stations;
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
        for (int i = 0; i < stations.length; i++) {
            Log.d("stations", stations[i]);
            int diff = checkSoundexDifference(s1, stations[i]);
            if (diff > max) {
                max = diff;
                bestMatch = stations[i];
            }
        }

        if (max > 2) {
            return bestMatch;
        } else {
            return "not_found";
        }
    }
}
