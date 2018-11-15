package org.irrad.graphics;

import org.irrad.geometry.*;

public class Material {
    public Vec3 mAlbedo = new Vec3(0);
    public Vec3 mEmission = new Vec3(0);
    // Vec3 mSpecular;

    public Material() {

    }

    public Material(Vec3 albedo, Vec3 emission) {
        mAlbedo = albedo;
        mEmission = emission;
    }
}