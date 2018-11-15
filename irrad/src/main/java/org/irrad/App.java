package org.irrad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.irrad.geometry.*;

public class App {
    public static void main(String[] args) {
        Scene scene = Scene.BasicScene();
        Camera camera = new Camera(new Vec3(5, 5, -5), new Vec3(0, 1, 0));
        Renderer renderer = new Renderer(scene);
        Timer timer = new Timer();
        Image img = renderer.render(camera, 600, 400);
        System.out.print(timer.elapsed() + "s");

        BufferedImage bufferedImage = img.toBufferedImage();

        File outputfile = new File("Data/image.jpg");
        try {
            ImageIO.write(bufferedImage, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}