package local.triforce.anton;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.triforce.model.World;
import local.triforce.rate.RateAPI;
import local.triforce.rate.SimpleRateAPI;
import local.triforce.sim.SimpleSimExample;
import local.triforce.sim.SimAPI;
import local.triforce.state.MockStateAPI;
import local.triforce.state.StateAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.random;

public class Anton implements Runnable {

    private RateAPI rate = new SimpleRateAPI();
    private SimAPI sim = new SimpleSimExample();
    private StateAPI state = new MockStateAPI();
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new Anton().run();
    }

    @Override
    public void run() {

        new File("output").mkdir();

        long consecutiveFails = 0;
        double best = 0;
        List<String> bestIdeas = null;
        long t = System.currentTimeMillis();//TODO;

        long id = 0;
        while (true) {
            id++;
            World world = state.generateWorld(t);
            Map<Long, Double> rates = new TreeMap<>();

            //apply ideas
            List<String> ideas = new ArrayList<>();
            double skew = Math.random() * 0.4 - 0.2;
            if (skew < 0) {
                ideas.add("Adding incentive to go to stationA with x = " + -skew);
            } else {
                ideas.add("Adding incentive to go to stationB with x = " + skew);
            }
            world.getSlopes().stream().filter(s -> s.getTo().equals("stationA")).findAny().ifPresent(
                    slope -> slope.setFunlevel(slope.getFunlevel() - skew)
            );
            world.getSlopes().stream().filter(s -> s.getTo().equals("stationB")).findAny().ifPresent(
                    slope -> slope.setFunlevel(slope.getFunlevel() + skew)
            );

            try {
                PrintWriter out = new PrintWriter(new FileOutputStream("output/run" + id + ".csv"));


//simulate for a bit
                int rounds = 100;
                for (int i = 0; i < rounds; i++) {
                    world = sim.step(world);
                    rates.put(t + i, rate.rate(world.getPeopleAtStation()));

                    if(i == 0) {
                        out.println(world.getPeopleAtStation().keySet().stream().reduce((a,b) -> a+";"+b).orElse("no data"));
                    }

                    String csv = world.getPeopleAtStation().entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(station -> station.getValue().toString())
                            .reduce((a, b) -> a + ";" + b).orElse("[no data]");
                    out.println(csv);
//                System.out.printf("sim step for (%d): %s\n", id, csv);
                }
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            double metaRate = rate.metaRate(rates);
            if (metaRate > best) {
                best = metaRate;
                bestIdeas = ideas;
                System.out.printf("found better ideas(id=%d) with rate %f, ideas: %s\n", id,  best, bestIdeas);
                consecutiveFails = 0;
            } else {
                consecutiveFails++;
            }
            if (consecutiveFails > 100) {
                System.out.printf("giving up, best idea with rate %f is: %s\n", best, bestIdeas);
                break;
            }
        }
    }

    public List<String> generateIdeas() {
        return List.of("give 4 people gutschein for coffee at place x");
    }

}