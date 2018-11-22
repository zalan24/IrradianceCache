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
    final int mCapacity;
    private int mSize = 0;
    final double mRange;
    final double mCos;

    /**
     * 
     * @param aCapacity max number of elemnets
     * @param aRange    search samples in range
     * @param aCos      search samples with dot product >= aCos
     */
    public Cache(int aCapacity, double aRange, double aCos) {
        mCapacity = aCapacity;
        mRange = aRange;
        mCos = aCos;
    }

    private CacheEntry search(Vec3 position, Vec3 normal, CacheEntry p, Vec3 A, Vec3 B) {
        if (p == null)
            return null;
        Vec3 AB = Vec3.sub(B, A);
        Vec3 leftB;
        Vec3 rightA;
        if (Math.abs(AB.x) >= Math.abs(AB.y) && Math.abs(AB.x) >= Math.abs(AB.z)) {
            leftB = new Vec3(p.position.x, B.y, B.z);
            rightA = new Vec3(p.position.x, A.y, A.z);
        } else if (Math.abs(AB.y) >= Math.abs(AB.z)) {
            leftB = new Vec3(B.x, p.position.y, B.z);
            rightA = new Vec3(A.x, p.position.y, A.z);
        } else {
            leftB = new Vec3(B.x, B.y, p.position.z);
            rightA = new Vec3(A.x, A.y, p.position.z);
        }
        CacheEntry left = null;
        CacheEntry right = null;
        if (Vec3.inside(position, mRange, A, leftB)) {
            if (p.left == null)
                left = null;
            else {
                left = search(position, normal, p.left, A, leftB);
                if (left != null && (Vec3.dot(left.normal, normal) < mCos
                        || Vec3.sqDistance(left.position, position) > mRange * mRange))
                    left = null;
            }
        }
        if (Vec3.inside(position, mRange, rightA, B)) {
            if (p.right == null)
                right = null;
            else {
                // System.out.println(" right");
                right = search(position, normal, p.right, rightA, B);
                if (right != null && (Vec3.dot(right.normal, normal) < mCos
                        || Vec3.sqDistance(right.position, position) > mRange * mRange))
                    right = null;
            }
        }
        CacheEntry ret = null;
        if (left == null)
            ret = right;
        else if (right == null)
            ret = left;
        else if (Vec3.sqDistance(left.position, position) < Vec3.sqDistance(right.position, position))
            ret = left;
        if (Vec3.dot(p.normal, normal) < mCos || Vec3.sqDistance(p.position, position) > mRange * mRange)
            return ret;
        if (ret == null)
            return p;
        if (Vec3.sqDistance(ret.position, position) < Vec3.sqDistance(p.position, position))
            return ret;
        return p;
    }

    public Vec3 get(Vec3 position, Vec3 normal) {
        Vec3 A = new Vec3(-1000000);
        Vec3 B = new Vec3(1000000);
        CacheEntry ret = search(position, normal, parent, A, B);
        if (ret == null)
            return null;
        return ret.light;
    }

    private void insert(CacheEntry e, Vec3 A, Vec3 B, CacheEntry p) {
        Vec3 AB = Vec3.sub(B, A);
        Vec3 leftB;
        Vec3 rightA;
        if (Math.abs(AB.x) >= Math.abs(AB.y) && Math.abs(AB.x) >= Math.abs(AB.z)) {
            leftB = new Vec3(p.position.x, B.y, B.z);
            rightA = new Vec3(p.position.x, A.y, A.z);
        } else if (Math.abs(AB.y) >= Math.abs(AB.z)) {
            leftB = new Vec3(B.x, p.position.y, B.z);
            rightA = new Vec3(A.x, p.position.y, A.z);
        } else {
            leftB = new Vec3(B.x, B.y, p.position.z);
            rightA = new Vec3(A.x, A.y, p.position.z);
        }
        if (Vec3.inside(e.position, A, leftB)) {
            if (p.left == null)
                p.left = e;
            else {
                // System.out.println(" left");
                insert(e, A, leftB, p.left);
            }
        } else {
            if (p.right == null)
                p.right = e;
            else {
                // System.out.println(" right");
                insert(e, rightA, B, p.right);
            }
        }
    }

    public void add(Vec3 position, Vec3 normal, Vec3 light) {
        CacheEntry e = new CacheEntry(position, normal, light);
        Vec3 A = new Vec3(-1000000);
        Vec3 B = new Vec3(1000000);
        if (parent == null)
            parent = e;
        else if (mSize < mCapacity) {
            // System.out.println("insert");
            insert(e, A, B, parent);
            mSize++;
        }
    }
}
