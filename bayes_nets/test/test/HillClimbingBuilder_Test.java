package test;

import java.util.ArrayList;

import pair.Pair;
import bayes_network.BayesianNetwork;
import bayes_network.builders.HillClimbingBuilder;
import bayes_network.builders.Operation;
import data.DataSet;

public class HillClimbingBuilder_Test 
{
    public static void main(String[] args)
    {
        testGetAllValidOperations();
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
