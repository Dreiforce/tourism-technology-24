package local.triforce.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import local.triforce.model.Lift;
import local.triforce.model.Slope;
import local.triforce.model.World;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static local.triforce.util.API.call;

public class UseRealAPI implements StateAPI {
    String base = "http://localhost:8000/";


    @Override
    public List<String> getStationNames() {
        List<String> res = new ArrayList<>();
        Iterator<JsonNode> iter = ((ArrayNode) call(base + "lift")).elements();
        while (iter.hasNext()) {
            res.add(iter.next().asText());
        }
        return res;
    }

    @Override
    public long getPeopleAt(long t, String station) {
        Timestamp timestamp = new Timestamp(t);
        Date date = new Date(timestamp.getTime());
        // 2024-03-03 07:00:00
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
        JsonNode res = call(base + "lift/events?current_time=" + simpleDateFormat.format(date) + "&time_window=15");
        return res.findPath(station).asLong(0);
    }

    @Override
    public World generateWorld(long timestamp) {
        HashMap<String, Long> peopleAt = new HashMap<>();
        getStationNames().forEach(stationName -> {
            peopleAt.put(stationName, getPeopleAt(timestamp, stationName));
        });
        World world = new World();
        world.setPeopleAtStation(peopleAt);
        world.setLifts(getLifts());
//        world.setLifts(List.of(
//                new Lift(100, "stationA", "Bergstation"), // Lift 1
//                new Lift(100, "stationB", "Bergstation") // Lift 2
//        ));
//        world.setSlopes(List.of(
//                new Slope(0, 0.3, 0.4, "Bergstation", "stationA"),
//                new Slope(0, 0.5, 0.6, "Bergstation", "stationB")
//        ));
        return world;
    }


    public List<Lift> getLifts() {
        List<Lift> res = new ArrayList<>();
        call(base + "lift/targets").elements().forEachRemaining(lift -> {
            res.add(new Lift(
                    lift.findPath("rate").asDouble(),
                    lift.findPath("from").asText(),
                    lift.findPath("to").asText()
            ));
        });
        return res;
    }
}
