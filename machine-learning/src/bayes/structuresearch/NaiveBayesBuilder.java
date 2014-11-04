package bayes.structuresearch;


import bayes.BNNode;
import bayes.BayesianNetwork;


import data.Attribute;
import data.DataSet;

/**
 * Builds a Bayesian Network with a Naive Bayes Structure
 * 
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
        BayesianNetwork net = super.setupNetwork(data, laplaceCount);
        net.setNetStructureAlgorithm(BayesianNetwork.StructureAlgorithm.NAIVE_BAYES);

        /*
         *  Create edges from the class Node to all other nodes
         */
        BNNode classAttrNode = net.getNode(data.getClassAttribute());
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
