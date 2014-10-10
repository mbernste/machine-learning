package bayes.structuresearch;

import graph.prim.Edge;
import graph.prim.Prim;

import java.util.ArrayList;
import java.util.List;


import pair.Pair;
import bayes.BNNode;
import bayes.BayesianNetwork;
import data.Attribute;
import data.DataSet;
import data.Instance;

public class TANBuilder extends NetworkBuilder
{
    private Integer laplaceCount;

    private Double[][] condMutualInfo;

    private DataSet data;

    private List<Attribute> nonClassAttributes;

    @SuppressWarnings("unchecked")
    public void Initialize(DataSet data, Integer laplaceCount)
    {
        this.data = data;
        this.laplaceCount = laplaceCount;

        // Get all Attributes not including the class attribute
      //  nonClassAttributes = (ArrayList<Attribute>)
        //        data.getAttributeSet().getAttributes().clone();
    
       // nonClassAttributes = new AttributeSet(data.getAttributeSet()).getAttributes();
        
        nonClassAttributes.remove(data.getClassAttribute());
    }

    @Override
    public BayesianNetwork buildNetwork(DataSet data, Integer laplaceCount)
    {
        this.Initialize(data, laplaceCount);

        // Create the Bayesian Network
        BayesianNetwork net = super.setupNetwork(data, laplaceCount);
        net.setNetInference(BayesianNetwork.StructureSearch.TAN);

        // Get the Node that represents the class attribute
        Attribute classAttr = data.getClassAttribute();
        BNNode classAttrNode = net.getNode(classAttr);

        // Create edges from the class Node to all other nodes
        for (BNNode node : net.getNodes())
        {
            if (!node.equals( classAttrNode ))
            {
                net.createEdge(classAttrNode, node, data, laplaceCount);
            }
        }

        // Build the conditional mutual information matrix
        buildConditionalMutualInfoMatrix(data);

        // Run Prim's Algorithm
        ArrayList<Edge> edges = Prim.runPrims(condMutualInfo);

        // Add edges as determined by Prim's
        for (Edge edge : edges)
        {
            Attribute parentAttr =  
                    nonClassAttributes.get(edge.getFirstVertex());

            Attribute childAttr =  
                    nonClassAttributes.get(edge.getSecondVertex());

            BNNode parent = net.getNode(parentAttr);
            BNNode child = net.getNode(childAttr);

            net.createEdge(parent, child, data, laplaceCount);
        }

        return net;
    }

    private void buildConditionalMutualInfoMatrix(DataSet data)
    {
        // Calculate number of non-class attributes
        int numAttributes = nonClassAttributes.size();

        // Create conditional mutual information matrix
        condMutualInfo = new Double[numAttributes][numAttributes];

        // Calculate each element of the matrix
        for (int r = 0; r < numAttributes; r++)
        {
            for (int c = 0; c < numAttributes; c++)
            {
                if (r != c)
                {
                    Attribute attr1 = nonClassAttributes.get(r);
                    Attribute attr2 = nonClassAttributes.get(c);

                    condMutualInfo[r][c] = 
                            conditionalMutualInfo(attr1, attr2);
                }
                else
                {
                    condMutualInfo[r][c] = null;
                }
            }
        }
    }

