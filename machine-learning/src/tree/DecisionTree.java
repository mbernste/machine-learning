package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.attribute.Attribute;
import data.attribute.AttributeSet;

public class DecisionTree 
{
	private DtNode root = null;
	private Map<Integer, DtNode> nodeSet;
	private AttributeSet attributeSet;
	private Attribute classAttribute;
	
	private int nodeCount = 0;			// Used for assigning unique node Id's to new nodes
		
	public DecisionTree(AttributeSet attributeSet, Attribute classAttribute)
	{
		nodeSet = new HashMap<Integer, DtNode>();
		this.classAttribute = classAttribute;
	}
	
	public void addNode(DtNode newNode)
	{
		newNode.setNodeId(nodeCount);
		nodeSet.put(nodeCount, newNode);
		nodeCount++;
	}
	
	public Node getRoot()
	{
		return root;
	}
	
	public Node getNode(Integer nodeId)
	{
		return nodeSet.get(nodeId);
	}
	
	public void setRoot(DtNode rootNode)
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
			System.out.print(node.getNodeString());
			
			
			if (node instanceof Leaf)
			{
				System.out.print(": ");
				String classLabel = classAttribute.getNominalValueName(((Leaf) node).getClassLabel());
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
