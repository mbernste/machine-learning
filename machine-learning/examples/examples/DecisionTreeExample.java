package examples;
import tree.classifiers.ID3TreeClassifier;
import classify.ClassificationResult;
import data.DataSet;
import data.reader.ArffReader;


/**
 * The following example shows how to learn an ID3 decision tree classifier on 
 * training data set and then how to classify a test data set.
 *
 */
public class DecisionTreeExample 
{
	public static void main( String[] args )
	{	    
		/*
		 * Paths to train and test ARFF files 
		 */
		String trainArffPath = args[0];
		String testArffPath = args[1];
		
		/*
		 * Read data		
		 */
		DataSet trainingData = ArffReader.readFile(trainArffPath);
		DataSet testData = ArffReader.readFile(testArffPath);

		/*
		 * Specify the class attribute name
		 */
		String classAttribute = args[2];
		trainingData.setClassAttribute(classAttribute);		
		testData.setClassAttribute(classAttribute);

		/*
		 * Specify minimum number of instances at a node for splitting on a new attribute.
		 */
		int minInstancesForStopping = Integer.decode(args[3]);
		
		/*
		 * Build the classifier
		 */
		ID3TreeClassifier classifier = new ID3TreeClassifier(minInstancesForStopping, trainingData);
		System.out.println(classifier);
		
		/*
		 * Classify each instance in the test data set 
		 */
		ClassificationResult result = classifier.classifyData(testData);
		System.out.println(result);
	}
}
