package tree;

import java.util.HashSet;
import java.util.Set;

/**
 * A node in a tree. 
 *
 */
public class Node 
{
    /**
     * This nodes parent in the tree
     */
	protected Node parent = null;
	
	/**
	 * This node's child in the tree
	 */
	protected final Set<Node> children;
		
	/**
	 * Constructor for creating a node
	 * @param nodeId
	 */
	public Node()
	{
		children = new HashSet<Node>();
	}
	
	/**
	 * Add a child Node.
	 * 
	 * @param child
	 */
	public void addChild(Node child)
	{
		child.setParent(this);
		children.add(child);
	}
	
	/**
	 *  Remove a child Node.
	 * 
	 * @param child
	 * @return true if the Node existed as child of the current Node
	 * 		   and was successfully removed
	 */
	public Boolean removeChild(Node child)
	{
		child.setParent(null);
		return  children.remove(child);
	}
	
	/**
	 * @return this node's children nodes
	 */
	public Set<Node> getChildren()
	{
		return children;
	}
	
	/**
	 * Set the parent of the node.
	 * 
	 * @param parent the node's parent
	 */
	public void setParent(Node parent)
	{
		this.parent = parent;
	}
}
