/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janMonteMario;

import engine.core.MarioForwardModel;
import engine.helper.MarioActions;

import java.util.ArrayList;

public class Node {
    public MarioForwardModel model;     // Forward Model of the Node
    public Node parent;                 // Parent Node
    public ArrayList<boolean[]> path;   // path of actions leading to the current node's game state
    public float nodeValue;             // Node Value
    boolean[][] possibleActions;       // List of possible actions
    public boolean[] chosenAction;      // selected Action for this node
    ArrayList<Node> children;           // List of Children

    public boolean aboveHole;           // Flag if vertex is above a hole
    public float xPosition;             // x Position of Node
    public int timesVisited;            // Times the node was visited
    public boolean expanded;            // Flag if a node is expanded
    public int expandedChildCount;      // Count of expanded children
    public float maxChildValue;         // Maximum Child Value


    /**
     * Constructor of Node Object for MCTS Agent
     *
     * @param model
     */
    public Node(MarioForwardModel model) {
        this.model = model;
        this.path = new ArrayList<>();
        this.nodeValue = 0f;
        this.possibleActions = setPossibleActions();
        this.children = new ArrayList<>();
        this.parent = null;
        this.timesVisited = 0;
        this.xPosition = model.getMarioFloatPos()[0];
        this.maxChildValue = 0f;
        this.expanded = false;
        this.expandedChildCount = 0;
        this.aboveHole = false;
    }

    /**
     * Copy Constructor of Node Object
     *
     * @return Node that was copied
     */
    public Node clone() {
        Node newNode = new Node(this.model.clone());
        newNode.nodeValue = this.nodeValue;
        newNode.path = this.path;
        newNode.parent = this.parent;
        newNode.timesVisited = this.timesVisited;
        newNode.maxChildValue = this.maxChildValue;
        newNode.expanded = this.expanded;
        newNode.expandedChildCount = this.expandedChildCount;
        newNode.aboveHole = this.aboveHole;
        return newNode;
    }

    /**
     * Setter Function for Chosen Action
     *
     * @param chosenAction
     */
    public void setChosenAction(boolean[] chosenAction) {
        this.chosenAction = chosenAction;
    }

    /**
     * Function that creates the list of Children for the current node, based on the possible actions.
     */
    public void createChildren() {
        for (boolean[] action : this.possibleActions) {
            Node newNode = new Node(this.model);
            newNode.path.addAll(this.path);
            newNode.path.add(action);
            //newNode.nodeValue = this.nodeValue;
            newNode.setChosenAction(action);
            newNode.parent = this;
            this.children.add(newNode);
        }
    }

    /**
     * Setter Function for nodeValue
     *
     * @param nodeValue
     */
    public void setNodeValue(float nodeValue) {
        this.nodeValue = nodeValue;
    }

    /**
     * Returns an Array of possible Actions, depending on the current game state.
     *
     * @return ActionSet
     */
    public boolean[][] setPossibleActions() {
        if (this.model.getMarioMode() == 2) {
            if (this.model.mayMarioJump() || model.getMarioCanJumpHigher()) {
                return ActionSet.moveSetWithJump;
            }
            return ActionSet.moveSetWithoutJump;
        }
        if (this.model.mayMarioJump() || this.model.getMarioCanJumpHigher()) {
            return ActionSet.moveSetWithoutShoot;
        }
        return ActionSet.moveSetWithoutJumpAndShoot;
    }

    /**
     * Getter Function for parent of current of node
     *
     * @return
     */
    public Node getParent() {
        return parent;
    }

}
