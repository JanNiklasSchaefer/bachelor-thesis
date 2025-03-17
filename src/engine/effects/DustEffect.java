/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.effects;

import java.awt.Graphics;

import engine.core.MarioEffect;

public class DustEffect extends MarioEffect {
    public DustEffect(float x, float y) {
        super(x, y, (float) (Math.random() * 2 - 1), (float) Math.random() * -1, 0, 0, 8 + (int) (Math.random() * 2), 10 + (int) (Math.random() * 5));
    }

    @Override
    public void render(Graphics og, float cameraX, float cameraY) {
        if (this.life > 10) {
            this.graphics.index = 7;
        } else {
            this.graphics.index = this.startingIndex + (10 - life) * 4 / 10;
        }
        super.render(og, cameraX, cameraY);
    }
}