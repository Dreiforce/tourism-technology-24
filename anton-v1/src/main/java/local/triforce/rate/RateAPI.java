package local.triforce.rate;

import java.util.Map;

public interface RateAPI {

    // example rate, rates how well a state is by measuring the people queued at each station, 0 is bad, 1 is good
    double rate(Map<String, Long> peoplePerStation);


    // rates a series of rates over time, 0 is bad, 1 is good
    double metaRate(Map<Long, Double> rates);
}
