package bayes_network.kullback_leibler;

import java.util.ArrayList;

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

	private final static double EPSILON = 0.00001;
	private static double pValue = 0;
	private static double qValue = 0;
	
	/**
	 * Calculates the joint Kullback-Leibler Divergence of the given attribute list
	 * between the two DataSets
	 * @param dataP first DataSet
	 * @param dataQ second DataSet
	 * @param attrs attributes being measured
	 * @return the divergence
	 */
	public static Double divergence(DataSet dataP, DataSet dataQ, ArrayList<Attribute> attrs){
		double divergence = 0;
		
		int numAttrs = attrs.size();
		int[] curIndices = new int[numAttrs];
		do {
			double pX = calculateAttributeValueProbability(dataP, attrs, curIndices);
			double qX = calculateAttributeValueProbability(dataQ, attrs, curIndices);
			divergence += pX * (Math.log(pX) - Math.log(qX));
		}while(incrementIndices(curIndices,attrs,numAttrs - 1));
		return divergence;
	}
	
	/**
	 * Sets pValue and qValue. Used to prevent probabilities of zero,
	 * which would cause infinite divergence.
	 * Possibly inefficient. Work around unknown.
	 * 
	 * @param dataP
	 * @param dataQ
	 */
	private static void setEpsilonValues(DataSet dataP, DataSet dataQ){
		ArrayList<Instance> union = new ArrayList<Instance>();
		ArrayList<Instance> uniqueP = new ArrayList<Instance>();
		ArrayList<Instance> uniqueQ = new ArrayList<Instance>();
		
		for(Instance inst: dataP.getInstanceSet().getInstanceList()){
			//If not yet seen, can add to both lists.
			if(!uniqueP.contains(inst)){
				uniqueP.add(inst);
				union.add(inst);
			}
		}
		
		for(Instance inst: dataP.getInstanceSet().getInstanceList()){
			//If not yet seen in uniqueQ, may still be in union
			if(!uniqueQ.contains(inst)){
				uniqueQ.add(inst);
				
				if(!union.contains(inst)){
					union.add(inst);
				}
			} 
		}
		
		pValue = EPSILON * (union.size() - uniqueP.size()) / uniqueP.size();
		qValue = EPSILON * (union.size() - uniqueQ.size()) / uniqueQ.size();
	}
	
	
	/**
	 * Helps iterate through every possible combination of attribute values
	 * @param indices array of current attr value indices
	 * @param attrs the attributes that the indices are for
	 * @param curIndex index that is currently being incremented
	 * @return whether or not there are more combinations of attribute values
	 */
	private static boolean incrementIndices(int[] indices, ArrayList<Attribute> attrs, int curIndex){
		boolean hasMoreCombinations = true;
		if(curIndex >= 0){
			int size = attrs.get(curIndex).getNominalValueMap().values().size();
			indices[curIndex] = (indices[curIndex] + 1) % size;
			if(indices[curIndex] == 0){
				incrementIndices(indices,attrs,curIndex-1);
			}
		} else {
			hasMoreCombinations = false;
		}
		return hasMoreCombinations;
	}
	
	/**
	 * Calculates the probability of seeing (attr = value) for all attributes in DataSet data.
	 * Will not work for non-nominal attributes.
	 * @param data	the DataSet
	 * @param attrs	the Attributes being checked
	 * @param valueIds	the ids of the attribute values being looked for
	 * @return	the probability
	 */
	private static Double calculateAttributeValueProbability(DataSet data, ArrayList<Attribute> attrs, int[] valueIds){
		double probability = 0;
		for(Instance inst: data.getInstanceSet().getInstanceList()){
			boolean allSame = true;
			int i = 0; 
			while(allSame && i < attrs.size()){
				allSame = inst.getAttributeValue(attrs.get(i).getId()).doubleValue() == valueIds[i];
				i++;
			}
			if(allSame){
				probability++;
			}
		}
		
		probability /= data.getNumInstances();
		return probability;
	}
}
