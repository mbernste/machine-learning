package bayes_network.builders.scoring;

import java.util.ArrayList;

import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;
import bayes_network.BNConditionalQuery;
import bayes_network.BayesianNetwork;

/**
 * Scores a Bayesian network against a dataset using Bayesian Information 
 * Criterion (BIC).
 * 
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BIC implements ScoringFunction
{    
    
    public Double scoreNet(BayesianNetwork net, DataSet data)
    {
        Double score = 0.0;
        
        /*
        for (Instance instance : data.getInstanceList())
        {
            ArrayList<BNConditionalQuery> queries = createQueries(instance, net, data);
        }*/
        
        return score;
    }
    
    public ArrayList<BNConditionalQuery> createQueries(Instance instance,
                                                       BayesianNetwork net,
                                                       DataSet data)
    {
        ArrayList<BNConditionalQuery> queries = new ArrayList<BNConditionalQuery>();
        
        for (Attribute targetAttr : data.getAttributeList())
        {
            BNConditionalQuery query = new BNConditionalQuery();
    
            /*
             * Set target attribute/value 
             */
            Integer targetAttrValue 
                        = instance.getAttributeValue(targetAttr).intValue();
            query.setTargetVariable(targetAttr, targetAttrValue);
            
            /*
             * Set each condition attribute/value
             */
            for (Attribute conditionAttr : data.getAttributeList())
            {
                if (!conditionAttr.equals(targetAttr))
                {
                    Integer conditionAttrValue
                           = instance.getAttributeValue(targetAttr).intValue();
                    query.addConditionVariable(conditionAttr, conditionAttrValue);
                }
            }
            
           queries.add(query);
        }        
        
        return queries;
    }
}
