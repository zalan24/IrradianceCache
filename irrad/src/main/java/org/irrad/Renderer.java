package org.irrad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.irrad.geometry.*;
import org.irrad.graphics.*;

class Renderer {
    private Scene mScene;

    /**
     * 
     * @param aScene Scene object
     */
    public Renderer(Scene aScene) {
        mScene = aScene;
    }

    protected class RenderJob {
        final Ray rayToCast;
        final int x;
        final int y;
        // results
        Scene.Intersection intersection;
        Vec3 rgb;

        public RenderJob(Ray r, int xx, int yy) {
            rayToCast = r;
            x = xx;
            y = yy;
        }
    }

    private void doJob(RenderJob job) {
        job.intersection = mScene.cast(job.rayToCast);
        job.rgb = Shader.shade(job.intersection);
    }

    public Image render(Camera camera, int width, int height) {
        Image ret = new Image(width, height);
        Collection<RenderJob> jobs = (Collection<RenderJob>) Collections
                .synchronizedCollection(new ArrayList<RenderJob>());
        Collection<RenderJob> completejobs = (Collection<RenderJob>) Collections
                .synchronizedCollection(new ArrayList<RenderJob>());
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < height; ++h) {
                RenderJob job = new RenderJob(camera.getRay(width, height, w, h), w, h);
                jobs.add(job);
            }
        }
        while (!jobs.isEmpty()) {
            RenderJob j = jobs.iterator().next();
            jobs.remove(j);
            doJob(j);
            completejobs.add(j);
        }
        for (RenderJob job : completejobs) {
            ret.setPixel(job.x, job.y, job.rgb);
        }
        return ret;
    }
}