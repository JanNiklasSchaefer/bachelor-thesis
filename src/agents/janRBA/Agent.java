/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janRBA;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

import java.util.ArrayList;

public class Agent implements MarioAgent {

    AStarTree astarTree;

    /**
     * Initialize Agent at the beginning of a level. Create all object needed for future searches.
     *
     * @param model forward model for simulation of game states
     * @param timer amount of time before the agent has to return
     */
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.astarTree = new AStarTree(timer);
    }

    /**
     * Start Search at the beginning of a frame.
     *
     * @param model           forward model for simulation of game states
     * @param timer           amount of time before the agent has to return
     * @param drawCoordinates array of coordinate pairs, which allows for drawing paths in subsequent frame between the coordinates.
     */
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        return this.astarTree.search(model, timer, drawCoordinates);
    }

    @Override
    public String getAgentName() {
        return "newAStarAgent";
    }
}
