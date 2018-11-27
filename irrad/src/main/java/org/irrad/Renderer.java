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
    final int mDivisionX = 2;
    final int mDivisionY = 2;
    final int mSamples = 4;
    final int mNumThreads = 16;
    boolean mHighLightCachePoint = false;
    boolean mAllowCachingOnFirstLevel = true;

    final double mCacheRadius = 0.05;
    final double mCacheCos = 0.8;

    private Scene mScene;
    private int maxDepth;

    private int index = 0;
    private Object[] jobs;
    Cache cache;
    Cache prevcache;
    Collection<RenderJob> completejobs;

    private class RenderThread extends Thread {

        private int mLevel = 1;

        public RenderThread(int aLevel) {
            mLevel = aLevel;
        }

        @Override
        public void run() {
            while (true) {
                // RenderJob j = jobs.iterator().next();
                RenderJob j = null;
                synchronized (jobs) {
                    if (index >= jobs.length)
                        return;
                    j = (RenderJob) jobs[index];
                    index++;
                }
                doJob(j, mLevel, cache, mLevel, mLevel + 1);
                // synchronized (completejobs) {
                completejobs.add(j);
                // }
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

    private Vec3 collect(Vec3 origin, Vec3 normal, int depth, Cache cache, int aStartDepth, int aMaxDepth) {
        // Only diffuse surfaces are supported
        Vec3 sum = Vec3.zero();
        final boolean allowed = mAllowCachingOnFirstLevel || depth > 1;
        if (allowed) {
            // Caching allowed
            Vec3 l = cache.get(origin, normal);
            if (l != null)
                return l;
        }
        boolean cached = false;
        if (prevcache != null && allowed) {
            Vec3 l = prevcache.get(origin, normal);
            if (l != null) {
                sum = l;
                cached = true;
            }
        }
        if (depth >= aMaxDepth)
            return sum;
        if (!cached) {
            Sampler sampler = new DiffuseSampler(mDivisionX, mDivisionY);
            Set<Sampler.Sample> samples = sampler.getSample(mSamples, origin, normal);
            Iterator<Sampler.Sample> itr = samples.iterator();
            while (itr.hasNext()) {
                Sampler.Sample sample = itr.next();
                RenderJob job = new RenderJob(sample.ray, 0, 0);
                doJob(job, depth + 1, cache, aStartDepth, aMaxDepth);
                sum = Vec3.add(sum, Vec3.mul(job.rgb, sample.weight));
            }
        }
        if (depth == aStartDepth && Vec3.inside(origin, new Vec3(-10), new Vec3(10)))
            cache.add(origin, normal, sum);
        if (mHighLightCachePoint)
            sum = new Vec3(1, 0, 0);
        return sum;
    }

    private void doJob(RenderJob job, int depth, Cache cache, int aStartDepth, int aMaxDepth) {
        job.intersection = mScene.cast(job.rayToCast);
        Vec3 origin = Vec3.add(job.intersection.ray.origin,
                Vec3.mul(job.intersection.ray.direction, job.intersection.intersection.dist));
        if (job.intersection.shape.getMaterial().mAlbedo.length() > 0) {
            Vec3 normal = job.intersection.intersection.normal;
            job.light = collect(origin, normal, depth, cache, aStartDepth, aMaxDepth);
        } else {
            job.light = Vec3.zero();
        }
        job.rgb = Shader.shade(job.intersection, job.light);
    }

    public Image render(Camera camera, int width, int height) {
        Image ret = new Image(width, height);
        cache = null;
        for (int depth = maxDepth - 1; depth >= 1; depth--) {
            System.out.println("Depth = " + depth);
            index = 0;
            prevcache = cache;
            if (prevcache != null)
                System.out.println(prevcache.size());
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
            for (int i = 0; i < mNumThreads; i++) {
                Thread thr = new RenderThread(depth);
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
            // System.out.println(completejobs.size());
        }
        for (RenderJob job : completejobs) {
            ret.setPixel(job.x, job.y, job.rgb);
        }
        return ret;
    }
}