/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.helper;

public enum EventType {
    BUMP(1),
    STOMP_KILL(2),
    FIRE_KILL(3),
    SHELL_KILL(4),
    FALL_KILL(5),
    JUMP(6),
    LAND(7),
    COLLECT(8),
    HURT(9),
    KICK(10),
    LOSE(11),
    WIN(12);

    private int value;

    EventType(int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
