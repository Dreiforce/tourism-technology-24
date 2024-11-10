package local.triforce.anton;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.triforce.model.Slope;
import local.triforce.model.World;
import local.triforce.rate.RateAPI;
import local.triforce.rate.SimpleRateAPI;
import local.triforce.sim.SimpleSimExample;
import local.triforce.sim.SimAPI;
import local.triforce.state.CacheAPI;
import local.triforce.state.MockStateAPI;
import local.triforce.state.StateAPI;
import local.triforce.state.UseRealAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.random;

public class Anton implements Runnable {
    private final int rounds = 10;
    private RateAPI rate = new SimpleRateAPI();
    private SimAPI sim = new SimpleSimExample();
    private StateAPI state = new CacheAPI(new UseRealAPI());
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
        Random random = new Random();

        // 2024-03-03 07:00:00
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long t = 0;
        try {
            t = simpleDateFormat.parse("2024-03-03 08:00:00").getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<Long>> csv = new TreeMap<>();


        List<String> stationNames = state.getStationNames();

        long id = 0;
        while (true) {
            id++;
            String prefix = "" + id;
            World world = state.generateWorld(t);
            Map<Long, Double> rates = new TreeMap<>();
            Map<String, List<Long>> tmpcsv = new HashMap<>();


            //apply ideas
            List<String> ideas = new ArrayList<>();
            Slope s = new Slope(
                    0, 10, 1,
                    stationNames.get(random.nextInt(stationNames.size())),
                    stationNames.get(random.nextInt(stationNames.size()))
            );
            world.getSlopes().add(s);
            ideas.add("adding incentive to go from " + s.getFrom() + " -> " + s.getTo());


//simulate for a bit
            for (int i = 0; i < rounds; i++) {
                world = sim.step(world);
                rates.put(t + i * 1000 * 60 * 15, rate.rate(world.getPeopleAtStation()));


                world.getPeopleAtStation().forEach((key, v) -> {
                    String finalkey = prefix + "/" + key;
                    List<Long> l = tmpcsv.getOrDefault(finalkey, new ArrayList<Long>());
                    l.add(v);
                    tmpcsv.put(finalkey, l);
                });
//                System.out.printf("sim step for (%d): %s\n", id, csv);
            }

            double metaRate = rate.metaRate(rates);
            if (metaRate > best) {
                best = metaRate;
                bestIdeas = ideas;
                csv.putAll(tmpcsv);
                System.out.printf("found better ideas(id=%d) with rate %f, ideas: %s\n", id, best, bestIdeas);
                consecutiveFails = 0;
            } else {
                consecutiveFails++;
            }
            if (consecutiveFails > 100) {
                System.out.printf("giving up, best idea with rate %f is: %s\n", best, bestIdeas);
                break;
            }
        }


        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("output/resultvalues.csv"));

            for (int i = 0; i < rounds; i++) {
                if (i == 0) {
                    out.println(csv.keySet().stream().reduce((a, b) -> a + ";" + b).orElse("no data"));
                }
                int finalI = i;
                out.println(csv.entrySet().stream().map(v -> v.getValue().get(finalI)).map(Object::toString).reduce((a, b) -> a + ";" + b).orElse("no data"));
            }

            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> generateIdeas() {
        return List.of("give 4 people gutschein for coffee at place x");
    }

}