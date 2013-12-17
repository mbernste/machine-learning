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
        Double logProduct = 0.0;
        
        /*
         * Calculate log-likelihood of the data
         */
        for (Instance instance : data.getInstanceList())
        {            
            ArrayList<BNConditionalQuery> queries = createQueries(instance, data);
            
            Double logInstanceProduct = 0.0; 
            for (BNConditionalQuery query : queries)
            {
                Double p = net.queryConditionalProbability(query);
                logInstanceProduct += -Math.log(p);
                logProduct += -Math.log(p);
            }
            System.out.println("Log Probability of Instance " + logInstanceProduct);  
        }
        
        /*
         * Calculate penalty term
         */
        Double penalty = calculatePenaltyTerm(net, data);
        
        System.out.println("TOTAL PROBABILITY: " + logProduct);
        
        return score;
    }
    
    public Double calculatePenaltyTerm(BayesianNetwork net, DataSet data)
    {
        //Integer freeParameters = net.totalFreeParameter();
        
        // TODO: CALCULATE THIS PENALTY TERM
        
        return null;
    }
    
    /**
     * For each instance we need to create a conditional probability 
     * query on the value of each instance's attributes given the values of the
     * rest of the attributes.
     * 
     * @param instance the instance under examination
     * @param net the Bayes net
     * @param data the data set 
     * @return a query for each attribute in the instance
     */
    public ArrayList<BNConditionalQuery> createQueries(Instance instance,
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
                           = instance.getAttributeValue(conditionAttr).intValue();
                                        
                    query.addConditionVariable(conditionAttr, conditionAttrValue);
                }
            }
           
           queries.add(query);
        }        
                
        return queries;
    }
}
