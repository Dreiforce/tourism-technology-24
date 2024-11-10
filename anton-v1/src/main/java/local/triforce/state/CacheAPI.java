package local.triforce.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.triforce.model.World;

import java.util.HashMap;
import java.util.List;

public class CacheAPI implements StateAPI {
    private final StateAPI sink;
    List<String> nameCache = null;
    HashMap<String, Long> tCache = new HashMap<>();
    HashMap<String, String> worldCache = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    public CacheAPI(StateAPI sink) {
        this.sink = sink;
    }

    @Override
    public List<String> getStationNames() {
        if (nameCache == null) {
            nameCache = sink.getStationNames();
        }
        return nameCache;
    }

    @Override
    public long getPeopleAt(long timestamp, String station) {
        String key = station + "-" + timestamp;
        if (!tCache.containsKey(key)) {
            tCache.put(key, sink.getPeopleAt(timestamp, station));
        }
        return tCache.get(key);
    }

    @Override
    public World generateWorld(long timestamp) {
        String key = "world-" + timestamp;
        if (!worldCache.containsKey(key)) {
            try {
                worldCache.put(key, mapper.writeValueAsString(sink.generateWorld(timestamp)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return mapper.readValue(worldCache.get(key), World.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
