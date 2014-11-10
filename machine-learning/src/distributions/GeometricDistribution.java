package distributions;

import java.util.Random;

public class GeometricDistribution implements Distribution 
{
    private final Random RNG;
    private final double PARAMETER;
    
    public GeometricDistribution(double parameter) 
    {
        this.PARAMETER = parameter;
        this.RNG = new Random();
    }
    
    public double sample()
    {        
        int result = 1;
        while (true)
        {
            if (RNG.nextDouble() < PARAMETER) 
            {
                return result;
            }
            else
            {
                result++;
            }
        }
    }
    
    public double[] sampleMany(int numSamples) 
    {
        double[] samples = new double[numSamples];
        for (int i = 0; i < numSamples; i++) 
        {
            samples[i] = sample();
        }
        return samples;
    }
 }
