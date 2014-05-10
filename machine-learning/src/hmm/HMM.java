package hmm;

import hmm.algorithms.SortSilentStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

public class HMM 
{
	protected StateContainer states;
	
	private String beginStateId;
	private String endStateId;

	
	public HMM()
	{
		states = new StateContainer();
	}
	
	/**
	 * Get an emission probability for a specific symbol from a specific state.
	 * 
	 * @param stateId the ID of the state that is emitting the symbol
	 * @param symbol the symbol the given state is emitting
	 * @return the emission probability
	 */
	public double getEmissionProb(String stateId, String symbol)
	{
		return states.getStateById(stateId).getEmissionProb(symbol);
	}
	
	/**
	 * Get a transition probability between two states in the HMM.
	 * 
	 * @param originId the ID of the origin state
	 * @param destId the ID of the destination state
	 * @return the probability of making that transition
	 */
	public double getTransitionProb(String originId, String destId)
	{
		return states.getStateById(originId).getTransitionProb(destId);
	}
	
	public boolean transitionExists(String originId, String destId)
	{
		return states.getStateById(originId).transitionExists(destId);
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		for (State state : states.getStates())
		{			
			result += state.toString();
		}
		
		return result;
	}
	
	/**
	 * @return the HMM's StateContainer instance
	 */
	public StateContainer getStateContainer()
	{
		return this.states;
	}
	
	public Collection<State> getStates()
	{
		return this.states.getStates();
	}
	
	public Collection<State> getSilentStates()
	{
		return this.states.getSilentStates();
	}
	
	public ArrayList<State> getSortedSilentStates()
	{
		return SortSilentStates.run(this);
	}
	
	public State getStateById(String id)
	{
		return this.states.getStateById(id);
	}
	
	/**
	 * @return the HMM's begin state
	 */
	public State getBeginState()
	{
		return this.states.getStateById(this.beginStateId);
	}
	
	/**
	 * @return the HMM's end state
	 */
	public State getEndState()
	{
		return this.states.getStateById(this.endStateId);
	}
	
	/**
	 * @return the number of states in the HMM
	 */
	public int getNumStates()
	{
		return states.getStates().size();
	}
	
	/**
	 * @return the ID of the begin state
	 */
	public String getBeginStateId()
	{
		return beginStateId;
	}
	
	public void addState(State newState)
	{
		this.states.addState(newState);
	}
	
	/**
	 * @param beginStateId the ID of the begin state
	 */
	public void setBeginStateId(String beginStateId)
	{
		this.beginStateId = beginStateId;
	}
	
	/**
	 * @return the ID of the end state
	 */
	public String getEndStateId()
	{
		return endStateId;
	}
	
	/**
	 * @param endStateId the ID of the end state
	 */
	public void setEndStateId(String endStateId)
	{
		this.endStateId = endStateId;
	}
	
}