    /**
     * Calculate the conditional mutual information between two attributes
     * conditioned on the class attribute:
     * <br>
     * I(X1, X2 | Y)
     * <br>
     * <br>
     * Where Y is the class attribute
     * <br>
     * <br>
     * Conditional mutual information is calculated as follows:
     * <br>
     * <br>
     * &Sigma; y &isin; Y &Sigma; x1 &isin; X1 &Sigma; x2 &isin; X2 
     * P(x1, x2 | y) / ( P(x1 | y) * P(x2 | y) )  
     * 
     * @param attr1	the first Attribute
     * @param attr2 the second Attribute
     * @param classAttr the class Attribute for which the mutual information
     * of the first two attributes are conditioned on
     * @return the conditional mutual information in bits
     */
    private Double conditionalMutualInfo(Attribute attr1, Attribute attr2)
    {
        Double result = 0.0;

        Attribute classAttr = data.getClassAttribute();

        for (Integer classValue : classAttr.getNominalValueMap().values())
        {
            for (Integer val1 : attr1.getNominalValueMap().values())
            {
                for (Integer val2 : attr2.getNominalValueMap().values())
                {

                    // Build the first Attribute, nominal value ID pair
                    // to be passed to the methods used for calculating the
                    // numerator and denominator used in the calculation
                    Pair<Attribute, Integer> attrVal1
                    = new Pair<Attribute, Integer>();

                    attrVal1.setFirst(attr1);
                    attrVal1.setSecond(val1);


                    // Build the second Attribute, nominal value ID pair
                    Pair<Attribute, Integer> attrVal2
                    = new Pair<Attribute, Integer>();

                    attrVal2.setFirst(attr2);
                    attrVal2.setSecond(val2);

                    /*
                     *  Calculate each component of the calculation
                     */

                    /*
                     *  P(X1 = x1, X2 = x2, Y = y)
                     */
                    Double jointProb = 
                            jointProbability(attrVal1, attrVal2, classValue);

                    /*
                     *  P(X1 = x1, X2 = x2 | Y = y)
                     */
                    Double conditionalJointProb = 
                            conditionalJointProbability(attrVal1, attrVal2,
                                    classValue);

                    /*
                     *  P(X1 = x1 | Y = y) 
                     */
                    Double condProb1 = conditionalProbability(attrVal1, 
                            classValue);

                    /*
                     *  P(X2 = x2 | Y = y) 
                     */
                    Double condProb2 = conditionalProbability(attrVal2, 
                            classValue);

                    // Calculate numerator and denominator
                    Double logTerm = Math.log(conditionalJointProb / (condProb1 * condProb2)) /
                            Math.log(2);

                    result += jointProb * logTerm;
                }
            }
        }

        return result;
    }

    /**
     * Calculate the conditional joint probability of two values of two
     * attributes conditioned on a specific value of the class attribute.
     * <br>
     * <br>
     * This method calculates P(X1 = x1, X2 = x2 | Y = y) where X1 and X2
     * are two attributes, and Y is the class attribute.
     * 
     * @param pair1 an Attribute, Integer pair referring to the pair (X1, x1)
     * @param pair2 an Attribute, Integer pair referring to the pair (X2, x2)
     * @param classVal the value of the class attribute, referring to y
     * @return the value of the calculated probability
     */
    public Double conditionalJointProbability(Pair<Attribute, Integer> pair1,
            Pair<Attribute, Integer> pair2,
            Integer classVal)
    {

        int classAttrId = data.getClassAttributeId();

        int numeratorCount = 0;
        int denomCount = 0;

        for (Instance instance : data.getInstanceSet().getInstances())
        {
            // Get the IDs of the two non-class Attributes
            Integer attrId1 = pair1.getFirst().getId();
            Integer attrId2 = pair2.getFirst().getId();

            // Get the value of the current instance's class attribute
            Integer instanceClassAttrValue = 
                    instance.getAttributeValue(classAttrId).intValue();

            // Get the value of the current instance's first attribute
            Integer instanceAttrValue1 =
                    instance.getAttributeValue(attrId1).intValue();

            // Get the value of the current instance's first attribute
            Integer instanceAttrValue2 =
                    instance.getAttributeValue(attrId2).intValue();	

            if (instanceClassAttrValue == classVal)
            {
                denomCount++;
            }

            if (instanceClassAttrValue == classVal &&
                    instanceAttrValue1 == pair1.getSecond() &&
                    instanceAttrValue2 == pair2.getSecond())
            {
                numeratorCount++;
            }
        }

        Integer numValuesAttr1 = pair1.getFirst().getNominalValueMap().size();
        Integer numValuesAttr2 = pair2.getFirst().getNominalValueMap().size();


        Double numerator = numeratorCount + (double) laplaceCount;
        Double denominator = denomCount + ((double) laplaceCount * 
                numValuesAttr1 *
                numValuesAttr2);

        return numerator / denominator;
    }	

