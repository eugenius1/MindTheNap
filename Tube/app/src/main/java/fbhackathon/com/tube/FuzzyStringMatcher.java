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

    public MatchResult findBestStationMatch(String s1) {
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
        MatchResult mr = new MatchResult();

        if (max > 2) {
            mr.confidence = max;
            mr.match = bestMatch;
        } else {
            mr.confidence = 0;
            mr.match = "";
        }
        return mr;
    }

    public String findBestSentenceMatch(ArrayList<String> arr) {
        MatchResult bestResult = new MatchResult();
        bestResult.confidence = 0;
        bestResult.match = "no_result";
        for(String sentence : arr) {
            String[] sent = sentence.split("\\s+");
            for(String word : sent) {
                MatchResult mr = findBestStationMatch(word.toLowerCase());
                if(mr.confidence > bestResult.confidence) {
                     bestResult = mr;
                }
            }
            for(int i = 0; i<sent.length - 1;i++) {
                String word = sent[i] + " " + sent[i+1];
                MatchResult mr = findBestStationMatch(word.toLowerCase());
                if(mr.confidence > bestResult.confidence) {
                    bestResult = mr;
                }
            }

        }

        return bestResult.match;
    }

    public class MatchResult {
        String match;
        int confidence;
    }
}
