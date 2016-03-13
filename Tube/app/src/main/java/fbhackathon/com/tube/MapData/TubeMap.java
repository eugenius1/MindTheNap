package fbhackathon.com.tube.MapData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TubeMap {

    private final String name;
    private final Map<String, Line> lines;

    public TubeMap(String name, Map<String, Line> lines) {
        this.name = name;
        this.lines = lines;
    }

    public Line getLine(String name) throws NoSuchElementException {
        Line line = lines.get(name);
        if (line != null) {
            return line;
        } else {
            throw new NoSuchElementException();
        }
    }

    public List<String> getAllLineNames() {
        List<String> names = new ArrayList<>(lines.keySet());
        Collections.sort(names);
        return names;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" Tube Map\n");
        for (Line l : lines.values()) {
            sb.append(l.toString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
