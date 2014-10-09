package data;

import java.util.ArrayList;
import java.util.List;

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
	private final List<Instance> instances;
	
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
	public List<Instance> getInstances()
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
	 * @param id the unique ID of a specific instance
	 * @return the instance with specified ID
	 */
	public Instance getInstanceById(int id)
	{
		return instances.get(id);
	}
	
	public String toString(){
		return instances.toString();
	}
}
