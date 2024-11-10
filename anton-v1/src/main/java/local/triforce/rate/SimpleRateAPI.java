package local.triforce.rate;

import java.util.Map;

public class SimpleRateAPI implements RateAPI {

    /**
     * TODO: improve
     * https://www.geogebra.org/solver/en/results/-%5Cleft%7Cx%20-%2010%5Cright%7C%20%252B%2010
     *
     * @param name
     * @param people
     * @return
     */
    private double rateStation(String name, Long people) {
        double width = 100;
        double peakAt = 50;
        return Math.max(0, width - Math.abs(people - peakAt)) / width;
    }


    /**
     * TODO improve
     *
     * @return
     */
    @Override
    public double rate(Map<String, Long> peoplePerStation) {
        return peoplePerStation.entrySet().stream()
                .mapToDouble(entry -> rateStation(entry.getKey(), entry.getValue()))
                .average().orElse(0);
    }

    /**
     * TODO improve
     *
     * @param rates
     * @return
     */
    @Override
    public double metaRate(Map<Long, Double> rates) {
        return rates.values().stream().mapToDouble(a -> a).average().orElse(0);
    }
}
