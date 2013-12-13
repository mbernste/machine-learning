package bayes_network.builders;

import bayes_network.BayesianNetwork;
import bayes_network.BNNode;
import data.DataSet;
import data.attribute.Attribute;

/**
 * Constructs a {@code BayesianNetwork} object
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.eud
 *
 */
abstract class NetworkBuilder 
{
    /**
     * The Laplace count used when generating all parameters in the network
     */
    protected Integer laplaceCount;

    /**
     * Builds a new Bayesian network given a dataset.
     * 
     * @param data the data set used to construct the network
     * @param laplaceCount the Laplace count used when generating all
     * parameters in the network
     * @return a constructed Bayesian network
     */
    public BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount)
    {
        /*
         *  Set laplace count
         */
        this.laplaceCount = laplaceCount;

        BayesianNetwork net = new BayesianNetwork();

        /*
         *  Create a node corresponding to each nominal attribute in the 
         *  dataset. Continuous attributes are ignored.
         */
        for (Attribute attr : data.getAttributeList())
        {
            if (attr.getType() == Attribute.NOMINAL)
            {
                BNNode newNode = new BNNode(attr);
                net.addNode( newNode, data, this.laplaceCount );
            }
        }

        return net;
    }
   
}
