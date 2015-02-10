package graph.bellmanford;

import graph.DirectedGraph;
import graph.Path;

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
	/**
	 * Run the Bellman-Ford algorithm.
	 * 
	 * @param graph the input graph
	 * @param sourceNode the node from which shortest paths will be computed
	 * @return a map mapping each node to the shortest path from the source node
	 */
    public static <T> Map<T, Path<T>> runBellmanFord(DirectedGraph<T> graph, 
                                                     T sourceNode)
    {
        
        /*
         * Maps a node ID to the current upper bound on the shortest distance.
         * By the end of the algorithm, this value will store the true shortest
         * distance from the source to that node.
         */
        Map<T, Double> nodeToShortestDistance = new HashMap<>();
        
        /*
         * Maps a node ID to the node's predecessor in the shortest path.
         * By the end of this algorithm, this data structure can be traversed
         * to recover the shortest path from each node to the source.
         */
        Map<T, T> nodeToPredecessor = new HashMap<>();
        
        initialize(graph, nodeToShortestDistance, nodeToPredecessor, sourceNode);

        for (int pass = 0; pass < graph.getNodes().size() - 1; pass++)
        {
            passOverEdges(graph, nodeToShortestDistance, nodeToPredecessor);
        }
        
        if (detectNegativeCycle(graph, nodeToShortestDistance))
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
     * For all nodes except the source node, set shortest distance values to 
     * infinity.  Set all predecessor node values to the null node.
     * 
     * @param graph the edge weights between the i,jth nodes
     * @param nodeToShortestDistance shortest distance upper bound for each node
     * @param nodeToPRedecessor the predecessor to each node in the shortest
     * path from the source node
     * @param sourceNode the source node
     */
    private static <T> void initialize(DirectedGraph<T> graph, 
            Map<T, Double> nodeToShortestDistance, 
            Map<T, T> nodeToPRedecessor, 
            T sourceNode)
    {
        for (T node : graph.getNodes())
        {
            if (node != sourceNode)
            {
                nodeToShortestDistance.put(node, Double.POSITIVE_INFINITY);
            }
            else
            {
                nodeToShortestDistance.put(node, 0d);
            }
            nodeToPRedecessor.put(node, null);
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
    private static <T> void passOverEdges(DirectedGraph<T> graph, 
                                      Map<T, Double> nodeToShortestDistance, 
                                      Map<T, T> nodeToPRedecessor) 
    {
        for (T orig : graph.getNodes())
        {
            for (T dest : graph.getNodes())
            {
                Double weight = graph.getEdgeWeight(orig, dest);
                if (weight != null)
                {
                    relaxEdge(orig, dest, weight, 
                        nodeToShortestDistance, nodeToPRedecessor);
                }
            }
        }
    }
    
    private static <T> void relaxEdge(T origin, 
            T destination,
            Double weight,
            Map<T, Double> nodeToShortestDistance,
            Map<T, T> nodeToPredecessor)
    {
        double potentialDistance = nodeToShortestDistance.get(origin) + weight;
        if (nodeToShortestDistance.get(destination) > potentialDistance)
        {
            nodeToShortestDistance.put(destination, potentialDistance);
            nodeToPredecessor.put(destination, origin);
        }
    }

    private static <T> boolean detectNegativeCycle(DirectedGraph<T> graph,
            Map<T, Double> nodeToShortestDistance)
    {
        for (T orig : graph.getNodes())
        {
            for (T dest : graph.getNodes())
            {
               
               if (graph.edgeExists(orig, dest) &&
                   nodeToShortestDistance.get(dest) > nodeToShortestDistance.get(orig) + graph.getEdgeWeight(orig, dest));
               {
                   // TODO FIX THERE IS A BUG HERE!!!
                   System.out.println("HERE!");
                   return false;
               }
            }
        }
        
        return false;
    }
    
    
    private static <T> Map<T, Path<T>> constructShortestPaths(DirectedGraph<T> graph,
                                                             T sourceNode,
                                                             Map<T, Double> nodeToShortestDistance, 
                                                             Map<T, T> nodeToPredecessor)
    {
        Map<T, Path<T>> nodeToPath = new HashMap<>();
        for (T toNode : nodeToShortestDistance.keySet())
        {   
            Path<T> path = new Path<>(sourceNode, toNode);  
            List<T> trace = getTracedPath(sourceNode, toNode, nodeToPredecessor);
                        
            for (int i = 1; i < trace.size(); i++)
            {                
                path.appendNodeToPath(trace.get(i), 
                                      graph.getEdgeWeight(trace.get(i-1), trace.get(i)));
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
    private static <T> List<T> getTracedPath(T sourceNode, 
                                               T destinationNode, 
                                               Map<T, T> nodeToPredecessor)
    {
        List<T> path = new ArrayList<>();
        T currNode = destinationNode;
        while (currNode != sourceNode)
        {
            path.add(currNode);
            currNode = nodeToPredecessor.get(currNode);   
        }
        path.add(sourceNode);
        Collections.reverse(path);
        return path;
    }
   
}
