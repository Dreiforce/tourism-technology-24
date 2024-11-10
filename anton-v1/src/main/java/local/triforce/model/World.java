package local.triforce.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {

    private Map<String, Long> peopleAtStation = new HashMap<>();
    private List<Lift> lifts = new ArrayList<>();
    private List<Slope> slopes = new ArrayList<>();

    public Map<String, Long> getPeopleAtStation() {
        return peopleAtStation;
    }

    public void setPeopleAtStation(Map<String, Long> peopleAtStation) {
        this.peopleAtStation = peopleAtStation;
    }

    public List<Lift> getLifts() {
        return lifts;
    }

    public void setLifts(List<Lift> lifts) {
        this.lifts = lifts;
    }

    public World clone() {
        //TODO: maybe switch to js?
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(this), World.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Slope> getSlopes() {
        return slopes;
    }

    public void setSlopes(List<Slope> slopes) {
        this.slopes = slopes;
    }
}
