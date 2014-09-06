package hmm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import math.LogP;



/**
 * This class implements a single state in the HMM.  Each State object
 * stores all Transition objects that transition from this state to 
 * other states in the model.
 * 
 *  @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class State
{
	/**
	 * Emission probabilities
	 */
	private Map<String, Double> emissionProbs;
	
	/**
	 * Unique ID
	 */
	protected String id;
	
	/**
	 * The transitions to other states
	 */
	protected Map<String, Transition> transitions;
	
	/**
	 * True if this state is silent
	 */
	protected boolean isSilent;
	
	/**
	 * Constructor
	 */
	public State()
	{
		transitions = new HashMap<String, Transition>();
		emissionProbs = new HashMap<String, Double>();
		this.isSilent = false;
	}
	
	/**
	 * Constructor
	 * 
	 * @param id the ID of the state
	 */
	public State(String id)
	{
		this();
		this.id = id;
	}
	
	/**
	 * Copy constructor
	 */
	public State(State s)
	{
		this(s.getId());
		
		for (Transition t : s.getTransitions())
		{
			this.transitions.put(t.getDestinationId(), new Transition(t));
		}

		for (String str : s.emissionProbs.keySet())
		{
			this.emissionProbs.put(str, LogP.ln(0.0));
		}
	}
	
	/**
	 * @return the unique integer ID of this state
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @param id the unique integer ID of this state
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * @return true if this state is a silent state
	 */
	public boolean isSilent()
	{
		return this.isSilent;
	}
	
	/**
	 * @return the emission probabilities from this state
	 */
	public Map<String, Double> getEmissionProbabilites()
	{
		return emissionProbs;
	}
	
	/**
	 * Add an emission probability to this State
	 * 
	 * @param symbol the symbol this state will emmit
	 * @param probability the probability the state will emmit this symbol
	 */
	public void addEmission(String symbol, Double probability)
	{
		emissionProbs.put(symbol, probability);
	}
	
	/**
	 * Get the emission probability of a specific symbol from this state
	 * 
	 * @param symbol the symbol of interest
	 * @return the emission probability
	 */
	public double getEmissionProb(String symbol)
	{
		if (emissionProbs.containsKey(symbol))
		{
			return emissionProbs.get(symbol);
		}
		else
		{
			return LogP.ln(0.0);
		}
	}
	
	/**
	 * Get the transition probability of transitioning from this state to the 
	 * state with the target ID
	 * 
	 * @param destId the target ID
	 * @return the transition probability of moving to the state with the target
	 * ID
	 */
	public double getTransitionProb(String destId)
	{
		if (transitions.containsKey(destId))
		{
			return transitions.get(destId).getTransitionProbability();
		}
		
		return LogP.ln(0.0);
	}
	
	public void normalizeTransitionProbabilities()
	{
		double sum = Double.NaN;
		for (Transition t : transitions.values())
		{
			sum = LogP.sum(sum, t.getTransitionProbability());
		}
		
		for (Transition t : transitions.values())
		{
			t.setTransitionProbability(LogP.div(t.getTransitionProbability(), sum));
		}
	}
	
	/**
	 * Determine if a transition exists from this state to the state with the 
	 * target ID.
	 * 
	 * @param destId the ID of the destination state 
	 * @return true if the transition exists, false otherwise.
	 */
	public boolean transitionExists(String destId)
	{
		return transitions.containsKey(destId);
	}
	
	/**
	 * Add an outgoing transition from this state to some other state.
	 *
	 * @param transition the new transition
	 */
	public void addTransition(Transition transition)
	{
		if (!transitionExists(transition.getDestinationId()))
		{
			transitions.put(transition.getDestinationId(), transition);
		}
	}

	/**
	 * @return the array list containing all transition objects moving from 
	 * this state
	 */
	public Collection<Transition> getTransitions()
	{
		return transitions.values();
	}
		
	/**
	 * Find and return a transition from this state to the specified state.  
	 * If this transition does not exist this method returns null.
	 * 
	 * @return the transition that moves from this state to the specified 
	 * state, if such a transition does not exist, this method returns null
	 */
	public Transition getTransition(String destinationId)
	{	
		return transitions.get(destinationId);
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
			 getEmissionProbabilites().entrySet())
		{
			result += (entry.getKey() + " >> " + LogP.exp(entry.getValue()) + "\n");
		}		

		result += "............\n";
		result += "\n";			
		
		return result;
	}
}
