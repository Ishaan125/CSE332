import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Plot extends JPanel {
    private final List<List<Pair>> clusters;
    private final int pointPixelSize;
    private final int padding;

    public static void plotClusters(String pointsFile, int numClusters) {
        List<Pair> points = loadPoints(pointsFile);
        double[][] distancesGrid = points2distances(points);
        Clusterer c = new Clusterer(distancesGrid, numClusters);
        List<List<Integer>> byIndex = c.getClusters();
        List<List<Pair>> byPoint = index2point(byIndex, points);
        plot(byPoint);
    }

    private static List<List<Pair>> index2point(List<List<Integer>> byIndex, List<Pair> points) {
        List<List<Pair>> byPoints = new ArrayList<>();
        for (List<Integer> indexCluster : byIndex) {
            List<Pair> pointCluster = new ArrayList<>();
            for (int i : indexCluster) {
                pointCluster.add(points.get(i));
            }
            byPoints.add(pointCluster);
        }
        return byPoints;
    }

    private static double[][] points2distances(List<Pair> points) {
        double[][] distances = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                distances[i][j] = points.get(i).dist(points.get(j));
            }
        }
        return distances;
    }

    private static List<Pair> loadPoints(String filename) {
        List<Pair> points = new ArrayList<>();
        File input = new File(filename);
        try (Scanner inputReader = new Scanner(input)) {
            while (inputReader.hasNext()) {
                String line = inputReader.nextLine();
                double x = Double.parseDouble(line.split(",")[0]);
                double y = Double.parseDouble(line.split(",")[1]);
                points.add(new Pair(x, y));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return points;
    }

    public Plot(List<List<Pair>> clusters) {
        this(clusters, 12, 20);
    }

    public Plot(List<List<Pair>> clusters, int pointPixelSize, int padding) {
        this.clusters = clusters;
        this.pointPixelSize = Math.max(1, pointPixelSize);
        this.padding = Math.max(0, padding);
        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(800, 600));
    }

    public static void plot(List<List<Pair>> clusters) {
        JFrame frame = new JFrame("Clusters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Plot plot = new Plot(clusters);
        frame.getContentPane().add(plot);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clusters == null || clusters.isEmpty())
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
            boolean hasPoint = false;
            for (List<Pair> cluster : clusters) {
                if (cluster == null)
                    continue;
                for (Pair p : cluster) {
                    hasPoint = true;
                    double x = p.getX(), y = p.getY();
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
            if (!hasPoint)
                return;

            double dataWidth = maxX - minX;
            double dataHeight = maxY - minY;
            if (dataWidth == 0)
                dataWidth = 1;
            if (dataHeight == 0)
                dataHeight = 1;

            int w = getWidth();
            int h = getHeight();

            double availW = Math.max(1, w - 2.0 * padding);
            double availH = Math.max(1, h - 2.0 * padding);

            double scale = Math.min(availW / dataWidth, availH / dataHeight);

            double usedW = dataWidth * scale;
            double usedH = dataHeight * scale;
            double extraX = (availW - usedW) / 2.0;
            double extraY = (availH - usedH) / 2.0;

            int clusterIndex = 0;

            for (List<Pair> cluster : clusters) {
                if (cluster == null || cluster.isEmpty()) {
                    clusterIndex++;
                    continue;
                }

                float rot = ((float) clusterIndex * 0.61803398875f) % 1f;
                Color base = Color.getHSBColor(rot, 1f, 1f);

                for (Pair p : cluster) {
                    double dx = p.getX();
                    double dy = p.getY();
                    double px = padding + extraX + (dx - minX) * scale;
                    double py = padding + extraY + (maxY - dy) * scale;

                    int r = pointPixelSize / 2;
                    int ix = (int) Math.round(px) - r;
                    int iy = (int) Math.round(py) - r;
                    g2.setColor(base);
                    g2.fillOval(ix, iy, pointPixelSize, pointPixelSize);

                    g2.setColor(Color.BLACK);
                    g2.drawOval(ix, iy, pointPixelSize, pointPixelSize);

                    Color textColor = 0.2126 * base.getRed() / 255.0
                            + 0.7152 * base.getGreen() / 255.0
                            + 0.0722 * base.getBlue() / 255.0 > 0.5 ? Color.BLACK : Color.WHITE;
                    g2.setColor(textColor);

                    float fontSize = pointPixelSize * 0.65f;
                    Font labelFont = g2.getFont().deriveFont(Font.BOLD, fontSize);
                    g2.setFont(labelFont);

                    FontMetrics fm = g2.getFontMetrics(labelFont);
                    int textWidth = fm.stringWidth(clusterIndex + "");
                    int textAscent = fm.getAscent();
                    int textDescent = fm.getDescent();

                    int textX = (int) Math.round(px) - textWidth / 2;
                    int textY = (int) Math.round(py) + (textAscent - textDescent) / 2;

                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.drawString(clusterIndex + "", textX, textY);

                    g2.setColor(textColor);
                    g2.drawString(clusterIndex + "", textX, textY);
                }
                clusterIndex++;
            }

        } finally {
            g2.dispose();
        }
    }

    private static class Pair {
        double x;
        double y;

        public Pair(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Pair other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }
}
