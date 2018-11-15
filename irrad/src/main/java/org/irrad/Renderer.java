package org.irrad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.irrad.geometry.*;
import org.irrad.graphics.*;

class Renderer {
    private Scene mScene;
    private int maxDepth;

    /**
     * 
     * @param aScene Scene object
     * @param maxD   max depth
     */
    public Renderer(Scene aScene, int maxD) {
        mScene = aScene;
        maxDepth = maxD;
    }

    protected class RenderJob {
        final Ray rayToCast;
        final int x;
        final int y;
        // results
        Scene.Intersection intersection;
        Vec3 light;
        Vec3 rgb;

        public RenderJob(Ray r, int xx, int yy) {
            rayToCast = r;
            x = xx;
            y = yy;
        }
    }

    private Vec3 collect(Vec3 origin, Vec3 normal, int depth) {
        // Only diffuse surfaces are supported
        Vec3 sum = Vec3.zero();
        if (depth >= maxDepth)
            return sum;
        Sampler sampler = new DiffuseSampler(8, 8);
        Set<Sampler.Sample> samples = sampler.getSample(64, origin, normal);
        Iterator<Sampler.Sample> itr = samples.iterator();
        while (itr.hasNext()) {
            Sampler.Sample sample = itr.next();
            RenderJob job = new RenderJob(sample.ray, 0, 0);
            doJob(job, depth + 1);
            sum = Vec3.add(sum, Vec3.mul(job.rgb, sample.weight));
        }
        return sum;
    }

    private void doJob(RenderJob job, int depth) {
        job.intersection = mScene.cast(job.rayToCast);
        Vec3 origin = Vec3.add(job.intersection.ray.origin,
                Vec3.mul(job.intersection.ray.direction, job.intersection.intersection.dist));
        if (job.intersection.shape.getMaterial().mAlbedo.length() > 0)
            job.light = collect(origin, job.intersection.intersection.normal, depth);
        else
            job.light = Vec3.zero();
        job.rgb = Shader.shade(job.intersection, job.light);
    }

    public Image render(Camera camera, int width, int height) {
        Image ret = new Image(width, height);
        Cache cache = new Cache();
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
            doJob(j, 1);
            completejobs.add(j);
        }
        for (RenderJob job : completejobs) {
            ret.setPixel(job.x, job.y, job.rgb);
        }
        return ret;
    }
}