package bayes.cpd;


import data.Attribute;
import data.Instance;
import data.InstanceSet;

/**
 * This class stores all instances for which a specific attribute matches a 
 * specific value. 
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class SplitBranch 
{

    /**
     * The value that an instance's attribute (the attribute determined by this
     * SplitBranch's attribute) is tested against to make this split.
     */
    private Double branchValue; 

    /**
     * The attribute this branch tests
     */
    private Attribute attribute;

    /**
     * All instances that fall to this branch
     */
    private InstanceSet instanceSet;

    /**
     * Constructor
     * 
     * @param attribute the attribute this branch tests
     * @param branchValue the value that an instance's attribute (this 
     * SplitBranch's attribute) is tested against to make this split.
     */
    public SplitBranch(Attribute attribute, Double branchValue)
    {
        this.instanceSet = new InstanceSet();
        this.attribute = attribute;
        this.branchValue = branchValue;
    }

    public InstanceSet getInstanceSet()
    {
        return instanceSet;
    }

    /**
     * Attempt to add an instance to the this split branch.  The instance is 
     * only add if it passes this branches test.
     * 
     * @param instance
     */
    public void tryAddInstance(Instance instance)
    {		
        if (this.doesInstanceMakeSplit(instance))
        {
            instanceSet.addInstance(instance);
        }
    }

    /**
     * @return the attribute this branch tests
     */
    public Attribute getAttribute() 
    { 
        return attribute;
    } 

    /**
     * @return the value that an instance is tested against to make this split
     */
    public Double getValue()
    {
        return branchValue;
    }

    /**
     * Tests whether an instance makes this split branch.  
     * 
     * @param instance the instance we are testing whether or not it makes
     * this SplitBranch
     * @return true if the instance's attribute's value matches this 
     * SplitBranch's value
     */
    public Boolean doesInstanceMakeSplit(Instance instance)
    {		
        Double instanceAttrValue = 
                instance.getAttributeValue(this.attribute);

        return (instanceAttrValue.doubleValue() == branchValue.doubleValue());
    }

}
