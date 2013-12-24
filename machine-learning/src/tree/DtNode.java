package tree;

import java.util.Comparator;
import java.util.Map;

import data.attribute.Attribute;
import data.instance.Instance;

public class DtNode extends Node {
	    
	public final static int EQUALS = 0;
	public final static int LESS_THEN_EQUAL_TO = 1;
	public final static int GREATER_THAN = 2;
	
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
	 * This describes the relation to node value that an instance
	 * must meet to satisfy this node's test
	 * 
	 * EQUALS 				 : instance value == branch value
	 * GREATER_THAN			 : instance value >  branch value
	 * GREATER_THAN_EQUAL_TO : instance value >= branch value
	 */
	private Integer relation;
	
	
	/**
	 * The lower bound of values that an instance must meet 
	 * to make satisfy this node's test
	 */
	private Double nodeValue; 
	
	/**
	 * Counts the number of instances that finally reached this node.
	 * The map maps a nominal value (class label) ID to a count
	 */
	private Map<Integer, Integer> classCounts;
	
	/**
	 * The attribute this node tests
	 */
	private Attribute attribute;
	
	public DtNode(Attribute attribute, 
				  Double nodeValue, 
				  Integer relation)
	{
		this.attribute = attribute;
		this.nodeValue = nodeValue;
		this.relation = relation;
	}
	
	public Attribute getAttribute() 
	{ 
		return attribute;
	} 
	
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
	
	private String getRelationString()
	{
		String relationStr = "";
		
		switch(this.relation)
		{
		case DtNode.EQUALS:
			relationStr = "=";
			break;
		case DtNode.GREATER_THAN:
			relationStr = ">";
			break;
		case DtNode.LESS_THEN_EQUAL_TO:
			relationStr = "<=";
			break;
		}
		
		return relationStr; 
	}
	
	public void setClassCounts(Map<Integer, Integer> classLabelCounts)
	{
		this.classCounts = classLabelCounts;
	}
	
	public Boolean doesInstanceSatisfyNode(Instance instance)
	{
		Double instanceAttrValue = instance.getAttributeValue(attribute);
		
		Boolean result = null;
		
		switch(this.relation)
		{
		case DtNode.EQUALS:
			result = instanceAttrValue.doubleValue() == nodeValue.doubleValue();
			break;
		case DtNode.GREATER_THAN:
			result = instanceAttrValue.doubleValue() > nodeValue.doubleValue();
			break;
		case DtNode.LESS_THEN_EQUAL_TO:
			result = instanceAttrValue.doubleValue() <= nodeValue.doubleValue();
			break;
		}
		
		return result;	
	}

}
