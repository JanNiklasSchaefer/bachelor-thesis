/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.core;

import java.util.ArrayList;

/**
 * Interface for agents that want to play in the framework
 *
 * @author AhmedKhalifa
 */
public interface MarioAgent {
    /**
     * initialize and prepare the agent before the game starts
     *
     * @param model a forward model object so the agent can simulate or initialize some parameters based on it.
     * @param timer amount of time before the agent has to return
     */
    void initialize(MarioForwardModel model, MarioTimer timer);

    /**
     * The following function was modified to contain the drawCoordinates Array, this enables the debug draw.
     * Author: Jan Niklas Schäfer
     * <p>
     * get mario current actions
     *
     * @param model           a forward model object so the agent can simulate the future.
     * @param timer           amount of time before the agent has to return the actions.
     * @param drawCoordinates array containing the position pairs which are to be drawn in the next frame
     * @return an array of the state of the buttons on the controller
     */
    boolean[] getActions(MarioForwardModel model, MarioTimer timer, ArrayList<Float[]> drawCoordinates);

    /**
     * Return the name of the agent that will be displayed in debug purposes
     *
     * @return
     */
    String getAgentName();
}
