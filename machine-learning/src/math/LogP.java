package math;

/**
 * Methods for dealing in log-probability space.  This class includes methods
 * for converting to and from log-values as well as taking products and 
 * summations over log-probabilities.
 * 
 */
public class LogP 
{
    /**
     * Raises a log-probability to the power E thereby converting the
     * log-probability to a proper probability.
     * 
     * @param x the log-probability
     * @return the proper probability
     */
	public static double exp(double x)
	{
		if (Double.isNaN(x))
		{
			return 0;
		}
		else
		{
			return Math.pow(Math.E, x);
		}
	}
	
	/**
	 * Take the natural logarithm of a probability thereby converting it to a
	 * log-probability.
	 * 
	 * @param x a probability
	 * @return the log-probability
	 */
	public static double ln(double x)
	{
		if (x == 0.0)
		{
			return Double.NaN;
		}
		else if (x > 0.0)
		{
			return  Math.log(x);
		}
		else
		{
			throw new IllegalArgumentException("Passed 'eLn' function the " +
											  "negative value " + x + ".  " +
											  "Argument must be greater than " +
											  "zero.");	
		}
	}
	
	/**
	 * Take the sum of two log-probabilities
	 * 
	 * @param eLnX the first log-probability
	 * @param eLnY the second log-probability
	 * @return the sum
	 */
	public static double sum(double eLnX, double eLnY)
	{
		if (Double.isNaN(eLnX) || Double.isNaN(eLnY))
		{
			if (Double.isNaN(eLnX))
			{
				return eLnY;
			}
			else
			{
				return eLnX;
			}
		}
		else
		{
			if (eLnX > eLnY)
			{
				return eLnX + ln(1 + Math.pow(Math.E, eLnY - eLnX));
			}
			else
			{
				return eLnY + ln(1 + Math.pow(Math.E, eLnX - eLnY));
			}
		}	
	}
	
	/**
	 * Take the product of two log-probabilities
	 * 
	 * @param eLnX the first log-probability
	 * @param eLnY the second log-probability
	 * @return the product
	 */
	public static double prod(double eLnX, double eLnY)
	{
		if (Double.isNaN(eLnX) || Double.isNaN(eLnY))
		{
			return Double.NaN;
		}
		else
		{
			return eLnX + eLnY;
		}
	}
	
	/**
	 * Take the quotient of two log-probabilities
	 * 
	 * @param eLnX the dividend
	 * @param eLnY the divisor
	 * @return the quotient
	 */
	public static double div(double eLnX, double eLnY)
	{
		if (Double.isNaN(eLnY))
		{
			throw new IllegalArgumentException("Passed 'eLnDivision' " +
					"function the a NaN quotient. Argument must be real.");
		}
		else if (Double.isNaN(eLnX))
		{
			return Double.NaN;
		}
		else
		{
			return eLnX - eLnY;
		}
	}	
}
