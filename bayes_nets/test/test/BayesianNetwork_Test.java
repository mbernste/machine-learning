package test;

import java.util.ArrayList;
import java.util.Set;

import pair.Pair;

import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;
import bayes_network.BNConditionalQuery;
import bayes_network.BNJointQuery;
import bayes_network.BNNode;
import bayes_network.BayesianNetwork;
import bayes_network.cpd.CPDTree;
import bayes_network.cpd.CPDTreeBuilder;

public class BayesianNetwork_Test 
{
    public static void main(String[] args)
    {  
        //testAllNodesAbove();
        //testJointProbabilityQuery2();
        //testConditionalProbabilityQuery();
        //testGenerateDataset();
        testIsValidEdge();
        //testEdgeExists();
    }
    
    public static void testEdgeExists()
    {
    
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();    
        
        BNNode E = net.getNode(data.getAttributeByName("E"));
        BNNode F = net.getNode(data.getAttributeByName("F"));
        BNNode G = net.getNode(data.getAttributeByName("G"));
        
        System.out.println( net.edgeExists(F, G) );   
    }
    
    public static void testIsValidEdge()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();   
        
        BNNode C = net.getNode(data.getAttributeByName("C"));
        BNNode G = net.getNode(data.getAttributeByName("G"));
        
        System.out.println( net.isValidEdge(G, C) );   
    }
    
    
    
    public static void testGenerateDataset()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();   
        
        System.out.println(net);
        
        DataSet generated = net.generateDataSet(30);
        
        System.out.println(generated.getNumInstances() + " TOTAL INSTANCES");
    }
    
    public static void testConditionalProbabilityQuery()
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/test_network2.arff");
        data.setClassAttribute("D");
        
        BayesianNetwork net = buildTestNetwork2(data);
        System.out.println(net);
        
        
        Attribute A = data.getAttributeByName("A");
        Integer valA = A.getNominalValueId("a1");
        
        Attribute D = data.getAttributeByName("C");
        Integer valD = D.getNominalValueId("c1");
        
        BNConditionalQuery query = new BNConditionalQuery();
        query.setTargetVariable(A, valA);
        query.addConditionVariable(D, valD);
        
        Double p = net.queryConditionalProbability(query);
        System.out.println("Conditional Probability: " + p);
    }
    
    public static void  testJointProbabilityQuery1()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();   
        
        Attribute B = data.getAttributeByName("B");
        Integer valB = B.getNominalValueId("b1");
        
        Attribute F = data.getAttributeByName("F");
        Integer valF = F.getNominalValueId("f1");
        
        BNJointQuery query = new BNJointQuery();
        query.addVariable(B, valB);
        query.addVariable(F, valF);
        
        net.queryJointProbability(query);
    }
    
    public static void  testJointProbabilityQuery2()
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/test_network2.arff");
        data.setClassAttribute("D");
        
        BayesianNetwork net = buildTestNetwork2(data);
        System.out.println(net);
        
        
        Attribute A = data.getAttributeByName("A");
        Integer valA = A.getNominalValueId("a1");
        
        Attribute D = data.getAttributeByName("D");
        Integer valD = D.getNominalValueId("d1");
        
        BNJointQuery query = new BNJointQuery();
        query.addVariable(A, valA);
        query.addVariable(D, valD);
        
        net.queryJointProbability(query);
    }
    
    public static void testAllNodesAbove()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        Set<BNNode> nodesAboveB 
                = net.getNodesAbove(net.getNode(data.getAttributeByName("G")));
        
        for (BNNode node : nodesAboveB)
        {
            System.out.println(node.getAttribute().getName() + " ");
        }

    }
    
    
    
    public static BayesianNetwork buildTestNetwork2(DataSet data)
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
      
        net.createEdge(B, A, data, 1);
        net.createEdge(C, A, data, 1);
        net.createEdge(C, D, data, 1);
                
        return net;
    }
    
  
    
}
