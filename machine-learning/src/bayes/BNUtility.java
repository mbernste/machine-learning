package bayes;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for miscellaneous methods needed for Bayesian network
 * learning.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNUtility 
{
    public static Double[][] convertToAdjacencyMatrix(List<BNNode> nodes)
    {
        int numNodes = nodes.size();  
        Double[][] graph = new Double[numNodes][numNodes];
        
        /*
         * Initialize every element in graph to null
         */
        for (int r = 0; r < numNodes; r++)
        {
            for (int c = 0; c < numNodes; c++)
            {
                graph[r][c] = null;
            }
        }
        
        /*
         * Assign graph[P][C] to 1.0 if C is a child of P
         */
        for (int pIndex = 0; pIndex < nodes.size(); pIndex++)
        {
            BNNode currNode = nodes.get(pIndex);
            
            for (BNNode child : currNode.getChildren())
            {
                int cIndex = nodes.indexOf(child);   
                graph[pIndex][cIndex] = 1.0;
            }
        }
        
        return graph;
    }
    
    public static Double[][] convertToAdjacencyMatrix(BayesianNetwork net)
    {
        int numNodes = net.getNumNodes();  
        Double[][] graph = new Double[numNodes][numNodes];
        
        /*
         * Initialize every element in graph to null
         */
        for (int r = 0; r < numNodes; r++)
        {
            for (int c = 0; c < numNodes; c++)
            {
                graph[r][c] = null;
            }
        }
        
        /*
         * Assign graph[P][C] to 1.0 if C is a child of P
         */
        List<BNNode> nodes = net.getNodes();
        for (int pIndex = 0; pIndex < nodes.size(); pIndex++)
        {
            BNNode currNode = nodes.get(pIndex);
            
            for (BNNode child : currNode.getChildren())
            {
                int cIndex = nodes.indexOf(child);   
                graph[pIndex][cIndex] = 1.0;
            }
        }
        
        return graph;
    }
}
