package org.irrad.geometry;

import java.util.Set;

public abstract class Sampler {

    public static class Sample {
        final Ray ray;
        final double weight;

        public Sample(Ray r, double w) {
            ray = r;
            weight = w;
        }

        public int hashCode() {
            return (int) Math
                    .floor(10000 * ray.direction.x * ray.direction.y * ray.direction.z * weight + ray.origin.length());
        }
    }

    public abstract Set<Sample> getSample(int raycount, Vec3 origin, Vec3 normal);
}