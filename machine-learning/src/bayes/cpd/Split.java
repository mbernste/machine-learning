package bayes.cpd;

import java.util.ArrayList;


import data.Attribute;
import data.DataSet;
import data.Instance;
import data.InstanceSet;

/**
 * Splits all instances along a specific attribute.
 * 
 * @author Matthew Bernstien - matthewb@cs.wisc.edu
 *
 */
public class Split 
{
    /**
     * All of the branches for this split. For splits on nominal attributes,
     * there will be one branch per nominal value.  For continuous attributes,
     * there will be two branches.  One branch for the all instances with a value
     * greater than the threshold and one branch for all instances less than or 
     * equal to the threshold value.
     */
    private ArrayList<SplitBranch> branches;

    /**
     * The attribute this Split splits instances on
     */
    private Attribute attribute;

    public Split(Attribute attribute)
    {
        branches = new ArrayList<SplitBranch>();
        this.attribute = attribute;
    }

    public Attribute getAttribute()
    {
        return attribute;
    }

    /**
     * Split a set of instances along this split's attribute.  Each split is 
     * stored in one of this Split's SplitBranch objects
     * 
     * @param instances the set of instances we wish to split
     */
    public void splitInstances(InstanceSet instances)
    {
        for (Instance instance : instances.getInstances())
        {
            for (SplitBranch branch : this.branches)
            {
                branch.tryAddInstance(instance);
            }
        }
    }

    /**
     * @return all of this split's branches
     */
    public ArrayList<SplitBranch> getSplitBranches()
    {
        return branches;
    }

    /**
     * Add a branch to the split
     * 
     * @param newBranch the new branch
     */
    public void addBranch(SplitBranch newBranch)
    {
        branches.add(newBranch);
    }

    /**
     * A helper method for generating a split along a nominal attribute
     * 
     * @param attrId
     * @return
     */
    public static Split createSplitNominal(Attribute attr, DataSet data)
    {					
        Split split = new Split(attr);

        for (Integer nominalValueId : attr.getNominalValueMap().values())
        {
            SplitBranch newBranch = new SplitBranch(attr, 
                                                    new Double(nominalValueId));
            split.addBranch(newBranch);
        }

        split.splitInstances(data.getInstanceSet());

        return split;
    }

}
