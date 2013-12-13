package test;

import classification.ClassificationResult;
import pair.Pair;
import prim.Prim;
import data.DataSet;
import data.arff.ArffReader;
import data.attribute.Attribute;
import data.instance.Instance;
import bayes_network.BayesianNetwork;
import bayes_network.builders.NaiveBayesBuilder;
import bayes_network.builders.TANBuilder;
import bayes_network.classification.NaiveBayesClassifier;
import bayes_network.cpd.CPDQuery;
import bayes_network.cpd.CPDTree;
import bayes_network.cpd.CPDTreeBuilder;

public class Test 
{
	public static void main(String[] args)
	{
		//testBayesTheorem();
		//testCPDQuery();
		//testTANJointConditionalProbability();
		//testTANConditionalProbability();
		//testTANJointProbability();
		testTANCondMutualInfo();
		//testPrim();
		testTANClassification();
	}
	
	public static void testTANClassification()
	{
		// Read the training data from the arff file
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile("./data/lymph_train.arff");
		data.setClassAttribute("class");
		
		// Create Naive Bayes Classifier	
		NaiveBayesClassifier nbClassifier = 
				new NaiveBayesClassifier(data, 1, true);
		
		// Read the training data from the arff file
		DataSet testData = reader.readFile("./data/lymph_test.arff");
		testData.setClassAttribute("class");
		
		ClassificationResult result = nbClassifier.classifyData(testData);
		
		System.out.println(nbClassifier);
		System.out.print("\n\n");
		System.out.println(result);
	}
	
	public static void testPrim()
	{
		Double[][] graph1 = 
				{
					{-1.0, 6.0, 3.0},
					{6.0, -1.0, 2.0},
					{3.0, 2.0, -1.0}
				};
		
		System.out.println(Prim.runPrims(graph1));
	}
	
	public static void testTANCondMutualInfo()
	{
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile("./data/lymph_train.arff");
		//DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		TANBuilder tBuilder = new TANBuilder();
		tBuilder.buildNetwork(data, 1);
	}
	
	public static void testTANJointProbability()
	{
		ArffReader reader = new ArffReader();
		//DataSet data = reader.readFile("./data/lymph_train.arff");
		DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		TANBuilder tBuilder = new TANBuilder();
		tBuilder.buildNetwork(data, 1);
		
		Attribute attr1 = data.getAttributeByName("sex");
		Integer val1 = attr1.getNominalValueId("female");
		
		Pair<Attribute, Integer> p1 = new Pair<Attribute, Integer>();
		p1.setFirst(attr1);
		p1.setSecond(val1);
		
		Attribute attr2 = data.getAttributeByName("color");
		Integer val2 = attr2.getNominalValueId("green");
		
		Pair<Attribute, Integer> p2 = new Pair<Attribute, Integer>();
		p2.setFirst(attr2);
		p2.setSecond(val2);
		
		Integer classVal = data.getClassAttribute().getNominalValueId("positive");
		
		Double result = tBuilder.jointProbability(p1, p2, classVal);
		
		System.out.println(result);
	}
	
	public static void testTANConditionalProbability()
	{
		ArffReader reader = new ArffReader();
		//DataSet data = reader.readFile("./data/lymph_train.arff");
		DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		TANBuilder tBuilder = new TANBuilder();
		tBuilder.buildNetwork(data, 1);
		
		Attribute attr = data.getAttributeByName("sex");
		Integer val = attr.getNominalValueId("female");
		
		Pair<Attribute, Integer> p = new Pair<Attribute, Integer>();
		p.setFirst(attr);
		p.setSecond(val);

		Integer classVal = data.getClassAttribute().getNominalValueId("positive");
		
		Double result = tBuilder.conditionalProbability(p, classVal);
		
		System.out.println(result);
	}
	
	public static void testTANJointConditionalProbability()
	{
		ArffReader reader = new ArffReader();
		//DataSet data = reader.readFile("./data/lymph_train.arff");
		DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		TANBuilder tBuilder = new TANBuilder();
		tBuilder.buildNetwork(data, 1);
		
		Attribute attr1 = data.getAttributeByName("sex");
		Integer val1 = attr1.getNominalValueId("female");
		
		Pair<Attribute, Integer> p1 = new Pair<Attribute, Integer>();
		p1.setFirst(attr1);
		p1.setSecond(val1);
		
		Attribute attr2 = data.getAttributeByName("color");
		Integer val2 = attr2.getNominalValueId("green");
		
		Pair<Attribute, Integer> p2 = new Pair<Attribute, Integer>();
		p2.setFirst(attr2);
		p2.setSecond(val2);
		
		Integer classVal = data.getClassAttribute().getNominalValueId("positive");
		
		Double result = tBuilder.conditionalJointProbability(p1, p2, classVal);
		
		System.out.println(result);
	}
	
	public static void testBayesTheorem()
	{
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		
		NaiveBayesClassifier nbClassifier = 
				new NaiveBayesClassifier(data, 1, false);
		
		Instance testInstance = data.getInstanceSet().getInstanceAt(0);
		
		Integer neg = data.getAttributeByName("class").getNominalValueId("positive");
		
		
		Double p = null;
		try
		{
			p = nbClassifier.bayesTheorem(testInstance, neg);
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		
		System.out.println(p);
	}
	
	
	public static void testCPDQuery()
	{
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile("./data/test.arff");
		data.setClassAttribute("class");
		
		CPDTreeBuilder treeBuilder = new CPDTreeBuilder();
		CPDTree tree = treeBuilder.buildCPDTree(data, data.getAttributeList(), 1);
		
		System.out.println(tree);
		
		Attribute sex = data.getAttributeByName("sex");
		Integer male = sex.getNominalValueId("male");
		
		Attribute color = data.getAttributeByName("color");
		Integer red = color.getNominalValueId("red");
		
		Attribute classA = data.getAttributeByName("class");
		Integer neg = classA.getNominalValueId("positive");
		
		CPDQuery query = new CPDQuery();
		query.addQueryItem(color, red);
		query.addQueryItem(classA, neg);
		query.addQueryItem(sex, male);
		
		System.out.println(tree.query(query));
	}

}
