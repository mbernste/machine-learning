package bayes.information;

import java.util.ArrayList;
import java.util.Arrays;


import data.Attribute;
import data.DataSet;
import data.Instance;

/**
 * This class is used to calculate the Kullback-Leibler Divergence 
 * between two DataSets given an attribute. 
 * 
 * @author schulzca
 *
 */

public class KLDivergence {

	private static int verbose = 0;
	
	/*
	 * Used for probability in place of 0 probability instances.
	 */
	private final static double EPSILON = 0.00001;
	
	/*
	 * Adjustment values to account for unseen data in order
	 * to keep the sum of the probability for all possible 
	 * attribute values equal to 1.
	 */
	private static double pValue = 0;
	private static double qValue = 0;
	
	/*
	 * Static vars used to prevent passing of data
	 */
	private static double[] pProbabilities = null;
	private static double[] qProbabilities = null;
	private static int[] offsets = null;
	private static Attribute[] conditions = new Attribute[2];
	
	/**
	 * Calculates the joint Kullback-Leibler Divergence of the given attribute list
	 * between the two DataSets. Extremely vulnerable to race conditions. Do not run
	 * in parallel.
	 * @param dataP first DataSet
	 * @param dataQ second DataSet
	 * @param attrs attributes being measured
	 * @return the divergence
	 */
	public static Double divergence(DataSet dataP, DataSet dataQ, Attribute a, Attribute b){
		setConditions(a,b);
		resetProbabilities(dataP, dataQ);
		resetOffsets();
		setEpsilonValues(dataP, dataQ);
		calculateAttributeValueProbabilities(dataP, dataQ);
		
		int[] curIndices = new int[conditions.length];
				
		double divergence = 0;
		do {
			double pX = pProbabilities[convertIndicesToIndex(curIndices)];
			double qX = qProbabilities[convertIndicesToIndex(curIndices)];
			//Prevent infinite divergence
			if(pX != 0 && qX == 0){
				pX -= pValue;
				qX = EPSILON;
			//Allow zero probability if both are zero.
			} else if(!(pX == 0 && qX == 0)){
				//Otherwise, adjust values as needed.
				pX = (pX == 0 ? EPSILON : pX - pValue);
				qX = (qX == 0 ? EPSILON : qX - qValue);		
			}
			
			//force 0 * log(0) to be 0 by skipping calculation
			//as 0 * -infinity is NaN in Java
			if(pX != 0){
				divergence += pX * (Math.log(pX) - Math.log(qX));
			}
		} while(incrementIndices(curIndices,conditions.length - 1));
		
		return divergence;
	}
	
	private static void setConditions(Attribute a, Attribute b) {
		conditions[0] = a;
		conditions[1] = b;	
	}

	private static int convertIndicesToIndex(int[] indices) {
		int index = 0;
		for(int i = 0; i < indices.length; i++){
			index += indices[i] * offsets[i];
		}
		return index;
	}

	private static void resetProbabilities(DataSet dataP, DataSet dataQ){
		int size = 1;
		for(Attribute attr: conditions){
			size *= attr.getNominalValueMap().size();
		}
		
		pProbabilities = new double[size];
		qProbabilities = new double[size];
	}
	
	private static void resetOffsets(){
		//initialize offset table
		offsets = new int[conditions.length];		
		int size = 1;
		for(int i = 0; i < offsets.length; i++){
			offsets[i] = size;
			size *= conditions[i].getNominalValueMap().size();
		}
	}
	
	private static void calculateAttributeValueProbabilities(DataSet dataP,
			DataSet dataQ) {
				
		//iterate through instances and update count of relevant entry in
		//the probability table
		for(Instance inst: dataP.getInstanceSet().getInstances()){
			int[] indices = convertInstanceToIndices(inst);
			pProbabilities[convertIndicesToIndex(indices)]++;
		}
		
		for(Instance inst: dataQ.getInstanceSet().getInstances()){
			int[] indices = convertInstanceToIndices(inst);
			qProbabilities[convertIndicesToIndex(indices)]++;
		}
		
		
		//divide each count by num instances to get probability
		int numPInstances = dataP.getInstanceSet().getInstances().size();
		int numQInstances = dataQ.getInstanceSet().getInstances().size();;
		for(int i = 0; i < pProbabilities.length; i++){
			pProbabilities[i] /= numPInstances;
			qProbabilities[i] /= numQInstances;
		}
		
		if(verbose > 1){
			System.out.println(Arrays.toString(pProbabilities));
			System.out.println(Arrays.toString(qProbabilities));
			
		}
	}

	private static int[] convertInstanceToIndices(Instance inst) {
		int[] indices = new int[conditions.length];
		for(int i = 0; i < indices.length; i++){
			indices[i] = inst.getAttributeValue(conditions[i]).intValue();
		}
		return indices;
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
		
		for(Instance inst: dataP.getInstanceSet().getInstances()){
			//If not yet seen, can add to both lists.
			if(!contains(uniqueP, inst)){
				uniqueP.add(inst);
				union.add(inst);
			}
		}
		
		for(Instance inst: dataQ.getInstanceSet().getInstances()){
			//If not yet seen in uniqueQ, may still be in union
			if(!contains(uniqueQ,inst)){
				uniqueQ.add(inst);
				
				if(!contains(union, inst)){
					union.add(inst);
				}
			} 
		}
		pValue = EPSILON * (union.size() - uniqueP.size()) / uniqueP.size();
		qValue = EPSILON * (union.size() - uniqueQ.size()) / uniqueQ.size();
		
		if(verbose > 0){
			System.out.println("Union: " + union.size() + 
							   "\tP: " + uniqueP.size() + 
							   "\tQ: " + uniqueQ.size());	
			System.out.println("pValue: " + pValue + "\tqValue: " + qValue);
		}
	}
	
	
	/**
	 * Helps iterate through every possible combination of attribute values
	 * @param indices array of current attr value indices
	 * @param attrs the attributes that the indices are for
	 * @param curIndex index that is currently being incremented
	 * @return whether or not there are more combinations of attribute values
	 */
	private static boolean incrementIndices(int[] indices, int curIndex){
		boolean hasMoreCombinations = true;
		if(curIndex >= 0){
			int size = conditions[curIndex].getNominalValueMap().values().size();
			indices[curIndex] = (indices[curIndex] + 1) % size;
			if(indices[curIndex] == 0){
				hasMoreCombinations = incrementIndices(indices,curIndex-1);
			}
		} else {
			hasMoreCombinations = false;
		}
		return hasMoreCombinations;
	}
	
	private static boolean contains(ArrayList<Instance> list, Instance inst){
		for(Instance currInstance: list){
			if(instancesAreEqual(currInstance, inst)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Checks if a and b are equals for the relevant attributes
	 */
	private static boolean instancesAreEqual(Instance a, Instance b){
		for(Attribute attr: conditions){
			if(!a.getAttributeValue(attr).equals(b.getAttributeValue(attr))){
				return false;
			}
		}
		return true;
	}
}
