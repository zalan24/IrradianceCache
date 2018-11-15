package org.irrad.geometry;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DiffuseSampler extends Sampler {

    static Random rnd = new Random();

    private final int divx;
    private final int divy;

    /**
     * straightified rendering is used
     * 
     * @param dx division on axis x
     * @param dy division on axis y
     */
    public DiffuseSampler(int dx, int dy) {
        divx = dx;
        divy = dy;
    }

    private double lerp(double a, double b, double f) {
        return (b - a) * f + a;
    }

    @Override
    public Set<Sample> getSample(int raycount, Vec3 origin, Vec3 normal) {
        Set<Sample> ret = new HashSet<>();
        if (normal.length() < 0.99)
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
            final double weight = 1.0 / (double) (divx * divy) / (double) samplesPerDiv;

            rx = lerp((double) dx / divx, (double) (dx + 1) / divx, rx);
            ry = lerp((double) dy / divy, (double) (dy + 1) / divy, ry);

            final double r = Math.sqrt(rx);
            final double theta = 2.0 * Math.PI * ry;

            final double x = r * Math.cos(theta);
            final double y = r * Math.sin(theta);

            Vec3 zdir = new Vec3(x, y, Math.sqrt(Math.max(0.0, 1 - rx)));

            Vec3 updir = Math.abs(normal.y) > 0.5 ? new Vec3(1, 0, 0) : Vec3.up();
            Vec3 xdir = Vec3.cross(updir, normal).normalized();
            updir = Vec3.cross(normal, xdir);

            final Vec3 dir = Vec3
                    .add(Vec3.mul(normal, zdir.z), Vec3.add(Vec3.mul(xdir, zdir.x), Vec3.mul(zdir.y, updir)))
                    .normalized();

            final Vec3 o = Vec3.add(origin, Vec3.mul(normal, 0.0001));

            Sample s = new Sample(new Ray(o, dir), weight / 2 * Vec3.dot(dir, normal));
            ret.add(s);
        }

        return ret;

    }

}