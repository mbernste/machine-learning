package main;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

import data.DataSet;
import data.arff.ArffGenerator;
import data.arff.ArffReader;
import data.instance.Instance;
import data.instance.InstanceSet;

public class Main 
{
	public final static String CLASS_ATTRIBUTE = "class";
	
	public static void main( String[] args )
	{
		boolean withReplacement = true;
		
		// Parse user input 
		Path trainFile = FileSystems.getDefault().getPath(args[0]);
		int numInstances = Integer.decode(args[1]);
		
		// Read the training data from the arff file
		ArffReader reader = new ArffReader();
		DataSet data = reader.readFile(trainFile);
		data.setClassAttribute(CLASS_ATTRIBUTE);
	
		data.printClassCounts();
	
		Random rand = new Random();
		int randI;
		
		InstanceSet instances = data.getInstanceSet();
		System.out.println("NUM INSTANCES: " + instances.getNumInstances());
		InstanceSet drawnInstances = new InstanceSet();
		
		Map<Integer, Integer> classCounts = data.getClassCounts();
		for (Integer classLabel : classCounts.keySet())
		{
			int numInstancesOfLabel = numInstances * classCounts.get(classLabel).intValue() / data.getNumInstances();
			System.out.println("Draw " + numInstancesOfLabel + " of " + classLabel);
			
			for (int i = 0; i < numInstancesOfLabel; i++)
			{
				Instance currInstance = null;
								
				// Randomly get an instance of the current class label
				do
				{
					randI = rand.nextInt(instances.getNumInstances());
					currInstance = instances.getInstanceAt(randI);	
					
				} while ( currInstance.getAttributeValue( data.getClassAttributeId() ).intValue()
						  != classLabel);
				
				if (!withReplacement)
				{
					instances.removeInstanceAt(randI);
				}
					
				System.out.println("Drew new instance:");
				data.printInstance(currInstance);
				System.out.print("\n");
				
				drawnInstances.addInstance(currInstance);
				
			}
		}
		
		DataSet newData = new DataSet();
		newData.setAttributeSet(data.getAttributeSet());
		newData.setInstanceSet(drawnInstances);
		newData.setClassAttribute(data.getClassAttribute().getName());
				
		ArffGenerator generator = new ArffGenerator();
		generator.generateFile("./subset_" + numInstances + "_1.arff", trainFile, newData);
	}
}
