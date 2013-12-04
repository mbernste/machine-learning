package data.arff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;

public class ArffGenerator 
{
	public void generateFile(String newArff, Path originalArff, DataSet data)
	{
		Charset charset = Charset.forName("US-ASCII");
		
		
		try (BufferedReader reader = Files.newBufferedReader(originalArff, charset)) 
		{
			FileWriter fw = new FileWriter(new File(newArff));
			BufferedWriter writer = new BufferedWriter(fw);

			// Write header lines to new file
		    String line = null;
		    while ((line = reader.readLine()) != null) 
		    {
	    		if (line.charAt(0) == '@')
	    		{	    			
	    			writer.write(line);
	    			writer.write("\n");
	    		}
		    }
		    
		    // Write instances to new file
		    for (Instance instance : data.getInstanceSet().getInstanceList())
		    {
		    	String instanceStr = "";
		    	
		    	for (Attribute attr : data.getAttributeSet().getAttributes())
		    	{		    				    		
		    		if (attr.getType() == Attribute.NOMINAL)
		    		{
		    			instanceStr += attr.getNominalValueName(
		    									instance.getAttributeValue(attr.getId()).intValue() );
		    		}
		    		else if (attr.getType() == Attribute.CONTINUOUS)
		    		{
		    			instanceStr += instance.getAttributeValue(attr.getId()).toString();
		    		}
		    		
		    		instanceStr += ",";
		    	}
		    	
		    	instanceStr = instanceStr.substring(0, instanceStr.length() - 1);
		    	instanceStr += "\n";
		    	
		    	writer.write(instanceStr);
		    	
		    }
		    
		    writer.flush();
		    writer.close();
		} 
		catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		}
	}
	
}
