package test;

import java.util.ArrayList;

import pair.Pair;
import bayes_network.BNConditionalQuery;
import bayes_network.BayesianNetwork;
import bayes_network.builders.scoring.BIC;
import data.DataSet;
import evaluate.BNEvaluator;

public class BNEvaluator_Test 
{
    public static void main(String[] args)
    {
        testCreateQueries();
    }
    
    public static void testCreateQueries()
    {        
        Pair<BayesianNetwork, DataSet> testKit = TestNetworkBuilder.buildTestNetwork1();
        BayesianNetwork net = testKit.getFirst();
        DataSet data = testKit.getSecond();
        
        BIC bic = new BIC();
        BNEvaluator.createQueries(data.getInstanceList().get(0), net, data);
        
        ArrayList<BNConditionalQuery> queries = BNEvaluator.createQueries(data.getInstanceList().get(0), net, data);
        for (BNConditionalQuery query : queries)
        {
            System.out.println(query + " = " + net.queryConditionalProbability(query) + "\n");
        }  
    }

}
