package org.irrad.geometry;

public class Sky extends Shape {

    @Override
    public Hit intersect(Ray r) {
        return new Hit(Double.POSITIVE_INFINITY, new Vec3(0));
    }

}