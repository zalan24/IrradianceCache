package org.irrad;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.irrad.geometry.*;

class Image {
    private final int width;
    private final int height;

    private Vec3[][] image;

    /**
     * 
     * @param w width
     * @param h height
     */
    public Image(int w, int h) {
        width = w;
        height = h;
        image = new Vec3[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                image[x][y] = new Vec3(0);
            }
        }
    }

    public void setPixel(int w, int h, Vec3 rgb) {
        image[w][h] = rgb;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Color c = new Color((float) image[x][y].x, (float) image[x][y].y, (float) image[x][y].z);
                img.setRGB(x, height - y - 1, c.getRGB());
            }
        }
        return img;
    }
}