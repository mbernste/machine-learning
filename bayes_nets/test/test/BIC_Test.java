package test;

import java.util.ArrayList;

import data.DataSet;
import pair.Pair;
import bayes_network.BNConditionalQuery;
import bayes_network.BayesianNetwork;
import bayes_network.builders.scoring.BIC;

public class BIC_Test 
{
    public static void main(String[] args)
    {
        testBIC();
    }
    
    public static void testBIC()
    {        
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        BIC bic = new BIC();
        bic.createQueries(data.getInstanceList().get(0), net, data);
        
        ArrayList<BNConditionalQuery> queries = bic.createQueries(data.getInstanceList().get(0), net, data);
        for (BNConditionalQuery query : queries)
        {
            System.out.println(query + " = " + net.queryConditionalProbability(query) + "\n");
        }  
    }

}
