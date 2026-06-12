import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class SliceFilter {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.takeSlice.
    // Ignoring the initialization of arrays, your implementation must have linear
    // work and log(n) span
    public static double[][] takeSlice(double[][] arr, double[] point, double[] normal, int cutoff) {
        SliceFilter.CUTOFF = cutoff;
        // Reminder: the main steps are:
        // 1) do a map on the arr of "vectors"
        // 2) do prefix sum on the map result (implementation provided for you in
        // ParallelPrefix.java)
        // 3) initialize and array whose length matches the last value in the prefix sum
        // result
        // 4) do a map to populate that new array.
        int[] map = new int[arr.length];
        double product = DotProduct.dotProduct(point, normal, cutoff);
        POOL.invoke(new MapVectors(arr, product, normal, map, 0, arr.length));
        int[] prefixSum = ParallelPrefix.prefixSum(map, SliceFilter.CUTOFF);
        double[][] slice = new double[prefixSum[prefixSum.length - 1]][arr[0].length];
        POOL.invoke(new PopulateSlice(arr, map, prefixSum, slice, 0, arr.length));
        return slice;
    }

    private static class MapVectors extends RecursiveAction {
        private double[][] arr;
        private double product;
        private double[] normal;
        private int[] map;
        private int lo;
        private int hi;

        public MapVectors(double[][] arr, double product, double[] normal, int[] map, int lo, int hi) {
            this.arr = arr;
            this.product = product;
            this.normal = normal;
            this.map = map;
            this.lo = lo;
            this.hi = hi;
        }

        public void compute() {
            if (hi - lo <= SliceFilter.CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    map[i] = Sequential.approxEquals(DotProduct.dotProduct(arr[i], normal, SliceFilter.CUTOFF), this.product) ? 1 : 0;
                }
                return;
            }

            int mid = lo + (hi - lo) / 2;
            MapVectors left = new MapVectors(arr, product, normal, map, lo, mid);
            MapVectors right = new MapVectors(arr, product, normal, map, mid, hi);
            left.fork();
            right.compute();
            left.join();
        }
    }

    private static class PopulateSlice extends RecursiveAction {
        private double[][] arr;
        private int[] map;
        private int[] prefixSum;
        private double[][] slice;
        private int lo;
        private int hi;

        public PopulateSlice(double[][] arr, int[] map, int[] prefixSum, double[][] slice, int lo, int hi) {
            this.arr = arr;
            this.map = map;
            this.prefixSum = prefixSum;
            this.slice = slice;
            this.lo = lo;
            this.hi = hi;
        }

        public void compute() {
            if (hi - lo <= SliceFilter.CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    if (map[i] == 1) {
                        slice[prefixSum[i] - 1] = arr[i];
                    }
                }
                return;
            }

            int mid = lo + (hi - lo) / 2;
            PopulateSlice left = new PopulateSlice(arr, map, prefixSum, slice, lo, mid);
            PopulateSlice right = new PopulateSlice(arr, map, prefixSum, slice, mid, hi);
            left.fork();
            right.compute();
            left.join();
        }
    }

    private static double dotProduct(double[] a, double[] b) {
        return DotProduct.dotProduct(a, b, SliceFilter.CUTOFF);
    }
}
