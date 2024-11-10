package local.triforce.state;

import local.triforce.model.Lift;
import local.triforce.model.Slope;
import local.triforce.model.World;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MockStateAPI implements StateAPI {

    @Override
    public List<String> getStationNames() {
        return List.of("stationA", "stationB", "Bergstation");
    }

    @Override
    public long getPeopleAt(long timestamp, String station) {
        //TODO: use actual data; for new deterministic random values
        Random rand = new Random(timestamp);
        for (int i = 0; i < station.hashCode() % 10; i++) {
            rand.nextInt();
        }
        return rand.nextInt(10, 100);
    }

    @Override
    public World generateWorld(long timestamp) {
        HashMap<String, Long> peopleAt = new HashMap<>();
        getStationNames().forEach(stationName -> {
            peopleAt.put(stationName, getPeopleAt(timestamp, stationName));
        });

        World world = new World();
        world.setPeopleAtStation(peopleAt);

        world.setLifts(List.of(
                new Lift(100, "stationA", "Bergstation"), // Lift 1
                new Lift(100, "stationB", "Bergstation") // Lift 2
        ));


        world.setSlopes(List.of(
                new Slope(0, 0.3, 0.4, "Bergstation", "stationA"),
                new Slope(0, 0.5, 0.6, "Bergstation", "stationB")
        ));

        return world;
    }
}
