/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janRBA;

import engine.core.MarioForwardModel;
import engine.helper.GameStatus;
import engine.helper.MarioActions;

import java.util.ArrayList;

public class Vertex {

    boolean holeDetectionActivated = true;      // toggle hole detection for RBA
    int holeDetectionDepths = 0;            // Set Hole Detection Depths, set to 0 showed best results
    MarioForwardModel model;                // forward model to simulate game states on
    boolean[][] possibleActions;            // array of possible actions
    ArrayList<Vertex> children;             // array of children
    float estimatedTime;                    // estimated time remaining according to heuristic
    float realRemainingTime;                // actual time remaining according to heuristic
    boolean[] action;                       // single action of this vertex
    Vertex parent;                          // parent of this vertex
    int marioMode;                          // marioMode of this vertex
    boolean visited;                        // visited flag of this vertex. Only true if at moment of simulation a similar vertex was already visited
    boolean simulated;                      // simulated flag of this vertex. Only true if associated action was simulated.
    boolean aboveHole;                      // above hole flag. Only true if after simulation game state is above hole

    boolean damageTaken;                    // damage taken flag, only true if mario mode of parent is greater than current mario mode

    float timeConstant = 100000f;           // big time constant for heuristic
    int steps;                              // step counter to favor nodes deeper down search tree

    /**
     * Constructor for Vertex Object. Forward Model while constructing is only referenced. Only copy when simulating for efficiency reasons.
     *
     * @param model  forward model to later copy and simulate on
     * @param action associated action of current vertex
     */
    public Vertex(MarioForwardModel model, boolean[] action) {
        this.model = model;
        this.parent = null;
        this.children = new ArrayList<Vertex>();
        this.estimatedTime = 0f;
        this.realRemainingTime = 0f;
        this.action = action;
        this.marioMode = model.getMarioMode();
        this.visited = false;
        this.simulated = false;
        this.steps = 0;
        this.aboveHole = false;
        this.damageTaken = false;
    }

    /**
     * Get Remaining Time. If current position was simulated return realRemainingTime. Else the estimated time.
     *
     * @return
     */
    public float getRemainingTime() {
        if (realRemainingTime == 0f) {
            return estimatedTime;
        }
        return realRemainingTime;
    }

    /**
     * Creates Children and estimates the remainingTime.
     */
    public void createChildren() {
        for (boolean[] action : this.possibleActions) {
            Vertex child = new Vertex(this.model, action);
            float estimatedSpeedGain = child.estimateSpeedGain();
            float estimatedSpeed = child.model.getMarioFloatVelocity()[0] + estimatedSpeedGain;
            float estimatedForwardMovement = child.estimateForwardMovement();
            float estimatedPosition = child.model.getMarioFloatPos()[0] + estimatedForwardMovement;
            child.steps = this.steps + 1;
            child.estimatedTime = getTimeToRightScreen(estimatedSpeed, estimatedPosition);
            child.parent = this;
            this.children.add(child);
        }
    }

    /**
     * Sets possible actions of vertex based on current game state.
     * Only call after vertex has been simulated otherwise it might set actions that are not possible after simulation.
     */
    public void setPossibleActions() {
        if (this.model.getMarioMode() == 2) {
            if (this.model.mayMarioJump() || model.getMarioCanJumpHigher()) {
                this.possibleActions = ActionSet.moveSetWithJump;
                return;
            }
            this.possibleActions = ActionSet.moveSetWithoutJump;
            return;
        }
        if (this.model.mayMarioJump() || this.model.getMarioCanJumpHigher()) {
            this.possibleActions = ActionSet.moveSetWithoutShoot;
            return;
        }
        this.possibleActions = ActionSet.moveSetWithoutJumpAndShoot;
    }

    /**
     * Estimate Forward Based on next Action.
     * While Pressing Sprint Button it is: currentSpeed + 1.2f
     * While not Pressing Sprint Button it is: currentSpeed + 0.6f
     * Functions assume mario is running in a straight line and are only approximated.
     * Factors such as Jumping / Falling are not estimated and thus it is only used as an estimation.
     *
     * @return
     */
    private float estimateForwardMovement() {
        int signature = 1;
        if (this.action[MarioActions.LEFT.getValue()]) {
            signature = -1;
        }
        if (this.action[MarioActions.DOWN.getValue()]) {
            return 0f;
        }
        float constant = this.action[MarioActions.SPEED.getValue()] ? 1.1999998f : 0.6000004f;

        return signature * (0.9999972912f * this.model.getMarioFloatVelocity()[0] + constant);
    }

    /**
     * Estimate Speed gain based on next action.
     * While Sprinting speed gain will be: -0.124f * currentSpeed + 1.2f
     * While Not Sprinting speed gain wille be: -0.124f * currentSpeed + 0.6f
     * Functions assume mario is running in a straight line and are only approximated.
     * Factors such as Jumping / Falling are not included in calculation and thus it is only used as
     * an estimation.
     *
     * @return
     */
    private float estimateSpeedGain() {
        float currentSpeed = this.model.getMarioFloatVelocity()[0];

        if (this.parent != null) {
            //Functionality that if we switched Directions speed is halved. Actual calculation more complex don't care about that
            if (this.parent.action[MarioActions.LEFT.getValue()] && this.action[MarioActions.RIGHT.getValue()]) {
                return this.model.getMarioFloatVelocity()[0] / 2;
            }
            if (this.parent.action[MarioActions.RIGHT.getValue()] && this.action[MarioActions.LEFT.getValue()]) {
                return this.model.getMarioFloatVelocity()[0] / 2;
            }
        }
        float constant = this.action[MarioActions.SPEED.getValue()] ? 1.2f : 0.6f;

        if (currentSpeed < 0.54f && !this.action[MarioActions.SPEED.getValue()]) {
            return 0.54f;
        }
        if (currentSpeed < 1.07f && this.action[MarioActions.SPEED.getValue()]) {
            return 1.07f;
        }
        return ((-0.124f) * currentSpeed + constant);
    }

