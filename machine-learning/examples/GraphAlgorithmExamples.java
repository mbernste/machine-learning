import java.util.Map;

import graph.bellmanford.BellmanFord;
import graph.bellmanford.Path;


public class GraphAlgorithmExamples 
{

    public static void main(String[] args)
    {
        bellmanFordExample();
    }
    
    
    /**
     * Running the Bellman-Ford algorithm to find the shortest paths to all
     * nodes from a source node
     */
    public static void bellmanFordExample()
    {
        Double[][] graph = createToyGraph_NoNegativeCycles();
        
        // Designate the source node
        int sourceNode = 0;
        
        // Run Bellman-Ford
        Map<Integer, Path> shortestPaths = BellmanFord.runBellmanFord(graph, sourceNode);
        
        // Print each path
        for (Path path : shortestPaths.values())
        {
            System.out.println(path);
        }  
    }
    
    /**
     * @return an example graph with no negative cycles.
     */
    public static Double[][] createToyGraph_NoNegativeCycles()
    {
        Double[][] graph = new Double[5][5];
        
        graph[0][1] = 3d;
        graph[0][4] = 7d;
        graph[1][2] = 5d;
        graph[1][4] = 8d;
        graph[1][3] = -4d;
        graph[2][1] = -2d;
        graph[3][2] = 7d;
        graph[3][0] = 2d;
        graph[4][2] = -3d;
        graph[4][3] = 9d;
        
        return graph;
    }
}
