/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.core;

/**
 * Mario timer object used to control the agents so they won't exceed the allowed time.
 *
 * @author AhmedKhalifa
 */
public class MarioTimer {
    private long startTimer;
    private long remainingTime;

    /**
     * Start a timer
     *
     * @param remainingTime the amount of milliseconds before the timer runs out
     */
    public MarioTimer(long remainingTime) {
        this.startTimer = System.currentTimeMillis();
        this.remainingTime = remainingTime;
    }

    /**
     * Get the remaining time in that timer since construction
     *
     * @return number of milliseconds remaining in that timer.
     */
    public long getRemainingTime() {
        return Math.max(0, this.remainingTime - (System.currentTimeMillis() - this.startTimer));
    }
}
