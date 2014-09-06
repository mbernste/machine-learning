package bayes.classifiers;

import java.util.ArrayList;
import java.util.Collection;

import classify.ClassificationResult;
import classify.Classifier;




import pair.Pair;

import data.Attribute;
import data.DataSet;
import data.Instance;
import bayes.BNNode;
import bayes.BayesianNetwork;
import bayes.cpd.CPDQuery;
import bayes.structuresearch.NaiveBayesBuilder;
import bayes.structuresearch.TANBuilder;

/**
 * A classifier for performing a classification task using a Naive Bayesian 
 * network.
 * 
 *  @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class NaiveBayesClassifier implements Classifier 
{	
    /**
     * The Bayesian Network used for doing the classification 
     */
    private BayesianNetwork bayesNet;

    /**
     * The Node of the Bayes Net corresponding to the class attribute we are
     * trying to classify 
     */
    private BNNode classNode;

    /**
     * Constructor
     * 
     * @param data the DataSet used to construct the classifier
     * @param laplaceCount the Laplace count to use when generating the
     * Bayesian network
     * @param tan true if we want to use the TAN algorithm for creating
     * a maximal spanning tree among the attributes that are not the class
     * attribute
     */
    public NaiveBayesClassifier(DataSet data, Integer laplaceCount, boolean tan)
    {
        if (tan) 	// Build with tree augmentation
        {
            TANBuilder tBuilder = new TANBuilder();
            this.bayesNet = tBuilder.buildNetwork(data, laplaceCount);
        }
        else		// Build standard bayes net
        {
            NaiveBayesBuilder nBuilder = new NaiveBayesBuilder();
            this.bayesNet = nBuilder.buildNetwork(data, laplaceCount);
        }

        this.classNode = bayesNet.getNode(data.getClassAttribute());	
    }

    /**
     * Classify all of the instances in a data set containing test instances
     * 
     * @param testData the DataSet object storing all Instance objects we wish
     * to classify
     * 
     * @return an ArrayList storing the results of the classification.  The
     * ith element of this list is a Pair object whose first element is the 
     * nominal value ID of the class Attribute for which the classifier 
     * predicted the ith Instance to be.  The second value is the probability
     * the ith instance is the predicted value.
     */
    public ClassificationResult classifyData(DataSet testData)
    {
        ArrayList<Pair<Integer, Double>> resultList = 
                new ArrayList<Pair<Integer, Double>>();

        /*
         *	Classify each instance in the test dataset 
         */
        for (Instance instance : testData.getInstanceSet().getInstances())
        {
            resultList.add( this.classifyInstance(instance) );
        }

        /*
         *	Process the results 
         */
        ClassificationResult result = new ClassificationResult(resultList,
                testData);

        return result;
    }

    /**
     * Classify a single Instance object
     * 
     * @param instance the Instance to be classified
     * @return A Pair object whose first element 
     * is the nominal value ID of the class Attribute for which the classifier 
     * predicted the ith Instance to be.  The second value is the probability
     * the ith instance is the predicted value.
     */
    public Pair<Integer, Double> classifyInstance(Instance instance)
    {
        Pair<Integer, Double> result = new Pair<Integer, Double>();

        Double maxProbability = 0.0;
        Integer classifiedValue = -1;

        Collection<Integer> classValuesIds = 
                classNode.getAttribute().getNominalValueMap().values();

        for (Integer classValueId : classValuesIds)
        {
            try
            {
                Double probability = bayesTheorem(instance, classValueId);

                if (probability >= maxProbability)
                {					
                    maxProbability = probability;
                    classifiedValue = classValueId;
                }
            }
            catch(Exception e)
            {
                System.err.print("Error classifying instance: ");
                System.err.println(instance);
                System.err.println(e.getMessage());
            }
        }

        result.setFirst(classifiedValue);
        result.setSecond(maxProbability);
        return result;
    }

    /**
     * 
     * @param instance
     * @param classValueId the nominal value ID of the class Attribute that we
     * are trying to calculate the probability for
     * @return
     * @throws Exception
     */
    public Double bayesTheorem(Instance instance, int classValueId)
            throws Exception
            {
        boolean isValidValue = checkNominalValueId(classValueId);
        Double probability = 1.0;

        // Check that this nominal value ID is valid nominal value of the 
        // class attribtue
        if (!isValidValue)
        {
            System.err.println();
            throw new Exception("Error classifying instance.  Could " +
                    "not find nominal value ID " + classValueId + " for " +
                    "attribute " + classNode.getAttribute().getName());
        }

        // Calculate the numerator P(X|Y)P(Y)
        Double numerator = calculateBayesNumerator(instance, classValueId);

        // Calculate the denominator P(X)
        Double denominator = calculateBayesDenominator(instance);

        // Calculate the final probability P(Y)
        probability = numerator / denominator;

        return probability;
            }

    /**
     * Given that Bayes Theorem is:
     * <br>
     * <br>
     * P(Y|X) = P(X|Y) / P(X)
     * <br>
     * <br>
     * This method calculates the numerator P(X|Y)
     * 
     * @param instance the instance for which were are calculating the
     * probability of a given Y
     * @param classValueId the nominal value ID of Y
     * @return the value of the numerator
     */
    private Double calculateBayesNumerator(Instance instance, 
            Integer classValueId)
    {
        return
                classAttrConditionalProbability(instance, classValueId);
    }

    /**
     * Given that Bayes Theorem is:
     * <br>
     * <br>
     * P(Y|X) = P(X|Y) / P(X)
     * <br>
     * <br>
     * This method calculates the denominator P(X)
     * 
     * @param instance the instance for which were are calculating the
     * probability of a given Y
     * @param nomValueId the nominal value ID of the Y attribute
     * @return the value of the denominator, P(X)
     */
    private Double calculateBayesDenominator(Instance instance)
    {
        Double denominator = 0.0;

        Collection<Integer> classValues = 
                classNode.getAttribute().getNominalValueMap().values();

        for (Integer classValue : classValues)
        {
            Double result = classAttrConditionalProbability(instance, classValue);

            denominator += result;
        }

        return denominator;
    }

    /**
     * Calculate the conditional probability of a set of non-class attributes
     * If we denote the class attribute Y, this method calculates:
     * <br>
     * P( X | Y = y )
     * <br>
     * Where X is the set of non-class attributes, X = {X_1...X_n}. The
     * values of X are determined by the Instance object passed to this method.
     * 
     * @param instance the Instance used to set the values of X, the set of 
     * non-class attributes
     * 
     * @param classValueId the value of the class attribute, y, for which we are 
     * using to calculate P(X|Y=y)
     *  
     * @return the value of P( X | Y = y )
     */
    public Double classAttrConditionalProbability(Instance instance, 
            Integer classValueId)	
    {
        Double result = 1.0;

        // Create query for P(Y)
        CPDQuery query = new CPDQuery();
        query.addQueryItem(classNode.getAttribute(), classValueId);

        // Calculate P(Y)
        result = classNode.query(query);

        // Calculate each P(X|Y) for all attributes
        for (BNNode node : bayesNet.getNodes())
        {	
            if (!node.equals(classNode))
            {
                // Get current Node attribute and the instances value for this
                // node's attribute
                Attribute nodeAttr = node.getAttribute();
                Integer instValue = 
                        instance.getAttributeValue(nodeAttr).intValue();

                // Create a new query with this node's attribute and the 
                // instant's value for that attribute
                query = new CPDQuery();
                query.addQueryItem(nodeAttr, instValue);

                // Iterate over this node's parents in order to build the query 
                // for P( A | B_1 ... B_n)
                for (BNNode parent : node.getParents())
                {	
                    if (parent.equals(classNode))
                    {
                        query.addQueryItem(classNode.getAttribute(), classValueId);
                    }
                    else
                    {
                        Attribute parentAttr = parent.getAttribute();
                        Integer parentValue = 
                                instance.getAttributeValue(parentAttr).intValue();

                        query.addQueryItem(parentAttr, parentValue);						
                    }
                }

                result *= node.query(query);
            }
        }

        return result;	
    }

    /**
     * A helper method for checking that a given nominal value ID is a valid 
     * nominal value ID for the class Attribute
     * 
     * @param nomValueId the nominal value ID we wish to test
     * @return true if this is a valid nominal value ID, false otherwise
     */
    private boolean checkNominalValueId(Integer nomValueId)
    {
        Collection<Integer> allValues = 
                classNode.getAttribute().getNominalValueMap().values();				

        return allValues.contains(nomValueId);
    }

    @Override
    public String toString()
    {
        return this.bayesNet.toString();
    }
    
    @Override
    public Object getModel()
    {
        return this.bayesNet;
    }

}
