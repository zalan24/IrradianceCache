package org.irrad.geometry;

import org.irrad.graphics.Material;

public abstract class Shape implements Intersectable {
    private Material mMaterial = new Material();

    public void setMaterial(Material mat) {
        mMaterial = mat;
    }

    public final Material getMaterial() {
        return mMaterial;
    }
}