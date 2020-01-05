package org.irrad.geometry;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DiffuseSampler extends Sampler {

    static Random rnd = new Random();

    private final int divx;
    private final int divy;
    private final boolean cosine;

    /**
     * straightified rendering is used
     * 
     * @param dx division on axis x
     * @param dy division on axis y
     */
    public DiffuseSampler(int dx, int dy, boolean cosine) {
        divx = dx;
        divy = dy;
        this.cosine = cosine;
    }

    private double lerp(double a, double b, double f) {
        return (b - a) * f + a;
    }

    @Override
    public Set<Sample> getSample(int raycount, Vec3 origin, Vec3 normal) {
        Set<Sample> ret = new HashSet<>();
        if (normal.length() < 0.95)
            return ret;
        // Random rnd = new Random();
        for (int i = 0; i < raycount; ++i) {
            int dx = i % divx;
            int dy = (i / divx) % divy;
            double rx = rnd.nextDouble();
            double ry = rnd.nextDouble();

            int samplesPerDiv = raycount / (divx * divy);
            if (i < raycount % (divx * divy))
                samplesPerDiv++;

            rx = lerp((double) dx / divx, (double) (dx + 1) / divx, rx);
            ry = lerp((double) dy / divy, (double) (dy + 1) / divy, ry);
            // final double phi = 2.0 * Math.PI * rx;

            final Vec3 o = Vec3.add(origin, Vec3.mul(normal, 0.0001));
            Vec3 updir = Math.abs(normal.y) > 0.5 ? new Vec3(1, 0, 0) : Vec3.up();
            Vec3 xdir = Vec3.cross(updir, normal).normalized();
            updir = Vec3.cross(normal, xdir);
            double weight;
            Vec3 zdir;
            // http://www.rorydriscoll.com/2009/01/07/better-sampling/
            if (cosine) {
                weight = 1.0 / (double) (divx * divy) / (double) samplesPerDiv;

                double r = Math.sqrt(rx);
                double theta = 2 * Math.PI * ry;

                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);

                zdir = new Vec3(x, y, Math.sqrt(1 - rx));
            } else {
                double r = Math.sqrt(1.0 - rx * rx);
                double phi = 2 * Math.PI * ry;

                zdir = new Vec3(Math.cos(phi) * r, Math.sin(phi) * r, rx);
                weight = zdir.z * 2 / (double) (divx * divy) / (double) samplesPerDiv;
            }
            final Vec3 dir = Vec3
                        .add(Vec3.mul(normal, zdir.z), Vec3.add(Vec3.mul(xdir, zdir.x), Vec3.mul(zdir.y, updir)))
                        .normalized();
            Sample s = new Sample(new Ray(o, dir), weight);

            ret.add(s);
        }

        return ret;

    }

}