/**
 * This code was created as part of Jan Niklas Schäfer's bachelor thesis
 * Author: Jan Niklas Schäfer
 */
package agents.janRBA;

public class ActionSet {
    public static final boolean[] moveRight = {false, true, false, false, false};
    public static final boolean[] moveLeft = {true, false, false, false, false};
    public static final boolean[] slowJumpRight = {false, true, false, false, true};
    public static final boolean[] slowJumpLeft = {true, false, false, false, true};
    public static final boolean[] fastJumpRight = {false, true, false, true, true};
    public static final boolean[] fastJumpLeft = {true, false, false, true, true};
    public static final boolean[] sprintRight = {false, true, false, true, false};
    public static final boolean[] sprintLeft = {true, false, false, true, false};
    public static final boolean[] duck = {false, false, true, false, false};
    public static final boolean[] shoot = {false, false, false, true, false};

    public static final boolean[] jump = {false, false, false, false, true};
    public static final boolean[] doNothing = {false, false, false, false, false};
    public static final boolean[][] moveSetWithJump = {sprintRight, fastJumpRight, moveRight, moveLeft, fastJumpLeft, sprintLeft, slowJumpRight, slowJumpLeft, shoot, jump, doNothing, duck};
    public static final boolean[][] moveSetWithoutJump = {moveRight, moveLeft, sprintRight, sprintLeft, shoot, doNothing, duck};
    public static final boolean[][] moveSetWithoutShoot = {sprintRight, moveRight, fastJumpRight, moveLeft, fastJumpLeft, sprintLeft, slowJumpRight, slowJumpLeft, jump, doNothing};
    public static final boolean[][] moveSetWithoutJumpAndShoot = {moveRight, moveLeft, sprintRight, sprintLeft, doNothing};
}
