package hmm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store the state objects that comprise the Markov 
 * model.
 */
public class StateContainer 
{
	/**
	 * Maps a state ID to a state object
	 */
	Map<String, State> states;
	
	/**
	 * Constructor.
	 */
	public StateContainer()
	{
		states = new HashMap<String, State>();
	}
	
	/**
	 * @return the array list of all of the states in the model
	 */
	public Collection<State> getStates()
	{
		return states.values();
	}
	
	/*
	 * TODO OPTIMIZE THIS!!!!
	 */
	public Collection<State> getSilentStates()
	{
		ArrayList<State> silent = new ArrayList<State>();
		for (State s : states.values())
		{
			if (s.isSilent)
				silent.add(s);
		}
		return silent;
	}
	
	/**
	 * Add a state to this container.
	 * 
	 * @param newState the new state
	 */
	public void addState(State newState)
	{
		states.put(newState.getId(), newState);
	}
	
	/**
	 * Retrieve a state with a specified unique ID from this state container.
	 * 
	 * @param id the unique integer of ID of the state to be retrieved
	 * @return The state that stores the specified ID.  If a state with the 
	 * specified ID does not exist in this container, this method returns
	 * null.
	 */
	public State getStateById(String id)
	{
		return states.get(id);
	}
	
	/**
	 * Determines whether this state container contains a state with the 
	 * specified ID
	 * 
	 * @param id the ID of the state we are querying for
	 * @return true if this StateContainer has a state with specified ID,
	 *  false otherwise
	 */
	public boolean containsState(String id)
	{
		if (states.containsKey(id))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
