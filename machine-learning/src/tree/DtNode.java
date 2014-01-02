package tree;

import java.util.Comparator;
import java.util.Map;

import data.attribute.Attribute;
import data.instance.Instance;

/**
 * A node in a decision tree.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class DtNode extends Node {
	    
    /**
     * How an attribute is tested against a value at a decision tree node
     */
    public static enum  Relation {EQUALS, LESS_THEN_EQUAL_TO, GREATER_THAN};
	
    /**
     * Compares two decision tree nodes
     */
	public static final Comparator<DtNode> DTNODE_ORDER = 
            new Comparator<DtNode>() 
            {
				public int compare(DtNode n1, DtNode n2) 
				{
					if (n1.attribute == n2.attribute)
					{
						Attribute attr = n1.attribute;
						if (attr.getType() == Attribute.NOMINAL)
						{
							return n1.nodeValue.compareTo(n2.nodeValue);
						}
						else if (attr.getType() == Attribute.CONTINUOUS)
						{
							return n1.relation.compareTo(n2.relation);
						}
					}
					
					return 0;
				}
            };

    /**
     * How this attribute is tested against this value at this decision tree 
     * node
     */
	private Relation relation;
	
	/**
	 * The lower bound of values that an instance must meet 
	 * to make satisfy this node's test
	 */
	private Double nodeValue; 
	
	/**
	 * Counts the number of instances that finally reached this node.
	 * The map maps a nominal value ID of the class label to a count
	 */
	private Map<Integer, Integer> classCounts;
	
	/**
	 * The attribute this node tests
	 */
	private Attribute attribute;
	
	public DtNode(Attribute attribute, 
				  Double nodeValue, 
				  Relation relation)
	{
		this.attribute = attribute;
		this.nodeValue = nodeValue;
		this.relation = relation;
	}
	
	/**
	 * @return the attribute tested at this decision tree node
	 */
	public Attribute getAttribute() 
	{ 
		return attribute;
	} 
	
	/**
	 * @return the value the attribute at this decision tree node is tested
	 * against
	 */
	public Double getNodevalue()
	{
		return this.nodeValue;
	}
	
	@Override
	public String toString()
	{
		String nodeStr = "";
		
		nodeStr += attribute.getName();
		nodeStr += " ";
		nodeStr += getRelationString();
		nodeStr += " ";
		
		if (this.attribute.getType() == Attribute.NOMINAL)
		{
			nodeStr += attribute.getNominalValueName(nodeValue.intValue());
		}
		else if (this.attribute.getType() == Attribute.CONTINUOUS)
		{
			nodeStr += nodeValue;
		}
		
		if (this.classCounts != null)
		{
			nodeStr += " [";
			for (int nominalValueId = 0; nominalValueId < classCounts.size(); nominalValueId++)
			{
				//System.out.println(classLabelCounts.get(nominalValueId));
				nodeStr += classCounts.get(nominalValueId).toString();
				nodeStr += ", ";
			}
			nodeStr = nodeStr.substring(0, nodeStr.length() - 2 );
			nodeStr += "]"; 
		}	
		return nodeStr;
	}
	
	/**
	 * @return "<", "=", or "<=" based on the relation to the value tested
	 * at this decision tree node
	 */
	private String getRelationString()
	{
		String relationStr = "";
		
		switch(this.relation)
		{
		case EQUALS:
			relationStr = "=";
			break;
		case GREATER_THAN:
			relationStr = ">";
			break;
		case LESS_THEN_EQUAL_TO:
			relationStr = "<=";
			break;
		}
		
		return relationStr; 
	}
	
	public void setClassCounts(Map<Integer, Integer> classLabelCounts)
	{
		this.classCounts = classLabelCounts;
	}
	
	public Map<Integer, Integer> getClassCounts()
	{
	    return this.classCounts;
	}
	
	public Boolean doesInstanceSatisfyNode(Instance instance)
	{
		Double instanceAttrValue = instance.getAttributeValue(attribute);
		
		Boolean result = null;
		
		switch(this.relation)
		{
		case EQUALS:
			result = instanceAttrValue.doubleValue() == nodeValue.doubleValue();
			break;
		case GREATER_THAN:
			result = instanceAttrValue.doubleValue() > nodeValue.doubleValue();
			break;
		case LESS_THEN_EQUAL_TO:
			result = instanceAttrValue.doubleValue() <= nodeValue.doubleValue();
			break;
		}
		
		return result;	
	}

}
