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

    public static Scene CornellBox() {
        Scene ret = new Scene();
        Plane bottom = new Plane(new Vec3(0), new Vec3(0, 1, 0));
        bottom.setMaterial(new Material(new Vec3(0.9), Vec3.zero()));
        ret.add(bottom);
        Plane top = new Plane(new Vec3(0, 1, 0), new Vec3(0, -1, 0));
        top.setMaterial(new Material(new Vec3(0.9), Vec3.zero()));
        ret.add(top);
        Plane back = new Plane(new Vec3(0, 0, 1), new Vec3(0, 0, -1));
        back.setMaterial(new Material(new Vec3(0.9), Vec3.zero()));
        ret.add(back);
        Plane left = new Plane(new Vec3(0, 0, 0), new Vec3(1, 0, 0));
        left.setMaterial(new Material(new Vec3(0.9, 0, 0), Vec3.zero()));
        ret.add(left);
        Plane right = new Plane(new Vec3(1, 0, 0), new Vec3(-1, 0, 0));
        right.setMaterial(new Material(new Vec3(0, 0.9, 0), Vec3.zero()));
        ret.add(right);

        Sphere s = new Sphere(new Vec3(0.5, 1, 0.5), 0.1);
        ret.add(s);
        s.setMaterial(new Material(new Vec3(), Vec3.mul(10, new Vec3(0.8, 0.75, 0.6))));

        Sphere s2 = new Sphere(new Vec3(0.5, 0.5, 0.5), 0.2);
        ret.add(s2);
        s2.setMaterial(new Material(new Vec3(0.4), Vec3.zero()));
        Sky sky = new Sky();
        sky.setMaterial(new Material(Vec3.zero(), new Vec3(1)));
        ret.add(sky);
        return ret;
    }

    public static Scene SpheresScene() {
        Scene ret = new Scene();
        Plane bottom = new Plane(new Vec3(0), new Vec3(0, 1, 0));
        bottom.setMaterial(new Material(new Vec3(0.9, 0.1, 0.1), Vec3.zero()));
        ret.add(bottom);
        Plane back = new Plane(new Vec3(1, 0, 1), new Vec3(-1, 0, -1).normalized());
        back.setMaterial(new Material(new Vec3(0.1, 0.9, 0.1), Vec3.zero()));
        ret.add(back);

        Sphere s1 = new Sphere(new Vec3(0.85, 0.201, 0.65), 0.2);
        ret.add(s1);
        s1.setMaterial(new Material(new Vec3(0.4), Vec3.zero()));
        Sphere s2 = new Sphere(new Vec3(0.65, 0.201, 0.85), 0.2);
        ret.add(s2);
        s2.setMaterial(new Material(new Vec3(0.4), Vec3.zero()));
        Sphere s3 = new Sphere(new Vec3(0.5, 0.201, 0.5), 0.2);
        ret.add(s3);
        s3.setMaterial(new Material(new Vec3(0.4), Vec3.zero()));
        Sphere s4 = new Sphere(new Vec3(0.6, 0.553, 0.6), 0.2);
        ret.add(s4);
        s4.setMaterial(new Material(new Vec3(0.4), Vec3.zero()));

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