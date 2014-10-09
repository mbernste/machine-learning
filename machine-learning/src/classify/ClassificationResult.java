package classify;

import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.DataSet;
import pair.Pair;

/**
 * Objects of this class are used to store the results of a single 
 * classification experiment.
 * 
 */
public class ClassificationResult 
{

    /**
     * String representation of the results
     */
    private String resultStr = "";

    /**
     * Accuracy of the classification task
     */
    private Double accuracy;

    /**
     * Size of the test set used in the classification
     */
    private Integer testDataSize; 

    /**
     * Constructor
     * 
     * @param resultList an ArrayList of predictions. Each Pair in this list
     * represents the classification of a single instance in the test data.
     * The first element of each pair refers to the predicted nominal value ID
     * the class attribute.  The second element refers to the confidence of the
     * classifier.
     *  
     * @param testData the DataSet object containing all test instances
     */
    public ClassificationResult(List<Pair<Integer, Double>> resultList,
            DataSet testData)
    {
        Attribute classAttr = testData.getClassAttribute();
        
        int correctCount = 0;

        for (int i = 0; i < resultList.size(); i++)
        {			
            Integer classification = resultList.get(i).getFirst();            
            Integer truth = testData.getInstanceSet()
                    .getInstanceById(i)
                    .getAttributeValue(classAttr).intValue();

            /*
             *  Check for correct classification
             */
            if (classification == truth)
            {
                correctCount++;
            }

            resultStr += classAttr.getNominalValueName(classification);
            resultStr += " ";
            resultStr += classAttr.getNominalValueName(truth);
            resultStr += " ";
            resultStr += resultList.get(i).getSecond();
            resultStr += "\n";
        }

        resultStr += "\n";
        resultStr += correctCount;

        // Set metrics
        this.testDataSize = testData.getInstanceSet().getInstances().size();
        this.accuracy = (double) correctCount / this.testDataSize;
    }

    /**
     * @return the classification accuracy from this experiment
     */
    public Double getAccuracy()
    {
        return this.accuracy;
    }

    @Override
    public String toString()
    {
        return resultStr;
    }

}
