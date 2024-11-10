package local.triforce.state;

import local.triforce.model.World;

import java.util.List;

public interface StateAPI {

    //TODO: api von jakob
    // these are just examples
    List<String> getStationNames();
    long getPeopleAt(long timestamp, String station);

    World generateWorld(long timestamp);
}
