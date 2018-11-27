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

    public static double clamp(double x, double mn, double mx) {
        if (x < mn)
            return mn;
        if (x > mx)
            return mx;
        return x;
    }

    public static double lin2rgb(double x) {
        return clamp(Math.pow(x, 1.0 / 2.2), 0, 1);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Color c = new Color((float) lin2rgb(image[x][y].x), (float) lin2rgb(image[x][y].y),
                        (float) lin2rgb(image[x][y].z));
                img.setRGB(x, height - y - 1, c.getRGB());
            }
        }
        return img;
    }
}