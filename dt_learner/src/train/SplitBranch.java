package train;

import tree.DtNode;
import data.attribute.Attribute;
import data.instance.Instance;
import data.instance.InstanceSet;

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
	private Integer relation;
	
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
		
	public SplitBranch(Attribute attribute, Double branchValue, Integer relation)
	{
		this.instanceSet = new InstanceSet();
		this.attribute = attribute;
		this.branchValue = branchValue;
		this.relation = relation;
	}
	
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
	
	public Attribute getAttribute() 
	{ 
		return attribute;
	} 
	
	public Integer getRelation()
	{
		return relation;
	}
	
	public Double getValue()
	{
		return branchValue;
	}
	
	public Boolean doesInstanceMakeSplit(Instance instance)
	{		
		Double instanceAttrValue = instance.getAttributeValue(this.attribute.getId());
				
		switch(this.relation)
		{
		case DtNode.EQUALS:
			return (instanceAttrValue.doubleValue() == branchValue.doubleValue());
		case DtNode.GREATER_THAN:
			return (instanceAttrValue.doubleValue() > branchValue.doubleValue());
		case DtNode.LESS_THEN_EQUAL_TO:
			return (instanceAttrValue.doubleValue() <= branchValue.doubleValue());
		default:
			throw new RuntimeException("Error testing instance in branch.  This branch's relation is not set to a" +
					"valid relation.");
		}
	}

}
