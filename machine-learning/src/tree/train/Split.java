package tree.train;

import java.util.ArrayList;

import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;
import data.instance.InstanceSet;

/**
 * This class splits a set of instances along an attribute.  It stores the
 * separated instances sorted by the value of this split's attribute.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Split 
{
	/**
	 * All of the branches for this split. For splits on nominal attributes,
	 * there will be one branch per nominal value.  For continuous attributes,
	 * there will be two branches.  One branch for the all instances with a 
	 * value greater than the threshold and one branch for all instances less 
	 * than or equal to the threshold value.
	 */
	private ArrayList<SplitBranch> branches;
	
	/**
	 * The attribute this Split splits instances on
	 */
	private Attribute attribute;
	
	/**
	 *	The information gain on this split
	 */
	private Double infoGain;
	
	/**
	 * Constructor
	 * 
	 * @param attribute the attribute along which this split splits
	 */
	public Split(Attribute attribute)
	{
		branches = new ArrayList<SplitBranch>();
		this.attribute = attribute;
	}
	
	/**
	 * @return this split's attribute
	 */
	public Attribute getAttribute()
	{
		return attribute;
	}
	
	/**
	 * @return the information gain along this split for the dataset's
	 * class attribute.
	 */
	public Double getInfoGain()
	{
		return infoGain;
	}
	
	// TODO: REFACTOR THIS!
	@Deprecated
	public void setInfoGain(Double infoGain)
	{
		this.infoGain = infoGain;
	}
	
	/**
	 * Split a set of instances along this split.
	 * 
	 * @param data the dataset containing the instances to be split
	 */
	public void splitInstances(DataSet data)
    {
	    InstanceSet instances = data.getInstanceSet();
	    
        for (Instance instance : instances.getInstanceList())
        {
            for (SplitBranch branch : this.branches)
            {
                branch.tryAddInstance(instance);
            }
        }
        
       this.infoGain = Entropy.informationGain(data, this);
    }
	
	/**
	 * @return each branch along this split
	 */
	public ArrayList<SplitBranch> getSplitBranches()
	{
		return branches;
	}
	
	/**
	 * Add a branch to the split
	 * 
	 * @param newBranch the new branch
	 */
	protected void addBranch(SplitBranch newBranch)
	{
		branches.add(newBranch);
	}
}
