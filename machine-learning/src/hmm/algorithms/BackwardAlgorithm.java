package hmm.algorithms;

import hmm.HMM;
import hmm.State;

import java.util.ArrayList;

import math.LogP;




import pair.Pair;


public class BackwardAlgorithm 
{	
	public static int debug = 0;
	
	public static Pair<Double, DpMatrix> run(HMM model, String[] sequence)
	{
		DpMatrix dpMatrix = new DpMatrix(model, sequence);
	
		/*
		 *  Initialize the matrix
		 */
		initialize(dpMatrix, model);
			
		/*
		 *  Run the algorithm
		 */
		Double finalProb = runIteration(dpMatrix, model, sequence);
				
		return new Pair<Double, DpMatrix>(finalProb, dpMatrix);
	}

	public static double runIteration(DpMatrix dpMatrix, 
									HMM model, 
									String[] sequence)
	{			
		
		for (int t = dpMatrix.getNumColumns() - 2; t >= 0; t--)
		{	
			/*
			 * Compute silent states
			 */
			ArrayList<State> sortedSilent = model.getSortedSilentStates();
			for (int j = sortedSilent.size() - 1; j >= 0; j--)
			{
				State currState = sortedSilent.get(j);
				
				double sum = Double.NaN;
				for (State forwardState : model.getStates())
				{
					double bValue;
					double tProb;
					double eProb = LogP.ln(1.0);
					
					if (forwardState.isSilent()) // Transitions to silent states
					{
						bValue = dpMatrix.getValue(forwardState, t);
					
						tProb  = model.getTransitionProb( currState.getId(),
															 forwardState.getId() 
															  );
					}
					else  // Transitions to non-silent states
					{
						eProb = model.getEmissionProb(forwardState.getId(),
						                              sequence[t]);
						
						bValue = dpMatrix.getValue(forwardState, t+1);
						
						tProb  = model.getTransitionProb( currState.getId(),
															 forwardState.getId() 
															  );
					}
					sum = LogP.sum(sum, LogP.prod( LogP.prod(tProb, eProb), bValue));
				}
				
				// Set the new value in the DP matrix
				dpMatrix.setValue(currState, t, sum);
			}
			
			/*
			 * Compute for non-silent states
			 */
			for (State currState : model.getStates())
			{
				if (!currState.isSilent())
				{
					double sum = Double.NaN;
					for (State forwardState : model.getStates())
					{
						double bValue;
						double tProb;
						double eProb = LogP.ln(1.0);
						
						if (forwardState.isSilent()) // Transitions to silent states
						{
							bValue = dpMatrix.getValue(forwardState, t);
						
							tProb  = model.getTransitionProb( currState.getId(),
															forwardState.getId() 
															 );
						}
						else  // Transitions to non-silent states
						{
							
							eProb = model.getEmissionProb(forwardState.getId(),
							  		                      sequence[t]);
							
							bValue = dpMatrix.getValue(forwardState, t+1);
							
							tProb  = model.getTransitionProb( currState.getId(),
															forwardState.getId() 
																  );
						}
									
						sum = LogP.sum(sum, LogP.prod( LogP.prod(tProb, eProb), bValue));
					}
					
					// Set the new value in the DP matrix
					dpMatrix.setValue(currState, t, sum);
				}
			}
			
			if (debug > 1)
				System.out.println(dpMatrix);
		}
				
		return dpMatrix.getValue(model.getBeginState(), 0);
	}
	
	/**
	 * Initialize the dynamic programming matrix
	 * 
	 * @param dpMatrix the dynamic programming matrix object
	 * @param model the HMM object
	 */
	public static void initialize(DpMatrix dpMatrix, HMM model)
	{
		
		/*
		 *  Set all elements to 0.0
		 */
		for (State state : model.getStateContainer().getStates())
		{
			dpMatrix.setValue(state, 0, Double.NaN);
		}
		
		/*
		 * If no end state, then set the probability at the last time step 
		 * for all states should be 1.0
		 */
		if (model.getEndState() == null)
		{
    		for (State state : model.getStates())
    		{
    			dpMatrix.setValue(state, dpMatrix.getNumColumns() - 1, LogP.ln(1.0));
    		}
		}
		else
		{
    		for (State state : model.getStateContainer().getStates())
    		{
    			if (state.transitionExists(model.getEndStateId()))
    			{				
    				dpMatrix.setValue(state, 
    								  dpMatrix.getNumColumns() - 1, 
    								  model.getTransitionProb(state.getId(), 
    														  model.getEndStateId())); 
    			}
    		}
		}
	}
	
	
}
