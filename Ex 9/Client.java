import java.io.File;
import java.util.Scanner;

public class Client {
    public static String testFileName = "5clusters100points.txt";

    public static void main(String[] args) throws Exception {
        try (Scanner s = new Scanner(new File(testFileName))) {
            int k = s.nextInt();
            int n = s.nextInt();
            double[][] distances = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    distances[i][j] = Double.parseDouble(s.next());
                }
            }

            Clusterer clusterer = new Clusterer(distances, k);
            System.out.println(clusterer.getCost());
        }

        // Try out some 2d clusters of your own!
        // You can make your .csv files.
        // Try adjusting k and see how it affects the clusters.
        // Uncomment below to plot
        Plot.plotClusters("1kpoints.csv", 5);
        Plot.plotClusters("smiley.csv", 4);
        Plot.plotClusters("moons.csv", 2);
    }
}
