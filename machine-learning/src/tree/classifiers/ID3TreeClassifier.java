package tree.classifiers;

import java.util.ArrayList;
import java.util.List;

import classify.ClassificationResult;
import classify.Classifier;
import pair.Pair;
import tree.DecisionTree;
import tree.algorithms.ID3TreeBuilder;
import data.DataSet;
import data.Instance;

public class ID3TreeClassifier implements Classifier
{
    private DecisionTree dtTree;
    
    public ID3TreeClassifier(int minInstances, DataSet trainData)
    {
        ID3TreeBuilder id3Builder = new ID3TreeBuilder(minInstances);       
        dtTree = id3Builder.buildDecisionTree(trainData);
    }
    
    @Override
    public ClassificationResult classifyData(DataSet testData) 
    {
        /*
         *  Classify each instance in the test dataset 
         */
        List<Pair<Integer, Double>> resultList = new ArrayList<>();
        for (Instance instance : testData.getInstanceSet().getInstances())
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
    
    @Override 
    public String toString()
    {
        return "ID3\n\n" + dtTree;
    }

}
