package tree;

import java.util.HashSet;
import java.util.Set;

public class Node 
{
	protected Node parent = null;
	protected Set<Node> children;
	protected Integer nodeId;
		
	/**
	 * Constructor for creating a node
	 * @param nodeId
	 */
	public Node()
	{
		children = new HashSet<Node>();
	}
	
	public Integer getId()
	{
		return this.nodeId;
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
	

	public Set<Node> getChildren()
	{
		return children;
	}
	
	/**
	 * Set the parent of the Node.
	 * 
	 * @param parent the node's parent
	 */
	public void setParent(Node parent)
	{
		this.parent = parent;
	}
	
	public String getNodeString()
	{
		return this.nodeId.toString();
	}
	
	/**
	 * Set the node's ID.
	 * 
	 * @param id the node ID
	 */
	protected void setNodeId(Integer id)
	{
		this.nodeId = id;
	}
}
