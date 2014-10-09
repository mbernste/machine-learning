package data.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import data.Attribute;
import data.AttributeSet;
import data.DataSet;
import data.Instance;
import data.InstanceSet;


/**
 * Reads an Attribute-Relation File Format (ARFF) file and extracts the
 * attributes and instances in the file.
 * 
 * test
 *
 */
public class ArffReader 
{
    private final static char ARFF_HEADER_CHAR = '@';
    private final static char ARFF_COMMENT_CHAR = '%';
    
	/**
	 * Stores the attribute set extracted from the ARFF file
	 */
	private AttributeSet attributeSet;
	
	/**
	 * Stores the instances extracted from the ARFF file
	 */
	private InstanceSet instanceSet;
	
	/**
	 * Constructor
	 */
	public ArffReader()
	{
		attributeSet = new AttributeSet();
		instanceSet = new InstanceSet();
	}
	
	/**
	 * Read and extract a data set from an ARFF file
	 * 
	 * @param file a path to the ARFF file
	 * @return a data set storing all data in the file
	 */
	public DataSet readFile(String file) 
	{
		attributeSet = new AttributeSet();
		instanceSet = new InstanceSet();
		
		try
		{
			Scanner scan = new Scanner(new FileInputStream(file));

			/*
			 *	Process every line in the file
			 */
		    String line = null;
		    while (scan.hasNextLine()) 
		    {
		    	line = scan.nextLine();
	    		parseLine(line);
		    }
		    
		    scan.close();	    
		} 
		catch (FileNotFoundException x) 
		{
		    System.err.format("FileNotFountException: %s%n", x);
		}
				
		/*
		 * Create the final data set
		 */
		DataSet dataSet = new DataSet();
		dataSet.setAttributeSet(attributeSet);
		dataSet.setInstanceSet(instanceSet);
		
		return dataSet;
	}
	
	/**
	 * Processes a single line in an ARFF file.  This method should be called 
	 * on each line of the ARFF file being read.
	 * 
	 * @param arffLine - any line from the ARFF file 
	 */
	private void parseLine(String arffLine)
	{
		if (arffLine.length() < 2)
		{
			return;
		}
		
		if (arffLine.charAt(0) == ARFF_HEADER_CHAR) 
		{
			parseHeaderLine(arffLine);
		}
		else if (arffLine.charAt(0) != ARFF_COMMENT_CHAR) 
		{								
			instanceSet.addInstance( createInstanceFromArffLine(arffLine) );
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
		/*
		 * Tokenize the line
		 */
		String[] tokens = arffLine.split(" ");
		
		/*
		 * If line represents an attribute, process the attribute 
		 */
		if (tokens[0].toLowerCase().equals("@attribute"))
		{
			addAttribute(arffLine);
		}
	}
	
	/**
	 * Helper method for adding an attribute to the attribute set.
	 * This method is called to process a line in a ARFF file with the
	 * first token being "@attribute"
	 * 
	 * @param arffLine - A line from the ARFF file where the first token is 
	 * "@attribute"
	 */
	private void addAttribute(String arffLine)
	{
		String[] tokens = arffLine.split(" ");
		
		String attrName = null;
		Attribute.Type attrType;
		String[] attributeValues = null;
		
		/*
		 * Parse attribute name
		 */
		if (tokens[1].charAt(0) == '"')
		{
		    attrName = tokens[1].split("\"")[1];
		}
		else if (tokens[1].charAt(0) == '\'')
		{
		    attrName = tokens[1].split("'")[1];
		}
		
		/*
		 *  Parse attribute type (continuous or nominal)
		 */
		if (tokens[2].equals("real"))
		{
			attrType =  Attribute.Type.CONTINUOUS;
		}
		else 
		{
			attrType = Attribute.Type.NOMINAL;
		}
	
		/*
		 *  Get nominal values for this attribute if it is nominal
		 */
		if (attrType == Attribute.Type.NOMINAL)
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
		/*
		 *  The pattern finds the string contained in curly braces
		 */
		String pattern = "\\{(.*)\\}";
		
		/*
		 *  Compile regex pattern and match it in our ARFF line
		 */
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher regexMatcher = regexPattern.matcher(arffLine);
		regexMatcher.find();
		
		/*
		 *  Tokenize the string inside the curly braces
		 */
		String[] attributeValues
						= trimAllStrings( regexMatcher.group(1).split(",") );
		
		return attributeValues;
	}
	
	/**
	 * Create an Instance object from a line in the ARFF file that corresponds 
	 * to an instance
	 * 
	 * @param arffLine a line in the ARFF file that corresponds to an instance
	 * @return
	 */
	private Instance createInstanceFromArffLine(String arffLine)
	{		    
		String[] tokens = trimAllStrings( arffLine.split(",") );
		
		Instance newInstance = new Instance();
	
		for (int index=0; index < tokens.length; index++)
		{
			
			/*
			 *  Find attribute at this index
			 */
			Attribute currAttribute = attributeSet.getAttributeById(index);
			/*
			if (tokens[index].equals("?"))
			{
				//TODO: Figure out how to handle missing attributes
			}
			else
			{*/			
				/*
				 *  Parse value of attribute
				 */
				if (currAttribute.getType() == Attribute.Type.CONTINUOUS)
				{
					newInstance.addAttributeInstance(index, Double.parseDouble(tokens[index]));
				}
				else if (currAttribute.getType() == Attribute.Type.NOMINAL)
				{
					Integer nominalValueId 
							= currAttribute.getNominalValueId(tokens[index]);
					
					newInstance.addAttributeInstance(index, nominalValueId.doubleValue());
				}
			//}
		}
		
		return newInstance;
	}
	
	/**
	 * Trim all Strings in an array of Strings of any proceeding and 
	 * preceding white space.
	 * 
	 * @param rawStrings the array of Strings
	 * @return an array of these trimmed Strings
	 */
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
