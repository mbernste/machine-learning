package bayes_network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bayes_network.cpd.CPDTree;
import bayes_network.cpd.CPDTreeBuilder;

import data.attribute.Attribute;
import data.attribute.AttributeSet;

public class BNNodeSet 
{
    /**
     * The attributes represented by nodes
     */
    private AttributeSet attributes;
    
    /**
     * A mapping of attributes to the nodes representing these attributes
     */
    private Map<Attribute, BNNode> nodes;
    
    /**
     * A sorted list of the nodes in the network.  Sorted topologically.
     */
    private ArrayList<BNNode> sorted;
    
    /**
     * Constructor
     */
    public BNNodeSet()
    {
        attributes = new AttributeSet();
        nodes = new HashMap<Attribute, BNNode>();
        sorted = new ArrayList<BNNode>();
    }
    
    /**
     * Get a node by the attribute it represents
     * 
     * @param attr the attribute the target node represents
     * @return the node
     */
    public BNNode getNode(Attribute attr)
    {   
        return nodes.get(attr);
    }
    
    /**
     * @return the attributes represented by nodes in the network
     */
    public AttributeSet getAttributeSet()
    {
        return this.attributes;
    }
    
    /**
     * @return a list of all attributes that are represented by nodes in the
     * network
     */
    public ArrayList<Attribute> getAttributes()
    {
        return this.attributes.getAttributes();
    }
    
    /**
     * @return the nodes in the network sorted topologically
     */
    public ArrayList<BNNode> topologicallySorted()
    {
        return sorted;
    }
    
    /**
     * Create a directed edge between two nodes
     * 
     * @param parent the parent node
     * @param child the child node
     */
    public void createEdge(BNNode parent, BNNode child)
    {
        parent.addChild(child);
        child.addParent(parent);
                
        topologicalSort();
    }
    
    /**
     * Add a node to the network and sort the all nodes topologically
     * 
     * @param newNode the new node
     */
    public void addNode(BNNode newNode)
    {
        attributes.addAttribute(newNode.getAttribute());
        nodes.put(newNode.getAttribute(), newNode);
        sorted.add(newNode);
        
        topologicalSort();
    }
    
    /**
     * Sort the nodes topologically
     */
    private void topologicalSort()
    {           
        /*
         * Contains all nodes whose parents are "cut" from the graph.  The
         * order that nodes are added to the list is the topological sort.
         */
        ArrayList<BNNode> cut = new ArrayList<BNNode>();
        
        /*
         * Find all nodes that are "cut" from the graph
         */
        ArrayList<BNNode> toRemove = new ArrayList<BNNode>();
           
        /*
         * Cut all Nodes that don't have any parents from the graph 
         */
        for (BNNode node : sorted)
        {
            if (node.getParents().isEmpty())
            {
                cut.add(node);
                toRemove.add(node);
            }
        }
        for (BNNode node : toRemove)
        {
            sorted.remove(node);
        }
        
        /*
         * Keep cutting nodes without parents from the graph until we have
         * processed every node.
         */
        while(!sorted.isEmpty())
        {
            topologicalSortIteration(cut);
        }
           
        /*
         * Reset the sort
         */
        sorted = cut;
        
        System.out.println("TOPOLOGICAL ORDERING");
        for (BNNode node : sorted)
        {
            System.out.println(node.getAttribute().getName());
        }
        System.out.println();
    }
    
    /**
     * This method blabalbala ....TODO
     * 
     * @return all nodes in the network that have no parents
     */
    private ArrayList<BNNode> topologicalSortIteration(ArrayList<BNNode> cut)
    {   
        /*
         * All nodes that need to be removed from the DAG
         */
        ArrayList<BNNode> toRemove = new ArrayList<BNNode>();
        
        /*
         * Cut all parentless nodes from the list of nodes that are still in
         * the graph.  Add these cut nodes to the sorted list.
         */
        for (BNNode node : sorted)
        {            
            boolean cutThisNode = true;
            for (BNNode parent : node.getParents())
            {                
                if (!cut.contains(parent))
                {
                    cutThisNode = false;
                }
            }
            
            if (cutThisNode)
            {
                cut.add(node);
                toRemove.add(node);
            }
        }
        
        /*
         * Remove parentless nodes
         */
        for (BNNode node : toRemove)
        {
            sorted.remove(node);
        }
        
        return cut;
    }

}
