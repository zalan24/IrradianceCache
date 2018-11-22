package org.irrad.geometry;

public class Vec3 implements Cloneable {
    public double x;
    public double y;
    public double z;

    public Vec3() {
        x = y = z = 0;
    }

    public Vec3(double a) {
        x = y = z = a;
    }

    public Vec3(double xx, double yy, double zz) {
        x = xx;
        y = yy;
        z = zz;
    }

    public static Vec3 add(Vec3 a, Vec3 b) {
        return new Vec3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vec3 sub(Vec3 a, Vec3 b) {
        return new Vec3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vec3 mul(Vec3 a, double b) {
        return new Vec3(a.x * b, a.y * b, a.z * b);
    }

    public static Vec3 mul(double b, Vec3 a) {
        return new Vec3(a.x * b, a.y * b, a.z * b);
    }

    public static Vec3 mul(Vec3 a, Vec3 b) {
        return new Vec3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vec3 div(Vec3 a, double b) {
        return new Vec3(a.x / b, a.y / b, a.z / b);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalized() {
        return Vec3.div(this, length());
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - b.x * a.y);
    }

    public static double dot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public String toString() {
        return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z) + ")";
    }

    public static double distance(Vec3 a, Vec3 b) {
        return Vec3.sub(a, b).length();
    }

    public static double sqDistance(Vec3 a, Vec3 b) {
        final Vec3 d = Vec3.sub(a, b);
        return Vec3.dot(d, d);
    }

    public static Vec3 zero() {
        return new Vec3(0);
    }

    public static Vec3 up() {
        return new Vec3(0, 1, 0);
    }

    /**
     * 
     * @param pos Position of the point
     * @param A   Left Bottom Front corner of the bounding box
     * @param B   Right Top Back corner of the bounding box
     * @return true if pos is inside the bouding box defined by A and B
     */
    public static boolean inside(Vec3 pos, Vec3 A, Vec3 B) {
        return pos.x >= A.x && pos.x <= B.x && pos.y >= A.y && pos.y <= B.y && pos.z >= A.z && pos.z <= B.z;
    }

    /**
     * 
     * @param pos Position of the point
     * @param rad Radius of the circle around pos
     * @param A   Left Bottom Front corner of the bounding box
     * @param B   Right Top Back corner of the bounding box
     * @return true if the circle around pos with radius of rad intersects the
     *         bouding box defined by A and B
     */
    public static boolean inside(Vec3 pos, double rad, Vec3 A, Vec3 B) {
        return pos.x + rad >= A.x && pos.x - rad <= B.x && pos.y + rad >= A.y && pos.y - rad <= B.y
                && pos.z + rad >= A.z && pos.z - rad <= B.z;
    }
}