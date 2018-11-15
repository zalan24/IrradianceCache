package org.irrad.graphics;

import org.irrad.geometry.*;

public class Shader {
    public static Vec3 shade(Scene.Intersection intersection, Vec3 inlight) {
        if (intersection.shape == null)
            return new Vec3(0);
        Vec3 normal = intersection.intersection.normal;
        if (Vec3.dot(normal, intersection.ray.direction) > 1)
            normal = Vec3.mul(normal, -1);
        // return Vec3.div(Vec3.add(normal, new Vec3(1, 1, 1)), 2);
        Vec3 ret = intersection.shape.getMaterial().mEmission;
        ret = Vec3.add(ret, Vec3.mul(intersection.shape.getMaterial().mAlbedo, inlight));
        return ret;
    }
}