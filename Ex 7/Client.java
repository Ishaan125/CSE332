import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client{
    // Try some other files too!
    public static String testFileName = "example.txt";

    public static void main(String[] args){
        File testFile = new File(testFileName);
        try{
            Scanner s = new Scanner(testFile);

            int numRecipients = s.nextInt();
            int numDonors = s.nextInt();

            String donorsLine = s.next();
            String[] donorsArray = donorsLine.split(",",0);
            int[] donorToBenefit = new int[donorsArray.length];
            for(int i=0; i<numDonors; i++){
                donorToBenefit[i] = Integer.parseInt(donorsArray[i]);
            }

            int[][] matchScores = new int[numDonors][numRecipients];
            for(int i=0; i<numDonors; i++){
                String matchscoreLine = s.next();
                String[] matchscoreArray = matchscoreLine.split(",",0);
                for(int j=0; j<numRecipients; j++){
                    matchScores[i][j] = Integer.parseInt(matchscoreArray[j]);
                }	
            }
            int query = s.nextInt();
            s.close();
            DonorGraph graph = new DonorGraph(donorToBenefit, matchScores);
            System.out.println(graph.findCycle(query));
            System.out.println(graph);
            System.out.println("COPY THE LINK BELOW TO SEE THE GRAPH VISUALLY");
            System.out.println(toGraphVizUrl(graph));
            System.out.println("COPY THE LINK ABOVE TO SEE THE GRAPH VISUALLY");
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    // Helpers for visualization. You don't need to understand these to complete the assignment.
    private static String toGraphVizUrl(DonorGraph graph){
        String dot = toGraphVizDot(graph);
        String encoded = URLEncoder.encode(dot, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return "https://dreampuf.github.io/GraphvizOnline/#" + encoded;
    }

    private static String toGraphVizDot(DonorGraph graph){
        StringBuilder sb = new StringBuilder();
        sb.append("digraph{");
        if(graph == null || graph.adjList == null){
            sb.append("}");
            return sb.toString();
        }

        int n = graph.adjList.size();
        for(int i = 0; i < n; i++){
            sb.append(i).append(";");
        }

        for(int beneficiary = 0; beneficiary < n; beneficiary++){
            for(Match m : graph.adjList.get(beneficiary)){
                sb.append(beneficiary)
                  .append("->")
                  .append(m.recipient)
                  .append("[label=d")
                  .append(m.donor)
                  .append("];");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}