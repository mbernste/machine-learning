package data.fold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.DataSet;
import data.Instance;
import data.InstanceSet;
import data.reader.ArffReader;

import pair.Pair;

public class KFoldCreator {
	
	public static void main(String[] args){
		ArffReader ar = new ArffReader();
		DataSet data = ar.readFile("data/kfold_test.arff");
		data.setClassAttribute("digit");
		List<Pair<DataSet, DataSet>> pairs = KFoldCreator.create(data, 5);
		for(Pair<DataSet, DataSet> pair: pairs){
			System.out.println("TRAIN:");
			System.out.println(pair.getFirst());
			System.out.println("\nTEST:");
			System.out.println(pair.getSecond());
			System.out.println("\n\n");
		}
	}
	
	public static List<Pair<DataSet, DataSet>> create(DataSet data, int K) {

		InstanceSet is = data.getInstanceSet();
		
		List<Instance> instances = is.getInstances();
		Collections.shuffle(instances);
		
		
		int numPerSplice = instances.size()/K;
		List<Pair<DataSet, DataSet>> pairs = new ArrayList<>();
		
		//For each fold
		for(int i = 0; i < K; i++){
			//fill training and testing lists
			List<Instance> trainInst = new ArrayList<>();
			List<Instance> testInst = new ArrayList<>();
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
			DataSet trainData = new DataSet(data.getAttributeSet(), trainSet);
			DataSet testData = new DataSet(data.getAttributeSet(),testSet); 
			
			//Add to list of pairs
			pairs.add(new Pair<DataSet, DataSet>(trainData, testData));
			
		}
		return pairs;
	}

}
