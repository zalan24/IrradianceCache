package org.irrad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.irrad.geometry.*;

public class App {
    public static void main(String[] args) throws IOException {
        // Scene scene = Scene.BasicScene();
        // Scene scene = Scene.CornellBox();
        Scene scene = Scene.SpheresScene();
        Camera camera = new Camera(new Vec3(0.5, 0.5, -1), new Vec3(0.5));
        Renderer renderer = new Renderer(scene, 5, 50000);
        // Renderer renderer = new Renderer(scene, 3, 50000);
        renderer.mAllowCachingOnFirstLevel = false;
        Timer timer = new Timer();
        // Image img = renderer.render(camera, 1920, 1080);
        Image img = renderer.render(camera, 512, 512);
        System.out.print(timer.elapsed() + "s");

        BufferedImage bufferedImage = img.toBufferedImage();

        File outDir = new File("Data");
        outDir.mkdirs();
        File outputfile = new File(outDir + "/image.png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
