/**
 * This code was taken from the Mario-AI-Framework GitHub repository.
 * Repository: https://github.com/amidos2006/Mario-AI-Framework
 * Maintainer: Ahmed Khalifa
 * Modifications made for the purpose of this thesis, are marked accordingly.
 */
package engine.graphics;

import java.awt.Graphics;
import java.awt.Image;

public class MarioImage extends MarioGraphics {
    public Image[][] sheet;
    public int index;

    public MarioImage(Image[][] sheet, int index) {
        super();
        this.sheet = sheet;
        this.index = index;
    }

    @Override
    public void render(Graphics og, int x, int y) {
        if (!visible) return;

        int xPixel = x - originX;
        int yPixel = y - originY;
        Image image = this.sheet[index % sheet.length][index / sheet.length];

        og.drawImage(image, xPixel + (flipX ? width : 0), yPixel + (flipY ? height : 0), flipX ? -width : width, flipY ? -height : height, null);
    }

}
