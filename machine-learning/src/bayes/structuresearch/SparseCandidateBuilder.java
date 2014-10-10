package bayes.structuresearch;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import data.DataSet;
import pair.Pair;
import bayes.BNNode;
import bayes.BNResultWriter;
import bayes.BayesianNetwork;
import bayes.information.KLDivergence;
import bayes.structuresearch.score.ScoringFunction;

public class SparseCandidateBuilder extends HillClimbingBuilder
{   
    public static final int NUM_CANDIDATES = 5;
      
    private Double currMaximizeScore = Double.MAX_VALUE-1;
    
    private Double prevMaximizeScore = Double.MAX_VALUE;
    
    public boolean stoppingMet;
    public boolean maximizeStoppingMet;

    
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
                                        ScoringFunction function,
                                        Pair<StoppingCriteria, Double> stop)
    {
        this.data = data;
        this.scoringFunction = function;
        this.net = super.setupNetwork(data, laplaceCount);
       
        /*
         * Run the hill climbing search
         */
        while (!stoppingMet)
        {
            if (verbose > 2)
            {
                System.out.println("Current network structure:");
                System.out.println(net);
            }
            
            runIteration();
            
            /*
             * TODO: CLEAN
             */
            BNResultWriter.WRITER.println("Iteration " + numIterations + ": " + prevMaximizeScore);
        }
        
        return this.net;   
    }
    
    /**
     * Checks whether the search's stopping criteria has been met.  
     * 
     * @return true if the stopping criteria has been met, false otherwise
     */
    protected boolean stoppingCriteriaMet()
    {
        return stoppingMet;
        /*
        if (this.currNetScore <= this.prevNetScore)
        {
            return false;
        }
        else
        {
            return true;
        }*/     
    }
    
    /**
     * A single iteration of the 
     */
    protected void runIteration()
    {
        /*
         * Increment number of iterations run
         */
        this.numIterations++;
        
        /*
         * Find all valid operations on the current net
         */
        // TODO: CLEAN
        System.out.println("ITERATION " + numIterations);
        System.out.println("GETTING CANDIDATE PARENTS");
        List<Operation> validOperations = getValidOperations(net.getNodes());
          
        boolean firstCall = true;
        maximizeStoppingMet = false;
        while (!maximizeStoppingMet)
        {
            maximizeStep(firstCall, validOperations);
            firstCall = false;
        }
    }
    
    protected boolean maximizeStoppingMet()
    {
        if (this.currMaximizeScore <= this.prevMaximizeScore)
        {
            return false;
        }
        else
        {
            return true;
        }     
    }
    
    protected void maximizeStep(boolean firstCall, 
                                List<Operation> validOperations )
    {
        if (verbose > 3)
        {
            System.out.println("--- MAXIMIZE STEP --- ");
        }
        
        filterOperations(validOperations);
        
        /*
         *  Calculate the score for each operation 
         */
        ArrayList<Double> operationScores = new ArrayList<Double>();
        for (int i = 0; i < validOperations.size(); i++)
        {
            Operation operation = validOperations.get(i);
           
            Double score =  scoreOperation(operation);
            
            if (verbose > 4)
            {
                System.out.println("Score for operation (" + operation + 
                                    ") = " + score);
            }
   
            operationScores.add( score ); 
        }
        
        /*
         * Find the operation that yields the minimum score
         */
        Operation minOperation = null;
        double minScore = Double.MAX_VALUE;
        for (int i = 0; i < operationScores.size(); i++)
        {
            if (operationScores.get(i) < minScore)
            {
                minOperation = validOperations.get(i);
                minScore = operationScores.get(i);
            }
        } 

        prevMaximizeScore = currMaximizeScore;
        currMaximizeScore = minScore;
        
        System.out.println("PREV MAXIMIZE SCORE " + prevMaximizeScore);
        System.out.println("CURR MAXIMIZE SCORE " + currMaximizeScore);
        
        if (currMaximizeScore < prevMaximizeScore)
        {
            BNResultWriter.WRITER.println("Max step: " + currMaximizeScore);
            
            System.out.println(net);
            executeOperation(minOperation);
            validOperations.remove(minOperation);

            /*
             * Add inverse operation
             */
            Operation inverseOperation = inverseOperation(minOperation);
            validOperations.add(inverseOperation);
            
            System.out.println(net);
            if (verbose > 0)
            {
                System.out.println("Executing operation: " + minOperation + "\n");
            }
        }  
        else
        {
            //System.out.println("HERE MATE!");
            if (firstCall) 
            {   
                //System.out.println("BAD SEARCH ON FIRST GO!");
                stoppingMet = true;
            }
            maximizeStoppingMet = true;
        }
    }
    
    
    public Operation inverseOperation(Operation o)
    {
        switch(o.getType())
        {
        case ADD:
            return new Operation(Operation.Type.REMOVE, o.getParent(), o.getChild());
        case REMOVE:
            return new Operation(Operation.Type.ADD, o.getParent(), o.getChild());
        case REVERSE:
            return new Operation( Operation.Type.REVERSE, o.getChild(), o.getParent());
        }
        
        return null;
    }
    
    public void filterOperations(List<Operation> ops)
    {
        List<Operation> toRemove = new ArrayList<>();
        
        for (Operation o : ops)
        {
            if (o.getParent().getName().equals("C") && o.getChild().getName().equals("G"))
            {
                System.out.println(o);
                System.out.println("FOUND C -> G");
            }
            
            switch(o.getType())
            {
            case ADD:                
                if (!net.isValidEdge(o.getParent(), o.getChild()))
                {   
                    System.out.println("REMOVING OPERATION " + o);
                    toRemove.add(o);
                }
                break;
            case REVERSE:
                if (!net.isValidReverseEdge(o.getChild(), o.getParent()))
                {
                    toRemove.add(o);
                }
                break;
            }
        }
        
        for (Operation o : toRemove)
        {
            ops.remove(o);
        }
        
    }
    
    /**
     * Determine all valid operations that can be performed on the network
     * 
     * @return an exhaustive list of all valid operations that can be 
     * performed on the network
     */
    @Override
    public  List<Operation> getValidOperations(List<BNNode> nodes)
    { 
        /*
         * Get all candidate edges
         */
        List<Pair<BNNode, BNNode>> candidateEdges = getCandidateEdges();
         
        List<Operation> operations = new ArrayList<>();
        
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
                                                        List<BNNode> nodes)
    {
        ArrayList<Pair<BNNode, BNNode>> topKEdges 
                                        = new ArrayList<Pair<BNNode, BNNode>>();
        
        /*
         * Maps a valid edge to a klScore
         */
        ArrayList<KlEdgeScorePair> klScores = new ArrayList<KlEdgeScorePair>();
        
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
                                                
                klScores.add( new KlEdgeScorePair(edge, kl) );
            } 
            
        }
        
        /*
         * Find k top edges to add to the list of candidate edges
         */
        ArrayList<KlEdgeScorePair> topEdgeScores = new ArrayList<KlEdgeScorePair>();
        for (KlEdgeScorePair klScore : klScores)
        {
            topEdgeScores.add(klScore);
            Collections.sort(topEdgeScores, KlEdgeScorePair.KL_EDGE_SCORE_ORDER);
            
            for (int i = k; i < topEdgeScores.size(); i++)
            {
                topEdgeScores.remove(i);
            }  
        }
        
        for (KlEdgeScorePair klScore : topEdgeScores)
        {
            topKEdges.add(klScore.getEdge());
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
    public Double calculateKLDivergence(Pair<BNNode, BNNode> edge)
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
        int numInstances = data.getInstanceSet().getInstances().size();
        DataSet bayesData = net.generateDataSet(numInstances);
        
        /*
         * Remove the edge
         */
        net.removeEdge(parent, child, data, 1);
        
        /*
         * Calculate KL-Divergence
         */
        return KLDivergence.divergence(data, 
                                       bayesData, 
                                       parent.getAttribute(), 
                                       child.getAttribute());
    }
    
    /**
     * A directed edge in the network paired with the KL-Divergence of the 
     * two attributes' joint probability between the training distribution and
     * the current net distribution
     */
    private static class KlEdgeScorePair
    {
        private Double score;
        private Pair<BNNode, BNNode> edge;
        
        public static final Comparator<KlEdgeScorePair> KL_EDGE_SCORE_ORDER = 
                new Comparator<KlEdgeScorePair>() 
                {
                    public int compare(KlEdgeScorePair k1, KlEdgeScorePair k2) 
                    {
                        if (k1.getScore() < k2.getScore())
                        {
                            return -1;
                        }
                        else if (k1.getScore() > k2.getScore())
                        {
                            return 1;
                        }
                        else
                        {
                            return 0;
                        }
                    }
                };
            
         public KlEdgeScorePair(Pair<BNNode, BNNode> edge, Double score)
         {
             this.edge = edge;
             this.score = score;
         }
                
         public  Double getScore()
         {
             return this.score;
         }
         
         public Pair<BNNode, BNNode> getEdge()
         {
             return this.edge;
         }
         
         public BNNode getParent()
         {
             return this.edge.getFirst();
         }
         
         public BNNode getChild()
         {
             return this.edge.getSecond();
         }
    }
    
}
