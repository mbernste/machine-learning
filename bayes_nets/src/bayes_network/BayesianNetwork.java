package bayes_network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pair.Pair;

import data.attribute.Attribute;

/**
 * Objects of this class encapsulate a complete Bayesian Network.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BayesianNetwork 
{
    /**
     * Sets verbose output on or off
     */
    public boolean verbose = true;

    /**
     * Network structure inference algorithms
     */
    public static enum Type { TEST, NAIVE_BAYES, TAN, HILL_CLIMBING,
                               SPARSE_CANDIDATE };
  

    /**
     * The algorithm used to build the network
     */
    private Type netInference;

    /**
     * A mapping of attributes to their corresponding node in the network.
     */
    Map<Attribute, BNNode> nodes;

    /**
     * Constructor
     */
    public BayesianNetwork()
    {
        this.nodes = new HashMap<Attribute, BNNode>();
    }

    public void setNetInference(BayesianNetwork.Type netInference)
    {
        this.netInference = netInference;
    }

    /**
     * @return an ArrayList holding all Node objects in the network
     */
    public ArrayList<BNNode> getNodes()
    {
        Collection<BNNode> col = nodes.values();
        return new ArrayList<BNNode>(col);
    }

    /**
     * Add a new Node to the network
     * 
     * @param newNode the new Node
     */
    public void addNode(BNNode newNode)
    {
        this.nodes.put(newNode.getAttribute(), newNode);
    }

    /**
     * Retrieve a Node according to the Attribute this Node represents
     * 
     * @param attr the Attribute represented by the Node of interest
     * @return the Node that represents this Attribute
     */
    public BNNode getNode(Attribute attr)
    {
        return this.nodes.get(attr);
    }

    /**
     * Create a directed edge in the network
     * 
     * @param parent the parent Node of the edge
     * @param child the child Node of the edge
     */
    public void createEdge(BNNode parent, BNNode child)
    {
        parent.addChild(child);
        child.addParent(parent);
    }

    /**
     * Remove a directed edge from the network
     * 
     * @param parent the parent Node of the edge
     * @param child the child Node of the edge
     */
    public void removeConnection(BNNode parent, BNNode child)
    {
        parent.getChildren().remove(child);
        child.getParents().remove(parent);
    }

    @Override
    public String toString()
    {

        String result = "\n\n";

        // For each node, print its parents
        for (BNNode node : nodes.values())
        {	
            result += node.getAttribute().getName();

            for (BNNode parent : node.getParents())
            {
                result += " ";
                result += parent.getAttribute().getName();
            }
            result += "\n";

            if (verbose)
            {
                result += "\n\n";
                result += node.getCPD().toString();
                result += "\n";
            }
        }

        return result;
    }

    /**
     * Query for a conditional probability in the Bayes net.  This method
     * returns the probability for the value of a specific attribute in the 
     * network conditioned on a set of values for other variables in the
     * network.  For example, this method is used for calculated probabilities
     * of the form P(A = a | E = e, D = d).
     *   
     * @param query
     * @return
     */
    public Double queryConditionalProbability(BNConditionalQuery query)
    {
        ArrayList<BNNode> aboveTargetNode = new ArrayList<BNNode>();
        
        /*
         * The conditional probability P(A = a | E = e, D = d) is found by
         * calculated P(B = b, E = e, D = d) / P(E = e, D = d).  
         * 
         * Calculate the numerator of this calculation.
         */
        BNJointQuery allVarJointQuery 
                      = new BNJointQuery( query.getAllVariableSet() );
                                           
        Double numerator = queryJointProbability(allVarJointQuery);
        
        /*
         * Calculate the denominator of the previously described calculation.
         */
        BNJointQuery conditionVarJointQuery 
                       = new BNJointQuery( query.getConditionalVariableSet() );
        Double denominator = queryJointProbability(conditionVarJointQuery);
        
        return numerator / denominator;
    }
    
    //TODO: CALCULATE THE JOINT PROBABILITY
    public Double queryJointProbability(BNJointQuery query)
    {
        /*
         * Contains all nodes for which we need to make a query into their
         * CPD table
         */
        ArrayList<BNNode> allNodes = new ArrayList<BNNode>();
        
        /*
         * Retrieve all nodes we need to consider in the calculation
         */
        for (Pair<Attribute, Integer> variable : query.getVariables())
        {
            BNNode queryNode = this.getNode(variable.getFirst());
            
            /*
             *  Get a list of all nodes that precede each variable node in the 
             *  network's DAG structure
             */
            allNodes.addAll( getNodesAbove(queryNode)  );
            Set<BNNode> uniqueNodes = new HashSet<BNNode>(allNodes);
            allNodes = new ArrayList<BNNode>(uniqueNodes);            
        }
        
        return null;
    }
    
    /**
     * Gets all nodes above a certain node in the network DAG structure
     * including the node itself.
     * <br>
     * <br>
     * Example: if we have a network with the following adjacency-list:
     * <br>
     * <br>
     * A -> B <br>
     * D -> A <br>
     * E -> A <br>
     * <br>
     * <br>
     * This method would return B,A,E,D for the node B.<br>
     * This method would return A,E,D for the node A. <br> 
     * 
     * @param node the query node
     * @return all nodes above the query node in the DAG structure of the net
     */
    public ArrayList<BNNode> getNodesAbove(BNNode node)
    {
        ArrayList<BNNode> aboveNode = new ArrayList<BNNode>();
        aboveNode.add(node);
        
        if (node.getParents().size() == 0)  // Stopping Criteria
        {
            return aboveNode;
        }
        else                        
        {
            for (BNNode parent : node.getParents())
            {
                /*
                 *  Recursive call to each parent of the query node 
                 */
                aboveNode.addAll( getNodesAbove(parent) );
                
                Set<BNNode> uniqueNodes = new HashSet<BNNode>(aboveNode);
                aboveNode = new ArrayList<BNNode>(uniqueNodes);
            }
            return aboveNode;
        }
    }

    /**
     * @return the name of the algorithm that was used to infer this Bayes 
     * network's structure
     */
    private String getNetInferenceName()
    {
        String result = "";

        switch(netInference)
        {
        case NAIVE_BAYES:
            result = "Naive Bayes";
        break;
        case TAN:
            result = "TAN";
        break;
        }

        return result;
    }

    /**
     * @param netInference the algorithm used to create the network's
     * structure
     */
    protected void setInference(BayesianNetwork.Type netInference)
    {
        this.netInference = netInference;
    }

}
