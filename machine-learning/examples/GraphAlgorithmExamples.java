import graph.bellmanford.BellmanFord;


public class GraphAlgorithmExamples 
{

    public static void main(String[] args)
    {
        Double[][] graph = createToyGraph();
        
        int sourceNode = 0;
        BellmanFord.runBellmanFord(graph, sourceNode);
        
    }
    
    public static Double[][] createToyGraph()
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
