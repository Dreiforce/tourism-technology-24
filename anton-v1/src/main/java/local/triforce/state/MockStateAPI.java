package local.triforce.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.triforce.model.World;

import java.util.List;

public class MockStateAPI implements StateAPI {



    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<String> getStationNames() {
        return List.of();
    }

    @Override
    public long getPeopleAt(long timestamp, String station) {
        return 0;
    }

    @Override
    public String generateWorld() {
        try {
            return mapper.writeValueAsString(new World());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
