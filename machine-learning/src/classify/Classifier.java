package classify;

import data.DataSet;

/**
 * Any model learned to perform a supervised classification task should 
 * implement this interface.
 * 
 */
public interface Classifier 
{
    public ClassificationResult classifyData(DataSet testData); 
    
    public Object getModel();
}
