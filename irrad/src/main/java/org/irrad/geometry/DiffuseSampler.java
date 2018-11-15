package org.irrad.geometry;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DiffuseSampler extends Sampler {

    private final int divx;
    private final int divy;

    /**
     * straightified rendering is used
     * 
     * @param dx division on axis x
     * @param dy division on axis y
     */
    public DiffuseSampler(int dx, int dy) {
        divx = dx;
        divy = dy;
    }

    @Override
    public Set<Sample> getSample(int raycount, Vec3 origin, Vec3 normal) {
        Set<Sample> ret = new HashSet<>();
        Random rnd = new Random();

        return ret;

    }

}