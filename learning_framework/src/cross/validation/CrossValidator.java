package cross.validation;

import data.DataSet;
import data.arff.ArffReader;
import data.instance.Instance;
import data.instance.InstanceSet;

public class CrossValidator {

	private final static int K = 5;
	
	public static void main(String[] args) {
		String inFile = args[0];
		String outFile = args[0] + ".results";
		
		ArffReader ar = new ArffReader();
		DataSet data = ar.readFile(inFile);
		
		InstanceSet is = data.getInstanceSet();
		
		ArrayList<Instance> instances = is.getInstanceList();
		
		Collections.shuffle(instances);
		
		ArrayList<Instance> training = new ArrayList<Instance>();
		ArrayList<Instance> testing = new ArrayList<Instance>();
		int numPerSplice = instances.size()/K;
		
		
		
		for(int i = 0; i < K; i++){
			training.clear();
			testing.clear();
			int left = i * numPerSplice;
			int right = i * numPerSplice + numPerSplice;
			training.addAll(instances.subList(0, left));
			if(i + 1 == K){
				testing.addAll(instances.subList(left, instances.size()));
			} else {
				testing.addAll(instances.subList(left, right));
				training.addAll(instances.subList(right, instances.size()));
			}
			
			
		}
	}

}
