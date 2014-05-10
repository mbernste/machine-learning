package common;

/**
 * Methods for dealing in log-probability space.  This class includes methods
 * for converting to and from log-values as well as taking products and 
 * summations over log-probabilities.
 * 
 * 
 * @author matthewbernstein
 *
 */
public class LogP 
{
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
