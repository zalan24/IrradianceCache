package org.irrad.geometry;

public class Ray {
    public Vec3 origin = new Vec3(0);
    public Vec3 direction = new Vec3(0);

    public Ray() {

    }

    /**
     * 
     * @param o origin
     * @param d direction
     */
    public Ray(Vec3 o, Vec3 d) {
        origin = o;
        direction = d;
    }
}