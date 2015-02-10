package examples;
import java.util.Map;

import graph.DirectedGraph;
import graph.Path;
import graph.bellmanford.BellmanFord;
import graph.floydwarshall.AllPairsShortestPaths;
import graph.floydwarshall.FloydWarshall;


public class GraphAlgorithmExamples 
{

    public static void main(String[] args)
    {
        bellmanFordExample();
        floydWarshallExample();
    }
    
    
    /**
     * Run the Bellman-Ford algorithm to find the shortest paths to all
     * nodes from a source node.
     */
    public static void bellmanFordExample()
    {
        DirectedGraph<String> graph = createToyGraph_NoNegativeCycles();
        
        // Designate the source node
        String sourceNode = "A";
        
        // Run Bellman-Ford
        Map<String, Path<String>> shortestPaths = BellmanFord.runBellmanFord(graph, sourceNode);
        
        // Print each path
        for (Path<String> path : shortestPaths.values())
        {
            System.out.println(path);
        }  
    }
    
    public static void floydWarshallExample()
    {
        DirectedGraph<String> graph = createToyGraph_NoNegativeCycles();

        AllPairsShortestPaths<String> paths = FloydWarshall.runFloydWarshall(graph);
        
        System.out.println(paths.getPath("A", "B"));
    }
    
    /**
     * @return an example graph with no negative cycles.
     */
    public static DirectedGraph<String> createToyGraph_NoNegativeCycles()
    {
        DirectedGraph<String> graph = new DirectedGraph<>();
        
        graph.addEdge("A", "B", 3d);
        graph.addEdge("A", "E", 7d);
        graph.addEdge("B", "C", 5d);
        graph.addEdge("B", "E", 8d);
        graph.addEdge("B", "D", -4d);
        graph.addEdge("C", "B", -2d);
        graph.addEdge("D", "C", 7d);
        graph.addEdge("D", "A", 2d);
        graph.addEdge("E", "C", -3d);
        graph.addEdge("E", "D", 9d);

        return graph;
    }
}
