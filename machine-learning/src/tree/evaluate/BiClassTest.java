package tree.evaluate;

import java.util.Set;

import tree.DecisionTree;
import tree.DtNode;
import tree.DtLeaf;
import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;

public class BiClassTest 
{	
	public static BiClassTestResults runTest(DataSet data, DecisionTree dt)
	{
		BiClassTestResults results = new BiClassTestResults();
		
		for (Instance instance : data.getInstanceSet().getInstanceList())
		{
			DtNode currNode = (DtNode) dt.getRoot();
			
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

			Attribute classAttr = data.getClassAttribute();
			
			DtLeaf leaf = (DtLeaf) currNode;
			
			String prediction = data.getClassAttribute().getNominalValueName(
					leaf.getClassLabel().intValue() );
			String truth = data.getClassAttribute().getNominalValueName(
					instance.getAttributeValue(classAttr).intValue() );
			
			// Print result of classification
			System.out.print(prediction);
			System.out.print(" ");
			System.out.print(truth);
			System.out.print("\n");
			
			// Add the prediction to the test results
			results.addClassification(leaf.getClassLabel().intValue(),
									  instance.getAttributeValue(classAttr).intValue());
		}
		
		System.out.println("\n");
		
		return results;
	}
}
