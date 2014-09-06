package classify.evaluate;

/**
 * Methods for evaluating percentage errors from classification experiments.
 * 
 */
public class PercentageError 
{
    /**
     * Mean percentage error
     * 
     * @param truthVals the true values
     * @param predictedVals the predicted values
     * @return the mean percentage error between the true and predicted values
     */
	public static double meanPercentageError(Double[] truthVals,
											 Double[] predictedVals)
	{
		double sum = 0.0;
		
		for (int i = 0; i < truthVals.length; i++)
		{
		    sum += percentageError(truthVals[i], predictedVals[i]);
		}
		
		return (100.0 / truthVals.length) * sum;
	}
	
	/**
	 * Percentage error
	 * 
	 * @param truth the true value
	 * @param predicted the predicted value
	 * @return the precentage error between the true and predicted values
	 */
	public static double percentageError(Double truth, Double predicted)
	{
	    return Math.abs((truth - predicted) / truth);
	}
}
