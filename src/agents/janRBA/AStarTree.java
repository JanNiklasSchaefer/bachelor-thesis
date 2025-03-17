/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janRBA;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.GameStatus;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStarTree {

    MarioTimer timer;                       // timer of remaining time per search
    PriorityQueue<Vertex> searchVertices;   // list of vertices that will be searched
    ArrayList<Vertex> visitedVertices;      // list of visited vertices
    Vertex bestPosition;                    // best vertex found yet
    Vertex furthestPosition;                // furthest vertex found yet
    int resetSearchLimit = 2;               // limit when search should be reset
    int resetSearchCounter = 0;             // counter to check if search should be reset
    boolean replanningNeeded = false;       // replanning flag

    /**
     * Constructor of A Search Tree
     *
     * @param timer remaining time in current frame
     */
    AStarTree(MarioTimer timer) {
        this.timer = timer;
        this.searchVertices = new PriorityQueue<Vertex>((v1, v2) -> Float.compare((v1.getRemainingTime() + 0.9f * v1.steps),
                (v2.getRemainingTime() + 0.9f * v2.steps)));
        this.visitedVertices = new ArrayList<Vertex>();
    }

    /**
     * Function to manage the search. It either starts with the search state of the previous frame or resets it and
     * searches from current position using A*.
     *
     * @param model forwardModel to simulate game actions without altering game state
     * @param timer remaining time in current frame
     * @return
     */
    public boolean[] search(MarioForwardModel model, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        Vertex root = initializeRoot(model, timer, drawCoordinates);
        root.setPossibleActions();
        root.createChildren();
        if (!root.children.isEmpty()) this.searchVertices.addAll(root.children);
        Vertex currentVertex = root;
        boolean goodMove = false;
        while (timer.getRemainingTime() > 0 && !this.searchVertices.isEmpty() && (bestPosition.model.getGameStatus() != GameStatus.WIN || !goodMove)) {
            goodMove = false;
            currentVertex = this.searchVertices.poll();
            if (currentVertex == null) {
                return ActionSet.duck;
            }
            if (!currentVertex.simulated) {
                currentVertex.simulateVertex(drawCoordinates, this.visitedVertices);
            }
            if (currentVertex.model.getGameStatus() == GameStatus.LOSE) {
                continue;
            }
            if (currentVertex.model.getGameStatus() == GameStatus.WIN) {
                bestPosition = currentVertex;
                break;
            }
            //If node was visited already
            if (!currentVertex.visited && currentVertex.isVisited(visitedVertices)) {
                currentVertex.realRemainingTime += 1500f;
                currentVertex.estimatedTime = currentVertex.realRemainingTime;
                this.searchVertices.add(currentVertex);
                continue;
            }

            //Move worse than estimated add to queue again with updated value
            if (currentVertex.realRemainingTime - currentVertex.estimatedTime > 0.1f) {
                currentVertex.estimatedTime = currentVertex.realRemainingTime;
                this.searchVertices.add(currentVertex);
                continue;
            }

            visitedVertices.add(currentVertex);
            goodMove = true;
            if (currentVertex.children.isEmpty()) {
                currentVertex.setPossibleActions();
                currentVertex.createChildren();
            }

            if (!currentVertex.children.isEmpty()) this.searchVertices.addAll(currentVertex.children);

            if (currentVertex.aboveHole) {
                goodMove = false;
                continue;
            }

            if (this.bestPosition.getRemainingTime() > currentVertex.getRemainingTime()) {
                this.bestPosition = currentVertex;
            }
            if (currentVertex.model.getMarioFloatPos()[0] > this.furthestPosition.model.getMarioFloatPos()[0] && !currentVertex.damageTaken) {
                this.furthestPosition = currentVertex;
            }
        }

        //Sometimes search is too fast and just wait for timer to run out to avoid skipping frames when displaying the gameplay
        //while(timer.getRemainingTime()>1){};


        if (this.furthestPosition.model.getMarioFloatPos()[0] > this.bestPosition.model.getMarioFloatPos()[0] + 20) {
            this.bestPosition = this.furthestPosition;
        }
        resetSearchCounter++;
        return extractAction();
    }

    /**
     * Extracts action from the current position. As no path is saved iterate up the tree until the last vertex before root and
     * choose its associated action. Thus advancing the game state to that vertex. Set that vertex as new parent to advance the
     * search tree one step further.
     *
     * @return
     */
    private boolean[] extractAction() {
        Vertex tmpVertex = this.bestPosition;
        if (tmpVertex == null) return null;
        //If best position is the root just do nothing. Probably game is already lost in this state anyway
        if (this.bestPosition.parent == null) {
            return ActionSet.doNothing;
        }
        //if(bestPosition.aboveHole) System.out.println("Best Position above hole");
        while (tmpVertex.parent.parent != null) {
            tmpVertex = tmpVertex.parent;
            if (tmpVertex.damageTaken) replanningNeeded = true;
        }
        tmpVertex.parent = null;
        return tmpVertex.action;
    }

    /**
     * Function to reset search tree
     *
     * @param timer
     */
    private void resetSearch(MarioTimer timer) {
        this.searchVertices.clear();
        this.visitedVertices.clear();
        this.timer = timer;
    }


    /**
     * Function to initialize search and with this the root. If needed the previous state of search is reset.
     *
     * @param model Initialize Root from current forwardModel
     * @return Vertex
     */
    private Vertex initializeRoot(MarioForwardModel model, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        Vertex root = new Vertex(model, ActionSet.doNothing);
        root.realRemainingTime = 100000f;
        if (resetSearchCounter == 0 || resetSearchCounter >= resetSearchLimit || replanningNeeded) {
            this.bestPosition = root;
            this.furthestPosition = root;
            resetSearchCounter = 0;
            resetSearch(timer);
        }
        return root;
    }

}
