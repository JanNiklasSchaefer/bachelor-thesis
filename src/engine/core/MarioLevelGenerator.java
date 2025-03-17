/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.core;

public interface MarioLevelGenerator {
    /**
     * Generate a playable mario level
     *
     * @param model contain a model of the level
     */
    String getGeneratedLevel(MarioLevelModel model, MarioTimer timer);

    /**
     * Return the name of the level generator
     *
     * @return the name of the level generator
     */
    String getGeneratorName();
}
