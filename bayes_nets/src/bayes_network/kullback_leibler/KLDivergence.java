package bayes_network.kullback_leibler;

import java.util.Map.Entry;

import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;

/**
 * This class is used to calculate the Kullback-Leibler Divergence 
 * between two DataSets given an attribute
 * 
 * @author schulzca
 *
 */

public class KLDivergence {

	/**
	 * Calculates the Kullback-Leibler Divergence of the given attribute
	 * between the two DataSets
	 * @param dataP first DataSet
	 * @param dataQ second DataSet
	 * @param attr attribute being measured
	 * @return the divergence
	 */
	public static Double divergence(DataSet dataP, DataSet dataQ, Attribute attr){
		double divergence = 0;
		
		for(Entry<String, Integer> value: attr.getNominalValueMap().entrySet()){
			double pX = calculateAttributeValueProbability(dataP, attr, value.getValue());
			double qX = calculateAttributeValueProbability(dataQ, attr, value.getValue());
			divergence += pX * (Math.log(pX) - Math.log(qX));
		}
		
		return divergence;
	}
	
	/**
	 * Calculates the probability of seeing (attr = value) in DataSet data.
	 * Will not work for non-nominal attributes.
	 * @param data	the DataSet
	 * @param attr	the Attribute being checked
	 * @param valueId	the id of the attribute value being looked for
	 * @return	the probability
	 */
	private static Double calculateAttributeValueProbability(DataSet data, Attribute attr, Integer valueId){
		double probability = 0;
		for(Instance inst: data.getInstanceSet().getInstanceList()){
			
			if(attr.getType() == Attribute.NOMINAL){
				if(inst.getAttributeValue(attr.getId()).doubleValue() == valueId){
					probability++;
				}
			} else {
				//Can't compare continuous features?
			}
			
		}
		
		probability /= data.getNumInstances();
		return probability;
	}
}
