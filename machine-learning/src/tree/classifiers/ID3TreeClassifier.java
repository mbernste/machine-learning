package tree.classifiers;

import java.util.ArrayList;

import pair.Pair;

import common.classification.ClassificationResult;
import common.classification.Classifier;

import tree.DecisionTree;
import tree.ID3Builder;
import data.DataSet;
import data.instance.Instance;

public class ID3TreeClassifier implements Classifier
{
    private DecisionTree dtTree;
    
    public ID3TreeClassifier(int minInstances, DataSet trainData)
    {
        ID3Builder id3Builder = new ID3Builder();       
        dtTree = id3Builder.generateDecisionTree(minInstances, trainData);
    }
    
    
    @Override
    public ClassificationResult classifyData(DataSet testData) 
    {
        ArrayList<Pair<Integer, Double>> resultList = 
                new ArrayList<Pair<Integer, Double>>();

        /*
         *  Classify each instance in the test dataset 
         */
        for (Instance instance : testData.getInstanceSet().getInstanceList())
        { 
            resultList.add( dtTree.classifyInstance(instance) );
        }

        /*
         *  Process the results 
         */
        ClassificationResult result = new ClassificationResult(resultList, testData);

        return result;
    }
    
    @Override
    public Object getModel()
    {
        return this.dtTree;
    }

}
