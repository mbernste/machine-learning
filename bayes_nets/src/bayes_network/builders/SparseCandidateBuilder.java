package bayes_network.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pair.Pair;
import bayes_network.BNNode;
import bayes_network.BayesianNetwork;
import bayes_network.builders.scoring.ScoringFunction;
import bayes_network.kullback_leibler.KLDivergence;
import data.DataSet;
import data.attribute.Attribute;

public class SparseCandidateBuilder extends HillClimbingBuilder
{   
    public static final int NUM_CANDIDATES = 5;
    
    /**
     * TODO: // FINISH DESCRIPTION
     * @param data
     * @param laplaceCount
     * @param function
     * @param stop
     * @return
     */
    public BayesianNetwork buildNetwork(DataSet data, 
                                        Integer laplaceCount,
                                        ScoringFunction function)
    {
        this.data = data;
        this.scoringFunction = function;
        this.net = super.buildNetwork(data, laplaceCount);
       
        /*
         * Run the hill climbing search
         */
        while (!stoppingCriteriaMet())
        {
            // TODO: REMOVE
            System.out.println("CURRENT NET");
            System.out.println(net);
            runIteration();
        }
        
        return this.net;   
    }
    
    /**
     * Determine all valid operations that can be performed on the network
     * 
     * @return an exhaustive list of all valid operations that can be 
     * performed on the network
     */
    @Override
    public  ArrayList<Operation> getValidOperations(ArrayList<BNNode> nodes)
    { 
        /*
         * Get all candidate edges
         */
        ArrayList<Pair<BNNode, BNNode>> candidateEdges = getCandidateEdges();
 
        ArrayList<Operation> operations = new ArrayList<Operation>();
        
        /*
         * For each candidate edge, create all operations that can be executed
         * on that edge
         */
        for (Pair<BNNode, BNNode> edge : candidateEdges) 
        {
            BNNode parent = edge.getFirst();
            BNNode child = edge.getSecond();
        
            operations.addAll(super.getOperationsOnEdge(parent, child));
        }
        
        /*
         *  Increment total number of operations examined 
         */
        this.numOperationsExamined += operations.size();
        
        return operations;
    }

    /**
     * Get all candidate edges in the network using the sparse candidate 
     * algorithm.
     * 
     * TODO: FINISH DESCRIPTION
     * 
     * @return all candidate edges
     */
    public ArrayList<Pair<BNNode, BNNode>> getCandidateEdges()
    {
        ArrayList<Pair<BNNode, BNNode>> candidateEdges 
                                       = new ArrayList<Pair<BNNode, BNNode>>();
        
        for (BNNode child : net.getNodes())
        {
            /*
             * Number of possible candidates
             */
            int k = NUM_CANDIDATES - child.getParents().size();
            
            /*
             * Add edges from the current node's current parents to this node
             * to the list of candidate edges.
             */
            for (BNNode parent : child.getParents())
            {
                Pair<BNNode, BNNode> edge = new Pair<BNNode, BNNode>(parent, child);
                candidateEdges.add(edge);
            }
            
            candidateEdges.addAll( getTopKEdges(k, child, net.getNodes()) );   
        }
        
        return candidateEdges;
    }
    
    /**
     * For a given child node, this method will find the top k edges between 
     * other nodes to this child scored by the KL-Divergence of the joint 
     * probability of the two nodes between the training data and data generated
     * by the Bayes net
     * 
     * @param k number of edges to return
     * @param child the child edge for which we need to find k parents
     * @param nodes all nodes to be considered
     * @return k edges to this child
     */
    public ArrayList<Pair<BNNode, BNNode>> getTopKEdges(int k, 
                                                        BNNode child, 
                                                        ArrayList<BNNode> nodes)
    {
        ArrayList<Pair<BNNode, BNNode>> topKEdges 
                                        = new ArrayList<Pair<BNNode, BNNode>>();
        
        /*
         * Maps a valid edge to a klScore
         */
        Map<Pair<BNNode, BNNode>, Double> klScores =
                    new HashMap<Pair<BNNode, BNNode>, Double>();
        
        /*
         * Calculate KL-Divergence for every possible edge
         */
        for (BNNode parent : nodes)
        {
            boolean valid = net.isValidEdge(parent, child);
            
            if (valid)
            {
                Pair<BNNode, BNNode> edge = new Pair<BNNode, BNNode>(parent, 
                                                                     child);
                Double kl = calculateKLDivergence(edge);
                
                klScores.put(edge, kl);
            } 
        }
        
        /*
         * Find k top edges to add to the list of candidate edges
         */
        for (Entry<Pair<BNNode, BNNode>, Double> klScore : klScores.entrySet())
        {
            
        }
        
        return topKEdges;
    }
    
    /**
     * Calculate the KL-Divergence for the joint probability of two attributes
     * between the training data set and the current bayes net.  This method 
     * calculates:
     * <br>
     * <br>
     * KL( P_data(A,B) || P_net(A,B) )
     * 
     * @param edge the edge A -> B
     * @return the KL-divergence for these two attributes
     */
    private Double calculateKLDivergence(Pair<BNNode, BNNode> edge)
    {
        BNNode parent = edge.getFirst();
        BNNode child = edge.getSecond();
        
        /*
         * Create the edge
         */
        net.createEdge(parent, child, data, 1);
        
        /*
         * Generate the dataset
         */
        DataSet bayesData = net.generateDataSet(data.getNumInstances());
        
        /*
         * Remove the edge
         */
        net.removeEdge(parent, child, data, 1);
        
        /*
         * Calculate KL-Divergence
         */
        ArrayList<Attribute> attrs = new ArrayList<Attribute>();
        attrs.add(parent.getAttribute());
        attrs.add(child.getAttribute());
        
        return KLDivergence.divergence(data, bayesData, attrs);
    }
    
}