    /**
     * Calculate the conditional probability of two values of two
     * attributes conditioned on a specific value of the class attribute.
     * <br>
     * <br>
     * This method calculates P(X = x | Y = y) where X is an attribute, 
     * and Y is the class attribute.
     * 
     * @param pair an Attribute, Integer pair referring to the pair (X, x)
     * @param classVal the value of the class attribute, referring to y
     * @return the value of the calculated probability
     */
    public Double conditionalProbability(Pair<Attribute, Integer> pair,
            Integer classVal)
    {
        int classAttrId = data.getClassAttributeId();

        int numeratorCount = 0;
        int denomCount = 0;

        for (Instance instance : data.getInstanceSet().getInstances())
        {
            // Get the IDs of the two non-class Attributes
            Integer attrId = pair.getFirst().getId();

            // Get the value of the current instance's class attribute
            Integer instanceClassAttrValue = 
                    instance.getAttributeValue(classAttrId).intValue();

            // Get the value of the current instance's first attribute
            Integer instanceAttrValue =
                    instance.getAttributeValue(attrId).intValue();

            if (instanceClassAttrValue == classVal)
            {
                denomCount++;
            }

            if (instanceClassAttrValue == classVal &&
                    instanceAttrValue == pair.getSecond())
            {
                numeratorCount++;
            }
        }

        Integer numValuesAttr = pair.getFirst().getNominalValueMap().size();		

        Double numerator = numeratorCount + (double) laplaceCount;
        Double denominator = denomCount + ((double) laplaceCount * 
                numValuesAttr);


        return numerator / denominator;

    }	

    /**
     * Calculate the conditional joint probability of two values of two
     * attributes conditioned on a specific value of the class attribute.
     * <br>
     * <br>
     * This method calculates P(X1 = x1, X2 = x2 | Y = y) where X1 and X2
     * are two attributes, and Y is the class attribute.
     * 
     * @param pair1 an Attribute, Integer pair referring to the pair (X1, x1)
     * @param pair2 an Attribute, Integer pair referring to the pair (X2, x2)
     * @param classVal the value of the class attribute, referring to y
     * @return the value of the calculated probability
     */
    public Double jointProbability(Pair<Attribute, Integer> pair1,
            Pair<Attribute, Integer> pair2,
            Integer classVal)
    {

        int classAttrId = data.getClassAttributeId();

        int numeratorCount = 0;
        int denomCount = data.getInstanceSet().getInstances().size();

        for (Instance instance : data.getInstanceSet().getInstances())
        {
            // Get the IDs of the two non-class Attributes
            Integer attrId1 = pair1.getFirst().getId();
            Integer attrId2 = pair2.getFirst().getId();

            // Get the value of the current instance's class attribute
            Integer instanceClassAttrValue = 
                    instance.getAttributeValue(classAttrId).intValue();

            // Get the value of the current instance's first attribute
            Integer instanceAttrValue1 =
                    instance.getAttributeValue(attrId1).intValue();

            // Get the value of the current instance's first attribute
            Integer instanceAttrValue2 =
                    instance.getAttributeValue(attrId2).intValue();	

            if (instanceClassAttrValue == classVal &&
                    instanceAttrValue1 == pair1.getSecond() &&
                    instanceAttrValue2 == pair2.getSecond())
            {
                numeratorCount++;
            }
        }

        Integer numValuesAttr1 = pair1.getFirst().getNominalValueMap().size();
        Integer numValuesAttr2 = pair2.getFirst().getNominalValueMap().size();
        Integer numValuesClass = data.getClassAttribute()
                .getNominalValueMap().size();

        Double numerator = numeratorCount + (double) laplaceCount;
        Double denominator = denomCount + ((double) laplaceCount * 
                numValuesAttr1 *
                numValuesAttr2 * 
                numValuesClass );

        return numerator / denominator;
    }	

}
