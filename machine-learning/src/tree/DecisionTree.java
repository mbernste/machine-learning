package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pair.Pair;

import data.Attribute;
import data.AttributeSet;
import data.Instance;

/**
 * A generic decision tree model.
 * 
 */
public class DecisionTree 
{   
    /**
     * The root node of the tree
     */
	private final DtNode root;
	
	/**
	 * The set of attributes used in the learning of this tree
	 */
	private AttributeSet attributeSet;
	
	/**
	 * The class attribute that the tree predicts
	 */
	private Attribute classAttribute;
	
	/**
	 * Constructor
	 * 	
	 * @param attributeSet set of attributes used in learning
	 * @param classAttribute the class attribute this tree predicts
	 */
	public DecisionTree(DtNode root, Attribute classAttribute)
	{
		this.root = root;
		this.classAttribute = classAttribute;
	}
	
	/**
	 * @return the tree's root node
	 */
	public Node getRoot()
	{
		return root;
	}
	
	@Override
	public String toString()
	{
	    String result = "";
	    
		@SuppressWarnings("unchecked")
		Set<DtNode> children = ((Set<DtNode>) ((Set<?>) root.getChildren()));
		List<DtNode> childrenList = new ArrayList<DtNode>(children);
		Collections.sort(childrenList, DtNode.DTNODE_ORDER);
		
		for (Node child : childrenList)
		{
			StringGenerator strGen = new StringGenerator(this.attributeSet, 
			                                             this.classAttribute);
			result +=  strGen.recurseStringGenerate( (DtNode) child, 0);
		}
		
		return result;
	}
	
	/**
	 * A private helper class used for traversing the tree recursively
	 * in order to print the print the tree to standard output
	 * 
	 */
	private static class StringGenerator
	{
		@SuppressWarnings("unused")
		private AttributeSet attributeSet;
		private Attribute classAttribute;
		
		public StringGenerator(AttributeSet attributeSet, 
		                           Attribute classAttribute)
		{
			this.attributeSet = attributeSet;
			this.classAttribute = classAttribute;
		}
		
		public String recurseStringGenerate(DtNode node, Integer depth)
		{
		    String result = "";
		    
			// Print the indentated "|" characters
			for (int i = 0; i < depth; i++)
			{
			    result += "|     ";
			}
			
			// Print the value at the current node
			result += node;
			
			if (node instanceof DtLeaf)
			{
			    result += ": ";
				String classLabel = classAttribute.getNominalValueName(((DtLeaf) node).getClassLabel());
				result += classLabel;
			}
			
			result += "\n";
			
			@SuppressWarnings("unchecked")
			Set<DtNode> children = ((Set<DtNode>) ((Set<?>) node.getChildren()));
			List<DtNode> childrenList = new ArrayList<DtNode>(children);
			Collections.sort(childrenList, DtNode.DTNODE_ORDER);
			
			for (Node child : childrenList)
			{
				DtNode dtChild = (DtNode) child;
				result += recurseStringGenerate (dtChild, depth+1);
			}
			
			return result;
		}
	}
	
	/**
	 * Classify an instance using this decision tree.
	 * 
	 * @param instance the instance to be classified
	 * @return the nominal value ID of the class attribute predicted for the
	 * given instance and a confidence value for that prediction
	 */
	public Pair<Integer, Double> classifyInstance(Instance instance)
	{
	    DtNode currNode = (DtNode) this.getRoot();
        	    
        while (!(currNode instanceof DtLeaf))
        {
            @SuppressWarnings("unchecked")
            Set<DtNode> children = ((Set<DtNode>) ((Set<?>) currNode.getChildren()));
        
            for (DtNode node : children)
            {
                if (node.doesInstanceSatisfyNode(instance))
                {
                    currNode = node;
                    break;
                }
            }
        }

        DtLeaf leaf = (DtLeaf) currNode;
        
        Integer prediction = leaf.getClassLabel().intValue();
        
        /*
         *  Get total number of instances that reached the leaf node 
         */
        Integer totalCount = 0;
        for (Integer count : leaf.getClassCounts().values())
        {
            totalCount += count;
        }
        
        /*
         * Calculate the confidence
         */
        Double confidence = (double) (leaf.getClassCounts().get(prediction)) / totalCount;
      
        return  new Pair<Integer, Double>(prediction, confidence);        
	}
}
