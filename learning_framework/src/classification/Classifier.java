package classification;

import data.DataSet;

public interface Classifier 
{
    public ClassificationResult classifyData(DataSet testData); 

}
