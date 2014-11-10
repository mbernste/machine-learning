package distributions;

public interface Distribution 
{
    public double sample();
    
    public double[] sampleMany(int numSamples);
}
