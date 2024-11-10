package local.triforce.sim;

import local.triforce.model.World;

import java.util.List;
import java.util.Map;

public interface SimAPI {

    /**
     * @param strategies, not necessarly strings, just a list of changes to the inital state, like extra lines, or incentives to move to somwhere
     * @return the amount of people
     */
    Map<String, Long> run(List<String> strategies/*add some world state? hide internal state*/);

    /**
     * better interface? get a world state in some form, do your stuff and return a new state
     * @param world
     * @return
     */
    World step(World world);
}
