package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A directed graph.
 *
 * @param <T> the type that represents each node
 */
public class DirectedGraph<T> 
{ 	
    /**
     * Maps each object to a node ID
     */
    private Map<T, Integer> nodeToId;
   
    /**
     * Used for assigning node IDs
     */
    private int currId = 0;
    
    /**
     * Adjacency matrix
     */
    List<List<Double>> edges;
    
    public DirectedGraph()
    {
        this.nodeToId = new HashMap<>();
        this.edges = new ArrayList<>();  
    }
    
    /**
     * Add a node to the graph.
     * 
     * @param node the new node
     */
    public void addNode(T node)
    {
        this.nodeToId.put(node, currId++);
        
        // Update the adjacency matrix
        System.out.println("Added node " + node);
        
        this.edges.add(new ArrayList<Double>(Collections.nCopies(edges.size(), Double.POSITIVE_INFINITY)));
        for (List<Double> edgeList : edges)
        {
            edgeList.add(Double.POSITIVE_INFINITY);
        }        
    }
    
    public String toString()
    {
        String str = "Node\tID\n";
        for (T node : getNodes())
        {
            str += node + "\t" + nodeToId.get(node) + "\n";
        }
        
        str += "\nEdges\n";
        str += "\t";
        for (int i = 0; i < edges.size(); i++)
        {
            str += i + "\t";
        }
        str += "\n";
        
        for (int i = 0; i < edges.size(); i++)
        {
            str += i + "\t";
            for (int j = 0; j < edges.get(i).size(); j++)
            {
                if (edges.get(i).get(j) == Double.POSITIVE_INFINITY)
                {
                    str += "-\t";
                }
                else
                {
                    str += edges.get(i).get(j) + "\t";
                }
            }
            str += "\n";
        }
        return str;
    }

    
    /**
     * Add an edge to the graph.  If the nodes are not already present in the
     * graph, they will be added.
     * 
     * @param origin origin node
     * @param destination destination node
     * @param weight weight of edge between origin and destination
     */
    public void addEdge(T origin, T destination, double weight)
    {
        if (!this.nodeToId.containsKey(origin))
        {
            addNode(origin);
        }
        
        if (!this.nodeToId.containsKey(destination))
        {
            addNode(destination);
        }
        
       this.edges.get(nodeToId.get(origin)).set(nodeToId.get(destination), weight);
       System.out.println(this.toString());
    }
    
    /**
     * Get the edge weight between two nodes.  If the edge does not exist, this
     * method returns null.
     * 
     * @param origin origin node
     * @param destination destination node
     * @return the weight of the edge going from the origin to the destination
     */
    public double getEdgeWeight(T origin, T destination)
    {
        return  this.edges.get(nodeToId.get(origin)).get(nodeToId.get(destination));
    }
    
    /**
     * @param origin the origin node
     * @param destination the destination node
     * @return true if an edge exists from the origin to the destination. false otherwise
     */
    public boolean edgeExists(T origin, T destination)
    {
        return edges.get(nodeToId.get(origin)).get(nodeToId.get(destination)) != Double.POSITIVE_INFINITY;
    }
    
    /**
     * @return all the nodes in the graph
     */
    public List<T> getNodes()
    {
        return new ArrayList<>(this.nodeToId.keySet());
    }
}
