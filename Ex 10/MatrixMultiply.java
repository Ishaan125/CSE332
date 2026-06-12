import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiply {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.multiply.
    // Ignoring the initialization of arrays, your implementation should have n^3
    // work and log(n) span
    public static double[][] multiply(double[][] a, double[][] b, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        double[][] product = new double[a.length][b[0].length];
        POOL.invoke(new MatrixMultiplyAction(a, b, product, 0, a.length, 0, b[0].length));
        return product;
    }

    // Behavior should match the 2d version of Sequential.dotProduct.
    // Your implementation must have linear work and log(n) span
    public static double dotProduct(double[][] a, double[][] b, int row, int col, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, row, col, 0, Math.min(a[row].length, b[col].length)));
    }

    private static class MatrixMultiplyAction extends RecursiveAction {
        private double[][] a;
        private double[][] b;
        private double[][] product;
        private int rowLo;
        private int rowHi;
        private int colLo;
        private int colHi;

        public MatrixMultiplyAction(double[][] a, double[][] b, double[][] product, int rowLo, int rowHi, int colLo, int colHi) {
            this.a = a;
            this.b = b;
            this.product = product;
            this.rowLo = rowLo;
            this.rowHi = rowHi;
            this.colLo = colLo;
            this.colHi = colHi;
        }

        public void compute() {
            if (Math.min(rowHi - rowLo, colHi - colLo) <= MatrixMultiply.CUTOFF) {
                for (int row = rowLo; row < rowHi; row++) {
                    for (int col = colLo; col < colHi; col++) {
                        product[row][col] = dotProduct(a, b, row, col, MatrixMultiply.CUTOFF);
                    }
                }
                return;
            }

            int rowMid = rowLo + (rowHi - rowLo) / 2;
            int colMid = colLo + (colHi - colLo) / 2;
            MatrixMultiplyAction leftUp = new MatrixMultiplyAction(a, b, product, rowLo, rowMid, colLo, colMid);
            MatrixMultiplyAction leftDown = new MatrixMultiplyAction(a, b, product, rowLo, rowMid, colMid, colHi);
            MatrixMultiplyAction rightUp = new MatrixMultiplyAction(a, b, product, rowMid, rowHi, colLo, colMid);
            MatrixMultiplyAction rightDown = new MatrixMultiplyAction(a, b, product, rowMid, rowHi, colMid, colHi);
            leftUp.fork();
            leftDown.fork();
            rightUp.fork();
            rightDown.compute();
            leftUp.join();
            leftDown.join();
            rightUp.join();
        }
    }

    private static class DotProductTask extends RecursiveTask<Double>{
        private double[][] a;
        private double[][] b;
        private int row;
        private int col;
        private int lo;
        private int hi;

        public DotProductTask(double[][] a, double[][] b, int row, int col, int lo, int hi){
            this.a = a;
            this.b = b;
            this.row = row;
            this.col = col;
            this.lo = lo;
            this.hi = hi;
        }

        public Double compute() {
            if (hi - lo <= MatrixMultiply.CUTOFF) {
                Double sum = 0.0;
                for (int i = lo; i < hi; i++) {
                    sum += a[row][i] * b[i][col];
                }
                return sum;
            }

            int mid = lo + (hi - lo) / 2;
            DotProductTask left = new DotProductTask(a, b, row, col, lo, mid);
            left.fork();
            DotProductTask right = new DotProductTask(a, b, row, col, mid, hi);
            Double r = right.compute();
            return left.join() + r;
        }
    }
}
