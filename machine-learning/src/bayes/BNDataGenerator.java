package bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import pair.Pair;
import bayes.cpd.CPDQuery;
import data.Attribute;
import data.DataSet;
import data.Instance;
import data.InstanceSet;

/**
 * Used for generating a data set from a learned Bayesian Network.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNDataGenerator 
{
    /**
     * This method produces an artificial data set generated from a Bayesian
     * network.
     * 
     * @param net the Bayesian network used to generate the data set
     * @param numInstances the number of instances to be generated
     * @return an artificial data set
     */
    public static DataSet generateDataSet(BayesianNetwork net, int numInstances)
    {
        /*
         * Build instance set and set the instance set to the new data set
         */
        InstanceSet instances = new InstanceSet();
        for (int i = 0; i < numInstances; i++)
        {
            Instance newInst = new Instance();
            for (BNNode node : net.network.topologicallySorted())
            {                              
                setAttrInstance(node, newInst); 
            }       
            instances.addInstance(newInst);
        } 
        
        return new DataSet(null, instances);   
    }
    
    
    /**
     * Given a node and an instance under construction, consider the value of
     * the parent attributes that have already been picked for this instance.
     * Choose the value for the specified node's attribute based on the 
     * values of the values of these parent attributes in the instance.
     * 
     * @param node the current node for which we need to pick a value to
     * assign to the instance
     * @param instance the instance for which we need to assign the value of the
     * specified node's attribute.  This instance MUST have values for the 
     * node's parent attributes in the network.  If this is not the case,
     * this method will throw an exception.
     */
    private static void setAttrInstance(BNNode node, Instance instance)
    {      
        /*
         * The current attribute
         */
        Attribute thisAttr = node.getAttribute();
        
        /*
         * Maps a nominal value ID of the current attribute to a probability
         */
        Map<Double, Double> valueProbabilities = 
                    new HashMap<Double, Double>();
        
        /*
         * Stores attribute/value pairs
         */
        List<Pair<Attribute, Integer>> parentValues
                                   = new ArrayList<Pair<Attribute, Integer>>();
        /*
         * Organize all parent's attribute/value pairs
         */
        for (BNNode parentNode : node.getParents())
        {

            Attribute parentAttr = parentNode.getAttribute();
            Double instValue = instance.getAttributeValue(parentAttr);
            
            if (instValue == null)
            {
                throw new RuntimeException("Error. While determining new " +
                        "instance value for attribute " + thisAttr.getName() + 
                        " the parent attribute, " + parentAttr.getName() + 
                        " value for this instance is null.");
            }
            
            Pair<Attribute, Integer> valuePair 
                        = new Pair<Attribute, Integer>(parentAttr, instValue.intValue());
            parentValues.add(valuePair);
        }
    
        /*
         * Build the CPD query for each nominal value of the current node's
         * nominal value and query the node for the probability of that
         * nominal value.
         */
        for (Integer nominalValue : node.getAttribute().getNominalValueMap().values())
        {
            CPDQuery query = new CPDQuery();
            for (Pair<Attribute, Integer> value : parentValues)
            {
                query.addQueryItem(value.getFirst(), value.getSecond());
            }
            query.addQueryItem(node.getAttribute(), nominalValue);
            
            // Add value/probability pair to the probability distribution
            valueProbabilities.put(nominalValue.doubleValue(), 
                                   node.query(query) );
        }
        
        /*
         * Pick nominal value and assign it to the instance
         */
        Double value = pickRandomValue(valueProbabilities);
        instance.addAttributeValue(thisAttr, value);
    }
    
    /**
     * Picks a nominal value for some attribute from a probability distribution.
     * 
     * @param valueProbabilities a mapping of nominal value ID's to 
     * probabilities of picking that nominal value
     * @return the nominal value ID chosen randomly from the given probability
     * distribution
     */
    public static Double pickRandomValue(Map<Double, Double> valueProbabilities)
    {
        Random rand = new Random();
        double pick = rand.nextDouble();
        
        /*
         * Begin range/end range. If the value of the pick falls in this range,
         * return the current value.  The size of this range is proportional
         * to the probability of picking that value.
         */
        double beginRange = 0;
        double endRange = 0;
       
        
        for (Entry<Double, Double> entry : valueProbabilities.entrySet())
        {
           // Update ranges
           beginRange = endRange;
           endRange += entry.getValue();
           
           // Determine if this value has been chosen
           if (pick >= beginRange && pick < endRange)
           {               
               return entry.getKey();
           }
        }
        
        return null;
    }
}
