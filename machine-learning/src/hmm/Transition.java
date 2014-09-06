package hmm;

import math.LogP;

/**
 * This class implements a transition between two states in the Markov model: 
 * an "origin" state and a "destination" state.  That is, each Transition 
 * object represents  a transition <i>from</i> the origin state <i>to</i> the 
 * destination state. Each Transition object stores a "count" that records the 
 * number of times we observe in the original text that the word associated 
 * with the destination state follows the word associate with the origin state.
 */
public class Transition
{
    /**
     * ID of the origin state
     */
	private String originId;
	
	/**
	 * ID of the destination state
	 */
	private String destinationId;
	
	/**
	 * The transition's associated probability
	 */
	private double probability;
	
	/**
	 * Constructor
	 * 
	 * @param originId ID of the origin state
	 * @param destinationId ID of the destination state
	 * @param probability probability of taking that transition
	 */
	public Transition(String originId, String destinationId, double probability)
	{
		this.originId = originId;
		this.destinationId = destinationId;
		this.probability = probability;
	}
	
	/**
	 * Copy constructor
	 */
	public Transition(Transition t)
	{
		this.originId = t.originId;
		this.destinationId = t.destinationId;
		
		this.probability = t.probability;
	}
	
	/**
	 * Get the integer ID of the state this transition moves to.
	 * 
	 * @return the destination ID
	 */
	public String getDestinationId()
	{
		return destinationId;
	}
	
	/**
	 * Set the integer ID of the state this transition moves to.
	 * 
	 * @param destinationId the destination ID
	 */
	public void setDestinationId(String destinationId)
	{
		this.destinationId = destinationId;
	}
	
	/**
	 * Get the ID of the state this transition moves from.
	 * 
	 * @return
	 */
	public String getOriginId()
	{
		return originId;
	}
	
	/**
	 * Set the ID of the state this transition moves from.
	 * 
	 * @param originId the ID of the state this transition moves from
	 */
	public void setOriginId(String originId)
	{
		this.originId = originId;
	}
	
	/**
	 * @param the transition probability
	 */
	public void setTransitionProbability(double probability)
	{
		this.probability = probability;
	}
	
	/**
	 * @return the transition probability
	 */
	public double getTransitionProbability()
	{
		return this.probability;
	}
	
	public void incrementTransitionValue(double value)
	{
		this.probability = LogP.sum(this.probability, value);
	}

}
