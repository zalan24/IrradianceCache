package org.irrad.geometry;

public class Sphere extends Shape {
    Vec3 origin = new Vec3(0);
    double R = 0;

    public Sphere() {

    }

    /**
     * 
     * @param o origin
     * @param r radius
     */
    public Sphere(Vec3 o, double r) {
        origin = o;
        R = r;
    }

    @Override
    public Intersectable.Hit intersect(Ray r) {
        final Vec3 diff = Vec3.sub(origin, r.origin);
        final Vec3 rToClosest = Vec3.mul(r.direction, Vec3.dot(r.direction, diff));
        final Vec3 closest = Vec3.add(r.origin, rToClosest);
        final double L2 = Vec3.sqDistance(closest, origin);
        if (L2 > R * R) {
            return new Intersectable.Hit(Double.POSITIVE_INFINITY, Vec3.zero());
        }
        final double t = Math.sqrt(R * R - L2);
        double dist = rToClosest.length();
        if (dist - t > 0)
            dist = dist - t;
        else if (dist + t > 0)
            dist = dist + t;
        else
            return new Intersectable.Hit(Double.POSITIVE_INFINITY, Vec3.zero());
        final Vec3 hitp = Vec3.add(r.origin, Vec3.mul(r.direction, dist));
        return new Intersectable.Hit(dist, Vec3.sub(hitp, origin).normalized());
    }

}