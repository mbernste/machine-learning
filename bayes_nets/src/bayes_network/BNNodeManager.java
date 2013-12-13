package bayes_network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bayes_network.cpd.CPDTree;
import bayes_network.cpd.CPDTreeBuilder;

import data.DataSet;
import data.attribute.Attribute;
import data.attribute.AttributeSet;

/**
 * Manages the nodes and all node and edge operations in a Bayesian network
 * such as adding/removing nodes and edges and updating the parameters in the
 * model.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNNodeManager 
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
    public BNNodeManager()
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
    public void createEdge(BNNode parent, 
                           BNNode child, 
                           DataSet data, 
                           Integer laplaceCount)
    {
        parent.addChild(child);
        child.addParent(parent);
                
        /*
         *  Rebuild the child's CPD
         */
        buildCPD( child, data, laplaceCount );
        
        /*
         *  Resort the nodes topologically
         */
        topologicalSort();
    }
    
    /**
     * Remove a directed edge between two nodes
     * 
     * @param parent the parent node
     * @param child the child node
     */
    public void removeEdge(BNNode parent, 
                            BNNode child, 
                            DataSet data, 
                            Integer laplaceCount)
    {
        parent.removeChild(child);
        child.removeParent(parent);
        
        /*
         *  Rebuild the child's CPD
         */
        buildCPD( child, data, laplaceCount );
        
        /*
         *  Resort the nodes topologically
         */
        topologicalSort();
    }
    
    /**
     * Reverse a directed edge between two nodes
     * 
     * @param parent the parent node
     * @param child the child node
     */
    public void reverseEdge(BNNode parent, 
                           BNNode child, 
                           DataSet data, 
                           Integer laplaceCount)
    {
        parent.removeChild(child);
        child.removeParent(parent);
        
        child.addChild(parent);
        parent.addParent(child);
                
        /*
         *  Rebuild the child's CPD
         */
        buildCPD( child, data, laplaceCount );
        buildCPD( parent, data, laplaceCount );
        
        /*
         *  Resort the nodes topologically
         */
        topologicalSort();
    }
    
   
    
    
    
    /**
     * Add a node to the network and sort the all nodes topologically
     * 
     * @param newNode the new node
     */
    public void addNode(BNNode newNode, DataSet data, Integer laplaceCount)
    {
        attributes.addAttribute(newNode.getAttribute());
        nodes.put(newNode.getAttribute(), newNode);
        sorted.add(newNode);
        
        /*
         * Resort the nodes topologically
         */
        topologicalSort();
        
        buildCPD( newNode, data, laplaceCount);
    }
    
    /**
     * Build the CPD Tree for a single node
     * 
     * @param node the node for which we need to build the CPD tree
     * @param data the data used to build the CPD
     */
    private void buildCPD(BNNode node, DataSet data, Integer laplaceCount)
    { 
        System.out.println("BUILDING CPD");
        
        ArrayList<Attribute> cpdAttributes = new ArrayList<Attribute>();

        /*
         *  Get parent node's associated attribute
         */
        for (BNNode parent : node.getParents())
        {
            cpdAttributes.add(parent.getAttribute());
        }

        /*
         *  Add the current node's attribute
         */
        cpdAttributes.add(node.getAttribute());

        /*
         *  Build the CPD at this node
         */
        CPDTreeBuilder treeBuilder = new CPDTreeBuilder();
        CPDTree cpdTree = treeBuilder.buildCPDTree(data, 
                                                   cpdAttributes,
                                                   laplaceCount);
        
        /*
         *  Set the CPD Tree
         */
        node.setCPDTree( cpdTree );   
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
        @SuppressWarnings("unchecked")
        ArrayList<BNNode> allNodesCopy = (ArrayList<BNNode>) sorted.clone();
        for (BNNode node : allNodesCopy)
        {
            if (node.getParents().isEmpty())
            {
                cut.add(node);
                toRemove.add(node);
            }
        }
        for (BNNode node : toRemove)
        {
            allNodesCopy.remove(node);
        }
        
        /*
         * Keep cutting nodes without parents from the graph until we have
         * processed every node.
         */
        while(!allNodesCopy.isEmpty())
        {
            topologicalSortIteration(cut, allNodesCopy);
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
    private ArrayList<BNNode> topologicalSortIteration(ArrayList<BNNode> cut,
                                                       ArrayList<BNNode> allNodesCopy)
    {   
        /*
         * All nodes that need to be removed from the DAG
         */
        ArrayList<BNNode> toRemove = new ArrayList<BNNode>();
        
        /*
         * Cut all parentless nodes from the list of nodes that are still in
         * the graph.  Add these cut nodes to the sorted list.
         */
        for (BNNode node : allNodesCopy)
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
            allNodesCopy.remove(node);
        }
        
        return cut;
    }
}
