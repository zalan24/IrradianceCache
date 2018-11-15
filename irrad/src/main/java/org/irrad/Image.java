package org.irrad;

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
                final int r = (int) Math.floor(image[x][y].x * 255);
                final int g = (int) Math.floor(image[x][y].y * 255);
                final int b = (int) Math.floor(image[x][y].z * 255);
                int value = r + (g << 8) + (b << 16);
                img.setRGB(x, y, value);
            }
        }
        return img;
    }
}