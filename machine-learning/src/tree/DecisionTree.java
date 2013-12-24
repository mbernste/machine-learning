package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.attribute.Attribute;
import data.attribute.AttributeSet;

/**
 * Encapsulates a learned decision tree.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class DecisionTree 
{
    /**
     * The root node of the tree
     */
	private DtNode root = null;
	
	/**
	 * A mapping of node ID to node objects 
	 */
	private Map<Integer, DtNode> nodeSet;
	
	/**
	 * The set of attributes used in the learning of the tree
	 */
	private AttributeSet attributeSet;
	
	/**
	 * The class attribute that the tree predicts
	 */
	private Attribute classAttribute;
	
	/**
	 * Used for assinging unique node Id's to new nodes
	 */
	private int nodeCount = 0;
	
	/**
	 * Constructor
	 * 	
	 * @param attributeSet set of attributes used in learning
	 * @param classAttribute the class attribute this tree predicts
	 */
	public DecisionTree(AttributeSet attributeSet, Attribute classAttribute)
	{
		nodeSet = new HashMap<Integer, DtNode>();
		this.classAttribute = classAttribute;
	}
	
	/**
	 * Add a node to the tree
	 * @param newNode
	 */
	protected void addNode(DtNode newNode)
	{
		newNode.setNodeId(nodeCount);
		nodeSet.put(nodeCount, newNode);
		nodeCount++;
	}
	
	/**
	 * @return the tree's root node
	 */
	public Node getRoot()
	{
		return root;
	}
	
	/**
	 * Get a node with specific ID
	 * 
	 * @param nodeId the node's ID
	 * @return the node
	 */
	public Node getNode(Integer nodeId)
	{
		return nodeSet.get(nodeId);
	}
	
	/**
	 * Set the tree's root node
	 * 
	 * @param rootNode the root node
	 */
	protected void setRoot(DtNode rootNode)
	{
		root = rootNode;
		root.setNodeId(nodeCount);
		nodeSet.put(nodeCount, rootNode);
		nodeCount++;
	}

	/**
	 * Print the subtree rooted at the input Node to standard output.
	 * 
	 * @param root the node that roots the subtree being printed
	 */
	public void printTree(Node root)
	{
		@SuppressWarnings("unchecked")
		Set<DtNode> children = ((Set<DtNode>) ((Set<?>) root.getChildren()));
		List<DtNode> childrenList = new ArrayList<DtNode>(children);
		Collections.sort(childrenList, DtNode.DTNODE_ORDER);
		
		for (Node child : childrenList)
		{
			TreePrinter treePrinter = new TreePrinter(this.attributeSet, this.classAttribute);
			treePrinter.print( (DtNode) child, 0);
		}
		
	}
	
	/**
	 * A private helper class used for traversing the tree recursively
	 * in order to print the print the tree to standard output
	 * 
	 * @author matthewbernstein
	 */
	private static class TreePrinter
	{
		@SuppressWarnings("unused")
		private AttributeSet attributeSet;
		private Attribute classAttribute;
		
		public TreePrinter(AttributeSet attributeSet, Attribute classAttribute)
		{
			this.attributeSet = attributeSet;
			this.classAttribute = classAttribute;
		}
		
		public void print(DtNode node, Integer depth)
		{
			// Print the indentated "|" characters
			for (int i = 0; i < depth; i++)
			{
				System.out.print("|     ");
			}
			
			// Print the value at the current node
			System.out.print(node);
			
			
			if (node instanceof DtLeaf)
			{
				System.out.print(": ");
				String classLabel = classAttribute.getNominalValueName(((DtLeaf) node).getClassLabel());
				System.out.print( classLabel );
			}
			
			System.out.print("\n");
			
			@SuppressWarnings("unchecked")
			Set<DtNode> children = ((Set<DtNode>) ((Set<?>) node.getChildren()));
			List<DtNode> childrenList = new ArrayList<DtNode>(children);
			Collections.sort(childrenList, DtNode.DTNODE_ORDER);
			
			for (Node child : childrenList)
			{
				DtNode dtChild = (DtNode) child;
				print(dtChild, depth+1);
			}			
		}
	}
}
