package classify;

import data.DataSet;

/**
 * Any model learned to perform a supervised classificiation task should 
 * implement this interface so that it can be integrated into an experimental/
 * learning framework.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public interface Classifier 
{
    public ClassificationResult classifyData(DataSet testData); 
    
    public Object getModel();
}
