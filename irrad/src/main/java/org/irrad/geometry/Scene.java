package org.irrad.geometry;

import java.util.ArrayList;

public class Scene {

    private ArrayList<Shape> mShapes = new ArrayList<>();

    public static Scene BasicScene() {
        Scene ret = new Scene();
        ret.add(new Plane(new Vec3(0), new Vec3(0, 1, 0)));
        ret.add(new Sphere(new Vec3(0, 1, 0), 1));
        return ret;
    }

    public void add(Shape s) {
        mShapes.add(s);
    }

    public void remove(Shape s) {
        mShapes.remove(s);
    }

    public class Intersection {
        public Shape shape;
        public Intersectable.Hit intersection;

        public Intersection(Shape s, Intersectable.Hit i) {
            shape = s;
            intersection = i;
        }
    }

    public Intersection cast(Ray ray) {
        double mindist = Double.POSITIVE_INFINITY;
        Intersectable.Hit hit = null;
        Shape ret = null;
        for (Shape s : mShapes) {
            Intersectable.Hit h = s.intersect(ray);
            if (h.dist < mindist) {
                mindist = h.dist;
                hit = h;
                ret = s;
            }
        }
        return new Intersection(ret, hit);
    }
}