package kfold;

import java.util.ArrayList;
import java.util.Collections;

import pair.Pair;
import data.DataSet;
import data.arff.ArffReader;
import data.instance.Instance;
import data.instance.InstanceSet;

public class KFoldCreator {
	
	public static void main(String[] args){
		ArffReader ar = new ArffReader();
		DataSet data = ar.readFile("data/kfold_test.arff");
		data.setClassAttribute("digit");
		ArrayList<Pair<DataSet, DataSet>> pairs = KFoldCreator.create(data, 5);
		for(Pair<DataSet, DataSet> pair: pairs){
			System.out.println("TRAIN:");
			System.out.println(pair.getFirst());
			System.out.println("\nTEST:");
			System.out.println(pair.getSecond());
			System.out.println("\n\n");
		}
	}
	
	public static ArrayList<Pair<DataSet, DataSet>> create(DataSet data, int K) {

		InstanceSet is = data.getInstanceSet();
		
		ArrayList<Instance> instances = is.getInstanceList();
		Collections.shuffle(instances);
		
		
		int numPerSplice = instances.size()/K;
		ArrayList<Pair<DataSet, DataSet>> pairs = new ArrayList<Pair<DataSet, DataSet>>();
		
		//For each fold
		for(int i = 0; i < K; i++){
			//fill training and testing lists
			ArrayList<Instance> trainInst = new ArrayList<Instance>();
			ArrayList<Instance> testInst = new ArrayList<Instance>();
			int left = i * numPerSplice;
			int right = i * numPerSplice + numPerSplice;
			trainInst.addAll(instances.subList(0, left));
			if(i + 1 == K){
				testInst.addAll(instances.subList(left, instances.size()));
			} else {
				testInst.addAll(instances.subList(left, right));
				trainInst.addAll(instances.subList(right, instances.size()));
			}
			
			//put in sets
			InstanceSet trainSet = new InstanceSet();
			for(Instance inst: trainInst){
				trainSet.addInstance(inst);
			}
			
			InstanceSet testSet = new InstanceSet();
			for(Instance inst: testInst){
				testSet.addInstance(inst);
			}
			//create data sets
			DataSet trainData = new DataSet();
			DataSet testData = new DataSet(); 
			
			//set up data sets
			trainData.setAttributeSet(data.getAttributeSet());
			testData.setAttributeSet(data.getAttributeSet());
			trainData.setInstanceSet(trainSet);
			testData.setInstanceSet(testSet);
			
			//Add to list of pairs
			pairs.add(new Pair<DataSet, DataSet>(trainData, testData));
			
		}
		return pairs;
	}

}
