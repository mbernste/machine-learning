package data.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import data.Attribute;
import data.AttributeSet;
import data.DataSet;
import data.Instance;
import data.InstanceSet;
import data.Attribute.Type;


/**
 * Reads an Attribute-Relation File Format (ARFF) file and extracts the
 * attributes and instances in the file.
 * 
 */
public class ArffReader 
{
    private final static char ARFF_HEADER_CHAR = '@';
    private final static char ARFF_COMMENT_CHAR = '%';
    private final static String REAL_VALUED_ATTRIBUTE = "real";
    
	/**
	 * Read and extract a data set from an ARFF file
	 * 
	 * @param file a path to the ARFF file
	 * @return a data set storing all data in the file
	 */
	public static DataSet readFile(String file) 
	{
	    InstanceSet instanceSet = new InstanceSet();
	    ImmutableList.Builder<Attribute> attributeListBuilder = new ImmutableList.Builder<>();	
		
		try
		{
			Scanner scan = new Scanner(new FileInputStream(file));
		    while (scan.hasNextLine()) 
		    {
	    		parseLine(scan.nextLine(), instanceSet, attributeListBuilder);
		    }    
		    scan.close();	    
		} 
		catch (FileNotFoundException x) 
		{
		    System.err.format("FileNotFountException: %s%n", x);
		}
		
		return new DataSet(new AttributeSet(attributeListBuilder.build()), instanceSet);		
	}
	
	/**
	 * Processes a single line in an ARFF file.  This method should be called 
	 * on each line of the ARFF file being read.
	 * 
	 * @param arffLine - any line from the ARFF file 
	 */
	private static void parseLine(String arffLine, InstanceSet instanceSet, ImmutableList.Builder<Attribute> attrListBuilder)
	{
		if (arffLine.length() < 2)
		{
			return;
		}
		
		if (arffLine.charAt(0) == ARFF_HEADER_CHAR) 
		{
			parseHeaderLine(arffLine, attrListBuilder);
		}
		else if (arffLine.charAt(0) != ARFF_COMMENT_CHAR) 
		{			
		    Instance newInstance = createInstanceFromArffLine(arffLine, attrListBuilder);
			instanceSet.addInstance(newInstance);
		}
	}
	
	/**
	 * This function is called on all lines that begin with the '@' symbol
	 * and is used for parsing all header lines.
	 * 
	 */
	private static void parseHeaderLine(String arffLine, ImmutableList.Builder<Attribute> attrListBuilder)
	{
		String[] tokens = arffLine.split(" ");
		
		if (tokens[0].toLowerCase().equals("@attribute"))
		{
			Attribute newAttr = parseAttribute(arffLine);
			attrListBuilder.add(newAttr);
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
	private static Attribute parseAttribute(String arffLine)
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
		if (tokens[2].equals(REAL_VALUED_ATTRIBUTE))
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
				
		return new Attribute(attrName, attrType, attributeValues);
	}
	
	/**
	 * A helper method that returns an array of strings where each string
	 * is a value of a nominal attribute in a given "@attribute" line in 
	 * the ARFF file
	 * 
	 * @param arffline - A line in the ARFF file that begins with "@attribute"
	 * @return The values of the nominal attributes in this line
	 */
	private static String[] getNominalAttributeValues(String arffLine)
	{
		/*
		 *  REGEX find string in curly braces
		 */
		String pattern = "\\{(.*)\\}";
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher regexMatcher = regexPattern.matcher(arffLine);
		regexMatcher.find();
		
		return trimAllStrings( regexMatcher.group(1).split(",") );
	}
	
	/**
	 * Create an Instance object from a line in the ARFF file that corresponds 
	 * to an instance
	 * 
	 * @param arffLine a line in the ARFF file that corresponds to an instance
	 * @return
	 */
	private static Instance createInstanceFromArffLine(String arffLine, ImmutableList.Builder<Attribute> attrListBuilder)
	{		    
		String[] tokens = trimAllStrings( arffLine.split(",") );
		Instance newInstance = new Instance();
		
		for (int index = 0; index < tokens.length; index++)
		{
			/*
			 *  Find attribute at this index
			 */
			Attribute currAttribute = attrListBuilder.build().get(index);
						
			if (tokens[index].equals("?")) // Missing value
			{
				//TODO: Figure out how to handle missing values
			}
			else
			{			
				if (currAttribute.getType() == Attribute.Type.CONTINUOUS)
				{
					newInstance.addAttributeValue(currAttribute, Double.parseDouble(tokens[index]));
				}
				else if (currAttribute.getType() == Attribute.Type.NOMINAL)
				{
					Integer nominalValueId 
							= currAttribute.getNominalValueId(tokens[index]);
					newInstance.addAttributeValue(currAttribute, nominalValueId.doubleValue());
				}
			}			
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
	private static String[] trimAllStrings(String[] rawStrings)
	{
		String[] trimmedStrings = new String[rawStrings.length];
		for (int i=0; i<rawStrings.length; i++)
		{
			trimmedStrings[i] = rawStrings[i].trim();
		}
		return trimmedStrings;	
	}
}
