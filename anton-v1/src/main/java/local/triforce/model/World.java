package local.triforce.model;

import java.util.Map;

public class World {

    private Map<String, Long> peopleAtStation;

    public Map<String, Long> getPeopleAtStation() {
        return peopleAtStation;
    }

    public void setPeopleAtStation(Map<String, Long> peopleAtStation) {
        this.peopleAtStation = peopleAtStation;
    }
}
