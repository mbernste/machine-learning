package hmm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import math.LogP;


/**
 * Implements a state whose emission probability distribution is tied to that
 * of another state.
 *
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 * 
 */
public class StateParamsTied extends State
{
    /**
     * Stores all emission probability distributions for all states with 
     * tied emission parameters
     */
	public static Map<String,Map<String, Double>> tiedEmissionParams
		= new HashMap<String, Map<String, Double>>();
	
	/**
	 * The ID of the parameters that this State uses
	 */
	private String paramsKey;
	
	/**
	 * Constructor.
	 * 
	 * @param paramsId the ID of the parameters that this State uses
	 */
	public StateParamsTied(String paramsKey, String id)
	{
		super(id);
		this.paramsKey = paramsKey;
		this.initializeParams();		
	}
	
	public StateParamsTied(State orig, String paramsKey)
	{
		super(orig);
		this.paramsKey = paramsKey;
		this.initializeParams();
	}
	
	public StateParamsTied(StateParamsTied orig)
	{
		super(orig);
		this.paramsKey = orig.paramsKey;
	}
	
	/**
	 * @return the emission probabilities from this state
	 */
	@Override
	public Map<String, Double> getEmissionProbabilites()
	{		
		return tiedEmissionParams.get(this.paramsKey);
	}
	
	/**
	 * Add an emission probability to this State
	 * 
	 * @param symbol the symbol this state will emmit
	 * @param probability the probability the state will emmit this symbol
	 */
	@Override
	public void addEmission(String symbol, Double probability)
	{
		tiedEmissionParams.get(this.paramsKey).put(symbol, probability);
	}
	
	/**
	 * Get the emission probability of a specific symbol from this state
	 * 
	 * @param symbol the symbol of interest
	 * @return the emission probability
	 */
	public double getEmissionProb(String symbol)
	{
		if (tiedEmissionParams.get(this.paramsKey).containsKey(symbol))
		{
			return tiedEmissionParams.get(this.paramsKey).get(symbol);
		}
		else
		{
			return LogP.ln(0.0);
		}
	}
	
	public String getParamsKey()
	{
		return this.paramsKey;
	}
	
	private void initializeParams()
	{
		if (!StateParamsTied.tiedEmissionParams.containsKey(this.paramsKey))
		{
			StateParamsTied.tiedEmissionParams.put(this.paramsKey, 
												   new HashMap<String, Double>());
			
			// TODO INITIALIZE PARAMETER PROBABILITIES IN MAP
		}
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
			result += (LogP.exp(e.getValue().getTransitionProbability()) + 
					" --> ");
			result += ("[" + destStateId + "]");
			result += "\n";
		}
		
		result += "............\n";
		
		for (Entry<String, Double> entry : 
			 StateParamsTied.tiedEmissionParams.get(this.paramsKey).entrySet())
		{
			result += (entry.getKey() + " >> " + LogP.exp(entry.getValue()) + "\n");
		}		

		result += "............\n";
		result += "\n";			
		
		return result;
	}
}
