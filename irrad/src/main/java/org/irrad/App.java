package org.irrad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.irrad.geometry.*;

public class App {
    public static void main(String[] args) {
        // Scene scene = Scene.BasicScene();
        Scene scene = Scene.CornellBox();
        Camera camera = new Camera(new Vec3(0.5, 0.5, -1), new Vec3(0.5));
        Renderer renderer = new Renderer(scene, 10, 50000);
        renderer.mAllowCachingOnFirstLevel = false;
        Timer timer = new Timer();
        Image img = renderer.render(camera, 512, 512);
        System.out.print(timer.elapsed() + "s");

        BufferedImage bufferedImage = img.toBufferedImage();

        File outputfile = new File("Data/image.png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
