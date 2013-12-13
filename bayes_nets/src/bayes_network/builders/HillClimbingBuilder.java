package bayes_network.builders;

import java.util.ArrayList;

import pair.Pair;
import bayes_network.BayesianNetwork;
import bayes_network.builders.scoring.ScoringFunction;
import data.DataSet;

/**
 * Implements a standard hill climbing search through the Bayes Nets structures
 * in order to optimize a scoring function.  Upon each iteration of the
 * search, this algorithm considers ALL possible operations on the network's 
 * structure:<br>
 * 1. Add edge <br>
 * 2. Remove edge <br>
 * 3. Reverse edge <br>
 * <br>
 * The algorithm stops its search when some stopping criteria is met.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class HillClimbingBuilder extends NetworkBuilder
{
    public enum StoppingCriteria {SMALL_GAIN};
        
    /**
     * Records the number of iterations 
     */
    private int numIterations = 0;
    
    /**
     * Records number of operations examined
     */
    private int numOperationsExamined = 0;
    
    private ScoringFunction scoringFunction = null;
    
    private BayesianNetwork net;
    private DataSet data;
    
    public BayesianNetwork buildNetwork(DataSet data, 
                                        Integer laplaceCount,
                                        ScoringFunction function,
                                        Pair<StoppingCriteria, Double> stop)
    {
        this.data = data;
        this.net = super.buildNetwork(data, laplaceCount);
        
        /*
         * Run the hill climbing search
         */
        while (!stoppingCriteriaMet())
        {
            runIteration();
        }
        
        return this.net;   
    }
    
    /**
     * Checks whether the search's stopping criteria has been met.  
     * 
     * @return true if the stopping criteria has been met, false otherwise
     */
    private boolean stoppingCriteriaMet()
    {
        return false;
    }
    
    /**
     * A single iteration of the 
     */
    private void runIteration()
    {
        /*
         * Increment number of iterations run
         */
        this.numIterations++;
        
        /*
         * Find all valid operations on the current net
         */
        ArrayList<Operation> validOperations = getAllValidOperations();
        
        /*
         * Stores the score for each operation
         */
        ArrayList<Double> operationScores = new ArrayList<Double>();
            
        /*
         *  Calculate the score for each operation 
         */
        for (int i = 0; i < validOperations.size(); i++)
        {
            Operation operation = validOperations.get(i);    
            operationScores.add( scoreOperation(operation) ); 
        }
        
        /*
         * Find the operation that yields the maximum score
         */
        Operation maxOperation = null;
        double maxScore = 0;
        for (int i = 0; i < operationScores.size(); i++)
        {
            if (operationScores.get(i) > maxScore)
            {
                maxOperation = validOperations.get(i);
                maxScore = operationScores.get(i);
            }
        }  
        
        /*
         * Execute operation
         */
        executeOperation(maxOperation);   
    }
    
    private Double scoreOperation(Operation operation)
    {
        Double score = null;
        
        // Execute the operation
        executeOperation(operation);
        
        // Score the operation
        score = scoringFunction.scoreNet(net, data);
        
        // Undo the operation
        undoOperation(operation);
        
        return score;
    }
    
    /**
     * Execute an operation on the network
     * 
     * @param operation the opeartion to be executed on the network
     */
    private void executeOperation(Operation operation)
    {
        switch(operation.getType())
        {
        case ADD:
            net.createEdge(operation.getParent(),
                           operation.getChild(), 
                           data, 
                           this.laplaceCount);
            break;
        case REMOVE:
            net.removeEdge(operation.getParent(), 
                           operation.getChild(),
                           data,
                           this.laplaceCount);
            break;
        case REVERSE:
            net.reverseEdge(operation.getParent(),
                            operation.getChild(),
                            data,
                            this.laplaceCount);
            break;  
        } 
    }
    
    /**
     * Undoes the specified operation. If the operation is to add an edge,
     * this operation removes the edge.  If the operation is to remove an
     * edge, this method will add it.  If the operation is to reverse an 
     * edge between two nodes, this method will reverse it the other way.
     * 
     * @param operation the operation to undo
     */
    private void undoOperation(Operation operation)
    {
        switch(operation.getType())
        {
        case ADD:
            net.removeEdge(operation.getParent(),
                           operation.getChild(), 
                           data, 
                           this.laplaceCount);
            break;
        case REMOVE:
            net.createEdge(operation.getParent(), 
                           operation.getChild(),
                           data,
                           this.laplaceCount);
            break;
        case REVERSE:
            net.reverseEdge(operation.getChild(),
                            operation.getParent(),
                            data,
                            this.laplaceCount);
            break;  
        } 
    }
    
    /**
     * Determine all valid operations that can be performed on the network
     * 
     * @return an exhaustive list of all valid operations that can be 
     * performed on the network
     */
    private  ArrayList<Operation> getAllValidOperations()
    {
        ArrayList<Operation> operations
                = new ArrayList<Operation>();
        
        // TODO
        // TODO: FIND ALL VALID OPERATIONS
        // TODO:
        
        /*
         *  Increment total number of operations examined 
         */
        this.numOperationsExamined += operations.size();
        
        return operations;
    }
    

}
