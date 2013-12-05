package main;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

import pair.Pair;

import bayes_network.BayesianNetwork;
import bayes_network.builders.NaiveBayesBuilder;
import bayes_network.classification.ClassificationResult;
import bayes_network.classification.NaiveBayesClassifier;

import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;
import data.instance.Instance;

public class MainNaiveBayes 
{
	public static void main(String[] args)
	{
		/*
		 * Print useage if not enough arguments
		 */
		if (args.length != 3)
		{
			printUsage();
			return;
		}
		
		/*
		 *  Determine whether to use TAN or simple Naive Bayes
		 */
		boolean tan;
		if (args[2].equals("t"))
		{
			tan = true;
		}
		else if (args[2].equals("n"))
		{
			tan = false;
		}
		else
		{
			printUsage();
			return;
		}
		
		/*
		 *  Read the training data from the arff file
		 */
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile(args[0]);
		data.setClassAttribute("class");
	
		/*
		 *  Create Naive Bayes classifier
		 */
		NaiveBayesClassifier nbClassifier = 
				new NaiveBayesClassifier(data, 1, tan);
		
		/*
		 *  Print network
		 */
		System.out.println(nbClassifier);
		
		/*
		 *  Read the training data from the arff file
		 */
		DataSet testData = reader.readFile(args[1]);
		testData.setClassAttribute("class");
		
		/*
		 * Classify the data
		 */
		ClassificationResult result = nbClassifier.classifyData(testData);
		
		/*
		 * Print results to standard output
		 */
		System.out.print("\n\n");
		System.out.println(result);
	}
	
	/**
	 * Prints this program's usage to standard output
	 */
	public static void printUsage()
	{
		System.out.println("\nUsage: bayes <train-set-file> <test-set-file> <n|t>\n");
	}
}
