package graph.dag;

import java.util.ArrayList;

public class DetectCycles 
{
    /**
     * Detects if there is a cycle in the graph
     * 
     * @param graph the adjacency matrix representing the graph.  Non-edges are
     * represented by the null reference
     * @return true if a cycle has been detected, false otherwise
     */
    public static Boolean run(Double[][] graph)
    {
        /*
         *  All unsorted vertices
         */
        ArrayList<Integer> toProcess = getAllVertices(graph);
                
        /*
         *  Run topological sort
         */
        while(!toProcess.isEmpty())
        {            
            int beforeSize = toProcess.size();
            runIteration(graph, toProcess);
            
            if (toProcess.size() == beforeSize)
            {
                return true;
            }
        }
        
       return false; 
    }
     
    /**
     * Run a single iteration of cycle detection algorithm
     * 
     * @param graph the graph
     * @param toProcess a list of vertices that have yet to be processed
     */
    private static void runIteration(Double[][] graph,
                                     ArrayList<Integer> toProcess)
    {
        
        for (int i = 0; i < toProcess.size(); i++)
        {
            int vertex = toProcess.get(i);
            
            /*
             * Find parent
             */
            boolean foundParent = false;
            for (int r = 0; r < graph.length; r++)
            {                
                if (graph[r][vertex] != null)
                {
                    foundParent = true;
                    break;
                }
            }
            
            /*
             * If no parents are found, cut this vertex from the graph
             */
            if (!foundParent)
            {
                cutVertex(graph, vertex);
                for (int j = 0; j < toProcess.size(); j++)
                {
                    if (toProcess.get(j) == vertex)
                    {
                        toProcess.remove(j);
                    }
                }
            }
        }
    }
    
    /**
     * Cut a vertex from the graph.  That is, cut all outgoing edges from this
     * vertex.
     * 
     * @param graph the graph
     * @param vertex the vertex to cut from the graph
     */
    private static void cutVertex(Double[][] graph, Integer vertex)
    {
        for (int c = 0; c < graph.length; c++)
        {
            graph[vertex][c] = null;
        }
    }
    
    /**
     * Get all vertices in a graph
     * 
     * @param graph the graph
     * @return all vertices in the graph
     */
    private static ArrayList<Integer> getAllVertices(Double[][] graph)
    {
        ArrayList<Integer> allNodes = new ArrayList<Integer>();
        
        for (int r = 0; r < graph.length; r++)
        {
            allNodes.add(r);
        }
        
        return allNodes;
    }

}
