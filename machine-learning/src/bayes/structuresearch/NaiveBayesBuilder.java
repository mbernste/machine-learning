package bayes.structuresearch;


import bayes.BNNode;
import bayes.BayesianNetwork;


import data.Attribute;
import data.DataSet;

/**
 * Builds a Bayesian Network with a Naive Bayes Structure
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class NaiveBayesBuilder extends NetworkBuilder
{
    /**
     * Builds Bayesian network with a Naive bayes structure.
     * 
     * @param data the data set used to construct the parameters.  This 
     * DataSet's class attribute must be set to a valid attribute.
     */
    @Override
    public BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount)
    {
        /*
         * Create a new network
         */
        BayesianNetwork net = super.buildNetwork(data, laplaceCount);
        net.setNetInference(BayesianNetwork.StructureSearch.NAIVE_BAYES);

        /*
         *  Get the Node that represents the class attribute
         */
        Attribute classAttr = data.getClassAttribute();
        BNNode classAttrNode = net.getNode(classAttr);

        /*
         *  Create edges from the class Node to all other nodes
         */
        for (BNNode node : net.getNodes())
        {
            if (!node.equals( classAttrNode ))
            {
                net.createEdge(classAttrNode, node, data, laplaceCount);
            }
        }
        
        return net;
    }
    
}
