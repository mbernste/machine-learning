package main;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import tree.DecisionTree;
import tree.TreeGenerator;

import data.DataSet;
import data.arff.ArffReader;
import evaluate.BiClassTest;
import evaluate.BiClassTestResults;

public class Main 
{	
	public final static String CLASS_ATTRIBUTE = "class";
	
	public static void main( String[] args )
	{
		// Parse user input
		Path trainFile = FileSystems.getDefault().getPath(args[0]);
		Path testFile = FileSystems.getDefault().getPath(args[1]);
		int minInstances = Integer.decode(args[2]);
		
		// Read the training data from the arff file
		ArffReader reader = new ArffReader();
		DataSet trainingData = reader.readFile(trainFile);
		trainingData.setClassAttribute(CLASS_ATTRIBUTE);		
				
		// Generate the decision tree
		TreeGenerator treeGenerator = new TreeGenerator();
		DecisionTree decisionTree = treeGenerator.generateDecisionTree(minInstances, trainingData);
		
		// Print the tree
		decisionTree.printTree(decisionTree.getRoot());
		System.out.print("\n");		
		
		// Evaluate the tree on the testing set
		DataSet testData = reader.readFile(testFile);
		testData.setClassAttribute(CLASS_ATTRIBUTE);
		BiClassTestResults results = BiClassTest.runTest(testData, decisionTree);
						
		// Print the results
		results.printResults();
	}
}
