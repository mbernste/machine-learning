package bayes.structuresearch;

import java.util.ArrayList;
import java.util.List;

import data.DataSet;
import pair.Pair;
import bayes.BNNode;
import bayes.BayesianNetwork;
import bayes.structuresearch.score.ScoringFunction;

/**
 * Implements a standard hill climbing search through the Bayes Nets structures
 * in order to optimize a scoring function.  Upon each iteration of the
 * search, this algorithm considers ALL possible operations on the network's 
 * structure:
 * <br>
 * <br>
 * 1. Add edge <br>
 * 2. Remove edge <br>
 * 3. Reverse edge <br>
 * <br>
 * The algorithm stops its search when some stopping criteria is met.
 * 
 */
public class HillClimbingBuilder extends NetworkBuilder
{
    protected int verbose = 5;
    
    public enum StoppingCriteria {SMALL_GAIN};
    
    /**
     * Records the number of iterations 
     */
    protected int numIterations = 0;
    
    /**
     * Records number of operations examined
     */
    protected int numOperationsExamined = 0;
    
    /**
     * The Scoring function to be optimized in the search
     */
    protected ScoringFunction scoringFunction = null;
    
    /**
     * The Bayes net under construction
     */
    public BayesianNetwork net;
    
    /**
     * The training set used to learn the Bayesin network
     */
    public DataSet data;
    
    /**
     * The current score of the network against the data set
     */
    protected Double currNetScore = Double.MAX_VALUE-1;
    
    protected Double prevNetScore = Double.MAX_VALUE;
    
    
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
        while (!stoppingCriteriaMet())
        {
            if (verbose > 2)
            {
                System.out.println("CURRENT NET");
                System.out.println(net);
            }
            
            runIteration();
            
            /*
             * TODO: CLEAN
             */
            //BNResultWriter.WRITER.println(currNetScore);
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
        if (this.currNetScore <= this.prevNetScore)
        {
            return false;
        }
        else
        {
            return true;
        }     
    }
    
    /**
     * A single iteration of the 
     */
    protected void runIteration()
    {
        this.numIterations++;
       
        List<Operation> validOperations = getValidOperations(net.getNodes());
        List<Double> operationScores = new ArrayList<>();
        
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
        
        /*
         * Execute the operation only if this raises the previous net score
         */
        prevNetScore = currNetScore;
        currNetScore = minScore;
        
        if (currNetScore < prevNetScore)
        {
            System.out.println(net);
            executeOperation(minOperation);
            
            System.out.println(net);
            if (verbose > 0)
            {
                System.out.println("Executing operation: " + minOperation + "\n");
            }
        }
    }
    
    /**
     * Calculate the score for an operation on the network
     * 
     * @param operation the operation to be scored
     * @return the score of the operation
     */
    protected Double scoreOperation(Operation operation)
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
    protected void executeOperation(Operation operation)
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
    protected void undoOperation(Operation operation)
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
    public  List<Operation> getValidOperations(List<BNNode> nodes)
    {
        List<Operation> operations
                = new ArrayList<>();
        
        for (BNNode parent : nodes)
        {
            for (BNNode child : nodes)
            {
                operations.addAll( getOperationsOnEdge(parent, child) );
            }
        }
        
        /*
         *  Increment total number of operations examined 
         */
        this.numOperationsExamined += operations.size();
        
        return operations;
    }
    
    protected List<Operation> getOperationsOnEdge(BNNode parent, 
                                                       BNNode child)
    {
        List<Operation> operations = new ArrayList<>();
        
        boolean exists = net.doesEdgeExist(parent, child);
        boolean valid = net.isValidEdge(parent, child);
        boolean reverseValid = net.isValidReverseEdge(parent, child);
          
        /*
         * If the edge does not exist and is valid, create 
         * ADD operation
         */
        if (valid && !exists) 
        {
            Operation o 
                = new Operation(Operation.Type.ADD, parent, child);
            operations.add(o);
        }
        
        /*
         * If the edge exists and the reversed edge is valid, create
         * REVERSE operation
         */
        if (reverseValid && exists)
        {
            Operation o 
                = new Operation(Operation.Type.REVERSE, parent, child);
            operations.add(o);
        }
        
        /*
         * If the edge exists, create REMOVE operation
         */
        if (exists)
        {
            Operation o 
                = new Operation(Operation.Type.REMOVE, parent, child);
            operations.add(o);
        }
        
        return operations;
   }

	@Override
	public BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount) {
		// TODO Auto-generated method stub
		return null;
	}
    

}
