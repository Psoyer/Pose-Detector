/*
 * @Author: yyj-wiki
 * @Creation Date:   2023-03-12
 */


package com.nxnu.sjxy.vision.demo.java.posedetector.classification;

import static com.google.common.primitives.Floats.max;

import com.google.mlkit.vision.common.PointF3D;
import java.util.List;
import java.util.ListIterator;

/**
 * 工具类 for operations.
 */
public class Utils {
  private Utils() {}

  public static PointF3D add(PointF3D a, PointF3D b) {
    return PointF3D.from(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
  }

  public static PointF3D subtract(PointF3D b, PointF3D a) {
    return PointF3D.from(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
  }

  public static PointF3D multiply(PointF3D a, float multiple) {
    return PointF3D.from(a.getX() * multiple, a.getY() * multiple, a.getZ() * multiple);
  }

  public static PointF3D multiply(PointF3D a, PointF3D multiple) {
    return PointF3D.from(
        a.getX() * multiple.getX(), a.getY() * multiple.getY(), a.getZ() * multiple.getZ());
  }

  public static PointF3D average(PointF3D a, PointF3D b) {
    return PointF3D.from(
        (a.getX() + b.getX()) * 0.5f, (a.getY() + b.getY()) * 0.5f, (a.getZ() + b.getZ()) * 0.5f);
  }

  public static float l2Norm2D(PointF3D point) {
    return (float) Math.hypot(point.getX(), point.getY());
  }

  public static float maxAbs(PointF3D point) {
    return max(Math.abs(point.getX()), Math.abs(point.getY()), Math.abs(point.getZ()));
  }

  public static float sumAbs(PointF3D point) {
    return Math.abs(point.getX()) + Math.abs(point.getY()) + Math.abs(point.getZ());
  }

  public static void addAll(List<PointF3D> pointsList, PointF3D p) {
    ListIterator<PointF3D> iterator = pointsList.listIterator();
    while (iterator.hasNext()) {
      iterator.set(add(iterator.next(), p));
    }
  }

  public static void subtractAll(PointF3D p, List<PointF3D> pointsList) {
    ListIterator<PointF3D> iterator = pointsList.listIterator();
    while (iterator.hasNext()) {
      iterator.set(subtract(p, iterator.next()));
    }
  }

  public static void multiplyAll(List<PointF3D> pointsList, float multiple) {
    ListIterator<PointF3D> iterator = pointsList.listIterator();
    while (iterator.hasNext()) {
      iterator.set(multiply(iterator.next(), multiple));
    }
  }

  public static void multiplyAll(List<PointF3D> pointsList, PointF3D multiple) {
    ListIterator<PointF3D> iterator = pointsList.listIterator();
    while (iterator.hasNext()) {
      iterator.set(multiply(iterator.next(), multiple));
    }
  }
}
