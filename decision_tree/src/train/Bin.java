package train;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Bin 
{
	public static final Comparator<Bin> BIN_ORDER = 
            new Comparator<Bin>() 
            {
				public int compare(Bin b1, Bin b2) 
				{
					return b2.getValue().compareTo(b1.getValue());
				}
            };
	
	private Double value;
	
	/**
	 * The classExistenceMap maps the nominal value ID of the class attribute to a Boolean variable.
	 * This Boolean is true if an instance with the given class label is in this bin. 
	 */
	private Map<Integer, Boolean> classExistenceMap;
	
	public Bin(Double value)
	{
		classExistenceMap = new HashMap<Integer, Boolean>();
		this.value = value;
	}
	
	public Double getValue()
	{
		return this.value;
	}
	
	public Map<Integer, Boolean> getExistenceMap()
	{
		return classExistenceMap;
	}
	
	public void includeInstance(Integer classLabel)
	{
		classExistenceMap.put(classLabel, true);
	}
}
