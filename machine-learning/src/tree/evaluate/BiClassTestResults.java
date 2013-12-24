package tree.evaluate;

public class BiClassTestResults 
{
	private static final int NEGATIVE = 0;
	private static final int POSITIVE = 1;
	
	private Integer posPredictions = 0;
	private Integer negPredictions = 0;
	
	private Integer falsePos = 0;
	private Integer truePos = 0;
	private Integer falseNeg = 0;
	private Integer trueNeg = 0;
	
	
	public void addClassification(Integer predictedLabel, Integer trueLabel)
	{
		
		if (predictedLabel.equals(POSITIVE))
		{
			this.posPredictions++;
		}
		else if (predictedLabel.equals(NEGATIVE))
		{
			this.negPredictions++;
		}
		
		// Compute whether this classification was TP, FP, TN, or FN
		if (predictedLabel.equals(trueLabel))
		{
			if (predictedLabel.equals(POSITIVE))
			{
				truePos++;
			}
			else if (predictedLabel.equals(NEGATIVE))
			{
				trueNeg++;
			}
		}
		else
		{
			if (predictedLabel.equals(POSITIVE))
			{
				falsePos++;
			}
			else if (predictedLabel.equals(NEGATIVE))
			{
				falseNeg++;
			}
		}
	}
	
	public void printResults()
	{
		System.out.println((truePos + trueNeg) + " " + (posPredictions + negPredictions));
		/*
		System.out.println("Correctly classified: " + (truePos + trueNeg));
		System.out.println("Total instances: " + (posPredictions + negPredictions));
		System.out.print("\n");*/
	}

}
