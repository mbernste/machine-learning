package bayes;

import java.util.ArrayList;


import data.Attribute;
import data.DataSet;
import data.Instance;

public class BNEvaluator 
{

    /**
     * 
     * For each instance in the data, calculate the log-probability of the 
     * bayes net producing this data.  Sum the resulting log-probability
     * of each instance to get the total log-likelihood of the network 
     * generating the data.  Tha  
     *
     * @param net the Bayes net used to calcuate the likelihood of the data
     * @param data the data for which we want to know the likelihood
     * @return the log-likelihood of seeing the data given the net
     */
    public static Double calculateLogLikelihood(BayesianNetwork net, DataSet data)
    {
        Double logProduct = 0.0;
        
        for (Instance instance : data.getInstanceSet().getInstances())
        {        
            ArrayList<BNConditionalQuery> queries = createQueries(instance, net, data);
            
            /*
             * Sum over the probability of each instance 
             */
            for (BNConditionalQuery query : queries)
            {     
                Double p = net.queryConditionalProbability(query);
                logProduct += -Math.log(p);
            }    
        }
        
        return logProduct;
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
    public static ArrayList<BNConditionalQuery> createQueries(Instance instance,
                                                              BayesianNetwork net,
                                                              DataSet data)
    {   
        ArrayList<BNConditionalQuery> queries = new ArrayList<BNConditionalQuery>();
        
        for (BNNode targetNode : net.getNodes())
        {
            BNConditionalQuery query = new BNConditionalQuery();
            
            /*
             * Set target attribute/value 
             */
            Attribute targetAttr = targetNode.getAttribute();
            Integer targetAttrValue 
                        = instance.getAttributeValue(targetAttr).intValue();
            query.setTargetVariable(targetAttr, targetAttrValue);
            
            /*
             * Set each condition attribute/value
             */
            for (BNNode conditionNode : net.getNodes())
            {
                Attribute conditionAttr = conditionNode.getAttribute();
                
                boolean isChildOfTarget = targetNode.getParents().contains(conditionNode);               
                
                if (!conditionNode.equals(targetNode) && isChildOfTarget)
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
