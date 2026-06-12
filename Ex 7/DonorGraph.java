import java.util.*;

public class DonorGraph {
    public List<List<Match>> adjList;

    // The donatingTo array indicates which repient each donor is
    // affiliated with. Specifically, the donor at index i has volunteered
    // to donate a kidney on behalf of recipient donatingTo[i].
    // The matchScores 2d array gives the match scores associated with each
    // donor-recipient pair. Specificically, matchScores[x][y] gives the
    // HLA score for donor x and reciplient y.
    public DonorGraph(int[] donorToBenefit, int[][] matchScores) {
        adjList = new ArrayList<>();
        int numRecipients = (matchScores.length > 0) ? matchScores[0].length : 0;
        for (int i = 0; i < numRecipients; i++) {
            adjList.add(new ArrayList<>());
        }
        for (int donor = 0; donor < donorToBenefit.length; donor++) {
            int beneficiary = donorToBenefit[donor];
            for (int recipient = 0; recipient < numRecipients; recipient++) {
                if (matchScores[donor][recipient] >= 60) {
                    adjList.get(beneficiary).add(new Match(donor, beneficiary, recipient));
                }
            }
        }
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public boolean isAdjacent(int start, int end) {
        for (Match m : adjList.get(start)) {
            if (m.recipient == end)
                return true;
        }
        return false;
    }

    // Will be used by the autograder to verify your graph's structure.
    // It's probably also going to helpful for your debugging.
    public int getDonor(int beneficiary, int recipient) {
        for (Match m : adjList.get(beneficiary)) {
            if (m.recipient == recipient)
                return m.donor;
        }
        return -1;
    }


    // returns a chain of matches to make a donor cycle
    // which includes the given recipient.
    // Returns an empty list if no cycle exists.
    public List<Match> findCycle(int recipient) {
        int len = adjList.size();
        boolean[] visited = new boolean[len];
        boolean[] done = new boolean[len];
        List<Match> cycle = new ArrayList<>();
        helpCycle(recipient, recipient, visited, done, cycle);
        return cycle;
    }

    // Helper method for findCycle. Uses DFS to find a cycle starting from the given recipient.
    // visited keeps track of which nodes have been visited in the current path, while done keeps track of which nodes have been fully explored.
    // cycle is used to store the current path of matches being explored. If a cycle is found, it will be stored in cycle.
    private boolean helpCycle(int recipient, int curr, boolean[] visited, boolean[] done, List<Match> cycle) {
        visited[curr] = true;
        for (Match m : adjList.get(curr)) {
            cycle.add(m);
            int v = m.recipient;
            if (v == recipient) {
                return true;
            } 
            else if (!visited[v] && !done[v]) {
                if (helpCycle(recipient, v, visited, done, cycle)) {
                    return true;
                }
            }
            cycle.remove(cycle.size() - 1); // backtrack
        }
        done[curr] = true;
        return false;
    }

    // returns true or false to indicate whether there
    // is some cycle which includes the given recipient.
    public boolean hasCycle(int recipient) {
        return findCycle(recipient).size() > 0;
    }
}
