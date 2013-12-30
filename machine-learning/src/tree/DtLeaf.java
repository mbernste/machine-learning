package tree;

import data.attribute.Attribute;

/**
 * The leaf of a decision tree.
 * 
 * @author Matthew Bernstein
 *
 */
public class DtLeaf extends DtNode 
{
	/**
	 * This integer must be a valid nominal value of the class attribute for the
	 * current learning problem
	 */
	private Integer classLabel;
	
	public DtLeaf(Attribute attribute,
				Double value,
				Relation relation, 
				Integer classLabel)
	{
		super(attribute, value, relation);
		this.classLabel = classLabel;
	}
	
	@Override
	public void addChild(Node child)
	{
		throw new UnsupportedOperationException("Error. Attempting to add" +
				" a child Node to Leaf with ID of " + super.nodeId);
	}
	
	public Integer getClassLabel()
	{
		return classLabel;
	}
}
