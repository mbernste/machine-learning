package main;

import tree.DecisionTree;
import tree.ID3Builder;

import data.DataSet;
import data.arff.ArffReader;

public class DTMain 
{	
	public final static String CLASS_ATTRIBUTE = "class";
	
	public static void main( String[] args )
	{	    
		// Parse user input
		int minInstances = Integer.decode(args[1]);
		
		// Read the training data from the arff file
		ArffReader reader = new ArffReader();
		DataSet trainingData = reader.readFile(args[0]);
		trainingData.setClassAttribute(CLASS_ATTRIBUTE);		
				
		// Generate the decision tree
		ID3Builder treeGenerator = new ID3Builder();
		DecisionTree decisionTree = treeGenerator.generateDecisionTree(minInstances, trainingData);
		
		// Print the tree
		decisionTree.printTree(decisionTree.getRoot());
		System.out.print("\n");		
	}
}
