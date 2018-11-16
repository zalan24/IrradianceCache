package org.irrad.geometry;

import java.util.ArrayList;
import org.irrad.graphics.*;

public class Scene {

    private ArrayList<Shape> mShapes = new ArrayList<>();

    public static Scene BasicScene() {
        Scene ret = new Scene();
        Plane p = new Plane(new Vec3(0), new Vec3(0, 1, 0));
        p.setMaterial(new Material(new Vec3(0.7), Vec3.zero()));
        ret.add(p);
        Sphere s = new Sphere(new Vec3(0, 1, 0), 1);
        ret.add(s);
        s.setMaterial(new Material(new Vec3(0.7), new Vec3()));
        Sky sky = new Sky();
        sky.setMaterial(new Material(Vec3.zero(), new Vec3(1)));
        ret.add(sky);
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
        public Ray ray;

        public Intersection(Shape s, Intersectable.Hit i, Ray r) {
            shape = s;
            intersection = i;
            ray = r;
        }
    }

    public Intersection cast(Ray ray) {
        double mindist = Double.POSITIVE_INFINITY;
        Intersectable.Hit hit = null;
        Shape ret = null;
        for (Shape s : mShapes) {
            Intersectable.Hit h = s.intersect(ray);
            if (h.dist <= mindist) {
                mindist = h.dist;
                hit = h;
                ret = s;
            }
        }
        return new Intersection(ret, hit, ray);
    }
}