    /**
     * Calculate as a heuristic time to right screen. Assume we can move in a straight line to
     * the right screen.
     *
     * @param marioSpeed     Current Mario Speed
     * @param marioXPosition Current Mario X Position
     * @return
     */
    public float getTimeToRightScreen(float marioSpeed, float marioXPosition) {
        float forward_loss = 88.26065980000004f - (9.09052339589794f * marioSpeed);

        //Maximum estimated forward Movement for 1000 Ticks. Assume we can sprint at max speed to the right
        float maxEstimatedForwardMovement = 10.909058f * 1000f - forward_loss;

        return (timeConstant - (maxEstimatedForwardMovement + marioXPosition)) / 9.7090845f;

    }

    /**
     * Function Simulates Vertex and updates Vertex Variables accordingly
     *
     * @param drawCoordinates
     */
    public void simulateVertex(ArrayList<Float[]> drawCoordinates, ArrayList<Vertex> visitedVertices) {
        if (this.parent != null)
            this.model = parent.model.clone();

        float oldXPos = this.model.getMarioFloatPos()[0];
        float oldYPos = this.model.getMarioFloatPos()[1];
        this.model.advance(this.action);
        float newXPos = this.model.getMarioFloatPos()[0];
        float newYPos = this.model.getMarioFloatPos()[1];
        drawCoordinates.add(new Float[]{oldXPos, oldYPos, newXPos, newYPos});

        if (holeDetectionActivated) {
            if (this.holeDetection(holeDetectionDepths)) {
                this.aboveHole = true;
            }
        }

        this.marioMode = this.model.getMarioMode();
        int marioDamage = this.getMarioDamage();
        // Calculate real remaining time after simulating
        this.realRemainingTime = this.getTimeToRightScreen(this.model.getMarioFloatVelocity()[0],
                this.model.getMarioFloatPos()[0]);
        // Add Damage Coefficient
        this.realRemainingTime += marioDamage * (timeConstant - (100 * this.steps));
        if (this.visited) {
            this.realRemainingTime += 1500f;
            this.estimatedTime += 1500f;
        }


        this.simulated = true;
    }

    /**
     * Function to Calculate the Damage Factor of Current Mario State. If Mario Mode is smaller then parent state we
     * took Damage. If we die close to the finish damage value is higher then if we die further away from finish.
     *
     * @return
     */
    public int getMarioDamage() {
        int damage = 0;
        if (this.marioMode < this.parent.marioMode) {
            damage += 1;
        }
        if (this.model.getGameStatus() == GameStatus.LOSE) {
            damage += 5;
        }

        if (damage > 0) this.damageTaken = true;

        return damage;
    }

    /**
     * Determines if current or the tileAmount of Tiles to the Right are above a hole. If tileAmount = 0. Look if node's
     * current tile is above a hole.
     *
     * @param tileAmount amount of tiles to look to the right for holes
     * @return
     */
    public boolean holeDetection(int tileAmount) {
        int[][] scene = this.model.getMarioSceneObservation();
        int[] tilePos = this.model.getMarioScreenTilePos();
        //Mario can jump out of frame on the top of the screen. If he does so tilePos[1] will be negative.
        //To avoid Out of Bounds error set it to 0 in that case.
        if (tilePos[1] < 0) {
            tilePos[1] = 0;
        }
        for (int i = tilePos[1]; i < 16; i++) {
            // We are currently over an obstacle and thus not over a hole
            for (int j = tilePos[0]; j < 16 && j <= tilePos[0] + tileAmount; j++) {
                if (scene[j][i] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Function to see if we already visited that Vertex. Take current Mario Position and time. If its close enough assume
     * we have visited this state already. We can use this because a lot of Actions lead to the nearly the same output and
     * this just filters a lot of the same moves unless they are really needed. To wait on an enemy might be a reason
     * to visit that Position multiple times over a short time frame.
     *
     * @param visitedVertices list of visited vertices
     * @return True if Vertex is Visited according to our approximation. False otherwise
     */
    public boolean isVisited(ArrayList<Vertex> visitedVertices) {
        for (Vertex visitedVertex : visitedVertices) {
            float xDiff = Math.abs(this.model.getMarioFloatPos()[0] - visitedVertex.model.getMarioFloatPos()[0]);
            float yDiff = Math.abs(this.model.getMarioFloatPos()[1] - visitedVertex.model.getMarioFloatPos()[1]);
            float timeDiff = Math.abs(this.steps - visitedVertex.steps);
            if (xDiff < 2f && yDiff < 2f && timeDiff < 5f) {
                this.visited = true;
                return true;
            }
        }
        return false;
    }
}
