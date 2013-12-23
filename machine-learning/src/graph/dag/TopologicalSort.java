package graph.dag;

import java.util.ArrayList;

/**
 * Topologically sorts the vertices in a Directed Acyclic Graph (DAG).
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class TopologicalSort 
{
    /**
     * Run the topological sort
     * 
     * @param graph the adjacency matrix representing the DAG.  Non-edges are
     * represented by the null reference
     * @return a sorted list of vertices
     */
    public static ArrayList<Integer> run(Double[][] graph)
    {
        /*
         *  All unsorted vertices
         */
        ArrayList<Integer> toProcess = getAllVertices(graph);
        
        /*
         *  Sorted vertices
         */
        ArrayList<Integer> sorted = new ArrayList<Integer>();
        
        /*
         *  Run topological sort
         */
        while(!toProcess.isEmpty())
        {
            sorted.addAll( runIteration(graph, toProcess) );
        }
        
       return sorted; 
    }
     
    /**
     * Run a single iteration of the topological sort
     * 
     * @param graph the graph
     * @param toProcess a list of vertices that have yet to be added to the list
     * of sorted vertices
     * @return a list of vertices from the list of unsorted vertices that must
     * be appended to the list of sorted vertices
     */
    private static ArrayList<Integer> runIteration(Double[][] graph,
                                                   ArrayList<Integer> toProcess)
    {
        ArrayList<Integer> newSorted = new ArrayList<Integer>();
        
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
             * If no parents are found, add the vertex to the list of vertices
             * to be returned and cut this vertex from the graph
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
                newSorted.add(vertex);
            }
        }
        
        return newSorted;
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
