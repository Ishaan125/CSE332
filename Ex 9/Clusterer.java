import java.util.*;

public class Clusterer {
    private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
    private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
    private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
    private double cost; // the distance between the closest pair of clusters
    private int length;

    public Clusterer(double[][] distances, int k) {
        adjList = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            List<WeightedEdge<Integer, Double>> edges = new ArrayList<>();
            for (int j = 0; j < distances[0].length; j++) {
                if (i != j) {
                    edges.add(new WeightedEdge<>(i, j, distances[i][j]));
                }
            }
            adjList.add(edges);
        }
        length = distances.length;
        prims(0);
        makeKCluster(k);
    }

    // implement Prim's algorithm to find a MST of the graph.
    // in my implementation I used the mstAdjList field to store this.
    private void prims(int start) {
        mstAdjList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            mstAdjList.add(new ArrayList<>());
        }

        boolean[] visited = new boolean[length];
        PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();

        // start from the given vertex
        visited[start] = true;
        for (WeightedEdge<Integer, Double> v : adjList.get(start)) {
            pq.add(v);
        }

        while (!pq.isEmpty()) {
            WeightedEdge<Integer, Double> node = pq.poll();
            int src = node.source;
            int dest = node.destination;

            if (visited[dest]) {
                continue;
            }

            // add edge to MST adjacency list
            mstAdjList.get(src).add(node);
            mstAdjList.get(dest).add(new WeightedEdge<>(dest, src, node.weight));
            visited[dest] = true;

            // add edges from the newly visited vertex
            for (WeightedEdge<Integer, Double> adj : adjList.get(dest)) {
                if (!visited[adj.destination]) {
                    pq.add(adj);
                }
            }
        }
    }


    // After making the minimum spanning tree, use this method to
    // remove its k-1 heaviest edges, then assign integers
    // to clusters based on which nodes are still connected by
    // the remaining MST edges.
    private void makeKCluster(int k) {
        // collect each undirected MST edge only once
        List<WeightedEdge<Integer, Double>> allEdges = new ArrayList<>();
        for (int i = 0; i < mstAdjList.size(); i++) {
            for (WeightedEdge<Integer, Double> e : mstAdjList.get(i)) {
                if (e.source < e.destination) {
                    allEdges.add(e);
                }
            }
        }

        // sort edges by weight in descending order
        allEdges.sort(Comparator.comparing((WeightedEdge<Integer, Double> e) -> e.weight).reversed());

        // remove the k-1 heaviest edges
        for (int i = 0; i < k - 1; i++) {
            WeightedEdge<Integer, Double> edge = allEdges.remove(0);
            if (i == k - 2) {
                cost = edge.weight;
            }
            // remove all matching edges from the MST adjacency list
            mstAdjList.get(edge.source).removeIf(e -> e.destination.equals(edge.destination));
            mstAdjList.get(edge.destination).removeIf(e -> e.destination.equals(edge.source));
        }

        // BFS to find clusters
        clusters = new ArrayList<>();
        boolean[] visited = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (!visited[i]) {
                List<Integer> cluster = new ArrayList<>();
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    cluster.add(node);
                    for (WeightedEdge<Integer, Double> edge : mstAdjList.get(node)) {
                        if (!visited[edge.destination]) {
                            visited[edge.destination] = true;
                            queue.add(edge.destination);
                        }
                    }
                }
                clusters.add(cluster);
            }
        }
    }

    public List<List<Integer>> getClusters() {
        return clusters;
    }

    public double getCost() {
        return cost;
    }

}
