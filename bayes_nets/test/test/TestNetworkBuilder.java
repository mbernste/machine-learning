package test;

import pair.Pair;
import bayes_network.BNNode;
import bayes_network.BayesianNetwork;
import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;

public class TestNetworkBuilder 
{
    /**
     * Creates the following bayesian network:
     * <br>
     * <br>
     * E -> A<br>
     * D -> A<br>
     * A -> B<br>
     * A -> C<br>
     * B -> G<br>
     * C -> F<br>
     * F -> G<br>
     *  
     * @return the network and the dataset that was used to learn the net
     */
    public static Pair<BayesianNetwork, DataSet> buildTestNetwork1()
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/test_network.arff");
        data.setClassAttribute("D");
        
        /*
         * Create network
         */
        BayesianNetwork net = new BayesianNetwork();
        net.setNetInference(BayesianNetwork.Type.TEST);
        
        /*
         *  Create a node corresponding to each nominal attribute in the 
         *  dataset. Continuous attributes are ignored.
         */
        for (Attribute attr : data.getAttributeList())
        {
            if (attr.getType() == Attribute.NOMINAL)
            {
                BNNode newNode = new BNNode(attr);
                net.addNode( newNode, data, 1 );
            }
        }
        
        /*
         * Set edges manually
         */
        BNNode A = net.getNode(data.getAttributeByName("A"));
        BNNode B = net.getNode(data.getAttributeByName("B"));
        BNNode C = net.getNode(data.getAttributeByName("C"));
        BNNode D = net.getNode(data.getAttributeByName("D"));
        BNNode E = net.getNode(data.getAttributeByName("E"));
        BNNode F = net.getNode(data.getAttributeByName("F"));
        BNNode G = net.getNode(data.getAttributeByName("G"));
      
        net.createEdge(B, G, data, 1);
        net.createEdge(F, G, data, 1);
        net.createEdge(C, F, data, 1);
        net.createEdge(A, B, data, 1);
        net.createEdge(A, C, data, 1);
        net.createEdge(E, A, data, 1);
        net.createEdge(D, A, data, 1);
                
        return new Pair<BayesianNetwork, DataSet>(net, data);
    }

}
