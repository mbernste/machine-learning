package bayes_network.builders;

import java.util.ArrayList;

import bayes_network.BayesianNetwork;
import bayes_network.BNNode;
import bayes_network.cpd.CPDTree;
import bayes_network.cpd.CPDTreeBuilder;
import data.DataSet;
import data.attribute.Attribute;

public class NetworkBuilder 
{
	private Integer laplaceCount;
	
	public BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount)
	{
		this.laplaceCount = laplaceCount;
		
		BayesianNetwork net = new BayesianNetwork();
		
		for (Attribute attr : data.getAttributeList())
		{
			if (attr.getType() == Attribute.NOMINAL)
			{
				BNNode newNode = new BNNode(attr);
				net.addNode( newNode );
			}
		}
		
		return net;
	}
	
	public void buildCPD(BayesianNetwork net, DataSet data)
	{
		for (BNNode node : net.getNodes())
		{
			ArrayList<Attribute> cpdAttributes = new ArrayList<Attribute>();
			
			for (BNNode parent : node.getParents())
			{
				cpdAttributes.add(parent.getAttribute());
			}
			
			// Add the current node's attribute
			cpdAttributes.add(node.getAttribute());
						
			// Build the CPD at this node
			CPDTreeBuilder treeBuilder = new CPDTreeBuilder();
			CPDTree cpdTree = treeBuilder.buildCPDTree(data, 
													   cpdAttributes,
													   this.laplaceCount);
			
			// Set the CPD Tree
			node.setCPDTree( cpdTree );
		}
		
		
	}
}
