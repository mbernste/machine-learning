package data.arff;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import data.DataSet;
import data.attribute.Attribute;
import data.attribute.AttributeSet;
import data.instance.Instance;
import data.instance.InstanceSet;


public class ArffReader 
{
	private AttributeSet attributeSet;
	private InstanceSet instanceSet;
	
	public ArffReader()
	{
		attributeSet = new AttributeSet();
		instanceSet = new InstanceSet();
	}
	
	public DataSet readFile(Path file) 
	{
		attributeSet = new AttributeSet();
		instanceSet = new InstanceSet();
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) 
		{
		    String line = null;
		    while ((line = reader.readLine()) != null) 
		    {
	    		parseLine(line);
		    }
		} 
		catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		}
				
		DataSet dataSet = new DataSet();
		dataSet.setAttributeSet(attributeSet);
		dataSet.setInstanceSet(instanceSet);
		
		return dataSet;
	}
	
	/**
	 * Processes a single line in an ARFF file.  This method shoudl be called on each
	 * line of the ARFF file being read.
	 * 
	 * @param arffLine - any line from the ARFF file 
	 */
	private void parseLine(String arffLine)
	{
		if (arffLine.length() < 3)
		{
			return;
		}
		
		if (arffLine.charAt(0) == '@')
		{
			parseHeaderLine(arffLine);
		}
		else if (arffLine.charAt(0) != '%')
		{
			instanceSet.addInstance( createInstance(arffLine) );
		}
	}
	
	/**
	 * This function is called on all lines that begin with the '@' symbol
	 * and is used for parsing all header lines.
	 * 
	 * If the 
	 */
	private void parseHeaderLine(String arffLine)
	{
		String[] tokens = arffLine.split(" ");
		if (tokens[0].equals("@attribute"))
		{
			addAttribute(arffLine);
		}
	}
	
	/**
	 * Helper method for adding an attribute to the attribute set.
	 * This method is called to process a line in a ARFF file with the
	 * first token being "@attribute"
	 * 
	 * @param arffLine - A line from the ARFF file where the first token is "@attribute"
	 */
	private void addAttribute(String arffLine)
	{
		String[] tokens = arffLine.split(" ");
		
		String attrName;
		Integer attrType = 0;
		String[] attributeValues = null;
		
		// Parse attribute name
		attrName = tokens[1].split("'")[1];
		
		// Parse attribute type (continuous or nominal)
		if (tokens[2].equals("real"))
		{
			attrType =  Attribute.CONTINUOUS;
		}
		else 
		{
			attrType = Attribute.NOMINAL;
		}
	
		
		// Get nominal values for this attribute if it is nominal
		if (attrType == Attribute.NOMINAL)
		{
			attributeValues = getNominalAttributeValues(arffLine);
		}
		
		attributeSet.addAttribute(attrName, attrType, attributeValues);	
	}
	
	/**
	 * A helper method that returns an array of strings where each string
	 * is a value of a nominal attribute in a given "@attribute" line in 
	 * the ARFF file
	 * 
	 * @param arffline - A line in the ARFF file that begins with "@attribute"
	 * @return The values of the nominal attributes in this line
	 */
	private String[] getNominalAttributeValues(String arffLine)
	{
		// The pattern finds the string contained in curly braces
		String pattern = "\\{(.*)\\}";
		
		// Compile regex pattern and match it in our ARFF line
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher regexMatcher = regexPattern.matcher(arffLine);
		regexMatcher.find();
		
		// Tokenize the string inside the curly braces
		String[] attributeValues = trimAllStrings( regexMatcher.group(1).split(",") );
		
		return attributeValues;
	}
	
	/**
	 * Create an Instance object from a line in the ARFF file that corresponds to an 
	 * instance
	 * 
	 * @param arffLine a line in the ARFF file that corresponds to an instance
	 * @return
	 */
	private Instance createInstance(String arffLine)
	{	
		String[] tokens = trimAllStrings( arffLine.split(",") );
		
		Instance newInstance = new Instance();
	
		for (int index=0; index < tokens.length; index++)
		{
			
			// Find attribute at this index
			Attribute currAttribute = attributeSet.getAttributeById(index);

			if (tokens[index].equals("?"))
			{
				//TODO: Figure out how to handle missing attributes
			}
			else
			{			
				// Parse value of attribute
				if (currAttribute.getType() == Attribute.CONTINUOUS)
				{
					newInstance.addAttributeInstance(index, Double.parseDouble(tokens[index]));
				}
				else if (currAttribute.getType() == Attribute.NOMINAL)
				{
					Integer nominalValueId = currAttribute.getNominalValueId(tokens[index]);
					newInstance.addAttributeInstance(index, nominalValueId.doubleValue());
				}
			}
		}
		
		return newInstance;
	}
	
	private String[] trimAllStrings(String[] rawStrings)
	{
		String[] trimmedStrings = new String[rawStrings.length];
		
		for (int i=0; i<rawStrings.length; i++)
		{
			trimmedStrings[i] = rawStrings[i].trim();
		}
		
		return trimmedStrings;
		
	}
}
