package org.irrad;

import org.irrad.geometry.Vec3;

class Cache {
    static class CacheEntry {
        final Vec3 position;
        final Vec3 normal;
        final Vec3 light;

        CacheEntry left = null;
        CacheEntry right = null;

        public CacheEntry(Vec3 p, Vec3 n, Vec3 l) {
            position = p;
            normal = n;
            light = l;
        }
    }

    CacheEntry parent = null;

    // public void add() {

    // }
}
