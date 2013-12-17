package test;

import java.util.Arrays;

import bayes_network.BNNode;
import bayes_network.BNUtility;
import bayes_network.BayesianNetwork;
import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;

public class BNUtility_Test 
{
    public static void main(String[] args)
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/test_network.arff");
        data.setClassAttribute("D");
        
        BayesianNetwork net = buildTestNetwork1(data);
        System.out.println(net);
        
        Double[][] graph = BNUtility.convertToAdjacencyMatrix(net);
        for (Double[] row : graph)
        {
            System.out.println(Arrays.toString(row));
        }
    }
    
    public static BayesianNetwork buildTestNetwork1(DataSet data)
    {
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
                
        return net;
    }

}
