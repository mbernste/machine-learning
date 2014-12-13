package bayes;

import java.util.ArrayList;

import pair.Pair;
import data.Attribute;

/**
 * This class represents represents a set of attribute/value pairs.  This is 
 * a component of Bayes net query objects.
 * 
 * @author matthewbernstein
 */
public class VariableSet 
{
    /**
     * Each pair of integers represents an Attribute ID and the nominal value ID
     * of the attribute of all of the variables in this set.
     */
    private ArrayList<Pair<Attribute, Integer>> variables;

    /**
     * Constructor
     */
    public VariableSet()
    {
        this.variables = new ArrayList<Pair<Attribute, Integer>>();
    }

    /**
     * Add an attribute/value pair to the set of attribute/value pairs
     * to this set.
     * 
     * @param attr the ID of the condition attribute to be added to the set 
     * @param nominalValueId the nominal value ID specified for this Attribute
     */
    public void addVariable(Attribute attr, Integer nomValueId)
    {       
        Pair<Attribute, Integer> newItem = 
                new Pair<Attribute, Integer>(attr, nomValueId);

        variables.add(newItem);
    }

    /**
     * Determines whether this set includes the value of a specific
     * attribute in the set of attributes
     * 
     * @param attr the target Attribute
     * @return true if this set has a value for this specific attribute
     */
    public Boolean containsAttribute(Attribute attr)
    {
        // Linear Search for a match in Attribute ID's
        for (Pair<Attribute, Integer> item : variables)
        {
            if (item.getFirst().equals(attr))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the value for a specific attribute in the set of
     * attributes. If this attribute is not in this set, this method returns 
     * null.
     *  
     * @param attr the target Attribute
     * @return the value of target Attribute specified in the
     * of the query.  null if this Attribute is not specified in this query
     */
    public Integer getValueForAttribute(Attribute attr)
    {
        if (attr == null)
        {
            return null;
        }

        /*
         *  Linear search for the attribute
         */
        for (Pair<Attribute, Integer> item : variables)
        {
            if (item.getFirst().equals(attr))
            {
                return item.getSecond();
            }
        }

        return null;
    }
    
    /**
     * @return the list of attribute/value pairs in this joint probability 
     * query.  Each pair is the ID of the attribute and the nominal value ID
     * of the value of this attribute.
     */
    public ArrayList<Pair<Attribute, Integer>> getVariables()
    {
        return this.variables;
    }
    
    @Override
    public Object clone()
    {
        VariableSet copy = new VariableSet();
       
        for (Pair<Attribute, Integer> pair : this.variables)
        {
            copy.addVariable(pair.getFirst(), pair.getSecond());
        }
        
        return copy;
    }
}
