package tree.classifiers;

import java.util.ArrayList;

import pair.Pair;

import tree.DecisionTree;
import tree.ID3Builder;
import common.classification.ClassificationResult;
import common.classification.Classifier;
import data.DataSet;

public class ID3TreeClassifier// implements Classifier
{

    private DecisionTree dtTree;
    
    public ID3TreeClassifier(int minInstances, DataSet trainData)
    {
        ID3Builder id3Builder = new ID3Builder();       
        dtTree = id3Builder.generateDecisionTree(minInstances, trainData);
    }
    
    /*
    @Override
    public ClassificationResult classifyData(DataSet testData) 
    {
        ArrayList<Pair<Integer, Double>> classifications =
                    new ArrayList<Pair<Integer, Double>>();
        
        for (Instance instance : testData.getInstanceList())
        {
            
        }
        
        //return new ClassificationResult();;
    }*/

}
