/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.graphics;

import java.awt.Graphics;

public abstract class MarioGraphics {
    public boolean visible;
    public float alpha;
    public int originX, originY;
    public boolean flipX, flipY;
    public int width, height;

    public MarioGraphics() {
        this.visible = true;
        this.alpha = 1;
        this.originX = this.originY = 0;
        this.flipX = this.flipY = false;
        this.width = this.height = 32;
    }

    public abstract void render(Graphics og, int x, int y);
}
