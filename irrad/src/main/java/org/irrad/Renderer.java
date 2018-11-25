package org.irrad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.irrad.geometry.*;
import org.irrad.graphics.*;

class Renderer {
    final int mCacheCapacity;
    final int mDivisionX = 8;
    final int mDivisionY = 8;
    final int mSamples = 256;

    final double mCacheRadius = 0.1;
    final double mCacheCos = 0.8;

    private Scene mScene;
    private int maxDepth;

    private Integer index = 0;
    private Object[] jobs;
    Cache cache;
    Collection<RenderJob> completejobs;

    private class RenderThread extends Thread {

        @Override
        public void run() {
            while (true) {
                // RenderJob j = jobs.iterator().next();
                RenderJob j = null;
                synchronized (index) {
                    if (index >= jobs.length)
                        return;
                    j = (RenderJob) jobs[index];
                    index++;
                }
                doJob(j, 1, cache);
                completejobs.add(j);
            }
        }

    }

    /**
     * 
     * @param aScene Scene object
     * @param maxD   max depth
     */
    public Renderer(Scene aScene, int maxD) {
        mScene = aScene;
        maxDepth = maxD;
        mCacheCapacity = 10000;
    }

    /**
     * 
     * @param aScene         Scene object
     * @param maxD           max depth
     * @param aCacheCapacity capacity of the irradiance cache
     */
    public Renderer(Scene aScene, int maxD, int aCacheCapacity) {
        mScene = aScene;
        maxDepth = maxD;
        mCacheCapacity = aCacheCapacity;
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

    private Vec3 collect(Vec3 origin, Vec3 normal, int depth, Cache cache) {
        // Only diffuse surfaces are supported
        Vec3 sum = Vec3.zero();
        Vec3 l = cache.get(origin, normal);
        if (l != null)
            return l; // Vec3.zero();
        if (depth >= maxDepth)
            return sum;
        Sampler sampler = new DiffuseSampler(mDivisionX, mDivisionY);
        Set<Sampler.Sample> samples = sampler.getSample(mSamples, origin, normal);
        Iterator<Sampler.Sample> itr = samples.iterator();
        while (itr.hasNext()) {
            Sampler.Sample sample = itr.next();
            RenderJob job = new RenderJob(sample.ray, 0, 0);
            doJob(job, depth + 1, cache);
            sum = Vec3.add(sum, Vec3.mul(job.rgb, sample.weight));
        }
        if (Vec3.inside(origin, new Vec3(-10), new Vec3(10)))
            cache.add(origin, normal, sum);
        // sum = new Vec3(1, 0, 0);
        return sum;
    }

    private void doJob(RenderJob job, int depth, Cache cache) {
        job.intersection = mScene.cast(job.rayToCast);
        Vec3 origin = Vec3.add(job.intersection.ray.origin,
                Vec3.mul(job.intersection.ray.direction, job.intersection.intersection.dist));
        if (job.intersection.shape.getMaterial().mAlbedo.length() > 0) {
            Vec3 normal = job.intersection.intersection.normal;
            job.light = collect(origin, normal, depth, cache);
        } else {
            job.light = Vec3.zero();
        }
        job.rgb = Shader.shade(job.intersection, job.light);
    }

    public Image render(Camera camera, int width, int height) {
        Image ret = new Image(width, height);
        cache = new Cache(mCacheCapacity, mCacheRadius, mCacheCos);
        ArrayList<RenderJob> shuffledRenderjobs = new ArrayList<>();
        completejobs = (Collection<RenderJob>) Collections.synchronizedCollection(new ArrayList<RenderJob>());
        for (int w = 0; w < width; ++w) {
            for (int h = 0; h < height; ++h) {
                RenderJob job = new RenderJob(camera.getRay(width, height, w, h), w, h);
                shuffledRenderjobs.add(job);
            }
        }
        Random rnd = new Random();
        jobs = shuffledRenderjobs.toArray();
        for (int i = 0; i < jobs.length; i++) {
            int j = rnd.nextInt(jobs.length);
            Object s = jobs[i];
            jobs[i] = jobs[j];
            jobs[j] = s;
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Thread thr = new RenderThread();
            threads.add(thr);
            thr.start();
        }
        for (Thread thr : threads) {
            try {
                thr.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (RenderJob job : completejobs) {
            ret.setPixel(job.x, job.y, job.rgb);
        }
        return ret;
    }
}