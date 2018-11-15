package org.irrad.geometry;

public interface Intersectable {
    static class Hit {
        public final double dist;
        public final Vec3 normal;

        public Hit(double d, Vec3 n) {
            dist = d;
            normal = n;
        }
    }

    Hit intersect(Ray r);
}