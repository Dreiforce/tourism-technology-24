package local.triforce.anton;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.triforce.rate.RateAPI;
import local.triforce.rate.SimpleRateAPI;
import local.triforce.sim.ActorSimAPI;
import local.triforce.sim.SimAPI;
import local.triforce.state.MockStateAPI;
import local.triforce.state.StateAPI;
import org.json.JSONObject;

import java.util.*;

public class Anton implements Runnable {

    private RateAPI rate = new SimpleRateAPI();
    private SimAPI sim = new ActorSimAPI();
    private StateAPI state = new MockStateAPI();
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new Anton().run();
    }

    @Override
    public void run() {

        long consecutiveFails = 0;
        double best = 0;
        List<String> bestIdeas = null;

        while (true) {
            List<String> ideas = generateIdeas();
            String world = state.generateWorld();
            Map<Long, Double> rates = new TreeMap<>();


            int rounds = 100;
            for (int i = 0; i < rounds; i++) {
                world = sim.step(world, ideas);
                rates.put(/*todo: how to deal with time=*/System.currentTimeMillis() + i, rate.rate(extractPeoplePerStation(world)));
            }

            double metaRate = rate.metaRate(rates);
            if (metaRate > best) {
                best = metaRate;
                bestIdeas = ideas;
                System.out.printf("found better ideas with rate %f, ideas: %s\n", best, bestIdeas);
                consecutiveFails = 0;
            } else {
                consecutiveFails++;
            }
            if(consecutiveFails > 1000) {
                System.out.println("too many fails, giving up, best result is as stated above");
                break;
            }
        }
    }

    private Map<String, Long> extractPeoplePerStation(String world) {
        //TODO: implement
        return Map.of("key", 10L, "other", 20L);
    }

    public List<String> generateIdeas() {
        return List.of("give 4 people gutschein for coffee at place x");
    }

}