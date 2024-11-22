package ch.ivyteam.java21.vector;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

public class VectorAPI {

  private static final int LOOPS = 10000;

  static void normal(float[] a, float[] b, float[] c) {
    for (int i = 0; i < a.length; i++) {
         c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
    }
  }

  static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

  static void simd(float[] a, float[] b, float[] c) {
      int i = 0;
      int upperBound = SPECIES.loopBound(a.length);
      for (; i < upperBound; i += SPECIES.length()) {
          // FloatVector va, vb, vc;
          var va = FloatVector.fromArray(SPECIES, a, i);
          var vb = FloatVector.fromArray(SPECIES, b, i);
          var vc = va.mul(va)
                     .add(vb.mul(vb))
                     .neg();
          vc.intoArray(c, i);
      }
      for (; i < a.length; i++) {
          c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
      }
  }

  public static void main(String[] args) {
    float[] a = new float[1000000];
    float[] b = new float[a.length];
    float[] c = new float[a.length];

    for (int pos = 0; pos < a.length; pos++) {
      a[pos] = (float) Math.random();
      b[pos] = (float) Math.random();
    }

    normalLoop(a, b, c);
    simdLoop(a, b, c);
    normalLoop(a, b, c);
    simdLoop(a, b, c);
    normalLoop(a, b, c);
    simdLoop(a, b, c);
  }

  private static void simdLoop(float[] a, float[] b, float[] c) {
    long simd = 0;
    long now = System.nanoTime();
    for (int pos = 0; pos < LOOPS; pos++) {
      simd(a, b, c);
    }
    simd = System.nanoTime() - now;
    System.out.println("Simd: " + simd);
  }

  private static void normalLoop(float[] a, float[] b, float[] c) {
    long normal=0;
    long now = System.nanoTime();
    for (int pos = 0; pos < LOOPS; pos++) {
      normal(a, b, c);
    }
    normal = System.nanoTime() - now;
    System.out.println("Norm: " + normal);
  }
}
