package tree.train;

import tree.DtNode;
import data.Attribute;
import data.Instance;
import data.InstanceSet;

/**
 * This class represents a single branch along a split.  If a split splits 
 * instances along attribute A where A can take values {a1, a2, a3}, this split
 * will store 3 split branches for storing instances whose value of A is a1, a2, 
 * and a3. 
 */
public class SplitBranch 
{
	/**
	 * This describes the relation to the {@branchValue} that an instance is 
	 * tested against. Possible relations are defined in {@code DtNode}:
	 * 
	 * EQUALS 				 : instance value == branch value
	 * GREATER_THAN			 : instance value >  branch value
	 * GREATER_THAN_EQUAL_TO : instance value >= branch value
	 */
	private DtNode.Relation relation;
	
	/**
	 * The value that an instance is tested against to make this split
	 */
	private Double branchValue; 
	
	/**
	 * The attribute this branch tests
	 */
	private Attribute attribute;
	
	/**
	 * All instances that fall to this branch
	 */
	private InstanceSet instanceSet;
		
	/**
	 * Constructor 
	 * 
	 * @param attribute the attribute along which this branch's split splits
	 * instances
	 * @param branchValue the value of the attribute that this branch tests
	 * @param relation the relation to the attribute an instance must be to
	 * make this branch
	 */
	protected SplitBranch(Attribute attribute, 
	                      Double branchValue, 
	                      DtNode.Relation relation)
	{
		this.instanceSet = new InstanceSet();
		this.attribute = attribute;
		this.branchValue = branchValue;
		this.relation = relation;
	}
	
	/**
	 * @return the set of instances that have made this branch
	 */
	public InstanceSet getInstanceSet()
	{
		return instanceSet;
	}
	
	/**
	 * Attempt to add an instance to the this split branch.  The instance is only
	 * add if it passes this branches test.
	 * 
	 * @param instance
	 */
	public void tryAddInstance(Instance instance)
	{		
		if (this.doesInstanceMakeSplit(instance))
		{
			instanceSet.addInstance(instance);
		}
	}
	
	/**
	 * @return the split's attribute
	 */
	public Attribute getAttribute() 
	{ 
		return attribute;
	}
	
	/**
	 * @return the relation to the attribute for which all instances in this
	 * split branch fall
	 */
	public DtNode.Relation getRelation()
	{
		return relation;
	}
	
	/**
	 * @return the value of the split branch
	 */
	public Double getValue()
	{
		return branchValue;
	}
	
	/**
	 * Tests whether an instance makes this split branch
	 * 
	 * @param instance the instance we are testing
	 * @return true if the instance makes the split branch. False otherwise.
	 */
	public Boolean doesInstanceMakeSplit(Instance instance)
	{		
		Double instanceAttrValue = instance.getAttributeValue(this.attribute);
				
		switch(this.relation)
		{
		case EQUALS:
			return (instanceAttrValue.doubleValue() == branchValue.doubleValue());
		case GREATER_THAN:
			return (instanceAttrValue.doubleValue() > branchValue.doubleValue());
		case LESS_THAN_EQUAL_TO:
			return (instanceAttrValue.doubleValue() <= branchValue.doubleValue());
		default:
			throw new RuntimeException("Error testing instance in branch.  " +
					"This branch's relation is not set to a valid relation.");
		}
	}
}
