package test;

import java.util.ArrayList;

import pair.Pair;
import bayes_network.BNNode;
import bayes_network.BayesianNetwork;
import bayes_network.builders.HillClimbingBuilder;
import bayes_network.builders.SparseCandidateBuilder;
import bayes_network.builders.scoring.BIC;
import data.DataSet;
import data.arff.ArffReader;

public class SparseCandidate_Test 
{
    public static void main(String[] args)
    {
        //testKlDivergence();
        //testTopKEdges();
        //testSparseCandidate();
        testSparseCandidateRealData();
    }
    
    public static void testSparseCandidateRealData()
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/lymph_train.arff");
        data.setClassAttribute("class");
        
        SparseCandidateBuilder spBuilder = new SparseCandidateBuilder();
        BIC bic = new BIC();
        
        BayesianNetwork net = spBuilder.buildNetwork(data, 1, bic, null);
        
        System.out.println("_______FINAL NET________");
        System.out.println(net);
    }
    
    public static void testSparseCandidate()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();        
        //BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        SparseCandidateBuilder spBuilder = new SparseCandidateBuilder();
        BIC bic = new BIC();
        
        BayesianNetwork net = spBuilder.buildNetwork(data, 1, bic, null);
        
        System.out.println("_______FINAL NET________");
        System.out.println(net);
    }
    
    public static void testTopKEdges()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();        
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        SparseCandidateBuilder spBuilder = new SparseCandidateBuilder();
        BIC bic = new BIC();

        spBuilder.data = data;
        spBuilder.net = net;

        BNNode G = net.getNode(data.getAttributeByName("G"));
        
        ArrayList<Pair<BNNode, BNNode>> edges = spBuilder.getTopKEdges(2, G, net.getNodes());
        
        for (Pair<BNNode, BNNode> edge : edges)
        {
            System.out.println(edge.getFirst().getName() + " -> " + edge.getSecond().getName());
        }
    }
    
    public static void testKlDivergence()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();        
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        SparseCandidateBuilder spBuilder = new SparseCandidateBuilder();
        BIC bic = new BIC();

        spBuilder.data = data;
        spBuilder.net = net;
        
        BNNode B = net.getNode(data.getAttributeByName("B"));
        BNNode F = net.getNode(data.getAttributeByName("F"));
        
        Pair<BNNode, BNNode> edge = new Pair<BNNode, BNNode>(B,F);
        Double kl = spBuilder.calculateKLDivergence(edge);
        
        System.out.println("KL-Divergence: " + kl);
    }

}
