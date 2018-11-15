package org.irrad;

import org.irrad.geometry.*;

public class Camera {
    private Vec3 mEyePos = new Vec3(0);
    private Vec3 mLookat = new Vec3(0);
    private double mFovy = Math.toRadians(60);
    private Vec3 mUp = new Vec3(0, 1, 0);

    /**
     * 
     * @param eye    eyepos
     * @param lookat lookat position
     */
    public Camera(Vec3 eye, Vec3 lookat) {
        mEyePos = eye;
        mLookat = lookat;
    }

    /**
     * 
     * @param eye    eyepos
     * @param lookat lookat position
     * @param fovy   Field of view Y
     */
    public Camera(Vec3 eye, Vec3 lookat, double fovy) {
        mEyePos = eye;
        mLookat = lookat;
        mFovy = fovy;
    }

    /**
     * 
     * @param eye    eyepos
     * @param lookat lookat position
     * @param up     up vector (0,1,0) by default
     */
    public Camera(Vec3 eye, Vec3 lookat, Vec3 up) {
        mEyePos = eye;
        mLookat = lookat;
        mUp = up;
    }

    /**
     * 
     * @param eye    eyepos
     * @param lookat lookat position
     * @param up     up vector (0,1,0) by default
     * @param fovy   Field of view Y
     */
    public Camera(Vec3 eye, Vec3 lookat, Vec3 up, double fovy) {
        mEyePos = eye;
        mLookat = lookat;
        mUp = up;
        mFovy = fovy;
    }

    public Ray getRay(int width, int height, int x, int y) {
        double sx = x;
        double sy = y;
        sx /= height;
        sy /= height;
        sx = 2 * sx - (double) width / height;
        sy = 2 * sy - 1;
        Vec3 forward = Vec3.sub(mLookat, mEyePos).normalized();
        Vec3 side = Vec3.cross(mUp, forward).normalized();
        Vec3 up = Vec3.cross(forward, side);
        final double dist = 1.0 / Math.tan(mFovy / 2);
        Vec3 dir = Vec3.mul(forward, dist);
        dir = Vec3.add(dir, Vec3.mul(side, sx));
        dir = Vec3.add(dir, Vec3.mul(up, sy));
        return new Ray(mEyePos, dir.normalized());
    }

}