package graph.prim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


import pair.Pair;

/**
 * Implements Prim's algorithm for finding the maximal/minimum spanning tree
 * in a directed graph.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Prim 
{   
    /**
     * Denotes a non-existant edge in the adjacency matrix
     */
    public final static Double NO_EDGE = null;

    /**
     * Denotes the ID of the first vertex to use when constructing the tree
     */
    public final static int FIRST_VERTEX = 0;

    /**
     * Run Prim's algorithm on a graph to find the maximal/minimal spanning 
     * tree.
     * 
     * @param graph an adjacency matrix representing the existing graph. Weights
     * are specified by Double objects.  Null objects represent non-existant
     * edges.
     * @return a list of edges representing the resulting spanning tree.
     */
    public static List<Edge> runPrims(Double[][] graph)
    {
        /*
         * Stores all vertices currently in the tree
         */
        Set<Integer> vInTree = new HashSet<Integer>();

        /*
         * Add first vertex to the tree
         */
        vInTree.add(FIRST_VERTEX);

        /*
         * Create a priority queue for storing potential edges
         */
        PriorityQueue<Edge> potentialEdges = 
                new PriorityQueue<Edge>(graph.length * graph.length, Edge.EDGE_ORDER);

        /*
         *  Add all edges of the first node to the queue of potential edges
         */
        addPotentialEdges(graph, FIRST_VERTEX, vInTree, potentialEdges);

        /*
         *  Initialize the set of Edges that constitute the spanning tree
         */
        Set<Edge> edgesInTree = new  HashSet<Edge>();

        /*
         * While there are vertices that have not yet been added to the tree
         * keep adding edges to the spanning tree.
         */
        while (vInTree.size() < graph.length)
        {		
            Edge potentialEdge;
            Integer newVertex = -1;

            boolean isInTree = true; // true if the current edge is already in
                                     // in the tree

            while (isInTree)
            {				
                potentialEdge = potentialEdges.remove();

                /*
                 * Add the new edge to the tree only if both vertices of this
                 * edge are not already in the tree.  We then add the new vertex
                 * to the tree.
                 */
                if ( ! (vInTree.contains( potentialEdge.getFirstVertex() ) &&
                        vInTree.contains( potentialEdge.getSecondVertex() ))  )
                {					
                    edgesInTree.add(potentialEdge);

                    isInTree = false;

                    if (!vInTree.contains(potentialEdge.getFirstVertex()))
                    {
                        newVertex = potentialEdge.getFirstVertex();
                    }
                    else
                    {
                        newVertex = potentialEdge.getSecondVertex();
                    }
                }	
            }

            vInTree.add(newVertex);
            addPotentialEdges(graph, newVertex, vInTree, potentialEdges);
        }

        return new ArrayList<Edge>(edgesInTree);

    }

    /**
     * Adds all edges incident to a new vertex in the spanning tree to the queue
     * containing all potential edges that may be added to the spanning tree
     * in the future.
     * 
     * @param graph - the original graph
     * @param newVertex - the new vertex to the spanning tree
     * @param vInTree - a set of all vertices already in the spanning tree
     * @param potentialEdges - the set of potential edges to the spanning tree
     */
    public static void addPotentialEdges(Double[][] graph, 
                                         Integer newVertex,
                                         Set<Integer> vInTree,
                                         PriorityQueue<Edge> potentialEdges)
    {

        /*
         * Iterate through all adjacent vertices to the new vertice, create
         * each edge, and add each edge to the set of potential edges. 
         */
        for (int currVertex = 0; currVertex < graph.length; currVertex++)
        {
            if (graph[newVertex][currVertex] != NO_EDGE)
            {
                /*
                 *  Create edge
                 */
                Pair<Integer, Integer> nodes = new Pair<Integer, Integer>();
                nodes.setFirst(newVertex);
                nodes.setSecond(currVertex);

                /*
                 *  Set weight
                 */
                Double weight = graph[newVertex][currVertex];

                /*
                 *  Add to potential edges
                 */
                Edge e = new Edge(nodes, weight);
                potentialEdges.add(e);
            }
        }
    }

}
