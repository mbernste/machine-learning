package test;

import java.util.ArrayList;

import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;
import bayes_network.BNNode;
import bayes_network.BayesianNetwork;

public class BayesianNetwork_Test 
{
    public static void main(String[] args)
    {   
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/test_network.arff");
        data.setClassAttribute("G");
        
        testAllNodesAbove(data);
    }
    
    public static void testAllNodesAbove(DataSet data)
    {
        BayesianNetwork net = buildTestNetwork(data);
        
        ArrayList<BNNode> nodesAboveB 
                = net.getNodesAbove(net.getNode(data.getAttributeByName("C")));
        
        for (BNNode node : nodesAboveB)
        {
            System.out.println(node.getAttribute().getName() + " ");
        }

    }
    
    public static BayesianNetwork buildTestNetwork(DataSet data)
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
                net.addNode( newNode );
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
      
        net.createEdge(B, G);
        net.createEdge(F, G);
        net.createEdge(C, F);
        net.createEdge(A, B);
        net.createEdge(A, C);
        net.createEdge(E, A);
        net.createEdge(D, A);
        
        return net;
    }
    
}
