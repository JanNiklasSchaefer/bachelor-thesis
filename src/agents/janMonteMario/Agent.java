/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janMonteMario;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.GameStatus;
import engine.sprites.Mario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Agent implements MarioAgent {

    // Toggle the different techniques using the next 4 variables.
    boolean mixMaxActivated = false;             //Toggle MixMax
    boolean partialExpansionActivated = false;  //Toggle Partial Expansion
    boolean rouletteWheelSelection = false;     //Toggle Roulette Wheel Seleciotn
    boolean holeDetectionActivated = true;     //Toggle Hole Detection

    int rolloutCap = 6;                         // Rollout Cap for Simulation

    float UcbFactor = 0.25f;                    // Explotation Factor of UCB

    int expansionCount = 0;                     // Overall count of expansions for this search cycle

    float mixMaxFactor = 0.125f;                // Explotation Factor for MixMax Technique

    Node furthestNode;                          // Furthest Node of current search
    Node bestNode;                              // Best Node of current search

    /**
     * Initialize Agent at the beginning of a level. Create all object needed for future searches.
     *
     * @param model forward model for simulation of game states
     * @param timer amount of time before the agent has to return
     */
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.furthestNode = new Node(model);
        this.bestNode = new Node(model);
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
        return mcts(model, timer, drawCoordinates);
    }

    /**
     * This function is responsible for repeating the MCTS steps. First initiate the current game state and repeat the 4 steps
     * of 1. Selection; 2. Expansion; 3. Simulation; 4. Backpropagation until the time runs out.
     *
     * @param model           forward model for simulation of game states
     * @param timer           amount of time before the agent has to return
     * @param drawCoordinates array of coordinate pairs, which allows for drawing paths in subsequent frame between the coordinates.
     * @return chosenAction
     */
    public boolean[] mcts(MarioForwardModel model, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        expansionCount = 0;
        Node root = new Node(model);
        root.setChosenAction(ActionSet.duck);
        if (partialExpansionActivated) initializePartialExpansion(root);
        if (!partialExpansionActivated) vanillaExpansion(root, drawCoordinates);
        Node expansionNode = root;

        if (furthestNode.path.isEmpty()) {
            furthestNode = root;
        }
        if (bestNode.path.isEmpty()) {
            bestNode = root;
        }

        while (timer.getRemainingTime() > 0) {
            if (partialExpansionActivated) {
                Node startingNode = partialNonRecursiveSelection(root, timer, drawCoordinates);
                expansionNode = partialExpansion(startingNode, timer, drawCoordinates);
                if (timer.getRemainingTime() < 0) continue;
            }
            if (!partialExpansionActivated) {
                Node startingNode = vanillaNonRecursiveSelection(root, timer, drawCoordinates);
                expansionNode = vanillaExpansion(startingNode, drawCoordinates);
            }
            simulation(expansionNode, drawCoordinates);
            backpropagation(expansionNode);

            // If current node is above hole. Don't safe as furthest and best hole.

            if (holeDetectionActivated) {
                if (holeDetection(expansionNode)) continue;
            }

            if (expansionNode.model.getGameStatus() != GameStatus.LOSE &&
                    expansionNode.model.getMarioFloatPos()[0] >= furthestNode.model.getMarioFloatPos()[0]) {
                furthestNode = expansionNode;
            }
            if (expansionNode.model.getGameStatus() != GameStatus.LOSE &&
                    expansionNode.nodeValue / expansionNode.timesVisited >= bestNode.nodeValue / bestNode.timesVisited
            ) {
                bestNode = expansionNode;
            }
        }

        //If best node is not close to furthes node just return furthest node
        if (bestNode.model.getMarioFloatPos()[0] + 20 < furthestNode.model.getMarioFloatPos()[0] && !furthestNode.path.isEmpty()) {
            // Debug Print to find out the number of expansions and depths of search tree.
            //System.out.println("Furthest Path with Depths: "+ furthestNode.path.size() + " chosen with Expansion Count: " + expansionCount + " Mario Game status: " + furthestNode.model.getGameStatus());

            bestNode = furthestNode;
            return furthestNode.path.removeFirst();
        }

        if (bestNode.path.isEmpty()) {
            return expansionNode.path.removeFirst();
        }
        // Debug Print to find out the number of expansions and depths of search tree.
        //System.out.println("Best Path with Depths: "+ bestNode.path.size() + " chosen with Expansion Count: " + expansionCount);

        furthestNode = bestNode;
        return bestNode.path.removeFirst();
    }

    /**
     * Calculate UCB with Function: UCB = X_j + UcbFactor * sqrt( (2 * expansionCount) / timesVisited)
     * X_j = the current node value averaged over the times the Node was visited
     * UcbFactor =  a constant
     * Expansion Count = is the amount of times we expanded a node
     * Times Visited = is the amount of times a node was visited
     *
     * @param node Search Node for which the value should be calculated
     * @return
     */
    public float ucbMonteMario(Node node) {
        return (node.nodeValue / node.timesVisited) + UcbFactor * (float) Math.sqrt((2f * Math.log(expansionCount)) / node.timesVisited);
    }

    /**
     * Calculate UCB using MixMax Heuristic: UCB = explotation + UcbFactor * sqrt( (2 * expansionCount) / timesVisited)
     * explotation = Q * max  + (1 - Q) * X_j.
     * Q is a constant number in [0,1], X_j is the current node value averaged over the times the Node was visited. Max the biggest Child Value
     *
     * @param node Search Node for which the value should be calculated
     * @return
     */
    public float mixMaxUcb(Node node) {
        //Calculate Explotation Factor explotation = Q * max  + (1 - Q) * X_j
        float explotation = mixMaxFactor * node.maxChildValue + ((1 - mixMaxFactor) * (node.nodeValue / node.timesVisited));
        return explotation + UcbFactor * ((float) Math.sqrt((2f * Math.log(expansionCount)) / node.timesVisited));
    }

    /**
     * Calculate the partialExpansionFactor. This gives us the UCB for partially expanded Children from current Node.
     * Partially Expanded Children are created but their action wasn't simulated yet.
     *
     * @param node Search Node for which the value should be calculated
     * @return
     */
    public float partialExpansionFactor(Node node) {
        return 0.5f + UcbFactor * (float) Math.sqrt((2f * Math.log(expansionCount)) / (1f + node.expandedChildCount));
    }

    /**
     * Expand all Children of the node at once. Like in classical MCTS
     *
     * @param node            Search Node for which the value should be calculated
     * @param drawCoordinates array of coordinate pairs, which allows for drawing paths in subsequent frame between the coordinates.
     * @return
     */
    public Node vanillaExpansion(Node node, ArrayList<Float[]> drawCoordinates) {
        float bestReward = 0f;
        node.createChildren();
        Node bestChild = node.children.getFirst();
        for (Node child : node.children) {
            child.model = child.model.clone();
            child.model.advance(child.chosenAction);
            float[] parentPos = child.getParent().model.getMarioFloatPos();
            float[] childPos = child.model.getMarioFloatPos();


            float reward = 0.5f + 0.5f * ((childPos[0] - parentPos[0]) / (11));


            if (child.model.getGameStatus() == GameStatus.LOSE) {
                reward = 0f;
            }

            if (child.model.getGameStatus() == GameStatus.WIN) {
                reward = 1f;
            }

            if (reward > bestReward) {
                bestReward = reward;
                bestChild = child;
            }

            child.expanded = true;
            child.timesVisited++;
            expansionCount++;
        }

        return bestChild;
    }


    /**
     * Select a Child Node According to "vanilla" Tree Policy. Vanilla Tree Policy is only used in combination with
     * vanillaExpansion(). Here we assume every node always has all it's child nodes expanded.
     *
     * @param node  Search Node from which to start the selection process
     * @param timer amount of time before the agent has to return
     * @return bestChild: Best Child to Expand on
     */
    public Node vanillaNonRecursiveSelection(Node node, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        Node bestChild = node;

        while (!bestChild.children.isEmpty() && timer.getRemainingTime() > 0) {
            float nodeValue = 0;
            ArrayList<Node> children = bestChild.children;
            for (Node child : children) {
                float ucbValue = 0f;

                if (mixMaxActivated) ucbValue = mixMaxUcb(child);
                if (!mixMaxActivated) ucbValue = ucbMonteMario(child);

                if (ucbValue > nodeValue) {
                    nodeValue = ucbValue;
                    bestChild = child;
                }
            }
            //Draw the best selected children to see which paths we explore
            float[] parentPos = bestChild.getParent().model.getMarioFloatPos();
            float[] childPos = bestChild.model.getMarioFloatPos();
            drawCoordinates.add(new Float[]{parentPos[0], parentPos[1], childPos[0], childPos[1]});

        }

        return bestChild;
    }

    /**
     * Select a Child Node according to Partial Expansion Policy. Either return an already created but not expanded child
     * or
     *
     * @param node Search Node from which to start the selection process
     * @return
     */
    public Node partialNonRecursiveSelection(Node node, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        Node bestChild = node;
        ArrayList<Node> unexploredChildren = new ArrayList<Node>();

        while (bestChild.expanded && !bestChild.children.isEmpty()) {
            float bestUcbValue = -1f;
            float ucbValue = 0f;
            for (Node child : bestChild.children) {
                if (!child.expanded) {
                    if (!rouletteWheelSelection) unexploredChildren.add(child);
                    // Add Weighted Elements. We assume integer weights, and just add it the amount of time we want it to be able to be pulled
                    if (rouletteWheelSelection) {
                        if (child.chosenAction == ActionSet.fastJumpRight || child.chosenAction == ActionSet.sprintRight) {
                            for (int i = 0; i < 10; i++) unexploredChildren.add(child);
                        } else if (child.chosenAction == ActionSet.slowJumpRight || child.chosenAction == ActionSet.moveRight) {
                            for (int i = 0; i < 5; i++) unexploredChildren.add(child);
                        } else unexploredChildren.add(child);
                    }
                }
                if (child.expanded) {

                    if (mixMaxActivated) ucbValue = mixMaxUcb(child);
                    if (!mixMaxActivated) ucbValue = ucbMonteMario(child);

                    if (ucbValue >= bestUcbValue) {
                        bestUcbValue = ucbValue;
                        bestChild = child;
                    }
                }
            }

            // If expansionFactor > UCB Value dont go deeper in the tree return a partially expanded Node. Return Random Node

            float partialExpansionBound = 1f;
            if (bestChild.parent != null) partialExpansionBound = partialExpansionFactor(bestChild.parent);
            if (rouletteWheelSelection) {
                if (partialExpansionBound > bestUcbValue && !unexploredChildren.isEmpty()) {
                    if (unexploredChildren.size() == 1) {
                        Node expansionNode = unexploredChildren.get(0);
                        unexploredChildren.clear();
                        return expansionNode;
                    }
                    Random random = new Random();
                    float factor = random.nextFloat();
                    int index = (int) ((unexploredChildren.size() - 1) * factor);
                    Node expansionNode = unexploredChildren.get(index);
                    unexploredChildren.clear();
                    return expansionNode;
                }
            }
            if (!rouletteWheelSelection) {
                if (partialExpansionBound > bestUcbValue && !unexploredChildren.isEmpty()) {
                    if (unexploredChildren.size() == 1) {
                        Node expansionNode = unexploredChildren.get(0);
                        unexploredChildren.clear();
                        return expansionNode;
                    }
                    Random random = new Random();
                    int ind = random.nextInt(0, unexploredChildren.size() - 1);
                    Node expansionNode = unexploredChildren.get(ind);
                    unexploredChildren.clear();
                    return expansionNode;
                }
            }
            if (bestChild.parent != null) {
                //Draw the best selected children to see which paths we explore
                float[] parentPos = bestChild.getParent().model.getMarioFloatPos();
                float[] childPos = bestChild.model.getMarioFloatPos();
                drawCoordinates.add(new Float[]{parentPos[0], parentPos[1], childPos[0], childPos[1]});
            }
            unexploredChildren.clear();
        }
        return bestChild;
    }

    /**
     * Partially Expand a node.
     *
     * @param node Search Node that will be partially expanded
     * @return
     */
    public Node partialExpansion(Node node, MarioTimer timer, ArrayList<Float[]> drawCoordinates) {
        if (!node.children.isEmpty()) {
            return node;
        } else {
            node.createChildren();
        }
        node.model = node.model.clone();
        node.model.advance(node.chosenAction);
        if (node.parent != null) {
            float[] parentPos = node.getParent().model.getMarioFloatPos();
            float[] childPos = node.model.getMarioFloatPos();
            node.xPosition = childPos[0];
            drawCoordinates.add(new Float[]{parentPos[0], parentPos[1], childPos[0], childPos[1]});
            node.parent.expandedChildCount++;
        }

        node.timesVisited++;
        node.expanded = true;
        expansionCount++;

        return node;
    }

    /**
     * Initializes partial expansion and sets the root node of search tree.
     *
     * @param root Search Node that is the root of current search tree
     */
    public void initializePartialExpansion(Node root) {
        root.createChildren();
        root.expanded = true;
        expansionCount++;
    }


    /**
     * Simulate Steps from current best position in search tree. Simulation Policy in this case are just random Actions.
     *
     * @param node            Search node to simulate on
     * @param drawCoordinates array of coordinate pairs, to draw paths between in the subsequent frame.
     */
    public void simulation(Node node, ArrayList<Float[]> drawCoordinates) {
        Node simulationNode = node.clone();

        Random randomNumberGenerator = new Random();
        int rolloutDepths = 0;
        float reward = 0f;
        float[] parentPos = simulationNode.parent.model.getMarioFloatPos();
        while (rolloutDepths < rolloutCap) {
            int randomActionIndex = randomNumberGenerator.nextInt(simulationNode.possibleActions.length);
            //Simulate random action
            simulationNode.model.advance(simulationNode.possibleActions[randomActionIndex]);
            simulationNode.setPossibleActions();
            rolloutDepths++;
        }

        reward = 0.5f + 0.5f * ((simulationNode.model.getMarioFloatPos()[0] - parentPos[0]) / (11 * (1 + rolloutCap)));


        if (simulationNode.model.getGameStatus() == GameStatus.LOSE) {
            reward = 0f;
        }

        if (simulationNode.model.getGameStatus() == GameStatus.WIN) {
            reward = 1f;
        }

        node.setNodeValue(reward);
    }

    /**
     * Determines if current Node is above a hole
     *
     * @param node Search Node to detect
     * @return
     */
    public boolean holeDetection(Node node) {
        int[][] scene = node.model.getMarioSceneObservation();
        int[] tilePos = node.model.getMarioScreenTilePos();
        if (tilePos[1] < 0) tilePos[1] = 0;
        for (int i = tilePos[1]; i < 16; i++) {
            // We are currently over an obstacle and thus not over a hole
            if (scene[tilePos[0]][i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Backpropagation the value of simulation of current node to all parent nodes.
     *
     * @param node Search Node which value should be propagated up the tree
     */
    public void backpropagation(Node node) {
        Node searchNode = node;
        float toAddValue = searchNode.nodeValue;
        //Update Parent if we got new max value
        if (searchNode.parent.maxChildValue < toAddValue) {
            searchNode.parent.maxChildValue = toAddValue;
        }
        while (searchNode.parent != null) {
            searchNode.parent.nodeValue += toAddValue;
            searchNode.parent.timesVisited++;
            searchNode = searchNode.parent;
        }
    }

    @Override
    public String getAgentName() {
        return "janMonteMario";
    }

}
