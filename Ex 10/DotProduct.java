import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DotProduct {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.dotProduct
    // Your implementation must have linear work and log(n) span
    public static double dotProduct(double[] a, double[]b, int cutoff){
        DotProduct.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, 0, a.length)); 
    }

    private static class DotProductTask extends RecursiveTask<Double>{
        private double[] a;
        private double[] b;
        private int lo;
        private int hi;

        public DotProductTask(double[] a, double[] b, int lo, int hi){
            this.a = a;
            this.b = b;
            this.lo = lo;
            this.hi = hi;
        }

        public Double compute(){
            if (hi - lo <= DotProduct.CUTOFF) {
                Double sum = 0.0;
                for (int i = lo; i < hi; i++) {
                    sum += a[i] * b[i];
                }
                return sum;
            }

            int mid = lo + (hi - lo) / 2;
            DotProductTask left = new DotProductTask(a, b, lo, mid);
            left.fork();
            DotProductTask right = new DotProductTask(a, b, mid, hi);
            Double r = right.compute();
            return left.join() + r;
        }
    }
}