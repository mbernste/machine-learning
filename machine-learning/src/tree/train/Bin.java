package tree.train;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * This helper class keeps track of which class labels are represented at each
 * value of a specific continuous attribute.  This class is specifically used 
 * for generating all possible splits along a continuous attribute.
 * <br>
 * <br>
 * For example, say we have two instances with attribute A = a. One of these
 * instances has Class = positive, the other instance has Class = negative. 
 * This class stores the knowledge that both class values are represented in
 * the instances whose attribute A = a.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Bin 
{
    /**
     * Ordering of bins based on value they represent
     */
	public static final Comparator<Bin> BIN_ORDER = 
            new Comparator<Bin>() 
            {
				public int compare(Bin b1, Bin b2) 
				{
					return b2.getValue().compareTo(b1.getValue());
				}
            };
	
    /**
     * The nominal value of some target attribute represented by the bin
     */
	private Double value;
	
	/**
	 * A mapping of a nominal value ID of the class attribute to 
	 * a Boolean variable.  This Boolean is true if an instance with the given 
	 * class label is in this bin. 
	 */
	private Map<Integer, Boolean> classExistenceMap;
	
	public Bin(Double value)
	{
		classExistenceMap = new HashMap<Integer, Boolean>();
		this.value = value;
	}
	
	/**
	 * @return the bin's value of the target attribute.
	 */
	public Double getValue()
	{
		return this.value;
	}
	
	/**
	 * @return a mapping of a nominal value ID of the class attribute to 
     * a Boolean variable.  This Boolean is true if an instance with the given 
     * class label is in this bin.
	 */
	public Map<Integer, Boolean> getExistenceMap()
	{
		return classExistenceMap;
	}
	
	/**
	 * @param classLabel a nominal value ID of the class attribute that is 
	 * included in this bin
	 */
	public void includeInstance(Integer classLabel)
	{
		classExistenceMap.put(classLabel, true);
	}
}
