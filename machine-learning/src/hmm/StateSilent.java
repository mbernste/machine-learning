package hmm;

import java.util.Map;
import java.util.Map.Entry;

import math.LogP;



/**
 * Implements a state that does not emit any symbols.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class StateSilent extends State
{
	/**
	 * Constructor
	 */
	public StateSilent()
	{
		super();
		this.isSilent = true;
	}
	
	public StateSilent(String id)
	{
		super(id);
		this.isSilent = true;
	}
	
	@Override
	public Map<String, Double> getEmissionProbabilites()
	{
		System.err.println("Attempting to retrieve emission probabilities on " +
						   "emission probabilities for silent state " + 
						   	this.id);
		return null;
	}
	
	@Override
	public void addEmission(String symbol, Double probability)
	{
		System.err.println("Attempting to add emission probabilities to " +
				   " silent state " + this.id);
	}
	
	@Override
	public double getEmissionProb(String symbol)
	{
		// TODO CHECK IF THIS IS CORRECT
		return LogP.ln(0.0);
	}
	
	@Override
	public String toString()
	{
		String result = "";
		result += "[";
		result += this.id;
		result += "]";
		result += "\n";
		
		result += "............\n";
		
		for (Entry<String, Transition> e : transitions.entrySet())
		{
			String destStateId = e.getKey();			
			result += LogP.exp(e.getValue().getTransitionProbability()) + 
					" --> ";
			result += ("[" + destStateId + "]");
			result += "\n";
		}
		
		result += "............\n";

		result += "silent\n";

		result += "............\n";
		result += "\n";			
		
		return result;
	}

}
