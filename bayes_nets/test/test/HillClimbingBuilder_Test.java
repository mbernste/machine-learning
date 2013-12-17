package test;

import java.util.ArrayList;

import pair.Pair;
import bayes_network.BayesianNetwork;
import bayes_network.builders.HillClimbingBuilder;
import bayes_network.builders.Operation;
import bayes_network.builders.HillClimbingBuilder.StoppingCriteria;
import bayes_network.builders.scoring.BIC;
import bayes_network.builders.scoring.ScoringFunction;
import data.DataSet;
import data.arff.ArffReader;

public class HillClimbingBuilder_Test 
{
    public static void main(String[] args)
    {
        //testGetAllValidOperations();
        //testHillClimbing();
        testHillClimbingRealData();
    }
    
    public static void testHillClimbingRealData()
    {
        /*
         *  Read the training data from the arff file
         */
        ArffReader reader = new ArffReader();
        DataSet data = reader.readFile("./data/lymph_train.arff");
        data.setClassAttribute("class");
        
        HillClimbingBuilder hcBuilder = new HillClimbingBuilder();
        BIC bic = new BIC();
        
        BayesianNetwork net = hcBuilder.buildNetwork(data, 1, bic, null);
        
        System.out.println("_______FINAL NET________");
        System.out.println(net);
    }
    
    public static void testHillClimbing()
    {
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();        
        //BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        HillClimbingBuilder hcBuilder = new HillClimbingBuilder();
        BIC bic = new BIC();
        
        BayesianNetwork net = hcBuilder.buildNetwork(data, 1, bic, null);
        
        System.out.println("_______FINAL NET________");
        System.out.println(net);
    }
    
    public static void testGetAllValidOperations()
    {   
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();        
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
                
        HillClimbingBuilder hcBuilder = new HillClimbingBuilder();
        hcBuilder.net = net;
        ArrayList<Operation> operations = hcBuilder.getAllValidOperations(net.getNodes());
        
        for (Operation o : operations)
        {
           System.out.println(o);
        }
    }
}
