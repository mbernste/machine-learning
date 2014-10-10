package bayes.structuresearch;


import bayes.BNNode;
import bayes.BayesianNetwork;
import data.Attribute;
import data.DataSet;

/**
 * Constructs a {@code BayesianNetwork} object
 * 
 */
abstract class NetworkBuilder 
{
    /**
     * The Laplace count used when generating all parameters in the network
     */
    protected Integer laplaceCount;

    public abstract BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount);
    
    /**
     * Builds a new Bayesian network given a dataset.
     * 
     * @param data the data set used to construct the network
     * @param laplaceCount the Laplace count used when generating all
     * parameters in the network
     * @return a constructed Bayesian network
     */
    public BayesianNetwork setupNetwork(DataSet data, Integer laplaceCount)
    {
        this.laplaceCount = laplaceCount;

        BayesianNetwork net = new BayesianNetwork();

        /*
         *  Create a node corresponding to each nominal attribute in the 
         *  dataset. Continuous attributes are ignored.
         */
        for (Attribute attr : data.getAttributeSet().getAttributes())
        {
            if (attr.getType() == Attribute.Type.NOMINAL)
            {
                BNNode newNode = new BNNode(attr);
                net.addNode( newNode, data, this.laplaceCount );
            }
        }

        return net;
    }
}
