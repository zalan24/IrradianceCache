package org.irrad.geometry;

public class Plane extends Shape {
    Vec3 origin = new Vec3(0);
    Vec3 normal = new Vec3(0, 1, 0);

    public Plane() {

    }

    /**
     * 
     * @param o origin
     * @param n normal vector
     */
    public Plane(Vec3 o, Vec3 n) {
        origin = o;
        normal = n.normalized();
    }

    @Override
    public Intersectable.Hit intersect(Ray r) {
        // https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-plane-and-ray-disk-intersection
        double denom = Vec3.dot(normal, r.direction);
        if (Math.abs(denom) > 1e-6) {
            Vec3 p0l0 = Vec3.sub(origin, r.origin);
            double t = Vec3.dot(p0l0, normal) / denom;
            if (t < 0)
                return new Intersectable.Hit(Double.POSITIVE_INFINITY, normal);
            else
                return new Intersectable.Hit(t, normal);
        }

        return new Intersectable.Hit(Double.POSITIVE_INFINITY, normal);
    }
}