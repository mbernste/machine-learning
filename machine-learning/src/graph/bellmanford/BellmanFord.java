package graph.bellmanford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Bellman-Ford algorithm.  Given a directed graph
 * and some specified node in the graph, this algorithm outputs the shortest
 * path and distance of this path to all other nodes in the graph.  The 
 * Bellman-Ford algorithm can detect negative cycles.
 */
public class BellmanFord 
{
    private static final Integer NULL_NODE = -1;

    /**
     * Run the Bellman-Ford algorithm to find the shortest paths from the source
     * node to each other node.
     * 
     * @param graph the i,jth value indicates the edge weight from node i to
     * node j
     * @param sourceNode the source node
     * @return a map that maps every node to the shortest path from the source
     * node that node
     */
    public static Map<Integer, Path> runBellmanFord(Double[][] graph, 
                                                            Integer sourceNode)
    {
        /*
         * Maps a node ID to the current upper bound on the shortest distance.
         * By the end of the algorithm, this value will store the true shortest
         * distance from the source to that node.
         */
        Map<Integer, Double> nodeToShortestDistance = new HashMap<>();

        /*
         * Maps a node ID to the node's predecessor in the shortest path.
         * By the end of this algorithm, this data structure can be traversed
         * to recover the shortest path from each node to the source.
         */
        Map<Integer, Integer> nodeToPredecessor = new HashMap<>();

        initialize(graph, nodeToShortestDistance, nodeToPredecessor, sourceNode);

        int numPasses = graph.length - 1;
        for (int pass = 0; pass < numPasses; pass++)
        {
            passOverEdges(graph, nodeToShortestDistance, nodeToPredecessor);
        }
        
        if (detectNegativeCycle(graph))
        {
            return null;
        }
        else
        {
            return constructShortestPaths(graph,
                                          sourceNode,
                                          nodeToShortestDistance,
                                          nodeToPredecessor);
        }        
   }

    /**
     * Pass over and relax all edges.
     * 
     * @param graph the i,jth value indicates the edge weight from node i to
     * node j
     * @param nodeToShortestDistance shortest distance upper bound for each node
     * @param nodeToPRedecessor the predecessor to each node in the shortest
     * path from the source node
     */
    private static void passOverEdges(Double[][] graph, 
                                      Map<Integer, Double> nodeToShortestDistance, 
                                      Map<Integer, Integer> nodeToPRedecessor) 
    {
        for (int orig = 0; orig < graph.length; orig++)
        {
            for (int dest = 0; dest < graph.length; dest++)
            {
                Double weight = graph[orig][dest];
                if (weight != null)
                {
                    relaxEdge(orig, dest, weight, 
                        nodeToShortestDistance, nodeToPRedecessor);
                }
            }
        }
    }

    private static void relaxEdge(Integer origin, 
                                  Integer destination,
                                  Double weight,
                                  Map<Integer, Double> nodeToShortestDistance,
                                  Map<Integer, Integer> nodeToPredecessor)
    {
        double potentialDistance = nodeToShortestDistance.get(origin) + weight;
        if (nodeToShortestDistance.get(destination) > potentialDistance)
        {
            nodeToShortestDistance.put(destination, potentialDistance);
            nodeToPredecessor.put(destination, origin);
        }
    }

    private static boolean detectNegativeCycle(Double[][] graph)
    {
        for (int orig = 0; orig < graph.length; orig++)
        {
            for (int dest = 0; dest < graph.length; dest++)
            {
               // TODO something 
            }
        }
        
        return false;
    }
    
    
    private static Map<Integer, Path> constructShortestPaths(Double[][] graph,
                                                             Integer sourceNode,
                                                             Map<Integer, Double> nodeToShortestDistance, 
                                                             Map<Integer, Integer> nodeToPredecessor)
    {
        Map<Integer, Path> nodeToPath = new HashMap<>();
        for (Integer toNode : nodeToShortestDistance.keySet())
        {   
            Path path = new Path(sourceNode, toNode);  
            List<Integer> trace = getTracedPath(sourceNode, toNode, nodeToPredecessor);
                        
            for (int i = 1; i < trace.size(); i++)
            {                
                path.appendNodeToPath(trace.get(i), 
                                      graph[trace.get(i-1)][trace.get(i)]);
            }
            nodeToPath.put(toNode, path);
        }
        return nodeToPath;
    }
    
    /**
     * Trace through the predecessors to construct the shortest path from the 
     * source node to the destination node
     *  
     * @param sourceNode the source node
     * @param destinationNode the destination node
     * @param nodeToPredecessor maps each node to it's predecessor in the 
     * shortest path
     * @return the constructed shortest path from the source node to the
     * destination
     */
    private static List<Integer> getTracedPath(Integer sourceNode, 
                                               Integer destinationNode, 
                                               Map<Integer, Integer> nodeToPredecessor)
    {
        List<Integer> path = new ArrayList<>();
        int currNode = destinationNode;
        while (currNode != sourceNode)
        {
            path.add(currNode);
            currNode = nodeToPredecessor.get(currNode);   
        }
        path.add(sourceNode);
        Collections.reverse(path);
        return path;
    }
    
    
    /**
     * For all nodes except the source node, set shortest distance values to 
     * infinity.  Set all predecessor node values to the null node.
     * 
     * @param graph the edge weights between the i,jth nodes
     * @param nodeToShortestDistance shortest distance upper bound for each node
     * @param nodeToPRedecessor the predecessor to each node in the shortest
     * path from the source node
     * @param sourceNode the source node
     */
    private static void initialize(Double[][] graph, 
            Map<Integer, Double> nodeToShortestDistance, 
            Map<Integer, Integer> nodeToPRedecessor, 
            Integer sourceNode)
    {
        for (int node = 0; node < graph.length; node++)
        {
            if (node != sourceNode)
            {
                nodeToShortestDistance.put(node, Double.POSITIVE_INFINITY);
            }
            else
            {
                nodeToShortestDistance.put(node, 0d);
            }
            nodeToPRedecessor.put(node, NULL_NODE);
        }
    }
}
