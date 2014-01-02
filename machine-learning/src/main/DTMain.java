package main;

import common.classification.ClassificationResult;

import tree.DecisionTree;
import tree.classifiers.ID3TreeClassifier;

import data.DataSet;
import data.arff.ArffReader;

public class DTMain 
{	
	public final static String CLASS_ATTRIBUTE = "class";
	
	public static void main( String[] args )
	{	    
		// Parse user input
		int minInstances = Integer.decode(args[2]);
		
		// Read the training data from the arff file
		ArffReader reader = new ArffReader();
		DataSet trainingData = reader.readFile(args[0]);
		trainingData.setClassAttribute(CLASS_ATTRIBUTE);		
		
		DataSet testData = reader.readFile(args[1]);
		testData.setClassAttribute(CLASS_ATTRIBUTE);
		
		ID3TreeClassifier classifier = new ID3TreeClassifier(minInstances, trainingData);
		
		ClassificationResult result = classifier.classifyData(testData);
		
		((DecisionTree)classifier.getModel()).printTree();
		System.out.println(result);
	}
}
