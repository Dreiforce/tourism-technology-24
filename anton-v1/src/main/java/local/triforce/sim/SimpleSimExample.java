package local.triforce.sim;

import local.triforce.model.Lift;
import local.triforce.model.Slope;
import local.triforce.model.World;

import java.util.List;
import java.util.Map;

public class SimpleSimExample implements SimAPI {
    @Override
    public Map<String, Long> run(List<String> strategies) {

        return null;
    }

    @Override
    public World step(World world) {
        long ms = 1000 * 60;// == 1 minute? othwerise issues with moving parts of people?
        World result = world.clone();
        Map<String, Long> peopleAtStation = result.getPeopleAtStation();

        for (Lift a : result.getLifts()) {
            long atFrom = peopleAtStation.get(a.getFrom());
            long atTo = peopleAtStation.get(a.getTo());

            long move = Math.min(
                    (long) (ms * a.getMoveRatePeoplePerHour() / (60 * 60 * 1000)),
                    atFrom
            );

            atFrom -= move;
            atTo += move;

            peopleAtStation.put(a.getFrom(), atFrom);
            peopleAtStation.put(a.getTo(), atTo);
        }
        for (Slope s : result.getSlopes()) {
            long peopleFrom = world.getPeopleAtStation().get(s.getFrom());
            long peopleTo = world.getPeopleAtStation().get(s.getTo());

            long people = (long) (s.getPeopleOnSlope());

            long add = (long) (s.getFunlevel() * peopleFrom);
            peopleAtStation.put(s.getFrom(), peopleFrom - add);
            people += add;

            long sub = (long) (s.getSpeed() * peopleFrom); //TODO: rework completely
            peopleAtStation.put(s.getTo(), peopleTo + sub);
            people -= sub;

            s.setPeopleOnSlope(Math.max(0, people));
        }

        return result;
    }

}
