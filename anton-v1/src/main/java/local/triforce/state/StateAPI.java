package local.triforce.state;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public interface StateAPI {

    //TODO: api von jakob
    // these are just examples
    List<String> getStationNames();
    long getPeopleAt(long timestamp, String station);

    String generateWorld();
}
