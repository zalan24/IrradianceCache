package org.irrad.graphics;

import org.irrad.geometry.*;

public class Shader {
    public static Vec3 shade(Scene.Intersection intersection) {
        if (intersection.shape == null)
            return new Vec3(0);
        return Vec3.div(Vec3.add(intersection.intersection.normal, new Vec3(1, 1, 1)), 2);
    }
}