/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.helper;

public enum MarioActions {
    LEFT(0, "Left"),
    RIGHT(1, "Right"),
    DOWN(2, "Down"),
    SPEED(3, "Speed"),
    JUMP(4, "Jump");

    private int value;
    private String name;

    MarioActions(int newValue, String newName) {
        value = newValue;
        name = newName;
    }

    public int getValue() {
        return value;
    }

    public String getString() {
        return name;
    }

    public static int numberOfActions() {
        return MarioActions.values().length;
    }

    public static MarioActions getAction(int value) {
        return MarioActions.values()[value];
    }
}
