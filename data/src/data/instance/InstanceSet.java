package data.instance;

import java.util.ArrayList;

/**
 * This class represents a set of {@code Instance} objects.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class InstanceSet 
{
	/**
	 * All instances in this instance set
	 */
	ArrayList<Instance> instances;
	
	/**
	 * Constructor
	 */
	public InstanceSet()
	{
		instances = new ArrayList<Instance>();
	}
	
	/**
	 * @return a list of all instances in this instance set
	 */
	public ArrayList<Instance> getInstanceList()
	{
		return instances;
	}
	
	/**
	 * Add an instance to this instance set
	 * 
	 * @param newInstance the new instance
	 */
	public void addInstance(Instance newInstance)
	{
		instances.add(newInstance);
	}
	
	/**
	 * Get an instance at a specific index of the internal list storing all
	 * instances
	 * 
	 * @param index the index of the instance to be retrieved
	 * @return the instance at the target instance
	 */
	public Instance getInstanceAt(int index)
	{
		return instances.get(index);
	}
	
	/**
	 * @return the number of instances in the instance set
	 */
	public int getNumInstances()
	{
		return instances.size();
	}
	
}
